package com.company.controllers;

import com.company.MuseumMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    final int START_TIME = 800;
    final int OPEN_TIME = 900;
    final int CLOSE_TIME = 1800;
    final int MAX_VISITORS = 900;
    final int MAX_AT_ONCE = 100;


    @FXML
    private Button startButton;

    @FXML
    private TextField clockStartInput;

    @FXML
    private TextField museumOpenInput;

    @FXML
    private TextField museumCloseInput;

    @FXML
    private TextField ticketsAvailableInput;

    @FXML
    private TextField capacityInput;


    public HomeController() {

    }

    @FXML
    public void initialize() {

        clockStartInput.setText(Integer.toString(START_TIME));
        museumOpenInput.setText(Integer.toString(OPEN_TIME));
        museumCloseInput.setText(Integer.toString(CLOSE_TIME));
        ticketsAvailableInput.setText(Integer.toString(MAX_VISITORS));
        capacityInput.setText(Integer.toString(MAX_AT_ONCE));

        startButton.setOnAction(eventHandler -> {
            Parent root;
            try {
                Runnable monitorController = new MonitorController(
                        clockStartInput.getText(), museumOpenInput.getText(), museumCloseInput.getText(), ticketsAvailableInput.getText(), capacityInput.getText()
                );
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("com/company/assets/monitor.fxml"));
                loader.setController(monitorController);
                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(root));
                stage.show();
                Thread monitorThread = new Thread(monitorController);
                monitorThread.start();
                // Hide this current window (if this is what you want)
                ((Node)(eventHandler.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    private void start(ActionEvent event) {

    }



}
