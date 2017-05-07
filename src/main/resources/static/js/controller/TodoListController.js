'use strict';

app.controller('TodoListController', ['$scope', '$rootScope', '$state', '$http', 'TodoListService', 'ModalService',
    function($scope, $rootScope, $state, $http, TodoListService, ModalService) {
        var self = this;
        $scope.task={};
        $scope.tasks=[];

        fetchAllTasks();

        function fetchAllTasks(){
            TodoListService.fetchAllTasks()
                .then(
                    function(d) {
                        $scope.tasks = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = "Oops, error while fetching tasks occurred :(\nPlease, try again later!";
                    }
                );
        }

        $scope.showNewTodo = function() {
            ModalService.showModal({
                templateUrl: '/views/newTodoTask.html',
                controller: "NewTodoController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    $state.go('dashboard.todo');
                    fetchAllTasks();
                });
            });
        };

        $scope.delete = function (id) {
            console.log(id);
            TodoListService.deleteTask(id)
                .then(
                    function(d) {
                        console.log("Task was deleted", d);
                        fetchAllTasks();
                    },
                    function(errResponse){
                        console.error('Error while deleting tasks -> from controller');
                        $scope.errorMessage = "Oops, error while deleting task occurred :(\nPlease, try again later!";
                    }
                );
        }

    }]);