'use strict';

app.controller('NewTodoController', ['$scope', '$rootScope', '$state', '$http', 'TodoListService', 'ModalService',
    function($scope, $rootScope, $state, $http, TodoListService, ModalService) {

        $scope.task = {description:'', priority:'', deadline:new Date()};
        $scope.priorities = [];
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-ddTHH:mm:ss"]
        };

        fetchAllPriorities();

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

        function createTask(task){
            TodoListService.createTask(task)
                .then(
                    function (d) {
                        // close(user, 500);
                        // $('#signUpModal').modal('hide');
                    },
                    function(errResponse){
                        console.error('Error while creating User');
                        $scope.errorMessage = "Oops, something went wrong :(\nPlease, try again!";
                    }
                );
        }

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            console.log('deadline-> ', result.deadline);
            createTask(result);
        };
        
        function validateDate(deadline){
            var today = new Date;
            var result = deadline - today;
            if (result > 0){
                return true;
            } else {
                return false;
            }
        }
    }]);