package com.melnyk.java.exceptions;

import com.melnyk.java.utils.DialogManager;

public class WrongInputNumbers extends Exception{
    public WrongInputNumbers(){

    }
    public void getErrorMessage(){
        DialogManager.showErrorDialog("Помилка",
                "Значення ID-номеру повинно бути цілим невід'ємним, а швидкості, висоти та дальності польоту дійсним невід'ємним числом");
    }
}
