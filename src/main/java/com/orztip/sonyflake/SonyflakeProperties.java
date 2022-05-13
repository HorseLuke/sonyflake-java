package com.orztip.sonyflake;

public class SonyflakeProperties {
	
	public static final int LENGTH_TIME_BIT_SLOT = 39;
	
    /**
     *-1 ^  (-1 << self::LENGTH_TIMESTAMP_IN_10MS)
     * @var int
     */
	public static final long MAX_NUMBER_TIME_BIT_SLOT = 549755813887L;
	
	public static final int LENGTH_OTHER_BIT_SLOT = 24;

	private int lengthSequenceBit = 8;
	
	private long maxNumberSequenceBit = 255;

	private int lengthMachineIdBit = 16;
	
	private long maxNumberMachineIdBit = 65535;

	/**
	 * 起始值（10ms）
	 * 2014-09-01 00:00:00 +0000 UTC的unix值
	 */
	private long startTimestampIn10ms = 1409529600;
	
	private long machineId = 0;
	
	private boolean waitForNextTimeBitSlotIfUnusual = true;
	
	public SonyflakeProperties() {
		
	}
	
	public SonyflakeProperties(int lengthSequenceBit, int lengthMachineIdBit) {
		this.setLengthBit(lengthSequenceBit, lengthMachineIdBit);
	}
	
	private void setLengthBit(int lengthSequenceBit, int lengthMachineIdBit) {
		
		if(lengthSequenceBit <= 0) {
			throw new IllegalArgumentException("Param lengthSequenceBit should be greater than 0");
		}
		
		if(lengthMachineIdBit <= 0) {
			throw new IllegalArgumentException("Param lengthMachineIdBit should be greater than 0");
		}
		
		if(lengthSequenceBit + lengthMachineIdBit  != LENGTH_OTHER_BIT_SLOT) {
			throw new IllegalArgumentException("Param lengthSequenceBit + lengthMachineIdBit  should be equal to " + LENGTH_OTHER_BIT_SLOT);
		}
		
		this.lengthSequenceBit = lengthSequenceBit;
		this.maxNumberSequenceBit =  -1 ^  (-1 << lengthSequenceBit);
		
		this.lengthMachineIdBit = lengthMachineIdBit;
		this.maxNumberMachineIdBit =  -1 ^  (-1 << lengthMachineIdBit);
		
	}

	public void setMachineId(long machindId) {
		if(machindId < 0) {
			throw new IllegalArgumentException("machine id must greater than or equal to 0");
		}
		
		if(machindId > this.maxNumberMachineIdBit) {
			throw new IllegalArgumentException("machine id must lower than " + this.maxNumberMachineIdBit);
		}
		
		this.machineId = machindId;
	}
	
	
	public long getMachineId() {
		return this.machineId;
	}
	
	
	public void setStartTimestampByMilsec(long milsec) {
		this.startTimestampIn10ms = milsec / 10L;
	}
	
	public void setStartTimestampByUnixTimestamp(long sec) {
		this.startTimestampIn10ms = sec * 100L;
	}
	
	public long getStartTimestampIn10ms() {
		return this.startTimestampIn10ms;
	}
	
	public int[] getBitAllocationConfig() {
		int[] bitAllocation = {
				LENGTH_TIME_BIT_SLOT, 
				this.lengthSequenceBit, 
				this.lengthMachineIdBit
		};
		return bitAllocation;
	}
	
	public long[] getBitAllocationMaxNumber() {
		long[] bitAllocationMaxNumber = {
				MAX_NUMBER_TIME_BIT_SLOT, 
				this.maxNumberSequenceBit, 
				this.maxNumberMachineIdBit
		};
		return bitAllocationMaxNumber;
	}
	
	public boolean getWaitForNextTimeBitSlotIfUnusual() {
		return this.waitForNextTimeBitSlotIfUnusual;
	}
	
	public void setWaitForNextTimeBitSlotIfUnusual(boolean val) {
		this.waitForNextTimeBitSlotIfUnusual = val;
	}
	
}
