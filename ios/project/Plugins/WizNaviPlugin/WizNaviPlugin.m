//
//  WizNaviPlugin.m
//  
//
//  Created by Jesse MacFadyen on 10-02-03.
//  MIT Licensed

//  Originally this code was developed my Michael Nachbaur
//  Formerly -> PhoneGap :: UIControls.h
//  Created by Michael Nachbaur on 13/04/09.
//  Copyright 2009 Decaf Ninja Software. All rights reserved.

//  << Modded by Wizards >>
//  @modder WizCorp Inc. [ Incorporated Wizards ] 
//  @file WizNaviPlugin.m for use with PhoneGap & WizNavi




#import "WizNaviPlugin.h"
#import <QuartzCore/QuartzCore.h>
#import "WizDebugLog.h"

@implementation WizNaviPlugin

@synthesize createNaviCBid;
@synthesize showNaviCBid;
@synthesize hideNaviCBid;
@synthesize setTabNaviCBid; 
@synthesize onTapSuccess;

-(PGPlugin*) initWithWebView:(UIWebView*)theWebView
{
    self = (WizNaviPlugin*)[super initWithWebView:theWebView];
    if (self) 
	{
        // 5 is strict for now..
        tabNames = [[NSMutableDictionary alloc] initWithCapacity:5];
        tabBarItems = [[NSMutableDictionary alloc] initWithCapacity:5];
		originalWebViewBounds = theWebView.bounds;
        
        //first time create
        isTabBarAlive = FALSE;
    }
    return self;
}

- (void)dealloc
{	
    if (tabBar)
        [tabBar release];
    
	if (toolBar)
	{
		[toolBarTitle release];
		[tabNames release];
		[toolBar release];
	}
    
    [super dealloc];
}

#pragma mark -
#pragma mark TabBar


/**
 * Enable the entire bar.
 */
- (void)enable:(NSArray*)arguments withDict:(NSDictionary*)options
{
    
    WizLog(@"[WizNaviPlugin] ******* enable nav bar!! ");
    
    NSString *callbackId = [arguments objectAtIndex:0];
    PluginResult* pluginResult;
    
    if (!tabBar){
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"call create first"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
		return;
	}
    if (tabBar.alpha == 1.0) {
        
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
		return;
    } else {
        
        tabBar.alpha = 1.0;
        tabBar.userInteractionEnabled = true;
        
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
        
    }
    
    
}


/**
 * Disable the entire bar.
 */
- (void)disable:(NSArray*)arguments withDict:(NSDictionary*)options
{
    WizLog(@"[WizNaviPlugin] ******* disable nav bar!! ");
    
    NSString *callbackId = [arguments objectAtIndex:0];
    PluginResult* pluginResult;
    
    if (!tabBar){
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"call create first"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
		return;
	}
    
    if (tabBar.alpha == 0.4){
        
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
		return;
        
	} else {
        tabBar.alpha = 0.4;
        tabBar.userInteractionEnabled = false;
        
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
    }
    
    
    
}


/**
 * Create a native tab bar at either the top or the bottom of the display.
 * @brief creates a tab bar
 */
