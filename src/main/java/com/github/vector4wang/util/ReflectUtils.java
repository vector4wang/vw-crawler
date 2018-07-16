package com.github.vector4wang.util;

import com.github.vector4wang.annotation.CssSelector;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  反射工具类
 *  https://www.cnblogs.com/panie2015/p/5571309.html
 * @author vector
 */
public class ReflectUtils {

	/**
	 * 根据类型将指定参数转换成对应的类型
	 *
	 * @param value
	 *            指定参数
	 * @param field
	 *            指定类型
	 * @return 返回类型转换后的对象
	 */
	public static Object parseValueWithType(String value, Field field) {
		Class<?> type = field.getType();
		Object result = null;
		try { // 根据属性的类型将内容转换成对应的类型
			if (Boolean.TYPE == type) {
				result = Boolean.parseBoolean(value);
			} else if (Byte.TYPE == type) {
				result = Byte.parseByte(value);
			} else if (Short.TYPE == type) {
				result = Short.parseShort(value);
			} else if (Integer.TYPE == type) {
				result = Integer.parseInt(value);
			} else if (Long.TYPE == type) {
				result = Long.parseLong(value);
			} else if (Float.TYPE == type) {
				result = Float.parseFloat(value);
			} else if (Double.TYPE == type) {
				result = Double.parseDouble(value);
			} else if (Date.class.equals(type)) {
				CssSelector annotation = field.getAnnotation(CssSelector.class);
				String dateFormat = annotation.dateFormat();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
				return simpleDateFormat.parse(value);
			} else {
				result = value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}