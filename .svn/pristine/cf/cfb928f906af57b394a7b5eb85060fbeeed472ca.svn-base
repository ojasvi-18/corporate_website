$(document).ready(function() {

  $(".submenu > a").click(function(e) {
    e.preventDefault();
    var $li = $(this).parent("li");
    var $ul = $(this).next("ul");

    if ($li.hasClass("open")) {
      $ul.slideUp(350);
      $li.removeClass("open");
    } else {
      $(".nav > li > ul").slideUp(350);
      $(".nav > li").removeClass("open");
      $ul.slideDown(350);
      $li.addClass("open");
    }
  });

  // event handling on click event for jquery
  $(".profile-button").click(function() {
    $(".profile-button").removeClass("active");
    $(".sidebar .menu li").removeClass("active");
    $(this).addClass("active");
  });
  $(".sidebar .menu li").click(function() {
    $(".profile-button").removeClass("active");
    $(".sidebar .menu li").removeClass("active");
    $(this).addClass("active");
  });

  var indexOfAViewTag = document.URL.indexOf("#!/");
  if (indexOfAViewTag == -1) {
    var leavePolicySidePanelItemAnchor = $('a[href="#!leavepolicypage"]');
    if (leavePolicySidePanelItemAnchor.length > 0) {
      $(leavePolicySidePanelItemAnchor)[0].click();
      var parentLI = $(leavePolicySidePanelItemAnchor).parent();
      $(parentLI)[0].click();
    }
  } else {
    if (document.URL.indexOf("#!/leavepolicypage") != -1) {
      var leavePolicySidePanelItemAnchor = $('a[href="#!leavepolicypage"]');
      if (leavePolicySidePanelItemAnchor.length > 0) {
        var parentLI = $(leavePolicySidePanelItemAnchor).parent();
        $(parentLI)[0].click();
      }
    }
  }

});
