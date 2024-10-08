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
