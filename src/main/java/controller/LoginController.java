package controller;

import DAO.BD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import model.Usuario;
import model.UsuarioSingleton;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.io.IOException;

public class LoginController {

    private Usuario loggedUser;
    private PrincipalController pc;
    public LoginController loginController;
    private Usuario usuario;
    BD bd = new BD();

    @FXML
    public TextField loginTextField, senhaTextField;

    @FXML
    public Button acessarButton, fecharButton, infoButton;

    @FXML
    public void initialize() {

        Image info = new Image(getClass().getResourceAsStream("/info.png"));
        infoButton.setGraphic(new ImageView(info));
    }

    @FXML
    public void entrar() {
        usuario = bd.getUsuarioMatricula(loginTextField.getText());

        System.out.println(usuario);

        if (loginTextField.getText() == null && senhaTextField.getText() == null) {
            msgLoginNull();
            return;
        } else if (usuario != null) {
            if (!BCrypt.checkpw(senhaTextField.getText(), usuario.getSenha())) {
                msgLoginDados();
            } else {
                try {
                    setLoggedUser(usuario);
                    System.out.println(getLoggedUser().getNome() + "\n" + getLoggedUser().getSetor());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            msgLoginIncs();
        }
    }

    @FXML
    public void info() {
        JOptionPane.showMessageDialog(null,
                "Bem vindo ao SAS - Sistema de Atividades dos Servidores\n"
                        + "Não possui usuário? Entre em contato com a GETIG.\n"
                        + "Contato: Guilherme - (48) 3665-9933\n"
                        + "Sistema desenvolvido por: Guilherme Humberto Dias");
    }

    public void msgLoginNull() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Usuário");
        alert.setHeaderText("Login e Senha em branco...");
        alert.setContentText("Entre em contato com a GETIG para dúvidas");
        alert.showAndWait();
    }

    public void msgLoginIncs() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Usuário");
        alert.setHeaderText("Login não existe...");
        alert.setContentText("Entre em contato com a GETIG para dúvidas");
        alert.showAndWait();
    }

    public void msgLoginDados() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Login ou Senha não conferem, tente novamente\n");
        alert.setContentText("Entre em contato com a GETIG para dúvidas");
        alert.setTitle("Dados de Login não conferem");
        alert.showAndWait();
    }

    @FXML
    public void fecharJanela() {
        fecharButton.getScene().getWindow().hide();
    }

    @FXML
    public void start() {
        Stage startStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("principal.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrincipalController principalController = loader.getController();
        principalController.setPrincipalController(this);

        principalController.usuarioLabel.setText(getLoggedUser().getNome());
        principalController.getUserLogged(getLoggedUser());
        //principalController.usrLogin.setSetor(getLoggedUser().getSetor());
        principalController.usrLogin = getLoggedUser();

        if (getLoggedUser().getTipoUsuario().getId() == 1) {
            principalController.cadastrarMenu.setVisible(true);
            //principalController.editarMenu.setVisible(true);
        } else if (getLoggedUser().getTipoUsuario().getId() == 2) {
            principalController.cadastrarMenu.setVisible(false);
            //principalController.editarMenu.setVisible(false);
            principalController.processoMenuItem.setVisible(false);
            principalController.listarMenuItem.setVisible(false);
        }

        //principalController.attTable(getLoggedUser().getSetor());
        principalController.attTable(getLoggedUser());

        startStage.setTitle("Cadastrar Atividade");
        startStage.setScene(new Scene(loader.getRoot()));
        startStage.setMaximized(true);
        startStage.show();
        acessarButton.getScene().getWindow().hide();
    }

    private void setPrincipalController(LoginController controller) {
        loginController = controller;
    }

    @FXML
    public void abrirSistema() throws IOException {
        Stage principalStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/controller/principal.fxml"));
        principalStage.setScene(new Scene(root));
        principalStage.setMaximized(true);
        principalStage.show();
        acessarButton.getScene().getWindow().hide();
        /*System.out.println(getLoggedUser().getNome());
        System.out.println(getLoggedUser().getSetor().toString());*/
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) throws IOException {
        this.loggedUser = loggedUser;
        start();
        //abrirSistema();
    }

    @FXML public void sobreSistema() {


    }

    @FXML
    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && event.getSource() == senhaTextField) {
            entrar();
            event.consume();
        } else if (event.getCode() == KeyCode.ENTER && event.getSource() == loginTextField){
           entrar();
            event.consume();
        }
    }
}