- (void)create:(NSArray*)arguments withDict:(NSDictionary*)options
{
    NSString *callbackId = [arguments objectAtIndex:0];
    if(isTabBarAlive)
    {
        PluginResult * pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
        return;
    }
    
    isTabBarAlive = TRUE;
    
    WizLog(@"[WizNaviPlugin] ******* callbackId %@",callbackId);
    
    self.createNaviCBid = callbackId;
    
    tabBar = [UITabBar new];
    [tabBar sizeToFit];
    tabBar.delegate = self;
    tabBar.multipleTouchEnabled   = NO;
    tabBar.autoresizesSubviews    = YES;
    tabBar.hidden                 = YES;
    tabBar.userInteractionEnabled = YES;
	tabBar.opaque                 = YES;
    
    // [tabBar recolorItemsWithColor:[UIColor purpleColor]];
    
    // [[[self tabBarController] tabBar] recolorItemsWithColor:[UIColor whiteColor] shadowColor:[UIColor blackColor] shadowOffset:CGSizeMake(0.0f, -1.0f) shadowBlur:3.0f];
    
    
    isTabBarDisplayed = NO;
    
	self.webView.superview.autoresizesSubviews = YES;
    self.webView.backgroundColor = [UIColor blackColor];
    
    tabBar.backgroundColor = [UIColor blackColor];
	[ self.webView.superview addSubview:tabBar];  
    
    if (options) 
	{
        
        // using with statBar plugin?
        statBarHeight       = [[options objectForKey:@"statBarHeight"] floatValue];
        if (!statBarHeight) {
            // default values
            statBarHeight       = 0.0f;
        }
        
        animDuration        = [[options objectForKey:@"duration"] floatValue];
        if (!animDuration) {
            // default values
            animDuration        = 0.7;
        }
        
        
        tabBarHeight   = [[options objectForKey:@"height"] floatValue];
        if(!tabBarHeight)
        {
            tabBarHeight        = 50.0f;
        }
        
        
        if([[options objectForKey:@"tabs"] isKindOfClass:[NSArray class]]){
            
            
            NSMutableArray * tabs = [options objectForKey:@"tabs"];
            
            int i, count = [tabs count];
            
            // Store Tab names
            NSMutableArray *allTabs = [[NSMutableArray alloc] initWithCapacity:count]; 
            
            for (i=0; i<count; i++) {
                
                NSDictionary * tab = [tabs objectAtIndex:i];
                
                int j = i+1;
                
                NSString  *name         = [tab objectForKey:@"name"];
                if (!name) {
                    name = [NSString stringWithFormat:@"tab%d", j];
                }
                //WizLog(@"[createTabBar] ******* name %@", name);
                
                
                NSString  *tag          = [NSString stringWithFormat:@"%d", j];
                //WizLog(@"[createTabBar] ******* tag %@", tag);
                
                
                NSString  *badge        = [tab objectForKey:@"badge"];
                //WizLog(@"[createTabBar] ******* badge %@", badge);
                
                
                NSString  *label        = [tab objectForKey:@"text"];
                //WizLog(@"[createTabBar] ******* label %@", label);
                
                
                NSString  *sound        = [tab objectForKey:@"sound"];
                if (!sound) {
                    sound = @"none";
                }
                //WizLog(@"[createTabBar] ******* sound %@", sound);
                
                
                NSString  *image        = [tab objectForKey:@"image"];
                //WizLog(@"[createTabBar] ******* image %@", image);
                
                
                NSString  *pressed      = [tab objectForKey:@"pressed"];
                if (!pressed) {
                    pressed = image;
                }
                //WizLog(@"[createTabBar] ******* pressed %@", pressed);
                
                
                NSString  *focus        = [tab objectForKey:@"focus"];
                if (!focus) {
                    focus = image;
                }
                //WizLog(@"[createTabBar] ******* focus %@", focus);
                
                
                // 0, 6, textme, function(), http://sound.mp3, btn1, btn1_over, btn2_focus, 0
                // NSArray *tabArray = [NSArray arrayWithObjects:tag, badge, label, sound, image, pressed, focus, nil];
                NSArray *tabArray = [NSArray arrayWithObjects:name, badge, label, sound, image, pressed, focus, tag, nil];
                [ self createTabBarItem:tabArray withDict:NULL ];
                
                [allTabs insertObject:name atIndex:i];
                [tabNames setObject:name forKey:tag];
            }
            
            [ self showTabBarItems: allTabs withDict:NULL ];
            [allTabs release]; 
            
        } else {
            // no tabs return error
            PluginResult * pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
            [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
            return;
        }
        
        
    } else {
        // totally no options
        // error return error
        PluginResult * pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
        return;
    }
    
    
}


/* 
 *  Set the callback for on tab select events 
 *
 */
- (void)onTapTab:(NSArray*)arguments withDict:(NSDictionary*)options
{
    NSString *callbackId = [arguments objectAtIndex:0];
    self.onTapSuccess = callbackId;
}



/*
 *  Set new settings on multiple tabs
 *
 */
- (void)setTabs:(NSArray*)arguments withDict:(NSArray*)options
{
    WizLog(@"[WizNaviPlugin] ******* setTabs ");
    PluginResult* pluginResult;
    
    NSString *callbackId    = [arguments objectAtIndex:0];
    
    if(options) {
        NSString *name;
        NSString *theTabtag;
        UITabBarItem *item;
        
        // now for each tab to be modified...
        for (int i = 0; i<[options count]; i++) {
            NSArray * tabSettings = [options objectAtIndex:i];
            // check for the 2 components we need; tab and the settings, if not there fail
            if ([tabSettings count]<2) {
                WizLog(@"[WizNaviPlugin setTabs] Not enough params to complete");
                pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
                [self writeJavascript: [pluginResult toSuccessCallbackString:self.setTabNaviCBid]];
                return;
            } 
                
            // the tag or name (ie the button number)
            name = [tabSettings objectAtIndex:0];
            name = [NSString stringWithFormat:@"%@", name];
            
            
            // TODO: proper type check
            if([name length] > 1) {
                // must be a name, get tag for this name
                item  = [tabBarItems objectForKey:name];
                
            } else {
                // name is the number of tab
                name = [tabNames objectForKey:name];
                item  = [tabBarItems objectForKey:name];
                
            }
                
            // now check the settings to apply to the tab
            NSDictionary *settings = [tabSettings objectAtIndex:1];
            
            // If text set a new label
            NSString  *label        = [settings objectForKey:@"text"];
            if (label){
                item.title = label;
            }
            
            // If active set the tab to highlighted
            bool  active        = [[settings objectForKey:@"active"]boolValue];
            if (active==1){
                [tabBar setSelectedItem:item];
            } else if(active==0){
                [tabBar setSelectedItem:NULL];
            }
            
            /*
            // assign a badge
            NSString  *badge        = [[settings objectForKey:@"badge"] stringValue];
            if (badge) {
                
                item.badgeValue = badge;
                
            }
            */
            
            // assign images
            NSString  *imageName        = [settings objectForKey:@"image"];
            if (imageName) {
                
                NSString * getImage = [imageName stringByAppendingString:@".png"];
                [item setImage:[UIImage imageNamed:getImage]];
                
            }
            // TODO: assign sound
            
            // DO THIS LAST
            // set a new name
            NSString  *newName        = [settings objectForKey:@"name"];
            if (newName){
                
                // update tabBarItems
                [tabBarItems removeObjectForKey:name];
                [tabBarItems setObject:item forKey:newName];
                
                theTabtag = [NSString stringWithFormat:@"%i", item.tag];
                [tabNames removeObjectForKey:theTabtag];
                [tabNames setObject:newName forKey:theTabtag];
            }
                
                
        } // end for
              
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
    } else {
        WizLog(@"[WizNaviPlugin setTabs] Missing params");
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
    }
    
    [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
    
}


/*
 *  Set new settings on one particular tab
 *
 */
- (void)setTab:(NSArray*)arguments withDict:(NSDictionary*)options
{
    WizLog(@"[WizNaviPlugin] ******* setTab ");
    
    
    PluginResult* pluginResult;
    NSString *name;
    NSString *theTabtag;
    NSString *callbackId    = [arguments objectAtIndex:0];
    self.setTabNaviCBid = callbackId;
    
    
    
    UITabBarItem *item;
    
    
    int count = [arguments count];
	if(count > 1) {
        
        // the tag or name (ie the button number)
        name = [arguments objectAtIndex:1];
        name = [NSString stringWithFormat:@"%@", name];
        
        
        // TODO: proper type check
        if([name length] > 1) {
            // must be a name, get tag for this name
            item  = [tabBarItems objectForKey:name];
            
        } else {
            // name is the number of tab
            name = [tabNames objectForKey:name];
            item  = [tabBarItems objectForKey:name];
            
        }
        
    } else {
        // missing the tag/name param return error
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:self.setTabNaviCBid]];
        return;
    }
    
    
    
    
    if (options) 
	{
        
        // If text set a new label
        NSString  *label        = [options objectForKey:@"text"];
        if (label){
            item.title = label;
        }
        
        // If active set the tab to highlighted
        bool  active        = [[options objectForKey:@"active"]boolValue];
        if (active==1){
            [tabBar setSelectedItem:item];
            
        } else if(active==0){
            [tabBar setSelectedItem:NULL];
        }
        
        // assign images
        NSString  *imageName        = [options objectForKey:@"image"];
        if (imageName) {
            
            NSString * getImage = [imageName stringByAppendingString:@".png"];
            [item setImage:[UIImage imageNamed:getImage]];
          
            
        }
        // TODO: assign sound
        
        // DO THIS LAST
        // set a new name
        NSString  *newName        = [options objectForKey:@"name"];
        if (newName){
            
            // update tabBarItems
            [tabBarItems removeObjectForKey:name];
            [tabBarItems setObject:item forKey:newName];
            
            theTabtag = [NSString stringWithFormat:@"%i", item.tag];
            [tabNames removeObjectForKey:theTabtag];
            [tabNames setObject:newName forKey:theTabtag];
        
            // WizLog(@"[NativeControlsPlugin] ******* tabBarItems, %@", tabBarItems);
            
        }
        
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        
    } else {
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParam"];
    }
    
    [self writeJavascript: [pluginResult toSuccessCallbackString:self.setTabNaviCBid]];
    
    
    
}

