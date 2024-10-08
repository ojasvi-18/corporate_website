var App = angular.module("App", [
  "ngRoute"
]);

App.config([
    '$routeProvider', function($routeProvider) {
      $routeProvider.when("/profile/:edit", {
        templateUrl : "profile.html",
        controller : 'ProfileController'
      });
    }
]);
