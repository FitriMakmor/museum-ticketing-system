package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Visitor implements Runnable {
    Museum museum;
    Ticket ticket;
    private int duration;
    public boolean isInside = false;
    private final Lock lock;
    private final Condition hasEntered;

    /**
     * Constructor
     *
     * @param museum   the museum
     * @param ticket   visitor's ticket
     * @param duration visitor's duration of staying
     */
    public Visitor(Museum museum, Ticket ticket, int duration) {
        this.ticket = ticket;
        this.museum = museum;
        this.duration = duration;
        lock = new ReentrantLock();
        hasEntered = lock.newCondition();
    }

    /**
     * Visitor will choose a gate to enter and wait for the signal if the visitor has entered the museum
     * Visitor will stay in the museum according to their duration and proceed with exiting the museum
     */
    @Override
    public void run() {
        lock.lock();
        try {
            museum.chooseGate(this, true);
            if (!museum.getIsClosed()) {
                try {
                    hasEntered.await();
                    Thread.sleep(duration * 100); //1 min = 100 ms
                } catch (InterruptedException e) {

                }
            }
            museum.chooseGate(this, false); //visitor choose gate to exit
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method for the visitor to exit museum after choosing the turnstile
     *
     * @param turnstile to pass through
     */
    public synchronized void exitMuseum(Turnstile turnstile) {
        museum.exitMuseum(this, turnstile);
    }

    /**
     * Method to signal the visitor that the visitor has entered the museum
     */
    public void hasEntered() {
        lock.lock();
        try {
            hasEntered.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Getter to get the visitor's duration of staying
     *
     * @return duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Getter to get the ticket's ID
     *
     * @return ticket ID
     */
    public String getTicketID() {
        return ticket.getTicketID();
    }

    public int getTicketTimeStamp() {
        return ticket.getPurchaseTimeStamp();
    }
}
