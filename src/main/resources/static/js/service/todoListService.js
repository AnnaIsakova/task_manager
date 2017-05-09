'use strict';

app.factory('TodoListService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){

    var REST_SERVICE_URI = 'http://localhost:8080/api/';

    var factory = {
        fetchAllTasks: fetchAllTasks,
        fetchAllStatus: fetchAllStatus,
        fetchAllPriorities: fetchAllPriorities,
        createTask:createTask,
        deleteTask:deleteTask,
        editTask:editTask
    };

    return factory;

    function fetchAllTasks() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'todo')
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
        $http.post(REST_SERVICE_URI + 'todo/new', task_json)
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

    function editTask(task) {
        var deferred = $q.defer();
        var task_json = angular.toJson(task);
        $http.post(REST_SERVICE_URI + 'todo/edit', task_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while editing Task');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteTask(id) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + 'todo/delete', id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting Task');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);