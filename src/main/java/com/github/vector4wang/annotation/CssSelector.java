package com.github.vector4wang.annotation;

import com.github.vector4wang.util.SelectType;

import java.lang.annotation.*;

/**
 * @author : wangxc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CssSelector {

	/**
	 * css 选择器
	 * @return 表达式
	 */
	String selector() default "";

	/**
	 * 匹配结果，默认为HTML代码
	 * @return 返回的结果：html内容还是txt文本
	 */
	SelectType resultType() default SelectType.TEXT;

	/**
	 * 日期的默认格式
	 * @return 根据指定的格式解析日期
	 */
	String dateFormat() default "yyyy-MM-dd HH:mm:ss";
}
