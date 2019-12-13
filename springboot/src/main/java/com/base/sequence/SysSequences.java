package com.base.sequence;

import java.io.Serializable;

/**
 * .
 * 
 * @version Revision 1.0.0
 * @see:
 * @创建日期:2015-4-9
 * @功能说明:
 * 
 */
public class SysSequences implements Serializable {
/*
 CREATE TABLE `sys_sequences` (
  `SEQUENCE_NAME` varchar(60) NOT NULL,
  `START_BY` bigint(20) unsigned NOT NULL DEFAULT '1',
  `INCREMENT_BY` bigint(10) unsigned NOT NULL DEFAULT '1',
  `LAST_NUMBER` bigint(20) unsigned NOT NULL DEFAULT '10000',
  `JVM_STEP_BY` bigint(10) DEFAULT '1',
  PRIMARY KEY (`SEQUENCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

 */
    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    private String sequenceName;
    private long startBy;
    private int incrementBy;
    private long lastNumber;
    private long jvmStepBy;
    /**
     * @return sequenceName属性
     */
    public String getSequenceName() {
        return sequenceName;
    }
    /**
     * @param sequenceName 设置sequenceName属性
     */
    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }
    /**
     * @return startBy属性
     */
    public long getStartBy() {
        return startBy;
    }
    /**
     * @param startBy 设置startBy属性
     */
    public void setStartBy(long startBy) {
        this.startBy = startBy;
    }
    /**
     * @return incrementBy属性
     */
    public int getIncrementBy() {
        return incrementBy;
    }
    /**
     * @param incrementBy 设置incrementBy属性
     */
    public void setIncrementBy(int incrementBy) {
        this.incrementBy = incrementBy;
    }
    /**
     * @return lastNumber属性
     */
    public long getLastNumber() {
        return lastNumber;
    }
    /**
     * @param lastNumber 设置lastNumber属性
     */
    public void setLastNumber(long lastNumber) {
        this.lastNumber = lastNumber;
    }
    /**
     * @return jvmStepBy属性
     */
    public long getJvmStepBy() {
        return jvmStepBy;
    }
    /**
     * @param jvmStepBy 设置jvmStepBy属性
     */
    public void setJvmStepBy(long jvmStepBy) {
        this.jvmStepBy = jvmStepBy;
    }
    
}
