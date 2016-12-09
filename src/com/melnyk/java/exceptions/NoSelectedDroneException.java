package com.melnyk.java.exceptions;

import com.melnyk.java.utils.DialogManager;

public class NoSelectedDroneException extends Exception {
    public NoSelectedDroneException() {
    }

    public void getErrorMessage() {
        DialogManager.showErrorDialog("Помилка","Виберіть дрона із загального списку для порівняння");
    }
}
