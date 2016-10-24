package within30.com.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import within30.com.dataobjects.CustomerDO;

/** this class having some common methods related to the dateofJorney, dateofJorney format,dateofJorney conversion, etc.**/
public class CalendarUtils 
{
	public static final String DATE_STD_PATTERN = "yyyy-MM-dd";

	public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	public static String getDateAsString(Date date)
	{
		String dateStr = null;

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_STD_PATTERN);
		dateStr = sdf.format(date);

		return dateStr;
	}


	/**
	 * Method to get current date to post all the orders. Date format - 2012-01-01
	 * @return String
	 */
	public static String getCurrentPostDateForBookedSlot(long estimatedTime)
	{
		//
		final long ONE_MINUTE_IN_MILLIS = 60000;
		Calendar calendar = Calendar.getInstance();
		long tempTime = calendar.getTimeInMillis();
		Date afterAddedEstTime = new Date(tempTime +(estimatedTime * ONE_MINUTE_IN_MILLIS));

		String dateStr = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateStr = sdf.format(afterAddedEstTime);
		return dateStr;
	}
	
	/**
	 * Method to get current date to post all the orders. Date format - 2012-01-01
	 * @return String
	 */
	public static String getCurrentPostDate(String pattern)
	{
		String dateStr = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * Method to get current date to post all the orders. Date format - 2012-01-01
	 * @return String
	 */
	public static String getCurrentDateTime()
	{
		String dateStr = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		dateStr = sdf.format(date);
		return dateStr;
	}


	public static boolean getComparisionSlotBookedDateAndPresentDate(String slotbookeddateandduration){

		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String currentDateAndtime = sdf.format(new Date());
			if(currentDateAndtime.compareTo(slotbookeddateandduration)>0){
				 return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	public static  String getTimeAddedDefaultDeutation(String time,int defaultDutation,String timeZone){
		String slotBookedTimePlusDefaultDuration = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			Date date = simpleDateFormat.parse(time);
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, defaultDutation);
			slotBookedTimePlusDefaultDuration =  simpleDateFormat.format(calendar.getTime());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return slotBookedTimePlusDefaultDuration;
	}

	public static boolean getComparisionCurrentTimeWithNextslotAt(CustomerDO customerDO,int minutes){

		try{
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(customerDO.getTimeZone()));
			Date currentDate = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String  currentTime = sdf.format(currentDate);
			// add 30 min to the current time
			final long SELECTED_MINUTE_IN_MILLIS = (minutes+10)*60000;
			Date currentDateAddedSelectedMin = new Date(c.getTimeInMillis() + SELECTED_MINUTE_IN_MILLIS);
			String  currentTimeAddedSelectedMin = sdf.format(currentDateAddedSelectedMin);

			if(checkSlotDateIsToday(customerDO.getTimeZone(),customerDO.getNextSlotDate())){
				if(customerDO.getNextSlotAt().compareTo(currentTimeAddedSelectedMin)<=0 ){
					return true;
				}else if (customerDO.getNextSlotAt().compareTo(currentTimeAddedSelectedMin)>0){
					return false;
				}
			}else {
				return false;
			}


		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	public static boolean getComparisionCurrentTimeWithNextslotAt1(CustomerDO customerDO){

		try{
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(customerDO.getTimeZone()));
			Date currentDate = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String  currentTime = sdf.format(currentDate);


			if (checkSlotDateIsToday(customerDO.getTimeZone(),customerDO.getNextSlotDate())) {
				if(customerDO.getNextSlotAt().compareTo(currentTime)<0 ){
					return false;
				}else if (customerDO.getNextSlotAt().compareTo(currentTime)>=0){
					return true;
				}
			}else{
				return true;
			}



		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * This method Checking the booked slot is today or not
	 * @param timeZone
	 * @param soltDateStr
     * @return
     */
	public static boolean checkSlotDateIsToday(String timeZone,String soltDateStr){

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		Date currentDate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String  currentDateStr = sdf.format(currentDate);
		if (soltDateStr.compareTo(currentDateStr)== 0 ) {
			return true;
		}else  {
			return false;
		}

	}
	/**
	 * This method Checking the booked slot is today or not

	 * @param soltDateStr
	 * @return
	 */
	public static String nextSlotDate(String soltDateStr){

	//	Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
	//	Date currentDate = c.getTime();
		String chnagedDateFormat = "";
		try {
			DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date date = originalFormat.parse(soltDateStr);
			SimpleDateFormat tartgetFormat = new SimpleDateFormat("MM/dd");
			chnagedDateFormat = tartgetFormat.format(date);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  chnagedDateFormat;
	}
	public static String nextSlotDate1(String soltDateStr){

		//	Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		//	Date currentDate = c.getTime();
		String chnagedDateFormat = "";
		try {
			DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			Date date = originalFormat.parse(soltDateStr);
			SimpleDateFormat tartgetFormat = new SimpleDateFormat("MM/dd");
			chnagedDateFormat = tartgetFormat.format(date);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  chnagedDateFormat;
	}
}