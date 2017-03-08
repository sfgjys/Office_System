package com.minlu.baselibrary.observer;

/**
 * 所有被通知对象的父类接口
 * */
public interface Observers {
	public void update(int distinguishNotified, int position, int cancelOrderBid);
}
