package jp.wizcorp.phonegap.plugin.WizNavi;

import jp.wizcorp.android.shell.AndroidShellActivity;
import jp.wizcorp.android.shell.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.phonegap.api.PhonegapActivity;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;


/**
 * 
 * @author WizCorp Inc. [ Incorporated Wizards ] 
 * @copyright 2011
 * @file WizNaviBar Class use with phonegap WizNaviPlugin
 * @about Builder and controller for Wizard Navigations
 *
 */

public class WizNaviBar {
	
	
	private static Context mContext;
	
	ViewGroup parentView; 
	public static RelativeLayout NavHolder;
	public static RelativeLayout buttonHolder;
	public static RelativeLayout mainView;
	
	// TODO auto get width heights
	int toolbarHeight;
	int screen_width;
	int screen_height;
	

	// Buttons
	private static Button aButton1;
	private static Button aButton2;
	private static Button aButton3;
	private static Button aButton4;
	private static Button aButton5;
	
	
	// Counters
	private TextView txtCount1;
	private TextView txtCount2;
	private TextView txtCount3;
	private TextView txtCount4;
	private TextView txtCount5;
	
	// Button Names Oject
	public static JSONObject buttonNames = new JSONObject();
	
	static JSONArray notificationArray;
	static JSONObject notificationObject;
	
	// Animations
	static Handler nav_slide_animHandler;
	static Runnable nav_slideUp;
	static Runnable nav_slideDown;
	static Runnable enable;
	static Runnable disable;
	
	//wiz Navi handlers
	static Handler nav_notify_Handler;
	static Runnable sendNavNotify;
	static Runnable sendBatchNavNotify;
	
	
	int startWithNavBar;
	
	static String onClick=null;
	static float animDuration;
	
	private static Activity that;

	
	public WizNaviBar (Activity mAct, JSONObject settings, PhonegapActivity ctx) {

		mContext = mAct.getApplicationContext();
		that = mAct;

		toolbarHeight	= AndroidShellActivity.getNavHeight();
		screen_width	= AndroidShellActivity.getWidth();
		screen_height	= AndroidShellActivity.getHeight();
		
		Log.d("WizNav", "[WIZNAVI] *************************************");
		Log.d("WizNav", "[WIZNAVI] binding - Wizard Navigationz");
		Log.d("WizNav", "[WIZNAVI] height - "+toolbarHeight+" px");
		Log.d("WizNav", "[WIZNAVI] *************************************");
		
		
		/*
	     * 	Animation Handlers
	     */
        nav_slide_animHandler = new Handler();
        // Create runnables for posting
        nav_slideUp = new Runnable() {
            public void run() {
				Log.d("WizNav", "[NAV ANIMATE] slideUp ******* ");
            	navSlideUpAnim();
            }
        };
        nav_slideDown = new Runnable() {
            public void run() {
				Log.d("WizNav", "[NAV ANIMATE] slideDown ******* ");
            	navSlideDownAnim();
            }
        };
        enable = new Runnable() {
            public void run() {
				Log.d("WizNav", "[NAV ANIMATE] enable ******* ");
            	navEnable();
            }
        };
        disable = new Runnable() {
            public void run() {
				Log.d("WizNav", "[NAV ANIMATE] disable ******* ");
            	navDisable();
            }
        };

        
        
		buttonHolder = new RelativeLayout(mAct.getApplicationContext());
		// Hide the buttonHolder
		RelativeLayout.LayoutParams bhl = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, toolbarHeight);
		bhl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		buttonHolder.setBackgroundColor(Color.BLACK);
		
		
		
		
		/*
	     * 	Set Button Listeners / default params
	     */
		Log.d("WizNav", "[WIZNAVI] Assign ******* buttons & listeners");
		
		aButton1 = (Button) ((Activity) mAct).findViewById(R.id.button1);
		// aButton1.setBackgroundResource(R.drawable.button1);

