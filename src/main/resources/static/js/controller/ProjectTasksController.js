'use strict';

app.controller('ProjectTasksController', ['$scope', '$rootScope', '$state', '$stateParams', '$http', 'ProjectService', 'ModalService',
    function($scope, $rootScope, $state, $stateParams, $http, ProjectService, ModalService) {

        $scope.task={};
        $scope.tasks=[];
        // $rootScope.projectInfo={};
        $scope.project = {};
        $scope.header = '';

        getHeader();
        fetchAllTasks();
        fetchProject();

        function fetchAllTasks(){
            ProjectService.fetchAllTasks($stateParams.projectID)
                .then(
                    function(d) {
                        var arr = [];
                        arr = d;
                        if ($stateParams.filter == 'new'){
                            for(var i=0; i<arr.length; i++){
                                if (arr[i].status == 'NEW'){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else if ($stateParams.filter == 'in_progress'){
                            for(var i=0; i<arr.length; i++){
                                if (arr[i].status == 'IN_PROGRESS'){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else if ($stateParams.filter == 'complete'){
                            for(var i=0; i<arr.length; i++){
                                if (arr[i].status == 'COMPLETE'){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else if ($stateParams.filter == 'approved'){
                            for(var i=0; i<arr.length; i++){
                                if (arr[i].status == 'APPROVED'){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else {
                            $scope.tasks = d;
                        }
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchProject(){
            ProjectService.fetchProject($stateParams.projectID)
                .then(
                    function(d) {
                        $scope.project = d;

                    },
                    function(errResponse){
                        console.error('Error while fetching project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function getHeader(){
            if ($stateParams.filter == 'all'){
                $scope.header = 'All Tasks';
            } else if ($stateParams.filter == 'new'){
                $scope.header = 'New Tasks';
            }
        }

        $scope.showNewTodo = function() {
            ModalService.showModal({
                templateUrl: '/views/newTaskForProject.html',
                controller: "NewTaskController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    $state.go('home.tasks');
                    fetchAllTasks();
                });
            });
        };
    }]);