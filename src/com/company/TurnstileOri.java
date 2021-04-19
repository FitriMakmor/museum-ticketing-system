package com.company;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TurnstileOri {
    private boolean isEntrance;
    private boolean isActive;
    private Visitor currentVisitor;
    private final Lock lock;
    private ConcurrentLinkedQueue<Visitor> waitingVisitors = new ConcurrentLinkedQueue<Visitor>();
    private Random r = new Random();

    public TurnstileOri(boolean isEntrance) {
        lock = new ReentrantLock();

        this.isEntrance = isEntrance;
    }

    public void getStatus() {

    }

    public int getQueueLength(){
        return waitingVisitors.size();
    }

    public void using(Visitor visitor) {
        waitingVisitors.add(visitor);
        lock.lock();
        waitingVisitors.remove(visitor);
        try {
            int duration = r.nextInt(3001) + 1000;
            currentVisitor = visitor;
            Thread.sleep(duration);
            if(isEntrance){
                //visitor.enterMuseum(this);
            }else{
                //visitor.exitMuseum(this);
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }

    }


}