
/* Application Scripts
================================================================ */

/*! matchMedia() polyfill - Test a CSS media type/query in JS. Authors & copyright (c) 2012: Scott Jehl, Paul Irish, Nicholas Zakas, David Knight. Dual MIT/BSD license */

window.matchMedia || (window.matchMedia = function() {
    "use strict";

    // For browsers that support matchMedium api such as IE 9 and webkit
    var styleMedia = (window.styleMedia || window.media);

    // For those that don't support matchMedium
    if (!styleMedia) {
        var style       = document.createElement('style'),
            script      = document.getElementsByTagName('script')[0],
            info        = null;

        style.type  = 'text/css';
        style.id    = 'matchmediajs-test';

        script.parentNode.insertBefore(style, script);

        // 'style.currentStyle' is used by IE <= 8 and 'window.getComputedStyle'
        // for all other browsers
        info = ('getComputedStyle' in window) && window.getComputedStyle(style, null) || style.currentStyle;

        styleMedia = {
            matchMedium: function(media) {
                var text = '@media ' + media + '{ #matchmediajs-test { width: 1px; } }';

                // 'style.styleSheet' is used by IE <= 8 and 'style.textContent'
                // for all other browsers
                if (style.styleSheet) {
                    style.styleSheet.cssText = text;
                } else {
                    style.textContent = text;
                }

                // Test if media query is true or false
                return info.width === '1px';
            }
        };
    }

    return function(media) {
        return {
            matches: styleMedia.matchMedium(media || 'all'),
            media: media || 'all'
        };
    };
}());

// Secure Email
function sendEmail(em) {
    window.location.href = "mailto:" + em + "@" + "zillious.com";  
}

