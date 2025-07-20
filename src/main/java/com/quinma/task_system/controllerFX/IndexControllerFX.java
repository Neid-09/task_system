package com.quinma.task_system.controllerFX;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.stereotype.Component;

import com.quinma.task_system.model.Tarea;
import com.quinma.task_system.service.ITareaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component //<- Si luego es por API REST, no es necesario
public class IndexControllerFX implements Initializable {

    @FXML
    private TableView<Tarea> tblTareas;

    @FXML
    private TableColumn<Tarea, String> colResponsable;

    @FXML
    private TableColumn<Tarea, Integer> colId;
    
    @FXML
    private TableColumn<Tarea, String> colTarea;
    
    @FXML
    private TableColumn<Tarea, String> colEstado;

    @FXML
    private TextField txtResponsable;

    @FXML
    private TextField txtTarea;

    /* Lista que cambia segun el tipo de dato de la Table. */
    private final ObservableList<Tarea> tareasList = FXCollections.observableArrayList();

    private static final Logger logger = LoggerFactory.getLogger(IndexControllerFX.class);

    private final ITareaService tareaService;

    public IndexControllerFX(ITareaService tareaService) {
        this.tareaService = tareaService;
        logger.info("IndexControllerFX inicializado con TareaService");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("IndexControllerFX inicializado");
        /* Para solo seleccionar un elemento de la tabla */
        tblTareas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarTabla();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colTarea.setCellValueFactory(new PropertyValueFactory<>("tarea"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

}
