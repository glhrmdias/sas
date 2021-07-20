package controller;

import DAO.BD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.Orgao;

import java.util.List;

public class ListOrgaoController {

    @FXML
    public ListView<Orgao> listViewOrgaos;

    @FXML
    public Button fecharButton;

    public BD banco = new BD();

    List<Orgao> listOrgao;
    ObservableList<Orgao> obsOrgao;

    public MovimentacaoController movimentacaoController;

    @FXML
    public void initialize(){
        populate();
    }

    @FXML
    public void populate(){
        listOrgao = banco.getOrgao();
        obsOrgao = FXCollections.observableArrayList(listOrgao);
        listViewOrgaos.getItems().addAll(obsOrgao);
    }

    @FXML
    public void close(){
        fecharButton.getScene().getWindow().hide();
    }

    public void setPrincipalController(MovimentacaoController controller) {
        movimentacaoController = controller;
    }

}
