package com.melnyk.java.controllers;

import com.melnyk.java.exceptions.EmptyListException;
import com.melnyk.java.exceptions.NoSelectedDroneException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import com.melnyk.java.services.DronesService;
import com.melnyk.java.utils.DialogManager;
import com.melnyk.java.models.Drone;
import com.melnyk.java.utils.WorkWithFile;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class MainController {

    private static ObservableList<Drone> mainDronesList;//Основний список дронів
    private ObservableList<Drone> tempDronesList;//Список для збереження списку дронів, відповідно до завдання
    private static final int NUMBER_OF_MAIN_COLUMNS = 7;//К-сть основних стовпців таблиці
    private static final int DEVIATION_COLUMN_INDEX = 7;//Індекс додаткового стовпця таблиці
    private File currentFile;//Поточний файл
    private AddController addController;//Об'єкт контроллера вікна додавання інформації про нового дрона
    private boolean isMainListShowed;//Визначає чи в даний момент в таблиці показаний основний список
    private boolean isSaveFileChosen;//Визначає чи вибраний файл для зберігання дпних списку
    //Таблиця та її стовпці
    @FXML
    private TableView<Drone> dronesTable;
    @FXML
    private TableColumn<Drone,Integer> idColumn;
    @FXML
    private TableColumn<Drone,String> modelColumn;
    @FXML
    private TableColumn<Drone,String> operatorColumn;
    @FXML
    private TableColumn<Drone,Double> distanceColumn;
    @FXML
    private TableColumn<Drone,Double> heightColumn;
    @FXML
    private TableColumn<Drone,Double> speedColumn;
    @FXML
    private TableColumn<Drone,String> statusColumn;
    //Ініціалізація деяких змінних класу та таблиці
    @FXML
    public void initialize()
    {
        isMainListShowed = true;
        isSaveFileChosen = false;
        mainDronesList = FXCollections.observableArrayList();
        tempDronesList = FXCollections.observableArrayList();
        dronesTable.getItems().clear();
        //Встановлення для кожного стовпця таблиці певної властивості дрону, за яку вона буде відповідати і представляти
        idColumn.setCellValueFactory(new PropertyValueFactory<Drone, Integer>("id"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Drone, String>("model"));
        operatorColumn.setCellValueFactory(new PropertyValueFactory<Drone, String>("operator"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<Drone, Double>("flyDistance"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<Drone, Double>("flyHeight"));
        speedColumn.setCellValueFactory(new PropertyValueFactory<Drone, Double>("speed"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Drone, String>("status"));

        idColumn.setMaxWidth(2000);
        dronesTable.setItems(mainDronesList);
    }

    public static ObservableList<Drone> getMainDronesList() {
        return mainDronesList;
    }

    public void setDronesList(ObservableList<Drone> dronesList) {
        MainController.mainDronesList = dronesList;
    }
    //Ф-ція відкриття нового файлу
    public void openFile(ActionEvent actionEvent) {
        FileChooser openFileChooser = new FileChooser();
        openFileChooser.setTitle("Виберіть файл для зчитування");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        openFileChooser.getExtensionFilters().add(extFilter);
        currentFile = openFileChooser.showOpenDialog(new Stage());//Вибір нового файлу
        try {
            if(currentFile!=null){
            WorkWithFile.readFileStrings(currentFile, mainDronesList);
            dronesTable.setItems(mainDronesList);
            }
        } catch (FileNotFoundException e) {
            DialogManager.showErrorDialog("Помилка","Не вдалося відкрити файл "+ currentFile.getName());
        }
    }
    //Ф-ція створення нового файлу
    public void newFile(ActionEvent actionEvent){
        String clickedButtonName = DialogManager.showConfirmationDialogForNewFile("Підтвердження","Зберегти попередній файл перед тим, як створити новий?");
        if(clickedButtonName.equals("Так")) {
            saveFile(actionEvent);
        }
        //Очищення попереднього списку, якщо була натиснута кнопка "Ні" або "Так" і попередній файл було збережено
        if(clickedButtonName.equals("Ні") ||
                (clickedButtonName.equals("Так") && isSaveFileChosen && currentFile==null) ||
                (clickedButtonName.equals("Так") && !isSaveFileChosen && currentFile!=null)) {
            mainDronesList.clear();
            tempDronesList.clear();
            dronesTable.getItems().clear();
            currentFile = null;
        }
        isSaveFileChosen = false;
    }
    //Ф-ція збереження
    public void saveFile(ActionEvent actionEvent){
        if (currentFile!=null){//Якщо файл вже існує на комп'ютері, зберегти список у поточний відкритий файл
            if(!isMainListShowed) WorkWithFile.writeToFile(currentFile, tempDronesList);//Якщо плказано загальний список зберегти його
            else WorkWithFile.writeToFile(currentFile, mainDronesList);//інакше зберегти поточний тимчасовий список
        }//Інакше вибрати або створити файл для збереження
        else
            saveFileAs(actionEvent);
    }
    //Ф-ція вибору або створення файлу та запису списку у цей файл
    public void saveFileAs(ActionEvent actionEvent) {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Виберіть файл для запису");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        saveFileChooser.getExtensionFilters().add(extFilter);
        File chosenFile = saveFileChooser.showSaveDialog(new Stage());
        if (chosenFile != null) {//Заппис списку у файл, якщо він вибраний
            if(!isMainListShowed) WorkWithFile.writeToFile(chosenFile, tempDronesList);
            else WorkWithFile.writeToFile(chosenFile, mainDronesList);
            currentFile = chosenFile;
            isSaveFileChosen = true;
        }
    }
    //Підтвердження виходу з програми
    public void exitFromProgram(ActionEvent actionEvent) {
        if(DialogManager.showConfirmationDialogForExit("Підтвердження виходу","Ви дійсно бажаєте закрити програму?"))
            Platform.exit();
    }
    //Ф-ція відериття нового вікна для введення інформації про нового дрона
    private void showAddWindow(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/melnyk/java/views/add_window.fxml"));
        Parent newWindow = null;
        try {
            newWindow = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage newStage = new Stage();
        newStage.setResizable(false);
        newStage.setTitle("Додавання інформації про нового дрона");
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        addController = loader.getController();
        Image mainIcon = new Image("/resources/images/add.png");
        newStage.getIcons().add(mainIcon);
        newStage.setScene(new Scene(newWindow));
        newStage.showAndWait();
    }
    //Ф-ція додавання нового дрону до списку
    public void addDrone(ActionEvent actionEvent)  {
        showAddWindow(actionEvent);//Показати вікно додавання
        if(addController.getNewDrone() !=null)//Якщо новий дрон був створений, додати його до загального списку
            mainDronesList.add(addController.getNewDrone());
        dronesTable.setItems(mainDronesList);
        getPreviousList(actionEvent);
    }
    //Виведення найкращих загальних результатів серед одакових моделей
    public void getBestResults(ActionEvent actionEvent) {
        try {
            isMainListShowed = false;
            tempDronesList.clear();
            if(dronesTable.getColumns().size() > NUMBER_OF_MAIN_COLUMNS) dronesTable.getColumns().remove(DEVIATION_COLUMN_INDEX);//Видалення додаткової колонки
            if (mainDronesList.isEmpty()) throw new EmptyListException();
            DronesService.findBestResults(mainDronesList, tempDronesList);
            dronesTable.setItems(tempDronesList);
        }catch (EmptyListException ex){
            ex.getErrorMessage();
        }
    }
    //Виведення загального списку в таблицю
    public void getPreviousList(ActionEvent actionEvent){
        isMainListShowed = true;
        tempDronesList.clear();
        dronesTable.setItems(mainDronesList);
        if(dronesTable.getColumns().size()> NUMBER_OF_MAIN_COLUMNS) dronesTable.getColumns().remove(DEVIATION_COLUMN_INDEX);
    }
    //Виведення в таблицю моделей з приблизно однаковою висотою польоту, в порівнянні з вибраним дроном
    public void getEqualModelsByHeight(ActionEvent actionEvent) {
        try {
            isMainListShowed = false;
            if(dronesTable.getColumns().size() > NUMBER_OF_MAIN_COLUMNS) dronesTable.getColumns().remove(DEVIATION_COLUMN_INDEX);
            tempDronesList.clear();
            if (mainDronesList.isEmpty()) throw new EmptyListException();
            Drone selectedDrone = dronesTable.getSelectionModel().getSelectedItem();//Вибраний дрон із загального списку
            if(selectedDrone==null) throw new NoSelectedDroneException();
            DronesService.findEqualHeight(mainDronesList, tempDronesList,selectedDrone);
            dronesTable.setItems(tempDronesList);
        }catch (NoSelectedDroneException ex){
            ex.getErrorMessage();
        } catch (EmptyListException e) {
            e.getErrorMessage();
        }
    }
    //Виведення моделей із статусом "втрачено" та їх відхилення висоти польоту від сердньої у метрах
    public void getDronesWithStatusLostAndDeviation(ActionEvent actionEvent) {
        try {
            isMainListShowed = false;
            tempDronesList.clear();
            TableColumn<Drone, Double> deviationColumn = new TableColumn<>("Відхилення\nдистанції від\nсередньої,м");
            deviationColumn.setCellValueFactory(new PropertyValueFactory<Drone, Double>("distanceDeviation"));
            if (mainDronesList.isEmpty()) throw new EmptyListException();
            dronesTable.getColumns().add(deviationColumn);
            DronesService.findStatusLost(mainDronesList, tempDronesList);
            DronesService.calculateEachDeviation(DronesService.getAverageDistance(mainDronesList), tempDronesList);
            dronesTable.setItems(tempDronesList);
        } catch (EmptyListException e) {
            e.getErrorMessage();
        }
    }
    //Виведення вікна із  короткою інформацією про дану програму
    public void showAboutProgram(ActionEvent actionEvent) {
        Image image = new Image(getClass().getResource("/resources/images/drone.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        DialogManager.showInfoDialog("Про програму","Курсова робота. Таблиця «Дрон»\n\n" +
                        "Програму розроблено на мові програмування Java у середовищі розробки IntelliJ IDEA 2016.3, і вона може\n" +
                        "експлуатуватися під управлінням сімейства операційних систем Windows.\n" +
                        "Під час проектування підсистем використовувася підхід до об’єктно-орієнтованого прогрмування.\n" +
                        "Для реалізації було використано архітектурний шаблон проектування MVС(Model-View-Controller)\n\n"+
                        "Версія 5.0 (2016)\n" +
                        "Виробник: студент групи ПІ-24 Мельник О.С.",imageView);
    }
    //Відкриття в браузері help-файлу з інструкцією користувача
    public void openHelpPage(ActionEvent actionEvent){
        try {
            String workingDir = System.getProperty("user.dir");//Шлях до поточного exe-файлу
            workingDir = workingDir.replace('\\','/');
            URI uri = new URI("file:///" + workingDir + "/Help.html").normalize();
            Desktop.getDesktop().browse(uri);
        }catch (URISyntaxException | IOException ex){
            DialogManager.showErrorDialog("Помилка відкриття", "Help.html повинен бути в одній папці з exe-файлом");
        }
    }
    //Виведення в таблицю дронів, які мають найкращу швидкість
    public void getBestSpeeds(ActionEvent actionEvent) {
        try {
            isMainListShowed = false;
            if (mainDronesList.isEmpty()) throw new EmptyListException();
            int displayedDrones;//К-сть дронів, які потрібно вивести
            displayedDrones = DialogManager.showChoiseDialog(mainDronesList.size(),"Вибір","Яку к-сть дронів з найкращими швидкостями показати?");
            if(displayedDrones !=0) {
                tempDronesList.clear();
                tempDronesList.setAll(mainDronesList);
                DronesService.findBestSpeeds(tempDronesList,displayedDrones);
                dronesTable.setItems(tempDronesList);
            }
            if(dronesTable.getColumns().size() > NUMBER_OF_MAIN_COLUMNS) dronesTable.getColumns().remove(DEVIATION_COLUMN_INDEX);
        } catch (EmptyListException e) {
            e.getErrorMessage();
        }
    }
    //Виведення в таблицю дронів із статусом "успішне повернення"
    public void getDronesWithStatusReturn(ActionEvent actionEvent) {
        try {
            isMainListShowed = false;
            tempDronesList.clear();
            if(dronesTable.getColumns().size() > NUMBER_OF_MAIN_COLUMNS) dronesTable.getColumns().remove(DEVIATION_COLUMN_INDEX);
            if (mainDronesList.isEmpty()) throw new EmptyListException();
            DronesService.findStatusSuccessReturn(mainDronesList, tempDronesList);
            dronesTable.setItems(tempDronesList);
        } catch (EmptyListException e) {
            e.getErrorMessage();
        }
    }
}