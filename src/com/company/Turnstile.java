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

    public Turnstile(Gate gate, boolean isEntrance, int num) {
        this.gate = gate;
        lock = new ReentrantLock();
        hasQueue = lock.newCondition();
        this.isEntrance = isEntrance;
        this.turnstileNum = num;
    }

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
                        boolean isSuccessful = currentVisitor.enterMuseum(this);
                        if (isSuccessful) {
                            waitingVisitors.poll();
                        }
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

    public int getQueueLength() {
        return waitingVisitors.size();
    }

    public void addToQueue(Visitor visitor) {
        lock.lock();
        try {
            waitingVisitors.add(visitor);
        } finally {
            hasQueue.signal();
            lock.unlock();
        }
    }

    public int getTurnstileNum() {
        return turnstileNum;
    }

    public String getGateName() {
        return gate.getName();
    }

    public void setMuseumClear(boolean isMuseumClear) {
        this.isMuseumClear = isMuseumClear;
        lock.lock();
        try {
            hasQueue.signalAll();
        } finally {
            lock.unlock();
        }
    }


}