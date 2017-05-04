'use strict';

var app = angular.module('app',['ui.router', 'angularModalService', 'ngCookies']);

app.run(function ($state,$rootScope) {
    $rootScope.$state = $state;
});

app.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $urlRouterProvider.otherwise('/');
    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: '/views/home.html'
        })
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        .state('dashboard', {
            url: '/dashboard',
            views: {
                "navBar": {
                    templateUrl: '/views/navBar.html',
                    controller: 'NavBarController'
                },
                "sideMenu": {
                    templateUrl: '/views/sideMenu.html'
                }
            }
        })
        .state('dashboard.projects', {
            url: '/projects',
            views: {
                "content": {
                    templateProvider: function(TemplateService) {
                        var result = TemplateService.getTemplate();
                        console.log(result);
                        // ngInclude template content based on the A/B test result
                        return '<div ng-include="" src="\'views/projects-' + result + '.html\'\"</div>';

                    },
                    controller: 'NavBarController'
                }
            }

        })
        .state('dashboard.todo', {
            url: '/todo',
            views: {
                "content": {
                    templateUrl: '/views/todoList.html',
                    controller: 'TodoListController'
                }
            }
        })
        .state('dashboard.calendar', {
            url: '/calendar',
            views: {
                "content": {
                    templateUrl: '/views/calendar.html',
                    controller: 'NavBarController'
                }
            }
        })
        .state('dashboard.charts', {
            url: '/charts',
            views: {
                "content": {
                    templateUrl: '/views/charts.html',
                    controller: 'NavBarController'
                }
            }
        })

});