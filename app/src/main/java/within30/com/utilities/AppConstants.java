package within30.com.utilities;

import android.graphics.Typeface;



public class AppConstants {
	
	public static final boolean TESTING = false;
	public static final boolean DEBUG 	= true;
	
	public static final int CAPTURE_PHOTO 	= 1000;
	public static final String SALON_MENU_FOLDER 	= "SalonMenu";
	public static final String SALON_PHOTO_FOLDER 	= "SalonPhotos";
	public static final String SALON_TEMP_FOLDER 	= "Temp";
	public static final String SALON_ROOT_FOLDER 	= "Salon";
	
	/** Deals through Search Info Service */
	public static final int SALONS_WITH_DEALS	 	= 0;
	public static final int SALONS_WITHOUT_DEALS 	= 1;
	public static final int ALL_SALONS 				= 2;
	
	public static final String GCM_SENDER_ID = "615210637061";
	
	
	
	
	public static final int IMG_SALON_TYPE 	= 4;
	public static final int IMG_MENU_TYPE 	= 5;
	
	public static final String USER_PROFILE_PIC 	= "profile";
	
	
	public static final String DATABASE_NAME_WITHEXTENSION 	= "merchandiser.sqlite";
	public static final String DATABASE_NAME_WITHOUTEXTENSION 	= "merchandiser";
	public static String DATABASE_PATH 	= "";
	public static Typeface SIT_HEADLINE, SIT_SUB_HEADLINE, SIT_TEXT;
	public static String LOGIN_FROM = "";
	public static String UNAME = "", IMG_NAME = "";

	
	//All possible roles of user's
	public static String Device_Display_Width 		= PreferenceUtils.DEVICE_DISPLAY_WIDTH;
	public static String Device_Display_Height 	  	= PreferenceUtils.DEVICE_DISPLAY_HEIGHT;
	public static int DEVICE_DISPLAY_WIDTH_DEFAULT  = 800;
	public static int DEVICE_DISPLAY_HEIGHT_DEFAULT	= 1216;
	public static String currentLat = "17.4387171";
	public static String currentLng = "78.3957388";
	public static float DEVICE_DENSITY 	= 0;
	public static long TIME_FOR_BACKGROUND_TASK = 10*60000;
	public static int DISPLAY_WIDTH 	= 0;
	public static int DISPLAY_HEIGHT	= 0;
	public static final int KEYBOARD_OFFSET = 20;
	public static String FORCE_CHECKIN = "Force Checkin";
	
	
	
	/**
	 * broadcast receiver actions
	 */
	public static String ACTION_LOGOUT					=	"com.winit.merchandiser.ACTION.LOGOUT";
	public static String ACTION_GOTO_SETTINGS_FINISH	=	"com.winit.merchandiser.ACTION_GOTO_SETTINGS_FINISH";
	
	public static final String PASSWORD_PATTERN = 
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	public static final int SHIMMER_PAUSE_DELAY = 100;

	public static  final String Failed= "Failed";
	public static final String OK =  "Ok";
	public static  final String INSERT = "insert";
	public static  final String SUCCESS = "Success";

}
