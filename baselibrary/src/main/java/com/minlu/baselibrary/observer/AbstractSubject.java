package com.minlu.baselibrary.observer;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 二级 具体实现了将被通知者加入或删除集合  以及通过遍历集合去通知各个被通知者
 * */
public abstract class AbstractSubject implements Subject {
	private Vector<Observers> vector = new Vector<Observers>();

	/**
	 * 现在已经存储   mPosition来区分  1为FragmentArea 2为FragmentAutonomously 3为AlarmServicer 
	 * */
	@Override
	public void add(Observers observer) {
		vector.add(observer);
		
	}

	@Override
	public void del(Observers observer) {
		vector.remove(observer);
		
	}

	@Override
	public void notifyObservers(int mPosition, int distinguishBeNotified, int cancelOrderBid) {
		Enumeration<Observers> enumo = vector.elements();
		while (enumo.hasMoreElements()) {
			enumo.nextElement().update(mPosition,distinguishBeNotified, cancelOrderBid);
		}
	}

}
