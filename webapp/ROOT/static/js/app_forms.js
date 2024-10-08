function contactJS(form, action) {
  this.form = form;
  this.formAction = action;
  var _self = this;

  this.init = function() {
    $(this.form).on('submit', function(e) {
      e.preventDefault();
      var validStatus = _self.validateContactForm(this);
      if (validStatus == true) {
        $(_self.form).attr('action', _self.formAction);
        this.submit();
      }
    });
  }
  this.validateContactForm = function(formElem) {
    var queryType = $(formElem).find('select[name="query"]').val();
    if (!$.isNumeric(queryType) || !this.isEnglish(queryType)) {
      this.addError("corp.client.contact.invalidquery", true);
      return false;
    }
    var phoneNumber = $(formElem).find('input[name="phone"]').val();
    if (phoneNumber.match(/[a-z]/i) || !this.isEnglish(phoneNumber)) {
      this.addError("corp.client.contact.invalidphone", true);
      return false;
    }
    var name = $(formElem).find('input[name="name"]').val();
    var nameRegex = /^[a-zA-Z ]{2,30}$/;
    if (!nameRegex.test(name)) {
      this.addError("corp.client.contact.invalidname", true);
      return false;
    }
    var message = $(formElem).find('textarea[name="message"]').val();
    if (!this.isEnglish(message)) {
      this.addError("corp.client.contact.invalidMessage", true);
      return false;
    }

    var googleResponse = jQuery('#g-recaptcha-response').val();
    if (!googleResponse) {
      this.addError("corp.client.contact.incompleteCaptcha", true);
      return false;
    }

    return true;
  }

  this.hideErrorDiv = function() {
    if ($("#errorDiv").length > 0) {
      $("#errorDiv").hide();
    }
  }

  this.addError = function(errorMsg, isTransform) {
    if ($("#errorDiv").length > 0) {
      $("#errorDiv").html(isTransform ? jQuery.i18n.prop(errorMsg) : errorMsg).show();
      $("#errorDiv")[0].scrollIntoView(0);
    }
  }

  this.isEnglish = function(msg) {
    var english = /^[A-Za-z0-9\+\-\\. ]*$/;
    return english.test(msg);
  }

  this.init();
}

/*
 * function i18nJS(form, defLanguage) { this.form = form; //
 * this.countryLangJSON = countryLangJSON; this.defLangugage = defLanguage; var
 * _i18n = this; this.selectLocale = function() { var popupElem =
 * $("#locale_popup"); if ($(popupElem).is(':visible')) { $(popupElem).hide(); }
 * else { $(popupElem).show(); // _i18n.changeLanguage(); } } this.init =
 * function() {
 * 
 * $("#localeSelect").click(_i18n.selectLocale);
 * 
 * this.changeLanguage = function() { var elem =
 * $(this.form).find("select[name='lang_i18n']"); // var countryDiv =
 * $(this.form).find("select[name='country_i18n']"); // var
 * selectedCountryValue; // selectedCountryValue = $(countryDiv).val(); var
 * langs;
 * 
 * for ( var i = 0; i < countryLang.length; i++) { var country = countryLang[i];
 * 
 * if (country.country === selectedCountryValue) { langs = country.lang; break; } }
 * 
 * $(elem).empty(); $.each(langs, function(i, lang) { $(elem).append($('<option>', {
 * value : lang.code, text : lang.name })); }); if ($(elem).find("option[value=" +
 * this.defLangugage + "]").length > 0) { $(elem).val(this.defLangugage); }
 * $(this.form).find("input[type=submit]")[0].scrollIntoView(0); } }
 * 
 * this.init(); }
 */

function newsLetterJS(form, action) {
  this.form = form;
  this.formAction = action;
  var _newsletter = this;

  this.init = function() {
    $(this.form).find($('[name="addSubscription"]')).click(function() {
      var emailBox = $(_newsletter.form).find('[name="email"]');
      var csVal = $(_newsletter.form).find('[name="cs"]').val();
      var emailID = $(emailBox).val();
      if (WEB_UTILS.isValidEmail(emailID)) {
        $.post(_newsletter.formAction, {
          email : emailID,
          cs : csVal
        }, function(response, status) {
          var responseJSON = eval('(' + response + ')')
          if (responseJSON.status == "success") {
            _newsletter.addMessage("corp.client.newsletter.successfulsubscription", true);
          } else if (responseJSON.status == "error") {
            _newsletter.addMessage(responseJSON.message, false);
          }
          $(_newsletter.form).trigger('reset');
        }, "text");
      } else {
        _newsletter.addMessage("corp.client.newsletter.errorinsubscription", true);
      }
    });
  }

  this.addMessage = function(message, isTransform) {
    if ($('#message').length > 0) {
      $("#message").html(isTransform ? jQuery.i18n.prop(message) : message).addClass('message').show();
      $("#message")[0].scrollIntoView(0);
    }
  }

  this.init();
}

