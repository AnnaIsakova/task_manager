'use strict';

App.directive('uniqueEmail', function(isEmailAvailable) {
    return {
        restrict: 'A',
        require: "ngModel",
        link: function(scope, element, attributes, ngModel) {
            ngModel.$asyncValidators.unique = isEmailAvailable;

        }
    };
});