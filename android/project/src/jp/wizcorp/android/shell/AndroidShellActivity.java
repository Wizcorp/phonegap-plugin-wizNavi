package jp.wizcorp.android.shell;


import jp.wizcorp.phonegap.plugin.GiraffePlugin.GiraffeManager;
import jp.wizcorp.phonegap.plugin.InAppPurchaseManager.InAppPurchaseManager;
import jp.wizcorp.phonegap.plugin.WizAssets.WizAssetManager;
import jp.wizcorp.phonegap.plugin.WizViewManager.WizViewManagerPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.android.vending.billing.BillingService;
import com.android.vending.billing.BillingService.RequestPurchase;
import com.android.vending.billing.BillingService.RestoreTransactions;
import com.android.vending.billing.Consts.PurchaseState;
import com.android.vending.billing.Consts.ResponseCode;
import com.android.vending.billing.IMarketBillingService;
import com.android.vending.billing.PurchaseDatabase;
import com.android.vending.billing.PurchaseObserver;
import com.android.vending.billing.ResponseHandler;
import com.phonegap.DroidGap;



/**
 * 
 * @author WizCorp Inc. [ Incorporated Wizards ] 
 * @copyright 2011
 * @file AndroidShellActivity.java
 * @about Main Activity
 *
 */

public class AndroidShellActivity extends DroidGap {
	
	// Config Vars
	// ** If ad Id's are set to "null" requests will be switched off **
	private static int statsHeight 		= 0;										// Modify height of tooldbars (if modify, remember to edit images too), bar auto adapts to screen dpi
	private static int navHeight 		= 0;
	private Boolean startWithNavBar 	= true;										// Hide or show bottom bar on start up?
	private Boolean startWithStatBar 	= true;										// Hide or show top stats bar on start up?
	
	/*
	private String googleAdId 	= "null"; // "a14e54789db3caa";						// Google Ads Id
	private String apsalarId 	= "ghansali";										// ApsalarId (example "bob")
	private String apsalarKey 	= "ywsAZWjU";										// ApsalarKey
	*/
	
	private String gameUrl 		= "file:///android_asset/www/index_game.html";
	// private String gameUrl 		= "file:///android_asset/www/index_test.html";
	
	
	
	
	
	
    /**
     * The SharedPreferences key for recording whether we initialized the
     * database.  If false, then we perform a RestoreTransactions request
     * to get all the purchases for this user.
     *     
     */
	private static final String DB_INITIALIZED = "db_initialized";

	
	// Vars
	public static int width = 0;
	public static int height = 0;

	
	// Views
	// private AdView adView;
	public static View html;
	public static RelativeLayout view;
	public static RelativeLayout mainView;

	
	// Webview resize handlers
	static Handler webview_Handler;
	static Runnable webview_shrinkfornav;
	static Runnable webview_expandfornav;
	static Runnable webview_shrinkforstats;
	static Runnable webview_expandforstats;
	
	// Asset Manager
	WizAssetManager wizAman;
	
	// Open Feint Handlers
	static Handler of_Handler;
	static Runnable of_boot;
	private static GiraffeManager Auth;
	
	
	static Activity that;
	
	// IAP
	public static IMarketBillingService mService;
	private static String purchaseItemId;
	public static Handler sPurchaseHandler;
	static Runnable postPurchase;
	static Runnable postSetup;
	static Runnable postGetPendingTransactions;
	
	public Handler mHandler;
	public BillingService mBillingService;
	private gamePurchaseObserver mGamePurchaseObserver;
	private PurchaseDatabase mPurchaseDatabase;



	
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        that 		= this;
        
        
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
	     * 	Splash settings
	     */
        appView.addJavascriptInterface(new AndroidShellSplashAddon(this, html), "SplashScreen");
        
