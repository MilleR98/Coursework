package com.melnyk.java.exceptions;

import com.melnyk.java.utils.DialogManager;

public class EmptyListException extends Exception {
    public EmptyListException() {
    }

    public void getErrorMessage(){
        DialogManager.showErrorDialog("Помилка","Список пустий");
    }
}