/**
 * Show the tab bar after its been created.
 * @brief show the tab bar
 * @param arguments unused
 * @param options used to indicate options for where and how the tab bar should be placed
 * - \c height integer indicating the height of the tab bar (default: \c 49)
 * - \c position specifies whether the tab bar will be placed at the \c top or \c bottom of the screen (default: \c bottom)
 */
- (void)show:(NSArray*)arguments withDict:(NSDictionary*)options
{
    WizLog(@"[WizNaviPlugin] ******* show navi ");
    
    NSString *callbackId = [arguments objectAtIndex:0];
    self.showNaviCBid = callbackId;
    
    // if no tabBar it has not been created yet..
    if (!tabBar){
        PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"call create first"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:self.hideNaviCBid]];
		return;
	}
    
    // animate is ON by default
    BOOL animate = YES;
    
	// if we are calling this again when its shown, reset
	if (isTabBarDisplayed) {
        PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:self.showNaviCBid]];
		return;
	}
    
    if (options) {
        animate = [[options objectForKey:@"animate"] boolValue];
        if (!animate) {
            animate = YES;
        }
    }
    
    //	CGRect offsetRect = [ [UIApplication sharedApplication] statusBarFrame];
    
    tabBar.hidden = NO;
    isTabBarDisplayed = YES;
    
    CGRect webViewBounds = originalWebViewBounds;
    
	NSNotification* notif = [NSNotification notificationWithName:@"PGLayoutSubviewAdded" object:tabBar];
	[[NSNotificationQueue defaultQueue] enqueueNotification:notif postingStyle: NSPostASAP];
    
    
    
    if(statBarHeight == 0.0f) {
        
        if(animate)
        {
            [tabBar setFrame:CGRectMake(
                                        webViewBounds.origin.x,
                                        webViewBounds.origin.y + webViewBounds.size.height,
                                        webViewBounds.size.width,
                                        tabBarHeight
                                        )];
            
            
            [self.webView setFrame:CGRectMake(
                                              webViewBounds.origin.x,
                                              webViewBounds.origin.y,
                                              webViewBounds.size.width,
                                              webViewBounds.size.height
                                              )];
            
            
            
            [UIView beginAnimations:nil context:NULL];
            [UIView setAnimationDuration:animDuration];
        }
        
        
        [tabBar setFrame:CGRectMake(
                                    webViewBounds.origin.x,
                                    webViewBounds.origin.y + webViewBounds.size.height - tabBarHeight,
                                    webViewBounds.size.width,
                                    tabBarHeight
                                    )];
        
        [self.webView setFrame:CGRectMake(
                                          webViewBounds.origin.x,
                                          webViewBounds.origin.y + statBarHeight,
                                          webViewBounds.size.width,
                                          webViewBounds.size.height - tabBarHeight - statBarHeight
                                          )];
        
        
        
        
        if(animate)
        {
            [UIView commitAnimations];
        }
    } else {
        // we are using statBar so adjust webview for statbar
        WizLog(@"[WizNaviPlugin] ******* we are using statBar.. adjust webview for it. %i", animDuration);
        if(animate)
        {
            [tabBar setFrame:CGRectMake(
                                        webViewBounds.origin.x,
                                        webViewBounds.origin.y + webViewBounds.size.height,
                                        webViewBounds.size.width,
                                        tabBarHeight
                                        )];
            
            
            [self.webView setFrame:CGRectMake(
                                              webViewBounds.origin.x,
                                              webViewBounds.origin.y,
                                              webViewBounds.size.width,
                                              webViewBounds.size.height
                                              )];
            
            
            
            [UIView beginAnimations:nil context:NULL];
            [UIView setAnimationDuration:animDuration];
        }
        
        
        [tabBar setFrame:CGRectMake(
                                    webViewBounds.origin.x,
                                    webViewBounds.origin.y + webViewBounds.size.height - tabBarHeight,
                                    webViewBounds.size.width,
                                    tabBarHeight
                                    )];
        
        [self.webView setFrame:CGRectMake(
                                          webViewBounds.origin.x,
                                          webViewBounds.origin.y + statBarHeight,
                                          webViewBounds.size.width,
                                          webViewBounds.size.height - tabBarHeight - statBarHeight
                                          )];
        
        
        
        
        if(animate)
        {
            [UIView commitAnimations];
        }
    }
    
    
    PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
    [self writeJavascript: [pluginResult toSuccessCallbackString:self.showNaviCBid]];
    
}

