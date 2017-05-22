'use strict';

app.controller('NewTodoController', ['$scope', '$rootScope', '$state', '$http', 'close', 'CrudService',
    function($scope, $rootScope, $state, $http, close, CrudService) {

        $scope.task = {description:'', priority:'', deadline:new Date()};
        $scope.priorities = [];
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };

        fetchAllPriorities();

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

        function createTask(task){
            CrudService.createObj('todo', task)
                .then(
                    function (d) {
                        close(task, 500);
                        $('#newTodoTaskModal').modal('hide');
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

    }]);