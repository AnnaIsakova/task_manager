'use strict';

App.controller('MainController', function($scope, $state, ModalService) {

    $scope.showSignUp = function() {
        ModalService.showModal({
            templateUrl: '/views/signUpModal.html',
            controller: "SignUpController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $state.go('afterReg', {id: result.id});
            });
        });
    };

});