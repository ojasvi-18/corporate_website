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
angular.module('App').controller(
    "LeavesController",
    function($scope, $http, $routeParams, $location, $rootScope, ErrorFactory, httpHitFactory) {

      // this variable is for holding the content-type requird for post hit and
      // is used in every post hit
      var contentType = 'application/json';

      // UI ACTIONS
      var leaveRequestBaseURL = leaveRequestsUrl;
      var editLeaveRequest = leaveRequestBaseURL + "/update";
      var addLeaveRequestsUrl = leaveRequestBaseURL + "/add";
      var deleteRaisedRequestUrl = leaveRequestBaseURL + "/delete";
      var leaveRequestStatusChangeURL = leaveRequestBaseURL + "/updateStatus";
      var sendLeaveReminderUrl = leaveRequestBaseURL + "/sendReminder";
      // UI actions end

      $scope.leavesRequestStatus = leavesRequestStatus;

      $scope.getLeaveRequestStatus = function() {
        return $scope.leavesRequestStatus.map(function(item) {
          return item.status;
        });
      }

      // this object holds the login user_id and it is required while making
      // post
      // hit at the time of loading of page
      $scope.userLeaveRequestData = {
        user_id : loggedInUserId,
      }

      var date = new Date();
      var curryear = date.getFullYear();
      $scope.year = "" + curryear;

      $scope.isValidYear = function() {

        if (!$scope.year) {
          return false;
        }

        if ($scope.previousYear) {
          if ($scope.year === $scope.previousYear) {
            return false;
          }
        }
        return $scope.year.length === 4 && !PORTAL.isNotAValidNumber($scope.year);
      }

      // this will hold the changed status of the request which are raised by
      // others
      $scope.changedStatus = {};

      // this will hold the data of selected leave request of user from leave
      // calendar
      $scope.selectedleaveRequestData = {};

      // this holds the object of leave_type of drop down made on UI
      $scope.resetModel = function(idx) {
        $scope.newLeave = {};
        $scope.hasAdditionalComment = false;
      }

      // THIS CALL RESET THE NEW LEAVE MODEL ON UI
      $scope.resetModel();

      // this represents the list of leave request pending for approval of other
      // users for
      // approval
      /*
       * FORMAT OF REQUEST
       * 
       * $scope.leaveRequestsList = [ { id : 1, status : "un-approved", noOfDays :
       * 8, startDate : "21-12-2017", endDate : "29-12-2017", reason : "trip" } ];
       */
      /*
       * THIS IS A TEMPLATE METHOD WHICH WILL RETURN ON UI WETHER "VIEW"
       * FUCNTIONALITY TO BE SHOWN OR "EDIT" FUNCTIONALITY
       */
      $scope.getTemplate = function(leaveRequestData) {

        // for new team added. Ideally this id should be populated from the json
        // result from the server.
        // ;
        if (leaveRequestData.id == null) {
          return 'display';
        }

        if (leaveRequestData.id === $scope.selectedleaveRequestData.id) {
          return 'edit';
        } else {
          return 'display';
        }
      };

      $scope.saveEditedRequest = function(updateData) {

        for (var i = 0; i < $scope.appliedLeaves.length; i++) {
          if ($scope.appliedLeaves[i].leave_id === updateData.leave_id) {
            $scope.appliedLeaves[i] = angular.copy(updateData);

            // Re-draw the charts
            $scope.regroupLeavesData();

            break;
          }
        }

      };

      $scope.renderSubmittedRequest = function(data) {
        if (data) {
          $scope.appliedLeaves.push(data);

          // Re-draw the charts
          $scope.regroupLeavesData();

          var color;
          for (var i = 0; i < $scope.leavesRequestStatus.length; i++) {
            if (data.status === $scope.leavesRequestStatus[i].status) {
              color = $scope.leavesRequestStatus[i].color;
              break;
            }
          }

          var calData = {
            leave_id : data.leave_id,
            leave_type : data.leave_type,
            start : data.start,
            end : data.end,
            reason : data.reason,
            status : data.status,
            backgroundColor : color,
            title : data.title,
            days : data.days
          };

          if (data.canRemind) {
            calData.canRemind = data.canRemind;
          } else {
            calData.canRemind = false;
          }

          $scope.calendar.fullCalendar('renderEvent', calData);

          PORTAL.showMessage("New Event Added");
        }

      };

      $scope.deleteSelectedRequest = function(leave_id_deleted) {
        for (var i = 0; i < $scope.appliedLeaves.length; i++) {
          if ($scope.appliedLeaves[i].leave_id === leave_id_deleted) {
            $scope.appliedLeaves.splice(i, 1);

            // Re-draw the charts
            $scope.regroupLeavesData();
          }
        }
      }

      // VALIDATION FOR REAISING REQUEST BEGINS
      var validateLeaveDetails = function(newLeaveRequest) {

        if (!PORTAL.validateDate(newLeaveRequest.start)) {
          return false;
        }

        if (!PORTAL.validateDate(newLeaveRequest.end)) {
          return false;
        }

        if (newLeaveRequest.start > newLeaveRequest.end) {
          return false;
        }

        if (PORTAL.isValueUndefined(newLeaveRequest.leave_type)) {
          return false;
        }

        if (!PORTAL.isValidString(newLeaveRequest.reason, 5, 255)) {
          return false;
        }

        if (newLeaveRequest.start > newLeaveRequest.end) {
          return false;
        }

        var currDate = new Date();
        if (newLeaveRequest.start < currDate) {
          return false;
        }

        return true;
      }

      /*
       * Java Script code for calendar FUNCTIONALITY PRESENT: ADD, UPDATE,
       * DELETE , elem IS THE ELEMENT OF CALENDAR , EVENTJSON IS THE EVENT LIST
       * FROM BACKEND
       */

      // APPLY LEAVES CALENDAR FUNCTIONALITY BEGINS
      $scope.createCalendar = function(elem, eventsJSON, date) {
        var eventDialogElem = $("#manage_leaves");
        $scope.calendar = $(elem).fullCalendar(
            {
              defaultDate : date,
              header : {
                left : 'prev,next',
                center : 'title',
                right : 'month,basicWeek,basicDay'
              },
              theme : true,
              events : eventsJSON,
              selectable : true,
              selectHelper : true,
              select : function(start, end, allDay) {
                var currDate = new Date();
                currDate = PORTAL.formatDate(currDate, 'YYYY-MM-DD')
                var startDate = start.format();
                // var startDate = start.format();
                // START DATE IS GREATER THAN && EQUAL TO CURRENT DATE THEN ONLY
                // USER CAN APPLY LEAVES OTHERWISE NOT
                // if (startDate >= currDate) {
                $(eventDialogElem).dialog({
                  modal : true,
                  width : 550,
                  closeOnEscape : false,
                  open : function(event, ui) {
                    $scope.$apply(function() {
                      $scope.resetModel();
                      $scope.newLeave.reason = "";
                      $scope.newLeave.start = start.format();
                      $scope.newLeave.end = ((end === null) ? start.format() : end.format());
                    });

                  },
                  buttons : {
                    "Add" : function() {
                      var newApplyLeaveRequest = {
                        user_id : $scope.userLeaveRequestData.user_id,
                        leave_type : $scope.newLeave.leave_type,
                        start : $scope.newLeave.start,
                        end : $scope.newLeave.end,
                        reason : $scope.newLeave.reason
                      }

                      if (validateLeaveDetails(newApplyLeaveRequest)) {
                        httpHitFactory.makePOSTHTTPHit(addLeaveRequestsUrl, newApplyLeaveRequest, contentType).then(function(response) {

                          if (response && response.data && response.data.status === "SUCCESS") {
                            newApplyLeaveRequest.leave_id = response.data.leave_info.leave_id;
                            newApplyLeaveRequest.status = response.data.leave_info.status;
                            newApplyLeaveRequest.title = response.data.leave_info.title;
                            newApplyLeaveRequest.days = response.data.leave_info.days;
                            newApplyLeaveRequest.canRemind = response.data.leave_info.canRemind;
                            newApplyLeaveRequest.backgroundColor = response.data.leave_info.backgroundColor;
                            $scope.renderSubmittedRequest(newApplyLeaveRequest);

                            $(eventDialogElem).dialog("close");
                            $scope.calendar.fullCalendar('unselect');
                            

                            $rootScope.overlay.show = false;
                            
                          } else {
                            $scope.renderSubmittedRequest(null);

                            PORTAL.showMessage(response.data.errorMsg);

                            $rootScope.overlay.show = false;
                          }
                          
                        }, function myError(response) {
                          if (response) {
                            
                            $(eventDialogElem).dialog("close");
                            $scope.calendar.fullCalendar('unselect');
                           
                            $rootScope.overlay.show = false;
                            
                            if (response.data.errorMsg) {

                              ErrorFactory.setmsg(response.data.errorMsg);
                            }
                            ErrorFactory.setURL($location.url());

                            // redirecting to error page if json http request
                            // return
                            // error
                            $location.url('/error.html');
                          }
                        });
                      } else {
                        PORTAL.showMessage("please enter valid data");
                        $(eventDialogElem).dialog("close");
                        $scope.calendar.fullCalendar('unselect');
                        
                      }
                    },
                    Cancel : function() {
                     
                      $(this).dialog("close");
                      
                    }
                  }

                });
                // } else {
                // $(function() {
                // // $("#errorDialog").dialog();
                // $("#errorDialog").dialog({
                // modal : true,
                // width : 550,
                // closeOnEscape : false,
                // open : function(event, ui) {
                // }
                //
                // });
                //
                // }

              },
              // event click functionality begins

              eventClick : function(calEvent, calElement) {
                var currDate = new Date();
                currDate = PORTAL.formatDate(currDate, 'YYYY-MM-DD')
                var eventStartDate = calEvent.start.format();
                if (currDate <= eventStartDate) {

                  if (true && eventDialogElem) {
                    var currTSID = calEvent.leave_id;
                    $(eventDialogElem).dialog(
                        {
                          modal : true,
                          width : 550,
                          closeOnEscape : false,
                          open : function(event, ui) {
                            $scope.resetModel();

                            $scope.$apply(function() {
                              $scope.newLeave.start = calEvent.start.format();
                              $scope.newLeave.end = (calEvent.end === null ? calEvent.start.format() : calEvent.end.format());
                              $scope.newLeave.reason = calEvent.reason;
                              $scope.newLeave.leave_type = (calEvent.leave_type);
                              $scope.newLeave.status = calEvent.status;
                              $scope.newLeave.days = calEvent.days;
                              $scope.newLeave.leave_id = currTSID;
                              if (calEvent.canRemind) {
                                $scope.newLeave.canRemind = calEvent.canRemind;
                              } else {
                                $scope.newLeave.canRemind = false;
                              }
                            });
                          },

                          buttons : {
                            "Update" : function() {
                              var formElem = $("#raise-leaveRequest-form");

                              var updateData = {
                                user_id : $scope.userLeaveRequestData.user_id,
                                leave_id : currTSID,
                                leave_type : $scope.newLeave.leave_type,
                                start : $scope.newLeave.start,
                                end : $scope.newLeave.end,
                                reason : $scope.newLeave.reason,
                                status : $scope.newLeave.status,
                                days : $scope.newLeave.days
                              };

                              if (calEvent.start <= calEvent.end) {
                                if (validateLeaveDetails(updateData)) {

                                  // httpHitFactory.makePOSTHTTPHit() is a
                                  // facory method for
                                  // post hit
                                  httpHitFactory.makePOSTHTTPHit(editLeaveRequest, updateData, contentType).then(function(response) {
                                    if (response && response.data && response.data.status === "SUCCESS") {
                                      updateData.leave_id = response.data.leave_info.leave_id;
                                      updateData.days = response.data.leave_info.days;
                                      updateData.title = response.data.leave_info.title;
                                      updateData.canRemind = response.data.leave_info.canRemind;
                                      $scope.saveEditedRequest(updateData);

                                      calEvent.leave_type = updateData.leave_type;
                                      calEvent.start = updateData.start;
                                      calEvent.end = updateData.end;
                                      calEvent.reason = updateData.reason;
                                      calEvent.status = updateData.status;
                                      calEvent.title = updateData.title;
                                      calEvent.days = updateData.days;
                                      calEvent.canRemind = updateData.canRemind;

                                      $scope.calendar.fullCalendar('updateEvent', calEvent);
                                      $(eventDialogElem).dialog("close");

                                      $rootScope.overlay.show = false;

                                      PORTAL.showMessage("Event Changes Successful");

                                    } else {
                                      PORTAL.showMessage(response.data.errorMsg);
                                      $rootScope.overlay.show = false;
                                    }
                                  }, function myError(response) {
                                    PORTAL.showMessage(response.data.errorMsg);
                                    $rootScope.overlay.show = false;
                                  });
                                } else {
                                  PORTAL.showMessage("Please check the info entered.");
                                  $rootScope.overlay.show = false;
                                }
                              }
                            },
                            "Delete" : function() {
                              if (confirm("Delete this event?")) {
                                $scope.data = {
                                  "leave_id" : currTSID
                                }

                                httpHitFactory.makePOSTHTTPHit(deleteRaisedRequestUrl, $scope.data, contentType).then(
                                    function mysuccess(data, headers, config) {
                                      if (data.data && data.data.status === "SUCCESS") {
                                        $scope.deleteSelectedRequest(data.data.leave_info.leave_id);
                                        $scope.calendar.fullCalendar('removeEvents', calEvent._id);
                                        PORTAL.showMessage("Event Deleted");
                                      }
                                      $(eventDialogElem).dialog("close");
                                      $rootScope.overlay.show = false;
                                    }, function error(response) {
                                      // HIDING overlay
                                      $rootScope.overlay.show = false;
                                      if (response) {
                                        $rootScope.overlay.show = false;

                                        if (response.data.errorMsg) {

                                          ErrorFactory.setmsg(response.data);
                                        }
                                        ErrorFactory.setURL($location.url());
                                        // redirecting to error page if json
                                        // http
                                        // request return error
                                        $location.url('/error.html');
                                      }
                                    });
                              }
                            },
                            Cancel : function() {
                              $(this).dialog("close");
                            }

                          }
                        });

                  }
                } else {
                  $(function() {
                    $("#errorDialog").dialog();
                  });
                }
                return false;
              }
            // event click ends
            });
        
      }

      $scope.goToLeave = function(leaveId) {
        var leaveDate = null;
        for (var i = 0; i < $scope.appliedLeaves.length; i++) {
          if ($scope.appliedLeaves[i].leave_id === leaveId) {
            leaveDate = $scope.appliedLeaves[i].start;
            break;
          }
        }

        if (leaveDate == null) {
          PORTAL.showMessage("Wrong date");
        } else {
          $scope.calendar.fullCalendar('gotoDate', leaveDate);
        }
      }
      // APPLY LEAVES CALENDAR FUNCTIONALITY ENDS

      // THIS CODE WILL BE USED WHEN REQUEST FOR JSON OF APPLIED LEAVES AND
      // LEAVE TYEPES ARE MADE. WILL BE CALLED WHEN REQUEST FOR APPLY LEAVES
      // PAGE IS MADE

      $scope.othersGroup = {};
      $scope.initialMemberLeaves = {};

      $scope.fetchLeavesList = function() {

        if (!$scope.isValidYear()) {
          PORTAL.showMessage("Incorrect year info");
          return;
        }

        $rootScope.overlay = {
          show : true
        }

        httpHitFactory.makePOSTHTTPHit(leaveRequestBaseURL, {
          "request_data" : $scope.userLeaveRequestData,
          "year" : $scope.year
        }, contentType).then(function mySuccess(response) {
          $rootScope.overlay = {
            show : false
          }

          if (response && response.data && response.data.status === "SUCCESS") {

            $scope.appliedLeaves = angular.copy(response.data.user_data.user_leaves);
            $scope.leaveTypes = response.data.leave_types.LeaveType;
            $scope.member_leaves = response.data.user_data.member_leaves;

            // newDate HOLDS THE DATE OF SEARCHED YEAR OR IT WILL HOLD THE
            // DATE OF HOLIDAY PRESENT IN HOLIDAYLIST FROM DATA BASE
            var newDate = new Date($scope.year, 0, 1);

            if ($scope.appliedLeaves.length > 0) {
              newDate = $scope.appliedLeaves[0].start;
            } else if (parseInt(curryear) === parseInt($scope.year)) {
              newDate = new Date();
            }

            if ($scope.calendar) {
              $scope.calendar.fullCalendar('destroy');
            }

            $scope.createCalendar($("#applyLeaveCalendar"), $scope.appliedLeaves, newDate);

            // draw initial charts
            $scope.regroupLeavesData();

            $scope.groupOthersLeaves();
            $scope.initialMemberLeaves = angular.copy($scope.othersGroup);
          } else {
            if (response.data.errorMsg) {
              ErrorFactory.setmsg("Error");
            }
            ErrorFactory.setURL($location.url());
            $location.url('/error.html');
          }
        }, function myError(response) {
          // HIDING overlay
          $rootScope.overlay = {
            show : false
          }
          if (response) {
            ErrorFactory.setURL($location.url());
            $location.url('/error.html');

            if (response.data.errorMsg) {
              ErrorFactory.setmsg("Error");
            }
          }
        });
        // AJAX HIT ENDS HERE FOR FETCHING LEAVE REQUEST DATA ALREADY PRSENT FOR
        // USER
      }

      $scope.fetchLeavesList();

      $scope.appliedLeaves = [];

      $scope.groupOthersLeaves = function() {
        $scope.othersGroup = {};
        for (var i = 0; i < $scope.leavesRequestStatus.length; i++) {
          $scope.othersGroup[$scope.leavesRequestStatus[i].status] = [];
        }

        if ($scope.member_leaves != null) {
          for (var i = 0; i < $scope.member_leaves.length; i++) {
            var othersLeaveObj = $scope.member_leaves[i];
            $scope.othersGroup[othersLeaveObj.status].push(othersLeaveObj);
          }
        }
      }

      $scope.canSaveChanges = function(val, index) {
        try {
          var newObj = $scope.othersGroup[val][index];
          var oldObj = $scope.initialMemberLeaves[val][index];

          return (newObj.status != oldObj.status)
              || (!PORTAL.isValueUndefined(newObj.additionalComments) && PORTAL.isValidString(newObj.additionalComments, 2, 500));
        } catch (err) {
          return false;
        }

      }

      $scope.changeRequestStatus = function(val, index) {
        try {

          var dataToHit = $scope.othersGroup[val][index];
          dataToHit.user_id = $scope.userLeaveRequestData.user_id;

          httpHitFactory.makePOSTHTTPHit(leaveRequestStatusChangeURL, dataToHit, contentType).then(function mySuccess(response) {
            $rootScope.overlay = {
              show : false
            }
            if (response.data && response.data.status && response.data.status === "SUCCESS") {
              var updatedLeaveInfo = response.data.updated_leave;

              for (var i = 0; i < $scope.member_leaves.length; i++) {
                if ($scope.member_leaves[i].leave_id === updatedLeaveInfo.leave_id) {
                  $scope.member_leaves[i] = updatedLeaveInfo;
                  break;
                }
              }

              $scope.groupOthersLeaves();
              $scope.initialMemberLeaves = angular.copy($scope.othersGroup);

              PORTAL.showMessage("success");
            } else {
              if (response.data.erorMsg) {

                ErrorFactory.setmsg(response.data.erorMsg);
              }
              ErrorFactory.setURL($location.url());
              $location.url('/error.html');
            }

          }, function myError(response) {
            // HIDING overlay
            $rootScope.overlay = {
              show : false
            }
            if (response) {
              if (respone.data.errorMsg) {

                ErrorFactory.setmsg(respone.data.errorMsg);
              }
              ErrorFactory.setURL($location.url());
              // redirecting to error page if json http request return error
              $location.url('/error.html');
            }
          });
        } catch (err) {

        }
      }

      /**
       * This api should be called every time there is change in applied leaves
       * data. this would redraw the charts by grouping them
       */
      $scope.regroupLeavesData = function() {
        $scope.groupLeaves();
      }

      // For Request Status Tab. This object will contain status specific arrays
      // of
      // the user's leave requests
      $scope.myGroup = {};

      $scope.groupLeaves = function() {
        var userLeaves = $scope.appliedLeaves;
        $scope.leavesGroupedByPolicy = [];
        $scope.leavesGroupedByStatus = [];
        $scope.statusGroupColor = [];

        $scope.chart_data = {
          leaveTypes : [],
          leavesRemaining : []
        }

        for (var i = 0; i < $scope.leavesRequestStatus.length; i++) {
          $scope.leavesGroupedByStatus.push({
            "label" : $scope.leavesRequestStatus[i].status,
            "value" : 0
          });

          $scope.myGroup[$scope.leavesRequestStatus[i].status] = [];
          $scope.statusGroupColor.push($scope.leavesRequestStatus[i].color);
        }

        for (var i = 0; i < userLeaves.length; i++) {
          var leaveObj = userLeaves[i];
          var matchedGroup = null;
          for (var groupNum = 0; groupNum < $scope.leavesGroupedByPolicy.length; groupNum++) {
            var currentGroup = $scope.leavesGroupedByPolicy[groupNum];
            if (currentGroup.label === leaveObj.leave_type.name) {
              matchedGroup = currentGroup;
              $scope.leavesGroupedByPolicy[groupNum].value += leaveObj.days;

              if (leaveObj.status === "APPROVED") {
                $scope.chart_data.leaveTypes[groupNum].value += leaveObj.days;
              }

              break;
            }
          }

          if (matchedGroup === null) {
            matchedGroup = {
              "label" : leaveObj.leave_type.name,
              "value" : leaveObj.days
            };

            $scope.leavesGroupedByPolicy.push(matchedGroup);

            var copyOfMatched = angular.copy(matchedGroup);
            if (leaveObj.status != "APPROVED") {
              copyOfMatched.value = 0;
            }

            $scope.chart_data.leaveTypes.push(copyOfMatched);
          }

          // Code to group leave requests by status
          matchedGroup = null;
          for (var groupNum = 0; groupNum < $scope.leavesGroupedByStatus.length; groupNum++) {
            var currentGroup = $scope.leavesGroupedByStatus[groupNum];

            // !IMPORTANT label is a keyword, used for Morris.Donut
            if (currentGroup.label === leaveObj.status) {
              matchedGroup = currentGroup;
              $scope.leavesGroupedByStatus[groupNum].value++;
              break;
            }
          }

          $scope.myGroup[leaveObj.status].push(leaveObj);

        }

        var temp1 = [];
        var temp2 = [];
        for (var i = 0; i < $scope.leavesGroupedByStatus.length; i++) {
          if ($scope.leavesGroupedByStatus[i].value != 0) {
            temp1.push($scope.leavesGroupedByStatus[i]);
            temp2.push($scope.statusGroupColor[i]);
          }
        }

        $scope.leavesGroupedByStatus = temp1;
        $scope.statusGroupColor = temp2;
        
        for (var g1 = 0; g1 < $scope.leaveTypes.length; g1++) {
          var value = $scope.leaveTypes[g1].days;

          for (var g2 = 0; g2 < $scope.chart_data.leaveTypes.length; g2++) {
            if ($scope.leaveTypes[g1].name === $scope.chart_data.leaveTypes[g2].label) {
              value = value - $scope.chart_data.leaveTypes[g2].value;
              break;
            }
          }

          $scope.chart_data.leavesRemaining.push({
            "label" : $scope.leaveTypes[g1].name,
            "value" : value
          });
        }
      }
      
      $scope.hasAdditionalComment = false;
      $scope.hasAdditionalComments = function(leaveType){
        if(leaveType.additionalComments == null){
          $scope.hasAdditionalComment = false;;
        } else {
          $scope.additionalComments = leaveType.additionalComments;
          $scope.hasAdditionalComment = true;
        }
      }

      $scope.sendReminder = function(leaveId) {

        $rootScope.overlay.show = true;

        var remindForLeaveId = null;
        for (var i = 0; i < $scope.appliedLeaves.length; i++) {
          if ($scope.appliedLeaves[i].leave_id === leaveId) {
            remindForLeaveId = leaveId;
            break;
          }
        }

        if (remindForLeaveId == null) {
          PORTAL.showMessage("Could not find that information. Please check again.");
        } else {
          httpHitFactory.makePOSTHTTPHit(sendLeaveReminderUrl, {
            "leave_id" : remindForLeaveId
          }, contentType).then(function(response) {

            if (response && response.data) {
              if (response.data.status === "SUCCESS") {
                PORTAL.showMessage("Reminder has been sent successfully");
              } else {
                if (response.data.errorMsg) {
                  PORTAL.showMessage(response.data.errorMsg);
                } else {
                  PORTAL.showMessage("Could not complete the request. Please contact the System Administrator");
                }
              }
            } else {
              PORTAL.showMessage("Could not complete the request. Please contact the System Administrator");
            }

            $rootScope.overlay.show = false;
          }, function myError(response) {
            if (response && response.data) {
              if (response.data.errorMsg) {
                PORTAL.showMessage(response.data.errorMsg);
              } else {
                PORTAL.showMessage("Could not complete the request. Please contact the System Administrator");
              }
            } else {
              PORTAL.showMessage("Could not complete the request. Please contact the System Administrator");
            }
          });
        }
      }

      /*
       * BELOW IS THE CODE FOR LEAVE STATUS BAR PRESENT ON UI IT IS HAVING
       * **DATA[] ARRAY MEANS FORMAT OF DATA REQUIRED IN BAR **COFIG ELEMENT
       * **OTHER ESSENTIAL CONFIGURATION REQUIRED FOR BAR CHART LIKE COLOR,
       * XKEYS, YKEYS ETC...
       */
      /*
       * var data = [ { month : 'Jan', approved : 10, pending : 8, un_approved :
       * 10 }, { month : 'Feb', approved : 9, pending : 5, un_approved : 10 }, {
       * month : 'March', approved : 10, pending : 10, un_approved : 5 }, {
       * month : 'April', approved : 3, pending : 7, un_approved : 10 }, { month :
       * 'May', approved : 10, pending : 45, un_approved : 7 }, { month :
       * 'June', approved : 10, pending : 70, un_approved : 10 }, { month :
       * 'July', approved : 45, pending : 39, un_approved : 8 }, { month :
       * 'August', approved : 4, pending : 10, un_approved : 1 }, { month :
       * 'Sepet', approved : 4, pending : 2, un_approved : 8 }, { month : 'Oct',
       * approved : 4, pending : 9, un_approved : 8 }, { month : 'Nov', approved :
       * 45, pending : 10, un_approved : 8 }, { month : 'Dec', approved : 4,
       * pending : 7, un_approved : 30 } ], config = { data : data, xkey :
       * 'month', ykeys : [ 'approved', 'pending', 'un_approved' ], labels : [
       * 'approved', 'pending', 'un_approved' ], fillOpacity : 0.6, hideHover :
       * 'auto', behaveLikeLine : true, resize : true, pointFillColors : [
       * '#ffffff' ], pointStrokeColors : [ 'black' ], lineColors : [ 'green',
       * 'yellow', 'red' ] }; config.element = 'leaveStatusBar';
       * Morris.Bar(config); colors_array = [ "#0797ea", "#fef200", "#799556",
       * "#070807" ];
       */

    });
