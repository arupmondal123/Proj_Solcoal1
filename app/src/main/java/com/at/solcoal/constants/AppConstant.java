package com.at.solcoal.constants;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class AppConstant
{
	public static final String URL = "http://drgrep.com/drgrep_web/webservice/";

	//public static final String URL = "http://drgrep-staging-731725861.ap-south-1.elb.amazonaws.com/webservice/";

	//new one
	// static final String URL = "http://ec2-52-66-74-188.ap-south-1.compute.amazonaws.com/drgrep_web/webservice/";

	public static final int PRODUCT_SINGLE_TITLE_CHAR_LIMIT =15; 
	
	public static final int GRIDVIEW_COUNT = 30;
	
	public static final int LIST_POPUP_WIDTH = 250;
	public static final int MENU_WIDTH = 120;
	
	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png"); 	// supported file formats
	
	

	public static final String Camera = "Camera";
	public static final String Gallery1 = "Gallery1";
	public static final String Gallery2 = "Gallery2";
	public static final String WEB = "WEB";

	public static final String PDetails = "PDetails";
	public static final String P_ID = "P_ID";
	
	public static final String PRODUCT = "_product_";
	
	public static final int IMAGE_FILE_SIZE_MIN = 512;  // in KB.

	
	public static final String DETAILS_PAGE_TYPE = "DETAILS_PAGE_TYPE";
	public static final String SHOW_DETAILS_FROM_LANDING_PAGE = "SHOW_DETAILS_FROM_LANDING_PAGE";
	public static final String WITH_Previous_Data = "WITH_Previous_Data";
	
	
	public static final String ADD = "ADD";
	public static final String EDIT = "EDIT";
	public static final String ADD_EDIT = "ADD_EDIT";

	public static final String app_font = "Calibri_Light.ttf";

	public static final LatLng Kolkata = new LatLng(22.5667, 88.3667);

	public static final int SUCCESS_RESULT = 0;
	public static final int FAILURE_RESULT = 1;
	public static final String PACKAGE_NAME = "";
	public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
	public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
	public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
	public static final String LINE_ADDRESS = PACKAGE_NAME + ".LINE_ADDRESS";

	public static final String PRODUCT_IMAGES = "Product_Images";

	/**/
	public static final String MAP_JOB_TYPE = "MAP_JOB_TYPE";
	public static final String CHANGE_USER_LOCATION = "CHANGE_USER_LOCATION";
	public static final String PRODUCT_ADD = "CHANGE_PRODUCT_LOCATION_ADD";
	public static final String PRODUCT_EDIT = "CHANGE_PRODUCT_LOCATION_EDIT";
	/**/

	public static final String START_APP = "START_APP";
	public static final String START_APP_FIRST_TIME = "START_APP_FIRST_TIME";
	public static final String DB_OPERATION_SUCCESS = "1";
	public static final String DB_OPERATION_FAILURE = "0";

	public static final String ADD_USER_ACTION = "user_add";
	public static final String GET_USER_ACTION = "user_get";
	public static final String UPDATE_ACTIVE_USER_ACTION = "user_update_inactive";
	public static final String LOGIN_PREFERENCE = "login_preference";
	public static final String LOGIN_PREFERENCE_EMAIL = "login_preference_email";
	public static final String LOGIN_PREFERENCE_SOURCE = "login_preference_source";
	public static final String LOGIN_PREFERENCE_EXT_ID = "login_preference_ext_id";
	public static final String LOGIN_PREFERENCE_ID = "login_preference_id";
	public static final String LOGIN_PREFERENCE_NAME = "login_preference_name";
	public static final String LOGIN_PREFERENCE_GENDER = "login_preference_gender";
	public static final String USER_HAS_SHOPS = "no";


	public static final String AFTER_LOGIN_ACTION = "after_login_action";
	public static final String AFTER_LOGIN_LIST_PRODUCT = "after_login_list_product";
	public static final String AFTER_LOGIN_LIST_BROWSE = "after_login_list_browse";

	public static final String USER_INFO_ACTION = "user_info_action";
	public static final String USER_INFO_USER = "user_info_user";
	public static final String USER_INFO_OTHER_USER = "user_info_other_user";


	
	
//	/**/
//	public static final String PRODUCT_PAGE_TYPE = "PRODUCT_PAGE_TYPE";
//	public static final String PRODUCT_ADD = "PRODUCT_ADD";
//	public static final String PRODUCT_EDIT = "PRODUCT_EDIT";
//	/**/

	
}
