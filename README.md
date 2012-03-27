


# PLUGIN: 

phonegap-plugin-wizNavi



# DESCRIPTION :

PhoneGap plugin for building native navigation component.

example:-

![iPhone example](https://github.com/Wizcorp/phonegap-plugin-wizNavi/raw/master/example.jpg)





# INSTALL (iOS):

Project tree

project
	/ www
		-index.html
		/ phonegap
			/ plugin
				/ wizNavi
					/ wizNavi.js		
	/ Classes	
	/ Plugins
		/ WizNaviPlugin 
			/ WizNaviPlugin.h
			/ WizNaviPlugin.m
	/ Resources
		/ nav
			/ [all images here]
	-project.xcodeproj
	


1 ) Arrange files to structure seen above.

2 ) Add to phonegap.plist in the plugins array;
Key : WizNaviPlugin
Type : String
Value : WizNaviPlugin

3 ) Add <script> tag to your index.html
<script type="text/javascript" charset="utf-8" src="phonegap/plugin/wizNavi/wizNavi.js"></script>
(assuming your index.html is setup like tree above)


3 ) Follow example code below.






# INSTALL (Android BETA!):

Docs coming soon...




# Example Code

// Example setup
var exampleNaviSettings = {
                     
	height 				: 70 ,
	statBarHeight       : 0 ,
	duration            : 1.0 ,
	tabs : 	
	[	{

			badge 		:  3 ,
			name       	: "button1" ,
			text 		: "My Page" ,
			image 		: "btn1"
		} ,
		{
			badge 		: 1 ,
			name       	: "button2" ,
			text 		: "News" ,
			sound 		: "" ,
			image 		: "btn2" ,
			pressed		: "btn2_over" ,
			focus		: "btn2_over" 
		} ,
		{
			badge 		: 0 ,
			text 		: "Wizard" ,
			name       	: "button3" ,
			sound 		: "" ,
			image 		: "btn3" ,
			pressed		: "btn3_over" ,
			focus		: "btn3_over" 
		} ,
		{
			badge 		: 0 ,
			text 		: "Navi" ,
			name       	: "button4" ,
			sound 		: "" ,
			image 		: "btn4" ,
			pressed		: "btn4_over" ,
			focus		: "btn4_over" 
		} ,
		{
			badge 		: 1 ,
			text 		: "Bar" ,
			name       	: "button5" ,
			sound 		: "" ,
			image 		: "btn5" ,
			pressed		: "btn5_over" ,
			focus		: "btn5_over" 
		}
	]
};



// Example create

wizNavi.create(exampleNaviSettings, null, null);


// Example show

wizNavi.show();


// Example notify

wizNavi.notify("button1",5, null, null);


// Example hide

wizNavi.hide();


// Example set tabs

function naviSetTabs()
{
                    
	var tab1 = new Array ("button1",{
	                      badge 		: 1 ,
	                      name          : "button2" ,
	                      text          : "ftw" ,
	                      image 		: "btn3" ,
	                      pressed       : "btn3_over" ,
	                      focus         : "btn3_over" 
	                      });
	
	var tab2 = new Array ("button3",{
	                      badge 		: 2 ,
	                      name          : "button1" ,
	                      text          : "lol" ,
	                      image 		: "btn3" ,
	                      pressed       : "btn3_over" ,
	                      focus         : "btn3_over" 
	                      });
	
	wizNavi.setTabs([tab1, tab2] , null , null);
}

naviSetTabs();



// Example enable

wizNavi.enable();


// Example disable

wizNavi.disable();