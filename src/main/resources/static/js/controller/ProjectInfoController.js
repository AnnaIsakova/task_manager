'use strict';

app.controller('ProjectInfoController', ['$scope', '$rootScope', '$state', '$http', '$stateParams', 'moment', 'ProjectService', 'ModalService',
    function($scope, $rootScope, $state, $http, $stateParams, moment, ProjectService, ModalService) {

        $scope.id = 0;
        $scope.project = {};
        $scope.progressDeadline = 0;
        $scope.newDeveloper = '';

        fetchProject();

        function fetchProject(){
            $scope.id = $stateParams.projectID;
            ProjectService.fetchProject($scope.id)
                .then(
                    function(d) {
                        $scope.project = d;
                        getDeadlineProgress();
                    },
                    function(errResponse){
                        console.error('Error while fetching project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.openEdit = function (project) {
            console.log('open editing: ', project);
            $rootScope.projectForEdit = project;
            ModalService.showModal({
                templateUrl: '/views/editProject.html',
                controller: "EditProjectController"
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(function(result) {
                    // $state.go('home.projects', {projectID: project.id});
                    // fetchAllTasks();
                });
            });
        };
        
        $scope.addDeveloper = function (email) {
            ProjectService.addDeveloper($scope.id, email)
                .then(
                    function(d) {
                        fetchProject();
                    },
                    function(errResponse){
                        console.error('Error while adding developer -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        function getDeadlineProgress(){
            var now = moment().startOf('day');
            var deadline = moment($scope.project.deadline).startOf('day');
            var createdAt = moment($scope.project.createDate).startOf('day');

            var diffPass = now.diff(createdAt);
            var durationPass = moment.duration(diffPass);
            var daysPass = durationPass.days();

            var diffTotal = deadline.diff(createdAt);
            var durationTotal = moment.duration(diffTotal);
            var daysTotal = durationTotal.days();

            if (daysTotal >= daysPass){
                $scope.progressDeadline = 100;
            } else {
                $scope.progressDeadline = (100 * daysPass)/daysTotal;
            }

        }


    }]);