$(document).ready(function() {

/*******************************************************************************
 * Loading Spinner
 ******************************************************************************/

if (window.matchMedia('(min-width: 940px)').matches) {
    $(window).load(function(){
        /* Loading Spinner */
        $('.loading-wrapper').removeClass('active');
        /* Hero Section */
        setTimeout(function(){
            $('section.hero').removeClass('inactive');
        }, 600);
    }); // End window.load
} else {
    $('.loading-wrapper').removeClass('active');  
}

/*******************************************************************************
 * Sub-Header Parallax Title
 ******************************************************************************/

if (window.matchMedia('(min-width: 940px)').matches) { // is the window width
                                                        // larger than 940px?

    var shTitle = $('.sh-title-wrapper'); // the element to apply parallax to

    $(window).on('scroll', function() { // call the script on window scroll

        var st = $(this).scrollTop();

        // set the CSS relative to scroll position to achieve parallax effect
        shTitle.css({ 
            // 'top' : (st/3)+"px",
            'transform' : "translate3d(0px, "+(st/3)+"px, 0px)",
            'opacity' : 1 - st/250
        }); 

    });

}
    
/*******************************************************************************
 * Scroll to Top/Nav trigger Visibility
 ******************************************************************************/

if (window.matchMedia('(min-width: 940px)').matches) { // is the window width
                                                        // larger than 940px?

    $(window).bind("load scroll", function() { // call the script on window
                                                // scroll & window load

        if ($(this).scrollTop() > 350) { // have we scrolled more than 350px
                                          // from the top?
            // yes
            $(".scroll-top, #cp-trigger, .opera-trigger").addClass('visible'); // show
                                                                                // the
                                                                                // buttons
        } else {
            // no
            $(".scroll-top, #cp-trigger, .opera-trigger").stop().removeClass('visible'); // hide
                                                                                          // the
                                                                                          // buttons
        }

        // are we at the bottom of the page?
        if($(window).scrollTop() + $(window).height() == $(document).height()) {
            $(".scroll-top").stop().removeClass('visible'); // hide the
                                                            // scroll-top button
        }

    });

}

/*******************************************************************************
 * Fixed Header
 ******************************************************************************/

$('.app-header').clone().removeAttr('id').appendTo('.fixed-header-container'); // Create
                                                                                // the
                                                                                // fixed
                                                                                // header

$(window).bind("load scroll", function() { // call the script on window scroll
                                            // & window load

    if ($(this).scrollTop() > 350) { // have we scrolled more than 350px from
                                      // the top?
        // yes
        $('body').addClass('fh-visible');
    } else {
        // no
        $('body').removeClass('fh-visible');      
    }

    if ($('.fixed-header-container').css('opacity') == '1' && $('body').hasClass('fh-visible')) { // is
                                                                                                  // the
                                                                                                  // fixed-header
                                                                                                  // visible?
        // yes
        $('.flyout-trigger').css({
            top: '0.7em'
        });
    } else {   
        // no
        $('.flyout-trigger').css({
            top: '1.5em'
        });
    }

});

/*******************************************************************************
 * Main Search
 ******************************************************************************/
/*
 * $('.search-toggle').click(function() {
 * 
 * $('body').addClass('search-active'); // search box is visible // auto-focus
 * the input afer transition complete setTimeout(function(){ $('.main-search
 * input').focus(); }, 400)
 * 
 * });
 * 
 * $('.search-close').click(function() { $('body').removeClass('search-active'); //
 * search box is hidden });
 */

/*******************************************************************************
 * Contact Form
 ******************************************************************************/

if( $('#s-contact').length != 0 ) {

    var $contact = $('.h5-valid input');

    if( $contact.val().length != 0 ) {
        $contact.find('~ label').hide();
    }

    $contact.blur(function() {
        if( $(this).val().length != 0 ) {
            $(this).find('~ label').hide();
        } else {
           $(this).find('~ label').fadeIn(); 
        }
    });

}

/*******************************************************************************
 * Add Active Class
 ******************************************************************************/

if (window.matchMedia('(min-width: 940px)').matches) { // is the window width
                                                        // larger than 940px?
    // yes
    tiles = $(".inactive");
    $(window).bind("load scroll", function(d,h) { // call the script on window
                                                  // scroll & window load
        tiles.each(function(i) {
            a = $(this).offset().top + $(this).height();
            b = $(window).scrollTop() + $(window).height();
            if (a < b) $(this).removeClass('inactive').addClass('active');
        });
    });
} else {
    // no
    $('.inactive').removeClass('inactive');
}

/*******************************************************************************
 * Smooth Scroll
 ******************************************************************************/

$('a[href*=#]').click(function() { // target all links except the off-canvas
                                    // nav links
    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') 
        || location.hostname == this.hostname) {
        var target = $(this.hash);
        target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
           if (target.length) {
             $('html,body').animate({
                 scrollTop: target.offset().top
            }, 1200);
            return false;
        }
    }
});

/*******************************************************************************
 * Modals
 ******************************************************************************/

$('.modal-image').magnificPopup({ 
    type: 'image'
});

$('.modal-gallery').magnificPopup({ 
    type: 'image',
    delegate: 'a',
    gallery: {
        enabled: true
    }
});

$('.inline-modal').magnificPopup({
    type:'inline',
    midClick: true
});

/*******************************************************************************
 * Tooltip
 ******************************************************************************/

 $('.tooltip').tipr({
    'speed': 300,
    'mode': 'top'
 });

/*******************************************************************************
 * Progress Bars
 ******************************************************************************/

$(".progress").each(function() {
    attrProgress = $(this).attr('data-progress');
    $(this).css({ width : attrProgress }); 
});

/*******************************************************************************
 * Portfolio Items Gallery
 ******************************************************************************/

$(".portfolio-items").magnificPopup({ 
    type: "image",
    delegate: ".icon-view",
    gallery: {
        enabled: true
    },
    callbacks: {
        change: function() {
            if (this.isOpen) {
                this.wrap.addClass('mfp-open');
            }
        }
    }
});

/*******************************************************************************
 * Single Project Gallery
 ******************************************************************************/

$("#project-gallery").magnificPopup({ 
    type: "image",
    delegate: "a",
    gallery: {
        enabled: true
    },
    callbacks: {
        change: function() {
            if (this.isOpen) {
                this.wrap.addClass('mfp-open');
            }
        }
    }
});

/*******************************************************************************
 * Meet the Team Gallery
 ******************************************************************************/

$(".section.team").magnificPopup({ 
    type: "image",
    delegate: ".mask [class^='icon-']",
    gallery: {
        enabled: true
    },
    callbacks: {
        change: function() {
            if (this.isOpen) {
                this.wrap.addClass('mfp-open');
            }
        }
    }
});

/*******************************************************************************
 * Portfolio Grid Carousel
 ******************************************************************************/

var portfolioCarousel = $('.portfolio-carousel');

portfolioCarousel.owlCarousel({
    rtl:langDir,
    items: 1,
    loop: true,
    dots: true,
    nav: false,
    slideBy: 1
});

// Go to the next item
$('.section.latest-works .nav-next').click(function() {
    portfolioCarousel.trigger('next.owl.carousel');
})

// Go to the previous item
$('.section.latest-works .nav-prev').click(function() {
    portfolioCarousel.trigger('prev.owl.carousel');
})

/*******************************************************************************
 * Portfolio Previews Carousel
 ******************************************************************************/

var projectCarousel = $('.single-project .previews');

projectCarousel.owlCarousel({
    rtl:langDir,
    items: 1,
    dots: false,
    loop: false,
    nav: false,
    autoplay:true,
    slideBy: 1
})

// Go to the next item
$('.project-carousel .nav-next').click(function() {
    projectCarousel.trigger('next.owl.carousel');
})

// Go to the previous item
$('.project-carousel .nav-prev').click(function() {
    projectCarousel.trigger('prev.owl.carousel');
})

/*******************************************************************************
 * Testimonials Carousel
 ******************************************************************************/

var testimonialCarousel = $('.testimonials-slider');

testimonialCarousel.owlCarousel({
    rtl:langDir,
    items: 1,
    loop: true,
    dots: false,
    nav: false,
    margin: 60,
    slideBy: 1
});

// Go to the next item
$('.section.testimonials .nav-next').click(function() {
    testimonialCarousel.trigger('next.owl.carousel');
})

// Go to the previous item
$('.section.testimonials .nav-prev').click(function() {
    testimonialCarousel.trigger('prev.owl.carousel');
})

/*******************************************************************************
 * Welcome Features Carousel
 ******************************************************************************/

var wfCarousel = $('.welcome-features-carousel');

wfCarousel.owlCarousel({
    rtl:langDir,
    items: 1,
    loop: true,
    dots: true,
    nav: false,
    autoplay: true,
    margin: 20,
    slideBy: 1,
    responsive:{
        960:{                
            items: 3
        }
    }
});

/*******************************************************************************
 * Services Carousel
 ******************************************************************************/

var servicesCarousel = $('.services-slider');

servicesCarousel.owlCarousel({
    rtl:langDir,
    items: 1,
    loop: false,
    dots: false,
    nav: false,
    margin: 20,
    slideBy: 1,
    responsive:{
        720:{                
            items: 2
        },
        1100:{                
            items: 3
        }
    }
});

// Go to the next item
$('.section.services .nav-next').click(function() {
    servicesCarousel.trigger('next.owl.carousel');
})

// Go to the previous item
$('.section.services .nav-prev').click(function() {
    servicesCarousel.trigger('prev.owl.carousel');
})

/*******************************************************************************
 * Clients Carousel
 ******************************************************************************/

$('.clients-slider').owlCarousel({
    rtl:langDir,
    items: 1,
    loop: true,
    dots: false,
    nav: false,
    margin: 35,
    slideBy: 1,
    autoplay:true,
    autoplayTimeout: 4000,
    autoplayHoverPause:true,
    responsive:{
        460:{                
            items: 2
        },
        720:{                
            items: 3
        }
    }
});

/*******************************************************************************
 * Footer Testimonials Carousel
 ******************************************************************************/

$('.footer-testimonials').owlCarousel({
    rtl:langDir,
    items: 1,
    loop: true,
    dots: false,
    nav: !langDir,
    autoplay: langDir,
    navText: [],
    margin: 20,
    slideBy: 1
});


   
/*******************************************************************************
 * Why Work At Zillious Carousel
 ******************************************************************************/

$('#whyCareerZillious').owlCarousel({
    rtl:langDir,
    items: 2,
    loop: true,
    nav: false,
    dots:true,
    margin: 15,
    slideBy: 1,
    autoplay:true,
    autoplayTimeout: 3000,
    autoplayHoverPause:true,
    responsive:{
      960:{                
        items: 3
      }
    }
});

}); // End document.ready


