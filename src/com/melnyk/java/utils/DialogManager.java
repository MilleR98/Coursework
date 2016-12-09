package com.melnyk.java.utils;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Optional;

public class DialogManager {
        //Вікно виведення додаткової інформації
        public static void showInfoDialog(String title, String text,ImageView image){
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle(title);
            infoAlert.setContentText(text);
            infoAlert.setHeaderText("");
            infoAlert.setGraphic(image);
            image.setFitWidth(64);
            image.setFitHeight(64);
            infoAlert.showAndWait();
        }
        //Вікно виведення прмилки
        public static void showErrorDialog(String title, String text) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle(title);
            errorAlert.setContentText(text);
            errorAlert.setHeaderText("");
            errorAlert.showAndWait();
        }
        //Вікно підтвердження створення нового файлу
        public static String showConfirmationDialogForNewFile(String title, String text){
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Підтвердження");
            confirmationAlert.setTitle(title);
            confirmationAlert.setContentText(text);
            confirmationAlert.setHeaderText("");

            ButtonType buttonTypeYes = new ButtonType("Так", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("Ні", ButtonBar.ButtonData.NO);
            ButtonType buttonTypeCancel = new ButtonType("Відмінити", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo,buttonTypeCancel);

            Optional<ButtonType> result =  confirmationAlert.showAndWait();
            if(result.get() == buttonTypeYes) return "Так";
            else if(result.get() == buttonTypeNo) return "Ні";
            else return "Відмінити";
        }
        //Вікно підтвердження виходу з програми
        public static boolean showConfirmationDialogForExit(String title, String text){
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Підтвердження");
            confirmationAlert.setTitle(title);
            confirmationAlert.setContentText(text);
            confirmationAlert.setHeaderText("");

            ButtonType buttonTypeYes = new ButtonType("Так", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("Ні", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);

            Optional<ButtonType> result =  confirmationAlert.showAndWait();
            if(result.get() == buttonTypeYes) {
                return true;
            }else
                return false;
        }
        //Вікно вибору к-сті об'єктів списку для виведення
        public static int showChoiseDialog(int numbersToChoose,String title,String headerText){
            ArrayList<Integer> choicesNumbers = new ArrayList<>();
            for (int i = 1; i <=numbersToChoose ; i++) choicesNumbers.add(i);
            ChoiceDialog<Integer> choiseDialog = new ChoiceDialog<Integer>(1, choicesNumbers);
            choiseDialog.setTitle(title);
            choiseDialog.setHeaderText(headerText);

            choiseDialog.getDialogPane().getButtonTypes().clear();
            ButtonType buttonTypeYes = new ButtonType("Так", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Відмінити", ButtonBar.ButtonData.CANCEL_CLOSE);
            choiseDialog.getDialogPane().getButtonTypes().setAll(buttonTypeYes,buttonTypeCancel);

            Optional<Integer> result = choiseDialog.showAndWait();
            return result.orElse(0);
        }
}