package com.melnyk.java.models;

public class Drone {
    private int id;
    private String model;
    private String operator;
    private double flyDistance;
    private double flyHeight;
    private double speed;
    private String status;
    private double distanceDeviation;//Відхилення результату "дистанція" від середнього значення

    public Drone(int id, String model, String operator, double flyDistance, double flyHeight, double speed, String status) {
        this.id = id;
        this.model = model;
        this.operator = operator;
        this.flyDistance = flyDistance;
        this.flyHeight = flyHeight;
        this.speed = speed;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getFlyDistance() {
        return flyDistance;
    }

    public void setFlyDistance(double flyDistance) {
        this.flyDistance = flyDistance;
    }

    public double getFlyHeight() {
        return flyHeight;
    }

    public void setFlyHeight(double flyHeight) {
        this.flyHeight = flyHeight;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String s = id + "\t" + model + "\t     " + operator + "\t     " + flyDistance + "\t     " + flyHeight + "\t     " + speed + "\t     " + status;
        return s;
    }

    public double getOverallResult(){
        return flyDistance+flyHeight+speed;
    }
    //Визначення, чи однакова висота польоту дронів (з відхиленням 7.359%)
    public boolean isEqualFlyHeight(Drone otherDrone){
        double difference = Math.abs(this.getFlyHeight()-otherDrone.getFlyHeight());
        double deviation = this.flyHeight*0.07359;
        return difference < deviation;
    }
    //Визначення відхилення результату "дистанція" від середнього значення
    public double calculateDistanceDeviation(double averageDistance){
        return Math.abs(this.flyDistance -averageDistance);
    }

    public void setDistanceDeviation(double distanceDeviation) {
        this.distanceDeviation = distanceDeviation;
    }

    public double getDistanceDeviation() {
        return distanceDeviation;
    }
}
