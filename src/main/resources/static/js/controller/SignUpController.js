'use strict';

app.controller('SignUpController', ['$scope', '$rootScope', '$state', '$http', 'close', 'UserService', 'RolesService',
    function($scope, $rootScope, $state, $http, close, UserService, RolesService) {
        var self = this;
        $scope.user={firstName:'', lastName:'', password: '', email:'', role:''};
        $scope.users=[];
        $scope.confirmPassword = '';
        $scope.role={role:''};
        $scope.roles=[];

        fetchAllRoles();

        function fetchAllRoles(){
            RolesService.fetchAllRoles()
                .then(
                    function(d) {
                        $scope.roles = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching Roles');
                        $scope.errorMessage = "Oops, error while fetching roles occurred :(\nPlease, try again later!";
                    }
                );
        }

        function createUser(user){
            UserService.createUser(user)
                .then(
                    function (d) {
                        close(user, 500);
                        $('#signUpModal').modal('hide');
                        var base64Credential = btoa($scope.user.email + ':' + $scope.user.password);
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
                        //
                    },
                    function(errResponse){
                        console.error(errResponse);
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }


        $scope.submit = function (result) {
            console.log(angular.toJson($scope.user));
            console.log('email: ', $scope.user.email);
            if ($scope.user.email == undefined){
                result.email = '';
            }
            createUser(result);
        };


    }]);