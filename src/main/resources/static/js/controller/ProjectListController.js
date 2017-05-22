'use strict';

app.controller('ProjectListController', ['$scope', '$rootScope', '$state', '$http', 'CrudService', 'ModalService',
    function($scope, $rootScope, $state, $http, CrudService, ModalService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.projectInfo={};

        fetchAllProjects();

        function fetchAllProjects(){
            CrudService.fetchAll('projects')
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

        $scope.showNewProject = function() {
            ModalService.showModal({
                templateUrl: '/views/newProject.html',
                controller: "NewProjectController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    $state.go('home.projects');
                    fetchAllProjects();
                });
            });
        };

    }]);