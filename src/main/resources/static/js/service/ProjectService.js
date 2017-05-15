'use strict';

app.factory('ProjectService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){

    var REST_SERVICE_URI = 'http://localhost:8080/api/projects';

    var factory = {
        fetchAllProjects: fetchAllProjects,
        fetchProject:fetchProject,
        createProject:createProject,
        editProject:editProject,
        deleteProject:deleteProject,
        addDeveloper:addDeveloper,
        uploadFile:uploadFile
    };

    return factory;

    function fetchAllProjects() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
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

    function fetchProject(id) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + '/' + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching this Project');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createProject(project) {
        var deferred = $q.defer();
        var project_json = angular.toJson(project);
        $http.post(REST_SERVICE_URI + '/new', project_json)
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
        $http.post(REST_SERVICE_URI + '/edit', task_json)
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
        $http.post(REST_SERVICE_URI + '/delete', id)
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
    
    function addDeveloper(id, email) {
        var deferred = $q.defer();
        console.log(email);
        console.log(id);
        $http.post(REST_SERVICE_URI + '/add_developer?id=' + id + '&email=' + email)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while adding developer');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function uploadFile(id, file){
        var deferred = $q.defer();
        var fd = new FormData();
        fd.append('file', file);
        $http.post(REST_SERVICE_URI + '/uploadFile?id=' + id, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .then(
                function (response) {
                    console.log("file uploaded");
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while adding developer');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);