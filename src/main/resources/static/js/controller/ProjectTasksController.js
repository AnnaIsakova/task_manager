'use strict';

app.controller('ProjectTasksController', ['$scope', '$rootScope', '$state', '$stateParams', '$http', 'CrudService', 'ModalService', 'TaskService',
    function($scope, $rootScope, $state, $stateParams, $http, CrudService, ModalService, TaskService) {

        $scope.task={};
        $scope.tasks=[];
        // $rootScope.projectInfo={};
        $scope.project = {};
        $scope.header = '';

        $scope.sortType     = 'status'; // set the default sort type
        $scope.sortReverse  = true;  // set the default sort order

        $scope.projID = $stateParams.projectID;
        $scope.filter = $stateParams.filter;

        getHeader();
        fetchAllTasks();
        fetchProject();

        function fetchAllTasks(){
            CrudService.fetchAll('projects/' + $stateParams.projectID + '/tasks')
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
                                if (arr[i].status == 'COMPLETE' && !arr[i].approved){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else if ($stateParams.filter == 'approved'){
                            for(var i=0; i<arr.length; i++){
                                if (arr[i].approved){
                                    $scope.tasks.push(arr[i]);
                                }
                            }
                        } else if ($stateParams.filter == 'all'){
                            $scope.tasks = d;
                        } else {
                            $state.go('home.tasks', {filter: 'all'});
                        }
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchProject(){
            CrudService.fetchOne('projects', $stateParams.projectID)
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
            } else if ($stateParams.filter == 'in_progress'){
                $scope.header = 'Tasks In Progress';
            } else if ($stateParams.filter == 'complete'){
                $scope.header = 'Complete Tasks';
            } else if ($stateParams.filter == 'approved'){
                $scope.header = 'Approved Tasks';
            }
        }

        $scope.showNewTodo = function() {
            ModalService.showModal({
                templateUrl: '/views/newTaskForProject.html',
                controller: "NewTaskController",
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    $state.go('home.tasks', {projectID:$stateParams.projectID, filter: 'all'});
                    fetchAllTasks();
                });
            });
        };

        $scope.changeStatus = function (id, status) {
            TaskService.changeStatus($stateParams.projectID, id, status)
                .then(
                    function(d) {
                        fetchAllTasks();
                    },
                    function(errResponse){
                        console.error('Error while set in progress -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                        $scope.newComment = {};
                    }
                );
        };

        $scope.openEdit = function (task) {
            $rootScope.taskForEdit = task;
            ModalService.showModal({
                templateUrl: '/views/editProjTask.html',
                controller: "EditTaskController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    fetchAllTasks();
                });
            });
        };

        $scope.openDelete = function (task) {
            $rootScope.projectId = $stateParams.projectID;
            $rootScope.taskForDelete = task;
            ModalService.showModal({
                templateUrl: '/views/confirmTaskDelete.html',
                controller: "DeleteController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    if (result === null){
                    } else{
                        $state.go('home.tasks', {'projectID': $stateParams.projectID});
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
            } else if (v1.value.hasOwnProperty('firstName')){
                var name1 = v1.value.firstName + " " + v1.value.lastName;
                var name2 = v2.value.firstName + " " + v2.value.lastName;
                return (name1 > name2) ? 1 : -1;
            } else {
                return (v1.value > v2.value) ? 1 : -1;
            }

        }
    }]);