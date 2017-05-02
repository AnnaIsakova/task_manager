'use strict';

app.controller('MainController', function($scope, $state, ModalService) {

    $scope.showSignUp = function() {
        ModalService.showModal({
            templateUrl: '/views/signUpModal.html',
            controller: "SignUpController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $state.go('dashboard.projects');
            });
        });
    };

    $scope.showSignIn = function() {
        ModalService.showModal({
            templateUrl: '/views/signInModal.html',
            controller: "SignInController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $state.go('dashboard.projects');
            });
        });
    };

});