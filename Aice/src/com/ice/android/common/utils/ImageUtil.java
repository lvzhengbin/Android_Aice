package com.ice.android.common.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片资源工具类
 * @author ice
 *
 */
public class ImageUtil {

	/**
	 * drawable对象转Bitmap对象
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable){
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), 
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	
	/**
	 * 图片的缩放
	 * @param bitmap  待缩放的原图
	 * @param newWidth  定义预转换成的图片的宽度
	 * @param newHeight  定义预转换成的图片的高度
	 * @return  缩放后的图
	 */
	public static BitmapDrawable createBitmap(Bitmap bitmap,int newWidth,int newHeight){
		// 获得原图的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
		// 创建操作图片用的matrix对象
	    Matrix matrix = new Matrix();
	    // 缩放图片动作
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 创建新的图片
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(resizedBitmap);
	}
	
	
	/**
	 * 将方形图片剪切成圆图
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);   // 防止边缘的锯齿
		paint.setFilterBitmap(true);  // 对位图进行滤波处理
		
		canvas.drawOval(rectF, paint);  // 根据rectF 绘制一个椭圆/圆形
		// 设置两张图片相交时的显示模式为 SRC_IN
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);
		
		return output;
	}
	
}
