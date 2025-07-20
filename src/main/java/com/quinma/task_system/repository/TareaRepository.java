package com.quinma.task_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quinma.task_system.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    
}
