'use strict';

app.controller('NavBarController', ['$http', '$scope', '$rootScope', '$state', 'UserService',
    function($http, $scope, $rootScope, $state, UserService) {
        $scope.user = JSON.parse(UserService.getCookieUser());
        $scope.name = $scope.user.principal.fullName;

        $scope.logout = function () {
            $state.go('home');
            UserService.logout();
        };
    }]);