package com.melnyk.java.exceptions;

import com.melnyk.java.utils.DialogManager;

public class EmptyFileException extends  Exception{
    public EmptyFileException() {
    }
    public void getErrorMessage() {
        DialogManager.showErrorDialog("Помилка зчитування","Файл пустий");
    }
}
