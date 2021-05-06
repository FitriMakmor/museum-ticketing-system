package com.company;

public class Ticket {
    private int purchaseTimeStamp;
    private String ticketID;

    /**
     * @param id           visitor's ticket ID
     * @param purchaseTime visitor's purchase time
     */
    public Ticket(String id, int purchaseTime) {
        purchaseTimeStamp = purchaseTime;
        this.ticketID = id;
    }

    /**
     * Getter method to get the visitor's purchase time
     *
     * @return purchaseTimeStamp
     */
    public int getPurchaseTimeStamp() {
        return purchaseTimeStamp;
    }

    /**
     * Getter method to get the ticketID of the visitor's ticket
     *
     * @return ticketID
     */
    public String getTicketID() {
        return ticketID;
    }


}
