package com.icfcc.example.Util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadUtil {

	private static ExecutorService es;

	static {
		es = Executors.newFixedThreadPool(3);
	}

	private ThreadUtil() {
	};

	public static Future<String> run(Callable<String> callable) {
		Future<String> future = es.submit(callable);
		return future;
	}

	public static void run(Runnable runnable) {
		es.execute(runnable);
	}

	public static void shutdown() {
		if (es != null) {
			es.shutdownNow();
		}
	}
}