function PopupJS(form, action) {
  this.form = form;
  this.formAction = action;
  var _self = this;

  this.init = function() {
    $(this.form).on('submit', function(e) {
      e.preventDefault();
      var validStatus = _self.validatePopupForm(this);
      if (validStatus == true) {
        $(_self.form).attr('action', _self.formAction);
        this.submit();
      }
    });
  }
  this.validatePopupForm = function(formElem) {
    var startDate = $(formElem).find('input[name="start_date"]').val();
    var endDate = $(formElem).find('input[name="end_date"]').val();
    var message = $(formElem).find('textarea[name="message"]').val();

    if (startDate == '' || !this.isValidDate(startDate)) {
      WEB_UTILS.addError("Invalid Start Date");
      return false;
    }

    if (endDate == '' || !this.isValidDate(endDate)) {
      WEB_UTILS.addError("Invalid End Date");
      return false;
    }

    if (message == '') {
      WEB_UTILS.addError("Invalid Message");
      return false;
    }

    return true;
  }

  this.isValidDate = function(date) {
    var validDate = /^[0-9]{2,2}[A-Z]{3,3}[0-9]{2,2}$/;
    return validDate.test(date);
  }

  this.init();
}

function LoginJS(form, action) {
  this.form = form;
  this.formAction = action;
  var _self = this;

  this.init = function() {
    $(this.form).on('submit', function(e) {
      e.preventDefault();
      var validStatus = _self.validateLoginForm(this);
      if (validStatus == true) {
        $(_self.form).attr('action', _self.formAction);
        this.submit();
      }
    });
  }
  this.validateLoginForm = function(formElem) {
    var emailId = $(formElem).find('input[name="email"]').val();
    var password = $(formElem).find('input[name="password"]').val();

    if (emailId == '') {
      this.addError("Invalid Email id");
      return false;
    }

    if (password == '' || !WEB_UTILS.isValidPassword(password)) {
      this.addError("Invalid Password");
      return false;
    }

    return true;
  }

  this.addError = function(errorMsg) {
    alert(errorMsg);
  }

  this.init();
}

function AttendanceTracker(form, action, isShowCalendar, isAdmin, attendanceJSON) {
  this.form = form;
  this.formAction = action;
  this.isShowCalendar = isShowCalendar;
  this.isAdmin = isAdmin;
  this.attendanceJSON = attendanceJSON;
  this.calendarInitialised = false;
  var _self = this;

  this.init = function() {
    if ($("#user_attendance").length != 0) {
      $("#user_attendance").DataTable({
        "order": []
      });
    }

    if ((this.isShowCalendar == true) && (this.isAdmin == false) && ($("#calendarDiv").length != 0)) {
      var eventJSON = this.attendanceJSON[0].attn;
      this.addCalendar(eventJSON);
    }

    var startDateElem = $(this.form).find("input[name='startdate']");

    var endDateElem = $(this.form).find("input[name='enddate']");

    WEB_UTILS.initPreviousDate(startDateElem[0], "dd/mm/yy");
    WEB_UTILS.initDate(endDateElem[0], "dd/mm/yy");
    $(this.form).on('submit', function(e) {
      e.preventDefault();
      var validStatus = _self.validateForm(this);
      if (validStatus == true) {
        $(_self.form).attr('action', _self.formAction);
        this.submit();
      }
    });
  }

  this.addCalendar = function(eventJSON) {
    if (this.calendarInitialised == false) {
      $("#calendarDiv").fullCalendar({
        defaultDate : eventJSON[eventJSON.length - 1].start.substring(0, 10),// extract
        // last
        // date
        // from
        // the
        // json,
        editable : false,
        eventLimit : true, // allow "more" link when too many events
        events : eventJSON,
      });
      this.calendarInitialised = true;
    } else {
      $("#calendarDiv").fullCalendar('removeEvents');
      $("#calendarDiv").fullCalendar('addEventSource', eventJSON);
    }
  }

  this.showFullCalendar = function(userId) {
    $("#calendar_header").html("User ID: <i>" + userId + "</i>");
    $("#calendarContainer").show();
    var eventJSON;
    for (var i = 0; i < this.attendanceJSON.length; i++) {
      var obj = this.attendanceJSON[i];
      if (obj.id == userId) {
        eventJSON = obj.attn;
        break;
      }
    }
    this.addCalendar(eventJSON);
  }

  this.validateForm = function(formElem) {
    var startDateElem = $(formElem).find("input[name='startdate']");
    var startDate = $(startDateElem).val();

    var endDateElem = $(formElem).find("input[name='enddate']");
    var endDate = $(endDateElem).val();

    if (startDate == '' || !this.isValidDate(startDate)) {
      this.addError("Invalid Start Date");
      return false;
    }

    if (endDate == '' || !this.isValidDate(endDate)) {
      this.addError("Invalid End Date");
      return false;
    }

    if (message == '') {
      this.addError("Invalid Message");
      return false;
    }

    return true;
  }

  this.addError = function(errorMsg) {
    alert(errorMsg);
  }

  this.isValidDate = function(date) {
    var validDate = /^\d{2}([\/])\d{2}\1\d{4}$/;
    return validDate.test(date);
  }

  this.init();
}

