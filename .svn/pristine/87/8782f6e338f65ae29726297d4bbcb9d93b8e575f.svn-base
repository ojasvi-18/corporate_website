//change this url with the url from the server that would be providing the json data for the user profile.
let userDataJsonURL = "static/portal/data/profile.json";

angular.module('App').controller("ProfileController", function($scope, $http, $routeParams) {
  var initialProfileObject;
  $scope.rowContent;
  
  let isEdit = $routeParams.edit === 'edit';
  $scope.isEdit = isEdit;
  
  $http.get(userDataJsonURL) // reading json file
  .then(function mySuccess(response) {
    initialProfileObject = response.data;
    $scope.rowContent = initialProfileObject;
  }, function myError(response) {
    console.error('failure loading the student record', response.statusText, response.data);
  });

  $scope.clickHandler = function() {
    console.log(this.rowContent);
  }
  
});