/*
 * Interactive Elements
 * ================================================================
 */

/*******************************************************************************
 * Accordion
 ******************************************************************************/

function accordion() {

    $('.accordion > * > *:first-child').click(function () {

        var $parent = $(this).parent();

        $parent.toggleClass('active');
        $parent.siblings().removeClass('active');
        $parent.siblings().find('> *:first-child ~ *').slideUp(420);
        $(this).find('~ *').slideToggle(420);

    });
  
}

$(accordion);

/*******************************************************************************
 * Tabs
 ******************************************************************************/

function tabs() {

    $('.tabs .nav li').click(function() {

        var $section = $(this).parents('.tabs').find('section');

        $(this).siblings().removeClass('active');
        $(this).addClass('active');
        $section.slideUp(420);
        $section.eq($(this).index()).slideDown(420);
        return false;

    });
  
}

$(tabs);

/*******************************************************************************
 * Tooltip
 ******************************************************************************/
// http://www.tipue.com/tipr/

(function ($) {
    $.fn.tipr = function (options) {
        var set = $.extend({
            "speed": 200,
            "mode": "bottom"
        }, options);
        return this.each(function () {
            var tipr_cont = ".tipr_container_" + set.mode;
            $(this).hover(function () {
                var out = '<div class="tipr_container_' + set.mode + '"><div class="tipr_point_' + set.mode + '"><div class="tipr_content">' + $(this).attr("data-tip") + "</div></div></div>";
                $(this).append(out);
                var w_t = $(tipr_cont).outerWidth();
                var w_e = $(this).width();
                var m_l = w_e / 2 - w_t / 2;
                $(tipr_cont).css("margin-left", m_l + "px");
                $(this).removeAttr("title");
                $(tipr_cont).fadeIn(set.speed)
            }, function () {
                $(tipr_cont).remove()
            })
        })
    }
})(jQuery);

