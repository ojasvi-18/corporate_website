//change this url with the url from the server that would be providing the json data for the user profile.

angular.module('App').controller("TeamController", function($scope, $http, $routeParams, $location, $rootScope, ErrorFactory, httpHitFactory) {

  // overlay propoerty show set to true before ajax request is made
  $rootScope.overlay = {
    show : true
  }

  $scope.teamsList = [];
  $scope.supervisorList;
  $scope.selectedTeamData = {};

  /*
   * UI actions jsonURLs 1. userTeamDataJsonURL : for fetching teams data to
   * show 2. addTeamDataJsonURl === userTeamDataJsonURL + add : url for
   * submitting new team 3. editTeamDataJsonURL === userTeamDataJsonURL + save :
   * url to make hit to save edit data 4. deleteTeamDataJSonURL ===
   * userTeamDataJsonURL + delete: url to hit for delete team
   */

  var addTeamDataJsonURl = userTeamDataJsonURL + "/add";
  var editTeamDataJsonURl = userTeamDataJsonURL + "/update";
  var deleteTeamDataJSonURL = userTeamDataJsonURL + "/delete";
  var getTeamMemberJsonURl = userTeamDataJsonURL + "/members";

  var saveChangesToTeamURL = userTeamDataJsonURL + "/updateMembers";

  // httpHitFactory.makeHTTPHit() is a factory method whic will make an
  // http.get() hit and is present in app.js
  httpHitFactory.makeHTTPHit(userTeamDataJsonURL) // reading json file
  .then(function mySuccess(response) {
    $scope.isAdmin = response.data.isAdmin;
    $scope.teamsList = response.data.TeamList;
    $scope.dispTeamStatus = {
      isNameValid : true,
      isEmailValid : true,
      isSupervisorValid : true
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

    if (response.status && response.statusText) {

      ErrorFactory.setmsg(response.statusText);
      ErrorFactory.setURL($location.url());
      // redirecting to error page if json http request return error
      $location.url('/error.html');
    }
  });

  $scope.sortTeamProperty;
  $scope.reverse = false;

  $scope.sortTeamBy = function(propertyName) {
    $scope.reverse = ($scope.sortTeamProperty === propertyName) ? !$scope.reverse : true;
    $scope.sortTeamProperty = propertyName;
  };

  // typeahead for Supervisor add in team.html
  $scope.onSelectForEdit = function($item, $model, $label) {
    $scope.selectedTeamData.Supervisor = $item;
    $scope.isValidSupervisor(-1);
  };

  $scope.onSelectForAdd = function($item, $model, $label) {
    $scope.newTeam.Supervisor = $item;
    $scope.isValidSupervisor();
  };

  // this method return SUPERVISOR name prepopulated on ui (type ahead)
  // this method will hit an http post request and will make a post request by
  // calling httpHitFactory.makePOSTHTTPHit() defined in app.js
  $scope.getSupervisorName = function(val) {
    return httpHitFactory.makePOSTHTTPHit(supervisorDataJsonURL, val).then(function(response) {
      if (response.data.employees) {
        return response.data.employees.map(function(item) {
          return item;
        });
      }
    }, function myError(response) {

      // TODO: do something(handle exceptions)
      console.log(response);
    });
  };

  // validate editing team data

  /*
   * template for team edit/delete/display
   */
  $scope.getTemplate = function(teamsData) {

    // for new team added. Ideally this id should be populated from the json
    // result from the server.
    if (teamsData.id == null) {
      return 'display';
    }

    if (teamsData.id === $scope.selectedTeamData.id) {
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

  this.isDuplicateName = function(newTeamName, completeTeams, idx) {
    for (var i = 0; i < completeTeams.length; i++) {
      if (i != idx) {
        if (newTeamName === completeTeams[i].TeamName) {
          return true;
        }
      }

    }
    return false;
  }
  var isDuplicateName = function(newTeamName, completeTeams, idx) {
    for (var i = 0; i < completeTeams.length; i++) {
      if (i != idx) {
        if (newTeamName === completeTeams[i].TeamName) {
          return true;
        }
      }

    }
    return false;
  }
  var isDuplicateEmail = function(newTeamEmail, completeTeams, idx) {
    for (var i = 0; i < completeTeams.length; i++) {
      if (i != idx) {
        if (newTeamEmail === completeTeams[i].EmailGroups) {
          return true;
        }
      }

    }
    return false;
  };

  $scope.resetNewTeamBlock = function() {
    $scope.newTeam = {};
    $scope.isEmailValid = false;
    $scope.isTeamNameValid = false;
    $scope.isSupervisorValid = false;
    $scope.isNew = true;
  }

  // make sure all the values are pristing in starting;
  $scope.resetNewTeamBlock();

  var isTeamNameValid = function(name) {
    if (PORTAL.isValueUndefined(name)) {
      return false;
    } else {
      return (PORTAL.isValidString(name) || isDuplicateName(name, $scope.teamsList, -1));
    }
  }

  var isEmailValid = function(email) {
    if (PORTAL.isValueUndefined(email)) {
      return false;
    }

    return (PORTAL.validateEmail(email) || isDuplicateEmail(email, $scope.teamsList));
  }

  $scope.isValidTeamName = function(id) {
    var name;
    if (id == null) {
      $scope.isNew = false;
      name = $scope.newTeam.TeamName;
      $scope.isTeamNameValid = isTeamNameValid(name);
    } else {
      name = $scope.selectedTeamData.TeamName;
      $scope.dispTeamStatus.isNameValid = isTeamNameValid(name);
    }
  }

  $scope.isValidEmail = function(id) {
    var email;
    if (id == null) {
      $scope.isNew = false;
      email = $scope.newTeam.EmailGroups;
      $scope.isEmailValid = isEmailValid(email);
    } else {
      email = $scope.selectedTeamData.EmailGroups;
      $scope.dispTeamStatus.isEmailValid = isEmailValid(email);
    }
  }

  var isSupervisorValid = function(supervisor) {

    // No supervisor
    if (!supervisor) {
      return false;
    }

    if (!supervisor.id || !supervisor.name) {
      return false;
    }

    return true;
  }

  $scope.isValidSupervisor = function(id) {
    var Supervisor;
    if (id == null) {
      $scope.isNew = false;
      Supervisor = $scope.newTeam.Supervisor;
      $scope.isSupervisorValid = isSupervisorValid(Supervisor);
    } else {
      Supervisor = $scope.selectedTeamData.Supervisor;
      $scope.dispTeamStatus.isSupervisorValid = isSupervisorValid(Supervisor);
    }
  }

  // check of submit button disabled
  $scope.canSubmit = function() {
    if ($scope.isTeamNameValid && $scope.isEmailValid && $scope.isSupervisorValid && $scope.isAdmin) {
      return true;
    } else {
      return false;
    }
  }

  // function edit and save already present team data
  $scope.editTeamData = function(selectedTeam) {
    $scope.selectedTeamData = angular.copy(selectedTeam);
    $scope.dispTeamStatus = {
      isNameValid : true,
      isEmailValid : true,
      isSupervisorValid : true
    };
  }

  // this method will udate team data when team data is edited
  $scope.saveEdits = function(idx) {
    // setting overlay true
    $rootScope.overlay = {
      show : true
    }
    // this method will make an http.post request.
    // httpHitFactory.makePOSTHTTPHit() will return a response of post hit and
    // is defined in app.js
    if ($scope.isAdmin) {
      httpHitFactory.makePOSTHTTPHit(editTeamDataJsonURl, $scope.selectedTeamData).then(function mysuccess(response) {
        if (response && response.data && response.data.status === "SUCCESS") {
          for (var idx = 0; idx < $scope.teamsList.length; idx++) {
            if ($scope.teamsList[idx].id === $scope.selectedTeamData.id) {
              $scope.teamsList[idx] = angular.copy($scope.selectedTeamData);
              $scope.reset();
              break;
            }
          }
          $rootScope.overlay = {
            show : false
          }
        }
      }, function error(response) {
        if (response && respose.data && response.data.status === "ERROR") {
          $rootScope.overlay.show = false;
          alert(response.data.status)
        }
      });
    } else {
      alert("Not an Authorized user to perform action");
    }
  }

  $scope.cancelEdits = function(idx) {
    $scope.selectedTeamData = {};
  }

  $scope.reset = function() {
    $scope.selectedTeamData = {};
  };

  $scope.deleteTeam = function(index) {
    $rootScope.overlay.show = true;
    var isDelete = confirm("Do you want to delete this team? This will delete all the members from the system as well");
    if ((isDelete === true) && $scope.isAdmin) {
      // this method will make an http.post hit and request a response form
      // httpHitFactory.makePOSTHTTPHit() factory method present in app.js
      httpHitFactory.makePOSTHTTPHit(deleteTeamDataJSonURL, $scope.teamsList[index]).then(function mysuccess(response) {
        if (response && response.data && response.data.status === "SUCCESS") {
          $scope.teamsList.splice(index, 1);
        } else {
          alert("Could not delete team. Please contact the administrator");
        }

        $rootScope.overlay.show = false;

      }, function error(response) {

        $rootScope.overlay.show = false;
        if (response && response.data && response.data.status === "ERROR") {
          ErrorFactory.setmsg(data.data);
          ErrorFactory.setURL($location.url());
          // redirecting to error page if json http request return error
          $location.url('/error.html');
        }

      });
    } else {
      $rootScope.overlay.show = false;
      if (isDelete === true) {
        alert("Not an authorized user to perform the action");
      }
    }

    // if (index < 0 || index >= $scope.teamsList.length) {
    // alert("Something gone wrong");
    // } else {
    // var isDelete = confirm("Do you want to delete this team?");
    // if (isDelete === true) {
    // // make an ajax hit to the server
    // $scope.teamsList.splice(index, 1);
    // }
    // }
  };

  // TODO: remove this after getting results from json
  let
  id = 1000;

  $scope.submitNewTeam = function() {
    $rootScope.overlay.show = true;

    // ........ submit button ajax hit when a new teamm is submitted
    if ($scope.isAdmin) {
      httpHitFactory.makePOSTHTTPHit(addTeamDataJsonURl, $scope.newTeam).then(function mysuccess(data, headers, config) {
        var responseData = data.data;
        if (responseData && responseData && responseData.status === "SUCCESS") {
          $scope.newTeam.id = responseData.id;
          $scope.newTeam.numMembers = 0;
          $scope.teamsList.push($scope.newTeam);
          $scope.resetNewTeamBlock();
        }
        $rootScope.overlay.show = false;
      }, function error(response) {
        // HIDING overlay
        $rootScope.overlay.show = false;
        if (responseData) {
          ErrorFactory.setmsg(data.data);
          ErrorFactory.setURL($location.url());
          // redirecting to error page if json http request return error
          $location.url('/error.html');
        }
      });
    } else {
      alert("Not an authorized to perfrm the action");
    }
  }

  $scope.lastTeamMembers = -1;

  $scope.fetchMembers = function(teamId) {

    if (teamId === $scope.lastTeamMembers) {
      return;
    }

    $scope.teamMembers = [];
    $rootScope.overlay = {
      show : true
    }

    httpHitFactory.makePOSTHTTPHit(getTeamMemberJsonURl, {
      "id" : teamId
    }).then(function mysuccess(response) {
      if (response && response.data && response.data.status === "SUCCESS") {
        $scope.teamMembers = response.data.members;
      }
      $rootScope.overlay.show = false;
    }, function error(response) {
      $rootScope.overlay.show = false;
      if (response && respose.data && response.data.errorMsg) {
        PORTAL.showMessage(response.data.errorMsg);
      }
    });

    $scope.lastTeamMembers = teamId;
  }

  // Manage members tab

  $scope.lastTeamServiced = -1;
  $scope.allEmployees = [];

  $scope.goToManageMembers = function(team) {
    PORTAL.switchToTab("manageMembers");
    $scope.selectTeam(team);
  }

  $scope.selectTeam = function(teamObj) {

    $scope.selectedTeam = angular.copy(teamObj);
    var teamId = teamObj.id;

    if (teamId === $scope.lastTeamServiced) {
      return;
    }

    $rootScope.overlay = {
      show : true
    }

    $scope.members = [];
    $scope.employeesWithoutMembers = [];

    var requestData = {
      "id" : teamId
    };

    if ($scope.allEmployees.length == 0) {
      requestData.getEmployees = true;
    }

    httpHitFactory.makePOSTHTTPHit(getTeamMemberJsonURl, requestData).then(function mysuccess(response) {
      if (response && response.data && response.data.status === "SUCCESS") {
        $scope.members = response.data.members;

        if (response.data.employees) {
          $scope.allEmployees = response.data.employees;
        }
        $scope.employeesWithoutMembers = [];
        for (var i = 0; i < $scope.allEmployees.length; i++) {
          var emp = $scope.allEmployees[i];
          var flag = false;
          for (var j = 0; j < $scope.members.length; j++) {
            var member = $scope.members[j];
            if (member.id === emp.id) {
              flag = true;
              break;
            }
          }

          if (flag === false) {
            $scope.employeesWithoutMembers.push(angular.copy(emp));
          }
        }
      }
      $rootScope.overlay.show = false;
    }, function error(response) {
      $rootScope.overlay.show = false;
      if (response && respose.data && response.data.errorMsg) {
        PORTAL.showMessage(response.data.errorMsg);
      }
    });

    $scope.lastTeamServiced = teamId;

  }

  $scope.addToTeamArray = [];
  $scope.removeFromTeamArray = [];

  $scope.removeFromTeam = function(empId) {
    for (var i = 0; i < $scope.members.length; i++) {
      var emp = $scope.members[i];
      var empToBeRemoved = null;
      if (emp.id === empId) {
        empToBeRemoved = angular.copy(emp);
        $scope.members.splice(i, 1);
        $scope.removeFromTeamArray.push(empToBeRemoved);
      }

    }
  }

  $scope.undoRemoveFromTeam = function(empId) {
    for (var i = 0; i < $scope.removeFromTeamArray.length; i++) {
      var emp = $scope.removeFromTeamArray[i];
      var empToBeRemoved = null;
      if (emp.id === empId) {
        empToBeRemoved = angular.copy(emp);
        $scope.removeFromTeamArray.splice(i, 1);
        $scope.members.push(empToBeRemoved);
      }

    }
  }

  $scope.addToTeam = function(empId) {
    for (var i = 0; i < $scope.employeesWithoutMembers.length; i++) {
      var emp = $scope.employeesWithoutMembers[i];
      var empToBeAdded = null;
      if (emp.id === empId) {
        empToBeAdded = angular.copy(emp);
        $scope.employeesWithoutMembers.splice(i, 1);
        $scope.addToTeamArray.push(empToBeAdded);
      }

    }
  }

  $scope.undoAddToTeam = function(empId) {
    for (var i = 0; i < $scope.addToTeamArray.length; i++) {
      var emp = $scope.addToTeamArray[i];
      var empToBeAdded = null;
      if (emp.id === empId) {
        empToBeAdded = angular.copy(emp);
        $scope.addToTeamArray.splice(i, 1);
        $scope.employeesWithoutMembers.push(empToBeAdded);
      }
    }
  }

  $scope.saveChanges = function() {
    if ($scope.addToTeamArray.length == 0 && $scope.removeFromTeamArray.length == 0) {
      PORTAL.showMessage("No Changes To Save");
      return;
    }

    var requestData = {
      "id" : $scope.selectedTeam.id
    }

    if ($scope.addToTeamArray.length > 0) {
      requestData.addToTeam = $scope.addToTeamArray;
    }

    if ($scope.removeFromTeamArray.length > 0) {
      requestData.removeFromTeamArray = $scope.removeFromTeamArray;
    }

    httpHitFactory.makePOSTHTTPHit(saveChangesToTeamURL, requestData).then(function mysuccess(response) {
      if (response && response.data && response.data.status === "SUCCESS") {
        // get team info as json and replace in the original array
        var team = response.data.team;
        for (var i = 0; i < $scope.teamsList.length; i++) {
          var origTeam = $scope.teamsList[i];
          if (origTeam.id === team.id) {
            $scope.teamsList[i] = angular.copy(team);
            break;
          }
        }

        delete $scope.selectedTeam;
        delete $scope.members;
        $scope.employeesWithoutMembers = angular.copy($scope.allEmployees);
        $scope.removeFromTeamArray = [];
        $scope.addToTeamArray = [];
        $scope.lastTeamServiced = -1;
        PORTAL.switchToTab("manageteams");

      } else {
        if (response && response.data && response.data.errorMessage) {
          ErrorFactory.setmsg(response.data.errorMsg);
        } else {
          ErrorFactory.setmsg("Error in saving changes to the team. Please contact the system administrator");
        }

        ErrorFactory.setURL($location.url());
        // redirecting to error page if json http request return error
        $location.url('/error.html');
      }

      $rootScope.overlay.show = false;
    }, function error(response) {
      // HIDING overlay
      $rootScope.overlay.show = false;

      if (response && response.data && response.data.errorMessage) {
        ErrorFactory.setmsg(response.data.errorMsg);
      } else {
        ErrorFactory.setmsg("Error in saving changes to the team. Please contact the system administrator");
      }

      ErrorFactory.setURL($location.url());
      // redirecting to error page if json http request return error
      $location.url('/error.html');
    });

  }

});
