package com.company;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Turnstile implements Runnable {
    private boolean isEntrance;
    private Visitor currentVisitor;
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
        System.out.println("Queue size in turnstile " + turnstileNum + " of Gate " + gate.getName() + " is currently at: " + waitingVisitors.size());
        try {
            while (waitingVisitors.isEmpty()) {
                try {
                    System.out.println("Queue in turnstile " + turnstileNum + " of Gate " + gate.getName() + " is empty: " + waitingVisitors.size());
                    hasQueue.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Queue size in turnstile " + turnstileNum + " of Gate " + gate.getName() + " is currently at: " + waitingVisitors.size());
            Visitor currentVisitor = waitingVisitors.poll();
            try {
                int duration = r.nextInt(3001) + 1000;
//                Thread.sleep(duration);
                if (isEntrance) {
                    currentVisitor.enterMuseum(this);
                } else {
                    currentVisitor.exitMuseum(this);
                }
            } catch (Exception e) {
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
            System.out.println("Visitor " + visitor.ticket.ticketID + " added to turnstile " + turnstileNum + " of Gate " + gate.getName() + ". Current Queue Size: " + waitingVisitors.size());
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


}