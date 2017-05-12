'use strict';

app.factory('ProjectService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){

    var REST_SERVICE_URI = 'http://localhost:8080/api/';

    var factory = {
        fetchAllProjects: fetchAllProjects,
        createProject:createProject,
        editProject:editProject,
        deleteProject:deleteProject
    };

    return factory;

    function fetchAllProjects() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'projects')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Projects');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createProject(project) {
        var deferred = $q.defer();
        var task_json = angular.toJson(project);
        $http.post(REST_SERVICE_URI + 'projects/new', task_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating Project');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function editProject(project) {
        var deferred = $q.defer();
        var task_json = angular.toJson(project);
        $http.post(REST_SERVICE_URI + 'projects/edit', task_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while editing project');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteProject(id) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + 'projects/delete', id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting project');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);