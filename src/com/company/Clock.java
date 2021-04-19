package com.company;

public class Clock implements Runnable {
    private int currentTime;
    private int closeTime;

    public Clock(int openTime, int closeTime) {
        currentTime = openTime;
        this.closeTime = closeTime;
    }

    public void run(){
        //System.out.printf("Time: %02d:%02d\n", currentTime/100, currentTime%100);
        //for (int i = 0; i < 660; i++){
        while(currentTime<(closeTime+200)){
            try {
                Thread.sleep(100);
                if(currentTime%100 < 59){ //if 0800-0858, just tambah 1
                    currentTime++;
                }else{ // 0859 tambah 41 = 0900
                    currentTime+=41;
                }
            } catch (InterruptedException ex) {
                break;
            }
            //System.out.printf("Time: %02d:%02d\n", currentTime/100, currentTime%100);
        }
        System.out.println("Clock finished");
    }

    public int getCurrentTime(){
        return currentTime;
    }


}
