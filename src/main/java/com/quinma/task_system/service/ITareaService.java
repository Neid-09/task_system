package com.quinma.task_system.service;

import java.util.List;

import com.quinma.task_system.model.Tarea;

public interface ITareaService {
    List<Tarea> getTareas();

    Tarea getTarea(Tarea tarea);

    Tarea saveTarea(Tarea tarea);

    boolean deleteTarea(Tarea tarea);
}
