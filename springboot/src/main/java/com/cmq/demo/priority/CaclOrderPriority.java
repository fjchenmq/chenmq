package com.cmq.demo.priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen.ming.qian on 2021/2/2.
 */
public class CaclOrderPriority {
    List<PriorityConfig> priorityConfigList = new ArrayList();
    Long                 lastDateTime       = 1231235959L;

    public static void main(String[] args) {

    }

    public static void InitPriorityConfig() {
        //创建时间
        PriorityConfig createDate = new PriorityConfig();

        //动作
        PriorityConfig serviceOffer_Add = new PriorityConfig();


        PriorityConfig serviceOffer_Del = new PriorityConfig();

        //客户等级
        PriorityConfig custLevel_1 = new PriorityConfig();


        PriorityConfig custLevel_2 = new PriorityConfig();

        //订单来源
        PriorityConfig source = new PriorityConfig();

    }
}
