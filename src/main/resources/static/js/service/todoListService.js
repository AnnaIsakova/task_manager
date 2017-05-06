'use strict';

app.factory('TodoListService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){

    var REST_SERVICE_URI = 'http://localhost:8080/api/';

    var factory = {
        fetchAllTasks: fetchAllTasks,
        fetchAllStatus: fetchAllStatus,
        fetchAllPriorities: fetchAllPriorities,
        createTask:createTask
    };

    return factory;

    function fetchAllTasks() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'tasks')
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
        $http.get(REST_SERVICE_URI + 'status')
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
        $http.get(REST_SERVICE_URI + 'priorities')
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

    function createTask(task) {
        var deferred = $q.defer();
        var task_json = angular.toJson(task);
        $http.post(REST_SERVICE_URI + 'new_task', task_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating Task');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);