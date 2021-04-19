package com.company;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        final int OPEN_TIME = 800;
        final int CLOSE_TIME = 1800;
        boolean gatesOpened = false;

        Clock clock = new Clock(OPEN_TIME, CLOSE_TIME);
        Museum museum = new Museum(clock);

        Random r = new Random();

        while (museum.getRemainingTickets() > 0 && clock.getCurrentTime() < CLOSE_TIME) {
            try {
                if (clock.getCurrentTime() > 900 && gatesOpened == false) {
                    museum.openGates();
                    gatesOpened = true;
                }
                if (clock.getCurrentTime() < CLOSE_TIME - 100) {
                    museum.buyTicket(r.nextInt(4) + 1);
                    Thread.sleep((r.nextInt(4) + 1) * 100);
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("While loop escaped! Current Time = " + clock.getCurrentTime());

        if (clock.getCurrentTime() >= CLOSE_TIME) {
            System.out.println("Museum has been closed!");
            museum.announceExit();
        } else if (museum.getRemainingTickets() <= 0) {
            System.out.println("Outta tickets!");
        } else {
            System.out.println("I dono why, system broken, gg!!");
        }
    }

}
