'use strict';

app.controller('NewTaskController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'close', 'CrudService', 'UserService',
    function($scope, $rootScope, $state, $http, $stateParams, close, CrudService, UserService) {

        $scope.task = {description:'', details:'', assignedTo:null, priority:'', deadline:new Date()};
        $scope.priorities = [];
        $scope.developers = [];
        var teamlead = JSON.parse(UserService.getCookieUser());
        var myId = teamlead.principal.id;
        $scope.me = {};
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };


        fetchAllPriorities();
        fetchAllDevelopers();

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

        function fetchAllDevelopers(){
            CrudService.fetchAll('projects/' + $stateParams.projectID + '/devs')
                .then(
                    function(d) {
                        $scope.developers = d;
                        fetchMe();
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        function fetchMe() {
            CrudService.fetchOne('users', myId)
                .then(
                    function(d) {
                        $scope.me = d;

                        if($scope.developers == ''){
                            $scope.developers = [];
                        }
                        $scope.developers.push($scope.me);

                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }
        function createTask(task){
            CrudService.createObj('projects/' + $stateParams.projectID + '/tasks', task)
                .then(
                    function (d) {
                        close(task, 500);
                        $('#newProjectTaskModal').modal('hide');
                    },
                    function(errResponse){
                        console.error('Error while creating task');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            console.log('result-> ', result);
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            console.log(result.assignedTo);
            createTask(result);
        };

        $scope.close = function (){
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            close(null, 500);
            $('#newProjectTaskModal').modal('hide');
        }

    }]);