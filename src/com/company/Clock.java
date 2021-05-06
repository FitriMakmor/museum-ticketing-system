package com.company;

public class Clock implements Runnable {
    private int currentTime;
    private int endTime;

    public Clock(int startTime, int closeTime) {
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
        while (currentTime < endTime) {
            try {
                Thread.sleep(100);
                if (currentTime % 100 < 59) {
                    currentTime++;
                } else {
                    currentTime += 41;
                }
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
