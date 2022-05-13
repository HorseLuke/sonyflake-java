package com.orztip.sonyflake;

/**
 * Sonyflake is a distributed unique ID generator inspired by Twitter's Snowflake.
 * 
 *   - 39 bits for time in units of 10 msec
 *   -  8 bits for a sequence number
 *   - 16 bits for a machine id
 *  
 * 时钟回拨时，根据SonyflakeProperties的waitForNextTimeBitSlotIfUnusual值确定：
 *     - true：等待下一个时机生成id
 *     - false：抛出异常
 * 
 * 
 * @author Horse Luke
 *
 */
public class Sonyflake {
	
	private SonyflakeProperties prop;
		
	private int[] bitAllocationConfig = {0, 0, 0};
	
	private long[] bitAllocationMaxNumber = {0, 0, 0};
	
	/**
	 * 起始值（10ms）
	 * 2014-09-01 00:00:00 +0000 UTC的unix值
	 */
	private long startTimestampIn10ms = 1409529600;
	
	private long machineId = 0;

	private volatile long currentTimeBitSlot = 0;
	
	private volatile long currentSequenceBitSlot = 0;
		
	public Sonyflake() {
		this.prop = new SonyflakeProperties();
		this.initConfigFromProp();
	}
	
	public Sonyflake(SonyflakeProperties prop) {
		this.prop = prop;
		this.initConfigFromProp();
	}
	
	
	private void initConfigFromProp() {
		this.bitAllocationConfig = this.prop.getBitAllocationConfig();
		this.bitAllocationMaxNumber = this.prop.getBitAllocationMaxNumber();
		this.startTimestampIn10ms = this.prop.getStartTimestampIn10ms();
		this.machineId = this.prop.getMachineId();
	}
	
	public synchronized long nextId() throws InterruptedException, RuntimeException {
		
		long time = this.generateTimeBitSlot();
		
		if(time > this.currentTimeBitSlot) {
			return this.nextIdForClockForward(time);
		}
		
		if(time == this.currentTimeBitSlot) {
			return this.nextIdForClockRemain(time);
		}
		
		if(!this.prop.getWaitForNextTimeBitSlotIfUnusual()) {
			throw new RuntimeException("Clock backward, cant not generate next id");
		}
		
		return this.nextIdForWaitToNextTime();
		
	}
	
	
	private long nextIdForClockForward(long time) {
		
		this.currentTimeBitSlot = time;
		this.currentSequenceBitSlot = 0;
		return this.buildId(time, 0, this.machineId);
	}
	
	
	private long nextIdForClockRemain(long time) throws InterruptedException {
		
		long sequenceBitSlot = this.currentSequenceBitSlot + 1;
		if(sequenceBitSlot > this.bitAllocationMaxNumber[1]) {
			if(!this.prop.getWaitForNextTimeBitSlotIfUnusual()) {
				throw new RuntimeException("Sequence full in current time");
			}
			return this.nextIdForWaitToNextTime();
		}
		
		this.currentSequenceBitSlot = sequenceBitSlot;
		return this.buildId(time, sequenceBitSlot, this.machineId);

	}
	
	
	private long nextIdForWaitToNextTime() throws InterruptedException {
		long time = this.waitToNextGenerateTimeBitSlot();
		return this.nextIdForClockForward(time);
	}
	
	
	private long generateTimeBitSlot() {
		
		long time =  System.currentTimeMillis() / 10L - this.startTimestampIn10ms;
		
		if(time > this.bitAllocationMaxNumber[0]) {
			throw new RuntimeException("TIME_BIT_SLOT_NUMBER_REACH_MAX");
		}
		
		return time;
	}
	
	
	private long waitToNextGenerateTimeBitSlot() throws InterruptedException {
		
		while(true) {
			
			Thread.sleep(10);
			long currentTimeBitSlot = this.generateTimeBitSlot();
			
			if(currentTimeBitSlot > this.currentTimeBitSlot) {
				return currentTimeBitSlot;
			}
			
		}
		
	}
	
	
	private long buildId(long timeId, long sequenceId, long machinId) {
		return (timeId << (this.bitAllocationConfig[1] + this.bitAllocationConfig[2]))
				  | (sequenceId << this.bitAllocationConfig[2])
				  | machinId;
	}
	
}
