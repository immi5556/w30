package within30.com.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	
	public static String getCurrentTime(){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");	
		Date today = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.clear();	   
	    cal.setTime(today);
	    cal.set(Calendar.HOUR_OF_DAY,today.getHours());
	    cal.set(Calendar.MINUTE,0);
	    cal.set(Calendar.SECOND,0);
	    cal.set(Calendar.MILLISECOND,0);       
	    String formattedDate = dateFormat.format(new Date(cal.getTimeInMillis())).toString();	   
		return formattedDate;
	}	
	  public static ArrayList<String> getSlots(String startTime,String endTime,boolean currentDay){
		
		String date1 = CalendarUtils.getDateAsString(new Date());
		String date2 = CalendarUtils.getDateAsString(new Date());
		
		String format = "yyyy-MM-dd hh:mm a";
		ArrayList<String> slots = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = null;
		Date dateObj2 = null;
		
		try {
			dateObj1 = sdf.parse(date1 + " " + startTime);				
			dateObj2 = sdf.parse(date2 + " " + endTime);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long dif = dateObj1.getTime();
		while (dif < dateObj2.getTime()) {
			try{				
			  
			   Date slot = new Date(dif);				  
			   DateFormat formatter = new SimpleDateFormat("HH:mm a");
			   String dateFormatted = formatter.format(slot);
			   SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
	           SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
	           Date _24HourDt = _24HourSDF.parse(dateFormatted);
	           String final_slot = _12HourSDF.format(_24HourDt);
	           slots.add(final_slot);
	           dif += 3600000;
			}catch(Exception e){  
				e.printStackTrace();
			}
		}
		ArrayList<String> mapSlots = new ArrayList<String>();
		try{
		
		String currentTime = getCurrentTime();
		SimpleDateFormat parser = new SimpleDateFormat("hh:mm a");
		Date currenttime_date = parser.parse(currentTime);
		
        for(int i =0;i<slots.size();i++){
        	
       	 	int temp = i+1;
	       	 if (temp <slots.size() ){
	       		String str = slots.get(i)+" "+slots.get(temp);
	       		Date parlourtime_date = parser.parse(slots.get(i));
	       		if(currentDay){
	       			if(parlourtime_date.after(currenttime_date)){
		       			mapSlots.add(str); 
		       		}
	       		}else{
	       			mapSlots.add(str); 
	       		}
	       		
	       	 }
        }	
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapSlots;
	}
}
