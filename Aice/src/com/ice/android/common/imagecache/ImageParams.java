package com.ice.android.common.imagecache;
/**
 * 要显示图片的参数实体
 * @author ice
 *
 */
public class ImageParams {

	/** 图片的url */
	private String url;
	
	/** 图片的更新时间   */
	private String updateTime;

	/** 有参构造函数  */
	public ImageParams(String url, String updateTime) {
		this.url = url;
		this.updateTime = updateTime;
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	
	public String getImageKey(){
		return this.url + this.updateTime;
	}
	
}
