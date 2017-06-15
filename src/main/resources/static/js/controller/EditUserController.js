'use strict';

app.controller('EditUserController', ['$scope', '$rootScope', '$http', '$state', 'close', 'CrudService', 'UserService',
    function($scope, $rootScope, $http, $state, close, CrudService, UserService) {

        var oldUser = $rootScope.userForEdit;
        $scope.user = angular.copy(oldUser);
        $scope.password = '';
        $scope.confirmPassword = '';
        var clearCookie = false;

        function editUser (user) {
            console.log('user for editing: ', user);
            CrudService.updateObj('users', user)
                .then(
                    function(d) {
                        // $scope.user = d;
                        $('#editProfileModal').modal('hide');
                        close(clearCookie, 500);
                        if(clearCookie){
                            console.log("user email: ", user.email);
                            console.log("user password: ", user.password);
                            clearCookie = false;
                        }
                    },
                    function(errResponse){
                        console.error('Error while editing project -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        };

        $scope.submit = function (result) {
            if (result.email == undefined){
                result.email = oldUser.email;
            }
            console.log("result.email != oldUser.email || $scope.password != ''", result.email != oldUser.email || $scope.password != '')
            if (result.email != oldUser.email || $scope.password != ''){
                if ($scope.password == $scope.confirmPassword){
                    result.password = $scope.password;
                    UserService.clearCookieData();
                    clearCookie = true;
                } else {
                    $scope.errorMessage = "Password and confirm password don't match";
                }
            }
            editUser(result);
        };

        $scope.close = function (){
            close(null, 500);
            $('#editProfileModal').modal('hide');
        }

        function login(email, pass) {
            var base64Credential = btoa(email + ':' + pass);
            UserService.login(base64Credential)
                .then(
                    function (d) {
                        if(d.authenticated) {
                            $http.defaults.headers.common['Authorization'] = 'Basic ' + base64Credential;
                            $rootScope.user = d;
                            UserService.setCookieData($rootScope.user, 'Basic ' + base64Credential);
                        }
                    },
                    function(errResponse){
                        console.error('Error while login User');
                        $scope.errorMessage = "Wrong email or password :(";
                    }
                );
        }

    }]);