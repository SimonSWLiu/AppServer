package com.onemenu.server.util;

import java.text.NumberFormat;
import java.util.regex.Pattern;
/**
 * String 工具类
 * @author file
 * @since 2015-1-12 下午5:47:56
 * @version 1.0
 */
public final class StringUtil {
	//数字正则表达式
	private static final  String  IS_NUMBER_EXP ="\\d+(\\.\\d+)?"; 
	/**
	 * 字符串是否为空（null或""）
	 * @param str 字符串
	 * @return 
	 */
	public static boolean isEmpty(final String str){
		return null==str||"".equals(str);
	}
	/**
	 * 字符串是否为空,忽略前后的空格
	 * @param str 字符串
	 * @return
	 */
	public static boolean isEmptyIgnoreBlank(final String str){
		return null==str||"".equals(str.trim());
	}
	/**
	 * 字符串是否可以转为数字
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNumber(final String str){
		if(isEmptyIgnoreBlank(str))
			return false;
		String num = str.trim();
		if(num.startsWith("-")){
			num = num.replace("-", "");
		}
		return Pattern.matches(IS_NUMBER_EXP, num);
	}
	/**
	 * 字符串是否相等，忽略前后空格
	 * @param str1     字符串1
	 * @param str2     字符串2
	 * @return
	 */
	public static boolean isEqualsIgnoreBABlank(final String str1,final String str2){
		if(str1==null&&str2==null)
			return true;
		else if(str1==null)
			return false;
		else if(str2==null)
			return false;
		else
			return str1.trim().equals(str2.trim());
	}
	/**
	 * 字符串是否相等
	 * @param str1     字符串1
	 * @param str2     字符串2
	 * @return
	 */
	public static boolean isEquals(final String str1,final String str2){
		if(str1==null&&str2==null)
			return true;
		else if(str1==null)
			return false;
		else if(str2==null)
			return false;
		else
			return str1.equals(str2);
	}
	
	/**
	 * 获取字符串的长度
	 * @param str 字符串
	 * @return
	 */
	public static int len(final String str){
		if(isEmpty(str))
			return 0;
		return str.length();
	}
	
	/**
	 * 去除字符串前后空格
	 * @param str 字符串
	 * @return
	 */
	public static String trim(final String str){
		if(str==null)
			return null;
		return str.trim();
	}
	/**
	 * 字符串转成int
	 * @param str 
	 * @return
	 */
	public static Integer toInt(final String str){
		if(isNumber(str))
			return Double.valueOf(str).intValue();
		return null;
	}
	/**
	 * 字符串转成long
	 * @param str
	 * @return
	 */
	public static Long toLong(final String str){
		if(isNumber(str))
			return Double.valueOf(str).longValue();
		return null;
	}
	/**
	 * 字符串转成double
	 * @param str
	 * @return
	 */
	public static Double toDouble(final String str){
		if(isNumber(str))
			return Double.valueOf(str);
		return null;
	}
	/**
	 * 字符串转成float
	 * @param str
	 * @return
	 */
	public static Float toFloat(final String str){
		if(isNumber(str))
			return Float.valueOf(str);
		return null;
	}
	/**
	 * 移除String的所有空格
	 * @param str
	 * @return
	 */
	public static String removalSpace(String str){
		if(isEmpty(str))
			return null;
		return str.replaceAll(" ", "");
	}
	
	/**
	 * 比较版本号，是否需要更新
	 * @param curVer 当前版本号
	 * @param newVer 新的版本号
	 * @return 是否需要更新
	 */
	public static boolean compare(String curVer,String newVer){
		//有一个版本号为空，不需要更新
		if(isEmpty(curVer)||isEmpty(newVer)||(curVer.trim().equals(newVer.trim())))
			return false;
			
		String[] curVers = curVer.split("\\.");
		String[] newVers = newVer.split("\\.");
		int curLen = curVers.length;
		int newLen = newVers.length;
		int minLen = curLen>newLen?newLen:curLen;
		for(int i=0;i<minLen;i++){
			String pv1 = curVers[i].trim();
			String pv2 = newVers[i].trim();
			//分解之后不是数字，无需更新
			if(!isNumber(pv1)||!isNumber(pv2))
				return false;
			
			int _pv1 = Integer.valueOf(pv1);
			int _pv2 = Integer.valueOf(pv2);
			//同个号段，如果新>当前，需要更新，<当前，不需要更新，不然比较下一段
			if(_pv2>_pv1)
				return true;
			else if(_pv2<_pv1){
				return false;
			}
		}
		//当前的长度比新的长度少，需要更新
		if(curLen<newLen)
			return true;
		return  false;
	}
	
	private static final NumberFormat nf = NumberFormat.getInstance();
	
	public static String  removeLastZero(Object amount){
		return nf.format(amount);
	}
	
	
	public static void main(String[] args) {
		System.out.println(removalSpace("  1 . 0 0  0 0 0  "));
		System.out.println(removalSpace("  kong fu tea  "));
	}
	
	
	
}
