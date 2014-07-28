package com.ice.android.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段的注解定义
 * @author ice
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/** 表字段名  */
	public String name() default "";
	
	/** 字段默认值  */
	public String defaultValue() default "";
	
	/*public int defaultIntValue() default 0;
	
	  public boolean defaultBooleanValue() default false; */
	
}