function ManageUsersJS(form, baseURI, userRoles) {
  this.form = form;
  this.baseUrl = baseURI;
  this.userRoles = userRoles;
  this.isEdit = false;

  this.init = function() {
    $("#existing_users").DataTable();
    $(this.form).find('input[type=radio][name=numusers]').change(function() {
      var radioButtonValue = this.value;
      if (radioButtonValue == 'bulk') {
        $("#singleUploadDiv").hide();
        $("#bulkUploadDiv").show();
      } else if (radioButtonValue == 'single') {
        $("#singleUploadDiv").show();
        $("#bulkUploadDiv").hide();
      }
      $("#addUserDiv").show();
    });
  }

  this.modifyUser = function() {
    var userVal = $(this.form).find('input[name=numusers]:checked').val();
    var url = this.baseUrl;
    if (userVal == 'bulk') {
      var fileName = $(this.form).find("input[name='uploadFile']").val().trim();
      var extension = fileName.replace(/^.*\./, '').toLowerCase();
      if ((fileName == '') || ((extension != "csv") && (extension != "txt"))) {
        WEB_UTILS.addError(fileName == "" ? "Please select a file" : "Only TXT and CSV files supported");
        return false;
      }
      url += "/bulkadd";
      $(this.form).attr("enctype", "multipart/form-data");
    } else if (userVal == 'single') {
      var name = $(this.form).find("input[name='name']").val();
      if (!WEB_UTILS.isValidNonEmptyString(name)) {
        WEB_UTILS.addError("Please enter a valid name.");
        return false;
      }

      var email = $(this.form).find("input[name='email']").val();
      if (!WEB_UTILS.isValidEmail(email)) {
        WEB_UTILS.addError("Please enter a valid email address. For example abc@domain.com");
        return false;
      }

      var userID = $(this.form).find("input[name='user_id']").val();
      var updatePasswordElem = $(this.form).find("input[name='update_password']");
      if (userID == "-1" || $(updatePasswordElem[0]).is(':checked')) {
        var password = $(this.form).find("input[name='password']").val();
        if (!WEB_UTILS.isValidPassword(password)) {
          WEB_UTILS.addError("Please enter a valid password, having atleast 8 characters.");
          return false;
        }
      }
      url += ((this.isEdit == false) ? "/add" : "");
    }

    $(this.form).attr("action", url);
    $(this.form).submit();
  }

  this.editUser = function(trElem) {
    $(this.form)[0].scrollIntoView();
    var td = $(trElem).find('td');

    var id = $(td[0]).html().trim();
    var role = $(td[1]).html().trim();
    var name = $(td[2]).html().trim();
    var email = $(td[4]).html().trim();
    var isEnabled = ("Enabled" == $(td[3]).html().trim());
    var roleID;
    for (var i = 0; i < this.userRoles.userroles.length; i++) {
      var userRole = this.userRoles.userroles[i];
      if (userRole.name == role) {
        roleID = userRole.id;
        break;
      }
    }

    // $(this.form).find("input[name='user_id']").val(id);
    $("#userIDTD").html(id + "<input type='hidden' name='user_id' value='" + id + "' />");

    var userRoleElem = $(this.form).find("select[name='role']");
    WEB_UTILS.setSelectBoxByValue($(userRoleElem), roleID);

    $(this.form).find("input[name='name']").val(name);
    $(this.form).find("input[name='email']").val(email);
    $(this.form).find("input[name='enabled']").prop('checked', (isEnabled));
    $(this.form).find("input[name='update_password']").prop('checked', false);
    $(this.form).find("button[name='modButton']").html("Modify Existing User");
    $("#actionDiv").html("Edit Exisiting User: " + id);
    $("#passwordDiv").show();
    $(userRoleElem).focus();

    this.isEdit = true;
    $(this.form).find('input[type=radio][name=numusers]').val([
      'single'
    ]);
    $("#singleUploadSpan").html("Edit Single User");
    $(this.form).find('input[type=radio][name=numusers][value=single]').change();

  }

  this.gotoAdd = function() {
    $(this.form)[0].scrollIntoView();
  }

  this.init();
}

function UserMappingJS(form, baseURI) {
  this.form = form;
  this.baseURI = baseURI;

  this.uploadMappings = function() {
    var fileElem = $(this.form).find("input[name='uploadMappingFile']");
    var fileName = $(fileElem).val().trim();
    var extension = fileName.replace(/^.*\./, '').toLowerCase();
    if ((fileName == '') || ((extension != "csv") && (extension != "txt"))) {
      WEB_UTILS.addError(fileName == "" ? "Please select a file" : "Only TXT and CSV files supported");
      return false;
    }
    
    
    $(this.form).attr("enctype", "multipart/form-data");
    $(this.form).attr('action', this.baseURI);
    $(this.form).submit();
  }
  
}
