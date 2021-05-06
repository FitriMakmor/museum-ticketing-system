package com.company;

public class Clock implements Runnable {
    private int currentTime;
    private int endTime;

    public Clock(int startTime, int closeTime) {
        currentTime = startTime;
        endTime = closeTime+200; //2 hours after museum closing time
    }

    public void run(){
        while(currentTime<endTime){
            try {
                Thread.sleep(100);
                if(currentTime%100 < 59){
                    currentTime++;
                }else{
                    currentTime+=41;
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
//        System.out.println("Clock finished");
    }

    public int getCurrentTime(){
        return currentTime;
    }


}