/**
 * Hide the tab bar
 * @brief hide the tab bar
 * @param arguments unused
 * @param options unused
 */
- (void)hide:(NSArray*)arguments withDict:(NSDictionary*)options
{
    WizLog(@"[WizNaviPlugin] ******* hide navi ");
    
    NSString *callbackId = [arguments objectAtIndex:0];
    self.hideNaviCBid = callbackId;
    // if no tabBar it has not been created yet..
    if (!tabBar){
        PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"call create first"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:self.hideNaviCBid]];
		return;
	}
    
	// if we are calling this again when its hidden, reset
	if (!isTabBarDisplayed) {
        PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
        [self writeJavascript: [pluginResult toSuccessCallbackString:self.hideNaviCBid]];
		return;
	}
    // animate is ON by default
    BOOL animate = YES;
	// if we are calling this again when its shown, reset
	if (!isTabBarDisplayed) {
		return;
	}
    if (options) {
        animate = [[options objectForKey:@"animate"] boolValue];
        if (!animate) {
            animate = YES;
        }
    }
    
    isTabBarDisplayed = NO;
    
    CGRect webViewBounds = originalWebViewBounds;
    
    if(animate)
    {
        [tabBar setFrame:CGRectMake(
                                    webViewBounds.origin.x,
                                    webViewBounds.origin.y + webViewBounds.size.height - tabBar.frame.size.height,
                                    webViewBounds.size.width,
                                    tabBar.frame.size.height
                                    )];
        
        [self.webView setFrame:CGRectMake(
                                          webViewBounds.origin.x,
                                          webViewBounds.origin.y,
                                          webViewBounds.size.width,
                                          webViewBounds.size.height - tabBar.frame.size.height
                                          )];
        
        
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:animDuration];
    }
    
    [tabBar setFrame:CGRectMake(
                                webViewBounds.origin.x,
                                webViewBounds.origin.y + webViewBounds.size.height,
                                webViewBounds.size.width,
                                tabBar.frame.size.height
                                )];
    
    [self.webView setFrame:CGRectMake(
                                      webViewBounds.origin.x,
                                      webViewBounds.origin.y,
                                      webViewBounds.size.width,
                                      webViewBounds.size.height
                                      )];
    
    
    
    if(animate)
    {
        [UIView commitAnimations];
    }
    
    
	NSNotification* notif = [NSNotification notificationWithName:@"PGLayoutSubviewRemoved" object:tabBar];
	[[NSNotificationQueue defaultQueue] enqueueNotification:notif postingStyle: NSPostASAP];
    
    PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
    [self writeJavascript: [pluginResult toSuccessCallbackString:self.hideNaviCBid]];
    
}





