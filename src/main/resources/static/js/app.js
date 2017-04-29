'use strict';

var App = angular.module('app',['ui.router', 'angularModalService']);
//
// App.run('', ['_', '$rootScope', '$state', 'Authorization', function(_, $rootScope, $state, Authorization) {
//
//     $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
//         if (!Authorization.authorized && _.has(toState, 'data.authorization') && _.has(toState, 'data.redirectTo')) {
//             console.log("OK")
//             $state.go(toState.data.redirectTo);
//         }
//     });
// }]);

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
            templateUrl: '/views/afterRegister.html'
        });
});