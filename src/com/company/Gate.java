package com.company;

public class Gate {
    private String name;
    private Turnstile[] turnstiles = new Turnstile[4];
    private Museum museum;


    public Gate(Museum museum, boolean isEntrance, String name) {
        this.name = name;
        for (int i = 0; i < turnstiles.length; i++) {
            turnstiles[i] = new Turnstile(this, isEntrance, i);
        }
        this.museum = museum;
    }

    public void decideTurnstile(Visitor visitor) {
        int min = 1000;
        Turnstile chosenTurnstile = null;
        for (Turnstile turnstile : turnstiles) {
            int queueLength = turnstile.getQueueLength();
            if (queueLength < min) {
                min = queueLength;
                chosenTurnstile = turnstile;
            }
        }
        chosenTurnstile.addToQueue(visitor);
    }

    public String getName() {
        return name;
    }

    public void openGates() {
        for (Turnstile turnstile : turnstiles) {
            Thread turnThread = new Thread(turnstile);
            turnThread.start();
        }
    }

    public void setMuseumClear() {
        for (Turnstile turnstile : turnstiles) {
            turnstile.setMuseumClear(true);
        }
    }

    public synchronized int getMuseumVisitorSize() {
        return museum.getVisitorController().size();
    }

    public Museum getMuseum() {
        return museum;
    }


}