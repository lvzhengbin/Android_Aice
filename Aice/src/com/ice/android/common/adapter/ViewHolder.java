package com.ice.android.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	/** ViewHolder 所Holder住布局中控件集合 */ 
	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mPosition;  
	
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position){
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
		this.mPosition = position;
	}
	
	
	/**
	 * 获得一个ViewHolder对象
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder getViewHolder(Context context, View convertView,  
            ViewGroup parent, int layoutId, int position){
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}
	
	
	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	
	public View getConvertView(){  
        return mConvertView;
    }
	
	
	/**
	 * 为TextView 设置文字
	 * @param viewId
	 * @param text
	 */
	public void setText(int viewId, String text){
		TextView view = getView(viewId);
		view.setText(text);
	}
	
	
	/**
	 * 为ImageView设置图片
	 * @param viewId
	 * @param drawableId
	 */
	public void setImageResource(int viewId, int resId){
		ImageView view = getView(viewId);
		view.setImageResource(resId);
	}
	
	
	/**
	 * 为ImageView设置图片
	 * @param viewId
	 * @param drawableId
	 */
	public void setImageBitmap(int viewId, Bitmap bm){
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
	}
	
	
	public int getPosition()  
    {  
        return mPosition;  
    }
	
	
	// TODO  后续扩展 完善
}
