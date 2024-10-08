var App = angular.module("App", [
    "ngRoute", 'ngAnimate', 'ui.bootstrap'
]);

// configuration for routing to diffrent pages in App

App.config([
    '$routeProvider', function($routeProvider) {

      // here profile/:edit/:id changed
      $routeProvider.when("/profile/:edit/:id", {
        templateUrl : "../../../portal-includes/includes/profile.html",
        controller : 'ProfileController'
      }).when("/teams", {
        templateUrl : "../../../portal-includes/includes/teams.html",
        controller : 'TeamController'
      }).when("/employees", {
        templateUrl : "../../../portal-includes/includes/employees.html",
        controller : 'EmployeesController'
      }).when("/leavestatus", {
        templateUrl : "../../../portal-includes/includes/applyLeave.html",
        controller : 'LeavesController'
      }).when("/leavepolicypage", {
        templateUrl : "../../../portal-includes/includes/leavePolicyPage.html",
        controller : 'leavePolicyCtrl'
      }).when("/attrckr", {
        templateUrl : "../../../portal-includes/includes/attendanceTracker.html",
        controller : 'attendanceCtrl'
      }).when("/teamleaves", {
        templateUrl : "../../../portal-includes/includes/leaveReport.html",
        controller : 'employeeLeavesCtrl'
      }).when("/timepunch", {
        templateUrl : "../../../portal-includes/includes/manualTimeEntry.html",
      // }).when("/employeeleaves", {
      // templateUrl : "../../../portal-includes/includes/leaveReport.html",
      // controller : 'employeeLeavesCtrl'
      // }).when("/", {
      // templateUrl : "../../../portal-includes/includes/leavePolicyPage.html",
      // controller : 'leavePolicyCtrl'
      }).otherwise({
        templateUrl : "../../../portal-includes/includes/error.html"
      });
    }
]);

// error msg factory to get and set messages between different controller
App.factory('ErrorFactory', function() {
  var savedData = {};
  function setmsg(data) {
    savedData.msg = data;
  }
  function getmsg() {
    return savedData.msg;
  }
  function setURL(data) {
    savedData.URL = data;
  }
  function getURL() {
    return savedData.URL;
  }
  return {
    setmsg : setmsg,
    setURL : setURL,
    getmsg : getmsg,
    getURL : getURL
  }

});

/* Factories */
// factory for ajax hit
App.factory('httpHitFactory', function($rootScope, $http, ErrorFactory, $window) {
  var factoryObj = {};
  factoryObj.makeHTTPHit = function(url) {
    // Request has not been made, so make it with no url params

    return $http.get(url).then(function mysucess(response) {
      if (response && response.data && !response.data.isLogout) {
        return response;
      } else {
        $window.location.href = loggedOutUrl;
      }

    }, function error(response) {
      if (response && response.data && response.data.isLogout) {
        $window.location.href = loggedOutUrl;
      } else {
        return response;
      }
    });

  }

  // method to make http hit for typeahaed on UI
  factoryObj.makePOSTHTTPHit = function(uRL, val, content) {
    var contentType = undefined;
    if (content === 'application/json') {
      contentType = 'application/json';
    }
    var content;
    if (angular.isObject(val)) {
      content = val;
    } else {
      content = {};
      content.val = val;
    }

    return $http({
      url : uRL,
      method : 'POST',
      data : content,
      headers : {
        'Content-Type' : contentType
      }
    }).then(function mysucess(response) {
      if (response && response.data && !response.data.isLogout) {
        return response;
      } else {
        $window.location.href = loggedOutUrl;
      }

    }, function error(response) {
      if (response && response.data && response.data.isLogout) {
        $window.location.href = loggedOutUrl;
      } else {
        return response;
      }
    });
  }

  return factoryObj;
});

// Birthday Controller on home page
angular.module('App').controller("BirthdayController", function($rootScope, $scope, $location, httpHitFactory) {
  $scope.birthdayList = birthdayList;

  $scope.viewProfile = function(index) {
    try {
      $location.path("/profile/view/" + $scope.birthdayList[index].id);
    } catch (err) {
      PORTAL.showMessage("Something went wrong. Please contact the administrator");
    }
  }

  $scope.birthdayWishObject = {};
  var eventDialogElem = $("#birthday-wish-model");
  $scope.sendWishes = function(index) {
    try {
      console.log($scope.birthdayList[index]);
      $(eventDialogElem).dialog({
        modal : true,
        width : 550,
        closeOnEscape : false,
        open : function(event, ui) {
          $scope.birthdayWishObject = {};
          $scope.birthdayWishObject.to = $scope.birthdayList[index].name;
          $scope.birthdayWishObject.to_user_id = $scope.birthdayList[index].id;
        },
        buttons : {
          "Send" : function() {
            // overlay propoerty show set to true before ajax request is made
            $rootScope.overlay = {
              show : true
            }

            httpHitFactory.makePOSTHTTPHit(wishBirthdayUrl, $scope.birthdayWishObject).then(function(response) {
              try {
                if (!(response && response.data && response.data.status === "SUCCESS")) {
                  if (response.data.errorMsg) {
                    PORTAL.showMessage(response.data.errorMsg);
                  } else {
                    PORTAL.showMessage("Sorry! Could not send wishes.");
                  }
                } else if (response && response.data && response.data.status === "SUCCESS") {
                  PORTAL.showMessage("Birthday Wishes sent.");
                }

              } catch (err) {
                PORTAL.showMessage("Sorry! Could not send wishes.");
              }

              $(eventDialogElem).dialog("close");
              $rootScope.overlay.show = false;

            }, function myError(response) {
              $(eventDialogElem).dialog("close");
              $rootScope.overlay.show = false;
            });
          },
          "Discard" : function() {
            $(this).dialog("close");
          }
        }
      });
    } catch (err) {
      PORTAL.showMessage("Something went wrong. Please contact the administrator");
    }

  }

});

// error page controller
angular.module('App').controller("ErrorPageCtrl", function($scope, $location, ErrorFactory) {

  $scope.errormsg = ErrorFactory.getmsg();
  $scope.reloadURL = ErrorFactory.getURL();

  $scope.retryButton = function() {
    $location.url($scope.reloadURL);
  }
  $scope.homeButton = function() {
    $location.url('/');
  }
});

// employee search controller
angular.module('App').controller("EmployeeSearchCtrl", function($scope, $http, $location) {
  $scope.empId;

  $scope.getEmployees = function(val) {
    return $http({
      url : supervisorDataJsonURL,
      method : 'POST',
      data : "{\"val\": \"" + val + "\"}",
      headers : {
        'Content-Type' : 'application/json'
      }
    }).then(function(response) {
      if (response.data.employees) {
        return response.data.employees.map(function(item) {
          return item;
        });
      }
    }, function myError(response) {
      console.error('failure loading the  record', response.statusText, response.data);
    });
  };

  $scope.onSelect = function($item, $model, $label) {
    $scope._selectedEmp = $item;
  };

  $scope.goToView = function() {
    if (!$scope._selectedEmp.id) {
      return;
    }

    $location.path("/profile/view/" + $scope._selectedEmp.id);
    $scope._selectedEmp = {};
  }
});
