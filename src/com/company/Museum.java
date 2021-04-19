package com.company;

import java.util.*;

public class Museum {

    private final int maxVisitors = 900;
    private final int visitorsOneTime = 100;
    private int runningNumber;
    Random r = new Random();
    private Gate southEntranceGate = new Gate(true, "South Entrance Gate");
    private Gate northEntranceGate = new Gate(true, "North Entrance Gate");
    private Gate eastExitGate = new Gate(false, "East Exit Gate");
    private Gate westExitGate = new Gate(false, "West Exit Gate");

    int openTime = 800;
    int closeTime = 1100;

    public Clock clock; // = new Clock(openTime, closeTime);
    private Thread tClock;
    private boolean isOpen;
    private int currentVisitorsInMuseum;
    private int remainingTickets;
    private List<Ticket> ticketController = Collections.synchronizedList(new ArrayList<>());
    private List<Visitor> visitorController = Collections.synchronizedList(new ArrayList<>());
    private Map<Visitor, Thread> visitorThreadMap = Collections.synchronizedMap(new HashMap<>());

    public Museum(Clock clock) {
        System.out.println("Museum is created");
        this.clock = clock;
        tClock = new Thread(clock);
        remainingTickets = maxVisitors;
        runningNumber = 1;
        tClock.start();
    }

    public synchronized void buyTicket(int ticketAmount) {
        System.out.println("Remaining tickets: " + remainingTickets);
        if (remainingTickets >= ticketAmount) {
            int purchaseTime = clock.getCurrentTime();
            // String[] tickets =
            for (int i = 0; i < ticketAmount; i++) {
//                int entryTime;
//                entryTime = ticketController.get(ticketController.size()-1).getEntryTime();
                //String stringRN  = runningNumber;
                String ticketID = "T" + String.format("%04d", runningNumber);
                //String.format("%04d" , runningNumber);
                runningNumber++;
                Ticket ticket = new Ticket(ticketID, purchaseTime /*, entryTime*/);
                Visitor visitor = new Visitor(this, ticket, r.nextInt(101) + 50); //randomize 50-150 mins
                Thread vThread = new Thread(visitor);

                //System.out.println(purchaseTime+ " Ticket " + ticketID+" sold.");
                System.out.printf("%04d Ticket %s sold.\n", clock.getCurrentTime(), ticket.ticketID);

                visitorThreadMap.put(visitor, vThread);
                vThread.start();
                ticketController.add(ticket);
                remainingTickets--;
            }
        } else {
            System.out.println("Out of tickets, purchase rejected!");
        }
    }

    public int getRemainingTickets() {
        return remainingTickets;
    }

    public void enterMuseum(Visitor visitor, Turnstile turnstile) {
        visitorController.add(visitor);
        System.out.printf("%04d Ticket %s entered da museum through " + turnstile.getTurnstileNum()
                        + " " + turnstile.getGateName() + " staying for %d minutes.\n",
                clock.getCurrentTime(), visitor.ticket.ticketID, visitor.duration);
    }

    public void exitMuseum(Visitor visitor, Turnstile turnstile) {
        System.out.printf("%04d Ticket %s exited.\n", clock.getCurrentTime(), visitor.ticket.ticketID);
        visitorController.remove(visitor);
        visitorThreadMap.remove(visitor);
        if (visitorThreadMap.isEmpty()) {
            tClock.interrupt();
        }
        //System.out.printf("%04d Ticket %s exited.\n", clock.getCurrentTime(),ticket.ticketID
    }

    public void announceExit() {
        System.out.println("Announcing Exit!");
        List<Thread> list = new ArrayList<Thread>(visitorThreadMap.values());
        Iterator hmIterator = visitorThreadMap.entrySet().iterator();
        for (Thread vThread : list) {
            vThread.interrupt();
        }
    }

    public void chooseGate(Visitor visitor, boolean isEntrance) {
        ticketController.remove(visitor.ticket);
        int chosenGate = r.nextInt(2);
        if (isEntrance) {
            if (chosenGate == 0) {
                southEntranceGate.decideTurnstile(visitor);
            } else {
                northEntranceGate.decideTurnstile(visitor);
            }
        } else {
            if (chosenGate == 0) {
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


    public int getCurrentTime() {
        return clock.getCurrentTime();
    }


}
