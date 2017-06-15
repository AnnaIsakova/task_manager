'use strict';

app.controller('ProfileController', ['$http', '$scope', '$rootScope', '$state', 'UserService', 'CrudService', 'ModalService',
    function($http, $scope, $rootScope, $state, UserService, CrudService, ModalService) {
        var usr  = JSON.parse(UserService.getCookieUser());
        var usrId = usr.principal.id;
        $scope.user = {};

        fetchMe();

        function fetchMe() {
            console.log("user cookie::: ", UserService.getCookieUser());
            CrudService.fetchOne('users', usrId)
                .then(
                    function(d) {
                        $scope.user = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        $scope.openEdit = function (user) {
            console.log('open editing: ', user);
            $rootScope.userForEdit = user;
            ModalService.showModal({
                templateUrl: '/views/editUser.html',
                controller: "EditUserController"
            }).then(function(modal) {
                modal.element.modal({backdrop: 'static'});
                modal.close.then(function(reLogin) {
                    console.log("reLogin: ", reLogin)
                    if (reLogin){
                        $state.go("login");
                    } else{
                        fetchMe();
                    }
                });
            });
        };
    }]);