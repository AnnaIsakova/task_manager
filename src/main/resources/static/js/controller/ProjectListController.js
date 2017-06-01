'use strict';

app.controller('ProjectListController', ['$scope', '$rootScope', '$state', '$http', 'CrudService', 'ModalService', 'UserService',
    function($scope, $rootScope, $state, $http, CrudService, ModalService, UserService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.projectInfo={};
        var user = JSON.parse(UserService.getCookieUser());
        var projects = [];
        var devs = [];
        var tasks = [];


        fetchAllProjects();

        function fetchAllProjects(){
            CrudService.fetchAll('projects')
                .then(
                    function(d) {
                        projects = d;
                        console.log("proj d: ", projects)
                        for (var i=0; i<projects.length; i++){
                            fetchAllTasks(projects[i]);
                            if (user.authorities[0].authority == "TEAMLEAD"){
                                fetchAllDevs(projects[i]);
                            }
                        }
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }
        
        function fetchAllTasks(project){
            CrudService.fetchAll('projects/' + project.id + '/tasks')
                .then(
                    function(d) {
                        tasks = d;
                        project.tasks = tasks;
                        $scope.projects.push(project);
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchAllDevs(project){
            CrudService.fetchAll('projects/' + project.id + '/devs')
                .then(
                    function(d) {
                        project.developers = d;
                        changeProj(project.id, project);
                        console.log("projects: ", $scope.projects);
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function changeProj( id, project) {
            for (var i in $scope.projects) {
                if ($scope.projects[i].id == id) {
                    $scope.projects[i] = project;
                    break; //Stop this loop, we found it!
                }
            }
        }

        $scope.showNewProject = function() {
            ModalService.showModal({
                templateUrl: '/views/newProject.html',
                controller: "NewProjectController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(result) {
                    $state.go('home.projects');
                    fetchAllProjects();
                });
            });
        };


        $scope.getApproved = function(project) {
            var count = 0;
            for(var i=0;i<project.tasks.length;i++){
                if (project.tasks[i].approved){
                    count++;
                }
            }
            return count;
        };

        $scope.getNew = function(project) {
            var count = 0;
            for(var i=0;i<project.tasks.length;i++){
                if (project.tasks[i].status == "NEW"){
                    count++;
                }
            }
            return count;
        };
        
        $scope.getMy = function (project) {
            var count = 0;
            for(var i=0;i<project.tasks.length;i++){
                if (project.tasks[i].assignedTo.email == user.name){
                    count++;
                }
            }
            return count;
        }

    }]);