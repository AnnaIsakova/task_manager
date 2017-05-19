'use strict';

app.controller('DeleteProjectController', ['$scope', '$rootScope', 'close', 'ProjectService',
    function($scope, $rootScope, close, ProjectService) {

        $scope.delete = function () {
            console.log('deleting: ', $rootScope.projectId);

            ProjectService.deleteProject($rootScope.projectId)
                .then(
                    function(d) {
                        $('#deletingProjectModal').modal('hide');
                        close(null, 500);
                    },
                    function(errResponse){
                        console.error('Error while deleting project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

    }]);