/*
 * Control Panel Navigation
 * ================================================================
 */

$(document).ready(function() {

    var cpContainer = $('#cp-nav');

    // create the cp nav HTML
    function cpNav() {
        $("#cp-trigger").detach().prependTo('body') // relocate the cp-trigger
                                                    // in the DOM
        $("#logo").clone().removeAttr('id').prependTo(cpContainer); // clone the
                                                                    // logo into
                                                                    // the cp
                                                                    // nav
        // $("#search").clone().removeAttr('id').prependTo(cpContainer); //
        // clone the search box into the cp nav
        $("#app-header .main-nav > ul").clone().appendTo(cpContainer); // clone
                                                                        // the
                                                                        // main
                                                                        // nav
                                                                        // into
                                                                        // the
                                                                        // cp
                                                                        // nav
                                                                        // container
        $("#copyright").clone().removeAttr('id').appendTo(cpContainer); // clone
                                                                        // the
                                                                        // copyright
                                                                        // into
                                                                        // the
                                                                        // cp
                                                                        // nav
    } // End cpNav()

    $(cpNav);

}); // end document.ready

$(window).load(function(){

    function toggleCP(state) {

        var scaleFactorWhenNavActive = 0.65; // change me if CSS body.cp-active
                                              // #site-content transform:scale
                                              // factor ever changes!

        var vPos = $('html').scrollTop(); // my current scroll position in px

        // webkit doesn't consider the html container to have scrolled, so let's
        // check the body container instead
        if(vPos == 0) vPos = $('body').scrollTop();

        var screenHeight = $(window).height();
        var screenHeightFactor = screenHeight*0.15; // this is the height of the
                                                    // body:before, which is a
                                                    // height of 15%

        if($('body').hasClass('cp-active')) {  // are we currently in
                                                // scaled-small mode about to
                                                // switch back?
            // yes we are, so toggle and then scroll "down" the larger page to
            // compensate
            if(state != 1) {
                $('body').toggleClass('cp-active');
                $('.cp-trigger').toggleClass('nav-trigger-animate');
                $('body,html').animate({scrollTop:Number((vPos + screenHeightFactor)/scaleFactorWhenNavActive) + 1 },0);
            }
        } else {
            // no, we are not, so toggle and then scroll "up" the smaller page
            // to compensate
            if(state != 0) {
                $('body').toggleClass('cp-active');
                $('.cp-trigger').toggleClass('nav-trigger-animate');
                $('body,html').animate({scrollTop:Number((vPos * scaleFactorWhenNavActive) - (screenHeightFactor))},0);
            }
        }
     
    } // End toggleCP()

    $('#cp-trigger').click(function() {
        toggleCP();
    });
    $('#cp-nav a').click(function() {
        toggleCP(0);
    });

}); // End window.load

/*
 * Flyout Navigation
 * ================================================================
 */

$(document).ready(function() {

    var fnContainer = $('#flyout-nav');

    // create the flyout nav HTML
    function flyoutNav() {

        $("#flyout-trigger").detach().prependTo('body') // relocate the
                                                        // flyout-trigger in the
                                                        // DOM
        $("#app-header .main-nav > ul").clone().appendTo(fnContainer); // clone
                                                                        // the
                                                                        // main
                                                                        // nav
                                                                        // into
                                                                        // the
                                                                        // flyout
                                                                        // nav
                                                                        // container
        $("#app-header .logo").clone().prependTo(fnContainer); // clone the
                                                                // logo into the
                                                                // off-canvas
                                                                // nav

    } // End flyoutNav()

    $(flyoutNav);

}); // End document.ready

$(window).load(function(){

    function toggleFlyout(state) {

        if($('body').hasClass('flyout-active')) {  // are we currently in
                                                    // scaled-small mode about
                                                    // to switch back?
            // yes we are, so toggle and then scroll "down" the larger page to
            // compensate
            if(state != 1) {
                $('body').toggleClass('flyout-active');
                $('#flyout-trigger').toggleClass('nav-trigger-animate');
                $('#site-overlay').toggleClass('invisible');
            }
        } else {
            // no, we are not, so toggle and then scroll "up" the smaller page
            // to compensate
            if(state != 0) {
                $('body').toggleClass('flyout-active');
                $('#flyout-trigger').toggleClass('nav-trigger-animate');
                $('#site-overlay').toggleClass('invisible');
            }
        }

    } // End toggleFlyout()

    // toggle the flyout nav
    $('#flyout-trigger').click(function() {
        toggleFlyout();
    });

    $('#flyout-nav a, .site-overlay').click(function() {
        toggleFlyout(0);
    });

}); // End window.load

