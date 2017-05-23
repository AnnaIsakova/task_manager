'use strict';

app.controller('EditTodoController', ['$scope', '$rootScope', 'close', 'CrudService',
    function($scope, $rootScope, close, CrudService) {

        $scope.allStatus = [];
        var oldTask = $rootScope.taskForEdit;
        $scope.task = angular.copy(oldTask);
        $scope.task.deadline = new Date($scope.task.deadline);
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };
        $scope.now = new Date();

        var desc = '';
        var stat = '';
        var dead = {};
        var prior = '';

        fetchAllPriorities();
        fetchAllStatus();
        setTaskBeforeEditing();

        function setTaskBeforeEditing() {
            desc = $rootScope.taskForEdit.description;
            stat = $rootScope.taskForEdit.status;
            dead = $rootScope.taskForEdit.deadline;
            prior = $rootScope.taskForEdit.priority;
        }

        function fetchAllPriorities(){
            CrudService.fetchAll('priorities')
                .then(
                    function(d) {
                        $scope.priorities = d;
                        console.log($scope.task.deadline - new Date());
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function fetchAllStatus(){
            CrudService.fetchAll('status')
                .then(
                    function(d) {
                        $scope.allStatus = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching status -> from controller');
                        $scope.errorMessage = "Oops, error while fetching status occurred :(\nPlease, try again later!";
                    }
                );
        }

        $scope.edit = function (task) {
            console.log('task for editing: ', task);
            CrudService.updateObj('todo', task)
                .then(
                    function(d) {
                        $scope.tasks = d;
                        close(task, 500);
                        $('#editTodoTaskModal').modal('hide');
                    },
                    function(errResponse){
                        console.error('Error while editing task -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };


        $scope.close = function (){
            close(null, 500);
            $('#editTodoTaskModal').modal('hide');
        }

    }]);