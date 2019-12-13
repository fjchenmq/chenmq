package com.cmq.demo.event;

import java.util.EventObject;

/**
 *
 * @author Thief
 *
 */
public class MyEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int sourceState;

    public MyEvent(Object source) {
        super(source);
        sourceState = ((MySource)source).getFlag();
    }

    public int getSourceState() {
        return sourceState;
    }

}
