package com.orztip.sonyflake;

public class IdGeneratorClockBackwardLogic {
	
	public static final long START_TIME = 1409529600;
	
	/**
	 * 起始值（10ms）
	 * 2014-09-01 00:00:00 +0000 UTC的unix值
	 */
	private long startTimestampIn10ms = START_TIME;
	
	private volatile long currentTimeBitSlot = 0;
	
	private boolean waitForNextTimeBitSlotIfUnusual = true;
	
	public synchronized long doClockBackwardLogic() throws InterruptedException, RuntimeException {
		
		long time = this.generateTimeBitSlot();
		
		if(time > this.currentTimeBitSlot) {
			return this.nextIdForClockForward(time);
		}
		
		if(time == this.currentTimeBitSlot) {
			return this.nextIdForClockRemain(this.currentTimeBitSlot);
		}

		if(!this.getWaitForNextTimeBitSlotIfUnusual()) {
			throw new RuntimeException("CAN_NOT_GENERATE_NEXT_ID_BY_CLOCK_BACKWARD");
		}
		
		return this.nextIdForWaitToNextTime();
		
	}
	

	private long nextIdForClockForward(long time) {
		
		this.currentTimeBitSlot = time;
		return this.currentTimeBitSlot;
		
	}
	
	
	private long nextIdForClockRemain(long time) throws InterruptedException {
		return this.currentTimeBitSlot;
	}
	
	private long generateTimeBitSlot() {
		
		long time =  System.currentTimeMillis() / 10L - this.startTimestampIn10ms;
		
		if(time > 549755813887L) {
			throw new RuntimeException("TIME_BIT_SLOT_NUMBER_REACH_MAX");
		}
		
		return time;
	}
	
	private long nextIdForWaitToNextTime() throws InterruptedException {
		long time = this.waitToNextGenerateTimeBitSlot();
		return this.nextIdForClockForward(time);
	}
	
	private synchronized long waitToNextGenerateTimeBitSlot() throws InterruptedException {
		
		while(true) {
			
			this.wait(10);
			long currentTimeBitSlot = this.generateTimeBitSlot();
			
			if(currentTimeBitSlot > this.currentTimeBitSlot) {
				return currentTimeBitSlot;
			}
			
		}
		
	}
	
	
	public long getCurrentTimeBitSlot() {
		return this.currentTimeBitSlot;
	}
	
	public void setCurrentTimeBitSlot(long time) {
		this.currentTimeBitSlot = time;
	}

	public boolean getWaitForNextTimeBitSlotIfUnusual() {
		return this.waitForNextTimeBitSlotIfUnusual;
	}
	
	public void setWaitForNextTimeBitSlotIfUnusual(boolean val) {
		this.waitForNextTimeBitSlotIfUnusual = val;
	}
	
}
