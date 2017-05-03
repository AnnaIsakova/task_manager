'use strict';

app.factory('TemplateService', ['$rootScope', function($rootScope){

    var factory = {
        getTemplate:getTemplate
    };

    return factory;

    function getTemplate() {
        if($rootScope.user.authorities[0].authority == 'TEAMLEAD'){
            return 'tl';
        } else {
            return 'dev';
        }
    }

}]);