'use strict';

App.factory('RolesService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI = 'http://localhost:8080/roles/';

    var factory = {
        fetchAllRoles: fetchAllRoles
    };

    return factory;

    function fetchAllRoles() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Roles');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);