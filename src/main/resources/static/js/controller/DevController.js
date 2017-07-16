'use strict';
app.controller('DevController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'CrudService', 'UserService',

    function($scope, $rootScope, $state, $http, $stateParams, CrudService, UserService) {

        $scope.developers = [];
        var teamlead = JSON.parse(UserService.getCookieUser());
        var myId = teamlead.principal.id;
        $scope.me = {};
        $scope.project = {};
        var x = 0;
        var tasks = [];

        fetchAllDevelopers();
        fetchAllTasks();

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

        function fetchAllTasks(){
            CrudService.fetchAll('projects/' + $stateParams.projectID + '/tasks')
                .then(
                    function(d) {
                        tasks = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.getTasks = function(dev, status) {
            var count = 0;
            for(var i=0;i<tasks.length;i++){
                if (tasks[i].assignedTo == null){
                    continue;
                }
                if (tasks[i].status == status && !tasks[i].approved && tasks[i].assignedTo.email == dev.email){
                    count++;
                }
            }
            return count;
        };

        $scope.getApproved = function(dev) {
            var count = 0;
            for(var i=0;i<tasks.length;i++){
                if (tasks[i].assignedTo == null){
                    continue;
                }
                if (tasks[i].approved && tasks[i].assignedTo.email == dev.email){
                    count++;
                }
            }
            return count;
        }

    }]);