package com.quinma.task_system;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quinma.task_system.mainFX.InitFx;

import javafx.application.Application;

@SpringBootApplication
public class TaskSystemApplication {

	public static void main(String[] args) {
		/* SpringApplication.run(TaskSystemApplication.class, args); */
		Application.launch(InitFx.class, args);
	}

}
