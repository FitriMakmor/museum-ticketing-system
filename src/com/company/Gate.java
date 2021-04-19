package com.company;

public class Gate {
    private boolean isEntrance;
    private String name;
    private Turnstile[] turnstiles = new Turnstile[4];


    public Gate(boolean isEntrance, String name){
        this.isEntrance = isEntrance;
        this.name = name;
        for(int i= 0;i<turnstiles.length;i++){
            turnstiles[i]= new Turnstile(this, isEntrance, i);
        }
    }

    public void decideTurnstile(Visitor visitor){
        int min = 1000;
        Turnstile chosenTurnstile = null;
        for(Turnstile turnstile: turnstiles){
            int queueLength = turnstile.getQueueLength();
            if(queueLength<min){
                min=queueLength;
                chosenTurnstile = turnstile;
            }
        }
        chosenTurnstile.addToQueue(visitor);
    }

    public String getName() {
        return name;
    }

    public void openGates(){
        for(Turnstile turnstile: turnstiles){
            Thread turnThread = new Thread(turnstile);
            turnThread.start();
        }
    }

}