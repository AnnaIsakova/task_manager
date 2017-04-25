'use strict';

App.controller('SignUpController', ['$scope', '$state', 'close', 'UserService', 'RolesService',
    function($scope, $state, close, UserService, RolesService) {
        var self = this;
        $scope.user={id:'', firstName:'', lastName:'', password: '', email:'', role:''};
        $scope.users=[];
        $scope.confirmPassword = '';
        $scope.role={role:''};
        $scope.roles=[];

        // self.submit = submit;
        // self.reset = reset;

        // $scope.close = function(result) {
        //
        //     close(result, 500);
        //     console.log(result);
        //     submit();// close, but give 500ms for bootstrap to animate
        // };

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
                        $scope.user.id = d;
                        console.log('id: ', $scope.user.id);
                        $('#signUpModal').modal('hide');
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