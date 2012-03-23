//
//  WizNaviPlugin.h
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

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <UIKit/UITabBar.h>
#import <UIKit/UIToolbar.h>
#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PGPlugin.h>
#else
#import "PGPlugin.h"
#endif

@interface WizNaviPlugin : PGPlugin <UITabBarDelegate, UIActionSheetDelegate, UIWebViewDelegate> {
	UITabBar* tabBar;
	NSMutableDictionary* tabBarItems;
    BOOL isTabBarDisplayed;
    BOOL isTabBarAlive;
    CGFloat tabBarHeight;
    NSMutableDictionary* tabNames;

	UIToolbar* toolBar;
	UIBarButtonItem* toolBarTitle;
    
	CGRect	originalWebViewBounds;
    
    NSString *createNaviCBid;
    NSString *showNaviCBid;
    NSString *hideNaviCBid;
    NSString *setTabNaviCBid;
    CGFloat statBarHeight;
    CGFloat animDuration;
    
    
    
}

@property (nonatomic, retain) NSString *onTapSuccess;
@property (nonatomic, retain) NSString *createNaviCBid;
@property (nonatomic, retain) NSString *showNaviCBid;
@property (nonatomic, retain) NSString *hideNaviCBid;
@property (nonatomic, retain) NSString *setTabNaviCBid;




/* 
 * Tab Bar methods
 */
- (void)create:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)onTapTab:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)setTab:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)setTabs:(NSArray*)arguments withDict:(NSArray*)options;
- (void)show:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)hide:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)showTabBarItems:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)createTabBarItem:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)notify:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)selectTabBarItem:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)enable:(NSArray*)arguments withDict:(NSDictionary*)options;
- (void)disable:(NSArray*)arguments withDict:(NSDictionary*)options;

/* 
 * ActionSheet
 */
- (void)createActionSheet:(NSArray*)arguments withDict:(NSDictionary*)options;


@end