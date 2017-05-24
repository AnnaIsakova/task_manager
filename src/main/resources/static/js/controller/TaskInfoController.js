'use strict';

app.controller('TaskInfoController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'moment', 'CrudService', 'FileService', 'ModalService', 'UserService',
    function($scope, $rootScope, $state, $http, $stateParams, moment, CrudService, FileService, ModalService, UserService) {

        $scope.prId = 0;
        $scope.taskId = 0;
        $scope.task = {};
        $scope.progressDeadline = 0;
        $scope.newComment = {text:''};
        // $scope.user = JSON.parse(UserService.getCookieUser());
        $scope.comment = {};
        // $scope.tasksCompleted = 0;
        // $scope.taskProgress = 0;
        $scope.myFile = {};

        $scope.comment.editingComment = false;
        $scope.toggleEditingComment = function(comment) {
            comment.editingComment = !comment.editingComment;
        };

        $scope.commentForm = false;
        $scope.toggleCommentForm = function() {
            $scope.commentForm = !$scope.commentForm;
            console.log($scope.commentForm)
        };


        fetchTask();

        function fetchTask(){
            $scope.prId = $stateParams.projectID;
            $scope.taskId = $stateParams.taskID;
            CrudService.fetchOne('projects/' + $scope.prId + '/tasks', $scope.taskId)
                .then(
                    function(d) {
                        $scope.task = d;
                        getDeadlineProgress();
                        // getTaskCompleted();
                        // getTaskCompletedProgress();
                    },
                    function(errResponse){
                        console.error('Error while fetching task -> from controller');
                        $scope.errorProject = errResponse.data.message;
                    }
                );
        }

        function fetchFiles(){
            $scope.prId = $stateParams.projectID;
            $scope.taskId = $stateParams.taskID;
            CrudService.fetchAll('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/files')
                .then(
                    function(d) {
                        $scope.task.files = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching files -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchAllComments(){
            $scope.prId = $stateParams.projectID;
            $scope.taskId = $stateParams.taskID;
            CrudService.fetchAll('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/comments')
                .then(
                    function(d) {
                        $scope.task.comments = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching comments -> from controller');
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

        $scope.uploadFile = function(){
            var file = $rootScope.file;

            console.log('file is ', file);
            console.dir(file);

            FileService.uploadFile('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/files', file)
                .then(
                    function(d) {
                        // $scope.myFile = null;
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
            FileService.downloadFile('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/files', fileId)
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
            CrudService.createObj('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/comments', comment)
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
            CrudService.updateObj('projects/' + $scope.prId + '/tasks/' + $scope.taskId + '/comments', comment)
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
            var deadline = moment($scope.task.deadline).startOf('day');
            var createdAt = moment($scope.task.createDate).startOf('day');

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