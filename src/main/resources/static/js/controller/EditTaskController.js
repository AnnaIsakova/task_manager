'use strict';

app.controller('EditTaskController', ['$scope', '$rootScope', '$stateParams', 'close', 'CrudService', 'UserService',
    function($scope, $rootScope, $stateParams, close, CrudService, UserService) {

        var oldTask = $rootScope.taskForEdit;
        $scope.task = angular.copy(oldTask);
        $scope.task.deadline = new Date($scope.task.deadline);
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };
        $scope.now = new Date();
        $scope.invalidDate = false;
        $scope.priorities = [];
        $scope.allStatus = [];
        $scope.developers = [];
        var teamlead = JSON.parse(UserService.getCookieUser());
        var myId = teamlead.principal.id;

        isInvalid();
        fetchAllPriorities();
        fetchAllStatus();
        fetchAllDevelopers();


        function isInvalid () {
            var today = moment().startOf('day');
            var deadline = moment($scope.task.deadline).startOf('day');
            if(today.isAfter(deadline)){
                $scope.invalidDate = true;
            }
        }

        $scope.isDateInvalid = function (dead) {
            var today = moment().startOf('day');
            var deadline = moment(dead).startOf('day');
            if(today.isAfter(deadline)){
                $scope.invalidDate = true;
            } else {
                $scope.invalidDate = false;
            }
            console.log($scope.invalidDate)
        };

        function fetchAllStatus(){
            CrudService.fetchAll('status')
                .then(
                    function(d) {
                        $scope.allStatus = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching status -> from controller');
                        $scope.errorMessage = "Oops, error while fetching status occurred :(\nPlease, try again later!";
                    }
                );
        }

        function fetchAllDevelopers(){
            CrudService.fetchAll('projects/' + $stateParams.projectID + '/devs')
                .then(
                    function(d) {
                        $scope.developers = d;
                        fetchMe();
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function fetchMe() {
            CrudService.fetchOne('users', myId)
                .then(
                    function(d) {
                        $scope.me = d;
                        if($scope.developers == ''){
                            $scope.developers = [];
                        }
                        $scope.developers.push($scope.me);
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function fetchAllPriorities(){
            CrudService.fetchAll('priorities')
                .then(
                    function(d) {
                        $scope.priorities = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function editTask (task) {
            console.log('task for editing: ', task);
            CrudService.updateObj('projects/' + $stateParams.projectID + '/tasks', task)
                .then(
                    function(d) {
                        $scope.task = d;
                        $('#editProjectTaskModal').modal('hide');
                        close(task, 500);
                    },
                    function(errResponse){
                        console.error('Error while editing project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            editTask(result);
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
        };

        $scope.close = function (){
            $('#editProjectTaskModal').modal('hide');
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            close(null, 500);
        }

    }]);