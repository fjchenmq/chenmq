package com.cmq.demo.priority;

/**
 * Created by chen.ming.qian on 2021/2/2.
 */
public class PriorityConfig {
    /**
     * 因子编码
     */
    private String divisorCode;
    /**
     * 因子名称
     */
    private String divisorName;

    /**
     * 因子分组
     */
    private String divisorGroupCode;

    /**
     * 该因子占总的因子权重  所有的因子权重总和为100%
     */
    private int divisorGroupWeight;

    /**
     * 因子权重 一个分组内的因子权重总和为100% 所有实际每个因子在所有的因子中的比重为：divisorGroupCode*divisorWeight
     * divisorGroupCode*divisorWeight总和为100*100
     */
    private int divisorWeight;

    private String divisorValue;
}
