package com.minlu.baselibrary.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
	private static ThreadManager mThreadManager;
	private ThreadPoolExecutor threadPoolExecutor;
	
	//cup核心数*2+1 = corePoolSize
	private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 +1;
	private static int maximumPoolSize = corePoolSize + 5;
	private static long keepAliveTime = 60;
	
	//单例
	
	private ThreadManager(int corePoolSize, int maximumPoolSize, long keepAliveTime){
		if(threadPoolExecutor == null){
			threadPoolExecutor = new ThreadPoolExecutor(
					//核心线程数，可以一直存活的线程数 ，3
					corePoolSize, 
					//最大线程数,包含corePoolSize，5
					maximumPoolSize,
					//空闲线程的存活时间
					keepAliveTime, 
					//存活时间的单位
					TimeUnit.SECONDS,
					//工作队列，
					new LinkedBlockingQueue<Runnable>(),
					//线程工厂
					Executors.defaultThreadFactory(),
					//异常处理方式,拒绝执行
					new ThreadPoolExecutor.AbortPolicy());
		}
	}
	
	//懒汉
	public static ThreadManager getInstance(){
		if(mThreadManager == null){ //线程1，线程2
			synchronized (ThreadManager.class) {
				if(mThreadManager == null){
					mThreadManager = new ThreadManager(corePoolSize,maximumPoolSize,keepAliveTime);
				}
			}
		}
		
		//影响性能
//		synchronized (ThreadManager.class) {
//			if(mThreadManager == null){
//				mThreadManager = new ThreadManager();
//			}
//		}
		
		
		return mThreadManager;
	}

	/**
	 * 执行任务
	 * @param task
	 */
	public void execute(Runnable task){
		if(task == null) return;
		if(threadPoolExecutor != null && !threadPoolExecutor.isShutdown()){
			threadPoolExecutor.execute(task);
		}
	}
	
	/**
	 * 取消任务
	 * @param task
	 */
	public void cancel(Runnable task){
		if(task == null) return;
		if(threadPoolExecutor != null && !threadPoolExecutor.isShutdown()){
			threadPoolExecutor.getQueue().remove(task);
		}
	}
}
