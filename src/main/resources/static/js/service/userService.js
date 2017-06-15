'use strict';

app.factory('UserService', ['$http', '$q', '$cookies', '$rootScope', function($http, $q, $cookies, $rootScope){

    var user = {};
    var header = '';

    var factory = {
        fetchAllUsers: fetchAllUsers,
        createUser: createUser,
        login:login,
        logout:logout,
        setCookieData:setCookieData,
        getCookieUser:getCookieUser,
        getCookieHeader:getCookieHeader,
        clearCookieData:clearCookieData
    };

    return factory;
    
    function fetchAllUsers() {
        var deferred = $q.defer();
        $http.get('/api/user/')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createUser(user) {
        var deferred = $q.defer();
        var user_json = angular.toJson(user);
        $http.post('/register', user_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log("data from service: ", response.data);
                },
                function(errResponse){
                    console.error('Error while creating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
    
    function login(base64Credential) {
        var deferred = $q.defer();
        $http.get('/sign_in', {
            headers : {
                // setting the Authorization Header
                'Authorization' : 'Basic ' + base64Credential
            }
        })
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while login User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function logout() {
        $http.defaults.headers.common['Authorization'] = null;
        clearCookieData();
    }
    
    function setCookieData(user, header) {
        $cookies.putObject("user", user);
        $cookies.put("header", header);
    }
    function getCookieUser() {
        user = $cookies.get("user");
        return user;
    }
    function getCookieHeader() {
        header = $cookies.get("header");
        return header;
    }
    function clearCookieData() {
        $rootScope.user = {};
        $cookies.remove("user");
        $rootScope.header = '';
        $cookies.remove("header");
    }

}]);