package com.company;

public class Gate {
    private String name;
    private Turnstile[] turnstiles = new Turnstile[4];
    private Museum museum;


    /**
     * Constructor
     * @param museum To pass the museum instance for Turnstile
     * @param isEntrance To determine whether the gate is either entrance or exit
     * @param name To assign a name to the gate
     */
    public Gate(Museum museum, boolean isEntrance, String name) {
        this.name = name;
        for (int i = 0; i < turnstiles.length; i++) {
            turnstiles[i] = new Turnstile(this, isEntrance, i);
        }
        this.museum = museum;
    }

    /**
     * Method for visitor to decide on which turnstile to go through.
     * Approach : the method will get a turnstile with the lowest length of queue.
     * @param visitor Visitor to pass through
     */
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

    /**
     * Getter method to get gate name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to kickstart all the t
     */
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