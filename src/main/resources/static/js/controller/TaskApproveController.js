'use strict';

app.controller('TaskApproveController', ['$scope', '$rootScope', 'close', 'TaskService',
    function($scope, $rootScope, close, TaskService) {

        $scope.approve = function (projId, taskId, modal) {

            TaskService.approveTask(projId, taskId)
                .then(
                    function(d) {
                        $(modal).modal('hide');
                        close({}, 500);
                    },
                    function(errResponse){
                        console.error('Error while approving task -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.close = function (modal){
            close(null, 500);
            $(modal).modal('hide');
        }
    }]);