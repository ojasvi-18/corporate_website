//change this url with the url from the server that would be providing the json data for the user profile.
var userDataJsonURL = "static/portal/data/profile.json";

angular.module('App').controller(
    "ProfileController",
    function($scope, $http, $routeParams) {
      var initialProfileObject;
      $scope.rowContent;

      var isEdit = $routeParams.edit === 'edit';
      $scope.isEdit = isEdit;

      $http.get(userDataJsonURL) // reading json file
      .then(function mySuccess(response) {
        initialProfileObject = response.data;
        $scope.rowContent = angular.copy(initialProfileObject);
      }, function myError(response) {
        console.error('failure loading the student record', response.statusText, response.data);
      });

      $scope.submit = function() {

        // var app = 'test';
        // var d1 = 9;
        // var d2 = 8;

        $http({
          url : 'postDummy',
          method : 'POST',
          data : $scope.rowContent,
          headers : {
            'Content-Type' : 'application/json'
          }
        }).success(function(data, headers, config) {
          // assign $scope.persons here as promise is resolved here
        }).error(function(data, status, headers, config) {
          $scope.status = status + ' ' + headers;
        });

        /*
         * var personalDetailIsValid, contactDetailIsValid, teamDetailIsValid,
         * addressDetailIsValid; PersonalDetailIsValid =
         * PersonalFormValidation(); // Add validation code contactDetailIsValid =
         * ContactFormValidation(); addressDetailIsValid =
         * AddressFormValidation(); if (personalDetailIsValid &&
         * contactDetailIsValid && addressDetailIsValid) {
         * console.log(this.rowContent.PersonalDetails); } else {
         * console.log("error"); }
         *  // code to submit data to the server from the json object
         *  }
         */
      }
      var flag = true;
      var bloodGroupRegex = "^(A|B|AB|O)[-+]$";

      var PersonalFormValidation = function() {
        if (angular.equals(($scope.rowContent.PersonalDetails), (initialProfileObject.PersonalDetails))) {
          return false;
        }
        if ((angular.equals($scope.rowContent.PersonalDetails.Name, "")) || ($scope.rowContent.PersonalDetails.Name.length < 5)) {
          return false;
        }
        if ((angular.equals($scope.rowContent.PersonalDetails.DOB, "")) || ($scope.rowContent.PersonalDetails.DOB.length < 10)) {
          return false;
        }
        if ((angular.equals($scope.rowContent.PersonalDetails.BloodGroup, ""))
            || !$scope.rowContent.PersonalDetails.BloodGroup.match(bloodGroupRegex)) {
          return false;
        }
        if (angular.equals($scope.rowContent.PersonalDetails.PassportNo, "")) {
          return false;
        }

        return true;
      }

      // ContactFormValidation
      $scope.ContactFormValidation = function() {
        if (angular.equals(($scope.rowContent.ContactDetails), (initialProfileObject.ContactDetails))) {
          return false;
        }
        if (((angular.equals($scope.rowContent.PersonalDetails.BloodGroup, "")) || ($scope.rowContent.ContactDetails.PersonalContact.length < 11)
            || ($scope.rowContent.ContactDetails.PersonalContact.length > 12) || !(isNumber($scope.rowContent.ContactDetails.PersonalContact)))) {
          return false;
        }
        if ((angular.equals($scope.rowContent.ContactDetails.OfficeContact, "") || ($scope.rowContent.ContactDetails.OfficeContact.length < 11)
            || ($scope.rowContent.ContactDetails.OfficeContact.length > 12) || !(isNumber($scope.rowContent.PersonalDetails.OfficeContact)))) {
          return false;
        }
        if ((angular.equals($scope.rowContent.ContactDetails.OtherContact, "") || ($scope.rowContent.ContactDetails.OtherContact.length < 11)
            || ($scope.rowContent.ContactDetails.OtherContact.length > 12) || !(isNumber($scope.rowContent.PersonalDetails.OtherContact)))) {
          return false;
        }
        return true;

      }

      // team validation
      $scope.TeamFormValidation = function() {
        if (angular.equals(($scope.rowContent.TeamDetails), (initialProfileObject.TeamDetails))) {
          return false;
        }

        return true;
      }

      // AddressForm validation
      $scope.AddressFormValidation = function() {
        if (angular.equals(($scope.rowContent.AddressDetails), (initialProfileObject.AddressDetails))) {
          return false;
        }
        if ((angular.equals($scope.rowContent.AddressDetails.HNo, "") || ($scope.rowContent.AddressDetails.HNo.length > 7))) {
          return false;
        }
        if (angular.equals($scope.rowContent.AddressDetails.Street, "")) {
          return false;
        }
        if (angular.equals($scope.rowContent.AddressDetails.City, "")) {
          return false;
        }
        if (angular.equals($scope.rowContent.AddressDetails.State, "")) {
          return false;
        }
        if (angular.equals($scope.rowContent.AddressDetails.Pin, "")) {
          return false;
        }
        if (angular.equals($scope.rowContent.AddressDetails.Country, "")) {
          return false;
        }
        return true;
      }
    });
