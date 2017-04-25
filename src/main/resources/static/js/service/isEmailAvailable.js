'use strict';

App.factory('isEmailAvailable', function($q, $http) {

    var REST_SERVICE_URI = 'http://localhost:8080/api/user/';

    return function(email) {
        var deferred = $q.defer();
        var encodedEmail = encodeURIComponent(email);
        console.log('res: ',encodedEmail);
        $http.get(REST_SERVICE_URI + encodedEmail).then(function() {
            // Found the user, therefore not unique.
            deferred.reject();
        }, function() {
            // User not found, therefore unique!
            deferred.resolve();
        });

        return deferred.promise;
    }
});