/**
 * Create a new tab bar item for use on a previously created tab bar.  Use ::showTabBarItems to show the new item on the tab bar.
 *
 * If the supplied image name is one of the labels listed below, then this method will construct a tab button
 * using the standard system buttons.  Note that if you use one of the system images, that the \c title you supply will be ignored.
 * - <b>Tab Buttons</b>
 *   - tabButton:More
 *   - tabButton:Favorites
 *   - tabButton:Featured
 *   - tabButton:TopRated
 *   - tabButton:Recents
 *   - tabButton:Contacts
 *   - tabButton:History
 *   - tabButton:Bookmarks
 *   - tabButton:Search
 *   - tabButton:Downloads
 *   - tabButton:MostRecent
 *   - tabButton:MostViewed
 * @brief create a tab bar item
 * @param arguments Parameters used to create the tab bar
 *  -# \c name internal name to refer to this tab by
 *  -# \c title title text to show on the tab, or null if no text should be shown
 *  -# \c image image filename or internal identifier to show, or null if now image should be shown
 *  -# \c tag unique number to be used as an internal reference to this button
 * @param options Options for customizing the individual tab item
 *  - \c badge value to display in the optional circular badge on the item; if nil or unspecified, the badge will be hidden
 */
- (void)createTabBarItem:(NSArray*)arguments withDict:(NSDictionary*)options
{
    
    //[NSArray arrayWithObjects:name, badge, label, onClick, sound, image, pressed, focus, tag, nil];
    
    
    NSString  *name             = [arguments objectAtIndex:0];
    NSString  *badge            = [[arguments objectAtIndex:1] stringValue];
    NSString  *title            = [arguments objectAtIndex:2];
    // NSString  *sound            = [arguments objectAtIndex:3]; ** currently not supported TODO
    NSString  *imageName        = [arguments objectAtIndex:4];
    // NSString  *imagePressed     = [arguments objectAtIndex:5];　** currently not supported TODO
    // NSString  *imageFocus       = [arguments objectAtIndex:6];　** currently not supported TODO
    int tag                     = [[arguments objectAtIndex:7] intValue];
    
    
    
    UITabBarItem *item = nil;    
    if ([imageName length] == 0) {
        //if ([imageName length] > 0) {
        UIBarButtonSystemItem systemItem = -1;
        
        /*
         if ([imageName isEqualToString:@"tabButton:More"])       systemItem = UITabBarSystemItemMore;
         if ([imageName isEqualToString:@"tabButton:Favorites"])  systemItem = UITabBarSystemItemFavorites;
         if ([imageName isEqualToString:@"tabButton:Featured"])   systemItem = UITabBarSystemItemFeatured;
         if ([imageName isEqualToString:@"tabButton:TopRated"])   systemItem = UITabBarSystemItemTopRated;
         if ([imageName isEqualToString:@"tabButton:Recents"])    systemItem = UITabBarSystemItemRecents;
         if ([imageName isEqualToString:@"tabButton:Contacts"])   systemItem = UITabBarSystemItemContacts;
         if ([imageName isEqualToString:@"tabButton:History"])    systemItem = UITabBarSystemItemHistory;
         if ([imageName isEqualToString:@"tabButton:Bookmarks"])  systemItem = UITabBarSystemItemBookmarks;
         if ([imageName isEqualToString:@"tabButton:Search"])     systemItem = UITabBarSystemItemSearch;
         if ([imageName isEqualToString:@"tabButton:Downloads"])  systemItem = UITabBarSystemItemDownloads;
         if ([imageName isEqualToString:@"tabButton:MostRecent"]) systemItem = UITabBarSystemItemMostRecent;
         if ([imageName isEqualToString:@"tabButton:MostViewed"]) systemItem = UITabBarSystemItemMostViewed;
         */
        
        systemItem = UITabBarSystemItemHistory;
        if (systemItem != -1)
            item = [[UITabBarItem alloc] initWithTabBarSystemItem:systemItem tag:tag];
    }
    
    
    if (item == nil) {
        NSString * getImage = [imageName stringByAppendingString:@".png"];
        item = [[UITabBarItem alloc] initWithTitle:title image:[UIImage imageNamed:getImage] tag:tag];
    }
    
    
    
    if ([badge isEqualToString:@"0"]) {
        
    } else {
        if(badge == nil){
            
        } else {
            item.badgeValue = badge ;
        }
    }
    
    
    [tabBarItems setObject:item forKey:name];
	[item release];
    
}


