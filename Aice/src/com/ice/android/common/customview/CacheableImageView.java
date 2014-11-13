package com.ice.android.common.customview;

import com.ice.android.common.imagecache.AsynImageLoader;
import com.ice.android.common.imagecache.ImageParams;
import com.ice.android.common.utils.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
/**
 * 自定义  加载网络图片控件<br/>
 * <b>该控件特征如下：</b><br/>
 * 首先从内存缓存中去获取图片数据，<br/>
 * 若内存缓存中没有图片数据   则默认先展现一张自定义的本地图片资源<br/>
 * 接着才开始异步加载网络上图片资源 并display<br/>
 * 主要参考方法：showImg(...)
 * @author ice
 *
 */
public class CacheableImageView extends ImageView {

	private static final String TAG = "CacheableImageView";
	
	private Bitmap bitmap = null;
	
	public CacheableImageView(Context context) {
		super(context);
	}

	public CacheableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CacheableImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	private class ImgHandler extends Handler{
		private ImageParams mImageParams;
		
		public ImgHandler(ImageParams imageParams) {
			this.mImageParams = imageParams;
		}

		@Override
		public void handleMessage(Message msg) {
			if(getTag()!=null){
				if(getTag().equals(mImageParams)){
					if (msg.obj != null) {
			            bitmap = (Bitmap) msg.obj;
			            if (bitmap != null) {
			              display( bitmap);
			            }
			        }
				}
			}	
		}
		
	}
	
	
	/**
	 * 请求显示图片，在有网络图片需要展示时调用此方法；<br>
     * 调用此方法后，不会立即从网络或者文件中获取图片数据；<br>
     * 会先展示内存缓存中存在的图片数据，<br>
     * 若内存缓存中不存在的图片数据则默认显示一张缺省时drawable <br>
     * 接着asynImageLoader.loadImageDataFromNet方法异步调用进行加载数据。<br>
	 * @param imageParams 图片参数封装对象
	 * @param drawable  缺省时显示
	 */
	public void showImg(ImageParams imageParams,Drawable drawable){
		AsynImageLoader asynImageLoader = AsynImageLoader.getInstance();
		// 先从内存缓存中获取，如果获取到了，直接展示
		bitmap = asynImageLoader.loadCacheImage(imageParams);
		if(bitmap != null){
			Log.d(TAG, "showImg，从内存缓存中获取到图片数据，url = " + imageParams.getUrl());
			display(bitmap);
		}else{
			// 如果没有从内存缓存中获取到 则异步加载网络图片资源
			bitmap = ImageUtil.drawableToBitmap(drawable);  // 显示缺省时图片
			display(bitmap);
			Log.d(TAG, "showImg，从内存缓存中没有获取到图片数据，开始从网络上获取图片 url = " + imageParams.getUrl());
			asynImageLoader.loadImageDataFromNet(imageParams, new ImgHandler(imageParams));
		}	
	}

	
	/**
	 * 设置显示图片
	 * @param bitmap
	 */
	private void display(Bitmap bitmap) {
		setImageBitmap(bitmap);
	}
	
	
	public void recycle(){
		if(bitmap != null){
			bitmap.recycle();
		}
	}
	
	
}
