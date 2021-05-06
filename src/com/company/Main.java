package com.company;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        final int START_TIME = 800;
        final int OPEN_TIME = 900;
        final int CLOSE_TIME = 1800;
        final int MAX_VISITORS = 900;
        final int MAX_AT_ONCE = 100;

        Museum museum = new Museum(START_TIME, CLOSE_TIME, MAX_VISITORS, MAX_AT_ONCE);

        Random r = new Random();

        boolean gatesOpened = false;
        while (museum.getCurrentTime() < CLOSE_TIME) {
            try {
                if (museum.getRemainingTickets() > 0) {
                    if (museum.getCurrentTime() >= OPEN_TIME && gatesOpened == false) {
                        museum.openGates();
                        gatesOpened = true;
                    }
                    if (museum.getCurrentTime() < CLOSE_TIME - 100) { //ticket can be purchased until one hour before closing time (hence the -100)
                        museum.buyTicket(r.nextInt(4) + 1);
                        Thread.sleep((r.nextInt(4) + 1) * 100);
                    } else {
                        Thread.sleep(100);
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        if (museum.getCurrentTime() >= CLOSE_TIME) {
            System.out.print("Museum has been closed!");
//            museum.closeGates();
            museum.announceExit();
        } else {
            System.out.println("Error: attempting to close Museum before closing time.");
        }
        
    }

}
