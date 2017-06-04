'use strict';

app.controller('CalendarController', ['$scope', '$rootScope', 'CrudService', 'UserService',
    function($scope, $rootScope, CrudService, UserService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.projectInfo={};
        var user  = JSON.parse(UserService.getCookieUser());
        var myId = user.principal.id;
        $scope.selectedProj = $scope.projects[0];
        var tasks = [];
        $scope.data = [];
        $scope.eventsF = function (start, end, timezone, callback) {
            callback($scope.data);
        };
        $scope.eventSources = [$scope.data, $scope.eventsF];

        fetchAllProjects();

        function fetchAllProjects(){
            CrudService.fetchAll('projects')
                .then(
                    function(d) {
                        $scope.projects = d;
                        $scope.selectedProj = $scope.projects[0];
                        fetchTasks($scope.selectedProj.id);
                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchTasks(id){
            console.log("proj id: ", id);
            CrudService.fetchAll('projects/' + id + '/tasks')
                .then(
                    function(d) {
                        tasks = d;
                        console.log("tasks: ", tasks)
                        getItems();
                    },
                    function(errResponse){
                        console.error('Error while fetching tasks -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }
        
        $scope.getData = function () {
            fetchTasks($scope.selectedProj.id);
        };



        function getItems() {
            $scope.data = [];
            var id;
            var title;
            var start;
            var end;
            var allDay = true;
            var item = {};
            for (var i=0; i<tasks.length; i++){
                title = tasks[i].description;
                start = new Date(tasks[i].createDate);
                end = new Date(tasks[i].deadline);
                id = i;
                item = {
                    _id: id,
                    title: title,
                    start: start,
                    end: end,
                    allDay: allDay
                };
                $scope.data.push(item);
            }
            $scope.eventsF = function (start, end, timezone, callback) {
                callback($scope.data);
            };
            $scope.eventSources = [$scope.data, $scope.eventsF];
        }


    }]);