        appView.enablePlatformNotifications();
        
        
        /*
	     * 	Boot URL
	     */
        super.loadUrl(gameUrl);
        
        
        /*
	     * 	Remove android system top bar
	     */
   	   		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
   	   				WindowManager.LayoutParams.FLAG_FULLSCREEN | 
   	   				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        
        
        
        /*
   	     * 	 Init Apsalar
   	     */
        /*
        if (apsalarId != "null") {
	        Apsalar.startSession(this, apsalarId, apsalarKey);
	        Apsalar.event("Booting Android Test App");
	        
        }
        */
        
        
   		/*
   	     * 	Init the Google adView
   	     */
        /*
        if (googleAdId != "null") {
	   	    adView = new AdView(this, AdSize.BANNER, googleAdId);
	   	    adView.loadAd(new AdRequest());
        }
   	    */
        
        
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
	     * 	Start Wizard Asset Manager
	     */
		wizAman = new WizAssetManager(this.getApplicationContext() );
        
        
        /*
	     * 	OpenFeint Boot Handlers
	     */
        of_Handler = new Handler();
        // Create runnables for posting
        of_boot = new Runnable() {
            public void run() {
            	
				Log.d("OpenFeintPlugin", "[OPEN FEINT] boot ******* ");
				Auth = new GiraffeManager();
				Auth.bootOpenFeintMain();
			
            }
            
        };
        
        
        
        
        
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
        
        
        
