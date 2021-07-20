package controller;

import DAO.SetorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Setor;

public class SetorController {

    public PrincipalController principalController;
    public Setor setor;

    SetorDAO setorDAO = new SetorDAO();

    @FXML
    public TextField nomeTextField, siglaTextField;

    @FXML
    public Button cadastrarButton, fecharButton;

    @FXML
    public void initialize() {


    }

    @FXML
    public void cadastrarSetor() {

        if (!validandoCampos()) {
            exibirMensagemErro("Preencha todos os dados!");
            return;
        }

        boolean editando = true;

        if (setor == null) {
            setor = new Setor();
            editando = false;
        }

        setor.setNome(nomeTextField.getText());
        setor.setSigla(siglaTextField.getText());

        if (editando == false) {
            principalController.adicionarSetor(setor);
            setor = null;
            System.out.println(setor);
            cadastrarButton.getScene().getWindow().hide();
        } else if (editando == true) {

        }
    }

    public boolean validandoCampos() {

        if (nomeTextField.getText() == null || nomeTextField.getText().isEmpty()) {
            return false;
        }

        if (siglaTextField.getText() == null || siglaTextField.getText().isEmpty()) {
            return false;
        }


        return true;
    }

    private void exibirMensagemErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void fechar() {
        fecharButton.getScene().getWindow().hide();
    }


    public void setPrincipalController(PrincipalController controller) {
        principalController = controller;
    }
}
