package com.quinma.task_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quinma.task_system.model.Tarea;
import com.quinma.task_system.repository.TareaRepository;

@Service
public class TareaService implements ITareaService{

    private final TareaRepository tareaRepoRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepoRepository = tareaRepository;
    }

    @Override
    public List<Tarea> getTareas() {
        return tareaRepoRepository.findAll();
    }

    @Override
    public Tarea getTarea(Tarea tarea) {
        return tareaRepoRepository.findById(tarea.getId()).orElse(null);
    }

    @Override
    public Tarea saveTarea(Tarea tarea) {
        return tareaRepoRepository.save(tarea);
    }

    @Override
    public boolean deleteTarea(Tarea tarea) {
        if (tareaRepoRepository.existsById(tarea.getId())) {
            tareaRepoRepository.delete(tarea);
            return true;
        } else {
            return false;
        }
    }
    
}