/**
 * Update an existing tab bar item to change its badge value.
 * @brief update the badge value on an existing tab bar item
 * @param arguments Parameters used to identify the tab bar item to update
 *  -# \c name internal name used to represent this item when it was created
 * @param options Options for customizing the individual tab item
 *  - \c badge value to display in the optional circular badge on the item; if nil or unspecified, the badge will be hidden
 */
- (void)notify:(NSArray*)arguments withDict:(NSDictionary*)options
{
    NSString *callbackId = [arguments objectAtIndex:0];
    PluginResult* pluginResult;
    
    if (!tabBar) {
        
        // missing the tag param return error
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"create tabBar before sending data"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
        return;
    }
    
    NSString*   name;
    NSString*   value;
    UITabBarItem* item;
    
    int count = [arguments count];
	if(count > 1) {
        WizLog(@"[WizNaviPlugin] ******* notify callbackId %@", callbackId);
        
        // the tag or name (ie the button number)
        name = [arguments objectAtIndex:1];
        name = [NSString stringWithFormat:@"%@", name];
        
        // value of badge
        value = [arguments objectAtIndex:2];
        
        if([name length] > 1) {
            // no problem it is name, we can carry on and use as it is
            
        } else {
            // using the tag, get the name
            name = [tabNames objectForKey:name];
            
        }
        
    } else {
        // missing the tag param return error
        pluginResult = [PluginResult resultWithStatus:PGCommandStatus_ERROR messageAsString:@"noParams"];
        [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
        return;
    }
    
    
    item = [tabBarItems objectForKey:name];
    
    NSString* zero = @"0";
    if (item) {
        // if 0 hide the badge
        if ([value isEqualToString:zero]) {
            item.badgeValue = nil;
        } else {
            item.badgeValue = value;
        }
        
    }
    
    pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
    [self writeJavascript: [pluginResult toSuccessCallbackString:callbackId]];
    
}


/**
 * Show previously created items on the tab bar
 * @brief show a list of tab bar items
 * @param arguments the item names to be shown
 * @param options dictionary of options, notable options including:
 *  - \c animate indicates that the items should animate onto the tab bar
 * @see createTabBarItem
 * @see createTabBar
 */
- (void)showTabBarItems:(NSArray*)arguments withDict:(NSDictionary*)options
{
    if (!tabBar)
        [self create:nil withDict:nil];
    
    int i, count = [arguments count];
    NSMutableArray *items = [[NSMutableArray alloc] initWithCapacity:count];
    for (i = 0; i < count; i++) {
        NSString *itemName = [arguments objectAtIndex:i];
        UITabBarItem *item = [tabBarItems objectForKey:itemName];
        if (item)
            [items addObject:item];
    }
    
    BOOL animateItems = NO;
    if ([options objectForKey:@"animate"])
        animateItems = [(NSString*)[options objectForKey:@"animate"] boolValue];
    [tabBar setItems:items animated:animateItems];
	[items release];
    
    
    
    // Callback success from booting nav
    PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK];
    [self writeJavascript: [pluginResult toSuccessCallbackString:self.createNaviCBid]];
}

