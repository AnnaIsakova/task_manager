'use strict';

app.directive('fileModel', function($parse, $rootScope) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    $rootScope.file = modelSetter(scope, element[0].files[0]);
                    console.log("model:", modelSetter(scope, element[0].files[0]));
                });
            });
        }
    };
});