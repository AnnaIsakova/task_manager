'use strict';
app.controller('DevController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'CrudService', 'UserService',

    function($scope, $rootScope, $state, $http, $stateParams, CrudService, UserService) {

        $scope.developers = [];
        var teamlead = JSON.parse(UserService.getCookieUser());
        var myId = teamlead.principal.id;
        $scope.me = {};
        $scope.project = {};
        var x = 0;

        fetchProject();

        function fetchProject(){
            $scope.id = $stateParams.projectID;
            CrudService.fetchOne('projects', $scope.id)
                .then(
                    function(d) {
                        $scope.project = d;
                        fetchAllDevelopers();
                    },
                    function(errResponse){
                        console.error('Error while fetching project -> from controller');
                        $scope.errorProject = errResponse.data.message;
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
                        $scope.developers.push($scope.me);
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        $scope.getTasks = function(dev, status) {
            var count = 0;
            for(var i=0;i<$scope.project.tasks.length;i++){
                if ($scope.project.tasks[i].assignedTo == null){
                    continue;
                }
                if ($scope.project.tasks[i].status == status && !$scope.project.tasks[i].approved && $scope.project.tasks[i].assignedTo.email == dev.email){
                    count++;
                }
            }
            return count;
        };

        $scope.getApproved = function(dev) {
            var count = 0;
            for(var i=0;i<$scope.project.tasks.length;i++){
                if ($scope.project.tasks[i].assignedTo == null){
                    continue;
                }
                if ($scope.project.tasks[i].approved && $scope.project.tasks[i].assignedTo.email == dev.email){
                    count++;
                }
            }
            return count;
        }

    }]);