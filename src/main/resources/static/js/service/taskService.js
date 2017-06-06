'use strict';

app.factory('TaskService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI = '/api/projects/';

    var factory = {
        approveTask: approveTask,
        changeStatus: changeStatus
    };

    return factory;

    function approveTask(projId, taskId) {
        var deferred = $q.defer();

        $http.post(REST_SERVICE_URI +projId+ '/tasks/' + taskId + '/approve')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while approving Task');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function changeStatus(projId, taskId, status) {
        var deferred = $q.defer();
        console.log(REST_SERVICE_URI  +projId+ '/tasks/' + taskId +'?status=' + status)
        $http.post(REST_SERVICE_URI  +projId+ '/tasks/' + taskId +'?status=' + status)
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

    function deleteDeveloper(name, id, keepTasks) {
        var deferred = $q.defer();
        console.log(REST_SERVICE_URI + name + '/delete?id=' + id + '&keep_tasks=' + keepTasks)
        $http.post(REST_SERVICE_URI + name + '/delete?id=' + id + '&keep_tasks=' + keepTasks)
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