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

    /**
     * Constructor
     *
     * @param startTime      Clock's start time
     * @param closeTime      Museum's close time
     * @param maxVisitors    Maximum number of visitors for the day
     * @param visitorsAtOnce Maximum number of visitors at one time
     */
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

    /**
     * Method for visitors to purchase tickets in bulk (1-4).
     * Approach : method will perform synchronized operation of buying tickets.
     *
     * @param ticketAmount Number of tickets being bought at one time.
     * @return void
     */
    public synchronized void buyTicket(int ticketAmount) {
        if (remainingTickets >= ticketAmount) {
            int purchaseTime = clock.getCurrentTime();
            for (int i = 0; i < ticketAmount; i++) {
                String ticketID = "T" + String.format("%04d", runningNumber++);
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

    /**
     * Method to compare and select which visitor should enter from competing turnstiles (possibility of 8 competitors in total).
     * Approach : method will perform synchronized operation of comparison between different visitor's purchaseTimeStamp.
     *
     * @param visitor   the visitor at the front-most queue of the represented turnstile.
     * @param turnstile the turnstile which corresponds to the visitor being passed in.
     * @return void
     */
    public synchronized void compareTime(Visitor visitor, Turnstile turnstile) {
        if (visitor.ticket.getPurchaseTimeStamp() < earliestTime) {
            earliestTime = visitor.ticket.getPurchaseTimeStamp();
        }
        try {
            wait(10);
        } catch (InterruptedException ex) {
        }
        notifyAll();
        if (earliestTime == visitor.ticket.getPurchaseTimeStamp()) {
            enterMuseum(visitor, turnstile);
            earliestTime = Integer.MAX_VALUE;
        }
    }

    /**
     * Method to allow the winning visitor in compareTime() to enter the museum.
     * Approach : method will perform synchronized operation to allow the visitor to enter the museum without exceeding max size.
     *
     * @param visitor   the winning visitor acquired from compareTime().
     * @param turnstile the turnstile which corresponds to the winning visitor.
     * @return void
     */
    public synchronized void enterMuseum(Visitor visitor, Turnstile turnstile) {
        if (visitorController.size() < visitorsAtOnce) {
            visitorController.add(visitor);
            visitor.isInside = true;
            visitor.hasEntered();
            System.out.printf("%04d Ticket %s purchased at %04d entered the museum through T" + (turnstile.getTurnstileNum() + 1) + " " + turnstile.getGateName() + ", staying for " + visitor.getDuration() + " minutes. Current Visitors inside the Museum (enter): %d\n", clock.getCurrentTime(), visitor.getTicketID(), visitor.ticket.getPurchaseTimeStamp(), visitorController.size());
            turnstile.getWaitingVisitors().poll();
        }
    }

    /**
     * Method to allow the visitor to exit the museum.
     * Approach : method will remove the visitor from both the controller and the thread map.
     *
     * @param visitor   the visitor exiting the museum.
     * @param turnstile the turnstile used by the visitor to exit the museum.
     * @return void
     */
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

    /**
     * Method to force the visitors in the museum to exit after closing time.
     * Approach : method will interrupt all sleeping visitor threads in the museum.
     *
     * @return void
     */
    public void announceExit() {
        System.out.println(" Announcing Exit!");
        List<Thread> list = new ArrayList<Thread>(visitorThreadMap.values());
        for (Thread vThread : list) {
            vThread.interrupt();
        }
        isClosed = true;
    }

    /**
     * Method to randomly decide which gate the visitor will use to enter/exit the museum.
     * Approach : method will remove the visitor's ticket from the ticketController and then select a gate for the visitor.
     *
     * @return void
     */
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

    /**
     * Method to open all gates when the museum is opened
     */
    public void openGates() {
        southEntranceGate.openGates();
        northEntranceGate.openGates();
        eastExitGate.openGates();
        westExitGate.openGates();
    }

    /**
     * Method to clear the museum and close all gates
     */
    public void setMuseumIsClear() {
        southEntranceGate.setMuseumClear();
        northEntranceGate.setMuseumClear();
        eastExitGate.setMuseumClear();
        westExitGate.setMuseumClear();
    }

    /**
     * Getter
     *
     * @return Museum's "Closed" status
     */
    public boolean getIsClosed() {
        return isClosed;
    }

    /**
     * Getter method to get the remaining museum tickets
     *
     * @return remaining ticket
     */
    public int getRemainingTickets() {
        return remainingTickets;
    }

    /**
     * Getter to get the clock's current time
     *
     * @return current time
     */
    public int getCurrentTime() {
        return clock.getCurrentTime();
    }

    /**
     * Getter to get the maximum number of visitors at one time
     *
     * @return max number of visitors that can enter the museum
     */
    public int getVisitorsAtOnce() {
        return visitorsAtOnce;
    }

    /**
     * Getter to get the list of visitors currently in the museum
     *
     * @return visitor controller list
     */
    public List<Visitor> getVisitorController() {
        return visitorController;
    }


}
