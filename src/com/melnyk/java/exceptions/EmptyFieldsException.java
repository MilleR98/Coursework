package com.melnyk.java.exceptions;

import com.melnyk.java.utils.DialogManager;

public class EmptyFieldsException extends Exception {
    public EmptyFieldsException() {
    }

    public void getErrorMessage() {
        DialogManager.showErrorDialog("Помилка", "Заповніть всі поля");
    }
}
