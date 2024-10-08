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
