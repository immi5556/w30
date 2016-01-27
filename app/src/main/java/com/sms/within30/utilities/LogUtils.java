package com.sms.within30.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;




public class LogUtils
{
	private static boolean isLogEnable = true;
	
	public static void errorLog(String tag, String msg)
	{
		if(isLogEnable)
			Log.e("" + tag, "" + msg);
	}
	
	public static void infoLog(String tag, String msg)
	{
		if(isLogEnable)
			Log.i(tag, msg);
	}

	public static void debug(String tag, String msg) 
	{
//		if(isLogEnable)
			Log.d(tag, msg);
	}
	
	public static void printMessage(String msg)
	{
		if(isLogEnable)
			System.out.println(msg);
	}
	
	
	public static void setLogEnable(boolean isEnable)
	{
		isLogEnable = isEnable;
	}
	/**
	 * This method stores InputStream data into a file at specified location
	 * @param is
	 */
	public static void convertResponseToFile(InputStream is) throws IOException
	{
		 BufferedInputStream bis = new BufferedInputStream(is);
		 FileOutputStream fos = new FileOutputStream("/sdcard/Response.xml");
		 BufferedOutputStream bos = new BufferedOutputStream(fos);
		 
		 byte byt[] = new byte[1024];
		 int noBytes;
		 
		 while((noBytes = bis.read(byt)) != -1)
			 bos.write(byt,0,noBytes);
		 
		 bos.flush();
		 bos.close();
		 fos.close();
		 bis.close();
	 }
	/**
	 * This method stores data in String into a file at specified location
	 * @param is
	 */
	public static void convertRequestToFile(String is) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(new File("/sdcard/Request.xml"));
		fos.write(is.getBytes());
		fos.close();
	 }
	
	/** This method will read data from the inputStream and return as StringBuffer
	 * @param inpStrData
	 */
	public static StringBuffer getDataFromInputStream(InputStream inpStrData)
	{
		if(inpStrData != null)
		{
			try
			{
				BufferedReader brResp = new BufferedReader(new InputStreamReader(inpStrData));
				String stringTemporaryVariable;
				StringBuffer sbResp = new StringBuffer();
				
				//Converts response as a StringBuffer
				while((stringTemporaryVariable = brResp.readLine()) != null)
					sbResp.append(stringTemporaryVariable); 
				
				brResp.close();
				inpStrData.close();
				
				return sbResp;
			}
			catch(Exception e)
			{
				LogUtils.errorLog("LogUtils", "Exception occurred in getDataFromInputStream(): "+e.toString());
			}
		}
		return null;
	}
	public static void writeIntoLog(String str, InputStream is)
			throws IOException {
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fosOrderFile = new FileOutputStream(
					AppConstants.DATABASE_PATH + "OrderResponse.xml");
			BufferedOutputStream bossOrderFile = new BufferedOutputStream(
					fosOrderFile);
			deleteLogFile(Environment.getExternalStorageDirectory().toString()
					+ "/OrderLog.txt");
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory().toString()
							+ "/OrderLog.txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			bos.write(str.getBytes());

			byte byt[] = new byte[1024];
			int noBytes;

			while ((noBytes = bis.read(byt)) != -1) {
				bossOrderFile.write(byt, 0, noBytes);
				bos.write(byt, 0, noBytes);
			}

			bossOrderFile.flush();
			bossOrderFile.close();
			fosOrderFile.close();
			bos.flush();
			bos.close();
			fos.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeIntoLog(String str) throws IOException {
		try {
			deleteLogFile(Environment.getExternalStorageDirectory().toString()
					+ "/OrderLog.txt");
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory().toString()
							+ "/OrderLog.txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(str.getBytes());

			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeIntoLog(String str, String filename)
			throws IOException {
		try {
			deleteLogFile(Environment.getExternalStorageDirectory().toString()
					+ "/" + filename + ".txt");
			FileOutputStream fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().toString()
					+ "/"
					+ filename
					+ ".txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(str.getBytes());

			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeIntoLogForDeliveryAgent(String str)
			throws IOException {
		try {
			deleteLogFile(Environment.getExternalStorageDirectory().toString()
					+ "/DeliveryLog.txt");
			FileOutputStream fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().toString()
					+ "/DeliveryLog.txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(str.getBytes());

			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeErrorLogForDeliveryAgent(String str)
			throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().toString()
					+ "/DeliveryErrorLog.txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(str.getBytes());

			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method stores InputStream data into a file at specified location
	 * 
	 * @param is
	 */
	public static void convertResponseToFile(InputStream is, String method)
			throws IOException {
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fos = new FileOutputStream(Environment
					.getExternalStorageDirectory().toString()
					+ "/"
					+ method
					+ "Response.xml");
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			byte byt[] = new byte[1024];
			int noBytes;

			while ((noBytes = bis.read(byt)) != -1)
				bos.write(byt, 0, noBytes);

			bos.flush();
			bos.close();
			fos.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void writeIntoLog(String type, String methodName,
			String strRequest, String success) {
		try {
			if (type.equalsIgnoreCase("all")) {
				writeIntoLogForDeliveryAgent("\n--------------------------------------------------------");
				writeIntoLogForDeliveryAgent("\n Posting Time: "
						+ new Date().toString());
				writeIntoLogForDeliveryAgent("\n URL: " + ServiceURLs.MAIN_URL);
				writeIntoLogForDeliveryAgent("\n SoapAction: "
						+ ServiceURLs.SOAPAction + methodName);
				writeIntoLogForDeliveryAgent("\n--------------------------------------------------------");
				writeIntoLogForDeliveryAgent("\n Request: " + strRequest);
				writeIntoLogForDeliveryAgent("\n Response: " + success);
			} else {
				writeErrorLogForDeliveryAgent("\n--------------------------------------------------------");
				writeErrorLogForDeliveryAgent("\n Posting Time: "
						+ new Date().toString());
				writeErrorLogForDeliveryAgent("\n URL: " + ServiceURLs.MAIN_URL);
				writeErrorLogForDeliveryAgent("\n SoapAction: "
						+ ServiceURLs.SOAPAction + methodName);
				writeErrorLogForDeliveryAgent("\n--------------------------------------------------------");
				writeErrorLogForDeliveryAgent("\n Request: " + strRequest);
				writeErrorLogForDeliveryAgent("\n Response: " + success);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*//*public static void writeIntoLog(String type, String methodName,
			String strRequest, String success) {
		try {
			if (type.equalsIgnoreCase("all")) {
				writeIntoLogForDeliveryAgent("\n--------------------------------------------------------");
				writeIntoLogForDeliveryAgent("\n Posting Time: "
						+ new Date().toString());
				writeIntoLogForDeliveryAgent("\n URL: " + ServiceURLs.MAIN_URL);
				writeIntoLogForDeliveryAgent("\n SoapAction: "
						+ ServiceURLs.SOAPAction + methodName);
				writeIntoLogForDeliveryAgent("\n--------------------------------------------------------");
				writeIntoLogForDeliveryAgent("\n Request: " + strRequest);
				writeIntoLogForDeliveryAgent("\n Response: " + success);
			} else {
				writeErrorLogForDeliveryAgent("\n--------------------------------------------------------");
				writeErrorLogForDeliveryAgent("\n Posting Time: "
						+ new Date().toString());
				writeErrorLogForDeliveryAgent("\n URL: " + ServiceURLs.MAIN_URL);
				writeErrorLogForDeliveryAgent("\n SoapAction: "
						+ ServiceURLs.SOAPAction + methodName);
				writeErrorLogForDeliveryAgent("\n--------------------------------------------------------");
				writeErrorLogForDeliveryAgent("\n Request: " + strRequest);
				writeErrorLogForDeliveryAgent("\n Response: " + success);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public static void deleteLogFile(String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				long sizeInMB = file.length() / 1048576;
				if (sizeInMB >= 100)
					file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDBInSDCard(Context context)
	{
		try {
			InputStream myInput = new FileInputStream(context.getFilesDir().toString() + "/" + AppConstants.DATABASE_NAME_WITHEXTENSION);

			File directory = new File(Environment.getExternalStorageDirectory()+"/IFFCO_DB");
			if (!directory.exists()) {
				directory.mkdirs();
			}

			String sqlitePath = directory.getPath();
			LogUtils.infoLog("Sqlite saved at : ", sqlitePath);
			OutputStream myOutput = new FileOutputStream( sqlitePath + "/iffco.sqlite");

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void saveRequestInSDCard(Context context, String strRequest, String fileName) throws IOException {
		try {
			File directory = new File(Environment.getExternalStorageDirectory()+"/IFFCO_REQUEST");
			if (!directory.exists()) {
				directory.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(directory.getPath()+File.separator+fileName+".txt", true);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(strRequest.getBytes());

			bos.flush();
			bos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