        /*
	     * 	purchase Handlers
	     */
        sPurchaseHandler = new Handler();
        postPurchase = new Runnable() {
            public void run() {
            		postMakePurchase();
            }
        };
        postSetup = new Runnable() {
            public void run() {
            	
        	    // Attempt to bind to Billing Market Service
        	     
                mHandler = new Handler();
                mGamePurchaseObserver = new gamePurchaseObserver(mHandler);
                mBillingService = new BillingService();
                mBillingService.setContext(that.getApplicationContext());
                mPurchaseDatabase = new PurchaseDatabase(that.getApplicationContext());
                ResponseHandler.register(mGamePurchaseObserver);
                if (!mBillingService.checkBillingSupported()) {
                	Log.e("MarketBillingService", "[checkBillingSupported] billing not supported! ");
                	InAppPurchaseManager.callbackSetup(false);
                } else {
                	InAppPurchaseManager.callbackSetup(true);
                }
            }
        };
        postGetPendingTransactions = new Runnable() {
            public void run() {

            	// Check and return any transactions that are pending
            	
            	
            	Cursor mCursor = mPurchaseDatabase.queryAllPurchasedItems();
            	JSONArray receipts = new JSONArray();
            	if (mCursor.getCount()==0) {            
                    // nothing to check
            		
            		String output = arrayToString2(mCursor.getColumnNames(), " - ");
            		
            		Log.d("MarketBillingService", "[postGetPendingTransactions] no transactions mCursor: "+output );
            		InAppPurchaseManager.callbackPendingTransactions(true, receipts);
                } else {
                	/*
                
                	mCursor.moveToFirst();
                	JSONObject obj = new JSONObject();
                    while(mCursor.isAfterLast()==false){

                    	obj.put("HISTORY_ORDER_ID_COL", mCursor.getString(1));
                    	obj.put("Item Purchased", mCursor.getString(2));
                    	receipts.put(obj);

                    	mCursor.moveToNext(); 

                    }
                    */
                	
                	String output = arrayToString2(mCursor.getColumnNames(), " - ");
                	
                	Log.d("MarketBillingService", "[postGetPendingTransactions] mCursor : "+output );
                	InAppPurchaseManager.callbackPendingTransactions(true, receipts);
                }

            }
        };
        
        
        
        
        /*
		 *	Override url loading on WebViewClient  
		 */
        super.appView.setWebViewClient(new DroidGap.GapViewClient(this) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	
    	    	Log.d("ShellActivity", "[GapViewClient] ****** "+ url);
    	    	
    	    	String[] urlArray;
    	    	String splitter = "://";
    	    	
    	    	// split url buy only 2 incase "://" occurs elsewhere (SHOULD be impossible because you string encoded right!?)
    	    	urlArray = url.split(splitter,2);
    	    	
    	    	if (urlArray[0].equalsIgnoreCase("wizmessageview") ) {
    	    		
    	    		String[] msgData;
    	    		splitter = "\\?";
    	    		
    	    		// split url buy only 2 again to make sure we only spit at the first "?"
    	    		msgData = urlArray[1].split(splitter); 
    	    		
    		    	
    	    		// target View = msgData[0] and message = msgData[1]
    	    		
    	    		// get webview list from View Manager
    	    		JSONObject viewList = WizViewManagerPlugin.getViews();

    	    		if (viewList.has(msgData[0]) ) {
        			
    	    			WebView targetView;
    					try {
    						
    						targetView = (WebView) viewList.get(msgData[0]);
    						
       						
    						
    						// send data to mainView
    						String data2send = msgData[1];
    						data2send = data2send.replace("'", "\\'");
    						Log.d("ShellActivity", "[GapViewClient] targetView ****** is " + msgData[0]+ " -> " + targetView + " with data -> "+data2send );
    						targetView.loadUrl("javascript:(wizMessageReceiver('"+data2send+"'))");

    					} catch (JSONException e) {
    						e.printStackTrace();
    						Log.e("ShellActivity", "[GapViewClient] FAIL to get target view and load URL: "+e);
    					}
    	    			
    	    			
    					
    	    			
    	    		}
    	    		
    	    		// app will override this url == true
    				return true;	
    	    		
    	    		
    	    	}
            	
    	    	// everything else, do not override == false
                return false;
            }
        });
        


        


        
    } // ************ END MAIN ACTIVITY **************
    
    

    
    
    
    
    public static String arrayToString2(String[] a, String separator) {
        StringBuffer result = new StringBuffer();
        if (a.length > 0) {
            result.append(a[0]);
            for (int i=1; i<a.length; i++) {
                result.append(separator);
                result.append(a[i]);
            }
        }
        return result.toString();
    }

  
    
    
    

    
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
    
    
    
    
    /*
     * 	Recieve foreign method calls
     */
	public static int getWidth() {
		
    	return width;
	}
    public static int getHeight() {
		
    	return height;
	}
    

    public static Boolean bootOpenFeint() {
		return of_Handler.post(of_boot);
		
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
	
	
	
	
	
    
	
    
    
    
	/*
	 * 
	 *  In App Purchase Methods...
	 *  
	 */
	public static void sPostMakePurchase(JSONObject product) throws JSONException {
		// posted from plugin
		purchaseItemId = product.getString("productId");
		sPurchaseHandler.post(postPurchase);
	}
	public static void sPostSetupBilling() {
		// setup billing
		sPurchaseHandler.post(postSetup);
	}
	public static void sPostGetPendingTransactions() {
		// get pending transactions
		sPurchaseHandler.post(postGetPendingTransactions);
	}
	
	public void postMakePurchase() {
		ResponseHandler.register(mGamePurchaseObserver);
		mBillingService.requestPurchase(purchaseItemId, null);	
	}
		  
	
	/**
     * If the database has not been initialized, we send a
     * RESTORE_TRANSACTIONS request to Android Market to get the list of purchased items
     * for this user. This happens if the application has just been installed
     * or the user wiped data. We do not want to do this on every startup, rather, we want to do
     * only when the database needs to be initialized.
     */
    private void restoreDatabase() {
        
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean initialized = prefs.getBoolean(DB_INITIALIZED, false);
        if (!initialized) {
            mBillingService.restoreTransactions();
            Toast.makeText(this, R.string.restoring_transactions, Toast.LENGTH_LONG).show();
        }
        
    }
    
	/*
	 * ***************************************************
	 */
	
	
	
	
	
	
	@Override
	protected void onStart() {
	    super.onStart();
	    ResponseHandler.register(mGamePurchaseObserver);
	}
	  

	@Override
	public void onStop()
	{
	   super.onStop();
	   ResponseHandler.unregister(mGamePurchaseObserver);

	   /*
	   // For Flurry
	   FlurryAgent.onEndSession(this);
	   */
	}

	@Override
	  public void onDestroy() {
		
		/*
		// For Google Adview
	    adView.destroy();
	    */
	    
	    super.onDestroy();
	    if (mBillingService != null) {
	    	mBillingService.unbind();
	    }
	    

	}




	
	
	
	
	
	
	
	/**
	 * gamePurchaseObserver
	 * A {@link PurchaseObserver} is used to get callbacks when Android Market sends
     * messages to this application so that we can update the UI.
	 *
	 */
	private class gamePurchaseObserver extends PurchaseObserver {

		public gamePurchaseObserver(Handler handler) {
			super(AndroidShellActivity.this, handler);
			// creator method
		}

		@Override
		public void onBillingSupported(boolean supported) {
			if (supported) {
				Log.d("MarketBillingService", "[onBillingSupported] billing supported!" );
				restoreDatabase();
            } else {
            	Log.e("MarketBillingService", "[onBillingSupported] billing NOT supported!" );
            }

			
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload) {
			
			
			Log.d("MarketBillingServiceState", "[onPurchaseStateChange] itemID - "+itemId+" purchaseState - "+purchaseState.toString() );
			if (purchaseState.toString() == "PURCHASED") {
				// item purchased. Verify with our server remotely

			} else if (purchaseState.toString() == "CANCELED") {
				
			}
			
			
			
		}

		@Override
		public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode) {
			// purchase response comes back to here
			
            Log.d("MarketBillingService", "ITEM >> "+request.mProductId + ": " + responseCode);
            if (responseCode == ResponseCode.RESULT_OK) {
            	// purchase success
            	Log.d("MarketBillingService", "AWESOME !!!! ");
            	Log.d("MarketBillingService", "AWESOME !!!! ");
            	Log.d("MarketBillingService", "AWESOME !!!! ");
            	Log.d("MarketBillingService", "AWESOME !!!! ");
            	
            } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
            	// user cancelled
            	Log.d("MarketBillingService", "CANCELED !!!! ");
            	Log.d("MarketBillingService", "CANCELED !!!! ");
            	Log.d("MarketBillingService", "CANCELED !!!! ");
            } else {
            	// purchase failed
            	Log.d("MarketBillingService", "FAILED !!!! ");
            	Log.d("MarketBillingService", "FAILED !!!! ");
            	Log.d("MarketBillingService", "FAILED !!!! ");
            }

		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode) {
			
			Log.d("MarketBillingService", "[onRestoreTransactionsResponse] responseCode - " +responseCode);

			if (responseCode == ResponseCode.RESULT_OK) {

                // Update the shared preferences so that we don't perform
                // a RestoreTransactions again.
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(DB_INITIALIZED, true);
                edit.commit();
                
            } else {
            	Log.e("MarketBillingService", "RestoreTransactions error: " + responseCode);
            }

			
		}



	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * SplashScreen
	 * Show a splashscreen while loading JavaScript. 
	 * 
	 * @author Daniel Kloosterman 2011 - <buz.i286@gmail.com>
	 * MIT Licensed ;)
	 * 
	 * Modified for SplashScreen Plugin Navigator
	 * @author Ally Ogilvie Wizcorp 2011 wizcorp.jp
	 *
	 */
	public static class AndroidShellSplashAddon {
		
	    private boolean isVisible = true;
	    private ImageView image;
	    private DroidGap gap;
	    
	    
	    public static Handler splashHandler;
		public static Runnable splash_hide;

	    /**
	     * Create the splashscreen
	     * @param gap The PhonegapActivity
	     * @param view The webview (easy way to get the layout)
	     */
	    public AndroidShellSplashAddon(DroidGap gap, View view)
	    {
	    	this.gap = gap;
	        image = new ImageView(gap.getBaseContext());
	        image.setBackgroundResource(R.drawable.splash);
	        // TODO add view above webview for progress bars ect?
	        
	        
	        
	        //gap.addView();
	        gap.addContentView(image, view.getLayoutParams());
	        
	        
	        splashHandler = new Handler();
		    // Create runnables for posting
	        splash_hide = new Runnable() {
		        public void run() {
					Log.d("AndroidShellSplashAddon", "[HIDE SPLASH] ******* ");
					setVisible(false);
		        }
		    };

	    }

	    /**
	     * Show the splashscreen
	     */
	    public void show() {
	    	setVisible(true);
	    }
	    
	    /**
	     * Hide the splashscreen
	     */
	    public static void hide(){
	    	splashHandler.post(splash_hide);

	    }
	    
	    
	    /**
	     * Change the visibility of the splashscreen.
	     * @param boolean aVisible Visible
	     */
	    public void setVisible(boolean aVisible) {
	    	
	    	// Declare
	    	final boolean visible = aVisible;
	    	  	
	    	if  (isVisible != visible){
	    		// Run
	    		gap.runOnUiThread(
		            new Runnable() {
		                public void run() {
		            		isVisible = visible;
		            		image.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
		                }
		            }
		        );
	    	}
	    }
	}








	/*
	 *  Getters
	 */

	public static WebView getAppView() {
		// returns DroidGap.appView
		WebView nWebView = (WebView) that.findViewById(0x64);
		return nWebView;
	}


	public static Activity getActivity() {
		// returns DroidGap context
		return that;
	}


	public static RelativeLayout getRLmainView() {
		// returns the Relative Layout view
		return mainView;
	}
	
	public static int getStatHeight() {
		// returns the StatBar Height
		return statsHeight;
	}
	
	public static int getNavHeight() {
		// returns the Nav bar Height
		return navHeight;
	}













	



}