'use strict';

var App = angular.module('app',['ui.router', 'angularModalService', 'ngCookies']);


App.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

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
        });
});