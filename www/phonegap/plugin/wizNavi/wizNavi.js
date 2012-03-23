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
        PhoneGap.exec(s, f, "WizNaviPlugin", "onTapTab", []);
    
    },
	
	show: function(a,s,f) {
	
		return PhoneGap.exec(s, f, "WizNaviPlugin", "show", [a]);
		 
	},
	
	
	hide: function(s,f) {
	
        return PhoneGap.exec(s, f, "WizNaviPlugin", "hide", []);

	},
    
    notify: function(tab, value, s, f) {
        value = value.toString();
        return PhoneGap.exec(s, f, "WizNaviPlugin", "notify", [tab, value]);
        
    },
    
    setTab: function(a, b, s, f) {

        return PhoneGap.exec(s, f, "WizNaviPlugin", "setTab", [a, b]);
        
    },
    
    setTabs: function(a, s, f) {
        
        return PhoneGap.exec(s, f, "WizNaviPlugin", "setTabs", [a]);
        
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