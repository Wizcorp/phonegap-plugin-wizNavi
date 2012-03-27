/* WizNavi for PhoneGap - Native nav bar communicator!
*
 * @author WizCorp Inc. [ Incorporated Wizards ] 
 * @copyright 2011
 * @file - navi.js
 * @about - JavaScript PhoneGap bridge for Native tools bars 
 *
 *
*/


var wizNavi = { 


	create: function(a,s,f) {
	
		return PhoneGap.exec(s, f, "WizNaviPlugin", "create", [a]);

	},
	
	onTapTab: function (s, f) {
        return PhoneGap.exec(s, f, "WizNaviPlugin", "onTapTab", []);
    
    },

	show: function(s,f) {
	
		return PhoneGap.exec(s, f, "WizNaviPlugin", "show", [{animate: true}]);
		 
	},
	
	
	hide: function(s,f) {
	
        return PhoneGap.exec(s, f, "WizNaviPlugin", "hide", [{animate: true}]);

	},
    
    
    notify: function(tab, value, s, f) {
        
        return PhoneGap.exec(s, f, "WizNaviPlugin", "notify", [tab, value]);
        
    },
    
    setTab: function(a, b, s, f) {
    	console.log("calling tab >> "+a+"with data >> "+b);
        return PhoneGap.exec(s, f, "WizNaviPlugin", "setTab", [a, b]);
        
    },
    
    enable: function(s, f) {
        if(typeof(s) == "undefined") { 
			return PhoneGap.exec(null, null, "WizNaviPlugin", "enable", []);
		} else {
			return PhoneGap.exec(s, f, "WizNaviPlugin", "enable", []);
		}
        
    },
        
    disable: function(s, f) {
        if(typeof(s) == "undefined") { 
			return PhoneGap.exec(null, null, "WizNaviPlugin", "disable", []);
		} else {
			return PhoneGap.exec(s, f, "WizNaviPlugin", "disable", []);
		}
        
    }
	
};


	
	
var naviSettings = {
	
	height				: 70 ,
    statBarHeight		: 62 ,
	tabs:
	[	{
     
     badge 		:  0 ,
     name       : "fff" ,
     text 		: "My Page" ,
     image 		: "no_image"
     } ,
     {
     
     badge 		: 0 ,
     name       : "ggg" ,
     text 		: "News" ,
     sound 		: "" ,
     image 		: "no_image" ,
     pressed	: "btn2_over" ,
     focus		: "btn2_over" 
     } ,
     {
     
     badge 		: 0 ,
     text 		: "Gacha" ,
     name       : "Gacha" ,
     sound 		: "" ,
     image 		: "no_image" ,
     pressed	: "btn3_over" ,
     focus		: "btn3_over" 
     } ,
     {
     
     badge 		: 0 ,
     text 		: "Fusion" ,
     name       : "Fusion" ,
     sound 		: "" ,
     image 		: "no_image" ,
     pressed	: "btn4_over" ,
     focus		: "btn4_over" 
     } ,
     {
     badge 		: 0 ,
     text 		: "Menu" ,
     name       : "Menu" ,
     sound 		: "" ,
     image 		: "no_image" ,
     pressed	: "btn5_over" ,
     focus		: "btn5_over" 
     }
     ]
    
};

var changeTabSettings = {

	text		:  "Hello" ,
	image		:  "noImage" 

};


/*-------------------
*
* TEST functions
*
--------------------*/ 


function NaviChange() {
	wizNavi.setTab( 
					3,
					changeTabSettings,
					function(){console.log("[PHONEGAP //////////////] wizNavi setTab - success")}, 
					function(){console.log("[PHONEGAP //////////////] wizNavi setTab - fail")}
                );
};

function NaviNotify2() {
	wizNavi.notify( 
                   1,
                   0,
                   function(){console.log("[PHONEGAP //////////////] wizNavi notify - success")}, 
                   function(){console.log("[PHONEGAP //////////////] wizNavi notify - fail")}
                );
};

function NaviNotify() {
	wizNavi.notify( 
                   1,
                   1,
                   function(){console.log("[PHONEGAP //////////////] wizNavi notify - success")}, 
                   function(){console.log("[PHONEGAP //////////////] wizNavi notify - fail")}
                );
};


function NaviCreate() {
	wizNavi.create(
                   naviSettings,
                   function(){console.log("[PHONEGAP //////////////] wizNavi create - success")}, 
                   function(){console.log("[PHONEGAP //////////////] wizNavi create - fail")}
                   );
    
    wizNavi.onTapTab(function (tag){ alert(tag); }, function (error) { alert(error); });
};

function NaviShow() {
	wizNavi.show(
		function(){console.log("[PHONEGAP //////////////] wizNavi show - success")}, 
		function(){console.log("[PHONEGAP //////////////] wizNavi hide - fail")}
	);
};	

function NaviHide() {
	wizNavi.hide(
		function(){console.log("[PHONEGAP //////////////] wizNavi hide - success")}, 
		function(){console.log("[PHONEGAP //////////////] wizNavi hide - fail")}
	);
};	

function NaviToggle() {
	wizNavi.toggle(
	     function(){console.log("[PHONEGAP //////////////] wizNavi toggle - success")}, 
	     function(){console.log("[PHONEGAP //////////////] wizNavi toggle - fail")}
     );
};



