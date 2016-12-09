package com.melnyk.java.utils;

import com.melnyk.java.exceptions.EmptyFileException;
import com.melnyk.java.exceptions.WrongFileDataException;
import com.melnyk.java.models.Drone;
import com.melnyk.java.services.DronesService;
import javafx.collections.ObservableList;
import java.io.*;

public class WorkWithFile{
    //Зчитування даних файлу та формування саписку дронів
    public static void readFileStrings(File fileName, ObservableList<Drone> dronesList) throws FileNotFoundException{
        try {
            if(fileName.length() == 0) throw new EmptyFileException();
            dronesList.clear();
            String inputFileLine, lineWords[];
            int currentLineCount = 0;
            //Порядкове зчитування файлу
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "Cp1251"));
            try {
                while ((inputFileLine = fileReader.readLine()) != null) {
                    lineWords = inputFileLine.split("\\s{1,20}", 7);
                    ++currentLineCount;
                    if (!checkFileData(lineWords)) throw new WrongFileDataException(currentLineCount);
                    dronesList.add(new Drone(Integer.parseInt(lineWords[0]),
                            lineWords[1], lineWords[2], Double.parseDouble(lineWords[3]),
                            Double.parseDouble(lineWords[4]), Double.parseDouble(lineWords[5]), lineWords[6]));
                }
            } finally {
                fileReader.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }catch (EmptyFileException ex){
            ex.getErrorMessage();
        } catch (WrongFileDataException ex){
            ex.getErrorMessage();
        }
        DronesService.checkAndFindWrongID(dronesList);
    }
    //Запис даних списку дронів у файл
    public static void writeToFile(File fileName, ObservableList<Drone> dronesList) {
        try {
            BufferedWriter fileWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(fileName), "Cp1251" ));
            for(Drone drone : dronesList) {
                fileWriter.write(drone.toString());
                fileWriter.newLine();
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Перевірка коректності даних у файлі
    private static boolean checkFileData(String fileLineWords[]) {
        if(fileLineWords.length<7) return false;
        try {
            int number1 = Integer.parseInt(fileLineWords[0]);
            double number2 = Double.parseDouble(fileLineWords[3]);
            double number3 = Double.parseDouble(fileLineWords[4]);
            double number4 = Double.parseDouble(fileLineWords[5]);
        if(number1<0 || number2<0 || number3<0 || number4<0) return false;
        }catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
}
