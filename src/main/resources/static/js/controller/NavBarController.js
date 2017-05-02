'use strict';

app.controller('NavBarController', ['$http', '$scope', '$rootScope', '$state', 'UserService',
    function($http, $scope, $rootScope, $state, UserService) {
        $scope.user = JSON.parse(UserService.getCookieData());
        $scope.name = $scope.user.principal.fullName;

        $scope.logout = function () {
            $state.go('home');
            UserService.logout();
        };
    }]);