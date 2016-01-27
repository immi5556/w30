package com.sms.within30.calender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sri Krishna on 9/20/2015.
 */
public class CalendarUtils {

    public final static int DAYS_TO_SHOW = 14;

    private int daysToShow = DAYS_TO_SHOW;

    public void setDaysToShow(int daysToShow)
    {
        this.daysToShow = daysToShow;
    }

    /** Static Inner class */
    public static class CalendarDO
    {
        public int dayInt;
        public String dayString;
    }

//    private Vector<CalendarDO> vecCalendarDOs;

//    public static String getCurrentDay()
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE LLL d");
//        String currentDay = sdf.format(Calendar.getInstance().getTime());
//        return currentDay;
//    }

    public static String getTitle(int dayIncrement)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE LLL d");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, dayIncrement);
        String currentDay = sdf.format(c.getTime());
      
        Date date = new Date();
        System.out.println("current date ->"+sdf.format(date));        
        
        return currentDay;
    }

    public static String getSelectedDateForServer(int dayIncrement)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, dayIncrement);
        String currentDay = sdf.format(c.getTime());
        return currentDay;
    }

    public static ArrayList<CalendarDO> populateCalendarDOs()
    {
        ArrayList<CalendarDO> arrCalendarDOs = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");

        for (int i = 0; i < DAYS_TO_SHOW; i++) {
            CalendarDO calendarDO = new CalendarDO();
            calendarDO.dayInt = cal.get(Calendar.DAY_OF_MONTH);
            calendarDO.dayString = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 1);
            arrCalendarDOs.add(calendarDO);
        }
        return arrCalendarDOs;
    }
}
