package controller;

import DAO.BD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Setor;
import model.TipoUsuario;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

public class UsuarioController {
    BD bd = new BD();

    private Usuario usuario;

    public PrincipalController principalController;

    private ObservableList<Setor> setores = FXCollections.observableArrayList();
    private ObservableList<TipoUsuario> tipos = FXCollections.observableArrayList();

    @FXML
    public TextField nomeTextField, matriculaTextField, senhaTextField;

    @FXML
    public ComboBox<Setor> setorComboBox;

    @FXML
    public ComboBox<TipoUsuario> tipoComboBox;

    @FXML
    public Button cadastrarButton, fecharButton;

    @FXML
    public void initialize() {
        setorComboBox.setItems(setores);
        setores.addAll(bd.getSetor());
        tipoComboBox.setItems(tipos);
        tipos.addAll(bd.getTipoUsuario());

    }

    @FXML
    public void cadastrarUsuario() {

        if (!validarDados()) {
            exibirMensagem("Existem campos em branco, por favor, preencher todos os campos!");
            return;
        }

        boolean editando = true;

        if (usuario == null) {
            usuario = new Usuario();
            editando = false;
        }

        usuario.setNome(nomeTextField.getText());
        usuario.setSenha(createEncryptedPassword(senhaTextField.getText()));
        usuario.setMatricula(matriculaTextField.getText());
        usuario.setSetor(setorComboBox.getValue());
        usuario.setTipoUsuario(tipoComboBox.getValue());

        if (editando == false){
            principalController.adicionarUsuario(usuario);
            usuario = null;
            cadastrarButton.getScene().getWindow().hide();
        } else if (editando == true) {
            //usuController.atualizarTabela();

        }

        System.out.println(usuario);

    }

    public boolean validarDados() {

        if (nomeTextField.getText() == null || nomeTextField.getText().isEmpty()) {
            return false;
        }

        if (matriculaTextField.getText() == null || matriculaTextField.getText().isEmpty()) {
            return false;
        }

        if (senhaTextField.getText() == null || senhaTextField.getText().isEmpty()) {
            return false;
        }

        if (setorComboBox.getValue() == null) {
            return false;
        }

        return true;
    }

    public void exibirMensagem(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private static String createEncryptedPassword(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    public void setPrincipalController(PrincipalController controller) {
        principalController = controller;
    }

    public void fechar() {
        fecharButton.getScene().getWindow().hide();
    }

}
