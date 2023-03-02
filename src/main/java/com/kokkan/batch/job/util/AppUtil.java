package com.kokkan.batch.job.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Util Class
 * �������� toString , ��¥�������� ��¥���ϱ� , ���� �޼��� ����
 *
 * @author teemo
 * @author ben
 *
 */
public class AppUtil {

	public final static int LEFT = 0;
	public final static int RIGHT = 1;


	/**
	 * trace stack�� String���� ��ȯ
	 *
	 * @param Throwable
	 * @return stackTrace String message
	 */
	public static String printStackTraceToString(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));

		return stringWriter.toString();

	}

	/**
	 * ���� �ð� ,��¥ ��ȯ
	 *
	 * @param dateformat
	 * @return ���糯¥ String
	 */
	public static String getDate(String dateformat) {
		return getDate(0, dateformat);
	}

	/**
	 * ���͹���ŭ ���ؼ� ������������ ��¥����
	 *
	 * @param interval
	 * @param dateformat
	 * @return ���ϰ��� �ϴ� ��¥ String
	 */
	public static String getDate(int interval, String dateformat) {
		Date date = new Date();
		SimpleDateFormat simpledate = new SimpleDateFormat(dateformat);
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.DATE, interval);

		return simpledate.format(cal.getTime());

	}

	public static String checkNull(Object obj){
		if(obj == null) return "";
		else return (String)obj;
	}

	public static String checkNullTrim(Object obj){
		if(obj == null) return "";
		else return ((String)obj).trim();
	}


	public static byte[] checkNullBytes(Object obj){
		if(obj == null) return "".getBytes();
		else return (byte[])obj;
	}


	public static byte[] makeLengthBytes(byte[] reciveByte, int headerLength){

		int length = reciveByte.length;
		byte[] bLength = AppUtil.leftPad(String.valueOf(length), headerLength, '0').getBytes();
		byte[] reqBytes = new byte[length + bLength.length];
		System.arraycopy(bLength, 0, reqBytes, 0, bLength.length);
		System.arraycopy(reciveByte, 0, reqBytes, bLength.length, length);

		return reqBytes;
	}


	/**
	 * �ٸ��� ����Ʈ ��ȸ
	 * @return
	 */
	public static String getJulianDay(){
		GregorianCalendar gc = new GregorianCalendar();
		String year = new Integer(gc.get(GregorianCalendar.YEAR)).toString();
		String jdPrefix = year.substring(3);
		int jdSuffixInt = gc.get(GregorianCalendar.DAY_OF_YEAR);
		String jdSuffix = String.format("%03d", jdSuffixInt);
		String jd = jdPrefix + jdSuffix;
		return jd;
	}


	/**
	 * ���� �ð� YYMMDDHHMMSS ��ȸ
	 * @return
	 */
	public static String getDateTime(){
		Calendar rightNow = Calendar.getInstance();
		int YYYY = rightNow.get(Calendar.YEAR);
		int MM = rightNow.get(Calendar.MONTH) + 1;
		int DD = rightNow.get(Calendar.DAY_OF_MONTH);
		int hh = rightNow.get(Calendar.HOUR_OF_DAY);
		int mm = rightNow.get(Calendar.MINUTE);
		int ss = rightNow.get(Calendar.SECOND);
		//int W = rightNow.get(Calendar.DAY_OF_WEEK);
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(YYYY);
		if (MM < 10) strbuf.append('0');
		strbuf.append(MM);

		if (DD < 10) strbuf.append('0');
		strbuf.append(DD);

		if (hh < 10) strbuf.append('0');
		strbuf.append(hh);

		if (mm < 10) strbuf.append('0');
		strbuf.append(mm);

		if (ss < 10) strbuf.append('0');
		strbuf.append(ss);
		//strbuf.append(W);

		return strbuf.toString();
	}


	/**
	 * bytes �е� ó��
	 *
	 * @param bytes
	 * @param align
	 * @param fillSize
	 * @param fillChar
	 * @return
	 */
	public static byte[] bytePadding (byte[] bytes, int align, int fillSize, char fillChar){

		int len = bytes.length;
		int paddingSize = fillSize - len;
		byte[] retBytes = new byte[fillSize];

		//padding ó�� ����
		if(paddingSize < 0) {
			//������ ���̺��� �����Ͱ� ���� ��� ������ ������ �����͸� ����� �� �� ���� (�̰� �� ����� �ʿ�)

			if(AppUtil.LEFT == align) {
				System.arraycopy(bytes, 0, retBytes, 0, retBytes.length);
			}else {
				int sOffSet = len - fillSize;
				System.arraycopy(bytes, sOffSet, retBytes, 0, bytes.length-sOffSet);
			}

			return retBytes;

		}else if(paddingSize == 0) {
			//padding ���ʿ�
			return bytes;
		}else {
			// padding ó��
			byte[] paddingData = new byte[paddingSize];
			Arrays.fill(paddingData, (byte)fillChar);



			if(AppUtil.LEFT == align) {
				//������ ���� ������ ���
//				System.arraycopy(src, srcPos, dest, destPos, length);
				System.arraycopy(bytes, 0, retBytes, 0, bytes.length);
				System.arraycopy(paddingData, 0, retBytes, bytes.length, paddingData.length);

			}else {
				//������ ���� ������ ���
				System.arraycopy(paddingData, 0, retBytes, 0, paddingData.length);
				System.arraycopy(bytes, 0, retBytes, paddingData.length, bytes.length);
			}

			return retBytes;
		}

	}


	/**
	 * ������ ������ ����, ���� fillChar ������ ���̸�ŭ �е�
	 * �����Ͱ� ���̺��� �� Ŭ��� ���̸�ŭ ����
	 * @param data
	 * @param fillSize
	 * @param fillChar
	 * @return
	 */
	public static String rightPad(String data, int fillSize, char fillChar) {
		return formatData(data, AppUtil.LEFT, fillSize, fillChar);
	}

	/**
	 * ������ ���� ����, ���� fillChar ������ ���̸�ŭ �е�.
	 * �����Ͱ� ���̺��� �� Ŭ��� ���̸�ŭ ����
	 * @param data
	 * @param fillSize
	 * @param fillChar
	 * @return
	 */
	public static String leftPad(String data, int fillSize, char fillChar) {
		return formatData(data, AppUtil.RIGHT, fillSize, fillChar);
	}

	public static String formatData(int data, int align, int fillSize, char fillChar) {
		return formatData(Integer.toString(data), align, fillSize, fillChar);
	}


	public static String formatData(String data, int align, int fillSize, char fillChar) {
		if (data == null) {
			data = "";
		}
		byte[] bytes = data.getBytes();
		int len = bytes.length;
		if (len < fillSize) {	// ���ڶ�� ���̸�ŭ ä�� ���ڿ��� �����.
			if (align == LEFT) {
				StringBuffer strbuf = new StringBuffer(data);
				for (int i = len; i < fillSize; i++) {
					strbuf.append(fillChar);
				}
				return strbuf.toString();
			}
			else {
				StringBuffer strbuf = new StringBuffer();
				for (int i = len; i < fillSize; i++) {
					strbuf.append(fillChar);
				}
				strbuf.append(data);
				return strbuf.toString();
			}
		}
		else {
			String tmp = "";
			if(align == LEFT){
				int idx = getUniLength(bytes, fillSize, LEFT);
				tmp = new String(bytes, 0, idx);
				if(idx < fillSize){
					StringBuffer strbuf = new StringBuffer(tmp);
					for(int i=idx; i<fillSize; i++){
						strbuf.append(fillChar);
					}
					return strbuf.toString();
				}else{
					if(tmp.equals("")) tmp = new String(bytes, 0, fillSize-1) + " ";
					return tmp;
				}
			}
			else {
				int idx = getUniLength(bytes, fillSize, RIGHT);
				tmp = new String(bytes, idx, bytes.length-idx);
				if(bytes.length-idx < fillSize){
					StringBuffer strbuf = new StringBuffer();
					for(int i=bytes.length-idx; i<fillSize; i++){
						strbuf.append(fillChar);
					}
					strbuf.append(tmp);
					return strbuf.toString();
				}else{
					if(tmp.equals("")) tmp = new String(bytes, len - fillSize + 1, fillSize) + " ";
					return tmp;
				}
			}
		}
	}

	private static int getUniLength(byte[] bytes, int size, int align){
		int idx = 0;
		if(align == LEFT){
			for(idx=0; idx<bytes.length && idx<size; idx++){
				if(bytes[idx] > 127 || bytes[idx] < 0){
					idx++;
				}
			}
			if(idx > size) idx -= 2;
		}else{
			for(idx=0; idx<bytes.length-size; idx++){
				if(bytes[idx] > 127 || bytes[idx] < 0){
					idx++;
				}
			}
		}

		return idx;
	}


	/**
	 * �ð� base ���� 10�ڸ� �� ����
	 * @return
	 */
	public static String getUniqueId(){
		String uniqueId = "";

		long ms = System.currentTimeMillis();
		long rn = (long)(Math.random() * (double)1000000000);

		String msHex = Long.toHexString(ms).toUpperCase();
		String rnHex = Long.toHexString(rn).toUpperCase();

		uniqueId = rnHex + msHex.substring(msHex.length() - (10 - rnHex.length()), msHex.length());

		return uniqueId;
	}


	/**
	 * ex)
	 * currency : USD, amount 10.01 > 1001
	 * currency : KRW, amount 10.0 > 10
	 *
	 * @param amount
	 * @param currency
	 * @return
	 */
	public static String moneyFormat(double amount, String currency) {

		int minorUnit = 0;
		if("USD".equals(currency)) {
			minorUnit = 2;
		}
		return new BigDecimal(amount).setScale(minorUnit, BigDecimal.ROUND_HALF_UP).toString().replace(".", "");
	}


	/**
	 * bytes �ڸ���
	 * @param bData : ���� ������
	 * @param position : ���� ��ġ
	 * @param length : ���� ��ġ���� �ڸ� ����
	 * @return
	 */
	public static byte[] byteSubtract(byte[] bData, int position, int length) {
		byte[] b = new byte[length];
		System.arraycopy(bData, position, b, 0, length);
		return b;
	}


	/**
	 * str�� �����Ϳ� ���� ������, str
	 * ���� ��� anotherStr���� ġȯ�Ѵ�.
	 *
	 * @param str : ��� ���ڿ�
	 * @param anotherStr : ġȯ ���ڿ�
	 * @return
	 */
	public static String strCompareTo(String str, String anotherStr) {

		if(anotherStr != null && !"".equals(anotherStr.trim()) && !str.trim().equals(anotherStr)) {
			return anotherStr;
		}else {
			return str;
		}

	}

	
	
	

}





