'use strict';

var App = angular.module('app',['ui.router', 'angularModalService']);

App.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $urlRouterProvider.otherwise('/');
    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: '/views/home.html',
            authenticated: false
        })
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        .state('afterReg', {
            url: '/:id',
            templateUrl: '/views/afterRegister.html',
            authenticated: true
        });
});