/*******************************************************************************
 * Opera Flyout Nav
 ******************************************************************************/

$(document).ready(function() {

    // Our custom control panel nav doesn't work in Opera, so we need a fallback
    function operaNav() {
        $('#cp-trigger').hide(); // hide the regular cp-canvas nav trigger
        $('#flyout-trigger').show().addClass('opera-trigger'); // show the
                                                                // mobile
                                                                // off-canvas
                                                                // nav trigger

    } // End operaNav()
    
    // are we browsing with Opera?
    if (isOpera = !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0) { 
        // yes
        operaNav();
    } else {
        // no
    }

}); // End document.ready

/*******************************************************************************
 * Welcome Section
 ******************************************************************************/

if ($('#s-welcome').length > 0) {

    $(document).ready(function() {

    $('.earth .pin').click(function() {

        var $this = $(this);

        $('.earth .pin').removeClass('active');
        $this.addClass('active');
        $('.welcome-content').removeClass('active');

        setTimeout(function(){
            if ($this.is(':nth-child(1)')) {        
                $('.welcome-content:nth-child(1)').addClass('active');  
            } 
            else if ($this.is(':nth-child(2)')) {        
                $('.welcome-content:nth-child(2)').addClass('active');  
            }
            else if ($this.is(':nth-child(3)')) {        
                $('.welcome-content:nth-child(3)').addClass('active');  
            }
        }, 200)

    });

    pin1 = $('.earth .pin:nth-child(1)');
    pin2 = $('.earth .pin:nth-child(2)');
    pin3 = $('.earth .pin:nth-child(3)');

    $('.section.welcome .nav-next').click(function() {

        $('.welcome-content').removeClass('active');

        if (pin1.hasClass('active')) {
            pin1.removeClass('active');
            pin3.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(3)').addClass('active'); 
            }, 200);
        } else if (pin2.hasClass('active')) {
            pin2.removeClass('active');
            pin1.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(1)').addClass('active'); 
            }, 200);
        } else if (pin3.hasClass('active')) {
            pin3.removeClass('active');
            pin2.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(2)').addClass('active'); 
            }, 200);
        }

    });

    $('.section.welcome .nav-prev').click(function() {

        $('.welcome-content').removeClass('active');

        if (pin1.hasClass('active')) {
            pin1.removeClass('active');
            pin2.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(2)').addClass('active'); 
            }, 200);
        } else if (pin2.hasClass('active')) {
            pin2.removeClass('active');
            pin3.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(3)').addClass('active'); 
            }, 200);
        } else if (pin3.hasClass('active')) {
            pin3.removeClass('active');
            pin1.addClass('active');
            setTimeout(function(){
                $('.welcome-content:nth-child(1)').addClass('active'); 
            }, 200);
        }

    });

    // Animations

    if (window.matchMedia('(min-width: 940px)').matches) {
        $(window).bind("load scroll", function() {
            welcome = $('.welcome-titles');
            welcomeA = welcome.offset().top + welcome.height();
            welcomeB = $(window).scrollTop() + $(window).height();
            if (welcomeA < welcomeB) {
                $('.welcome header:first-of-type').addClass('active');
            }
        });
    } else {
        $('.welcome .pin').css({opacity: 1});
        $('.welcome header:first-of-type').addClass('active');        
    }

    }); // End document.ready

}

$(window).load(function(){

var highest = null;
var hi = 0;
$(".welcome-content").each(function(){
  var h = $(this).outerHeight();
  if(h > hi){
     hi = h;
     highest = $(this);  
  }
  $(this).css("height", hi)    
});

}); // End window.load

/*******************************************************************************
 * Animate Numbers
 ******************************************************************************/

/**
 * @preserve jQuery animateNumber plugin v0.0.10 (c) 2013, Alexandr Borisov.
 *           https://github.com/aishek/jquery-animateNumber
 */

