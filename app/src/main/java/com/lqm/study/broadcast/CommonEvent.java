package com.lqm.study.broadcast;

/**
 * user：lqm
 * desc：普通广播
 */

public class CommonEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommonEvent(String message) {
        this.message = message;
    }
}
