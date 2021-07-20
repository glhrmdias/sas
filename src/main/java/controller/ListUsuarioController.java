package controller;

import DAO.BD;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.TipoUsuario;
import model.Usuario;

import javax.management.StandardMBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListUsuarioController {

    @FXML
    public TableView<Usuario> usuarioTableView;

    @FXML
    public TableColumn<Usuario, String> nomeTableColumn, matriculaTableColumn;

    @FXML
    public TableColumn<Usuario, TipoUsuario> tipoUsuarioTableColumn;

    @FXML
    public TextField nomeTextField, matriculaTextField;

    @FXML
    public Button fecharButton;

    public PrincipalController principalController;
    public BD banco = new BD();


    public ObservableList<Usuario> obUsuario;

    @FXML
    public void initialize() {

        nomeTableColumn.setCellValueFactory(param -> {
            return new SimpleObjectProperty(param.getValue().getNome());
        });

        matriculaTableColumn.setCellValueFactory(param -> {
            return new SimpleObjectProperty(param.getValue().getMatricula());
        });

        tipoUsuarioTableColumn.setCellValueFactory(param -> {
            return new SimpleObjectProperty(param.getValue().getTipoUsuario().getTipoUsuario());
        });

        getData();
        populate();
    }

    public void populate() {
        List<Usuario> listaUsuarios = banco.getUsuario();
        usuarioTableView.getItems().clear();
        usuarioTableView.getItems().addAll(listaUsuarios);
    }

    public void setPrincipalController(PrincipalController controller) {
        principalController = controller;
    }

    public void getData() {
        usuarioTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Usuario usuarioProperty = usuarioTableView.getSelectionModel().getSelectedItem();
                nomeTextField.setText(usuarioProperty.getNome());
                matriculaTextField.setText(usuarioProperty.getMatricula());
            }
        });
    }

}
