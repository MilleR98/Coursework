package com.melnyk.java;

import com.melnyk.java.utils.DialogManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/melnyk/java/views/main_window.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Курсова робота. Таблиця «Дрон»");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1000);
        primaryStage.setMaximized(true);

        primaryStage.setOnCloseRequest( event -> {//Показати вікно підтвердження виходу при закритті вікна
            if(!DialogManager.showConfirmationDialogForExit("Підтвердження виходу","Ви дійсно бажаєте закрити програму?"))
                event.consume();
        });
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
