package com.cmq.demo.event;

import java.util.EventListener;

/**
 *
 * @author Thief
 *
 */
public class StateChangeToOneListener implements EventListener {

    public void handleEvent(MyEvent event) {
        System.out.println("StateChangeToOneListener 监听到消息，当前事件源状态为：" + event.getSourceState());
        System.out.println("...................................");
    }

}
