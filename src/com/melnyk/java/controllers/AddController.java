package com.melnyk.java.controllers;

import com.melnyk.java.exceptions.EmptyFieldsException;
import com.melnyk.java.exceptions.WrongIdException;
import com.melnyk.java.exceptions.WrongInputNumbers;
import com.melnyk.java.services.DronesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.melnyk.java.models.Drone;

public class AddController {

    private Drone newDrone;//Новий дрон, який буде додано до списку
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtModel;
    @FXML
    private TextField txtOperator;
    @FXML
    private TextField txtDistance;
    @FXML
    private TextField txtHeight;
    @FXML
    private TextField txtSpeed;
    @FXML
    private TextField txtStatus;

    public Drone getNewDrone() {
        return newDrone;
    }

    public void setNewDrone(Drone newDrone) {
        this.newDrone = newDrone;
    }
    //Закриття вікна додавання
    public void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    //
    public void applyAdding(ActionEvent actionEvent) {
        try {
            checkFieldsTextSize();
            if(!checkNumbers()) throw new WrongInputNumbers();
            checkAddingId();
            newDrone = new Drone(Integer.parseInt(txtID.getText()), txtModel.getText(), txtOperator.getText(),
                    Double.parseDouble(txtDistance.getText()), Double.parseDouble(txtHeight.getText()),
                    Double.parseDouble(txtSpeed.getText()), txtStatus.getText());
            closeWindow(actionEvent);
        } catch (WrongInputNumbers ex) {
            ex.getErrorMessage();
        }
        catch (WrongIdException ex){
            ex.getAddingErrorMessage();
        }
        catch (EmptyFieldsException ex){
            ex.getErrorMessage();
        }
    }
    //Перевірка чи всі необхідіні поля заповнені
    private void checkFieldsTextSize() throws EmptyFieldsException {
        if (txtModel.getText().trim().length() == 0 || txtOperator.getText().trim().length() == 0
                || txtDistance.getText().trim().length() == 0 || txtHeight.getText().trim().length() == 0
                || txtStatus.getText().trim().length() == 0 || txtSpeed.getText().trim().length() == 0
                || txtID.getText().trim().length() == 0) throw new EmptyFieldsException();
    }
    //Перевірка введеного ID-номеру
    private void checkAddingId() throws WrongIdException,NumberFormatException{
        if (!DronesService.checkID(MainController.getMainDronesList(),Integer.parseInt(txtID.getText())))
            throw new WrongIdException();
    }
    //Перевірка коректності введених числових даних
    private boolean checkNumbers(){
        try {
            if (Integer.parseInt(txtID.getText()) < 0 ||
                    Double.parseDouble(txtDistance.getText()) < 0 ||
                    Double.parseDouble(txtHeight.getText()) < 0 ||
                    Double.parseDouble(txtSpeed.getText()) < 0)
                return false;
        }catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
}
