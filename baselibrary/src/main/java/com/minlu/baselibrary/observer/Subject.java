package com.minlu.baselibrary.observer;

/**
 * 顶级    规定了通知被通知者的接口
 * */
public interface Subject {
	/* 增加观察者 */
	public void add(Observers observer);

	/* 删除观察者 */
	public void del(Observers observer);

	/* 通知所有的观察者 */
	public void notifyObservers(int mPosition, int distinguishBeNotified, int cancelOrderBid);

	/* 自身的操作 */
	public void operation(int mPosition, int distinguishBeNotified, int cancelOrderBid);
}
