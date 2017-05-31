'use strict';

app.controller('EditUserController', ['$scope', '$rootScope', '$http', 'close', 'CrudService', 'UserService',
    function($scope, $rootScope, $http, close, CrudService, UserService) {

        var oldUser = $rootScope.userForEdit;
        $scope.user = angular.copy(oldUser);
        $scope.password = '';
        $scope.confirmPassword = '';
        var clearCoockie = false;

        function editUser (user) {
            console.log('user for editing: ', user);
            CrudService.updateObj('users', user)
                .then(
                    function(d) {
                        // $scope.user = d;
                        $('#editProfileModal').modal('hide');
                        close(user, 500);
                        if(clearCoockie){
                            login(user.email, user.password);
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
            if (result.email != undefined || $scope.password != ''){
                if ($scope.password == $scope.confirmPassword){
                    result.password = $scope.password;
                    UserService.clearCookieData();
                    clearCoockie = true;
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