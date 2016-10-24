package within30.com.webservices.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import within30.com.utilities.LogUtils;
import within30.com.utilities.StringUtils;
import within30.com.webservices.ServiceMethods;


public abstract class BaseParser extends DefaultHandler
{
	public StringBuilder currentValue;
	public boolean currentElement = false;
	public Context context;
	
	protected BaseParser(Context context)
	{
		this.context = context;

	}
	
	public BaseParser(InputStream is)
	{
	}
	public abstract String getErrorMessage();
	public abstract int getMessageCode();
	public abstract Object getData();
	public abstract void parse(String jsonString, ServiceMethods wsMethod);
	public static BaseParser getParser(ServiceMethods wsMethod,Context context) {
		switch (wsMethod) 
		{
			case WS_MAP_INFO:
			case WS_SERVICES:
			case WS_CUSTOMERS:
			case WS_BOOKSLOT:
			case WS_GETCITIES:
			case WS_GETENDUSER:
			case WS_SAVEENDYUSER:
			case WS_UPDATEENDUSER:
			case WS_SUBMITRATING:
			case WS_GETCUSTOMERINFO:
			return new CommonParser(context);
		
		}
		return null;
	}
	
	
	
	/**
	 * Method to convert StringBuffer to String.
	 * @param sb
	 * @return String
	 */
	public String sb2String(StringBuffer sb)
	{
		if(sb == null)
			return "";
		try
		{
			return sb.toString();
		}
		catch(Exception e)
		{
	   		LogUtils.errorLog(this.getClass().getName(), "sb2String exception:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Method to convert StringBuffer to Long.
	 * @param sb
	 * @return long
	 */
	public long sb2Long(StringBuffer sb)
	{
		if(sb == null)
			return 0;
		try
		{
			return Long.parseLong(sb.toString());
		}
		catch(Exception e)
		{
	   		LogUtils.errorLog(this.getClass().getName(), "sb2Long exception:"+e.getMessage() );
		}
		return 0;
	}
	
	/**
	 * Method to convert StringBuffer to double.
	 * @param sb
	 * @return double
	 */
	public double sb2Double(StringBuffer sb)
	{
		if(sb == null)
			return 0;
		try
		{
			return Double.parseDouble(sb.toString());
		}
		catch(Exception e)
		{
	   		LogUtils.errorLog(this.getClass().getName(), "sb2Long exception:"+e.getMessage() );
		}
		return 0;
	}
	
	/**
	 * Method to convert StringBuffer to boolean.
	 * @param sb
	 * @return boolean
	 */
	public boolean sb2Boolean(StringBuffer sb)
	{
		boolean result = false;
		
		if(sb == null)
			return result;
		
		if (sb.length() > 0)
		{
			try
			{
				result = sb.toString().equalsIgnoreCase("true");
			}
			catch(Exception e)
			{
		   		LogUtils.errorLog(this.getClass().getName(), "sb2Boolean exception:"+e.getMessage() );
			}
			
		}
		return result;
	}
	
	/**
	 * Method to convert StringBuffer to int.
	 * @param sb
	 * @return int
	 */
	public int sb2Int(StringBuffer sb)
	{
		if (sb==null) 
			return 0;
		
		return string2Int(sb.toString());
	}
	
	/**
	 * Method to convert String to int.
	 * @param string
	 * @return int
	 */
	public int string2Int(String string)
	{
		int result = 0;
		if (string != null && string.length() > 0)
		{
			try
			{
				result = StringUtils.getInt(string);
			}
			catch(Exception e)
			{
		   		LogUtils.errorLog(this.getClass().getName(), "string2Int exception:"+e.getMessage() );
			}
		}
		return result;
	}
	public double string2Double(String string)
	{
		double result = 0;
		if (string != null && string.length() > 0)
		{
			try
			{
				result = StringUtils.getDouble(string);
			}
			catch(Exception e)
			{
		   		LogUtils.errorLog(this.getClass().getName(), "string2Dobule exception:"+e.getMessage() );
			}
		}
		return result;
	}
	
	public String getStringFromInputStream(InputStream inputStream)
	{
		if(inputStream != null)
		{
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();
		}
		else
		{
			return "";
		}
	}
}
