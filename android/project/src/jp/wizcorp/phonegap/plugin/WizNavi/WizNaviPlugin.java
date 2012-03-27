package jp.wizcorp.phonegap.plugin.WizNavi;

import jp.wizcorp.android.shell.AndroidShellActivity;
import jp.wizcorp.android.shell.R;
import jp.wizcorp.phonegap.plugin.StatBar.StatBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

/**
 * 
 * @author WizCorp Inc. [ Incorporated Wizards ] 
 * @copyright 2011
 * @file StatBarPlugin for PhoneGap
 * @about Handle JavaScript API calls from PhoneGap to WizNavi
 *
 */
public class WizNaviPlugin extends Plugin {

	/*
	 * 
	 * JavaScript Usage ->
	 * a is an array (send empty array if nothing to declare to StatBar)
	 * s is success callback
	 * f is fail callback
	 * 
	 * var wizNavi = { 
	 * 
	 * 		show: function(s,f) {
	 *			return PhoneGap.exec( s, f, 'StatBarPlugin', 'moreCoins', [{animate: true}] );	
	 *		},
	 * 
	 * 
	 * }
	 * 
	 * example call
	 * 
	 * 	function myFunction() {
	 *   	wizNavi.show(
	 *			function(){console.log("[PHONEGAP //////////////] wizNavi show - success")}, 
	 *			function(){console.log("[PHONEGAP //////////////] wizNavi hide - fail")}
	 *		);
	 * 
	 * 
	 * 
	 * 
	 */
	
	JSONObject returnObj;
	JSONArray returnArr;
	
	static String tapTabCallbackId;
	static String setTabCallbackId;
	static String returnbuttonName;
	
	static Plugin thisPlugin;
	
	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId)  {
		
		PluginResult result = null;
		thisPlugin = this;
		
		if (action.equals("create")) {
			// create the nav bar
			Log.d("WizNaviPlugin", "[create] ****** ");
			JSONObject dataObj = new JSONObject();
			try {
				
				dataObj = data.getJSONObject(0);
				final JSONObject settings = dataObj;
				// get DroidGap context from main Activity
				final Activity mAct = AndroidShellActivity.getActivity();
				
				ctx.runOnUiThread(
		            new Runnable() {
		                public void run() {
		                	@SuppressWarnings("unused")
		    		        WizNaviBar newNaviBar = new WizNaviBar(mAct, settings, ctx);
		                }
		            }
		        );

				result = new PluginResult(Status.OK);
			} catch (JSONException e1) {
				result = new PluginResult(Status.ERROR, "failed");
			}
			
		} else if (action.equals("onTapTab")) {
			// Set up button listeners
			Log.d("WizNaviPlugin", "[onTapTab] set callbackID ****** ");
			
			// Tell PhoneGap to wait, callback will be sent everytime we click a button.
			tapTabCallbackId = callbackId;
			result = new PluginResult(Status.NO_RESULT);
			result.setKeepCallback(true);
	
		} else if (action.equals("setTab")) {

			// Customise a tab
			Log.d("WizNaviPlugin", "[setTab] set the tab ==================== "+callbackId);

			Boolean isSuccess = WizNaviBar.setTab(data);
			
			if (isSuccess) {
				result = new PluginResult(Status.OK);
			} else {
				result = new PluginResult(Status.ERROR, "failed to setTab");
			}

		} else if (action.equals("notify")) {
			// Send notification
			Log.d("WizNaviPlugin", "[notify] ****** ");
			navNotify(data, callbackId);

			PluginResult pluginResulter = new PluginResult(Status.NO_RESULT);
			pluginResulter.setKeepCallback(true);
			return pluginResulter;
			
			
		} else if (action.equals("hide")) {
			// Hide the bottom nav bar
			
			Log.d("WizNaviPlugin", "[hide navBar] ****** ");
			WizNaviBar.hideNav();

			result = new PluginResult(Status.OK);
			
		} else if (action.equals("show")) {
			//show the bottom nav bar
			
			Log.d("WizNaviPlugin", "[show navBar] ****** ");
			WizNaviBar.showNav();
			
			result = new PluginResult(Status.OK);
			
		} else if (action.equals("enable")) {
			//show the bottom nav bar
			Log.d("WizNaviPlugin", "[enable navBar] ****** ");
			WizNaviBar.enable();
			
			result = new PluginResult(Status.OK);
		} else if (action.equals("disable")) {
			//show the bottom nav bar
			Log.d("WizNaviPlugin", "[disable navBar] ****** ");
			WizNaviBar.disable();
			
			result = new PluginResult(Status.OK);
		}
		
		
		return result;
	}
	
	

	private void navNotify(JSONArray data, String callbackId) {
		// Parse data and send to main thread
		
		// Structure
		// [ 0, 0 ] 
		String notifies;
		
		notifies = data.toString();
		Log.d("StatBarPlugin", "[sendNotify] SENT "+ notifies);
		WizNaviBar.navNotify(data);
	
		PluginResult navData = null;
		navData = new PluginResult(Status.OK);
		success(navData,callbackId);
	}

	
	public static String getTapCallback() {
		// return to javascript a success callback with name of button called
		return tapTabCallbackId;
	}

	public static Plugin getPluginObj() {
		// return the Plugin for access outside of plugin
		return thisPlugin;
	}



}
