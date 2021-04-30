package com.company;

public class Ticket {
    int purchaseTimeStamp;
    String ticketID;

    public Ticket(String id, int purchaseTime){
        purchaseTimeStamp = purchaseTime;
        this.ticketID = id;
    }

}