/**
 * Manually select an individual tab bar item, or nil for deselecting a currently selected tab bar item.
 * @brief manually select a tab bar item
 * @param arguments the name of the tab bar item to select
 * @see createTabBarItem
 * @see showTabBarItems
 */
- (void)selectTabBarItem:(NSArray*)arguments withDict:(NSDictionary*)options
{
    if (!tabBar)
        [self create:nil withDict:nil];
    
    NSString *itemName = [arguments objectAtIndex:0];
    UITabBarItem *item = [tabBarItems objectForKey:itemName];
    if (item)
        tabBar.selectedItem = item;
    else
        tabBar.selectedItem = nil;
}


- (void)tabBar:(UITabBar *)theTabBar didSelectItem:(UITabBarItem *)item
{   // Called when tab is selected...
    
    // clear selection
    [theTabBar setSelectedItem:NULL];
    
    // Pull onClick script from onClick array holder and execute
    // int i = item.tag -1;
    // NSString * script = [onClickArray objectAtIndex:i];
    // [self.webView stringByEvaluatingJavaScriptFromString:script];
    
    // search for the tab name using the tab's tag..
    NSString *itemTagString = [NSString stringWithFormat:@"%i", item.tag];
    NSString *tabName = [tabNames objectForKey:itemTagString];
    
    
    
    WizLog(@"Return success callback with button tapped tag: %@", tabName);
    
    PluginResult* pluginResult = [PluginResult resultWithStatus:PGCommandStatus_OK messageAsString:tabName];
    [pluginResult setKeepCallbackAsBool:YES];
    [self writeJavascript: [pluginResult toSuccessCallbackString:self.onTapSuccess]];
    
}


