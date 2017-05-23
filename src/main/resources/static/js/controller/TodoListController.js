'use strict';

app.controller('TodoListController', ['$scope', '$rootScope', '$state', '$http', 'CrudService', 'ModalService',
    function($scope, $rootScope, $state, $http, CrudService, ModalService) {

        $scope.task={};
        $scope.tasks=[];
        $rootScope.taskForEdit={};

        $scope.sortType     = 'status'; // set the default sort type
        $scope.sortReverse  = true;  // set the default sort order

        fetchAllTasks();

        function fetchAllTasks(){
            CrudService.fetchAll('todo')
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
                modal.element.modal({backdrop: 'static'});
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
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    $state.go('home.todo');
                    fetchAllTasks();
                });
            });
        };

        $scope.openDelete = function (id) {
            console.log('open deleting: ', id);
            $rootScope.taskId = id;
            ModalService.showModal({
                templateUrl: '/views/confirmTodoDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        $state.go('home.todo');
                        fetchAllTasks();
                    }
                });
            });
        };

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