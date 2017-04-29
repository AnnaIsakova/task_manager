'use strict';

App.controller('SignUpController', ['$scope', '$state', '$http', 'close', 'UserService', 'RolesService',
    function($scope, $state, $http, close, UserService, RolesService) {
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
                        console.log('roles:', $scope.roles);
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
                                    // $scope.user = d;
                                    console.log($http.defaults.headers.common['Authorization']);
                                }
                            },
                            function(errResponse){
                                console.error('Error while login User');
                                $scope.errorMessage = "Wrong email or password :(";
                                $timeout(function () { $scope.errorMessage = false; }, 2000);
                            }
                        );
                        //
                    },
                    function(errResponse){
                        console.error('Error while creating User');
                        $scope.errorMessage = "Oops, something went wrong :(\nPlease, try again!";
                    }
                );
        }


        $scope.submit = function (result) {
            // if(self.user.id===null){
            console.log(angular.toJson($scope.user));
            createUser(result);

            // }else{
            //     updateUser(self.user, self.user.id);
            //     console.log('User updated with id ', self.user.id);
            // }
        };


        function reset(){
            $scope.user={firstName:'', lastName:'', password: '', email:'', role:''};
            $scope.confirmPassword = '';
            $scope.singUpForm.$setPristine(); //reset Form
        }

    }]);