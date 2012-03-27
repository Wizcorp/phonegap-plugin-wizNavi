


# PLUGIN: 

phonegap-plugin-wizNavi



# DESCRIPTION :

PhoneGap plugin for building native navigation component.

example:-

![iPhone example](https://github.com/Wizcorp/phonegap-plugin-wizNavi/raw/master/example.jpg)





# INSTALL (iOS):

Project tree<br />

<pre><code>
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
</code></pre>



1 ) Arrange files to structure seen above.

2 ) Add to phonegap.plist in the plugins array;<br />
Key : WizNaviPlugin<br />
Type : String<br />
Value : WizNaviPlugin<br />

3 ) Add \<script\> tag to your index.html<br />
\<script type="text/javascript" charset="utf-8" src="phonegap/plugin/wizNavi/wizNavi.js"\>\</script\><br />
(assuming your index.html is setup like tree above)


3 ) Follow example code below.






# INSTALL (Android BETA!):


Project setup should be quite self explanatory.
Look at layout/main.xml to get an idea of layout structure (hoping wizNavi will have its own xml later).
Remember to add the plugin to plugins.xml

Add the following to your main Activity.
<pre><code>
// Vars
public static int width = 0;
public static int height = 0;


// Views
public static View html;
public static RelativeLayout view;
public static RelativeLayout mainView;
// Webview resize handlers
static Handler webview_Handler;
static Runnable webview_shrinkfornav;
static Runnable webview_expandfornav

/*
 * 	Reset Layouts (re-structure phonegap webview - enclose in relative layout)
 */
super.init();
setContentView(R.layout.main);
mainView = (RelativeLayout)findViewById(R.id.mainView);			// Holds Nav / Stat bars
view = (RelativeLayout)findViewById(R.id.phonegap_container); 	// Holds PhoneGap


/*
 * 	Re-configure PhoneGap "appView" layout
 */
html = (View)super.appView.getParent();
//html.setBackgroundColor(Color.parseColor("#212c39"));

super.appView.setClipToPadding(true);
view.setClipToPadding(true);
view.addView(html, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));


/*
 * 	Boot URL
 */
super.loadUrl(indexUrl);


/*
 * 	Remove android system top bar
 */
   		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
   				WindowManager.LayoutParams.FLAG_FULLSCREEN | 
   				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);



/*
 * 	Get device screen size
 */
Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
width = display.getWidth(); 
height = display.getHeight();

// calculate bar heights
statsHeight = (int) (height/8.74);
navHeight	= (int)	(height/9.6);

 /*
 * 	Resize WebView Handlers
 */
webview_Handler = new Handler();
// Create runnables for posting
webview_shrinkfornav = new Runnable() {
    public void run() {
    		Log.d("Android Activity", "[RESIZE] shrinkfornav ******* ");
			resizeWebview("shrinkfornav");
    }
};
webview_expandfornav = new Runnable() {
    public void run() {
    		Log.d("Android Activity", "[RESIZE] expandfornav ******* ");
			resizeWebview("expandfornav");
    }
};
webview_shrinkforstats = new Runnable() {
    public void run() {
    		Log.d("Android Activity", "[RESIZE] shrinkforstats ******* ");
			resizeWebview("shrinkforstats");
    }
};
webview_expandforstats = new Runnable() {
    public void run() {
    		Log.d("Android Activity", "[RESIZE] expandforstats ******* ");
			resizeWebview("expandforstats");
    }
};
        
        
        
        
        
(outside of main class … )


/*
 * 	Webview size Controller
 */
public void resizeWebview(String str) {
	// Webview resizer
	if (str =="shrinkfornav"){
		
			if (startWithStatBar == true) {
				// statbar resize will handle nav too.
				Log.d("Android Activity", "[RESIZE] shrinkforNAV -startWithStatBar-true ");
				super.appView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (height-(navHeight+statsHeight)) ));
			} else {
				Log.d("Android Activity", "[RESIZE] shrinkforNAV -startWithStatBar-false ");
				super.appView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height-navHeight ));
			}
			
	} else if (str == "expandfornav") {
		
		Log.d("Android Activity", "[RESIZE] expandforNAV ******* ");
		super.appView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ));

	} else if (str == "shrinkforstats") {

		
	} else if (str == "expandforstats") {

		
	}
}
    
    
    
public static void webviewShrinkForNav() {
	// Post to handler so we can run on main thread
	webview_Handler.post(webview_shrinkfornav);
}
public static void webviewExpandForNav() {
	// Post to handler so we can run on main thread
	webview_Handler.post(webview_expandfornav);
}
public static void webviewShrinkForStats() {
	// Post to handler so we can run on main thread
	webview_Handler.post(webview_shrinkforstats);
}
public static void webviewExpandForStats() {
	// Post to handler so we can run on main thread
	webview_Handler.post(webview_expandforstats);
}
</pre></code>


Sorry that is it for now..
(Better docs coming soon ..)




<br />
# Example Code

Example setup<br />
<pre><code>
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
</code></pre>


Example create<br />
```
wizNavi.create(exampleNaviSettings, null, null);
```


Example show<br />
```
wizNavi.show();
```


Example notify<br />
```
wizNavi.notify("button1",5, null, null);
```


Example hide<br />
```
wizNavi.hide();
```


Example set tabs<br />
<pre><code>
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
};

naviSetTabs();
</code></pre>


Example enable<br />
```
wizNavi.enable();
```


Example disable<br />
```
wizNavi.disable();
```