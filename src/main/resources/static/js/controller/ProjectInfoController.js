'use strict';

app.controller('ProjectInfoController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'moment', 'CrudService', 'FileService', 'ModalService', 'UserService',
    function($scope, $rootScope, $state, $http, $stateParams, moment, CrudService, FileService, ModalService, UserService) {

        $scope.id = 0;
        $scope.project = {};
        $scope.progressDeadline = 0;
        $scope.newDeveloper = '';
        $scope.newComment = {text:''};
        $scope.confirmMessage = 'hello';
        $scope.user = JSON.parse(UserService.getCookieUser());
        $scope.comment = {};
        $scope.tasksCompleted = 0;
        $scope.taskProgress = 0;

        $scope.comment.editingComment = false;
        $scope.toggleEditingComment = function(comment) {
            comment.editingComment = !comment.editingComment;
        };

        $scope.commentForm = false;
        $scope.toggleCommentForm = function() {
            $scope.commentForm = !$scope.commentForm;
            console.log($scope.commentForm)
        };


        fetchProject();

        function fetchProject(){
            $scope.id = $stateParams.projectID;
            CrudService.fetchOne('projects', $scope.id)
                .then(
                    function(d) {
                        $scope.project = d;
                        getDeadlineProgress();
                        getTaskCompleted();
                        getTaskCompletedProgress();
                    },
                    function(errResponse){
                        console.error('Error while fetching project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchFiles(){
            $scope.id = $stateParams.projectID;
            CrudService.fetchAll('projects/' + $scope.id + '/files')
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
            CrudService.fetchAll('projects/' + $scope.id + '/comments')
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
            CrudService.fetchAll('projects/' + $scope.id + '/devs')
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
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    fetchProject();
                    // fetchAllTasks();
                });
            });
        };

        $scope.openDelete = function () {
            $rootScope.projectId = $scope.id;
            ModalService.showModal({
                templateUrl: '/views/confirmProjectDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        $state.go('home.projects');
                    }
                });
            });
        };

        $scope.addDeveloper = function (email) {
            console.log(email)
            CrudService.createObj('projects/' + $scope.id + '/devs', email)
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
        
        $scope.deleteDeveloper = function (dev) {
            $rootScope.dev = dev;
            $rootScope.projectId = $scope.id;
            ModalService.showModal({
                templateUrl: '/views/confirmDevDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        fetchDevs();
                    }
                });
            });
        };

        $scope.uploadFile = function(){
            var file = $rootScope.file;

            console.log('file is ', file);
            console.dir(file);

            FileService.uploadFile('projects/' + $scope.id + '/files', file)
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
            FileService.downloadFile('projects/' + $scope.id + '/files', fileId)
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

        $scope.deleteFile = function (file) {
            $rootScope.file = file;
            $rootScope.projectId = $scope.id;
            ModalService.showModal({
                templateUrl: '/views/confirmFilePrDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        fetchFiles();
                    }
                });
            });
        };

        $scope.deleteComment = function (comment) {
            $rootScope.comment = comment;
            $rootScope.projectId = $scope.id;
            ModalService.showModal({
                templateUrl: '/views/confirmCommentPrDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        fetchAllComments();
                    }
                });
            });
        };

        $scope.addComment = function (comment) {
            CrudService.createObj('projects/' + $scope.id + '/comments', comment)
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
            CrudService.updateObj('projects/' + $scope.id + '/comments', comment)
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

        function getTaskCompleted(){
            for (var i=0; i<$scope.project.tasks.length; i++){
                if ($scope.project.tasks[i].approved){
                    $scope.tasksCompleted++;
                }
            }
        }
        
        function getTaskCompletedProgress() {
            var tasks = $scope.project.tasks.length;
            var completed = $scope.tasksCompleted;
            $scope.taskProgress = (100 * completed) / tasks;
            console.log($scope.taskProgress);
        }
        
    }]);