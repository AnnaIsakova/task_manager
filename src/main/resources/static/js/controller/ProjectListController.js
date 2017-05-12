'use strict';

app.controller('ProjectListController', ['$scope', '$rootScope', '$state', '$http', 'ProjectService', 'ModalService',
    function($scope, $rootScope, $state, $http, ProjectService, ModalService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.infoProject={};

        fetchAllProjects();

        function fetchAllProjects(){
            ProjectService.fetchAllProjects()
                .then(
                    function(d) {
                        $scope.projects = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
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

        $scope.openEdit = function (task) {
            console.log('open editing: ', task);
            $rootScope.taskForEdit = task;
            ModalService.showModal({
                templateUrl: '/views/editTodoTask.html',
                controller: "EditTodoController"
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
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }


    }]);