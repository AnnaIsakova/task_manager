'use strict';

app.factory('TodoListService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){

    var REST_SERVICE_URI = 'http://localhost:8080/';

    var factory = {
        fetchAllTasks: fetchAllTasks,
        fetchAllStatus: fetchAllStatus,
        fetchAllPriorities: fetchAllPriorities
    };

    return factory;

    function fetchAllTasks() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'api/tasks')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Tasks');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchAllStatus() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'api/status')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching status');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchAllPriorities() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'api/priorities')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching priorities');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);