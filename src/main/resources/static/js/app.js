'use strict';

var app = angular.module('app',[
    'ui.router',
    'angularModalService',
    'ngCookies',
    'kendo.directives',
    'angularMoment',
    'ckeditor',
    'ngSanitize',
    'ngFileSaver',
    'chart.js',
    'ui.bootstrap',
    'ui.calendar'
]);

app.run(function ($state, $rootScope, $http, UserService) {
    $rootScope.$state = $state;

    $rootScope.$on( '$stateChangeStart', function(e, toState  , toParams, fromState, fromParams) {

        var isLoggedIn = UserService.getCookieHeader() !== undefined;
        if (toState.name === 'login'){
            return;
        }
        if (isLoggedIn){
            $http.defaults.headers.common['Authorization'] = UserService.getCookieHeader();
            return;
        } else {
            e.preventDefault();
            $state.go('login');
        }

    });
});

app.config(function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $compileProvider) {

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: true,
        rewriteLinks: true
    });

    $urlRouterProvider.otherwise('/login');
    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: '/views/home.html'
        })
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        .state('home', {
            url: '',
            abstract: true,
            views: {
                "navBar": {
                    templateUrl: '/views/navBar.html',
                    controller: 'NavBarController'
                },
                "sideMenu": {
                    templateUrl: '/views/sideMenu.html'
                },
                "content": {
                    templateUrl: '/views/content.html'
                }
            }
        })
        .state('home.projects', {
            url: '/projects',
            views: {
                "inner_content": {
                    templateProvider: function(TemplateService) {
                        var result = TemplateService.getTemplate();
                        console.log(result);
                        // ngInclude template content based on the A/B test result
                        return '<div ng-include="" src="\'views/projects-' + result + '.html\'\"</div>';

                    },
                    controller: 'ProjectListController'
                }
            }

        })
        .state('home.project', {
            url: '/projects/:projectID',
            views: {
                "inner_content": {
                    templateProvider: function(TemplateService) {
                        var result = TemplateService.getTemplate();
                        console.log(result);
                        // ngInclude template content based on the A/B test result
                        return '<div ng-include="" src="\'views/projectInfo-' + result + '.html\'\"</div>';

                    },
                    controller: 'ProjectInfoController'
                }
            }

        })
        .state('home.devs', {
            url: '/projects/:projectID/devs',
            views: {
                "inner_content": {
                    templateUrl: '/views/devList.html',
                    controller: 'DevController'
                }
            }

        })
        .state('home.tasks', {
            url: '/projects/:projectID/tasks/:filter',
            views: {
                "inner_content": {
                    templateProvider: function(TemplateService) {
                        var result = TemplateService.getTemplate();
                        console.log(result);
                        // ngInclude template content based on the A/B test result
                        return '<div ng-include="" src="\'views/tasksProjectList-' + result + '.html\'\"</div>';

                    },
                    controller: 'ProjectTasksController'
                }
            }

        })
        .state('home.task', {
            url: '/projects/:projectID/tasks/:filter/:taskID',
            views: {
                "inner_content": {
                    templateProvider: function(TemplateService) {
                        var result = TemplateService.getTemplate();
                        return '<div ng-include="" src="\'views/taskInfo-' + result + '.html\'\"</div>';

                    },
                    controller: 'TaskInfoController'
                }
            }

        })
        .state('home.todo', {
            url: '/todo',
            views: {
                "inner_content": {
                    templateUrl: '/views/todoList.html',
                    controller: 'TodoListController'
                }
            }
        })
        .state('home.calendar', {
            url: '/calendar',
            views: {
                "inner_content": {
                    templateUrl: '/views/calendar.html',
                    controller: 'CalendarController'
                }
            }
        })
        .state('home.charts', {
            url: '/charts',
            views: {
                "inner_content": {
                    templateUrl: '/views/charts.html',
                    controller: 'ChartController'
                }
            }
        })
        .state('home.profile', {
            url: '/profile',
            views: {
                "inner_content": {
                    templateUrl: '/views/profile.html',
                    controller: 'ProfileController'
                }
            }
        })

});