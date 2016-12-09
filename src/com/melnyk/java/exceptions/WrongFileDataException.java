package com.melnyk.java.exceptions;


import com.melnyk.java.utils.DialogManager;

public class WrongFileDataException extends Exception{
    private int breakLine;
    public WrongFileDataException(int breakLine) {
        this.breakLine = breakLine;
    }


    public void getErrorMessage() {
        DialogManager.showErrorDialog("Помилка зчитування","Направильний формат запису даних у файлі.\n" +
                "Зчитування перервано в рядку "+breakLine+"\n" +
                "Правильний формат та порядок запису:\n" +
                "«ID-номер»(невід'ємне ціле число) «Модель» «Оператор» «Дистанція»(невід'ємне дійсне число) «Висота»(невід'ємне дійсне число) «Швидкість»(невід'ємне дійсне число) «Статус»");
    }
}
