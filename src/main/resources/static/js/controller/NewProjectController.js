'use strict';

app.controller('NewProjectController', ['$scope', '$rootScope', '$state', '$http', 'close', 'CrudService',
    function($scope, $rootScope, $state, $http, close, CrudService) {

        $scope.project = {name:'', description:'', details:'', deadline:new Date()};
        $scope.priorities = [];
        $scope.datePickerOptions = {
            min: new Date(),
            parseFormats: ["yyyy-MM-dd"]
        };

        $scope.options = {
            language: 'en',
            allowedContent: true,
            entities: false
        };

        $scope.onReady = function () {
            // ...
        };


        function createProject(project){
            CrudService.createObj('projects', project)
                .then(
                    function (d) {
                        close(project, 500);
                        $('#newProjectModal').modal('hide');
                    },
                    function(errResponse){
                        console.error('Error while creating project');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.submit = function (result) {
            console.log(angular.toJson(result));
            console.log('deadline-> ', result.deadline);
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            createProject(result);
        };

        $scope.close = function (){
            var name;
            for (name in CKEDITOR.instances) {
                CKEDITOR.instances[name].destroy(true);
            }
            close(null, 500);
            $('#newProjectModal').modal('hide');
        }

    }]);