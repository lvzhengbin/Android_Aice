package com.ice.android.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用的Adapter [还未完善，试验阶段]
 * @author ice
 * @param <T>
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas; 
	protected final int mItemLayoutId;
	
	public CommonAdapter(Context context, List<T> datas, int itemLayoutId){
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = datas;
		this.mItemLayoutId = itemLayoutId;
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView, parent);  
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}
	
	public abstract void convert(ViewHolder viewHolder, T item);
	
	
	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.getViewHolder(mContext, convertView, parent,
				mItemLayoutId, position);
	}

}
