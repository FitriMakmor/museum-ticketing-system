package com.company;

import java.util.*;

public class Museum {

    private Random r = new Random();
    private int visitorsAtOnce;
    private int runningNumber;

    private Gate southEntranceGate = new Gate(this, true, "SN Gate");
    private Gate northEntranceGate = new Gate(this, true, "NN Gate");
    private Gate eastExitGate = new Gate(this, false, "EX Gate");
    private Gate westExitGate = new Gate(this, false, "WX Gate");

    private Clock clock;
    private Thread tClock;
    private boolean isClosed;
    private int remainingTickets;
    private int earliestTime;
    private List<Ticket> ticketController = Collections.synchronizedList(new ArrayList<>());
    private List<Visitor> visitorController = Collections.synchronizedList(new ArrayList<>());
    private Map<Visitor, Thread> visitorThreadMap = Collections.synchronizedMap(new HashMap<>());

    public Museum(int startTime, int closeTime, int maxVisitors, int visitorsAtOnce) {
        System.out.println("Museum is created");
        this.clock = new Clock(startTime, closeTime);
        this.visitorsAtOnce = visitorsAtOnce;
        this.tClock = new Thread(clock);
        this.remainingTickets = maxVisitors;
        this.runningNumber = 1;
        this.earliestTime = Integer.MAX_VALUE;
        tClock.start();
    }

    public synchronized void buyTicket(int ticketAmount) {
        if (remainingTickets >= ticketAmount) {
            int purchaseTime = clock.getCurrentTime();
            for (int i = 0; i < ticketAmount; i++) {
                String ticketID = "T" + String.format("%04d", runningNumber);
                runningNumber++;
                Ticket ticket = new Ticket(ticketID, purchaseTime);
                Visitor visitor = new Visitor(this, ticket, r.nextInt(101) + 50); //randomize 50-150 mins
                Thread vThread = new Thread(visitor);
                System.out.printf("%04d Ticket %s sold.\n", clock.getCurrentTime(), ticket.getTicketID());
                visitorThreadMap.put(visitor, vThread);
                vThread.start();
                ticketController.add(ticket);
                remainingTickets--;
            }
        } else {
            System.out.println("Out of tickets, purchase rejected!");
        }
        System.out.println("Remaining tickets: " + remainingTickets);
    }

    public synchronized void compareTime(Visitor visitor, Turnstile turnstile){
        if(visitor.ticket.getPurchaseTimeStamp()<earliestTime){
            earliestTime = visitor.ticket.getPurchaseTimeStamp();
        }

        try{
            wait(10);
        }catch(InterruptedException ex){

        }
        notifyAll();
        if(earliestTime == visitor.ticket.getPurchaseTimeStamp()){
            enterMuseum(visitor, turnstile);
            earliestTime = Integer.MAX_VALUE;
        }
    }

    public synchronized void enterMuseum(Visitor visitor, Turnstile turnstile) {

        if (visitorController.size() < visitorsAtOnce) {
            visitorController.add(visitor);
            visitor.isInside = true;
            visitor.hasEntered();
            System.out.printf("%04d Ticket %s purchased at %04d entered the museum through T" + (turnstile.getTurnstileNum() + 1) + " " + turnstile.getGateName()
                            + ", staying for " + visitor.getDuration() + " minutes. Current Visitors inside the Museum (enter): %d\n",
                    clock.getCurrentTime(), visitor.getTicketID(), visitor.ticket.getPurchaseTimeStamp(), visitorController.size());
            turnstile.getWaitingVisitors().poll();
        } else {

        }
    }

    public synchronized void exitMuseum(Visitor visitor, Turnstile turnstile) {
        visitorController.remove(visitor);
        visitorThreadMap.remove(visitor);
        System.out.printf("%04d Ticket %s exited the museum through T" + (turnstile.getTurnstileNum() + 1)
                        + " " + turnstile.getGateName() + ". Current Visitors inside the Museum (exit): %d\n",
                clock.getCurrentTime(), visitor.getTicketID(), visitorController.size());
        if (visitorThreadMap.isEmpty() && visitorController.isEmpty()) {
            tClock.interrupt();
            setMuseumIsClear();
            System.out.println("Museum is clear, remaining number of visitors in the museum: " +
                    visitorController.size());
        }
    }

    public void announceExit() {
        System.out.println(" Announcing Exit!");
        List<Thread> list = new ArrayList<Thread>(visitorThreadMap.values());
        for (Thread vThread : list) {
            vThread.interrupt();
        }
        isClosed = true;
    }

    public void chooseGate(Visitor visitor, boolean isEntrance) {
        ticketController.remove(visitor.ticket);
        boolean chosenGate = r.nextBoolean(); //Randomly decides which gate to choose
        if (isEntrance) {
            if (chosenGate) {
                southEntranceGate.decideTurnstile(visitor);
            } else {
                northEntranceGate.decideTurnstile(visitor);
            }
        } else {
            if (chosenGate) {
                eastExitGate.decideTurnstile(visitor);
            } else {
                westExitGate.decideTurnstile(visitor);
            }
        }
    }

    public void openGates() {
        southEntranceGate.openGates();
        northEntranceGate.openGates();
        eastExitGate.openGates();
        westExitGate.openGates();
    }

    public void setMuseumIsClear() {
        southEntranceGate.setMuseumClear();
        northEntranceGate.setMuseumClear();
        eastExitGate.setMuseumClear();
        westExitGate.setMuseumClear();
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public int getRemainingTickets() {
        return remainingTickets;
    }

    public int getCurrentTime() {
        return clock.getCurrentTime();
    }

    public int getVisitorsAtOnce() {
        return visitorsAtOnce;
    }

    public List<Visitor> getVisitorController() {
        return visitorController;
    }


}
