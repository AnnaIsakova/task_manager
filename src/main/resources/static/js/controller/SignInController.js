'use strict';

app.controller('SignInController', ['$http', '$scope', '$rootScope', '$state', '$timeout', 'close', 'UserService',
    function($http, $scope, $rootScope, $state, $timeout, close, UserService) {

        // creating base64 encoded String from username and password
        $scope.login = function (){
            var base64Credential = btoa($scope.email + ':' + $scope.password);
            UserService.login(base64Credential)
                .then(
                    function (d) {
                        close(d, 500);
                        $('#signInModal').modal('hide');
                        if(d.authenticated) {
                            $http.defaults.headers.common['Authorization'] = 'Basic ' + base64Credential;
                            $rootScope.user = d;
                            console.log(d);
                            UserService.setCookieData($rootScope.user);
                        }
                    },
                    function(errResponse){
                        console.error('Error while login User');
                        $scope.errorMessage = "Wrong email or password :(";
                        $timeout(function () { $scope.errorMessage = false; }, 2000);
                    }
                );
        }
    }]);