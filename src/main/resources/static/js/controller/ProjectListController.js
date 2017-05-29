'use strict';

app.controller('ProjectListController', ['$scope', '$rootScope', '$state', '$http', 'CrudService', 'ModalService', 'UserService',
    function($scope, $rootScope, $state, $http, CrudService, ModalService, UserService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.projectInfo={};
        $scope.user = JSON.parse(UserService.getCookieUser());

        fetchAllProjects();

        function fetchAllProjects(){
            CrudService.fetchAll('projects')
                .then(
                    function(d) {
                        $scope.projects = d;
                        console.log($scope.projects)
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
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
                if (project.tasks[i].assignedTo == null){
                    continue;
                }
                if (project.tasks[i].approved && project.tasks[i].assignedTo.email == $scope.user.name){
                    count++;
                }
            }
            return count;
        }

        $scope.getNew = function(project) {
            var count = 0;
            for(var i=0;i<project.tasks.length;i++){
                if (project.tasks[i].assignedTo == null){
                    continue;
                }
                if (project.tasks[i].status == "NEW" && project.tasks[i].assignedTo.email == $scope.user.name){
                    count++;
                }
            }
            return count;
        }
        
        $scope.getMy = function (project) {
            var count = 0;
            for(var i=0;i<project.tasks.length;i++){
                if (project.tasks[i].assignedTo == null){
                    continue;
                }
                if (project.tasks[i].assignedTo.email == $scope.user.name){
                    count++;
                }
            }
            return count;
        }

    }]);