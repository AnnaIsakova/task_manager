'use strict';

app.factory('CrudService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI = 'http://localhost:8080/api/';

    var factory = {
        fetchAll: fetchAll,
        fetchOne:fetchOne,
        createObj:createObj,
        deleteObj:deleteObj,
        updateObj:updateObj
    };

    return factory;

    function fetchAll(entity) {
        var deferred = $q.defer();
        console.log(REST_SERVICE_URI + entity)
        $http.get(REST_SERVICE_URI + entity)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching ' + entity);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchOne(entity, id) {
        var deferred = $q.defer();
        console.log(REST_SERVICE_URI + entity + '/' + id)
        $http.get(REST_SERVICE_URI + entity + '/' + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching this ' + entity);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createObj(name, object) {
        var deferred = $q.defer();
        console.log(REST_SERVICE_URI + name + '/new')
        $http.post(REST_SERVICE_URI + name + '/new', object)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating ' + name);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function updateObj(name, object) {
        var deferred = $q.defer();
        var object_json = angular.toJson(object);

        $http.post(REST_SERVICE_URI + name + '/edit', object_json)
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

    function deleteObj(name, id) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + name + '/delete?id=' + id)
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