		parentView = (ViewGroup)aButton1.getParent();
		parentView.removeView(aButton1);
        aButton1.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View arg0) {
        		
        		try {
					onTapTab((String) buttonNames.get("button1"));
				} catch (JSONException e) {
					//give a default
					onTapTab("tab1");
				}
        		/*
        		if (aButton1.isSelected()){
        			aButton1.setSelected(false);
                } else {
                	aButton1.setSelected(true);
                }
                */
			}
    	});

        aButton2 = (Button) ((Activity) mAct).findViewById(R.id.button2);
        // aButton2.setBackgroundResource(R.drawable.button2);
        
        parentView = (ViewGroup)aButton2.getParent();
		parentView.removeView(aButton2);
        aButton2.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View arg0) {
        		try {
					onTapTab((String) buttonNames.get("button2"));
				} catch (JSONException e) {
					//give a default
					onTapTab("tab2");
				}
        		
        		/*
        		if (aButton2.isSelected()){
        			aButton2.setSelected(false);
                } else {
                	aButton2.setSelected(true);
                }
                */

			}
    	});
       
        
        aButton3 = (Button) ((Activity) mAct).findViewById(R.id.button3);
        aButton3.setSaveEnabled(true);
        // aButton3.setBackgroundResource(R.drawable.button3);
        
        parentView = (ViewGroup)aButton3.getParent();
		parentView.removeView(aButton3);
        aButton3.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View arg0) {
        		try {
					onTapTab((String) buttonNames.get("button3"));
				} catch (JSONException e) {
					//give a default
					onTapTab("tab3");
				}
        		
        		/*
        		if (aButton3.isSelected()){
        			aButton3.setSelected(false);
                } else {
                	aButton3.setSelected(true);
                }
                */
			}
    	});
        
        aButton4 = (Button) ((Activity) mAct).findViewById(R.id.button4);
        aButton4.setSaveEnabled(true);
        // aButton4.setBackgroundResource(R.drawable.button4);
        
        parentView = (ViewGroup)aButton4.getParent();
		parentView.removeView(aButton4);
        aButton4.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View arg0) {
        		try {
					onTapTab((String) buttonNames.get("button4"));
				} catch (JSONException e) {
					//give a default
					onTapTab("tab4");
				}
        		
        		/*
        		if (aButton4.isSelected()){
        			aButton4.setSelected(false);
                } else {
                	aButton4.setSelected(true);
                }
                */
			}
    	});
        
        aButton5 = (Button) ((Activity) mAct).findViewById(R.id.button5);
        aButton5.setSaveEnabled(true);
        // aButton5.setBackgroundResource(R.drawable.button5);
        
        parentView = (ViewGroup)aButton5.getParent();
        parentView.removeView(aButton5);
        aButton5.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View arg0) {
        		try {
					onTapTab((String) buttonNames.get("button5"));
				} catch (JSONException e) {
					//give a default
					onTapTab("tab5");
				}
        		
        		/*
        		if (aButton5.isSelected()){
        			aButton5.setSelected(false);
                } else {
                	aButton5.setSelected(true);
                }
                */
			}
    	});
		
        
        Log.d("WizNav", "[WIZNAVI] innit ******* hide all");
        
        // Hide the Nav bar on startup
		buttonHolder.setVisibility(View.INVISIBLE);
		aButton1.setVisibility(View.INVISIBLE);
		aButton2.setVisibility(View.INVISIBLE);
		aButton3.setVisibility(View.INVISIBLE);
		aButton4.setVisibility(View.INVISIBLE);
		aButton5.setVisibility(View.INVISIBLE);
			
        
        
		Log.d("WizNav", "[WIZNAVI] Assign ******* layouts");
		// Set layouts to Views
		buttonHolder.setLayoutParams(bhl);
							
		Log.d("WizNav", "[WIZNAVI] Assign ******* views");
		
		// Add Views to main View
		mainView = (RelativeLayout) ((Activity) mAct).findViewById(R.id.mainView);	// Holds Nav / Stat bars
		mainView.setBackgroundColor(Color.BLACK);
		mainView.addView(buttonHolder);
		
		buttonHolder.addView(aButton1);
		buttonHolder.addView(aButton2);
		buttonHolder.addView(aButton3);
		buttonHolder.addView(aButton4);
		buttonHolder.addView(aButton5);
		
		
		
		/*
	     * 	Set Counters
	     */
		Log.d("WizNav", "[WIZNAVI] Assign ******* counters");
		
		txtCount1 = (TextView) ((Activity) mAct).findViewById(R.id.txtCount1); 
		parentView = (ViewGroup)txtCount1.getParent();
        parentView.removeView(txtCount1);
        buttonHolder.addView(txtCount1);
        RelativeLayout.LayoutParams notifyLayout1 = (LayoutParams)txtCount1.getLayoutParams();
        notifyLayout1.topMargin = 5;
        notifyLayout1.leftMargin = 5;
        txtCount1.setLayoutParams(notifyLayout1);
        
        txtCount2 = (TextView) ((Activity) mAct).findViewById(R.id.txtCount2); 
        parentView = (ViewGroup)txtCount2.getParent();
        parentView.removeView(txtCount2);
        buttonHolder.addView(txtCount2);
        RelativeLayout.LayoutParams notifyLayout2 = (LayoutParams)txtCount2.getLayoutParams();
        notifyLayout2.topMargin = 5;
        notifyLayout2.leftMargin = (screen_width/5)+5;
        // notifyLayout.addRule(notifyLayout.leftMargin =50);
        // notifyLayout.leftMargin = 50;
        txtCount2.setLayoutParams(notifyLayout2);
        
        txtCount3 = (TextView) ((Activity) mAct).findViewById(R.id.txtCount3); 
        parentView = (ViewGroup)txtCount3.getParent();
        parentView.removeView(txtCount3);
        buttonHolder.addView(txtCount3);
        RelativeLayout.LayoutParams notifyLayout3 = (LayoutParams)txtCount3.getLayoutParams();
        notifyLayout3.topMargin = 5;
        notifyLayout3.leftMargin = ( (screen_width/5)*2 )+5;
        txtCount3.setLayoutParams(notifyLayout3);
        
        txtCount4 = (TextView) ((Activity) mAct).findViewById(R.id.txtCount4); 
        parentView = (ViewGroup)txtCount4.getParent();
        parentView.removeView(txtCount4);
        buttonHolder.addView(txtCount4);
        RelativeLayout.LayoutParams notifyLayout4 = (LayoutParams)txtCount4.getLayoutParams();
        notifyLayout4.topMargin = 5;
        notifyLayout4.leftMargin = ( (screen_width/5)*3 )+5;
        txtCount4.setLayoutParams(notifyLayout4);
        
        txtCount5 = (TextView) ((Activity) mAct).findViewById(R.id.txtCount5); 
        parentView = (ViewGroup)txtCount5.getParent();
        parentView.removeView(txtCount5);
        buttonHolder.addView(txtCount5);
        RelativeLayout.LayoutParams notifyLayout5 = (LayoutParams)txtCount5.getLayoutParams();
        notifyLayout5.topMargin = 5;
        notifyLayout5.leftMargin = ( (screen_width/5)*4 )+5;
        txtCount5.setLayoutParams(notifyLayout5);
        
        txtCount1.setVisibility(View.INVISIBLE);
        txtCount2.setVisibility(View.INVISIBLE);
        txtCount3.setVisibility(View.INVISIBLE);
        txtCount4.setVisibility(View.INVISIBLE);
        txtCount5.setVisibility(View.INVISIBLE);
        
		
        Log.d("WizNav", "[WIZNAVI] Assign ******* handlers");
        
        
        nav_notify_Handler = new Handler();
        // Create runnables for posting
        sendNavNotify = new Runnable() {
            public void run() {
				updateNotify();
            }
        };
        sendBatchNavNotify = new Runnable() {
            public void run() {
				updateBatchNotify();
            }
        };
        
        // now parse settings object (if exist?)
        
        
        try {

			if ( settings.has("duration")) {
				String temp = settings.getString("duration");
				animDuration = Float.parseFloat(temp);

			} else {
				// default is 0.8
				animDuration = (float) 0.8;
			}

			Log.d("WizNav", "[WIZNAVI || createNav] animDuration >> "+animDuration);
			
			if ( settings.has("tabs")) { 
				JSONArray tabs = (JSONArray) settings.getJSONArray("tabs");
			
				// define var out of for loop so we can re-assign
				JSONObject badgeData = new JSONObject();
				JSONObject imageData = new JSONObject();
				
				JSONObject tabObject = new JSONObject();
				
				for (int i=0; i <tabs.length(); i+=1) {
					
				
					tabObject 	= (JSONObject) tabs.get(i);
					
					String image = "";
					String pressed;
					String focus;

					// test for options
					/*
					if ( tabObject.has("sound")) {
						String sound 		= (String) tabObject.get("sound");
					}
					*/
					if ( tabObject.has("badge")) {
						int badge 			= (int) tabObject.getInt("badge");
						if (badge > 0) {
							badgeData.put(Integer.toString(i+1), badge);
						}
					}
					
					if ( tabObject.has("text")) {
						final String label 		= (String) tabObject.get("text");
						
						// int switchMe = i+1;

						switch (i+1) {
							case 1:
								aButton1.setText( label );
								break;
							case 2:
								aButton2.setText( label );
								break;
							case 3:
								aButton3.setText( label );
								break;
							case 4:
								aButton4.setText( label );
								break;
							case 5:
								aButton5.setText( label );
								break;
							default:
								
								break;
						}

					}
					
					
					if ( tabObject.has("name")) {
	
						String indexString 	= "button"+Integer.toString(i+1);
						Object value = tabObject.get("name");
						buttonNames.put(indexString, value);
	
					} else {
						// create default button name
						String indexString 	= "button"+Integer.toString(i+1);
						Object defaultName 	= "tab"+Integer.toString(i+1);
						buttonNames.put(indexString, defaultName);
					}
					
					
					if ( tabObject.has("image")) {
						image 				= (String) tabObject.get("image");
					} else {
						// fail we need this image name to create the nav bar
						// isSuccess = false;
						Log.e("WizNav", "[WIZNAVI || ERROR] NO IMAGE FOR CREATING NAVBAR");
					}
					
					
					if ( tabObject.has("pressed")) {
						pressed 			= (String) tabObject.get("pressed");
					} else {
						// if not pressed specified use image
						pressed 			= image;
					}
					
					
					if ( tabObject.has("focus")) {
						focus 				= (String) tabObject.get("focus");
					} else {
						// if not focus specified use image
						focus 				= image;
					}
					
					
					if ( 
							( image != null && image.length() != 0 ) &&
							( pressed != null && pressed.length() != 0 ) &&
							( focus != null && focus.length() != 0 ) 
						) {
						

						Log.d("WizNav", "[WIZNAVI || insert images] IMPLEMENT image >> "+image);
						Log.d("WizNav", "[WIZNAVI || insert images] IMPLEMENT pressed >> "+pressed);
						Log.d("WizNav", "[WIZNAVI || insert images] IMPLEMENT focus >> "+focus);

						// change images
						
						final String images [];
						images = new String[3];
						images[0] = image;
						images[1] = pressed;
						images[2] = focus;
						
						final int theTab = i+1;
						/*
						ctx.runOnUiThread(
				            new Runnable() {
				                public void run() {
				                	Log.d("WizNav", "[WIZNAVI || change image] theTab >> "+theTab+" images >> "+images[0]);
				                	navBGchange(theTab, images);
				                }
				            }
					    );
						*/
						
						//imageData.put(Integer.toString(i+1), images);
					
					}
					
					
					
				} // end for
				
				// if badges add them
				if (badgeData.length() > 0) {
					notificationObject = badgeData;
					nav_notify_Handler.post(sendBatchNavNotify);
				}
				
			} else {
				Log.d("WizNav", "[WIZNAVI || createNav] error no  tabs! ******* ########### ");
			}
			
		} catch (JSONException e) {
			// catch any other errors
			Log.d("WizNav", "[WIZNAVI || createNav] error  ******* ########### " +e);
		}

     

	} // ************ END CONSTRUCTOR **************
	

	
	private void navEnable() {
		// enable nav bar
		
		that.runOnUiThread(
	            new Runnable() {
	                public void run() {
	                		                	
	                	aButton1.setClickable(true);
	    		    	aButton2.setClickable(true);
	    		    	aButton3.setClickable(true);
	    		    	aButton4.setClickable(true);
	    		    	aButton5.setClickable(true);
	    		    	
	    		    	AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
	                	// Make animation instant
	                	alpha.setDuration(0); 		
	                	// Tell it to persist after the animation ends
	                	alpha.setFillAfter(true); 
	                	// And then on your layout
	                	buttonHolder.startAnimation(alpha);
	                	
	                }
	            }
	        );
	
	}
	
	private void navDisable() {
		// disable nav bar
		
		that.runOnUiThread(
            new Runnable() {
                public void run() {
                	
                	aButton1.setClickable(false);
    		    	aButton2.setClickable(false);
    		    	aButton3.setClickable(false);
    		    	aButton4.setClickable(false);
    		    	aButton5.setClickable(false);
    		    	
    		    	AlphaAnimation alpha = new AlphaAnimation(0.4F, 0.4F);
                	// Make animation instant
                	alpha.setDuration(0); 		
                	// Tell it to persist after the animation ends
                	alpha.setFillAfter(true); 
                	// And then on your layout
                	buttonHolder.startAnimation(alpha);

                }
            }
        );
		
	}
	private void navSlideDownAnim() {
    	// Animate down
    	
     	Animation slidedown = AnimationUtils.loadAnimation(mContext, R.anim.nav_slidedown);
     	slidedown.setFillAfter(true);
     	slidedown.setFillEnabled(true);
     	slidedown.setDuration((long) (animDuration *1000));
     	buttonHolder.startAnimation(slidedown);
     	slidedown.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// Once animated off-screen, hide the views
				aButton1.setVisibility(View.INVISIBLE);
				aButton2.setVisibility(View.INVISIBLE);
				aButton3.setVisibility(View.INVISIBLE);
				aButton4.setVisibility(View.INVISIBLE);
				aButton5.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO optional
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// Grow the webview to fit screen after nav bar is animated
				//resizeWebview("expandfornav");
				AndroidShellActivity.webviewExpandForNav();
				
			}
     	});
    	
    }
    
    private void navSlideUpAnim() {
    	// Animate up
    	
     	Animation slideup = AnimationUtils.loadAnimation(mContext, R.anim.nav_slideup);
     	slideup.setFillAfter(true);
     	slideup.setFillEnabled(true);
     	slideup.setDuration((long) (animDuration *1000));
     	buttonHolder.startAnimation(slideup);
     	slideup.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// Shrink the webview to fit screen minus nav bar height after nav bar animation
				//resizeWebview("shrinkfornav");
				
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO optional
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// Before animating on-screen, set all views VISIBLE
				buttonHolder.setVisibility(View.VISIBLE);
		    	aButton1.setVisibility(View.VISIBLE);
		    	aButton2.setVisibility(View.VISIBLE);
		    	aButton3.setVisibility(View.VISIBLE);
		    	aButton4.setVisibility(View.VISIBLE);
		    	aButton5.setVisibility(View.VISIBLE);
		    	AndroidShellActivity.webviewShrinkForNav();
			}
     	});
    	
    }
    
    
    public static void enable() {
		
    	nav_notify_Handler.post(enable);
	}

	public static void disable() {
		
		nav_notify_Handler.post(disable);
	}


	public static void showNav() {
		
		nav_slide_animHandler.post(nav_slideUp);
		//stats_slide_animHandler.post(stats_slideDown);
	}
	
	public static void hideNav() {
		nav_slide_animHandler.post(nav_slideDown);
		//stats_slide_animHandler.post(stats_slideUp);
	}
	
	
	public static void navNotify(JSONArray data) {
		// Recieve notifications on NavBar
		notificationArray = data;
		nav_notify_Handler.post(sendNavNotify);
		
	}
	
	private void updateBatchNotify() {
		// Send notifications to NavBar from Object
		
		try {
			Log.d("WizNav", "[WIZNAVI || updateBatchNotify] ALL >>  "+notificationObject);
			
			if ( notificationObject.has("1") ){
				String value 	= " "+Integer.toString( notificationObject.getInt("1") );
				TextView tabview1 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount1);
				tabview1.setText( value );
				tabview1.setVisibility(View.VISIBLE);
				
			} 
			if ( notificationObject.has("2") ){
				String value 	= " "+Integer.toString( notificationObject.getInt("2") );
				TextView tabview2 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount2);
				tabview2.setText( value );
				tabview2.setVisibility(View.VISIBLE);
				
			} 
			if ( notificationObject.has("3") ){
				
				String value 	= " "+Integer.toString( notificationObject.getInt("3") );
				TextView tabview3 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount3);
				tabview3.setText( value );
				tabview3.setVisibility(View.VISIBLE);
				
			} 
			if ( notificationObject.has("4") ){
				
				String value 	= " "+Integer.toString( notificationObject.getInt("4") );
				TextView tabview4 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount4);
				tabview4.setText( value );
				tabview4.setVisibility(View.VISIBLE);
				
			} 
			if ( notificationObject.has("5") ){
				
				String value 	= " "+Integer.toString( notificationObject.getInt("5") );
				TextView tabview5 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount5);
				tabview5.setText( value );
				tabview5.setVisibility(View.VISIBLE);
			
			}
		} catch (JSONException e) {
			// failed
		}
			
		
		
	}
	
	public void updateNotify() {
		// Send notifications to NavBar
		
		// Structure
		// [ 0, 0, 0, 0, 0 ] 
		
			
		int tab;
		String value;
		int checkValue;
		try {
			value = notificationArray.get(1).toString();
			checkValue = notificationArray.getInt(1);
			if (checkValue < 10) {
				value = " "+value;
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		
		
		try {
			tab = notificationArray.getInt(0);
			Log.d("WizNav", "[WIZNAVI || updateNotify] value  ******* ########### " +value);
			switch (tab) {
			case 1 : TextView tabview1 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount1);
						tabview1.setText(value);
						if (checkValue == 0) {
							tabview1.setVisibility(View.INVISIBLE);
						} else {
							tabview1.setVisibility(View.VISIBLE);
						}
				break;
			case 2 : TextView tabview2 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount2);
						tabview2.setText(value);
						if (checkValue == 0) {
							tabview2.setVisibility(View.INVISIBLE);
						} else {
							tabview2.setVisibility(View.VISIBLE);
						}
				break;
			case 3 : TextView tabview3 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount3);
						tabview3.setText(value);
						if (checkValue == 0) {
							tabview3.setVisibility(View.INVISIBLE);
						} else {
							tabview3.setVisibility(View.VISIBLE);
						}
				break;
			case 4 : TextView tabview4 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount4);
						tabview4.setText(value);
						if (checkValue == 0) {
							tabview4.setVisibility(View.INVISIBLE);
						} else {
							tabview4.setVisibility(View.VISIBLE);
						}
				break;	
			case 5 : TextView tabview5 = (TextView) ((Activity) mContext).findViewById(R.id.txtCount5);
						tabview5.setText(value);
						if (checkValue == 0) {
							tabview5.setVisibility(View.INVISIBLE);
						} else {
							tabview5.setVisibility(View.VISIBLE);
						}
				break;	
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		
			
		
		
		
		
		
		
	}
	
	
	
	private static void navBGchange(int theTab, String[] images) {
		
		/*
		StateListDrawable states = new StateListDrawable();
		
		if (images[0].equalsIgnoreCase("noImage") ){
			images[0] = "no_image";
		}
		if (images[1].equalsIgnoreCase("noImage") ){
			images[1] = "no_image";
		}
		if (images[2].equalsIgnoreCase("noImage") ){
			images[2] = "no_image";
		}
		
		int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+images[0], null, null);
		int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+images[1], null, null);
		int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+images[2], null, null);
		
		Drawable drawImage = mContext.getResources().getDrawable(bgImage);
		Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
		Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);

		states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
		states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
		states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
		states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);
		
		switch (theTab) {
		case 1 : 
			
			aButton1.setBackgroundDrawable(states);
			
			break;
		
		case 2 :
			
			aButton2.setBackgroundDrawable(states);
			
			break;
		case 3 :
			
			aButton3.setBackgroundDrawable(states);
			
			break;
		case 4 :
			
			aButton4.setBackgroundDrawable(states);
			
			break;
		case 5 :
			
			aButton5.setBackgroundDrawable(states);
			
			break;
		}
		

		Log.d("WizNav", "[WIZNAVI || navBGchange] add image >> "+images[0] + " to the button >> "+theTab);
		*/
	
	}
	

	public static Boolean setTab(JSONArray data){
		
		// TODO: dynamically change images on bar
		
		Boolean isSuccess = null;
		
		String image 	= null;
		// String pressed 	= null;
		// String focus 	= null;
		String label 	= null;
		// String sound 	= null;
		String name 	= null;
		Boolean active 	= false; // by default
		
		
		try {
			JSONObject dataObj = data.getJSONObject(1);
			Object checktab = data.get(0);
			String tabString = null;
			
			int tab = 0;
			if (checktab instanceof java.lang.Integer) {
				// checktab is a number
				tab = (Integer) data.get(0);

			} else {
				// checktab is a string so lets check the nane in our names object
				tabString = checktab.toString();
				if (buttonNames.get("button1").equals(tabString)) {
					tab = 1;
				} else if (buttonNames.get("button2").equals(tabString)) {
					tab = 2;
				} else if (buttonNames.get("button3").equals(tabString)) {
					tab = 3;
				} else if (buttonNames.get("button4").equals(tabString)) {
					tab = 4;
				} else if (buttonNames.get("button5").equals(tabString)) {
					tab = 5;
				} else {
					Log.e("WizNav", "[WIZNAVI || SETTAB] IMPLEMENT >> no tab found - return ");
					tab = 0;
					tabString = null;
					return false;
				}

			}
			
			Log.d("WizNavi", "[WIZNAVI || SETTAB] data: "+data+" tabString : "+tabString+" tab number or name is >> "+tab);

			
			
			if ( dataObj.has("active")) {
				active = (Boolean) dataObj.get("active");
			} else {
				active = false;
			}
			Log.d("WizNav", "[WIZNAVI || SETTAB] active is : "+active+" tab : "+tab);
			
			if ( dataObj.has("text")) {
				label = (String) dataObj.get("text");
			}
			/*
			if ( dataObj.has("sound")) {
				sound = (String) dataObj.get("sound");
			}
			*/
			
			
			if ( dataObj.has("image")) {
				image = (String) dataObj.get("image");
			}

			/*
			if ( dataObj.has("pressed")) {
				pressed = (String) dataObj.get("pressed");
			} else {
				if (image != null) {
					pressed = image;
				}
			}
			
			if ( dataObj.has("focus")) {
				focus = (String) dataObj.get("focus");
			} else {
				if (image != null) {
					focus = image;
				}
			}
			*/
			
			if ( dataObj.has("name")) {
				name = (String) dataObj.get("name");
			}

			
			
			// final StateListDrawable states = new StateListDrawable();
		
			if (active == false) {
				
				that.runOnUiThread(
					new Runnable() {
						public void run() {
							aButton1.setSelected(false);
							aButton2.setSelected(false);
							aButton3.setSelected(false);
							aButton4.setSelected(false);
							aButton5.setSelected(false);
						}
					}
				);
				
			}
			
  			switch (tab) {
 
				case 1 : 
							
					
					// is active?
					if (active == true) {
						
						that.runOnUiThread(
							new Runnable() {
								public void run() {
									aButton1.setSelected(true);

								}
							}
						);
						
					}
					
					
					// Change image
					if (image != null) {
						if (image.contains("noImage")) {
							that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton1.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
									}
								}
							);
						}						
					}
					/*
					// Change image
					if ( 
							image != null && ( pressed != null && focus != null )
						) {
						
						int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+image, null, null);
						int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+pressed, null, null);
						int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+focus, null, null);
						
						Drawable drawImage = mContext.getResources().getDrawable(bgImage);
						Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
						Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);
	
						states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
						states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
						states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
						states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);

						that.runOnUiThread(
				            new Runnable() {
				                public void run() {
									aButton1.setBackgroundDrawable(states);
				                }
				            }
					    );
					}
					*/
					
					// change text label
					if (label != null) {
						final String _label = label;
						that.runOnUiThread(
					            new Runnable() {
					                public void run() {
					                	aButton1.setText( _label );
					                }
					            }
					     );
					}
					
					
					// change name
					if (name != null) {
						buttonNames.remove("button1");
						Object value = name;
						buttonNames.put("button1", value);
					}

					isSuccess = true;
					break;
				case 2 : 
					
					// is active?
					if (active == true) {

						that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton2.setSelected(true);

									}
								}
						);
						
					}
					
					
					// Change image
					if (image != null) {
						Log.d("WizNavi", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
						if (image.contains("noImage")) {
							Log.d("WizNavi", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
							that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton2.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
									}
								}
							);
						}						
					}
					  
					  
					  
					/*
					if ( 
							image != null && ( pressed != null && focus != null )
						) {
						
						int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+image, null, null);
						int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+pressed, null, null);
						int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+focus, null, null);
						
						Drawable drawImage = mContext.getResources().getDrawable(bgImage);
						Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
						Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);
	
						states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
						states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
						states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
						states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);

						that.runOnUiThread(
				            new Runnable() {
				                public void run() {

									aButton2.setBackgroundDrawable(states);
				                }
				            }
					    );
					}
					*/
					
					// change text label
					if (label != null) {
						final String _label = label;
						that.runOnUiThread(
					            new Runnable() {
					                public void run() {
					                	aButton2.setText( _label );
					                }
					            }
					     );
					}

					// change tab name
					if (name != null) {
						buttonNames.remove("button2");
						Object value = name;
						buttonNames.put("button2", value);
					}
					
					isSuccess = true;
					break;
				case 3 : 

									
					// is active?
					if (active == true) {
						that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton3.setSelected(true);

									}
								}
						);
					}
			    		
					
					// Change image
					if (image != null) {
						if (image.contains("noImage")) {
							that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton3.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
									}
								}
							);
						}						
					}
					
					
					/*
					// Change image
					if ( 
							image != null && ( pressed != null && focus != null )
						) {
						
					
						int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+image, null, null);
						int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+pressed, null, null);
						int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+focus, null, null);
						
						Drawable drawImage = mContext.getResources().getDrawable(bgImage);
						Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
						Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);
	
						states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
						states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
						states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
						states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);

						that.runOnUiThread(
				            new Runnable() {
				                public void run() {

									aButton3.setBackgroundDrawable(states);
				                }
				            }
					    );
					}
					*/
					
					// change text label
					if (label != null) {
						final String _label = label;
						that.runOnUiThread(
					            new Runnable() {
					                public void run() {
					                	aButton3.setText( _label );
					                }
					            }
					     );
						
					}

					// change tab name
					if (name != null) {
						buttonNames.remove("button3");
						Object value = name;
						buttonNames.put("button3", value);
					}
					
					
					isSuccess = true;
					break;
				case 4 : 

					
					// is active?
					if (active) {

			    		that.runOnUiThread(
				            new Runnable() {
				                public void run() {
				                	aButton4.setSelected(true);

				                }
				            }
				        );
					}
					
					
					// Change image
					if (image != null) {
						if (image.contains("noImage")) {
							that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton4.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
									}
								}
							);
						}						
					}
					
					/*
					// Change image
					if ( 
							image != null && ( pressed != null && focus != null )
						) {
						
					
						int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+image, null, null);
						int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+pressed, null, null);
						int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+focus, null, null);
						
						Drawable drawImage = mContext.getResources().getDrawable(bgImage);
						Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
						Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);
	
						states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
						states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
						states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
						states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);

						that.runOnUiThread(
				            new Runnable() {
				                public void run() {

									aButton4.setBackgroundDrawable(states);
				                }
				            }
					    );
					}
					*/
					
					// change text label
					if (label != null) {
						final String _label = label;
						that.runOnUiThread(
					            new Runnable() {
					                public void run() {
					                	aButton4.setText( _label );
					                }
					            }
					     );
						
					}

					
					// change tab name
					if (name != null) {
						buttonNames.remove("button4");
						Object value = name;
						buttonNames.put("button4", value);
					}
					
					isSuccess = true;
										
					break;	
				case 5 : 


					// is active?
					if (active) {

			    		that.runOnUiThread(
				            new Runnable() {
				                public void run() {
				                	aButton5.setSelected(true);

				                }
				            }
				        );
					}
					
					
					// Change image
					if (image != null) {
						if (image.contains("noImage")) {
							that.runOnUiThread(
								new Runnable() {
									public void run() {
										aButton5.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
									}
								}
							);
						}						
					}
					
					
					/*
					// Change image
					if ( 
							image != null && ( pressed != null && focus != null )
						) {
						
						int bgImage = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+image, null, null);
						int bgPressed = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+pressed, null, null);
						int bgFocus = mContext.getResources().getIdentifier(mContext.getPackageName()+":drawable/"+focus, null, null);
						
						Drawable drawImage = mContext.getResources().getDrawable(bgImage);
						Drawable drawFocus = mContext.getResources().getDrawable(bgFocus);
						Drawable drawPressed = mContext.getResources().getDrawable(bgPressed);
	
						states.addState(new int[]{android.R.attr.state_pressed}, drawPressed);
						states.addState(new int[]{android.R.attr.state_focused}, drawFocus);
						states.addState(new int[]{android.R.attr.state_enabled}, drawImage);
						states.addState(new int[]{-android.R.attr.state_enabled}, drawPressed);

						that.runOnUiThread(
				            new Runnable() {
				                public void run() {

									aButton5.setBackgroundDrawable(states);
				                }
				            }
					    );
					}
					*/
					
					// change text label
					if (label != null) {
						final String _label = label;
						that.runOnUiThread(
					            new Runnable() {
					                public void run() {
					                	aButton5.setText( _label );
					                }
					            }
					     );
					}

					// change tab name
					if (name != null) {
						buttonNames.remove("button5");
						Object value = name;
						buttonNames.put("button5", value);
					}
					
					isSuccess = true;
					
					break;	
					
					default:
						break;
				}
		
			
  				// release vars for GC
  				label = null;
  				name = null;
  				
			
		
		} catch (JSONException e) {
			Log.d("WizNav", "[WIZNAVI || setTab] error  ******* ########### " +e);
			isSuccess = false;
		}
		
		return isSuccess;
	}

	
	
	/*
     * 	Button Listeners
     */
    private void onTapTab(String buttonName) {
    	
    	String tapCallback = WizNaviPlugin.getTapCallback();
    	Log.d("WizNav", "[WIZNAVI || onTapTab] return tap  ******* ########### " +tapCallback +" >> " +buttonName);
    	
    	PluginResult resulter = new PluginResult(com.phonegap.api.PluginResult.Status.OK, buttonName);
    	resulter.setKeepCallback(true);

    	Plugin wizNaviPlugin = WizNaviPlugin.getPluginObj(); 
    	
    	wizNaviPlugin.success(resulter, tapCallback);

	}

	

}
