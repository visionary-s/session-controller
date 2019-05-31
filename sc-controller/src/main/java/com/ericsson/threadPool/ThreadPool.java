package com.ericsson.threadPool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class for getting thread pool.
 * @author shibiyun
 * 
 */
public class ThreadPool {
	
	public static final ThreadPool instance = new ThreadPool();
	
	private ThreadPoolExecutor sessionStart = null;	// Thread pool for session start
	
	private ScheduledExecutorService sessionStop = null;	// Thread pool for session stop
	
	private ThreadPool(){
		//
	}
	
	/**
	 * Get the thread pool performing common task.
	 * @return
	 */
	public ThreadPoolExecutor getThreadPool() {
		synchronized (ThreadPool.class) {
			if (this.sessionStart == null) {
				SynchronousQueue<Runnable> syncQueue = new SynchronousQueue<>();
				this.sessionStart = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, syncQueue);
			}
			return this.sessionStart;
		}
	}
	
	/**
	 * Get the thread pool for scheduled task.
	 * @return
	 */
	public ScheduledExecutorService getScheduledThreadPool() {
		synchronized (ThreadPool.class) {
			if (this.sessionStop == null) {
				this.sessionStop = new ScheduledThreadPoolExecutor(0);
			}
			return this.sessionStop;
		}
	}
	
}
