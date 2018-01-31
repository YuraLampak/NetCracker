package com.yura.lampak;


import com.yura.lampak.controller.ConsoleController;
import com.yura.lampak.controller.Controller;
import com.yura.lampak.model.Model;
import com.yura.lampak.model.TaskException;
import com.yura.lampak.view.ConsoleView;
import com.yura.lampak.view.View;


public class Main {
    public static void main(String[] args) throws TaskException {
        Model theModel = new Model();
        View theView = new ConsoleView();
        Controller controller = new ConsoleController(theModel, (ConsoleView) theView);
        controller.execute();
    }
}
