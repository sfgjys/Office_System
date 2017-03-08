package com.minlu.baselibrary.base;

import android.view.View;

/**
 * 这里的泛型决定的是setData方法在适配器中传递进来的数据的类型
 */
public abstract class BaseHolder<T> {

	private View mView;
	private T mData;
	private int position=-1;

	public BaseHolder() {
		this.mView = initView();
		// 在此处进行绑定，想一想原先使用新View获取控件和使用convertView获取控件
		mView.setTag(this);
	}

	public View getRootView() {
		return mView;
	}

	// 此方法是给每个Holder传递数据的
	public void setData(T mData) {
		// 这个方法的目的是传递加载更多的数据
		this.mData = mData;
		// 这个方法目的是传递一般条目的数据
		setRelfshData(mData,position);
	}

	public T getData() {
		return mData;
	}

	public abstract View initView();

	public abstract void setRelfshData(T mData,int postion);

	public void setPosition(int position) {
		this.position = position;
	}
	
}
