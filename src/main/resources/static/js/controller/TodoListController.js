'use strict';

app.controller('TodoListController', ['$scope', '$rootScope', '$state', '$http', 'TodoListService', 'ModalService',
    function($scope, $rootScope, $state, $http, TodoListService, ModalService) {

        $scope.task={};
        $scope.tasks=[];
        $rootScope.taskForEdit={};

        $scope.sortType     = 'status'; // set the default sort type
        $scope.sortReverse  = true;  // set the default sort order

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
                    $state.go('home.todo');
                    fetchAllTasks();
                });
            });
        };

        $scope.openEdit = function (task) {
            console.log('open editing: ', task);
            $rootScope.taskForEdit = task;
            ModalService.showModal({
                templateUrl: '/views/editTodoTask.html',
                controller: "EditTodoController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    $state.go('home.todo');
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
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.prioritiesComparator = function(v1, v2) {
            // If we don't get strings, just compare by index
            if (v1.value == 'LOW' && v2.value == 'NORMAL') {
                return  -1;
            } else if (v1.value == 'NORMAL' && v2.value == 'LOW') {
                return 1;
            } else if (v1.value == 'LOW' && v2.value == 'HIGH'){
                return -1;
            } else if (v1.value == 'HIGH' && v2.value == 'LOW'){
                return 1;
            } else if (v1.value == 'NORMAL' && v2.value == 'HIGH'){
                return -1;
            } else if (v1.value == 'HIGH' && v2.value == 'NORMAL'){
                return 1;
            } else {
                return (v1.value > v2.value) ? 1 : -1;
            }

        }

    }]);