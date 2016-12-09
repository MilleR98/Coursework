package com.melnyk.java.exceptions;

import com.melnyk.java.models.Drone;
import com.melnyk.java.utils.DialogManager;

import java.util.ArrayList;

public class WrongIdException extends Exception {
    private ArrayList<Drone> wrongIdDrones;
    public WrongIdException() {

    }
    public WrongIdException(ArrayList<Drone> dronesList){
        wrongIdDrones = dronesList;
    }
    public void getAddingErrorMessage(){
        DialogManager.showErrorDialog("Помилка додавання", "Дрон із заданим ID-номером уже існує. Вкажіть інший");
    }
    public void getFileReadErrorMessage(){
        String dronesString = "";
        for (Drone drone:wrongIdDrones) {
            dronesString+="Модель - " + drone.getModel()+ ", Оператор - " + drone.getOperator()+"\n";
        }
        DialogManager.showErrorDialog("Помилка зчитування", "Зчитування перервано.\nЄ дрони з однаковим ID-номером - "+
                wrongIdDrones.get(0).getId() +":\n" +dronesString);
    }
}
