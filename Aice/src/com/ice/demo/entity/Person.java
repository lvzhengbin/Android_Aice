package com.ice.demo.entity;

import com.ice.android.common.annotation.Column;
import com.ice.android.common.annotation.Key;
import com.ice.android.common.annotation.Table;
import com.ice.android.common.annotation.Transient;

@Table(name = "t_person")
public class Person {
    @Column(name = "_id")
    @Key private int id;
    @Column(name = "name")
	private String name;
    @Column(name = "age", defaultValue = "23")   
	private int age;
    @Column(name = "sex", defaultValue = "男")
	private String sex;
    // 缺省Column注解时  会生成表字段名为属性本身
    private boolean isMarryed;
    @Transient    // 注解 该属性 标识为 该实体类字段为非数据库字段
    private boolean flag;
 
    public Person(){}
    
    public Person(String name, int age, String sex, boolean isMarryed,
			boolean flag) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.isMarryed = isMarryed;
		this.flag = flag;
	}

	// ---get/set方法
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public boolean isMarryed() {
		return isMarryed;
	}

	public void setMarryed(boolean isMarryed) {
		this.isMarryed = isMarryed;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	
    
    
	
}
