package com.onemenu.server.util;
/**
 * 设备枚举
 * @author file
 * @since 2015年5月31日 下午12:45:03
 * @version 1.0
 * @copyright  by onemenu
 */
public enum Equipment {
	Android(1),
	IOS(2),
	Web(3),
	WinPhone(4),;
	
	private byte value;
	
	private Equipment(int value){
		this.value = (byte) value;
	}
	
	public byte getCode(){
		return value;
	}
	
	public static Equipment getEquipmentByCode(int code){
		for(Equipment e :values()){
			if(e.value == code)
				return e;
		}
		return IOS;
	}
	
	public static Equipment getEquipmentByCode(String code){
		if(StringUtil.isNumber(code)){
			byte _code = Byte.valueOf(code);
			return getEquipmentByCode(_code);
		}
		return IOS;
	}
	
}
