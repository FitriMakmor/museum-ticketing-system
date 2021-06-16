package com.company.controllers;

import com.company.Museum;
import com.company.MuseumMain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.concurrent.*;
import javafx.util.Callback;

import java.util.Set;

public class MonitorController implements Runnable {

    @FXML
    private Text clockText;

    @FXML
    private Text visitorsCountText;

    @FXML
    private Text ticketRemainingText;

    @FXML
    private Text ticketSoldText;

    @FXML
    private ListView nnTurnstileOneList;

    @FXML
    private ListView nnTurnstileTwoList;

    @FXML
    private ListView nnTurnstileThreeList;

    @FXML
    private ListView nnTurnstileFourList;

    @FXML
    private ListView snTurnstileOneList;

    @FXML
    private ListView snTurnstileTwoList;

    @FXML
    private ListView snTurnstileThreeList;

    @FXML
    private ListView snTurnstileFourList;

    @FXML
    private ListView museumVisitorsList;

    @FXML
    private ListView exTurnstileOneList;

    @FXML
    private ListView exTurnstileTwoList;

    @FXML
    private ListView exTurnstileThreeList;

    @FXML
    private ListView exTurnstileFourList;

    @FXML
    private ListView wxTurnstileOneList;

    @FXML
    private ListView wxTurnstileTwoList;

    @FXML
    private ListView wxTurnstileThreeList;

    @FXML
    private ListView wxTurnstileFourList;

    private String startTime;
    private String openTime;
    private String closeTime;
    private String maxVisitors;
    private String maxAt;
    private boolean isRunning;
    private ObservableList museumObservableList = FXCollections.observableArrayList();



    private ListView[][] listViews = new ListView[4][4];
    private ObservableList[][] turnstileObservableList = new ObservableList[4][4];

    public MonitorController(String startTime, String openTime, String closeTime, String maxVisitors, String maxAt) {
        this.startTime = startTime;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.maxVisitors = maxVisitors;
        this.maxAt = maxAt;
        this.isRunning = true;
    }

    public void addMuseumList(String visitorDetails){
        museumObservableList.add(visitorDetails);
        museumVisitorsList.setItems(museumObservableList);
    }

    public void removeMuseumList(String ticketID){
        museumObservableList.remove(ticketID);
        museumVisitorsList.setItems(museumObservableList);
    }

    public void addTurnstileList(String ticketID, int gateIndex, int turnstileIndex){
        turnstileObservableList[gateIndex][turnstileIndex].add(ticketID);
        listViews[gateIndex][turnstileIndex].setItems(turnstileObservableList[gateIndex][turnstileIndex]);
    }

    public void removeTurnstileList(String ticketID, int gateIndex, int turnstileIndex){
        turnstileObservableList[gateIndex][turnstileIndex].remove(ticketID);
        listViews[gateIndex][turnstileIndex].setItems(turnstileObservableList[gateIndex][turnstileIndex]);
    }

    @FXML
    public void initialize() {

        listViews[0][0]=nnTurnstileOneList;
        listViews[0][1]=nnTurnstileTwoList;
        listViews[0][2]=nnTurnstileThreeList;
        listViews[0][3]=nnTurnstileFourList;
        listViews[1][0]=snTurnstileOneList;
        listViews[1][1]=snTurnstileTwoList;
        listViews[1][2]=snTurnstileThreeList;
        listViews[1][3]=snTurnstileFourList;
        listViews[2][0]=exTurnstileOneList;
        listViews[2][1]=exTurnstileTwoList;
        listViews[2][2]=exTurnstileThreeList;
        listViews[2][3]=exTurnstileFourList;
        listViews[3][0]=wxTurnstileOneList;
        listViews[3][1]=wxTurnstileTwoList;
        listViews[3][2]=wxTurnstileThreeList;
        listViews[3][3]=wxTurnstileFourList;

        for(int i=0; i<4;i++){
            for(int j=0; j<4;j++) {
                turnstileObservableList[i][j] = FXCollections.observableArrayList();
            }
        }
        visitorsCountText.setText("0");
    }

    @Override
    public void run() {
        new MuseumMain(this, this.startTime, this.openTime, this.closeTime, this.maxVisitors, this.maxAt);
        while(isRunning){
            // Will listen to any UI updates
        }
    }

    public Text getClockText() { return clockText; }
    public Text getVisitorsCountText() {
        return visitorsCountText;
    }
    public Text getTicketRemainingText() {
        return ticketRemainingText;
    }
    public Text getTicketSoldText() {
        return ticketSoldText;
    }
    public ListView getNnTurnstileOneList() {
        return nnTurnstileOneList;
    }
    public ListView getNnTurnstileTwoList() {
        return nnTurnstileTwoList;
    }
    public ListView getNnTurnstileThreeList() {
        return nnTurnstileThreeList;
    }
    public ListView getNnTurnstileFourList() {
        return nnTurnstileFourList;
    }
    public ListView getSnTurnstileOneList() {
        return snTurnstileOneList;
    }
    public ListView getSnTurnstileTwoList() {
        return snTurnstileTwoList;
    }
    public ListView getSnTurnstileThreeList() {
        return snTurnstileThreeList;
    }
    public ListView getSnTurnstileFourList() {
        return snTurnstileFourList;
    }
    public ListView getMuseumVisitorsList() {
        return museumVisitorsList;
    }
    public ListView getExTurnstileOneList() {
        return exTurnstileOneList;
    }
    public ListView getExTurnstileTwoList() {
        return exTurnstileTwoList;
    }
    public ListView getExTurnstileThreeList() {
        return exTurnstileThreeList;
    }
    public ListView getExTurnstileFourList() {
        return exTurnstileFourList;
    }
    public ListView getWxTurnstileOneList() {
        return wxTurnstileOneList;
    }
    public ListView getWxTurnstileTwoList() {
        return wxTurnstileTwoList;
    }
    public ListView getWxTurnstileThreeList() {
        return wxTurnstileThreeList;
    }
    public ListView getWxTurnstileFourList() {
        return wxTurnstileFourList;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getOpenTime() {
        return openTime;
    }
    public String getCloseTime() {
        return closeTime;
    }
    public String getMaxVisitors() {
        return maxVisitors;
    }
    public String getMaxAt() {
        return maxAt;
    }
    public boolean isRunning() {
        return isRunning;
    }
}
