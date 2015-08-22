package com.kz.redminesweeper;

public interface ActivityErrorReceiver {

    void onReceivedError(int msgId, Throwable e);

}
