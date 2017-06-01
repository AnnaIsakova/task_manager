'use strict';

app.controller('ChartController', ['$scope', '$rootScope', 'CrudService', 'UserService',
    function($scope, $rootScope, CrudService, UserService) {

        $scope.project={};
        $scope.projects=[];
        $rootScope.projectInfo={};
        var user  = JSON.parse(UserService.getCookieUser());
        var myId = user.principal.id;
        $scope.selectedProj = $scope.projects[0];

        $scope.labels = ["New", "In progress", "Complete", "Approved"];
        $scope.doughData = [];
        $scope.colors = ["#d9534f","#f0ad4e","#5bc0de", "#5cb85c"];

        $scope.horizData = [];

        fetchAllProjects();

        function fetchAllProjects(){
            CrudService.fetchAll('projects')
                .then(
                    function(d) {
                        $scope.projects = d;
                        console.log($scope.projects)
                        $scope.selectedProj = $scope.projects[0];
                        if (user.authorities[0].authority == "TEAMLEAD"){
                            fetchMe();
                        }

                    },
                    function(errResponse){
                        console.error('Error while fetching projects -> from controller');
                        $scope.errorMessage = errResponse.data.message;
                    }
                );
        }

        function fetchMe() {
            CrudService.fetchOne('users', myId)
                .then(
                    function(d) {
                        $scope.me = d;
                        $scope.getData();
                    },
                    function(errResponse){
                        console.error('Error while fetching priorities -> from controller');
                        $scope.errorMessage = "Oops, error while fetching priorities occurred :(\nPlease, try again later!";
                    }
                );
        }

        $scope.getData = function(){
            getHorizData();
            getDoughData();
        };

        function getDoughData () {
            $scope.doughData = [];
            var newTask = 0;
            var progress = 0;
            var complete = 0;
            var approved = 0;

            if (user.authorities[0].authority == "TEAMLEAD"){
                $scope.selectedProj.developers.push($scope.me);
            }
            for (var i=0; i<$scope.selectedProj.developers.length; i++){
                newTask = 0;
                progress = 0;
                complete = 0;
                approved = 0;
                var dev = $scope.selectedProj.developers[i];
                for (var j=0; j<$scope.selectedProj.tasks.length; j++){
                    var task = $scope.selectedProj.tasks[j];
                    if (task.assignedTo != null && dev.email == task.assignedTo.email){
                        if (task.status == "NEW") {
                            newTask++;
                        } else if (task.status == "IN_PROGRESS"){
                            progress++;
                        }  else if (task.status == "COMPLETE" && !task.approved){
                            complete++;
                        }  else if (task.approved){
                            approved++;
                        }
                    }
                }
                var data = [newTask, progress, complete, approved];
                var name = dev.firstName + " " + dev.lastName;
                var obj = {};
                obj[name] = data;
                $scope.doughData.push(obj);
            }
            
            if(newTask == 0 && progress == 0 && complete == 0 && approved == 0){
                $scope.doughData = [];
            }
        }

        function getHorizData () {
            $scope.horizData = [];
            var newTask = 0;
            var progress = 0;
            var complete = 0;
            var approved = 0;

            for (var j=0; j<$scope.selectedProj.tasks.length; j++){
                var task = $scope.selectedProj.tasks[j];
                if (task.status == "NEW") {
                    newTask++;
                } else if (task.status == "IN_PROGRESS"){
                    progress++;
                }  else if (task.status == "COMPLETE" && !task.approved){
                    complete++;
                }  else if (task.approved){
                    approved++;
                }
            }
            $scope.horizData = [newTask, progress, complete, approved];
            if(newTask == 0 && progress == 0 && complete == 0 && approved == 0){
                $scope.horizData = [];
            }
            }


    }]);