package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Visitor implements Runnable {
    Museum museum;
    Ticket ticket;
    int duration;
    public boolean isInside = false;
    private final Lock lock;
    private final Condition hasEntered;


    public Visitor(Museum museum, Ticket ticket, int duration) {
        this.ticket = ticket;
        this.museum = museum;
        this.duration = duration;
        lock = new ReentrantLock();
        hasEntered = lock.newCondition();
    }

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
            museum.chooseGate(this, false);
        } finally {
            lock.unlock();
        }
    }

    public synchronized boolean enterMuseum(Turnstile turnstile) {
        return museum.enterMuseum(this, turnstile);
    }

    public synchronized void exitMuseum(Turnstile turnstile) {
        museum.exitMuseum(this, turnstile);
    }

    public void hasEntered() {
        lock.lock();
        try {
            hasEntered.signal();
        } finally {
            lock.unlock();
        }

    }
}