angular.module('App').controller(
    "attendanceCtrl",
    function($scope, $location, $rootScope, ErrorFactory, httpHitFactory, $timeout) {

      // this variable is for holding the content-type required for post hit and
      // is used in every post hit
      var contentType = 'application/json';

      // UI ACTIONS
      var attendanceBaseURL = attendanceUrl;
      var calendarDataURL = attendanceBaseURL + "/calendar";
      var downloadAttendanceData = attendanceBaseURL + "/download";
      // UI actions end

      // this object holds the login user_id and it is required while making
      // post
      // hit at the time of loading of page
      $scope.userData = {
          user_id : loggedInUserId,
      }

      $scope.isPristine = true;

      $rootScope.overlay = {
          show : false
      }

      $scope.dpStart = {
          opened : false
      };

      $scope.openStartDatePicker = function() {
        $scope.dpStart.opened = true;
      };

      $scope.dpEnd = {
          opened : false
      };

      $scope.openEndDatePicker = function() {
        $scope.dpEnd.opened = true;
      };

      // Initially last 7 days of attendance is being fetched
      $scope.attendanceEnd = new Date();
      $scope.attendanceStart = new Date($scope.attendanceEnd);
      $scope.attendanceStart.setDate($scope.attendanceEnd.getDate() - 7);

      var calendar;
      $timeout(function() {
        calendar = $("#elaborateCalendar").fullCalendar({
          editable : false,
          eventLimit : true, // allow "more" link when too many events
        });

      });

      $scope.createCalendar = function() {
        $timeout(function() {
          calendar.fullCalendar('destroy');
          var date = PORTAL.formatDate($scope.attendanceStart, "YYYY-MM-DD");
          calendar = $("#elaborateCalendar").fullCalendar({
            defaultDate : date,
            editable : false,
            eventLimit : true, // allow "more" link when too many events
            events : $scope.calendar_data
          });

          // $scope.calendar.fullCalendar('gotoDate', date);
        });
      }

      $scope.$watch("calendar_data", function(newValue, oldValue) {
        if (newValue && newValue.length > 0) {
          $(".nav-tabs " + 'a[href="#calendarview"]').tab('show');
        }
      });

      $(document).ready(function() {
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
          var tab = e.currentTarget.hash
          if (tab == '#calendarview') {
            calendar.fullCalendar('render');
          }
        });
      });

      $scope.getCalendarDataAjax = function(calendarRequestData) {
      }

      $scope.checkData = function() {
        if (PORTAL.isValueUndefined($scope.attendance_data)) {
          PORTAL.showMessage("Problem with the data");
          return false;
        }

        if ($scope.attendanceStart > $scope.attendanceEnd) {
          PORTAL.showMessage("Please choose correct start and end dates");
          return false;
        }
      }

      $scope.getCalendarData = function(userId) {

        var isDataOkay = $scope.checkData();
        if (isDataOkay == false) {
          return;
        }

        var user;
        for (var i = 0; i < $scope.attendance_data.length; i++) {
          if ($scope.attendance_data[i].user_id === userId) {
            user = angular.copy($scope.attendance_data[i]);
            break;
          }
        }

        if (user == null || PORTAL.isValueUndefined(user)) {
          PORTAL.showMessage("Problem with the data");
          return;
        }

        $scope.calendar_data = [];

        // The object to store calendar view display information
        $scope.calendarDispData = null;
        $scope.calendarDispData = {
            name : user.name,
            startDate : PORTAL.formatDate($scope.attendanceStart, 'DD-MMM-YY'),
            endDate : PORTAL.formatDate($scope.attendanceEnd, 'DD-MMM-YY')
        }

        // user_id will be used only in case of specific user attendance
        var users = [];
        users.push(angular.copy(user));
        var calendarRequestData = {
            users : users,
            startDate : PORTAL.formatDate($scope.attendanceStart),
            endDate : PORTAL.formatDate($scope.attendanceEnd)
        };

        $rootScope.overlay = {
            show : true
        }

        try {
          httpHitFactory.makePOSTHTTPHit(calendarDataURL, calendarRequestData, contentType).then(function mySuccess(response) {

            var isError = false;
            if (response && response.data) {
              if (response.data.status === "SUCCESS") {
                if (!PORTAL.isValueUndefined(response.data.calendar_data)) {
                  $scope.calendar_data = response.data.calendar_data[0].data;
                  $scope.createCalendar();
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

          }, function myError(response) {

            ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
            ErrorFactory.setURL($location.url());
            $location.url('/error.html');

            // HIDING overlay
            $rootScope.overlay.show = false;
          });
        } catch (err) {
          PORTAL.showMessage("Please check the data entered");
        }
      }

      $scope.downloadAttendanceData = function() {

        var isDataOkay = $scope.checkData();
        if (isDataOkay == false) {
          return;
        }

        var downloadRequestData = {
            users : $scope.selectedUsers,
            startDate : PORTAL.formatDate($scope.attendanceStart),
            endDate : PORTAL.formatDate($scope.attendanceEnd),
        }

        $rootScope.overlay = {
            show : true
        }

        try {
          httpHitFactory.makePOSTHTTPHit(calendarDataURL, downloadRequestData, contentType).then(function mySuccess(response) {

            var isError = false;
            if (response && response.data) {
              if (response.data.status === "SUCCESS") {
                if (!PORTAL.isValueUndefined(response.data.calendar_data)) {
                  $scope.convertToCSVAndDownload(response.data.calendar_data,response.data.leavesData, downloadRequestData);
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

          }, function myError(response) {

            ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
            ErrorFactory.setURL($location.url());
            $location.url('/error.html');

            // HIDING overlay
            $rootScope.overlay.show = false;
          });
        } catch (err) {
          console.log(err);
          PORTAL.showMessage("Please check the data entered");
        }
      }

      // What I am trying to do: for every user, traverse the
      /*
       * each object => row { }
       */
      $scope.convertToCSVAndDownload = function(jsonData,leavesData, requestData) {
        console.log(jsonData);
        var CSV = [];

        var attendanceENUM = [
                              "PRESENT", "ABSENT", "HALFDAY", "APPROVED"
                              ];
        // outer user loop


        for (var i = 0; i < jsonData.length; i++) {
          var userData = jsonData[i];
          var prevDateOfRecord = null;

          if (!userData.data) {

            continue;
          }


          var x=1;
          for (var j = 0; j < userData.data.length; j++) {
            var record = userData.data[j];
            //   var userInfo=userData.user[j];

            var dateOfRecord = record.start.substring(0, 10);

            var time = record.start.substring(11);
            var isMainRecord = false;
            var total = null;
            var title = null;
            var notes="";

            for (var count = 0; count < attendanceENUM.length; count++) {
              var indexOfEnum = record.title.indexOf(attendanceENUM[count]);
              if (indexOfEnum != -1) {
                total = record.title.substring(indexOfEnum + attendanceENUM[count].length).trim();
                isMainRecord = true;
                title = attendanceENUM[count];
                break;
              }
            }

            //for notes  
            if(userData.status!=attendanceENUM[0]){
              for(var k=0; k<leavesData.length ; k++){

                if(!leavesData[k].additionalComments){
                  continue;
                } else{
                  if(userData.user.id==leavesData[k].requestor.id){

                    if(dateOfRecord>=leavesData[k].start && dateOfRecord<leavesData[k].end){
                      notes=leavesData[k].additionalComments;
                    }   
                  }
                }            
              } 
            }

            if (isMainRecord == true) {
              var recordObject = {
                  name : userData.user.name,
                  id : userData.user.id,
                  date : dateOfRecord,
                  status : title,
                  total : total,
                  notes : notes,
                  isMain : true
              };
              CSV.push(recordObject);
            } else {
              if (record.title == "In-Time") {
                for (var k = CSV.length - 1; k >= 0; k--) {
                  var oldRecord = CSV[k];
                  if (oldRecord.date != dateOfRecord) {
                    break;
                  }
                  if (oldRecord.isMain == true) {
                    oldRecord.in_time = time;
                    break;
                  }
                }
              } else if (record.title == "Out-Time") {
                for (var k = CSV.length - 1; k >= 0; k--) {
                  var oldRecord = CSV[k];
                  if (oldRecord.date != dateOfRecord) {
                    break;
                  }
                  if (oldRecord.isMain == true) {
                    oldRecord.out_time = time;
                    break;
                  }
                }
              }
            }
            prevDateOfRecord = dateOfRecord;
          }

        }

        console.log(CSV);

        var csvContent = "EMP_ID,NAME,DATE,STATUS,TOTAL_TIME,IN_TIME,OUT_TIME,NOTES\r\n";
        for (var i = 0; i < CSV.length; i++) {
          var record = CSV[i];
          csvContent += record.id + "," + record.name + "," + record.date + "," + record.status + "," + (record.total != null ? record.total : '')
          + "," + (record.in_time != null ? record.in_time : '') + "," + (record.out_time != null ? record.out_time : '' + "," + record.notes) + "\r\n";
        }

        console.log(csvContent);

        var fileName = "AttendanceData_" + requestData.startDate + "-" + requestData.endDate;
        var uri = 'data:application/csv;charset=utf-8,' + escape(csvContent);
        var link = document.createElement("a");
        link.href = uri;
        link.style = "visibility:hidden";
        link.download = fileName + ".csv";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }

      $scope.selectedUsers = [];

      $scope.selectUser = function(selectedUser) {
        if (!selectedUser) {
          PORTAL.showMessage("Wrong user selected");
        }
        var flag = false;
        for (var i = 0; i < $scope.selectedUsers.length; i++) {
          var obj = $scope.selectedUsers[i];
          if (obj.user_id === selectedUser.user_id) {
            $scope.selectedUsers.splice(i, 1);
            flag = true;
            break;
          }
        }

        if (flag == false) {
          $scope.selectedUsers.push(angular.copy(selectedUser));
        }
      }

      $scope.selectRecord = {
          prevAllSelectedValue : false,
          allSelected : false
      }

      $scope.toggleSelectAll = function() {
        $scope.selectedUsers = [];
        var selectAll = $scope.selectRecord.allSelected === true && $scope.selectRecord.prevAllSelectedValue == false;
        for (var i = 0; i < $scope.attendance_data.length; i++) {
          var record = $scope.attendance_data[i];
          record.selected = selectAll;
          if (selectAll === true) {
            $scope.selectedUsers.push(angular.copy(record));
          }
        }

        $scope.selectRecord.prevAllSelectedValue = $scope.selectRecord.allSelected;
      }

      $scope.getAttendance = function() {
        try {
          if ($scope.attendanceStart > $scope.attendanceEnd) {
            PORTAL.showMessage("Please choose correct start and end dates");
            return;
          }

          $scope.attendance_data = [];

          // user_id will be used only in case of specific user attendance
          var attendanceData = {
              // user_id : $scope.userData.user_id,
              startDate : PORTAL.formatDate($scope.attendanceStart),
              endDate : PORTAL.formatDate($scope.attendanceEnd)
          };

          if ($scope._selectedTeamMem && $scope._selectedTeamMem.id) {
            attendanceData.user_id = $scope._selectedTeamMem.id;
          }

          $rootScope.overlay = {
              show : true
          }

          $scope.isPristine = false;

          httpHitFactory.makePOSTHTTPHit(attendanceBaseURL, attendanceData, contentType).then(function mySuccess(response) {

            var isError = false;
            if (response && response.data) {
              if (response.data.status === "SUCCESS") {
                if (!PORTAL.isValueUndefined(response.data.attendance_data)) {
                  $scope.attendance_data = response.data.attendance_data;

                  $scope.selectRecord = {
                      prevAllSelectedValue : false,
                      allSelected : false
                  }

                  $scope.selectedUsers = [];

                  // $timeout(function() {
                  // $('#attendance_table').dataTable();
                  // });
                }
              } else if (response.data.errorMsg) {
                ErrorFactory.setmsg(response.data.errorMsg);
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

          }, function myError(response) {

            ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
            ErrorFactory.setURL($location.url());
            $location.url('/error.html');

            // HIDING overlay
            $rootScope.overlay.show = false;
          });
        } catch (err) {
          PORTAL.showMessage("Please check the data entered");
        }

        $timeout(function() {
          $('#specificUser').val("");
        });
        delete $scope._selectedTeamMem;

      }

      $scope.init = function() {

        $rootScope.overlay = {
            show : true
        };

        httpHitFactory.makePOSTHTTPHit(isAdminCheckURL, $scope.userData, contentType).then(function mySuccess(response) {

          if (response && response.data && response.data.isAdmin) {
            $scope.isAdmin = response.data.isAdmin;
          } else {
            $scope.isAdmin = false;
          }

          $rootScope.overlay.show = false;

        }, function myError(response) {

          $scope.isAdmin = false;

          // HIDING overlay
          $rootScope.overlay.show = false;
        });
      }

      $scope.getTeamMembers = function(val) {
        return httpHitFactory.makePOSTHTTPHit(teamMemberSearchURL, val, contentType).then(function(response) {
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
        $scope._selectedTeamMem = angular.copy($item);
      };

      $scope.init();
    });
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
//add  holidayCalendarJson.json and holiday list json 

angular.module('App').controller(
    "leavePolicyCtrl",
    function($scope, $http, $routeParams, ErrorFactory, $rootScope, httpHitFactory, $location, $route) {

      // this variable is for holding the content-type requird for post hit and
      // is used in every post hit
      var contentType = 'application/json';

      // fetching current date
      var currDate = new Date();
      currDate = PORTAL.formatDate(currDate, 'YYYY-MM-DD');
      // UI ACTIONS FOR GET && POST HIT

      var holidayCalendarJsonUrl = leavePolicyJsonUrl + "/holidaycalendar";
      var addHolidayToCalendar = leavePolicyJsonUrl + "/holidaycalendar/add";
      var editHolidayCalendar = leavePolicyJsonUrl + "/holidaycalendar/update";
      var deleteHolidayCalendar = leavePolicyJsonUrl + "/holidaycalendar/delete";
      var leaveTypeJsonUrl = leavePolicyJsonUrl + "/leavetype";
      var addLeaveType = leavePolicyJsonUrl + "/leavetype/add";
      var editLeaveType = leavePolicyJsonUrl + "/leavetype/update";
      var deleteLeaveTypeJSonURL = leavePolicyJsonUrl + "/leavetype/delete";
      // UI ACTIONS ENDS

      // $scope.selectedHolidayEvent will hold the details of event which is to
      // be updated in holiday calendar;
      $scope.selectedHolidayEvent = {};

      $rootScope.overlay = {
        show : true
      }
      // validation helper functions
      var isDuplicateName = function(newHolidayName, completeList, idx) {
        for (var i = 0; i < completeList.length; i++) {
          if (i != idx) {
            if (newHolidayName === completeList[i].TeamName) {
              return true;
            }
          }

        }
        return false;
      }

      var isHolidayNameValid = function(name, obj, id) {
        if (PORTAL.isValueUndefined(name)) {
          return false;
        } else {
          return (PORTAL.isValidString(name, 5, 25) || isDuplicateName(name, obj, id));
        }
      }

      var isHolidayDaysValid = function(days) {
        if (PORTAL.isValueUndefined(days)) {
          return false;
        }
        if (!PORTAL.isEmptyString(days)) {
          return (!PORTAL.isNotAValidNumber(days));
        } else {
          return false;
        }
      };
      var isHolidayDateValid = function(eventdate) {
        if (PORTAL.isValueUndefined(eventdate)) {
          return false;
        } else {
          return (PORTAL.validateDate(eventdate));
        }
      };

      var eventDialogElem = $("#modelContent");

      // helper methods for delete save used in holiday calendar
      $scope.renderSubmittedEvent = function(data) {
        if (data) {
          $scope.newHolidayCalendar.id = data.id.toString();
          $scope.holidayCalendarList.push($scope.newHolidayCalendar);

          $scope.calendar.fullCalendar('renderEvent', {
            id : data.id,
            title : data.title,
            start : data.start,
            end : data.end,
            allDay : data.allDay
          });

          PORTAL.showMessage("New Event Added");
        }
        $(eventDialogElem).dialog("close");
        $scope.calendar.fullCalendar('unselect');

      };

      $scope.saveEditedEvent = function(data) {
        for (var i = 0; i < $scope.holidayCalendarList.length; i++) {
          if ($scope.holidayCalendarList[i].id === data.id) {
            $scope.holidayCalendarList[i].title = data.title;
            $scope.holidayCalendarList[i].start = data.start;
            $scope.holidayCalendarList[i].end = data.end;
            $scope.holidayCalendarList[i].allDay = data.allDay;
            break;
          }
        }

        // TODO: put PORTAL.showMessage message if after no successful ajax hit
        // is done and
        // erro
        // is received
        $(eventDialogElem).dialog("close");
        $scope.calendar.fullCalendar('unselect');

      };

      $scope.deleteSelectedEvent = function(eventID) {
        for (var i = 0; i < $scope.holidayCalendarList.length; i++) {
          if ($scope.holidayCalendarList[i].id === eventID) {
            $scope.holidayCalendarList.splice(i, 1);
          }
        }
      }

      $scope.broadcastYearlyCalendar = function(eventId) {
        try {
          if (!$scope.holidayCalendarList || $scope.holidayCalendarList.length < 1) {
            PORTAL.showMessage("No events to notify");
            return;
          }

          $rootScope.overlay.show = true;

          var dataToHit = {
            "year" : $scope.year
          }

          if (eventId) {
            var dataArray = [];
            for (var i = 0; i < $scope.holidayCalendarList.length; i++) {
              if ($scope.holidayCalendarList[i].id === eventId) {
                dataArray.push($scope.holidayCalendarList[i]);
                break;
              }
            }

            if (dataArray.length != 0) {
              dataToHit.list = dataArray;
            } else {
              PORTAL.showMessage("No events to notify");
              return;
            }
          } else {
            dataToHit.list = $scope.holidayCalendarList;
          }

          dataToHit.list.sort(function(a, b) {
            var aDt = PORTAL.getDate(a.start, "YYYY-MM-DD");
            var bDt = PORTAL.getDate(b.start, "YYYY-MM-DD");
            return aDt.getTime() - bDt.getTime();
          });

          httpHitFactory.makePOSTHTTPHit(broadcastYearlyCalendarURL, dataToHit, 'application/json').then(function(response) {
            if (response && response.data) {
              if (response.data.status === "SUCCESS") {
                PORTAL.showMessage("Notifications Sent");
              } else {
                if (response.data.errorMsg) {
                  PORTAL.showMessage(response.data.errorMsg);
                }
              }
            } else {
              PORTAL.showMessage("There had been an error. Please contact the System Administrator");
            }
            $rootScope.overlay.show = false;
          }, function error(response) {
            if (response && response.data && response.data.errorMsg) {
              PORTAL.showMessage(response.data.errorMsg);
            } else {
              PORTAL.showMessage("Error in sending notifications");
            }
            $rootScope.overlay.show = false;
          });

        } catch (err) {
          PORTAL.showMessage("Error in sending notifications");
          $rootScope.overlay.show = false;
        }

      }

      // creatinG holiday calendar with the help of full calendar API
      $scope.createCalendar = function(elem, eventsJSON, isAdmin, isSelectable, date) {

        $scope.calendar = $(elem).fullCalendar(
            {
              defaultDate : date,
              header : {
                left : 'prev,next',
                center : 'title',
                right : 'month,basicWeek,basicDay'
              },

              theme : true,
              events : eventsJSON,
              selectable : isSelectable,
              selectHelper : false,
              select : function(start, end, allDay) {

                var startDate = start.format();

                if ((startDate >= currDate) || (isAdmin)) {
                  if (isAdmin && eventDialogElem) {
                    $(eventDialogElem).dialog(
                        {
                          modal : true,
                          width : 550,
                          closeOnEscape : false,
                          open : function(event, ui) {
                            $scope.$apply(function() {
                              $scope.newHolidayCalendar = {
                                id : "",
                                title : "",
                                start : start.format(),
                                end : end.format()
                              };
                              var formElem = $("#event-details-form");
                              $(formElem).find("input[name='title']").val("");
                              $(formElem).find("input[name='start']").val(start.format());
                              $(formElem).find("input[name='end']").val(end === null ? start.format() : end.format());
                            });

                          },
                          buttons : {
                            "Add" : function() {
                              var formElem = $("#event-details-form");
                              $scope.newHolidayCalendar.title = $(formElem).find("input[name='title']").val();

                              var newEvent = {
                                // id : currTSID.toString(),
                                title : $scope.newHolidayCalendar.title,
                                start : start,
                                end : end,
                                allDay : allDay
                              }

                              if (isAdmin && (newEvent.start.format() < newEvent.end.format()) && !PORTAL.isEmptyString(newEvent.title)
                                  && (isHolidayDateValid(newEvent.start.format())) && (isHolidayDateValid(newEvent.end.format()))) {
                                $rootScope.overlay.show = true;

                                httpHitFactory.makePOSTHTTPHit(addHolidayToCalendar, newEvent, contentType).then(function(response) {
                                  if (response && response.data && response.data.status === "SUCCESS") {
                                    newEvent.id = response.data.id;
                                    $scope.renderSubmittedEvent(newEvent);
                                  } else {
                                    $scope.renderSubmittedEvent(null);
                                  }
                                  $rootScope.overlay.show = false;
                                }, function myError(response) {
                                  $rootScope.overlay.show = false;
                                  if (response) {
                                    if (response.data.errorMsg) {
                                      PORTAL.showMessage(response.data.errorMsg);
                                    }
                                    $(eventDialogElem).dialog("close");
                                    $scope.calendar.fullCalendar('unselect');
                                  }
                                });
                              }
                            },
                            Cancel : function() {
                              $(this).dialog("close");
                            }
                          }
                        });
                  }
                } else {
                  $(function() {
                    // $("#errorDialog").dialog();
                    $("#errorDialog").dialog({
                      modal : true,
                      width : 550,
                      closeOnEscape : false,
                      open : function(event, ui) {
                      }

                    });

                  });
                }

              },

              eventClick : function(calEvent, calElement) {

                var eventStartDate = calEvent.start.format();
                if ((currDate <= eventStartDate) || (isAdmin)) {

                  if (isAdmin && eventDialogElem) {
                    var currTSID = calEvent.id;
                    $(eventDialogElem).dialog(
                        {
                          modal : true,
                          width : 550,
                          closeOnEscape : false,
                          open : function(event, ui) {
                            var formElem = $("#event-details-form");
                            $(formElem).find("input[name='title']").val(calEvent.title);
                            $(formElem).find("input[name='start']").val(calEvent.start.format());
                            $(formElem).find("input[name='end']").val(calEvent.end === null ? calEvent.start.format() : calEvent.end.format());
                          },

                          buttons : {
                            "Save " : function() {
                              var formElem = $("#event-details-form");
                              calEvent.title = $(formElem).find("input[name='title']").val();
                              calEvent.start = $(formElem).find("input[name='start']").val();
                              calEvent.end = $(formElem).find("input[name='end']").val();
                              calEvent.allDay = "allDay";
                              $scope.selectedHolidayEvent = {
                                id : calEvent.id,
                                title : calEvent.title,
                                start : calEvent.start,
                                end : calEvent.end,
                                allDay : true
                              };
                              if (calEvent.start <= calEvent.end) {
                                if (isAdmin && !PORTAL.isEmptyString(calEvent.title) && (isHolidayDateValid(calEvent.start))
                                    && (isHolidayDateValid(calEvent.end))) {
                                  // httpHitFactory.makePOSTHTTPHit() is a
                                  // facory method for post hit
                                  httpHitFactory.makePOSTHTTPHit(editHolidayCalendar, $scope.selectedHolidayEvent, contentType).then(
                                      function(response) {
                                        if (response && response.data && response.data.status === "SUCCESS") {
                                          $scope.saveEditedEvent(calEvent);
                                          $scope.calendar.fullCalendar('updateEvent', calEvent);
                                          $rootScope.overlay.show = false;
                                          PORTAL.showMessage("Event Changes Successful");
                                          $(eventDialogElem).dialog("close");
                                        } else {
                                          $rootScope.overlay.show = false;
                                          PORTAL.showMessage("error in updating info");
                                          $(eventDialogElem).dialog("close");
                                        }
                                      }, function myError(response) {
                                        $rootScope.overlay.show = false;
                                        if (response) {
                                          if (response.data.errorMsg) {
                                            PORTAL.showMessage(response.data.errorMsg);
                                          }
                                          $(eventDialogElem).dialog("close");
                                          $scope.calendar.fullCalendar('unselect');
                                        }
                                      });
                                } else {
                                  PORTAL.showMessage("DATA IS NOT CORRECT ");
                                }
                              }
                            },
                            "Delete Event" : function() {
                              if (isAdmin) {
                                if (confirm("Delete this event?")) {

                                  $rootScope.overlay.show = true;
                                  $scope.content = {
                                    "id" : currTSID
                                  };

                                  httpHitFactory.makePOSTHTTPHit(deleteHolidayCalendar, $scope.content, contentType).then(
                                      function mysuccess(data, headers, config) {
                                        if (data.data && data.data.status === "SUCCESS") {
                                          $scope.deleteSelectedEvent(currTSID);
                                          $scope.calendar.fullCalendar('removeEvents', currTSID);
                                          PORTAL.showMessage("Event Deleted");
                                        }
                                        $(eventDialogElem).dialog("close");
                                        $rootScope.overlay.show = false;
                                      }, function error(response) {
                                        // HIDING overlay
                                        $rootScope.overlay.show = false;
                                        if (response) {
                                          $rootScope.overlay.show = false;
                                          if (response.data.errorMsg) {

                                            ErrorFactory.setmsg(response.data.errorMsg);
                                          }
                                          ErrorFactory.setURL($location.url());
                                          // redirecting to error page if json
                                          // http
                                          // request return error
                                          $location.url('/error.html');
                                        }
                                      });
                                }
                              }
                            },
                            Cancel : function() {
                              $(this).dialog("close");
                            }

                          }
                        });

                  }
                } else {
                  $(function() {
                    $("#errorDialog").dialog();
                  });
                }
                // if there is an event link, do not go there on clicking it
                // from
                // here
                // in the admin panel
                return false;
              }
            });
      }

      // gotoDate function for full calendar to go to specific date in the
      // calendar
      $scope.goToHoliday = function(idx) {
        var eventDate = null;

        for (var i = 0; i < $scope.holidayCalendarList.length; i++) {
          if (idx === $scope.holidayCalendarList[i].id) {
            eventDate = $scope.holidayCalendarList[i].start;
            break;
          }
        }

        if (eventDate != null) {
          $scope.calendar.fullCalendar('gotoDate', eventDate);
        }
      }

      // objects for holiday calendar and holiday type data
      $scope.holidayCalendarList;
      $scope.holidayTypeList;

      // this is used in leave type
      $scope.selectedHolidayTypeData = {};
      var date = new Date();
      var curryear = date.getFullYear();
      $scope.year = "" + curryear;

      $scope.isValidYear = function() {

        if (!$scope.year) {
          return false;
        }

        if ($scope.previousYear) {
          if ($scope.year === $scope.previousYear) {
            return false;
          }
        }
        return $scope.year.length === 4 && !PORTAL.isNotAValidNumber($scope.year);
      }

      // this will be called whenever user enter a year in search box
      $scope.fetchHolidayList = function() {

        $rootScope.overlay = {
          show : true
        }

        if ($scope.isValidYear()) {

          $scope.previousYear = $scope.year;

          // TODO: use factory method get
          httpHitFactory.makePOSTHTTPHit(holidayCalendarJsonUrl, {
            "year" : $scope.year
          }, 'application/json').then(function(response) {
            // Return the myObject stored on the service
            if (response && response.data) {
              $rootScope.overlay.show = false;
              $scope.isAdmin = response.data.isAdmin;
              $scope.isSelectable = false;
              $scope.holidayCalendarList = response.data.HolidayCalendar;
              if ($scope.isAdmin === true) {
                $scope.isSelectable = true;
              }

              // newDate HOLDS THE DATE OF SEARCHED YEAR OR IT WILL HOLD THE
              // DATE OF HOLIDAY PRESENT IN HOLIDAYLIST FROM DATA BASE
              var newDate = new Date($scope.year, 0, 1);

              if ($scope.holidayCalendarList.length > 0) {
                newDate = $scope.holidayCalendarList[0].start;
              } else if (parseInt(curryear) === parseInt($scope.year)) {
                newDate = new Date();
              }

              if ($scope.calendar) {
                $scope.calendar.fullCalendar('destroy');
              }

              $scope.createCalendar($("#holidayDispCalendar"), $scope.holidayCalendarList, $scope.isAdmin, $scope.isSelectable, newDate);
            } else {
              if (response.data.errorMsg) {

                ErrorFactory.setmsg(response.data.errorMsg);
              }
              ErrorFactory.setURL($location.url());
              $rootScope.overlay.show = false;
              // redirecting to error page if json http request return error
              $location.url('/error.html');
            }

          }, function error(response) {
            if (response.data.errorMsg) {

              ErrorFactory.setmsg(response.data.errorMsg);
            }
            ErrorFactory.setURL($location.url());
            $rootScope.overlay.show = false;
            // redirecting to error page if json http request return error
            $location.url('/error.html');
          });
        } else {
          $rootScope.overlay.show = false;
          PORTAL.showMessage("Please enter a valid value for year.");
        }
      };

      // fetching json object for holiday type data by calling a factory method

      // makeHTTPHit is a factory method which is used for http.get() hit and
      // will return which type of hit is returned

      $scope.fetchHolidayList();
      httpHitFactory.makeHTTPHit(leaveTypeJsonUrl).then(function(response, $location) {
        $rootScope.overlay.show = false;
        $scope.isAdmin = response.data.isAdmin;
        $scope.holidayTypeList = response.data.LeaveType;
        $scope.dispHolidayTypeStatus = {
          isNameValid : true,
          isDaysValid : true
        };
      }, function error(response) {
        if (response) {

          if (response.data.errorMsg) {

            ErrorFactory.setmsg(response.data.errorMSg);
          }

          ErrorFactory.setURL($location.url());
          $rootScope.overlay.show = false; // redirecting to error page if json
          // http request return error
          $location.url('/error.html');
        }
      });

      // this fucntion change the color of the event which are having date less
      // than
      // current date in holiday list
      $scope.disableEvent = function(eventDate) {
        var currDate = new Date();
        // var todayDate = currDate.getMonth();
        currDate = PORTAL.formatDate(currDate, 'YYYY-MM-DD');
        // console.log(currDate);
        if (eventDate < currDate) {
          return 'disableEvent';
        }
      };

      // template for edit/delete/display for LeaveType
      //       
      $scope.getTemplate = function(data) {
        if (data.id == null || data.id != $scope.selectedHolidayTypeData.id) {
          return 'displayHolidayType';
        } else {
          return 'editHolidayType';
        }
      };

      /*
       * template ends
       */

      // THIS FUNCTION RESET THE SUBMIT DATA BLOCK for LeaveType
      $scope.resetNewHolidayBlock = function() {
        $scope.newHolidayType = {};
        $scope.isHolidayNameValid = false;
        $scope.isHolidayDaysValid = false;
        $scope.isNew = true;
      }

      $scope.resetNewHolidayBlock();
      // methods for validation called by ng-change in html and helper methods
      // used in these methods are present in the begining of this script
      $scope.isValidHolidayName = function(id) {
        var name;

        if (PORTAL.isValueUndefined(id)) {
          $scope.isNew = false;
          name = $scope.newHolidayType.name;
          $scope.isHolidayNameValid = isHolidayNameValid(name, $scope.holidayTypeList, -1);
        } else {
          name = $scope.selectedHolidayTypeData.name;
          $scope.dispHolidayTypeStatus.isNameValid = isHolidayNameValid(name, $scope.holidayTypeList, id);
        }
      };

      $scope.isValidHolidayDays = function(id) {
        var days;
        if (PORTAL.isValueUndefined(id)) {
          $scope.isNew = false;
          days = $scope.newHolidayType.days;
          $scope.isHolidayDaysValid = isHolidayDaysValid(days);
        } else {
          days = $scope.selectedHolidayTypeData.days;
          $scope.dispHolidayTypeStatus.isDaysValid = isHolidayDaysValid(days);
        }
      };

      // check of submit button disabled
      $scope.canSubmit = function() {
        if ($scope.isHolidayNameValid && $scope.isHolidayDaysValid) {
          return true;
        } else {
          return false;
        }
      }

      // validation ends

      // edit button function for LeaveType
      $scope.editHolidayData = function(selectedData, idx) {
        $scope.selectedHolidayTypeData = angular.copy(selectedData);
      };

      // delete LeaveType function: to delete the holiday type
      $scope.deleteHoliday = function(index) {
        $rootScope.overlay.show = true;
        var isDelete = confirm("Do you want to delete this team? This will delete leave type form system to");
        if (isDelete === true) {

          httpHitFactory.makePOSTHTTPHit(deleteLeaveTypeJSonURL, $scope.holidayTypeList[index], contentType).then(
              function mysuccess(data, headers, config) {
                if (data.data && data.data.status === "SUCCESS") {
                  $scope.holidayTypeList.splice(index, 1);
                } else {
                  PORTAL.showMessage("could not delete holiday type");
                }
                $rootScope.overlay.show = false;
              }, function error(response) {
                // HIDING overlay
                $rootScope.overlay.show = false;
                if (response) {
                  if (response) {
                    if (response.data.errorMsg) {

                      ErrorFactory.setmsg(response.data.errorMsg);
                    }
                    ErrorFactory.setURL($location.url());
                    // redirecting to error page if json http request return
                    // error
                    $location.url('/error.html');
                  }
                }

              });
        } else {
          $rootScope.overlay.show = false;
        }
      };

      // this function cancel edit of selected row for LeaveType
      $scope.cancelEdits = function() {

        $scope.selectedHolidayTypeData = {};
        $scope.dispHolidayTypeStatus = {
          isNameValid : true,
          isDaysValid : true
        };

      };

      $scope.reset = function() {
        $scope.selectedHolidayTypeData = {};
      };

      // save function to update the edited data fro LeaveType section.
      $scope.saveEdits = function(idx) {
        $rootScope.overlay.show = true;
        return httpHitFactory.makePOSTHTTPHit(editLeaveType, $scope.selectedHolidayTypeData, contentType).then(function(response) {
          if (response.data.status === "SUCCESS") {
            $rootScope.overlay.show = false;
            for (var idx = 0; idx < $scope.holidayTypeList.length; idx++) {
              if ($scope.holidayTypeList[idx].id === $scope.selectedHolidayTypeData.id) {
                $scope.holidayTypeList[idx] = angular.copy($scope.selectedHolidayTypeData);
                $scope.reset('HolidayType');
                break;
              }
            }
            $rootScope.overlay.show = false;
          } else {
            $rootScope.overlay.show = false;
            if (response.data.errorMsg) {

              ErrorFactory.setmsg(response.data.errorMsg);
            }
            ErrorFactory.setURL($location.url());
            // redirecting to error page if json http request return error
            $location.url('/error.html');
          }
        }, function myError(response) {
          if (response) {
            if (response.data.errorMsg) {

              ErrorFactory.setmsg(response.data.errorMsg);
            }
            ErrorFactory.setURL($location.url());
            // redirecting to error page if json http request return error
            $location.url('/error.html');
          }

        });

      };

      // this function is for submitting new LeaveType and making ajax hit
      var addNewHolidayType = leavePolicyJsonUrl + "/leavetype/add";
      $scope.submitNewHoliday = function() {
        // setting overlay true
        // $scope.newHolidayType.id = ++id;
        $rootScope.overlay.show = true;
        // this is a factory method for post hit
        httpHitFactory.makePOSTHTTPHit(addLeaveType, $scope.newHolidayType, contentType).then(function(response) {
          if (response.data && response.data.status === "SUCCESS") {
            $scope.newHolidayType.id = response.data.id;
            $scope.holidayTypeList.push($scope.newHolidayType);
            $scope.resetNewHolidayBlock();
            PORTAL.showMessage("data updated successfully");
            $rootScope.overlay.show = false;
          } else {
            if (response.data.errorMsg) {

              ErrorFactory.setmsg(response.data.errorMsg);
            }
            ErrorFactory.setURL($location.url());
            // redirecting to error page if json http request return error
            $location.url('/error.html');
            $rootScope.overlay.show = false;
          }
        }, function myError(response) {
          if (response) {
            if (response.data.errorMsg) {

              ErrorFactory.setmsg(response.data.errorMsg);
            }
            ErrorFactory.setURL($location.url());
            // redirecting to error page if json http request return error
            $location.url('/error.html');
          }

        });
        $rootScope.overlay.show = false;
      };
    });
angular.module('App').controller("employeeLeavesCtrl", function($scope, $location, $rootScope, ErrorFactory, httpHitFactory, $timeout) {

  // this variable is for holding the content-type required for post hit and
  // is used in every post hit
  var contentType = 'application/json';

  // UI ACTIONS
  var leaveReportBaseURL = attendanceUrl + "/leavereport";
  var calendarDataURL = leaveReportBaseURL + "/calendar";
  // UI actions end

  var date = new Date();
  var curryear = date.getFullYear();
  $scope.year = "" + curryear;
  $scope.prevYear = null;

  $rootScope.overlay = {
    show : false
  }

  var calendar;
  $timeout(function() {
    calendar = $("#leaveReportCalendar").fullCalendar({
      editable : false,
      eventLimit : true, // allow "more" link when too many events
    });

  });

  $scope.createCalendar = function() {
    $timeout(function() {
      calendar.fullCalendar('destroy');
      calendar = $("#leaveReportCalendar").fullCalendar({
        editable : false,
        eventLimit : true, // allow "more" link when too many events
        events : $scope.calendar_data
      });

    });
  }

  $scope.$watch("calendar_data", function(newValue, oldValue) {
    if (newValue && newValue.length > 0) {
      $(".nav-tabs " + 'a[href="#calendarview"]').tab('show');
    }
  });

  $(document).ready(function() {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
      var tab = e.currentTarget.hash
      if (tab == '#calendarview') {
        calendar.fullCalendar('render');
      }
    });
  });

  $scope.getCalendarData = function(index) {
    if (PORTAL.isValueUndefined($scope.leaveReport_data) || index < 0 || index >= $scope.leaveReport_data.length) {
      PORTAL.showMessage("Problem with the data");
      return;
    }

    $scope.chart_data = null;

    try {

      $scope.calendar_data = [];
      $scope.calendarDispData = null;
      $scope.calendarDispData = {
        name : $scope.leaveReport_data[index].name,
        year : $scope.year
      }

      // user_id will be used only in case of specific user leaveReport
      var calendarRequestData = {
        user_id : $scope.leaveReport_data[index].user_id,
        year : $scope.year
      };

      $rootScope.overlay = {
        show : true
      }

      httpHitFactory.makePOSTHTTPHit(calendarDataURL, calendarRequestData, contentType).then(function mySuccess(response) {

        var isError = false;
        if (response && response.data) {
          if (response.data.status === "SUCCESS") {
            if (!PORTAL.isValueUndefined(response.data.calendar_data)) {
              $scope.calendar_data = response.data.calendar_data;
            } else {
              $scope.calendar_data = [];
            }

            if (response.data.chart_data) {
              $scope.chart_data = response.data.chart_data;
            }

            $scope.createCalendar();
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

      }, function myError(response) {

        ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
        ErrorFactory.setURL($location.url());
        $location.url('/error.html');

        // HIDING overlay
        $rootScope.overlay.show = false;
      });
    } catch (err) {
      PORTAL.showMessage("Please check the data entered");
    }
  }

  $scope.getLeaveReportData = function() {
    try {
      
      if ($scope.year === $scope.prevYear) {
        return;
      }

      $scope.leaveReport_data = [];

      var leaveReportData = {
        year : $scope.year
      };

      $rootScope.overlay = {
        show : true
      }

      httpHitFactory.makePOSTHTTPHit(leaveReportBaseURL, leaveReportData, contentType).then(function mySuccess(response) {

        var isError = false;
        if (response && response.data) {
          if (response.data.status === "SUCCESS") {
            $scope.prevYear = $scope.year;
            if (!PORTAL.isValueUndefined(response.data.leaveReport_data)) {
              $scope.leaveReport_data = response.data.leaveReport_data;
              $timeout(function() {
                $('#leaveReport_table').dataTable();
              });
            }
          } else if (response.data.errorMsg) {
            ErrorFactory.setmsg(response.data.errorMsg);
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

      }, function myError(response) {

        ErrorFactory.setmsg("There had been an error. Please contact the System Administrator");
        ErrorFactory.setURL($location.url());
        $location.url('/error.html');

        // HIDING overlay
        $rootScope.overlay.show = false;
      });
    } catch (err) {
      PORTAL.showMessage("Please check the data entered");
    }

  }

  $scope.getLeaveReportData();
});
angular.module('App').controller("manualCtrl", function($scope, $location, $rootScope, ErrorFactory, httpHitFactory) {

  // this variable is for holding the content-type required for post hit and
  // is used in every post hit
  var contentType = 'application/json';

  $scope.isDataFetched = false;

  var isInTimeChanged=false;
  var isOutTimeChanged=false;
  $scope.selectedEmployeeData={};
  $scope.tempData = {
  };

  // this variable acts as a url for fetching attendance list from data base 
  var usersAttendanceList = usersDataJsonURL + "/attendancelist";

  // date picker functionalities
  $scope.currentDate = new Date();

  $scope.dpObj = {
      opened : false
  };

  $scope.openDatePicker = function() {
    $scope.dpObj.opened = true;
    $scope.isDataFetched=false;
  };

  // this function gets the attendance records of all the users by making a post hit
  var fetchAttendanceDataForAllUsers = function (){
    httpHitFactory.makePOSTHTTPHit(usersAttendanceList, $scope.entry, contentType).then(function mysuccess(response) {

      $scope.empList = response.data.empList;      
      $scope.isAdmin = response.data.isAdmin;
      $scope.entry.in_time=new Date();
      $scope.entry.out_time=new Date();

    }, function myError(response) {
      // HIDING overlay
      $rootScope.overlay = {
          show : false
      }
      if (response.status) {
        ErrorFactory.setmsg("Error");
        ErrorFactory.setURL($location.url());
        // redirecting to error page if json http request return error
        $location.url('/error.html');
      }
    });
  }

  // this function fetches attendance records only when the input date is greater than the current date
  $scope.$watch("tempData.entryDate", function(newValue, oldValue) {
    if (angular.isUndefined(newValue)) {
    } else {
      $scope.entry.date = PORTAL.formatDate(newValue, "DD/MM/YYYY");

      if(new Date() < newValue){
        PORTAL.showMessage("Please enter a valid date");
      }
      else{
        fetchAttendanceDataForAllUsers();
        $scope.isDataFetched=true;
      }
    }
  });

  $scope.$watch("tempData.inTime", function(newValue, oldValue){
    if(angular.isUndefined(newValue)){      
    } else {
      isInTimeChanged = true;
      console.log("In time changed.");
    }
  });

  $scope.$watch("tempData.outTime", function(newValue, oldValue){
    if(angular.isUndefined(newValue)){
    } else {
      isOutTimeChanged = true;
      console.log("Out time changed");
    }
  });

  $scope.setTime = function(empId) {
    $scope.entry.in_time = PORTAL.formatTime($scope.tempData.inTime, "HH:mm");
    $scope.entry.out_time = PORTAL.formatTime($scope.tempData.outTime, "HH:mm");
    $scope.saveChanges(empId);
  }

  $scope.sortProperty ;
  $scope.reverse = false;

  $scope.sortBy = function(propertyName) {
    $scope.reverse = ($scope.sortProperty === propertyName) ? !$scope.reverse : true;
    $scope.sortProperty = propertyName;
  };

  // template for selecting edit mode and display mode
  $scope.getTemplate = function(employee){
    if(employee.id == null){
      return 'display';
    } 

    if($scope.selectedEmployeeData == null){
      return 'edit';
    }

    if(employee.id === $scope.selectedEmployeeData.id){
      return 'edit';
    } else {
      return 'display';
    }
  }

  // function to copy employee to be edited in selectedEmployeeData
  $scope.editTimeEntryData=function(selectedEmployee){
    $scope.selectedEmployeeData = angular.copy(selectedEmployee);
  }

  $scope.cancelEdits = function(){
    $scope.selectedEmployeeData = {};
  }

  // check if the in time entry fetched from database is empty or not
  // if empty current time is displayed else time retrieved from database
  $scope.isInTimeSet = function(empInTime){
    if(empInTime == ""){
      return false;
    } else {
      return true;
    }
  }

  // checks if the out time entry fetched from the database is empty or not
  // if empty current time is displayed else time retrieved from database
  $scope.isOutTimeSet = function(empOutTime){
    if(empOutTime == ""){
      return false;
    } else {
      return true;
    }
  }

  // function to save the time entry
  $scope.saveChanges = function(empId) {
    var timeData = [];
    if(isInTimeChanged){
      input = {
          id : empId ,  
          name : "in_time",
          entryTime : angular.copy($scope.entry.in_time)
      };
      timeData.push(input);
    }

    if(isOutTimeChanged){
      input={
          name : "out_time",
          entryTime : angular.copy($scope.entry.out_time)
      };
      timeData.push(input);
    }

    var entries ={
        date : angular.copy($scope.entry.date),
        time : angular.copy(timeData)
    };

    var data = {
        employee : angular.copy($scope.selectedEmployeeData),
        datetime : entries
    };

    var addManualAttendanceEntryURL = randomUtilitiesURL + "/manualEntry";
    // this function makes a post hit to save the entries in database 
    // and delete entries if required
    // and update the in_time and out_time in json list to reflect the changes on the page
    httpHitFactory.makePOSTHTTPHit(addManualAttendanceEntryURL, data, contentType).then(function mySuccess(response) {

      if (response.data && response.data.status) {
        var message;
        if (response.data.status === "SUCCESS") {
          message = "Entry saved";

          for(var idx = 0; idx<$scope.empList.length; idx++){
            if($scope.empList[idx].id === empId) {

              if(isInTimeChanged){
                $scope.empList[idx].in_time = $scope.entry.in_time;
              }
              if(isOutTimeChanged){
                $scope.empList[idx].out_time = $scope.entry.out_time;
              }
              console.log($scope.empList[idx].in_time);
              console.log($scope.empList[idx].out_time);
              break;
            }
          }

          isInTimeChanged=false;
          isOutTimeChanged=false;
        } else {
          message = "Could not save the attendance entry. Please contact the Administrator";
        }
        PORTAL.showMessage(message);
      } else {
        PORTAL.showMessage("Could not save the attendance entry. Please contact the Administrator");
      }

    }, function myError(response) {
      // HIDING overlay
      $rootScope.overlay = {
          show : false
      }
      if (response.status) {
        ErrorFactory.setmsg("Error");
        ErrorFactory.setURL($location.url());
        // redirecting to error page if json http request return error
        $location.url('/error.html');
      }
    });
    
    $scope.selectedEmployeeData = {};
  };

  $scope.entry={
  };

});
//change this url with the url from the server that would be providing the json data for the user profile.
angular.module('App').controller(
    "ProfileController",
    function($scope, $http, $routeParams, $location, $rootScope, ErrorFactory, $route, httpHitFactory) {

      // this variable is for holding the content-type requird for post hit and
      // is used in every post hit
      var contentType = 'application/json';

      // OVERLAY IS SET TO TRUE BEFORE PROCEESING A REQUEST
      $rootScope.overlay = {
        show : true
      }

      // DATEPICKER FUNCTIONLITY STARTS
      $scope.newDate = {
        dobCopied : new Date()
      }

      $scope.dpObj = {
        opened : false
      };

      $scope.openDatePicker = function() {
        $scope.dpObj.opened = true;
      };

      // calculate 15 years from now.
      var maxDate = PORTAL.getChangedYearDateClearFields(-15);

      $scope.dateOptions = {
        dateDisabled : false,
        maxDate : maxDate,
        startingDay : 1
      };

      // DATE PICKER FUNCTIONALITY ENDS

      // VARIBLES, ARRAYS FOR HOLDING THE DATA FROM BACK END && FRONT END

      // THIS WILL HOLD THE DATA WHICH WILL RECEIVED FROM BACKEND && IT WILL
      // REMAIN UNCHANGED
      $scope.initialProfileObject = [];
      $scope.initialTeamObject = [];

      // THIS WILL HOLD THE PROFILE DATA FROM BACKEND && ITS BINDING IS DONE
      // FROM FRONT END ANY CHANGES MADE FROM FRONT END WILL BE REFLECTED &&
      // STORED IN THIS ARRAY
      $scope.rowContent = [];

      // THIS VARIABLES DECIDES ON UI WETHER USER IS IN VIEW MODE OR EDIT MODE
      var isEdit = $routeParams.edit === 'edit';
      $scope.isEdit = isEdit;

      // change this id to the person who is logged in
      $scope.profile_user_id = $routeParams.id;

      // THESE ARE UI ACTIONS
      var userEditJsonURL;
      var userUpdateJsonURL = userDataJsonURL + "/update";

      if ($scope.isEdit === true) {
        userEditJsonURL = userDataJsonURL + "/edit";
      } else {
        userEditJsonURL = userDataJsonURL + "/view";
      }
      // UI ACTIONS ENDS

      // profile.json fetching function httpHitFactory.makeHTTPHit() this is a
      // factory method for hhtp.get() present in app.js and will return
      // response of http.get() hit
      // reading json file

      // this $scope.content{} hold user_id in form of an object which is
      // required in http.post hit and is consider as a json on back end
      $scope.content = {
        "user_id" : $scope.profile_user_id
      };
      httpHitFactory.makePOSTHTTPHit(userEditJsonURL, $scope.content, contentType).then(function mySuccess(response) {
        $rootScope.overlay = {
          show : true
        }
        if (response && response.data && (response.data.status === "SUCCESS")) {

          $scope.initialProfileObject = response.data;
          $scope.rowContent = angular.copy($scope.initialProfileObject);
          if (!PORTAL.isValueUndefined($scope.rowContent)) {
            $scope.attachValidators();
          }

          // function to hide overlay
          $rootScope.overlay = {
            show : false
          }

        } else {
          if (response.data.errorMsg) {

            ErrorFactory.setmsg(response.data.errorMsg);
          }
          ErrorFactory.setURL($location.url());
          $rootScope.overlay = {
            show : false
          }
          // redirecting to error page if json http request return error
          $location.url('/error.html');
        }

      }, function myError(response) {
        // HIDING overlay
        $rootScope.overlay = {
          show : false
        }

        if (response && response.data && response.data.status === "ERROR") {

          if (response.data.errorMsg) {

            ErrorFactory.setmsg(response.data.errorMsg);
          }
          ErrorFactory.setURL($location.url());
          // redirecting to error page if json http request return error
          $location.url('/error.html');
        }
      });

      $scope.isUndefined = function(val) {
        return (typeof val === "undefined") || val == null;
      }

      /*
       * validator function before submiting the form when user entering data
       * VALIDATION FUNCTIONALITY AT THE TIME WHEN USER IS ENTERING DATA TO
       * PROFILE
       */
      $scope.attachValidators = function() {

        if (!PORTAL.isValueUndefined($scope.rowContent.TeamDetails)) {
          // $scope.totalTeams = $scope.rowContent.TeamDetails.length;
        } else {
          // $scope.totalTeams = 0;
        }

        if (!PORTAL.isValueUndefined($scope.rowContent.PersonalDetails.Name)) {
          $scope.isNameValid = PORTAL.isValidString($scope.rowContent.PersonalDetails.Name, 5, 25);
        } else {
          $scope.isNameValid = false;
        }

        $scope.isValidName = function() {
          var name = $scope.rowContent.PersonalDetails.Name;
          // portal.isValidString will give false if name not valid IT WILL
          // RETURN FALSE IF NAME NOT VALID
          $scope.isNameValid = PORTAL.isValidString(name, 5, 25);
        }

        // DESIGNATION VALIDATION
        $scope.isDesignationValid = true;
        if (!PORTAL.isValueUndefined($scope.rowContent.PersonalDetails.Designation)) {
          $scope.isDesignationValid = PORTAL.isValidString($scope.rowContent.PersonalDetails.Designation, 5, 200);
        } else {
          $scope.isDesignationValid = false;
        }

        $scope.isValidDesignation = function() {
          var designation = $scope.rowContent.PersonalDetails.Designation;
          $scope.isDesignationValid = PORTAL.isValidString(designation, 5, 200);
        }

        // GENDER VALIDATION
        $scope.genderchanged;
        $scope.genderValid = function() {
          if ($scope.genderchanged != $scope.rowContent.PersonalDetails.Gender) {
            $scope.rowContent.PersonalDetails.Gender = $scope.genderchanged;
          }
        }

        // DATE OF BIRTH VALIDATIONS
        $scope.newDate.dobCopied = PORTAL.getDate($scope.rowContent.PersonalDetails.DOB);

        $scope.$watch("newDate.dobCopied", function(newValue, oldValue) {
          if (angular.isUndefined(newValue)) {
            // add validation code here
            $scope.isDOBValid = false;
          } else {
            $scope.rowContent.PersonalDetails.DOB = PORTAL.formatBirthdate(newValue);
            $scope.isDOBValid = true;
          }
        });

        if (!PORTAL.isValueUndefined($scope.rowContent.PersonalDetails.BloodGroup)) {
          $scope.isBloodGroupValid = PORTAL.isValidBloodGroup($scope.rowContent.PersonalDetails.BloodGroup);
        } else {
          $scope.isBloodGroupValid = true;
        }
        
        $scope.isValidBloodGroup = function() {
          var bg = $scope.rowContent.PersonalDetails.BloodGroup;
          $scope.isBloodGroupValid = PORTAL.isValidBloodGroup(bg);

        }

        // PASSPORT VALIDATION
        if (!PORTAL.isValueUndefined($scope.rowContent.PersonalDetails.PassportNo)) {
          $scope.isPassportValid = PORTAL.isValidPassport($scope.rowContent.PersonalDetails.PassportNo);
        } else {
          $scope.isPassportValid = false;
        }

        $scope.isValidPassport = function() {
          var pn = $scope.rowContent.PersonalDetails.PassportNo;
          $scope.isPassportValid = PORTAL.isValidPassport(pn);
        }

        // CONTACT DETAILS VALIDATIONS
        if (!PORTAL.isValueUndefined($scope.rowContent.ContactDetails)) {
          $scope.isPersonalContactValid = true;

          if (!PORTAL.isValueUndefined($scope.rowContent.ContactDetails.PERSONAL)) {
            $scope.isPersonalContactValid = PORTAL.isValidContact($scope.rowContent.ContactDetails.PERSONAL.number);
          }

          // on-change handler for personal contact
          $scope.isValidPersonalContact = function() {
            var contact = $scope.rowContent.ContactDetails.PERSONAL.number;

            $scope.isPersonalContactValid = PORTAL.isValidContact(contact);

            if ($scope.isPersonalContactValid === true) {
              if (contact === "") {
                delete $scope.rowContent.ContactDetails.PERSONAL;
              } else {
                $scope.rowContent.ContactDetails.PERSONAL.type = "P";
              }
            } else {
              $scope.rowContent.ContactDetails.PERSONAL.type = "";
            }
          }

          $scope.isOfficeContactValid = true;

          if (!PORTAL.isValueUndefined($scope.rowContent.ContactDetails.OFFICE)) {
            $scope.isOfficeContactValid = PORTAL.isValidContact($scope.rowContent.ContactDetails.OFFICE.number);
          }

          $scope.isValidOfficeContact = function() {
            var contact = $scope.rowContent.ContactDetails.OFFICE.number;
            $scope.isOfficeContactValid = PORTAL.isValidContact(contact);
            if ($scope.isOfficeContactValid === true) {
              if (contact === "") {
                delete $scope.rowContent.ContactDetails.OFFICE;
              } else {
                $scope.rowContent.ContactDetails.OFFICE.type = "F";
              }
            } else {
              $scope.rowContent.ContactDetails.OFFICE.type = "";
            }
          }

          $scope.isOtherContactValid = true;

          if (!PORTAL.isValueUndefined($scope.rowContent.ContactDetails.OTHER)) {
            $scope.isOtherContactValid = PORTAL.isValidContact($scope.rowContent.ContactDetails.OTHER.number);
          }

          $scope.isValidOtherContact = function() {
            var contact = $scope.rowContent.ContactDetails.OTHER.number;
            $scope.isOtherContactValid = PORTAL.isValidContact(contact);
            if ($scope.isOtherContactValid === true) {
              if (contact === "") {
                delete $scope.rowContent.ContactDetails.OTHER;
              } else {
                $scope.rowContent.ContactDetails.OTHER.type = "O";
              }
            } else {
              $scope.rowContent.ContactDetails.OTHER.type = "";
            }
          }
        }

        // ADDRESS VALIDATION STARTS
        if (!PORTAL.isValueUndefined($scope.rowContent.AddressDetails.HNoStreet)) {
          $scope.isHNoValid = PORTAL.isValidHno($scope.rowContent.AddressDetails.HnoStreet);
        } else {
          $scope.isHNoValid = false;
        }
        $scope.isValidHno = function() {
          var hno = $scope.rowContent.AddressDetails.HnoStreet;
          $scope.isHNoValid = PORTAL.isValidHno(hno);

        }

        if (!PORTAL.isValueUndefined($scope.rowContent.AddressDetails.State)) {
          $scope.isStateValid = PORTAL.isValidString($scope.rowContent.AddressDetails.State, 5, 50);
        } else {
          $scope.isStateValid = false;
        }
        
        $scope.isValidState = function() {
          var state = $scope.rowContent.AddressDetails.State;
          $scope.isStateValid = PORTAL.isValidString(state, 5, 50);

        }

        if (!PORTAL.isValueUndefined($scope.rowContent.AddressDetails.Pin)) {
          $scope.isPinValid = PORTAL.isValidPin($scope.rowContent.AddressDetails.Pin);
        } else {
          $scope.isPinValid = false;
        }
        $scope.isValidPin = function() {
          var pin = $scope.rowContent.AddressDetails.Pin;
          $scope.isPinValid = PORTAL.isValidPin(pin);
        }

        if (!PORTAL.isValueUndefined($scope.rowContent.AddressDetails.Country)) {
          $scope.isCountryValid = PORTAL.isValidString($scope.rowContent.AddressDetails.Country, 5, 25)
        } else {
          $scope.isCountryValid = false;
        }
        $scope.isValidCountry = function() {
          var country = $scope.rowContent.AddressDetails.Country;
          $scope.isCountryValid = PORTAL.isValidString(country, 5, 25);
        }

        $scope.genderchanged;
        $scope.genderValid = function() {
          if ($scope.genderchanged != $scope.rowContent.PersonalDetails.Gender) {
            $scope.rowContent.PersonalDetails.Gender = $scope.genderchanged;
          }
        }

        /*
         * IT WILL DECIDE WETHER THE SUBMIT BUTTON WILL BE DISABLED OR ENABLED
         * BY RETURNING TRUE IF STATUS OF ALL THE FIELDS ARE OK ACC TO THEIR
         * SCOPE ALSO IT WILL CHECK IF THE DATA PRESENT AFTER EDITING IS SAME AS
         * THE DATA RECEIVED FROM BACKEND THEN ALSO SUBMIT BUTTON WILL BE
         * DISABLED
         * 
         */
        $scope.isAllInputValid = function() {
          // THIS WILL CHECK IF NEW JSON AND INTIAL JSON IS SAME OR NOT
          if (this.isJsonUnchanged()) {
            return false;
          }

          return $scope.rowContent.canEdit
              && ($scope.isNameValid && (!$scope.rowContent.isAdmin || $scope.isDesignationValid) && $scope.isDOBValid && $scope.isBloodGroupValid
                  && $scope.isPassportValid && $scope.isHNoValid && $scope.isStateValid && $scope.isPinValid && $scope.isCountryValid
                  && $scope.isPersonalContactValid && $scope.isOfficeContactValid && $scope.isOtherContactValid);

        }

        // TESTING Already present teams for adding new team in user profile
        $scope.testTeamName = function() {
          $scope.testTeamNameflag = false;
          if (!PORTAL.isValueUndefined($scope.rowContent.TeamDetails)) {

            for (var i = 0; i < $scope.rowContent.TeamDetails.length; i++) {
              if ($scope._selectedTeam.TeamName === $scope.rowContent.TeamDetails[i].TeamName) {
                $scope.testTeamNameflag = false;
                return;
              }
            }

            for (var i = 0; i < $scope.sessionTeams.length; i++) {
              if ($scope._selectedTeam.TeamName === $scope.sessionTeams[i].TeamName) {
                $scope.testTeamNameflag = false;
                return;
              }
            }
          }
          $scope.testTeamNameflag = true;
        }
      }

      /*
       * THIS API CHECKS IF NEW JSON AND INTIAL JSON IS SAME OR NOT AND IS
       * CALLED FROM ISALLINPUTVALID () IF SAME JSON THEN NO NEED TO ENABLE
       * SUBMIT BUTTON
       */
      $scope.isJsonUnchanged = function() {

        if ($scope.rowContent.PersonalDetails.Name != $scope.initialProfileObject.PersonalDetails.Name) {
          return false;
        }

        if ($scope.rowContent.PersonalDetails.Designation != $scope.initialProfileObject.PersonalDetails.Designation) {
          return false;
        }

        if ($scope.rowContent.PersonalDetails.DOB != $scope.initialProfileObject.PersonalDetails.DOB) {
          return false;
        }

        if ($scope.rowContent.PersonalDetails.BloodGroup != $scope.initialProfileObject.PersonalDetails.BloodGroup) {
          return false;
        }
        if ($scope.rowContent.PersonalDetails.Gender.code != $scope.initialProfileObject.PersonalDetails.Gender.code) {
          return false;
        }

        if ($scope.rowContent.PersonalDetails.PassportNo != $scope.initialProfileObject.PersonalDetails.PassportNo) {
          return false;
        }

        if ($scope.rowContent.ContactDetails.PERSONAL.number != $scope.initialProfileObject.ContactDetails.PERSONAL.number) {
          return false;
        }
        if ($scope.rowContent.ContactDetails.OFFICE.number != $scope.initialProfileObject.ContactDetails.OFFICE.number) {
          return false;
        }

        if ($scope.rowContent.ContactDetails.OTHER.number != $scope.initialProfileObject.ContactDetails.OTHER.number) {
          return false;
        }

        if ($scope.rowContent.AddressDetails.HNoStreet != $scope.initialProfileObject.AddressDetails.HNoStreet) {
          return false;
        }

        if ($scope.rowContent.AddressDetails.State != $scope.initialProfileObject.AddressDetails.State) {
          return false;
        }

        if ($scope.rowContent.AddressDetails.Pin != $scope.initialProfileObject.AddressDetails.Pin) {
          return false;
        }

        if ($scope.rowContent.AddressDetails.Country != $scope.initialProfileObject.AddressDetails.Country) {
          return false;
        }

        // added new team in session
        if ($scope.sessionTeams.length > 0) {
          return false;
        }

        // deleted a team from originally existing teams
        if ($scope.rowContent.TeamDetails.length != $scope.initialProfileObject.TeamDetails.length) {
          return false;
        }

        return true;
      }

      /*
       * TYPEAHAED CODE FOR DISPLAYING TEAM NAME
       */
      $scope.flag = true;
      $scope._selectedTeam = {};
      $scope.sessionTeams = [];

      // Add a new team code : select team
      $scope.onSelect = function($item, $model, $label) {
        $scope._selectedTeam = $item;
        $scope.flag = true;
        $scope.testTeamName();
      };

      // this function will pre populate the team
      $scope.getTeamName = function(val) {

        // this method will hit a http.post request for making post request and
        // httpHitFactory.makePOSTHTTPHit() is present in app.js
        return httpHitFactory.makePOSTHTTPHit(teamsByNameJsonURL, val, contentType).then(function(response) {
          if (response.data.teamsList) {
            return response.data.teamsList.map(function(item) {
              console.log(item);
              return item;

            });
          }
        }, function myError(response) {
          console.error('failure loading the  record', response.data);
        });
      };

      /*
       * add team code for user profile
       */
      $scope.testTeamNameflag = true;
      $scope.addTeam = function() {
        if ($scope._selectedTeam.TeamName != null) {
          // $scope.testTeamNameflag = true;
          // this.testTeamName();
          if ($scope.testTeamNameflag) {
            $scope.sessionTeams.push($scope._selectedTeam);
          }
        }
        $scope._selectedTeam = {};
      }

      /**
       * this method will delete user teams which are received with json which
       * are stored in user profile in db
       */
      $scope.deleteUserTeam = function(name) {
        $rootScope.overlay = {
          show : true
        }
        var index = -1;
        var commonArray = $scope.rowContent.TeamDetails;
        for (var i = 0; i < commonArray.length; i++) {
          if (commonArray[i].TeamName === name) {
            index = i;
            break;
          }
        }
        if (index === -1) {
          PORTAL.showMessage("Team is not deleted");
          $rootScope.overlay = {
            show : false
          }
        } else {
          if (commonArray[index].isSupervisor === true) {
            PORTAL.showMessage("To delete this team from the list, please change the supervisor from Teams tab");
            $rootScope.overlay = {
              show : false
            }
          } else {
            $scope.rowContent.TeamDetails.splice(index, 1);
          }
        }
        $rootScope.overlay = {
          show : false
        }
        // $scope.totalTeams = $scope.rowContent.TeamDetails.length;
      };

      /**
       * this method will be called when user wants to delete "current session"
       * teams which are new in profile
       */
      $scope.deleteUserSessionTeam = function(name) {
        $rootScope.overlay = {
          show : true
        }
        var index = -1;
        var commonArray = $scope.sessionTeams;
        for (var i = 0; i < commonArray.length; i++) {
          if (commonArray[i].TeamName === name) {
            index = i;
            break;
          }
        }

        if (index === -1) {
          PORTAL.showMessage("CAN NOT DELETE TEAM");
          $rootScope.overlay = {
            show : false
          }
        } else {
          $scope.sessionTeams.splice(index, 1);
        }
        $rootScope.overlay = {
          show : false
        }
      };

      // View Team
      $scope.sortTeamProperty;
      $scope.reverse = false;

      $scope.sortTeamBy = function(propertyName) {
        $scope.reverse = ($scope.sortTeamProperty === propertyName) ? !$scope.reverse : true;
        $scope.sortTeamProperty = propertyName;
      };

      /*
       * SUBMIT BUTTON TO SAVE CHANGED MADE FON UI ON USER PROFILE
       */
      $scope.submit = function() {

        // DOING TEAM STUFF OF SESSION TEAMS BY ADDING TEAMS EXCLUDING ALREADY
        // PRESENT IN INITAL JSON TO TO FINAL JSON
        if ($scope.sessionTeams.length != 0) {
          for (var i = 0; i < $scope.sessionTeams.length; i++) {
            $scope.rowContent.TeamDetails.push($scope.sessionTeams[i]);
          }
        }

        // this method will make an hhtp.post hit for post request and
        // httpHitFactory.makePOSTHTTPHit() is present in app.js
        httpHitFactory.makePOSTHTTPHit(userUpdateJsonURL, $scope.rowContent, contentType).then(function mysuccess(response) {
          if (response.data.status === "SUCCESS") {
            PORTAL.showMessage("Changes saved successfully");
            // redirect to view page: "#!/profile/view/" + id
            $route.reload();
          } else {
            PORTAL.showMessage(response.data.errorMsg);
          }

        }, function error(response) {

          // HIDING overlay
          $rootScope.overlay.show = false;

          if (response && response.data && response.data.errorMsg) {
            ErrorFactory.setmsg(response.data.errorMsg);
          }

          ErrorFactory.setURL($location.url());
          // redirecting to error page if json http request return error
          $location.url('/error.html');

        });
      };

      // PROFILE PIC UPLOAD FUNCTIONALITY BEIGINS

      // FUNCTION TO OPEN MODEL FOR PROFILE PICTURE
      $scope.profilePic = function() {
        $("#profile_pic").dialog({
          modal : true,
          width : 330,
          closeOnEscape : false,
          open : function(event, ui) {
            $(document).on('click', '.browse', function() {
              var file = $(this).parent().parent().parent().find('.file');
              file.trigger('click');
            });
            $(document).on('change', '.file', function() {
              $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
            });
          },
          close : function() {
            $(".browse").unbind('click');
            $(".file").unbind('change');
          }
        });

      };

      // FUNCTION TO SUBMIT PROFILE PICTURE (ADD/EDIT)
      $scope.upload = function() {
        $rootScope.overlay = {
          show : true
        }
        var form_data = new FormData(); // Creating object of FormData class
        var files = $("#file").prop('files');
        for (var i = 0; i < files.length; i++) {
          form_data.append('file', files[i]);
        }
        form_data.append("user_id", $scope.profile_user_id); // Adding extra
        // parameters to
        // form_data

        if ($scope.rowContent.profilePic === null) {
          PORTAL.showMessage("PLEASE SELECT A PICTURE BEFORE UPLOADING");
          $('#profile_pic').dialog('close');
        } else {

          /*
           * $http({ method : 'POST', url : userDataJsonURL + "/picture/add",
           * data : userDataJsonURL + "/picture/add", headers : { 'Content-Type' :
           * undefined }, //transformRequest : angular.identity, })
           */
          httpHitFactory.makePOSTHTTPHit(userDataJsonURL + "/picture/add", form_data, undefined).then(function mysucess(response) {
            $rootScope.overlay = {
              show : false
            }

            if (response && response.data && response.data.status === "SUCCESS") {
              $scope.rowContent.profilePic = response.data.profilePic;
              PORTAL.showMessage("PROFILE PICTURE UPLOADED SUCCESSFULLY");
              $('#profile_pic').dialog('close');
            } else {
              $scope.rowContent.profilePic = $scope.initialProfileObject.profilePic;
              PORTAL.showMessage("ERROR IN UPLOADING PROFILE PICTURE");
              $('#profile_pic').dialog('close');
            }

          }, function error(response) {
            $rootScope.overlay = {
              show : false
            }
            if (response && response.data && response.data.status === "ERROR") {
              $scope.rowContent.profilePic = $scope.initialProfileObject.profilePic;
              PORTAL.showMessage("ERROR IN UPLOADING PROFILE PICTURE");
              $('#profile_pic').dialog('close');
            }
          });
        }

      };

      // FUNCTION TO REMOVE PROFILE PICTURE
      $scope.remove = function() {
        $rootScope.overlay = {
          show : true
        }

        if ($scope.rowContent.profilePic === null) {
          PORTAL.showMessage("NO PROFILE PICTURE TO REMOVE");
          $('#profile_pic').dialog('close');

        } else {

          httpHitFactory.makePOSTHTTPHit(userDataJsonURL + "/picture/delete", $scope.content, contentType).then(function mySuccess(response) {
            $rootScope.overlay = {
              show : false
            }
            if (response && response.data && response.data.status === "SUCCESS") {
              $scope.rowContent.profilePic = null;
              PORTAL.showMessage("PROFILE PICTURE REMOVED SUCCESSFULLY");
              $('#profile_pic').dialog('close');

            } else {
              PORTAL.showMessage(response.data.errorMsg);
              $scope.rowContent.profilePic = $scope.initialProfileObject.profilePic;
              $scope.$apply(function() {
                $('#profile_pic').dialog('close');

              });
            }

          }, function error(response) {
            $rootScope.overlay = {
              show : false
            }
            if (response && response.data && response.data.status === "ERROR") {
              $scope.rowContent.profilePic = $scope.initialProfileObject.profilePic;
              PORTAL.showMessage(response.data.errorMsg);
              $('#profile_pic').dialog('close');
            }
          });
        }
      };
      // PROFILE PICTURE FUNCTIOANLY ENDS
    });
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
