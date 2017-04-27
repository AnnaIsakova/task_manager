'use strict';

App.controller('SignInController', ['$http', '$scope', '$state', '$timeout', 'close', 'UserService',
    function($http, $scope, $state, $timeout, close, UserService) {

        // creating base64 encoded String from username and password
        $scope.login = function (){
            var base64Credential = btoa($scope.email + ':' + $scope.password);
            console.log(base64Credential);
            UserService.login(base64Credential)
                .then(
                    function (d) {
                        console.log(d);
                    },
                    function(errResponse){
                        console.error('Error while login User');
                        $scope.errorMessage = "Wrong email or password :(";
                        $timeout(function () { $scope.errorMessage = false; }, 2000);
                    }
                );
        }
    }]);