#pragma mark -
#pragma mark ActionSheet

- (void)createActionSheet:(NSArray*)arguments withDict:(NSDictionary*)options
{
    
	NSString* title = [options objectForKey:@"title"];
    
    
	UIActionSheet* actionSheet = [ [UIActionSheet alloc ] 
                                  initWithTitle:title 
                                  delegate:self 
                                  cancelButtonTitle:nil 
                                  destructiveButtonTitle:nil
                                  otherButtonTitles:nil
                                  ];
    
	int count = [arguments count];
	for(int n = 0; n < count; n++)
	{
		[ actionSheet addButtonWithTitle:[arguments objectAtIndex:n]];
	}
    
	if([options objectForKey:@"cancelButtonIndex"])
	{
		actionSheet.cancelButtonIndex = [[options objectForKey:@"cancelButtonIndex"] intValue];
	}
	if([options objectForKey:@"destructiveButtonIndex"])
	{
		actionSheet.destructiveButtonIndex = [[options objectForKey:@"destructiveButtonIndex"] intValue];
	}
    
	actionSheet.actionSheetStyle = UIActionSheetStyleBlackTranslucent;//UIActionSheetStyleBlackOpaque;
    [actionSheet showInView:self.webView.superview];
    [actionSheet release];
    
}



- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
	NSString * jsCallBack = [NSString stringWithFormat:@"window.plugins.nativeControls._onActionSheetDismissed(%d);", buttonIndex];    
    [self.webView stringByEvaluatingJavaScriptFromString:jsCallBack];
}


@end