'use strict';

app.factory('TemplateService', ['$rootScope', 'UserService', function($rootScope, UserService){

    var factory = {
        getTemplate:getTemplate
    };

    return factory;

    function getTemplate() {
        var user = JSON.parse(UserService.getCookieUser());
        if(user.authorities[0].authority == 'TEAMLEAD'){
            return 'tl';
        } else {
            return 'dev';
        }
    }

}]);