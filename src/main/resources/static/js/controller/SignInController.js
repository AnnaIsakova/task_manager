'use strict';

App.controller('SignInController', ['$http', '$scope', '$state', '$timeout', 'close', 'UserService',
    function($http, $scope, $state, $timeout, close, UserService) {

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
                            $scope.user = d;
                            console.log($http.defaults.headers.common['Authorization']);
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