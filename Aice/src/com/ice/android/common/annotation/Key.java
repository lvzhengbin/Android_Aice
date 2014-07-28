package com.ice.android.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表主键的注解定义
 * @author ice
 * @description 不配置的时候默认找类的id或_id字段作为主键
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
	/** 表字段名  */
	public String name() default "_id";
}
