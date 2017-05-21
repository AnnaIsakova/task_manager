'use strict';

app.controller('NewTaskController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'close', 'ProjectService', 'TodoListService', 'ModalService',
    function($scope, $rootScope, $state, $http, $stateParams, close, ProjectService, TodoListService, ModalService) {

        $scope.task = {description:'', details:'', assignedTo:{}, priority:'', deadline:new Date()};
        $scope.priorities = [];
        $scope.developers = [];
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };

        fetchAllPriorities();
        fetchAllDevelopers();

        function fetchAllPriorities(){
            TodoListService.fetchAllPriorities()
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

        function fetchAllDevelopers(){
            ProjectService.fetchAllDevelopers($stateParams.projectID)
                .then(
                    function(d) {
                        $scope.developers = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function createTask(task){
            ProjectService.addTask($stateParams.projectID, task)
                .then(
                    function (d) {
                        close(task, 500);
                        $('#newProjectTaskModal').modal('hide');
                    },
                    function(errResponse){
                        console.error('Error while creating task');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            console.log('result-> ', result);
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            createTask(result);
        };

    }]);