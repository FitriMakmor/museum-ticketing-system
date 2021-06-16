package com.company;

import com.company.controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final int START_TIME = 800;
        final int OPEN_TIME = 900;
        final int CLOSE_TIME = 1800;
        final int MAX_VISITORS = 900;
        final int MAX_AT_ONCE = 100;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/home.fxml"));
        loader.setController(new HomeController());
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Museum Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

