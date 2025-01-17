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
