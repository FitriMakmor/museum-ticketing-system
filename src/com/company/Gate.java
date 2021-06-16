package com.company;

import com.company.controllers.MonitorController;

import java.util.Random;

public class Gate {
    private String name;
    private Turnstile[] turnstiles = new Turnstile[4];
    private Museum museum;
    private MonitorController mc;
    private int gateIndex;
    private int turnstileCounter = 0;
    private Random r = new Random();


    /**
     * Constructor
     *
     * @param museum     To pass the museum instance for Turnstile
     * @param isEntrance To determine whether the gate is either entrance or exit
     * @param name       To assign a name to the gate
     */
    public Gate(MonitorController mc, int gateIndex, Museum museum, boolean isEntrance, String name) {
        this.turnstileCounter = 0;
        this.mc = mc;
        this.gateIndex = gateIndex;
        this.name = name;
        for (int i = 0; i < turnstiles.length; i++) {
            turnstiles[i] = new Turnstile(mc, gateIndex, i, this, isEntrance, i);
        }
        this.museum = museum;
    }

    /**
     * Method for visitor to decide on which turnstile to go through.
     * Approach : the method will get a turnstile with the lowest length of queue.
     *
     * @param visitor Visitor to pass through
     */
    public void decideTurnstile(Visitor visitor) {
        int min = 1000;
        Turnstile chosenTurnstile = turnstiles[turnstileCounter++];
        if (turnstileCounter > 3) {
            turnstileCounter = 0;
        }

        chosenTurnstile.addToQueue(visitor);
    }

    /**
     * Getter method to get gate name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to start all the turnstile threads.
     */
    public void openGates() {
        for (Turnstile turnstile : turnstiles) {
            Thread turnThread = new Thread(turnstile);
            turnThread.start();
        }
    }

    /**
     * This method is used to call setMuseumClear for every turnstile.
     */
    public void setMuseumClear() {
        for (Turnstile turnstile : turnstiles) {
            turnstile.setMuseumClear(true);
        }
    }

    /**
     * This method is to get the current museum visitor size.
     *
     * @return visitor size
     */
    public synchronized int getMuseumVisitorSize() {
        return museum.getVisitorController().size();
    }

    /**
     * Getter method to get museum instance.
     *
     * @return museum instance
     */
    public Museum getMuseum() {
        return museum;
    }

}