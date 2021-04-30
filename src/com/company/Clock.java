package com.company;

public class Clock implements Runnable {
    private int currentTime;
    private int closeTime;

    public Clock(int openTime, int closeTime) {
        currentTime = openTime;
        this.closeTime = closeTime;
    }

    public void run(){
        while(currentTime<(closeTime+200)){
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
