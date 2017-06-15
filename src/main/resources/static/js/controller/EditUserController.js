'use strict';

app.controller('EditUserController', ['$scope', '$rootScope', '$http', '$state', 'close', 'CrudService', 'UserService',
    function($scope, $rootScope, $http, $state, close, CrudService, UserService) {

        var oldUser = $rootScope.userForEdit;
        $scope.user = angular.copy(oldUser);
        $scope.password = '';
        $scope.confirmPassword = '';
        var reLogin = false;
        var header = '';
        var oldCookieUser = {};

        function editUser (user) {
            console.log('user for editing: ', user);
            CrudService.updateObj('users', user)
                .then(
                    function(d) {
                        // $scope.user = d;
                        if (!reLogin){
                            UserService.setCookieData(user, header);
                            console.log("old coockie user before:", oldCookieUser.principal);
                            oldCookieUser.principal.fullName = user.firstName + ' ' + user.lastName;
                            console.log("old coockie user after:", oldCookieUser)
                            UserService.setCookieData(oldCookieUser, header);
                            console.log("cookies:: ", UserService.getCookieUser());
                        }
                        $('#editProfileModal').modal('hide');
                        close(reLogin, 500);
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
            if (result.email != oldUser.email || $scope.password != ''){
                if ($scope.password == $scope.confirmPassword){
                    result.password = $scope.password;
                    reLogin = true;
                } else {
                    $scope.errorMessage = "Password and confirm password don't match";
                }
            }
            if (!reLogin){
                header = UserService.getCookieHeader();
                oldCookieUser = JSON.parse(UserService.getCookieUser());
            }
            UserService.clearCookieData();
            editUser(result);
        };

        $scope.close = function (){
            close(null, 500);
            $('#editProfileModal').modal('hide');
        };

    }]);