package com.company;

import com.company.controllers.MonitorController;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Random;

public class MuseumMain{

    /**
     * Driver method to:
     * 1. Initialize constant variables.
     * 2. Create the Museum instance.
     * 3. Run loop which constantly attempts to perform ticket transactions while checking whether the museum has reached closing time.
     * 4. Inform that the museum has been closed.
     * @param mc
     * @param startTime
     * @param openTime
     * @param closeTime
     * @param maxVisitors
     * @param maxAt
     */
    public MuseumMain(MonitorController mc, String startTime, String openTime, String closeTime, String maxVisitors, String maxAt) {

        final int START_TIME = Integer.parseInt(startTime);
        final int OPEN_TIME = Integer.parseInt(openTime);
        final int CLOSE_TIME = Integer.parseInt(closeTime);
        final int MAX_VISITORS = Integer.parseInt(maxVisitors);
        final int MAX_AT_ONCE = Integer.parseInt(maxAt);

        Museum museum = new Museum(mc, START_TIME, CLOSE_TIME, MAX_VISITORS, MAX_AT_ONCE);

        Random r = new Random();

        boolean gatesOpened = false;
        while (museum.getCurrentTime() < CLOSE_TIME) {
            try {
//                mc.getTicketRemainingText().setText(Integer.toString(museum.getRemainingTickets()));
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

//        mc.getTicketRemainingText().setText(Integer.toString(museum.getRemainingTickets()));

        if (museum.getCurrentTime() >= CLOSE_TIME) {
            System.out.print("Museum has been closed!");
            museum.announceExit();
        } else {
            System.out.println("Error: attempting to close Museum before closing time.");
        }

    }

}