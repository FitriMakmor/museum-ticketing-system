package com.company;

import java.util.Random;

public class Visitor implements Runnable {
    Museum museum;
    Random r = new Random();
    Ticket ticket;
    int duration;
    //Turnstile enterTurnstile;
    //Turnstile exitTurnstile;

    public Visitor(Museum museum, Ticket ticket, int duration) {
        this.ticket = ticket;
        this.museum = museum;
        this.duration = duration;
    }

    @Override
    public void run() {
        museum.chooseGate(this, true);
        try {
            Thread.sleep(duration * 100);
        } catch (InterruptedException e) {
            System.out.println("Visitor " + ticket.ticketID + " has been forced to exit.");
        }
        museum.chooseGate(this, false);
    }

    public void enterMuseum(Turnstile turnstile) {
        museum.enterMuseum(this, turnstile);
    }

    public void exitMuseum(Turnstile turnstile) {
        museum.exitMuseum(this, turnstile);
    }

    public void insideMuseum(){

    }
}
