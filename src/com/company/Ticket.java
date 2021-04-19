package com.company;

public class Ticket {
    int purchaseTimeStamp;
//    int entryTimeStamp;
    String ticketID;

    public Ticket(String id, int purchaseTime){
        purchaseTimeStamp = purchaseTime;
        this.ticketID = id;
    }

//    public int getEntryTime(){
//        return entryTimeStamp;
//    }


}
