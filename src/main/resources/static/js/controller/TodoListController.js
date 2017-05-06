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
                        $scope.errorMessage = "Oops, error while fetching roles occurred :(\nPlease, try again later!";
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
                    // $state.go('dashboard.projects');
                });
            });
        };

    }]);