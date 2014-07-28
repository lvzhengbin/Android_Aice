package com.ice.demo.entity;


/**
 * 不添加注释 情况测试
 * 测试结果：
 * 1、如果有字段id/_id,在不添加注释 Key的情况下会默认把id/_id字段生成为表的主键字段 _id,若没有id/_id，则 一定要有主键Key注释：即表的主键注释, 不然程序无法生成建表语句
 *   如果id字段为int类型 则生成的表主键字段为自动增长型,其他类型则不会 
 * 2、不注释Table 的情况下 生产的表名为 com_ice_demo_entity_Product  // 建议注释Table
 * 3、字段属性不注释生产的表字段名就为他本身
 * 4、一定要有一个空的构造函数  如：public Product(){}  不然程序会报错
 * @author ice
 *
 */
public class Product {
	private int id;   // 主键id
	private String itemNumber;  // 产品编号
	private String itemName;   //  产品名称
	private double price;      //  价钱
	private String description;   // 产品描述
    private String imageUrl;    //  图片URL
    
    public Product(){}
    
    public Product(String itemNumber, String itemName, double price,String description, String imageUrl) {
		this.itemNumber = itemNumber;
		this.itemName = itemName;
		this.price = price;
		this.description = description;
		this.imageUrl = imageUrl;
	}

	/** get/set */
   public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getItemNumber() {
		return itemNumber;
	}


	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
