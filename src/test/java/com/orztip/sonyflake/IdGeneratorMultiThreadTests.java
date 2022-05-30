package com.orztip.sonyflake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.orztip.sonyflake.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class IdGeneratorMultiThreadTests {
	
	private class TestCount{
		
		private volatile int count = 0;
		
		public int getCount() {
			return count;
		}
		
		public synchronized void addCount() {
			count++;
		}
		
	}
	
	
	private class IncrRunnable implements Runnable{
		
		int t;
		
		HashSet<Long> data;
		
		IdGenerator sf;
		
		TestCount counter;

		public IncrRunnable(int t, HashSet<Long> data, IdGenerator sf, TestCount counter) {
			this.data = data;
			this.t = t;
			this.sf = sf;
			this.counter = counter;
		}
		
		@Override
		public void run() {
			
			System.out.println("thread started" + t);
			
			for(int i = 1; i <= 2048; i++) {
				try {
					long id = sf.nextId();
					//System.out.println("thread run" + t + " created id " + id);
					if(data.contains(id)) {
						Assertions.fail("duplicated id generated");
					}
					data.add(id);
					//System.out.println("thread run" + t + " data length " + data.size());
				} catch (RuntimeException e) {
					Assertions.fail(e);
				}
			}
			
			this.counter.addCount();

			System.out.println("thread finish ,now exit. thread#" + t);

			
			System.out.println("threadsFinish count:" + this.counter.getCount() );

		}
		
	}


	@Test
	void testMultiThread2() {
		
		
		IdGenerator sf = new IdGenerator();
		HashSet<Long> data = new HashSet<Long>();

		ArrayList<Thread> threads = new ArrayList<Thread>();
		TestCount counter = new TestCount();

		
		int totalThreads = 50;

		for(int t = 0; t < totalThreads; t++) {
			threads.add(new Thread(new IncrRunnable(t, data, sf, counter)));
		}

		for(int t = 0; t < totalThreads; t++) {
			threads.get(t).start();
		}
		
		
		long startTime = System.currentTimeMillis();
		long maxExecuteTime = 30 * 1000L;
		
		while(true) {
			
			int sizeX = counter.getCount();
			
			//String sizeX3 = String.valueOf(sizeX);  //TestCount的count没有volatile修饰时，去掉这句测试不通过
			
			if(sizeX >= totalThreads) {
				break;
			}
			
			if(System.currentTimeMillis() - startTime > maxExecuteTime) {
				Assertions.fail("run timeout, current threads finished count" + sizeX);
			}
			
		}
		
		
		System.out.println("all thread finish count:" + counter.getCount() );
		//System.out.println("all thread finish");
		
	}
	

	
}
