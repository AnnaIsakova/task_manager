'use strict';

app.controller('DeleteController', ['$scope', '$rootScope', 'close', 'CrudService',
    function($scope, $rootScope, close, CrudService) {

        $scope.delete = function (link, id, modal) {

            CrudService.deleteObj(link, id)
                .then(
                    function(d) {
                        $(modal).modal('hide');
                        close({}, 500);
                    },
                    function(errResponse){
                        console.error('Error while deleting project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.close = function (modal){
            close(null, 500);
            $(modal).modal('hide');
        }

        $scope.DeleteDeveloper = function (link, id, keepTasks, modal) {

            CrudService.deleteDeveloper(link, id, keepTasks)
                .then(
                    function(d) {
                        $(modal).modal('hide');
                        close({}, 500);
                    },
                    function(errResponse){
                        console.error('Error while deleting project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

    }]);