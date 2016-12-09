package com.melnyk.java.services;

import com.melnyk.java.exceptions.WrongIdException;
import com.melnyk.java.models.Drone;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.Collections;

public class DronesService {
    //Перевірка всіх дронів списку на наявність однакових ID-номерів
    public static void checkAndFindWrongID(ObservableList<Drone> dronesList){
        int first = 0, second = 0;
        try {
            ArrayList<Drone> wrongDronesList = new ArrayList<Drone>();
            for (first = 0; first < dronesList.size(); first++) {
                wrongDronesList.add(dronesList.get(first));
                for (second = first + 1; second < dronesList.size(); second++) {
                    if (dronesList.get(first).getId() == dronesList.get(second).getId()) {
                        wrongDronesList.add(dronesList.get(second));
                    }
                }
                if(wrongDronesList.size()>1)throw new WrongIdException(wrongDronesList);
                else wrongDronesList.clear();
            }
        }catch (WrongIdException ex){
            dronesList.remove(first,dronesList.size());
            ex.getFileReadErrorMessage();
        }
    }
    //Визначення для кожного дрону списку відхилення дистанції польоту від середньої
    public static void calculateEachDeviation(double averageDistance, ObservableList<Drone> tempList){
        for (Drone nextDrone : tempList) {
            nextDrone.setDistanceDeviation(nextDrone.calculateDistanceDeviation(averageDistance));
        }
    }
    //Вирахувати середню дистанцю списку дронів
    public static double getAverageDistance(ObservableList<Drone> dronesList){
        double totalDistance = 0;
        for (Drone nextDrone: dronesList) {
            totalDistance += nextDrone.getFlyDistance();
        }
        return totalDistance/ dronesList.size();
    }
    //Пошук в списку дронів із статусом "втрачено"
    public static void findStatusLost(ObservableList<Drone> dronesList,ObservableList<Drone> tempList) {
        for (Drone nextDrone : dronesList) {
            if (nextDrone.getStatus().equals("Втрачено"))
                tempList.add(nextDrone);
        }
    }
    //Пошук в списку дронів із статусом "успішне повернення"
    public static void findStatusSuccessReturn(ObservableList<Drone> dronesList,ObservableList<Drone> tempList)  {
        for (Drone nextDrone : dronesList) {
            if (nextDrone.getStatus().equals("Успішне повернення"))
                tempList.add(nextDrone);
        }
    }
    //Пошук в списку певну вказану к-сть дронів із найкращими швидкостями
    public static void findBestSpeeds(ObservableList<Drone> tempList, int displayedDronesNumber){
       sortBySpeed(0,tempList.size()-1,tempList);
       tempList.remove(displayedDronesNumber,tempList.size());
    }
    //Сортування списку дронів в порядку спадання їх швидкостей
    public static void sortBySpeed(int start,int end,ObservableList<Drone> dronesList){
        int left = start;
        int right = end;
        double middleDroneSpeed = dronesList.get((left+right)/2).getSpeed();
        do{
            while (dronesList.get(left).getSpeed()>middleDroneSpeed)++left;
            while (dronesList.get(right).getSpeed()<middleDroneSpeed)--right;
            if(left<=right){
                Collections.swap(dronesList,left,right);
                left++;
                right--;
            }
        }while (left<right);
        if(start<right) sortBySpeed(start,right,dronesList);
        if(left<end) sortBySpeed(left,end,dronesList);
    }
    //Пошук в списку дронів із приблизно однаковою висотою,в порівнянні з вибраним дроном
    public static void findEqualHeight(ObservableList<Drone> dronesList,ObservableList<Drone> tempList,Drone drone){
        for (Drone nextDrone : dronesList) {
            if (drone.isEqualFlyHeight(nextDrone))
                tempList.add(nextDrone);
        }
    }
    //Формування списку всіх із всіх унікальних моделей, що зустрічаються в списку дронів
    public static ArrayList<String> getModelList(ObservableList<Drone> dronesList){
        ArrayList<String> modelList = new ArrayList<>();
        modelList.add(dronesList.get(0).getModel());
        for (int i = 1; i < dronesList.size(); i++) {
            boolean canAdd = true;
            for (String nextModel : modelList) {
                if (nextModel.equals(dronesList.get(i).getModel())) canAdd = false;
            }
            if (canAdd) modelList.add(dronesList.get(i).getModel());
        }
        return modelList;
    }
    //Визначення найкращих загальних результатів списку серед однакових моделей дронів
    public static void findBestResults(ObservableList<Drone> dronesList,ObservableList<Drone> tempList) {
        for (String modelName : getModelList(dronesList)) {
            Drone tempDrone = getFirstDroneWithModel(dronesList,modelName);
            for (Drone nextDrone : dronesList) {
                if (modelName.equals(nextDrone.getModel()) && tempDrone.getOverallResult() < nextDrone.getOverallResult()) {
                    tempDrone = nextDrone;
                }
            }
            tempList.add(tempDrone);
        }
    }
    //Пошук першого дрона, який має задану модель
    public static Drone getFirstDroneWithModel(ObservableList<Drone> dronesList,String model){
        for (Drone nextDrone : dronesList) {
            if (nextDrone.getModel().equals(model)) return nextDrone;
        }
        return null;
    }
    //Перевірка правильності заданого ID-номеру
    public static boolean checkID(ObservableList<Drone> dronesList,int id){
        for (Drone drone: dronesList) {
            if(drone.getId()==id) return false;
        }
        return true;
    }
}