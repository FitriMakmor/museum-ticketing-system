package com.company;

import com.company.controllers.MonitorController;
import javafx.application.Platform;

public class Clock implements Runnable {
    private int currentTime;
    private int endTime;
    private MonitorController mc;


    public Clock(MonitorController mc, int startTime, int closeTime) {
        this.mc = mc;
        currentTime = startTime;
        endTime = closeTime + 200; /** 2 hours after museum closing time */
    }

    /***
     * Thread continuously runs and increase the time by 1 minute for each loop.
     * 1 minute in clock = 100 ms
     * for minute 00 - 58, the clock adds 1
     * for minute 59, the clock adds 41 to get to the next hour and return to minute 00
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                if (currentTime % 100 < 59) {
                    currentTime++;
                } else {
                    currentTime += 41;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mc.getClockText().setText(String.format("%02d", currentTime/100)+":"+String.format("%02d", currentTime%100));
                    }
                });
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    /**
     * Getter to get the current time
     * @return current time
     */
    public int getCurrentTime() {
        return currentTime;
    }


}
