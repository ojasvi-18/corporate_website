var PORTAL = new function() {

  this.isValidString = function(str, min, max) {

    if (this.isEmptyString(str) || this.isOutOfBounds(str, min, max)) {
      return false;
    } else {
      return true;
    }
  }

  this.formatDate = function(dt, format) {
    if (!dt) {
      return "";
    }

    if (!format) {
      format = "MM-DD-YYYY";
    }

    var momentObj = moment(dt);
    return momentObj.format(format);
  }
  
  this.formatTime = function(dt, format) {
    if (!dt) {
      return "";
    }

    if (!format) {
      format = "HH:mm:ss";
    }

    var momentObj = moment(dt);
    return momentObj.format(format);
  }

  this.getChangedYearDateClearFields = function(year) {
    var date = new Date();

    return new Date(date.getFullYear() + (year ? year : 0), 0, 1);
  }

  this.formatBirthdate = function(dt) {
    return this.formatDate(dt, "MM-DD-YYYY");
  }

  this.isValidBloodGroup = function(str) {
    if (this.isEmptyString(str)) {
      return true;
    }

    switch (str) {
      case 'A+' :
      case 'B+' :
      case 'O+' :
      case 'AB+' :
      case 'A-' :
      case 'B-' :
      case 'O-' :
      case 'AB-' :
        return true;
    }

    return false;

  }

  // return false if passport is not empty and not in length bounds other wise
  // return true
  this.isValidPassport = function(str) {
    if (!this.isEmptyString(str)) {
      if (this.isOutOfBounds(str, 7, 9)) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  // checkbound length will send true if contact is not in bounds then
  // isValidContact will return false
  this.isValidContact = function(str) {
    if (this.isEmptyString(str)) {
      return true;
    }

    if (this.isOutOfBounds(str, 10, 10) || this.isNotAValidNumber(str)) {
      return false;
    } else {
      return true;
    }
  }

  this.isValidHno = function(str) {
    if (this.isEmptyString(str)) {
      return false;
    } else {
      return true;
    }
  }
  this.isValidPin = function(str) {
    if (this.isEmptyString(str) || this.isOutOfBounds(str, 5, 7) || this.isNotAValidNumber(str)) {
      return false;
    } else {
      return true;
    }
  }

  // check if string is null
  this.isEmptyString = function(str) {
    return angular.equals(str, "");
  }

  this.isValueUndefined = function(obj) {
    return angular.isUndefined(obj) || (obj == null);
  }

  this.isNotAValidNumber = function(str) {
    return (isNaN(str));
  }

  // check string length if not in bounds return true
  this.isOutOfBounds = function(str, lowerbound, upperbound) {
    if ((str.length < lowerbound) || (str.length > upperbound)) {
      return true;
    } else {
      return false;
    }
  }

  this.validateEmail = function(email) {
    return email.match(/^[a-zA-Z0-9]+([a-zA-Z0-9_]+(\.){0,1})*[a-zA-Z0-9]+@([a-zA-Z0-9]+(\.){1})+[a-zA-Z0-9]+$/);
  }
  // end of validation of teams.html

  this.validateDate = function(eventdate) {
    return eventdate.match(/([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/);
  }

  this.showMessage = function(message) {
    if (message == null || (typeof message === "undefined")) {
      message = "SOME ERROR OCCURED...TRY AGAIN";
      // alert(message);
      // } else {
      // alert(message);
    }

    if ($("#dialog-message-modal").length > 0 && $("#dialog-message").length > 0) {
      $("#dialog-message").html(message);
      $("#dialog-message-modal").dialog({
        modal : true,
        buttons : {
          Dismiss : function() {
            $(this).dialog("close");
          }
        }
      });
    }
  }

  this.getDate = function(dt, format) {
    if (!format) {
      format = "MM-DD-YYYY";
    }
    return new Date(moment(dt, format).format());
  }

  this.switchToTab = function(tabName) {
    if (!tabName) {
      return;
    }

    var tabElem = $(".nav-tabs " + 'a[href="#' + tabName + '"]');
    if ($(tabElem).length > 0) {
      $(tabElem).tab('show');
    }
  }
}
