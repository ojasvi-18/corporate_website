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
