'use strict';

app.controller('TodoListController', ['$scope', '$rootScope', '$state', '$http', 'TodoListService',
    function($scope, $rootScope, $state, $http, TodoListService) {
        var self = this;
        $scope.task={};
        $scope.tasks=[];

        fetchAllTasks();

        function fetchAllTasks(){
            TodoListService.fetchAllTasks()
                .then(
                    function(d) {
                        $scope.tasks = d;
                        console.log('tasks:', $scope.tasks);
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = "Oops, error while fetching roles occurred :(\nPlease, try again later!";
                    }
                );
        }

    }]);