package com.quinma.task_system.controllerFX;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quinma.task_system.model.Tarea;
import com.quinma.task_system.service.ITareaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

@Component // <- Si luego es por API REST, no es necesario
public class IndexControllerFX implements Initializable {

    /* TABLA */
    @FXML
    private TableView<Tarea> tblTareas;

    @FXML
    private TableColumn<Tarea, String> colResponsable;

    @FXML
    private TableColumn<Tarea, Integer> colId;

    @FXML
    private TableColumn<Tarea, String> colTarea;

    @FXML
    private TableColumn<Tarea, String> colEstatus;

    /* CAMPOS DE TEXTO */
    @FXML
    private Label lblId;

    @FXML
    private TextField txtResponsable;

    @FXML
    private TextField txtTarea;

    @FXML
    private TextField txtEstatus;

    /* Botones */
    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnNuevo;

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
        configurarColumnas();
        listarTareas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colTarea.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus")); // <- Cambiar por estatus(En todo)
    }

    private void listarTareas() {
        logger.info("Listando tareas desde el servicio");
        /* Limpiar la lista dinámica */
        tareasList.clear();
        /* Obtener las tareas del servicio y añadirlas a la lista */
        tareasList.addAll(tareaService.getTareas());
        /* Actualizar la tabla(el componente view) */
        tblTareas.setItems(tareasList);
    }

    @FXML
    void guardarTarea(ActionEvent event) {
        logger.info("Guardando tarea");
        if (lblId.getText() == null || lblId.getText().isEmpty()) {
            agregarTarea();
        } else {
            actualizarTarea();
        }
    }

    private void agregarTarea() {
        logger.info("Agregando tarea desde el formulario");

        if(!validarCampos()) return;
        logger.info("Campos validados correctamente");  

        /* Crear una nueva tarea */
        Tarea nuevaTarea = new Tarea();
        recolectarDatosForm(nuevaTarea);

        /* Guardar la nueva tarea */
        /* Guardamos en var para asignarla a la tabla */
        Tarea tareaGuardada = tareaService.saveTarea(nuevaTarea);
        tareasList.add(tareaGuardada);
        
        limpiarCampos();

        mostrarMensaje("Tarea agregada", "La tarea se ha agregado correctamente");

    }

    private void actualizarTarea(){
        logger.info("Actualizando tarea desde el formulario");

        if(!validarCampos()) return;
        logger.info("Campos validados correctamente");

        /* Recolectar datos del formulario */
        Tarea tareaActualizada = new Tarea();
        recolectarDatosForm(tareaActualizada);

        /* Actualizar la tarea */
        tareaService.saveTarea(tareaActualizada);

        limpiarCampos();

        listarTareas(); // <- Actualizar la lista de tareas, llamado a DB

        mostrarMensaje("Tarea actualizada", "La tarea se ha actualizado correctamente");
    }

    private boolean validarCampos() {
        logger.info("Validando campos del formulario");
        /* Validar campos vacios */
        if (txtTarea.getText().isEmpty() || txtResponsable.getText().isEmpty() || txtEstatus.getText().isEmpty()) { 
            mostrarMensaje("Error al validar campos", "Todos los campos son obligatorios");
            // Algo del focus del txtField
            return false;
        }
        return true;
    }

    private void recolectarDatosForm(Tarea tarea){
        logger.info("Recolectando datos del formulario para la tarea");
        tarea.setId(lblId.getText() == null || lblId.getText().isEmpty() ? null : Integer.parseInt(lblId.getText()));
        tarea.setNombreTarea(txtTarea.getText().strip());
        tarea.setResponsable(txtResponsable.getText().strip());
        tarea.setEstatus(txtEstatus.getText().strip());
    }

    @FXML
    void eliminarTarea(ActionEvent event) {
        logger.info("Eliminando tarea");
        Tarea tareaSeleccionada = new Tarea();
        recolectarDatosForm(tareaSeleccionada);
        if (tareaSeleccionada.getId() == null) {
            mostrarMensaje("Error al eliminar tarea", "No se ha seleccionado una tarea para eliminar");
            return;
        }
        tareaService.deleteTarea(tareaSeleccionada);
        tareasList.removeIf(t -> t.getId().equals(tareaSeleccionada.getId()));
        limpiarCampos();
        mostrarMensaje("Tarea eliminada", "La tarea se ha eliminado correctamente");
    }

    @FXML
    void nuevaTarea(ActionEvent event) {
        logger.info("Creando nueva tarea");
        limpiarCampos();
    }

    
    @FXML
    void cargarTareaForm(MouseEvent event) { //<- Recolectar tarea de la tabla
        logger.info("Cargando tarea desde la tabla al formulario");
        Tarea tareaSeleccionada = tblTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) { //<- si existe una tarea seleccionada
            cargarDatosForm(tareaSeleccionada);
        }
    }

    private void cargarDatosForm(Tarea tarea) {
        logger.info("Cargando datos desde el fomulario, tarea: {}", tarea.getId());
        lblId.setText(String.valueOf(tarea.getId()));
        txtTarea.setText(tarea.getNombreTarea());
        txtResponsable.setText(tarea.getResponsable());
        txtEstatus.setText(tarea.getEstatus());
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        logger.warn("{}: {}", titulo, mensaje);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos(){
        logger.info("Limpiando campos del formulario");
        lblId.setText(null);
        txtTarea.clear();
        txtResponsable.clear();
        txtEstatus.clear();
        tblTareas.getSelectionModel().clearSelection(); // Limpiar selección de la tabla
    }

}
