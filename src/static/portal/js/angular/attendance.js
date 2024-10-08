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
