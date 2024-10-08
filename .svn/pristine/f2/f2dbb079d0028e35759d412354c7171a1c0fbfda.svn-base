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
