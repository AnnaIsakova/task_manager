'use strict';

app.controller('DeleteProjectController', ['$scope', '$rootScope', 'close', 'CrudService',
    function($scope, $rootScope, close, CrudService) {

        $scope.delete = function () {
            console.log('deleting: ', $rootScope.projectId);

            CrudService.deleteObj('projects', $rootScope.projectId)
                .then(
                    function(d) {
                        $('#deletingProjectModal').modal('hide');
                        close({}, 500);
                    },
                    function(errResponse){
                        console.error('Error while deleting project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.close = function (){
            close(null, 500);
            $('#deletingProjectModal').modal('hide');
        }

    }]);