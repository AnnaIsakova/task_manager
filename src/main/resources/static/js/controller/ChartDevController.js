'use strict';

app.controller('ChartDevController', ['$scope', '$rootScope', 'CrudService',
    function($scope, $rootScope, CrudService) {

        $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
        $scope.data = [300, 500, 100];
    }]);