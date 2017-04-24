'use strict';

var App = angular.module('app',['ui.router', 'angularModalService']);

App.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider

        .state('home', {
            url: '/',
            templateUrl: '/views/home.html'
        })
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        .state('afterReg', {
            url: '/after',
            templateUrl: '/views/afterRegister.html'
        });

});