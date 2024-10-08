//change this url with the url from the server that would be providing the json data for the user profile.

angular.module('App').controller("EmployeesController", function($scope, $http, $routeParams, $location, $rootScope, ErrorFactory, httpHitFactory) {

  // this variable is for holding the content-type requird for post hit and is
  // used in every post hit
  var contentType = 'application/json';

  // overlay propoerty show set to true before ajax request is made
  $rootScope.overlay = {
    show : true
  }

  $scope.empList;
  $scope.selectedEmployee = {};

  /*
   * UI actions jsonURLs 1. usersDataJsonURL : for fetching teams data to show
   * 2. addEmployeeDataJsonURl === usersDataJsonURL + add : url for submitting
   * new team
   */

  var addEmployeeDataJsonURl = usersDataJsonURL + "/add";
  var editEmployeeDataJsonURl = usersDataJsonURL + "/update";

  // reading json file(HTTP GET HIT)
  httpHitFactory.makeHTTPHit(usersDataJsonURL).then(function mySuccess(response) {
    $scope.empList = response.data.empList;
    $scope.isAdmin = response.data.isAdmin;

    $scope.dispEmpStatus = {
      isEmpIdValid : true,
      isRoleValid : true,
      isNameValid : true,
      isStatusValid : true,
      isEmailValid : true,
      isDeviceIdValid : true,
      isPasswordIdValid : true
    };

    // function to hide overlay
    $rootScope.overlay = {
      show : false
    }
  }, function myError(response) {
    // HIDING overlay
    $rootScope.overlay = {
      show : false
    }
    // TODO:change this status to proper error message
    if (response.status) {

      ErrorFactory.setmsg("Error");
      ErrorFactory.setURL($location.url());
      // redirecting to error page if json http request return error
      $location.url('/error.html');
    }
  });

  $scope.getAllStatus = function() {
    return empStatus.map(function(item) {
      return item;
    });
  }
  $scope.getAllRoles = function() {
    return roles.map(function(item) {
      return item;
    });
  }
  $scope.sortProperty;
  $scope.reverse = false;

  $scope.sortBy = function(propertyName) {
    $scope.reverse = ($scope.sortProperty === propertyName) ? !$scope.reverse : true;
    $scope.sortProperty = propertyName;
  };

  // validate editing data

  /*
   * template for team edit/delete/display
   */
  $scope.getTemplate = function(empData) {

    // for new team added. Ideally this id should be populated from the json
    // result from the server.
    if (empData.id == null) {
      return 'display';
    }

    if (empData.id === $scope.selectedEmployee.id) {
      return 'edit';
    } else {
      return 'display';
    }
  };
  /*
   * template ends
   */

  /*
   * function for validation email re-occurance name reoccurance and other
   * validations of add teams of teams.html
   */

  isDuplicateName = function(newTeamName, completeTeams, idx) {
    for (var i = 0; i < completeTeams.length; i++) {
      if (i != idx) {
        if (newTeamName === completeTeams[i].TeamName) {
          return true;
        }
      }

    }
    return false;
  }

  isDuplicateEmail = function(newTeamEmail, completeTeams, idx) {
    for (var i = 0; i < completeTeams.length; i++) {
      if (i != idx) {
        if (newTeamEmail === completeTeams[i].EmailGroups) {
          return true;
        }
      }

    }
    return false;
  };

  $scope.resetNewEmpBlock = function() {
    $scope.newEmp = {};
    $scope.newEmpStatus = {
      isEmpIdValid : true,
      isRoleValid : false,
      isNameValid : false,
      isStatusValid : false,
      isEmailValid : false,
      isDeviceIdValid : false,
      isPasswordIdValid : true
    };
    $scope.isNew = true;
  }

  // make sure all the values are pristing in starting;
  $scope.resetNewEmpBlock();

  var isPasswordValid = function(pass) {
    if (PORTAL.isValueUndefined(pass)) {
      return false;
    } else {
      return true;
    }
  }

  var isEmpIdValid = function(empId) {
    if (PORTAL.isEmptyString(empId) || PORTAL.isValueUndefined(empId) || PORTAL.isNotAValidNumber(empId)) {
      return false;
    } else {
      for (var i = 0; i < $scope.empList.length; i++) {
        if ($scope.empList[i].id == empId) {
          return false;
        }
      }
      return true;
    }
  }
  var isNameValid = function(name) {
    if (PORTAL.isValueUndefined(name)) {
      return false;
    } else {
      return (PORTAL.isValidString(name, 5, 25) || isDuplicateName(name, $scope.empList, -1));
    }
  }

  var isEmailValid = function(email) {
    if (PORTAL.isValueUndefined(email)) {
      return false;
    }

    return (PORTAL.validateEmail(email) || isDuplicateEmail(email, $scope.empList));
  }

  var isIDValid = function(dId, index) {
    if (PORTAL.isEmptyString(dId) || PORTAL.isValueUndefined(dId) || PORTAL.isNotAValidNumber(dId)) {
      return false;
    } else {
      for (var i = 0; i < $scope.empList.length; i++) {
        if (!PORTAL.isValueUndefined(index) && $scope.empList[i].id === index) {
          continue;
        }
        if ($scope.empList[i].d_id == dId) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  var isRoleValid = function(role) {
    if (!role) {
      return false;
    }

    for (var i = 0; i < roles.length; i++) {
      var uRole = roles[i];
      if (uRole.code === role.code && uRole.name === role.name) {
        return true;
      }
    }

    return false;
  }

  var isStatusValid = function(status) {
    if (!status) {
      return false;
    }

    for (var i = 0; i < empStatus.length; i++) {
      var uStatus = empStatus[i];
      if (uStatus.id === status.id && uStatus.status === status.status) {
        return true;
      }
    }

    return false;
  }

  $scope.isValidDeviceId = function(index) {
    var id;
    if (index >= 0) {
      id = $scope.selectedEmployee.d_id;
      var empId = $scope.selectedEmployee.id;
      $scope.dispEmpStatus.isDeviceIdValid = isIDValid(id, empId);
    } else {
      $scope.isNew = false;
      id = $scope.newEmp.d_id;
      $scope.newEmpStatus.isDeviceIdValid = isIDValid(id);
    }
  }

  $scope.isValidRole = function(index) {
    var role;
    if (index >= 0) {
      role = $scope.selectedEmployee.role;
      $scope.dispEmpStatus.isRoleValid = isRoleValid(role);
    } else {
      $scope.isNew = false;
      role = $scope.newEmp.role;
      $scope.newEmpStatus.isRoleValid = isRoleValid(role);
    }
  }

  $scope.isValidPassword = function(index) {
    var password;
    if (!index) {
      $scope.isNew = false;
      password = $scope.newEmp.password;
      $scope.newEmpStatus.isPasswordIdValid = isPasswordValid(password);
    } else {
      password = $scope.selectedEmployee.password;
      $scope.dispEmpStatus.isPasswordIdValid = isPasswordValid(password);
    }
  }

  $scope.isValidName = function(index) {
    var name;
    if (!index) {
      $scope.isNew = false;
      name = $scope.newEmp.name;
      $scope.newEmpStatus.isNameValid = isNameValid(name);
    } else {
      name = $scope.selectedEmployee.name;
      $scope.dispEmpStatus.isNameValid = isNameValid(name);
    }
  }

  $scope.isValidEmail = function(index) {
    var email;
    if (!index) {
      $scope.isNew = false;
      email = $scope.newEmp.email;
      $scope.newEmpStatus.isEmailValid = isEmailValid(email);
    } else {
      email = $scope.selectedEmployee.email;
      $scope.dispEmpStatus.isEmailValid = isEmailValid(email);
    }
  }

  $scope.isValidEmpId = function() {
    var empId;
    $scope.isNew = false;
    empId = $scope.newEmp.id;
    $scope.newEmpStatus.isEmpIdValid = isEmpIdValid(empId);
  }

  $scope.isValidStatus = function(index) {
    var status;
    if (index >= 0) {
      status = $scope.selectedEmployee.status;
      $scope.dispEmpStatus.isStatusValid = isStatusValid(status);
    } else {
      $scope.isNew = false;
      status = $scope.newEmp.status;
      $scope.newEmpStatus.isStatusValid = isStatusValid(status);
    }
  }

  function checkIfAllValid(obj) {
    for ( var key in obj) {
      if (obj[key] === false) {
        return false;
      }
    }

    return true;
  }

  // check of submit button disabled
  $scope.canSubmit = function() {
    return checkIfAllValid($scope.newEmpStatus);
  }

  $scope.isAllFieldsValid = function() {
    return checkIfAllValid($scope.dispEmpStatus);
  }

  $scope.viewProfile = function(emp) {
    if (!emp || !emp.id) {
      return;
    }

    $location.path("/profile/view/" + emp.id);
  }

  // function edit and save already present team data
  $scope.editEmpData = function(selectedEmp) {
    $scope.selectedEmployee = angular.copy(selectedEmp);
    $scope.dispEmpStatus = {
      isEmpIdValid : true,
      isRoleValid : true,
      isNameValid : true,
      isStatusValid : true,
      isEmailValid : true,
      isDeviceIdValid : true,
      isPasswordIdValid : true,
    };
  }

  // THIS METHOD IS USED TO SAVE THE EDIT DATA OF EMPLOYEE PRESENT
  $scope.saveEdits = function() {

    httpHitFactory.makePOSTHTTPHit(editEmployeeDataJsonURl, $scope.selectedEmployee, contentType).then(function mysuccess(response) {
      $rootScope.overlay = {
        show : true
      }

      var isError = false;
      if (response && response.data) {
        if (response.data.status === "SUCCESS") {
          for (var idx = 0; idx < $scope.empList.length; idx++) {
            if ($scope.empList[idx].id === $scope.selectedEmployee.id) {
              $scope.empList[idx] = angular.copy($scope.selectedEmployee);
              $scope.reset();
              break;
            }
          }
        } else {
          if (response.data.errorMsg) {
            ErrorFactory.setmsg(response.data.errorMsg);
          }
          isError = true;
        }
      } else {
        ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
        isError = true;
      }

      if (isError === true) {
        ErrorFactory.setURL($location.url());
        $location.url('/error.html');
      }

      $rootScope.overlay.show = false;
    }, function error(response) {
      if (response && response.data && response.data.errorMessage) {
        ErrorFactory.setmsg(response.data.errorMsg);
      } else {
        ErrorFactory.setmsg("Error in adding employee. Try logging in again. Contact System Admin if problem persists");
      }

      ErrorFactory.setURL($location.url());
      // redirecting to error page if json http request return error
      $location.url('/error.html');
      // HIDING overlay
      $rootScope.overlay = {
        show : false
      }
    });
  }

  $scope.cancelEdits = function() {
    $scope.selectedEmployee = {};
  }

  $scope.reset = function() {
    $scope.selectedEmployee = {};
  };

  // THIS METHOD IS USED TO SUBMIT DETAILS OF NEW EMPLOYEE TO THE DATA BASE
  $scope.submitNewEmployee = function() {
    $rootScope.overlay = {
      show : true
    }
    // use $http to make ajax hit to the server and use the status code to
    // decide whether to push data or not
    httpHitFactory.makePOSTHTTPHit(addEmployeeDataJsonURl, $scope.newEmp, contentType).then(function mysuccess(response) {
      var isError = false;
      if (response && response.data) {
        if (response.data.status === "SUCCESS") {
          $scope.newEmp.id = response.data.id;
          $scope.empList.push($scope.newEmp);
          $scope.resetNewEmpBlock();
        } else {
          if (response.data.errorMsg) {
            ErrorFactory.setmsg(response.data.errorMsg);
          }
          isError = true;
        }
      } else {
        ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
        isError = true;
      }

      if (isError === true) {
        ErrorFactory.setURL($location.url());
        $location.url('/error.html');
      }

      $rootScope.overlay.show = false;
    }, function error(response) {

      // HIDING overlay
      $rootScope.overlay.show = false;

      if (response && response.data && response.data.errorMessage) {
        ErrorFactory.setmsg(response.data.errorMsg);
      } else {
        ErrorFactory.setmsg("Error in adding employee. Try logging in again. Contact System Admin if problem persists");
      }

      ErrorFactory.setURL($location.url());
      // redirecting to error page if json http request return error
      $location.url('/error.html');
    });
  }
});
