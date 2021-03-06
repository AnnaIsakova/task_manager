'use strict';

app.factory('FileService', ['$http', '$q', '$window', '$rootScope','Blob', 'FileSaver', function($http, $q, $window, $rootScope, Blob, FileSaver){

    var REST_SERVICE_URI = '/api/';

    var factory = {
        uploadFile:uploadFile,
        downloadFile:downloadFile
    };

    return factory;

    function uploadFile(name, file){
        var deferred = $q.defer();
        var fd = new FormData();
        fd.append('file', file);
        $http.post(REST_SERVICE_URI + name + '/new', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
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

    function downloadFile(name, fileId) {
        var deferred = $q.defer();
        $http({
            method: 'GET',
            url: REST_SERVICE_URI + name + '/' + fileId,
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
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


}]);