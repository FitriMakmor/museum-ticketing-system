package com.company;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Turnstile implements Runnable {
    private boolean isEntrance;
    private boolean isMuseumClear;
    private final Lock lock;
    private final Condition hasQueue;

    private ConcurrentLinkedQueue<Visitor> waitingVisitors = new ConcurrentLinkedQueue<Visitor>();
    private Random r = new Random();
    private Gate gate;
    private int turnstileNum;

    /**
     * Constructor
     * @param gate       Pass the gate instance
     * @param isEntrance Set the entrance or exit behaviour
     * @param num        set the turnstile number
     */
    public Turnstile(Gate gate, boolean isEntrance, int num) {
        this.gate = gate;
        lock = new ReentrantLock();
        hasQueue = lock.newCondition();
        this.isEntrance = isEntrance;
        this.turnstileNum = num;
    }

    /**
     * Thread for each turnstile that has the main job of clearing visitors queueing at the turnstile
     * and also do the following:-
     * - will wait when the queue(waitingVisitors) is empty
     * - if the turnstile set for entrance gate, the visitor at the front of the queue will be
     * compared first with all other visitors at the front of the queue of all other turnstile.
     * - if the turnstile set for exit gate, it will exit the visitors queueing at the turnstile
     */
    @Override
    public void run() {
        lock.lock();
        try {
            while (true) {
                while (waitingVisitors.isEmpty()) {
                    try {
                        if (isMuseumClear) {
                            break;
                        }
                        hasQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (isMuseumClear) {
                    break;
                }

                try {
                    if (isEntrance && gate.getMuseumVisitorSize() < gate.getMuseum().getVisitorsAtOnce()) {
                        Visitor currentVisitor = waitingVisitors.peek();
                        gate.getMuseum().compareTime(currentVisitor, this);
                    } else if (!isEntrance) {
                        Visitor currentVisitor = waitingVisitors.poll();
                        currentVisitor.exitMuseum(this);
                    }
                } catch (Exception e) {
                }
                if (isMuseumClear) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method to get the queue size at the turnstile
     */
    public int getQueueLength() {
        return waitingVisitors.size();
    }

    /**
     * Method for visitor to decide on which turnstile to go through.
     * Approach : the method will get a turnstile with the lowest length of queue.
     *
     * @param visitor Visitor to pass through
     */
    public void addToQueue(Visitor visitor) {
        lock.lock();
        try {
            waitingVisitors.add(visitor);
        } finally {
            hasQueue.signal();
            lock.unlock();
        }
    }

    /**
     * Getter method for turnstile number
     *
     * @return turnstile number
     */
    public int getTurnstileNum() {
        return turnstileNum;
    }

    /**
     * Getter method to get the gate name
     *
     * @return gate name
     */
    public String getGateName() {
        return gate.getName();
    }

    /**
     * Method to update turnstile state to clearing, hence no more visitors can enter the museum once it is true.
     *
     * @param isMuseumClear
     */
    public void setMuseumClear(boolean isMuseumClear) {
        this.isMuseumClear = isMuseumClear;
        lock.lock();
        try {
            hasQueue.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Getter
     *
     * @return the waiting visitor queue
     */
    public ConcurrentLinkedQueue<Visitor> getWaitingVisitors() {
        return waitingVisitors;
    }


}