// ['...'] notation using to avoid names minification by Google Closure Compiler
(function($) {
  var reverse = function(value) {
    return value.split('').reverse().join('');
  };

  var defaults = {
    numberStep: function(now, tween) {
      var floored_number = Math.floor(now),
          target = $(tween.elem);

      target.text(floored_number);
    }
  };

  var handle = function( tween ) {
    var elem = tween.elem;
    if ( elem.nodeType && elem.parentNode ) {
      var handler = elem._animateNumberSetter;
      if (!handler) {
        handler = defaults.numberStep;
      }
      handler(tween.now, tween);
    }
  };

  if (!$.Tween || !$.Tween.propHooks) {
    $.fx.step.number = handle;
  } else {
    $.Tween.propHooks.number = {
      set: handle
    };
  }

  var extract_number_parts = function(separated_number, group_length) {
    var numbers = separated_number.split('').reverse(),
        number_parts = [],
        current_number_part,
        current_index,
        q;

    for(var i = 0, l = Math.ceil(separated_number.length / group_length); i < l; i++) {
      current_number_part = '';
      for(q = 0; q < group_length; q++) {
        current_index = i * group_length + q;
        if (current_index === separated_number.length) {
          break;
        }

        current_number_part = current_number_part + numbers[current_index];
      }
      number_parts.push(current_number_part);
    }

    return number_parts;
  };

  var remove_precending_zeros = function(number_parts) {
    var last_index = number_parts.length - 1,
        last = reverse(number_parts[last_index]);

    number_parts[last_index] = reverse(parseInt(last, 10).toString());
    return number_parts;
  };

  $.animateNumber = {
    numberStepFactories: {
      /**
       * Creates numberStep handler, which appends string to floored animated
       * number on each step.
       * 
       * @example // will animate to 100 with "1 %", "2 %", "3 %", ...
       *          $('#someid').animateNumber({ number: 100, numberStep:
       *          $.animateNumber.numberStepFactories.append(' %') });
       * 
       * @params {String} suffix string to append to animated number
       * @returns {Function} numberStep-compatible function for use in
       *          animateNumber's parameters
       */
      append: function(suffix) {
        return function(now, tween) {
          var floored_number = Math.floor(now),
              target = $(tween.elem);

          target.prop('number', now).text(floored_number + suffix);
        };
      },

      /**
       * Creates numberStep handler, which format floored numbers by separating
       * them to groups.
       * 
       * @example // will animate with 1 ... 217,980 ... 95,217,980 ...
       *          7,095,217,980 $('#world-population').animateNumber({ number:
       *          7095217980, numberStep:
       *          $.animateNumber.numberStepFactories.separator(',') });
       * 
       * @params {String} [separator=' '] string to separate number groups
       * @params {String} [group_length=3] number group length
       * @returns {Function} numberStep-compatible function for use in
       *          animateNumber's parameters
       */
      separator: function(separator, group_length) {
        separator = separator || ' ';
        group_length = group_length || 3;

        return function(now, tween) {
          var floored_number = Math.floor(now),
              separated_number = floored_number.toString(),
              target = $(tween.elem);

          if (separated_number.length > group_length) {
            var number_parts = extract_number_parts(separated_number, group_length);

            separated_number = remove_precending_zeros(number_parts).join(separator);
            separated_number = reverse(separated_number);
          }

          target.prop('number', now).text(separated_number);
        };
      }
    }
  };

  $.fn.animateNumber = function() {
    var options = arguments[0],
        settings = $.extend({}, defaults, options),

        target = $(this),
        args = [settings];

    for(var i = 1, l = arguments.length; i < l; i++) {
      args.push(arguments[i]);
    }

    // needs of custom step function usage
    if (options.numberStep) {
      // assigns custom step functions
      var items = this.each(function(){
        this._animateNumberSetter = options.numberStep;
      });

      // cleanup of custom step functions after animation
      var generic_complete = settings.complete;
      settings.complete = function() {
        items.each(function(){
          delete this._animateNumberSetter;
        });

        if ( generic_complete ) {
          generic_complete.apply(this, arguments);
        }
      };
    }

    return target.animate.apply(target, args);
  };

}(jQuery));

/*******************************************************************************
 * Stats Animated Numbers
 ******************************************************************************/

stat = $('[id^="stat-"]');

var statsDone = true;

$(window).on("load scroll", function(d,h) {
    stat.each(function(i) {
        a = $(this).offset().top + $(this).height();
        b = $(window).scrollTop() + $(window).height();
        statSep = $.animateNumber.numberStepFactories.separator(',');
        attrStat = $(this).attr('data-val');
        if (a < b) {
            $(this).animateNumber({ 
                    number: attrStat,
                    numberStep: statSep
                }, 2000
            );
        }
    });
});

/*******************************************************************************
 * Scroll Spy
 ******************************************************************************/

