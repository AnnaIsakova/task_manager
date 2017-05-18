'use strict';

app.controller('ProjectInfoController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'moment', 'ProjectService', 'ModalService', 'UserService',
    function($scope, $rootScope, $state, $http, $stateParams, moment, ProjectService, ModalService, UserService) {

        $scope.id = 0;
        $scope.project = {};
        $scope.progressDeadline = 0;
        $scope.newDeveloper = '';
        $scope.newComment = {text:''};
        $scope.confirmMessage = 'hello';
        $scope.user = JSON.parse(UserService.getCookieUser());
        $scope.comment = {};
        $scope.comment.editingComment = false;
        $scope.toggleEditingComment = function(comment) {
            comment.editingComment = !comment.editingComment;
        };

        $scope.commentForm = false;
        $scope.toggleCommentForm = function() {
            $scope.commentForm = !$scope.commentForm;
        };

        fetchProject();

        function fetchProject(){
            $scope.id = $stateParams.projectID;
            ProjectService.fetchProject($scope.id)
                .then(
                    function(d) {
                        $scope.project = d;
                        getDeadlineProgress();
                        console.log($scope.user);

                    },
                    function(errResponse){
                        console.error('Error while fetching project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchFiles(){
            $scope.id = $stateParams.projectID;
            ProjectService.fetchAllFiles($scope.id)
                .then(
                    function(d) {
                        $scope.project.files = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching files -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchAllComments(){
            $scope.id = $stateParams.projectID;
            ProjectService.fetchAllComments($scope.id)
                .then(
                    function(d) {
                        $scope.project.comments = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching comments -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchDevs(){
            $scope.id = $stateParams.projectID;
            ProjectService.fetchAllDevelopers($scope.id)
                .then(
                    function(d) {
                        $scope.project.developers = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching devs -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.openEdit = function (project) {
            console.log('open editing: ', project);
            $rootScope.projectForEdit = project;
            ModalService.showModal({
                templateUrl: '/views/editProject.html',
                controller: "EditProjectController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    fetchProject();
                    // fetchAllTasks();
                });
            });
        };
        
        
        $scope.addDeveloper = function (email) {
            ProjectService.addDeveloper($scope.id, email)
                .then(
                    function(d) {
                        fetchDevs();
                        $scope.newDeveloper = '';
                    },
                    function(errResponse){
                        console.error('Error while adding developer -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                        $scope.newDeveloper = '';
                    }
                );
        };
        
        $scope.deleteDeveloper = function (devId) {
            ProjectService.deleteDeveloper($scope.id, devId)
                .then(
                    function(d) {
                        fetchDevs();
                    },
                    function(errResponse){
                        console.error('Error while adding developer -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.uploadFile = function(){
            var file = $rootScope.file;

            console.log('file is ', file);
            console.dir(file);

            ProjectService.uploadFile($scope.id, file)
                .then(
                    function(d) {
                        fetchFiles();
                    },
                    function(errResponse){
                        console.error('Error while uploading file -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };
        
        $scope.getFileExt = function (fileName) {
            return fileName.substr(fileName.lastIndexOf('.')+1);
        };
        
        $scope.downloadFile = function (fileId) {
            ProjectService.downloadFile($scope.id, fileId)
                .then(
                    function(d) {
                    },
                    function(errResponse){
                        console.error('Error while downloading file -> from controller');
                        var decodedString = String.fromCharCode.apply(null, new Uint8Array(errResponse.data));
                        var obj = JSON.parse(decodedString);
                        $scope.errorMessage = obj['message'];
                    }
                );
        };

        $scope.deleteFile = function (fileId) {
            ProjectService.deleteFile($scope.id, fileId)
                .then(
                    function(d) {
                        fetchFiles();
                    },
                    function(errResponse){
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.deleteComment = function (commentId) {
            ProjectService.deleteComment($scope.id, commentId)
                .then(
                    function(d) {
                        fetchAllComments();
                    },
                    function(errResponse){
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.addComment = function (comment) {
            ProjectService.addComment($scope.id, comment)
                .then(
                    function(d) {
                        fetchAllComments();
                        $scope.newComment = {};
                        $scope.commentForm = !$scope.commentForm;
                    },
                    function(errResponse){
                        console.error('Error while adding developer -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                        $scope.newComment = {};
                    }
                );
        };

        $scope.editComment = function (comment) {
            ProjectService.editComment($scope.id, comment)
                .then(
                    function(d) {
                        fetchAllComments();
                        comment.editingComment = !comment.editingComment;
                        console.log("new text: ", comment.text)
                    },
                    function(errResponse){
                        console.error('Error while adding developer -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                        $scope.newComment = {};
                    }
                );
        };

        function getDeadlineProgress(){
            var now = moment().startOf('day');
            var deadline = moment($scope.project.deadline).startOf('day');
            var createdAt = moment($scope.project.createDate).startOf('day');

            var diffPass = now.diff(createdAt);
            var durationPass = moment.duration(diffPass);
            var daysPass = durationPass.days();

            var diffTotal = deadline.diff(createdAt);
            var durationTotal = moment.duration(diffTotal);
            var daysTotal = durationTotal.days();

            if (daysTotal <= daysPass){
                $scope.progressDeadline = 100;
            } else if(daysTotal == 1 && now.isSame(createdAt)){
                $scope.progressDeadline = 50;
            }else {
                $scope.progressDeadline = (100 * daysPass)/daysTotal;
            }

        }
        
    }]);