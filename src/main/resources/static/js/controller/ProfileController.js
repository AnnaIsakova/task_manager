'use strict';

app.controller('ProfileController', ['$http', '$scope', '$rootScope', '$state', 'UserService', 'CrudService',
    function($http, $scope, $rootScope, $state, UserService, CrudService) {
        var usr  = JSON.parse(UserService.getCookieUser());
        var usrId = usr.principal.id;
        $scope.user = {};

        fetchMe();

        function fetchMe() {
            CrudService.fetchOne('users', usrId)
                .then(
                    function(d) {
                        $scope.user = d;
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }
    }]);