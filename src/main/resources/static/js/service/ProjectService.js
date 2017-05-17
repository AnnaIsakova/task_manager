'use strict';

app.factory('ProjectService', ['$http', '$q', '$window', '$rootScope','Blob', 'FileSaver', function($http, $q, $window, $rootScope, Blob, FileSaver){

    var REST_SERVICE_URI = 'http://localhost:8080/api/projects';

    var factory = {
        fetchAllProjects: fetchAllProjects,
        fetchProject:fetchProject,
        createProject:createProject,
        editProject:editProject,
        deleteProject:deleteProject,
        addDeveloper:addDeveloper,
        deleteDeveloper:deleteDeveloper,
        fetchAllDevelopers:fetchAllDevelopers,
        uploadFile:uploadFile,
        downloadFile:downloadFile,
        deleteFile:deleteFile,
        fetchAllFiles:fetchAllFiles,
        addComment:addComment,
        fetchAllComments:fetchAllComments,
        deleteComment:deleteComment
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
    
    function deleteDeveloper(projectId, devId) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + '/deleteDeveloper?projectId=' + projectId + '&devId=' + devId)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting developer');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchAllDevelopers(projectID) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + '/' + projectID + '/getDevelopers')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching this files');
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
    
    function downloadFile(projectId, fileId) {

        var deferred = $q.defer();
        console.log(projectId);
        console.log(fileId);
        $http({
            method: 'GET',
            url: REST_SERVICE_URI + '/' + projectId + '/download/' + fileId,
            responseType: 'arraybuffer'
        })
            .then(
                function (response) {
                    var contentType = response.headers(["content-type"]);
                    var fileName = response.headers(["file-name"]);
                    var blob = new Blob([response.data], {type: contentType});
                    FileSaver.saveAs(blob, fileName);
                },
                function(errResponse){
                    console.log(errResponse);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteFile(projectID, fileId) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + '/deleteFile?projectId=' + projectID + '&fileId=' + fileId)
            .then(
                function (response) {
                    console.log("file deleted");
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while adding developer');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
    
    function fetchAllFiles(projectID) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + '/' + projectID + '/getFiles')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching this files');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function addComment(projectID, comment){
        var deferred = $q.defer();
        console.log(comment);
        $http.post(REST_SERVICE_URI + '/addComment?id=' + projectID, comment)
            .then(
                function (response) {
                    console.log("comment added");
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while adding comment');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchAllComments(projectID) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + '/' + projectID + '/getComments')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching comments');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteComment(projectId, commentId) {
        var deferred = $q.defer();
        console.log(commentId)
        $http.post(REST_SERVICE_URI + '/deleteComment?projectId=' + projectId + '&commentId=' + commentId)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting comment');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);