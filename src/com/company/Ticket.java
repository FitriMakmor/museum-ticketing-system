package com.company;

public class Ticket {
    private int purchaseTimeStamp;
    private String ticketID;



    public Ticket(String id, int purchaseTime){
        purchaseTimeStamp = purchaseTime;
        this.ticketID = id;
    }

    public int getPurchaseTimeStamp() {
        return purchaseTimeStamp;
    }

    public String getTicketID() {
        return ticketID;
    }


}
