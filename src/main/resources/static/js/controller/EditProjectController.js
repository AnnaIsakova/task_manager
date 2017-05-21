'use strict';

app.controller('EditProjectController', ['$scope', '$rootScope', 'close', 'ProjectService', 
    function($scope, $rootScope, close, ProjectService) {

        var oldProject = $rootScope.projectForEdit;
        $scope.project = angular.copy(oldProject);
        $scope.project.deadline = new Date($scope.project.deadline);
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };
        $scope.now = new Date();
        $scope.invalidDate = false;

        isInvalid();
        
        function isInvalid () {
            var today = moment().startOf('day');
            var deadline = moment($scope.project.deadline).startOf('day');
            if(today.isAfter(deadline)){
                $scope.invalidDate = true;
            }
        }

        $scope.isDateInvalid = function (dead) {
            var today = moment().startOf('day');
            var deadline = moment(dead).startOf('day');
            if(today.isAfter(deadline)){
                $scope.invalidDate = true;
            } else {
                $scope.invalidDate = false;
            }
            console.log($scope.invalidDate)
        }


        function editProject (project) {
            console.log('project for editing: ', project);
            ProjectService.editProject(project)
                .then(
                    function(d) {
                        $scope.project = d;
                        $('#editProjectModal').modal('hide');
                        close(project, 500);
                    },
                    function(errResponse){
                        console.error('Error while editing project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            editProject(result);
        };

        $scope.rollback = function () {
            // $scope.task.status = stat;
            // $scope.task.description = desc;
            // $scope.task.priority = prior;
            // $scope.task.deadline = dead;
        }

    }]);