package com.quinma.task_system.mainFX;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.quinma.task_system.TaskSystemApplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InitFx extends Application {

    /* Configurar inicio con para pasarle spring */

    private  ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(TaskSystemApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TaskSystemApplication.class.getResource("/templates/Index.fxml"));
        /* Definimos la fábrica de controladores */
        loader.setControllerFactory(applicationContext::getBean);
        
        Scene escena = new Scene(loader.load());
        primaryStage.setScene(escena);
        primaryStage.setTitle("Task System");
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close(); //<- Solo si es monolito
        Platform.exit();
    }
    
}
