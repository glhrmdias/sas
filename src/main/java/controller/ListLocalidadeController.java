package controller;

import DAO.BD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.Localidade;

import java.util.List;

public class ListLocalidadeController {

    public MovimentacaoController movimentacaoController;
    public BD banco = new BD();

    @FXML
    public ListView<Localidade> listViewLocalidades;

    @FXML
    public Button fecharButton;

    List<Localidade> listLocalidade;
    ObservableList<Localidade> obsLocalidades;

    @FXML
    public void initialize(){
        populate();
    }

    public void populate(){
        listLocalidade = banco.getLocal();
        obsLocalidades = FXCollections.observableArrayList(listLocalidade);
        listViewLocalidades.getItems().addAll(obsLocalidades);
    }

    public void close(){
        fecharButton.getScene().getWindow().hide();
    }

    public void setPrincipalController(MovimentacaoController controller) {
        movimentacaoController = controller;
    }
}
