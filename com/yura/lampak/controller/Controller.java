package com.yura.lampak.controller;

import com.yura.lampak.model.TaskException;

public interface Controller {
    void execute() throws TaskException;
}