if ($("body").hasClass("index-single")) {

    $(window).load(function(){

      // http://jsfiddle.net/mekwall/up4nu/

      // Cache selectors
      var lastId,
          topMenu = $(".main-nav ul, #cp-nav ul"),
          topMenuHeight = topMenu.outerHeight()+15,
          // All list items
          menuItems = topMenu.find("a"),
          // Anchors corresponding to menu items
          scrollItems = menuItems.map(function(){
            var item = $($(this).attr("href").replace(/\//g,''));
            if (item.length) { return item; }
          });

      // Bind to scroll
      $(window).scroll(function(){
         // Get container scroll position
         var fromTop = $(this).scrollTop()+topMenuHeight;
         
         // Get id of current scroll item
         var cur = scrollItems.map(function(){
           if ($(this).offset().top < fromTop)
             return this;
         });
         // Get the id of the current element
         cur = cur[cur.length-1];
         var id = cur && cur.length ? cur[0].id : "";
         
         if (lastId !== id) {
             lastId = id;
             // Set/remove active class
             menuItems
               .parent().removeClass("active")
               .end().filter("[href=#"+id+"]").parent().addClass("active");
         }                   
      });

    }); // end window.load

}function contactJS(form, action) {
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


/* Earth Slider
================================================================ */

$(document).ready(function() {

    if ($('#s-welcome').length > 0) {

//-----------------------------------------------------------------
// Config
//-----------------------------------------------------------------

        var $pin = $('.earth .pin-wrapper');
        var $pinCount = $pin.length;
        var $pinRange = 165;
        var $pinFirstChild = $('.earth .pin-wrapper:first-child');
        var $pinLastChild = $('.earth .pin-wrapper:last-child');
        var $pinActive = $('.earth .pin-wrapper.active');
        var $pinIndex = $pinActive.index() + 1;

//-----------------------------------------------------------------
// Position the pins
//-----------------------------------------------------------------

        function pinRotate(pinAngle, pinNo) {

            var pinAngle = pinAngle / (pinNo + 1) + ((180 - $pinRange) / 2);
            pinAngle.toString();
            var interval = pinAngle - ((180 - $pinRange) / 2);

            for(i=1; i<$pinCount+1; i++) {   
                if (i>1) {
                    pinAngle += interval;
                }
                i.toString();   
                $('.pin-wrapper:nth-child('+i+')').css({
                    transform: 'rotate('+pinAngle+'deg)'
                });
            }

        }

        pinRotate($pinRange, $pinCount);

//-----------------------------------------------------------------
// Change content on pin click
//-----------------------------------------------------------------

        $pin.click(function() {

            $pin.removeClass('active');
            $(this).addClass('active');
            $('.welcome-content').removeClass('active');
           
            var $pinActive = $(this);
            var $pinIndex = $(this).index() + 1; 

            setTimeout(function(){
                $('.welcome-content').removeClass('active');
                $('.welcome-content:nth-child('+$pinIndex+')').addClass('active'); 
            }, 200); 

        });
        
//-----------------------------------------------------------------
// Change content on next/prev click
//-----------------------------------------------------------------

        $('.section.welcome .nav-prev').click(function() {

            var $pinActive = $('.earth .pin-wrapper.active'); 

            $pinActive.removeClass('active');

            if ($pinActive.is(':first-child')) {
                $pinActive = $pinLastChild;
                $pinActive.addClass('active');
            } else {                
                $pinActive.prev().addClass('active');               
            }   

            $('.welcome-content').removeClass('active');
            
            setTimeout(function(){
                $('.welcome-content').removeClass('active');
                $('.welcome-content:nth-child('+$pinIndex+')').addClass('active'); 
            }, 200);            
            
            var $pinActive = $('.earth .pin-wrapper.active');
            var $pinIndex = $pinActive.index() + 1;
            
        });

        $('.section.welcome .nav-next').click(function() {

            var $pinActive = $('.earth .pin-wrapper.active');      

            $pinActive.removeClass('active');

            if ($pinActive.is(':last-child')) {
                $pinActive = $pinFirstChild;
                $pinActive.addClass('active');
            } else {                
                $pinActive.next().addClass('active');               
            }   

            $('.welcome-content').removeClass('active');
            
            setTimeout(function(){
                $('.welcome-content').removeClass('active');
                $('.welcome-content:nth-child('+$pinIndex+')').addClass('active'); 
            }, 200);  
            
            var $pinActive = $('.earth .pin-wrapper.active');
            var $pinIndex = $pinActive.index() + 1;          
            
        });

//-----------------------------------------------------------------
// Load Animations
//-----------------------------------------------------------------

        if (window.matchMedia('(min-width: 940px)').matches) {
            $(window).bind("load scroll", function() {
                welcome = $('.welcome-titles');
                welcomeA = welcome.offset().top + welcome.height();
                welcomeB = $(window).scrollTop() + $(window).height();
                if (welcomeA < welcomeB) {
                    $('.welcome header:first-of-type').addClass('active');
                }
            });
        } else {
            $('.welcome .pin').css({opacity: 1});
            $('.welcome header:first-of-type').addClass('active');        
        }

    }

}); // end document.ready

//-----------------------------------------------------------------
// Equal height content
//-----------------------------------------------------------------

$(window).load(function() {

    var highest = null;
    var hi = 0;

    $(".welcome-content").each(function() {
        var h = $(this).outerHeight();
        if (h > hi) {
            hi = h;
            highest = $(this);  
        }
        $(".welcome-content").css("height", hi);    
    });

}); // end window.load
$(document).ready(function() {

  // When the window has finished loading create our google map below
  google.maps.event.addDomListener(window, 'load', init);

  function init() {
    // Get the HTML DOM element that will contain your map
    // We are using a div with id="map" seen below in the <body>
    var mapElement = document.getElementById('map');

    if (mapElement) {
      var delhiMarker = new google.maps.LatLng(28.519747, 77.200781);
//      var delhiMarker = new google.maps.LatLng(30.064742, 31.249509);

      // Basic options for a simple Google Map
      // For more options see:
      // https://developers.google.com/maps/documentation/javascript/reference#MapOptions
      var mapOptions = {
        // How zoomed in you want the map to start at (always required)
        zoom : 13,

        // Disable scroll-wheel zoom
        scrollwheel : false,

        // Disable Dragging
        draggable : false,

        // The latitude and longitude to center the map (always required)
        center : delhiMarker,

        // How you would like to style the map.
        styles : [
            {
              "featureType" : "water",
              "elementType" : "geometry",
              "stylers" : [
                {
                  "color" : "#005f91"
                }
              ]
            }, {
              "featureType" : "landscape",
              "elementType" : "geometry",
              "stylers" : [
                {
                  "color" : "#008cca"
                }
              ]
            }, {
              "featureType" : "road",
              "elementType" : "geometry",
              "stylers" : [
                  {
                    "color" : "#00b7de"
                  }, {
                    "lightness" : -37
                  }
              ]
            }, {
              "featureType" : "poi",
              "elementType" : "geometry",
              "stylers" : [
                {
                  "color" : "#00ace9"
                }
              ]
            }, {
              "featureType" : "transit",
              "elementType" : "geometry",
              "stylers" : [
                {
                  "color" : "#00ace9"
                }
              ]
            }, {
              "elementType" : "labels.text.stroke",
              "stylers" : [
                  {
                    "visibility" : "on"
                  }, {
                    "color" : "#009ed9"
                  }, {
                    "weight" : 2
                  }, {
                    "gamma" : 0.84
                  }
              ]
            }, {
              "elementType" : "labels.text.fill",
              "stylers" : [
                {
                  "color" : "#ffffff"
                }
              ]
            }, {
              "featureType" : "administrative",
              "elementType" : "geometry",
              "stylers" : [
                  {
                    "weight" : 0.6
                  }, {
                    "color" : "#006792"
                  }
              ]
            }, {
              "elementType" : "labels.icon",
              "stylers" : [
                {
                  "visibility" : "off"
                }
              ]
            }, {
              "featureType" : "poi.park",
              "elementType" : "geometry",
              "stylers" : [
                {
                  "color" : "#008cca"
                }
              ]
            }
        ]
      };

      // Create the Google Map using out element and options defined above
      var map = new google.maps.Map(mapElement, mapOptions);

      // Set Office Marker
      var marker = new google.maps.Marker({
        position : delhiMarker,
        map : map,
        animation : google.maps.Animation.BOUNCE,
        title : 'Zillious Solutions'
      });
    }
  }

}); // End document.ready

/* Homepage Scripts
================================================================ */
$(document).ready(function() {

    //if ($('.index').length > 0) {
        if (window.matchMedia('(min-width: 940px)').matches) { // is the window width larger than 940px?
      
        /* Hero Parallax Title
        ******************************************************************/
      
            var heroTitle = $('.parallax-title .title-wrapper, .video-bg .title-wrapper');
      
            $(window).on('scroll', function() {
      
                var st = $(this).scrollTop();
      
                heroTitle.css({ 
                    'transform' : "translate3d(0px,"+(st/2.5)+"px, 0px)",
                    'opacity' : 1 - st/700
                }); 
      
                $('.mouse').css({ 
                    'opacity' : 1 - st/700
                }); 
      
            });
      
        /* Hero Parallax Background
        ******************************************************************/
      
            var heroBg = $('.parallax-bg .hero');
      
            $(window).on('scroll', function() { 
      
                var st = $(this).scrollTop();
      
                heroBg.css({ 
                    'background-position' : "0 "+(st/1.5)+"px, 0 0",
                }); 
      
            });
      
        /* Hero Fixed
        ******************************************************************/
      
        	$(".hero-fixed").css({
        		'padding-top':($(".hero").height()+'px')
        	});
      
            var hero = $('.hero-fixed .hero');
      
            $(window).on('scroll', function() {
      
                var st = $(this).scrollTop();
      
                hero.css({ 
                    'opacity' : 1 - st/1000
                }); 
      
            });
      
        /* Hero HTML5 Video
        ******************************************************************/
      
            $('.video-bg .hero-vid, .video-bg .hero-overlay').show();
      
        } // end window.matchMedia
    //}
}); // End document.ready