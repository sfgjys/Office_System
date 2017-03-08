package com.minlu.office_system.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class PagerBaseAdapter<T> extends PagerAdapter {

	public List<T> mList;

	public PagerBaseAdapter(List<T> list) {
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 初始化
	 */
	@Override
	public abstract Object instantiateItem(ViewGroup container, int position);

	/**
	 * 销毁
	 */
	@Override
	public abstract void destroyItem(ViewGroup container, int position,
			Object object);
}
