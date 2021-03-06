package controller;

import DAO.BD;
import DAO.MovimentacaoDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;
import util.ComboBoxKeyCompleter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MovimentacaoController {
    BD bd = new BD();
    MovimentacaoDAO movDAO = new MovimentacaoDAO()
;
    public Usuario usuario;

    private Movimentacao movimentacao;

    public PrincipalController principalController;

    private ObservableList<Orgao> orgaos = FXCollections.observableArrayList();
    private ObservableList<Localidade> localidades = FXCollections.observableArrayList();

    @FXML
    public ComboBox<String> atividadeComboBox, assuntoComboBox,
                    horaInicioComboBox, horaFimComboBox,
                    conclusaoComboBox, tempoComboBox;

    @FXML
    public ComboBox<Setor> setorComboBox;

    @FXML
    public ComboBox<Orgao> orgaoComboBox;

    @FXML
    public ComboBox<Localidade> localidadeComboBox;

    @FXML
    public Button cadastrarButton, fecharButton, orgaoButton, localButton;

    @FXML
    public DatePicker dataRefDatePicker, dataInicioDatePicker, dataFimDatePicker;

    @FXML
    public CheckBox processoCheckBox;

    @FXML
    public TextField processoTextField, observacaoTextField, usrTextField;

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
        preMovimentacao();
    }

    public void preMovimentacao() {
        dataRefDatePicker.setValue(movimentacao.getDataRegistro());
        usrTextField.setText(movimentacao.getUsuario());
        atividadeComboBox.setValue(movimentacao.getAtividade());
        assuntoComboBox.setValue(movimentacao.getAssunto());
        orgaoComboBox.setValue(movimentacao.getOrgao());
        processoTextField.setText(movimentacao.getProcesso());
        localidadeComboBox.setValue(movimentacao.getLocal());
        dataInicioDatePicker.setValue(movimentacao.getDataInicio());
        dataFimDatePicker.setValue(movimentacao.getDataFim());
        //horaInicioComboBox.setValue(movimentacao.getHoraInicio());
        tempoComboBox.setValue(movimentacao.getTempoAtividade());
        conclusaoComboBox.setValue(movimentacao.getConclusao());
        observacaoTextField.setText(movimentacao.getOberva????o());
    }

    @FXML
    public void initialize() {
        dataRefDatePicker.setValue(LocalDate.now());

        orgaoComboBox.setItems(orgaos);
        orgaos.addAll(bd.getOrgao());

        localidadeComboBox.setItems(localidades);
        localidades.addAll(bd.getLocal());

        conclusaoComboBox.setItems(conclusao);

        //horaInicioComboBox.setItems(horario);
        tempoComboBox.setItems(tempo);

        /*definirAssunto();*/

        processoCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                processoTextField.setDisable(true);
                processoTextField.setText("");
            } else {
                processoTextField.setDisable(false);
            }
        });

        Image cadastrar = new Image(getClass().getResourceAsStream("/edit1.png"));
        cadastrarButton.setGraphic(new ImageView(cadastrar));

        Image cancelar = new Image(getClass().getResourceAsStream("/can1.png"));
        fecharButton.setGraphic(new ImageView(cancelar));

        ComboBoxKeyCompleter completer = new ComboBoxKeyCompleter();
        completer.install(atividadeComboBox);
        completer.install(assuntoComboBox);
        completer.install(conclusaoComboBox);
        completer.install(localidadeComboBox);
        completer.install(orgaoComboBox);

    }

    @FXML
    public void listaOrgao() throws IOException {
        Stage usuarioStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("listaOrgaos.fxml"));
        Parent root = loader.load();

        ListOrgaoController listOrgaoController = loader.getController();
        listOrgaoController.setPrincipalController(this);
        usuarioStage.setTitle("Lista Secretarias e ??rg??os");
        usuarioStage.setScene(new Scene(root));
        usuarioStage.setResizable(false);
        usuarioStage.show();
    }

    @FXML
    public void listaLocal() throws IOException {
        Stage usuarioStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("listaLocalidades.fxml"));
        Parent root = loader.load();

        ListLocalidadeController listLocalidadeController = loader.getController();
        listLocalidadeController.setPrincipalController(this);
        usuarioStage.setTitle("Lista Localidades");
        usuarioStage.setScene(new Scene(root));
        usuarioStage.setResizable(false);
        usuarioStage.show();
    }

    public void verifyUser(Usuario usr) {

        if (usrTextField.getText() == usr.getNome()) {
            JOptionPane.showMessageDialog(null,
                    "Voc?? n??o pode editar uma atividade que pertence a outro usu??rio");
        } else {
            localidadeComboBox.setVisibleRowCount(0);
        }
    }

    @FXML
    public void fecharJanela() {
        fecharButton.getScene().getWindow().hide();
    }

    ObservableList<String> tempo = FXCollections.observableArrayList(
            "10 minutos", "30 minutos", "1 hora", "3 horas", "5 horas", "7 horas", "Indefinido"
    );

    ObservableList<String> conclusao = FXCollections.observableArrayList(
            "Deferido", "Suspenso", "Diligenciado", "Parecer jur??dico", "Aguardando an??lise",
            "Aguardando compensa????o", "Compensado", "Criado", "Em an??lise", "Indeferido", "Inclu??do",
            "Alterado", "Bloqueado", "Encaminhado", "Emitido", "Conclu??do", "Em andamento"
    );

    public void corafi() {
        // Atividades e Assuntos CORAFI
        ObservableList<String> atvCoraf = FXCollections.observableArrayList(
                "APURA????O DE D??BITOS PREVIDENCI??RIOS(Contrib. Prev)",
                "APURA????O DE D??BITOS PREVIDENCI??RIOS(Benef. Prev)",
                "ATENDIMENTO DEMANDA OUTROS SETORES",
                "AUDITORIA"
        );

        ObservableList<String> assCorafi1 = FXCollections.observableArrayList(
                "Licen??a Sem Remunera????o LSR", "?? Disposi????o",
                "Mandato Eletivo", "Cartor??rio", "Outros"
        );

        ObservableList<String> assCorafi2 = FXCollections.observableArrayList(
                "Res??duos de Aposentadoria", "Res??duos de Pens??o",
                "Pens??es Irregulares", "Outros"
        );

        ObservableList<String> assCorafi3 = FXCollections.observableArrayList(
                "Averigua????o de d??bitos de contribui????o previdenci??ria",
                "Averigua????o de d??bitos de contribui????o previdenci??ria de instituidores de pens??o e pensionistas",
                "Suspender/cancelar/sobrestar cobran??a administrativa de contribui????o previdenci??ria por decis??o judicial",
                "Proceder Busca em Cart??rios por Bens de Notificados",
                "Proceder Busca de endere??os de Notificados",
                "Responder Dilig??ncias do TCE/SC",
                "Outros"
        );

        ObservableList<String> assCorafi4 = FXCollections.observableArrayList(
                "Auditoria em Benef??cios Previdenci??rios(Aposentadoria)",
                "Auditoria em Benef??cios Previdenci??rios(Pens??o)",
                "Auditoria em Contribui????es Previdenci??rias(Servidor)",
                "Auditoria em Contribui????es Previdenci??rias(Patronal)"
        );


        atividadeComboBox.setItems(atvCoraf);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "APURA????O DE D??BITOS PREVIDENCI??RIOS(Contrib. Prev)"){
                assuntoComboBox.setItems(assCorafi1);
            } else if (newValue == "APURA????O DE D??BITOS PREVIDENCI??RIOS(Benef. Prev)") {
                assuntoComboBox.setItems(assCorafi2);
            } else if (newValue == "ATENDIMENTO DEMANDA OUTROS SETORES") {
                assuntoComboBox.setItems(assCorafi3);
            } else if (newValue == "AUDITORIA") {
                assuntoComboBox.setItems(assCorafi4);
            }
        });

    }

    public void gecomp() {
        // Atividades e assuntos GECOMP
        ObservableList<String> atvGecomp = FXCollections.observableArrayList(
                "ANALISE DE COMP. (RO) IPREV--->RGPS/INSS",
                "ANALISE DE COMP. (RO) IPREV SOLICITANTE RGPS/INSS",
                "ANALISE DE COMP.(RO) DAS PENS??ES",
                "ANALISE DE COMP. (RI) SENDO O INSS SOLICITANTE",
                "CORRE????O DOS FORMUL??RIOS DE ANALISE(SIST. ANTIGO)",
                "DIGITALIZA????O DE DOC. F??SICOS AOS ANALISTAS E PERITOS DO COMPREV",
                "EMISS??O DE RELAT??RIOS",
                "REGISTRO DE AN??LISE DE DEMANDAS(SIST. RAFAEL)",
                "ANALISE DAS APOSENT. POR INVALIDEZ POR M??DICO PERITO NO SISTEMA COMPREV.",
                "ANALISE DE PENS??O FILHO MAIOR INV??LIDO POR M??DICO PERITO NO SISTEMA COMPREV"
        );

        ObservableList<String> assGecomp1 = FXCollections.observableArrayList(
                "Registro de an??lise e cadastro de demandas(IPREV--->RGPS/INSS)",
                "AConfec????o de documentos(DPT e Certid??es Espec??ficas)(IPREV--->RGPS/INSS)",
                "Diligencia para setorial(IPREV--->RGPS/INSS)",
                "Requerimento no COMPREV e envio das imagens de documentos(IPREV--->RGPS/INSS)",
                "Cadastro de DTC no COMPREV(IPREV--->RGPS/INSS)",
                "Solicita????o de processo ou documento ao setorial(IPREV--->RGPS/INSS)",
                "Encaminhamento de laudo m??dico das aposent. por invalidez aos peritos(IPREV--->RGPS/INSS)"
        );

        ObservableList<String> assGecomp2 = FXCollections.observableArrayList(
                "Registro de an??lise e cadastro de demandas(IPREV--->RPPS)",
                "Confec????o de documentos(DPT e Certid??es Espec??ficas)(IPREV--->RPPS)",
                "Diligencia para setorial(IPREV--->RPPS)",
                "Requerimento no COMPREV e envio das imagens de documentos(IPREV--->RPPS)",
                "Respostas de diligencias ao COMPREV(IPREV--->RPPS)",
                "Cadastro de DTC no COMPREV(IPREV--->RPPS)",
                "Solicita????o de processo ou documento ao setorial(IPREV--->RPPS)",
                "Encaminhamento de laudo m??dico das aposent. por invalidez aos peritos(IPREV--->RPPS)"
        );

        ObservableList<String> assGecomp3 = FXCollections.observableArrayList(
                "Requerimento comp.(RO) pens??o",
                "Envios de documentos comp.(RO) pens??o",
                "Acompanhamento e cadastro de DCB comp(RO) Pens??o",
                "Disponibiliza????o de laudos m??dicos filho maior invalido e dependentes"
        );

        ObservableList<String> assGecomp4 = FXCollections.observableArrayList(
                "Registro no COMPREV(Deferimento) ",
                "Registro no COMPREV(Indeferimento) ",
                "Registro no COMPREV(Exig??ncias)",
                "Registro no COMPREV(Suspens??o de Analise)",
                "Solicita????o de informa????es ao setorial do ex-servidor",
                "Solicita????o de ficha financeira a SEF"
        );

        ObservableList<String> assGecomp5 = FXCollections.observableArrayList(
                "Cont??beis", "Extra????o de ??bitos no SISOB",
                "Busca de inconsist??ncias no COMPREV",
                "Extra????o de novas exig??ncias",
                "Levantamento de prioridades(Processos antigos ou sobrestado)",
                "DOE (ESTADUAL/TJSC/ALESC/TCE)"
        );

        ObservableList<String> assGecomp6 = FXCollections.observableArrayList(
                "Sem assunto para esta atividade"
        );

        atividadeComboBox.setItems(atvGecomp);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "ANALISE DE COMP. (RO) IPREV--->RGPS/INSS"){
                assuntoComboBox.setItems(assGecomp1);
            } else if (newValue == "ANALISE DE COMP. (RO) IPREV SOLICITANTE RGPS/INSS") {
                assuntoComboBox.setItems(assGecomp2);
            } else if (newValue == "ANALISE DE COMP.(RO) DAS PENS??ES") {
                assuntoComboBox.setItems(assGecomp3);
            } else if (newValue == "ANALISE DE COMP. (RI) SENDO O INSS SOLICITANTE") {
                assuntoComboBox.setItems(assGecomp4);
            } else if (newValue == "CORRE????O DOS FORMUL??RIOS DE ANALISE(SIST. ANTIGO)") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "DIGITALIZA????O DE DOC. F??SICOS AOS ANALISTAS E PERITOS DO COMPREV") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "EMISS??O DE RELAT??RIOS") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "REGISTRO DE AN??LISE DE DEMANDAS(SIST. RAFAEL)") {
                assuntoComboBox.setItems(assGecomp6);
            } else if (newValue == "ANALISE DAS APOSENT. POR INVALIDEZ POR M??DICO PERITO NO SISTEMA COMPREV.") {
                assuntoComboBox.setItems(assGecomp6);
            } else if (newValue == "ANALISE DE PENS??O FILHO MAIOR INV??LIDO POR M??DICO PERITO NO SISTEMA COMPREV") {
                assuntoComboBox.setItems(assGecomp6);
            }
        });
    }

    public void gfpag() {
        ObservableList<String> atvGfpag = FXCollections.observableArrayList(
                "Confer??ncia final da folha de pagamento dos inativos",
                "Ressarcimento de remunera????o por ??bito de servidor inativo",
                "Afastar da folha de pagamento os servidores inativos que vieram a ??bito",
                "Receber e distribuir os processos com demandas judiciais e administrativas",
                "Elaborar relat??rio pens??o"
        );

        ObservableList<String> assGfpag1 = FXCollections.observableArrayList(
                "Gerar Resumo para  Compara????o entre os arquivos da Folha no SIGRH",
                "Gerar relat??rio de ??bitos", "Gerar relat??rio de aposentadoria do per??odo",
                "Conferir a folha do m??s anterior com a folha que est?? em desenvolvimento no m??s, gerando relat??rio espec??fico para cada rubrica",
                "Encaminhamento dos arquivos gerados da folha de pagamento, via SGPE, ao Banco do Brasil",
                "Verifica????o dos valores rejeitados por motivos de mudan??a de cadastro do servidor ou ??bito",
                "Acompanhar arquivos de folha suplementar se necess??rio",
                "Acompanhamento do cadastro e do recadastramento de servidores inativos",
                "Acompanhamento e levantamento de valores mensalmente bloqueados por falta de recadastramento",
                "Gerar o arquivo com relat??rio do banco mensal",
                "Gerar arquivos de pagamento por recadastramento, via arquivos banc??rios",
                "Busca de inativos que n??o se recadastraram no per??odo certo, para evitar o bloqueio e cancelamento do cadastro"
        );

        ObservableList<String> assGfpag2 = FXCollections.observableArrayList(
                "Analisar processos relativos ao aux??lio funeral",
                "Identificar os servidores falecidos que receberam valores indevidos",
                "Fazer o c??lculo dos valores pagos indevidamente em raz??o de ??bito de servidor inativo",
                "Encaminhar of??cio ao Banco do Brasil solicitando estorno de pagamento",
                "Solicitar c??pia da certid??o de ??bito junto aos cart??rios",
                "Providenciar abertura de processo"
        );

        ObservableList<String> assGfpag3 = FXCollections.observableArrayList(
                "Identificar e selecionar os servidores inativos, que vieram a ??bito, nos relat??rios enviados pelos cart??rios",
                "Acompanhar os pedidos de pens??o para identificar servidores aposentados que faleceram",
                "Encerrar (Afastar), no SIGRH, os servidores inativos que faleceram (Cart??rios e SISOB)"
        );

        ObservableList<String> assGfpag4 = FXCollections.observableArrayList(
                "Providenciar a solu????o para as demandas judiciais",
                "Providenciar a solu????o para as demandas administrativas",
                "Calcular o redutor da aposentadoria nos casos de ac??mulo com pens??o",
                "Lan??ar o redutor da aposentadoria no SIGRH",
                "Comunicar o servidor acerca do redutor de sua aposentadoria",
                "Comunicar a ger??ncia de compensa????o previdenci??ria as aposentadorias que incidiu o redutor"
        );


        ObservableList<String> assGfpag5 = FXCollections.observableArrayList(
                "Calcular os valores de contribui????o previdenci??ria e/ou de imposto de renda a serem restitu??dos a(o) pensionista",
                "Lan??ar no SIGRH os valores da contribui????o previdenci??ria e/ou do imposto de renda a serem restitu??dos a(o) pensionista"
        );

        ObservableList<String> assGfpag6 = FXCollections.observableArrayList(
                "Calcular o valor da pens??o relativa ao IPALESC",
                "Elaborar o relat??rio com o resumo da folha da pens??o paga pelos Poderes",
                "Gerar o Relat??rio 41",
                "Encaminhar email ?? Fazenda, ?? Controladoria e, ?? Diretoria de Previd??ncia, ?? Presid??ncia, ?? GEAFC disponibilizando os relat??rios"
        );

        atividadeComboBox.setItems(atvGfpag);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Confer??ncia final da folha de pagamento dos inativos"){
                assuntoComboBox.setItems(assGfpag1);
            } else if (newValue == "Ressarcimento de remunera????o por ??bito de servidor inativo") {
                assuntoComboBox.setItems(assGfpag2);
            } else if (newValue == "Afastar da folha de pagamento os servidores inativos que vieram a ??bito") {
                assuntoComboBox.setItems(assGfpag3);
            } else if (newValue == "Receber e distribuir os processos com demandas judiciais e administrativas") {
                assuntoComboBox.setItems(assGfpag4);
            } else if (newValue == "Restituir valores de contribui????o previdenci??ria e de IRRF aprovados pela Per??cia pensionistas") {
                assuntoComboBox.setItems(assGfpag5);
            } else if (newValue == "Elaborar relat??rio pens??o") {
                assuntoComboBox.setItems(assGfpag6);
            }
        });

    }

    public void getig() {
        ObservableList<String> atvGetig = FXCollections.observableArrayList(
                "Suporte em software", "Suporte em hardware",
                "Cadastramento e manuten????o de usu??rios", "Desenvolvimento", "Servi??os administrativos",
                "Outros"
        );

        ObservableList<String> assGetig1 = FXCollections.observableArrayList(
                "Sistema operacional", "Pacote Office", "Sistema legado", "SGPE",
                "SIGRH", "SICOP", "SAP", "SOS - Chamados", "Navegadores", "Certificado Digital",
                "Planilhas", "Power BI", "Boa vista", "Arquivos acervo"
        );

        ObservableList<String> assGetig2 = FXCollections.observableArrayList(
                "Computadores", "Monitores", "Teclados", "Mouse", "Estabilizadores",
                "Cabeamento", "Switch", "Roteadores", "Telefones IP", "Projetores", "Som",
                "TV", "Cameras", "Catraca", "Ponto de rede", "Rede el??trica"
        );

        ObservableList<String> assGetig3 = FXCollections.observableArrayList(
                "E-Mail", "Rede Iprev", "Senhas de rede", "Pastas de rede", "Catraca",
                "CIASC ETERNAL", "CIASC(PROD)", "Ramais", "Outro sistema"
        );

        ObservableList<String> assGetig4 = FXCollections.observableArrayList(
                "Sistemas", "Bancos de dados", "Scripts", "API'S", "Ferramentas anal??ticas",
                "Query Boa Vista", "Processos de ETL", "Dashboards", "Migra????o entre sistemas",
                "Planilhas e Formul??rios"
        );

        ObservableList<String> assGetig5 = FXCollections.observableArrayList(
                "Certifica????o de notas fiscais", "Coordenar Dev. e aquisi????o de sistemas",
                "Levantamento de or??amento", "Elabora????o de contratos", "Participa????o em reuni??es"
        );

        ObservableList<String> assGetig6 = FXCollections.observableArrayList(
                "Recupera????o de arquivos Acervo"
        );

        atividadeComboBox.setItems(atvGetig);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Suporte em software"){
                assuntoComboBox.setItems(assGetig1);
            } else if (newValue == "Suporte em hardware") {
                assuntoComboBox.setItems(assGetig2);
            } else if (newValue == "Cadastramento e manuten????o de usu??rios") {
                assuntoComboBox.setItems(assGetig3);
            } else if (newValue == "Desenvolvimento") {
                assuntoComboBox.setItems(assGetig4);
            } else if (newValue == "Servi??os administrativos") {
                assuntoComboBox.setItems(assGetig5);
            } else if (newValue == "Outros") {
                assuntoComboBox.setItems(assGetig6);
            }
        });
    }

    public void presidencia() {
        ObservableList<String> atvPres = FXCollections.observableArrayList(
                "An??lise de processo"
        );

        ObservableList<String> assPres = FXCollections.observableArrayList(
                "Pens??o", "Averba????o", "Retifica????o", "Aposentadoria", "Habitacional",
                "CTC", "Pareceres", "Portaria", "Levantamento de d??bito", "Valores pendentes",
                "Restitui????o de Contribui????o Previdenci??ria / Devolu????o de valores", "Encaminhamentos"
        );

        atividadeComboBox.setItems(atvPres);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "An??lise de processo"){
                assuntoComboBox.setItems(assPres);
            }
        });
    }

    public void gerac() {
        ObservableList<String> atvGerac = FXCollections.observableArrayList(
                "TRIAR DEMANDAS RECEBIDAS",
                "ANALISAR PROCESSO JUDICIAL PARA ELABORA????O DE C??LCULO",
                "SOLICITAR ESCLARECIMENTOS",
                "SOLICITA????O DE DOCUMENTOS COMPLEMENTARES",
                "REALIZAR C??LCULOS",
                "EMITIR PARECER",
                "OUTRAS DEMANDAS"
        );

        ObservableList<String> assGerac1 = FXCollections.observableArrayList(
                "VERIFICAR E ORGANIZAR AS DEMANDAS ENCAMINHADAS AO SETOR (ASSUNTO/ COMPLEXIDADE)",
                "DISTRIBUIR PROCESSOS",
                "ACOMPANHAR PRAZOS"
        );

        ObservableList<String> assGerac2 = FXCollections.observableArrayList(
                "VERIFICAR ASSUNTO E PRAZO DE ENTREGA PARA ORGANIZAR E PLANEJAR FLUXO DE TRABALHO",
                "ANALISAR SOLICITA????O DO PROCURADOR DO IPREV, PETI????O JUDICIAL E RUBRICAS SOLICITADAS",
                "CONSULTAR SIGRH OU RH SETORIAL E A FICHA FINANCEIRA  DO SERVIDOR",
                "PESQUISAR SE PARTE AUTORA J?? RECEBEU ADM  (SIGRH E SGPE)",
                "PESQUISAR SE PARTE AUTORA POSSUI LITISPEND??NCIAS (PGNET e TJSC)",
                "SELECIONAR DOCUMENTOS PARA COMPROVAR JUDICIALMENTE SE A PARTE AUTORA J?? RECEBEU RUBRICAS"
        );

        ObservableList<String> assGerac3 = FXCollections.observableArrayList(
                "SOLICITAR (SE NECESS??RIO) ESCLARECIMENTOS AO PROCURADOR RESPONS??VEL (GECOJ)",
                "SOLICITAR (SE NECESS??RIO)  INFORMA????ES PARA OUTROS SETORES DO IPREV",
                "SOLICITAR (SE NECESS??RIO)  INFORMA????ES AOS RH SETORIAIS"
        );

        ObservableList<String> assGerac4 = FXCollections.observableArrayList(
                "SOLICITAR (SE NECESS??RIO) CERTID??O (SE VIVO FOSSE) PARA C??LCULO DE PENS??O",
                "SOLICITAR  (SE NECESS??RIO) EXTRATO DE VALORES RECEBIDOS / PAGOS (DE PREFEITURAS)",
                "SOLICITAR (SE NECESS??RIO)  CONTRACHEQUES, FICHA FINANCEIRA E/OU FUNCIONAL DE ??RG??OS QUE N??O EST??O NO SIGRH",
                "SOLICITAR OUTROS DOCUMENTOS CONFORME NECESSIDADE ESPEC??FICA DOS PROCESSOS"
        );

        ObservableList<String> assGerac5 = FXCollections.observableArrayList(
                "CONFERIR  C??LCULO DA PARTE AUTORA",
                "REALIZAR C??LCULOS",
                "VERIFICAR SE DEVE HAVER A RETEN????O DA CONTRIBUI????O PREVIDENCI??RIA",
                "ELABORAR FUNDAMENTA????O PARA A IMPUGNA????O DE VALORES  (PARECER T??CNICO)",
                "ANEXAR (SE NECESS??RIO) DOCUMENTA????O COMPROBAT??RIA",
                "ANEXAR  PROCESSO AO PGNET E/OU SGPE"
        );

        ObservableList<String> assGerac6 = FXCollections.observableArrayList(
                "CONFERIR SE PLANILHA DE C??LCULO E DOCUMENTOS COMPLEMENTARES FORAM CORRETAMENTE ANEXADOS AO PROCESSO",
                "PRESTAR INFORMA????ES RELEVANTES QUE N??O CONSTAM NA PLANILHA DE C??LCULO",
                "EMITIR PARECER DE ENCAMINHAMENTO ",
                "ENVIAR O PROCESSO AO SETOR DE ORIGEM"
        );

        ObservableList<String> assGerac7 = FXCollections.observableArrayList(
                "ACOMPANHAR FLUXO DE TRABALHO E PLANEJAR ATIVIDADES",
                "ACOMPANHAR E DOCUMENTAR TRABALHO DO SETOR (RESULTADOS, VALORES, ETC)",
                "CADASTRAR DADOS EM SISTEMAS PARA ACOMPANHAMENTO DE RESULTADOS OU REGISTRO DE TRABALHO",
                "MANUALIZAR ROTINAS E ATIVIDADES DO SETOR",
                "ACOMPANHAR INFORMATIZA????O DO SETOR",
                "PARTICIPAR DE REUNI??ES E/OU TREINAMENTOS CONFORME DEMANDA DOS GESTORES",
                "ANALISAR E/OU ELABORAR RELAT??RIOS, PARECERES, PLANILHAS OU OUTROS DOCUMENTOS SOLICITADOS PELOS GESTORES",
                "ELABORAR/LEVANTAR NECESSIDADES/DADOS E/OU COMPILAR INFORMA????ES SOLICITADAS PELOS GESTORES"
        );

        atividadeComboBox.setItems(atvGerac);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "TRIAR DEMANDAS RECEBIDAS"){
                assuntoComboBox.setItems(assGerac1);
            } else if (newValue == "ANALISAR PROCESSO JUDICIAL PARA ELABORA????O DE C??LCULO"){
                assuntoComboBox.setItems(assGerac2);
            } else if (newValue == "SOLICITAR ESCLARECIMENTOS"){
                assuntoComboBox.setItems(assGerac3);
            } else if (newValue == "SOLICITA????O DE DOCUMENTOS COMPLEMENTARES"){
                assuntoComboBox.setItems(assGerac4);
            } else if (newValue == "REALIZAR C??LCULOS"){
                assuntoComboBox.setItems(assGerac5);
            } else if (newValue == "EMITIR PARECER"){
                assuntoComboBox.setItems(assGerac6);
            } else if (newValue == "OUTRAS DEMANDAS"){
                assuntoComboBox.setItems(assGerac7);
            }
        });
    }

    public void gerat() {
        ObservableList<String> atvGerat = FXCollections.observableArrayList(
                "RECEBER E RESPONDER E-MAILS, WHATSAAP E TELEF??NICO",
                "RECEBER E-MAILS PARA AUTUA????O PROCESSOS",
                "ATENDIMENTO PRESENCIAL",
                "OUTRAS DEMANDAS"
        );

        ObservableList<String> assGerat1 = FXCollections.observableArrayList(
                "D??VIDAS SOBRE RECADASTRAMENTO",
                "EMISS??O DE SENHA PORTAL SERVIDOR - PENSIONISTA",
                "ORIENTA????O SOBRE DOCUMENTOS NECESS??RIOS PEDIDO DE PENS??O POR MORTE",
                "ORIENTA????O SOBRE DOCUMENTOS NECESS??RIOS PEDIDO DE PROCESSOS DE VALORES PENDENTES, " +
                "REAJUSTE DE PENS??O, COMUNICA????O DE ??BITO, ISEN????O DE IMPOSTO DE RENDA E DEDU????O PREVID??NCIARIA, REATIVA????O PENS??O",
                "D??VIDAS DIVERSAS - PENS??O , APOSENTADORIA, CERTID??O DE TEMPO DE CONTRIBUI????O, AVERBA????O",
                "ORIENTA????O SOBRE SOLICITA????O C??PIA PROCESSOS",
                "ORIENTA????O COMO SOLICITAR DECLARA????O DE N??O RECEBIMENTO DE BENEF??CIO PREVIDENCI??RIO"
        );

        ObservableList<String> assGerat2 = FXCollections.observableArrayList(
                "DISTRIBUIR E-MAILS PARA AUTUA????O PROCESSOS PENS??O INCIAL",
                "DISTRIBUIR E-MAILS PARA AUTUA????O PROCESSOS VALORES PENDENTES , REAJUSTE DE PENS??ES E INSEN????O DE IMPOSTO DE RENDA E DEDU????O PREVIDENCI??RIA - PENSIONISTA"
        );

        ObservableList<String> assGerat3 = FXCollections.observableArrayList(
                "AGENDAMENTO  - AUTUA????O PROCESSO PENS??O INICIAL "
        );

        ObservableList<String> assGerat4 = FXCollections.observableArrayList(
                "ACOMPANHAR FLUXO DE TRABALHO E PLANEJAR ATIVIDADES",
                "ACOMPANHAR E DOCUMENTAR TRABALHO DO SETOR (RESULTADOS, VALORES, ETC)",
                "CADASTRAR DADOS EM SISTEMAS PARA ACOMPANHAMENTO DE RESULTADOS OU REGISTRO DE TRABALHO",
                "MANUALIZAR ROTINAS E ATIVIDADES DO SETOR",
                "ACOMPANHAR INFORMATIZA????O DO SETOR",
                "PARTICIPAR DE REUNI??ES E/OU TREINAMENTOS CONFORME DEMANDA DOS GESTORES",
                "ANALISAR E/OU ELABORAR RELAT??RIOS, PARECERES, PLANILHAS OU OUTROS DOCUMENTOS SOLICITADOS PELOS GESTORES",
                "ELABORAR/LEVANTAR NECESSIDADES/DADOS E/OU COMPILAR INFORMA????ES SOLICITADAS PELOS GESTORES"
        );

        atividadeComboBox.setItems(atvGerat);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "RECEBER E RESPONDER E-MAILS, WHATSAAP E TELEF??NICO"){
                assuntoComboBox.setItems(assGerat1);
            } else if (newValue == "RECEBER E-MAILS PARA AUTUA????O PROCESSOS"){
                assuntoComboBox.setItems(assGerat2);
            } else if (newValue == "ATENDIMENTO PRESENCIAL"){
                assuntoComboBox.setItems(assGerat3);
            } else if (newValue == "OUTRAS DEMANDAS"){
                assuntoComboBox.setItems(assGerat4);
            }
        });
    }

    public void gecad() {
        ObservableList<String> atvGecad = FXCollections.observableArrayList(
                "Executar, acompanhar, operacionalizar e controlar todas as atividades da ger??ncia",
                "Emitir parecer jur??dico, solicitar dilig??ncias, prestar informa????es t??cnicas de mat??rias jur??dicas afetas a sua compet??ncia, em processos de interesse do IPREV",
                "Realizar estudo social com a finalidade de elabora????o do Relat??rio Social",
                "Emitir parecer jur??dico, solicitar dilig??ncias, prestar informa????es t??cnicas de " +
                "mat??rias jur??dicas que demandam a elabora????o de manifesta????o orientativa, que servir?? de base para aplica????o em diversas demandas que envolvam a mesma mat??ria analisada"
        );

        ObservableList<String> assGecad1 = FXCollections.observableArrayList(
                "Receber cita????es e intima????es, na aus??ncia do Gerente do Contencioso Judicial",
                "Coordenar e supervisionar as atividades do servi??o social, dilig??ncia, elabora????o de laudo social e " +
                        "per??cias t??cnico-administrativas na concess??o e revis??o do benef??cio previdenci??rio",
                "Emitir parecer jur??dico, solicitar dilig??ncias, prestar informa????es t??cnicas de mat??rias afetas a sua compet??ncia, " +
                        "despachar encaminhamento de processos administrativos de sua compet??ncia",
                "Analisar pareceres, dilig??ncias e informa????es produzidas pelos advogados, assistentes jur??dicos e t??cnicos da Ger??ncia",
                "Responder emails direcionados ?? Ger??ncia"
        );

        ObservableList<String> assGecad2 = FXCollections.observableArrayList(
                "Analisar processos para a concess??o e revis??o de benef??cios previdenci??rios, " +
                        "de licita????o, projetos normativos, dentre outros determinados pela Ger??ncia do Contencioso Administrativo, " +
                        "emitindo parecer jur??dico, dilig??ncias e informa????es"
        );

        ObservableList<String> assGecad3 = FXCollections.observableArrayList(
                "Leitura e an??lise do conte??do processual/documental",
                "Entrevistas sociais (remoto/presencial)",
                "Visitas domiciliares",
                "Visitas institucionais",
                "Contatos telef??nicos/por e-mail/whatsapp",
                "Emiss??o de Relat??rio Social ap??s a constata????o da situa????o",
                "Inserir o Relat??rio Social e documentos no SGPE",
                "Registro e acompanhamento da planilha de processos no google drive",
                "Verifica????o do SGPE e da planilha dos processos no google drive",
                "Verifica????o/leitura do e-mail institucional e respostas ??s mensagens, caso necess??rio",
                "Grava????o das entrevistas por ??udio/chamada de v??deo, quando realizadas no trabalho remoto",
                "Reuni??o de equipe, com a gest??o e advogado/a vinculado ao processo a fim de avaliar a metodologia de trabalho, demanda de processos, entre outros",
                "Consulta bibliogr??fica para a realiza????o de documentos escritos",
                "Prestar orienta????o para atendimento da rede de sa??de e ou socioassistencial do munic??pio onde o/a requerente reside",
                "Pesquisa Social"
        );

        ObservableList<String> assGecad4 = FXCollections.observableArrayList(
                "Analisar processos para a concess??o e revis??o de benef??cios previdenci??rios, de licita????o, projetos normativos, " +
                        "dentre outros determinados pela Ger??ncia do Contencioso Administrativo, emitindo parecer jur??dico, dilig??ncias e informa????es"
        );

        atividadeComboBox.setItems(atvGecad);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Executar, acompanhar, operacionalizar e controlar todas as atividades da ger??ncia"){
                assuntoComboBox.setItems(assGecad1);
            } else if (newValue == "Emitir parecer jur??dico, solicitar dilig??ncias, prestar informa????es t??cnicas de " +
                    "mat??rias jur??dicas afetas a sua compet??ncia, em processos de interesse do IPREV"){
                assuntoComboBox.setItems(assGecad2);
            } else if (newValue == "Realizar estudo social com a finalidade de elabora????o do Relat??rio Social"){
                assuntoComboBox.setItems(assGecad3);
            } else if (newValue == "Emitir parecer jur??dico, solicitar dilig??ncias, prestar informa????es t??cnicas de mat??rias jur??dicas que demandam a elabora????o de manifesta????o orientativa, " +
                    "que servir?? de base para aplica????o em diversas demandas que envolvam a mesma mat??ria analisada"){
                assuntoComboBox.setItems(assGecad4);
            }
        });


    }

    public void gerin() {
        ObservableList<String> atvGerin = FXCollections.observableArrayList(
                "ANALISE DE PROCESSO APOSENTADORIA",
                "INATIVA????O DO SERVIDOR NO SIGRH",
                "ANALISE DE AVERBA????O",
                "INCLUS??O DE AVERBA????O NO SIGRH",
                "AN??LISE DE REVIS??O DE PROVENTOS",
                "DEMANDAS JUDICIAIS",
                "AN??LISE DE REQUERIMENTO DE CTC",
                "EMISS??O DE RELAT??RIO SAL??RIOS DE CONTRIBUI????O",
                "EMISS??O DE INFORMA????O DE CONTRIBUI????O DE PER??ODOS LTIP",
                "AN??LISE DE PEDIDO DE ISEN????O DE CONTRIB PREVIDENCI??RIA",
                "CONFEC????O DE PORTARIAS",
                "CONFER??NCIA DAS PUBLICA????ES",
                "COMUNICA????O DE PUBLICA????ES",
                "DILIG??NCIAS DO TCE",
                "AN??LISE DE PEDIDO DE APOSENTADORIA CARTOR??RIOS"
        );

        ObservableList<String> assGerin1 = FXCollections.observableArrayList(
                "ANALISE DOCUMENTAL",
                "VERIFICA????O DE REQUISITOS",
                "VERIFICA????O DA INCORPORA????O DE RUBRICAS NOS PROVENTOS DE APOSENTADORIA",
                "ENCAMINHAMENTO DE DILIG??NCIAS",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin2 = FXCollections.observableArrayList(
                "CONFER??NCIA  DOS ATOS DE APOSENTADORIA PUBLICADOS",
                "INCLUS??O DA MODALIDADE DE APOSENTADORIA NO SISTEMA",
                "INCLUS??O DAS RUBRICAS DO INATIVO",
                "EMISS??O E CONFER??NCIA CONTRACHEQUE INATIVO"
        );

        ObservableList<String> assGerin3 = FXCollections.observableArrayList(
                "AN??LISE DE CTC/DTC",
                "CONFER??NCIA DOS PER??ODOS SOLICITADOS",
                "ENCAMINHAMENTO DE DILIG??NCIAS",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin4 = FXCollections.observableArrayList(
                "INCLUS??O DAS AVERBA????ES PUBLICADAS NO SIGRH",
                "INCLUS??O DOS SAL??RIOS DE CONTRIBUI????O A PARTIR DE JULHO/1994"
        );

        ObservableList<String> assGerin5 = FXCollections.observableArrayList(
                "VERIFICA????O DO PEDIDO",
                "AN??LISE DA DOCUMENTA????O",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin6 = FXCollections.observableArrayList(
                "IMPLANTA????O DE DEMANDAS JUDICIAIS",
                "SUBSIDIOS PARA DEFESAS JUDICIAIS"
        );

        ObservableList<String> assGerin7 = FXCollections.observableArrayList(
                "AN??LISE DOCUMENTAL",
                "EMISS??O DE CTC"
        );

        ObservableList<String> assGerin8 = FXCollections.observableArrayList(
                "EMISS??O DE RELAT??RIOS A PARTIR JULHO/1994 PARA ANEXAR AS CTC"
        );

        ObservableList<String> assGerin9 = FXCollections.observableArrayList(
                "INFORMA????O UTILIZADA PARA COMPROVAR PER??ODOS AVERBADOS"
        );

        ObservableList<String> assGerin10 = FXCollections.observableArrayList(
                "AN??LISE LAUDO PERICIAL",
                "DESPACHO DEFERIMENTO/INDEFERIMENTO DO PEDIDO",
                "EMISS??O DE PLANILHA DE C??LCULO DE RESTITUI????O DAS CONTRIBUI????ES PREV",
                "IMPLANTA????O DOS RETROATIVOS NO SIGRH",
                "OFICIAR  OS  DEMAIS PODERES"
        );

        ObservableList<String> assGerin11 = FXCollections.observableArrayList(
                "EMISS??O DE ATOS DE APOSENTADORIA E AVERBA????ES"
        );

        ObservableList<String> assGerin12 = FXCollections.observableArrayList(
                "LIBERA????O PARA PUBLICA????O NO DOE E CONFER??NCIA DAS PUBLICA????ES"
        );

        ObservableList<String> assGerin13 = FXCollections.observableArrayList(
                "COMUNICAR O SERVIDOR INTERESSADO QUANTO A APUBLICA????O DA APOSENTADORIA"
        );

        ObservableList<String> assGerin14 = FXCollections.observableArrayList(
                "ANALISAR O SOLICITADO EM AUDI??NCIA PELO TCE",
                "ENCAMINHAR RESPOSTA ?? GEDIL",
                "SOLICITAR RETIFICA????O DE ATOS DE APOSENTADORIA VISANDO HOMOLOGA????O PELO TCE"
        );

        ObservableList<String> assGerin15 = FXCollections.observableArrayList(
                "ABERTURA PROCESSO/RECEBIMENTO DO PEDIDO",
                "ENCAMINHAMENTO GEAFC PARA RELAT??RIO",
                "VERIFICA????O DO TEMPO DE CONTRIBUI????O",
                "ENCAMINHAMENTO GECAD VERIFICA????O DE DECIS??ES JUDICAIS",
                "RETORNO DE INFORMA????ES AO TJSC"
        );

        atividadeComboBox.setItems(atvGerin);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "ANALISE DE PROCESSO APOSENTADORIA"){
                assuntoComboBox.setItems(assGerin1);
            } else if (newValue == "INATIVA????O DO SERVIDOR NO SIGRH"){
                assuntoComboBox.setItems(assGerin2);
            } else if (newValue == "ANALISE DE AVERBA????O"){
                assuntoComboBox.setItems(assGerin3);
            } else if (newValue == "INCLUS??O DE AVERBA????O NO SIGRH"){
                assuntoComboBox.setItems(assGerin4);
            } else if (newValue == "AN??LISE DE REVIS??O DE PROVENTOS"){
                assuntoComboBox.setItems(assGerin5);
            } else if (newValue == "DEMANDAS JUDICIAIS"){
                assuntoComboBox.setItems(assGerin6);
            } else if (newValue == "AN??LISE DE REQUERIMENTO DE CTC"){
                assuntoComboBox.setItems(assGerin7);
            } else if (newValue == "EMISS??O DE RELAT??RIO SAL??RIOS DE CONTRIBUI????O"){
                assuntoComboBox.setItems(assGerin8);
            } else if (newValue == "EMISS??O DE INFORMA????O DE CONTRIBUI????O DE PER??ODOS LTIP"){
                assuntoComboBox.setItems(assGerin9);
            } else if (newValue == "AN??LISE DE PEDIDO DE ISEN????O DE CONTRIB PREVIDENCI??RIA"){
                assuntoComboBox.setItems(assGerin10);
            } else if (newValue == "CONFEC????O DE PORTARIAS"){
                assuntoComboBox.setItems(assGerin11);
            } else if (newValue == "CONFER??NCIA DAS PUBLICA????ES"){
                assuntoComboBox.setItems(assGerin12);
            } else if (newValue == "COMUNICA????O DE PUBLICA????ES"){
                assuntoComboBox.setItems(assGerin13);
            } else if (newValue == "DILIG??NCIAS DO TCE"){
                assuntoComboBox.setItems(assGerin14);
            } else if (newValue == "AN??LISE DE PEDIDO DE APOSENTADORIA CARTOR??RIOS"){
                assuntoComboBox.setItems(assGerin15);
            }
        });


    }

    public void geafc() {

    }

    public void geapo() {
        ObservableList<String> atvGeapo = FXCollections.observableArrayList(
                "SEADC - Setor de Administra????o de Contratos",
                "SECOD - Setor de Compras Diretas",
                "SEDIA - Setor de Di??rias",
                "SEPAT - Setor de Patrim??nio",
                "SEBEN - Setor de Bens Previdenci??rios"
        );

        ObservableList<String> assGeapo1 = FXCollections.observableArrayList(
                "Autua????o processos de pagamentos mensais",
                "Controle contratos de presta????o de servi??os",
                "Execu????o e encaminhamento pagamentos mensais",
                "Rotinas no SIGEF (Certifica????o)",
                "Rotinas no SIGEF (Demais Assuntos)",
                "Emiss??o de corrspond??ncias",
                "Acompanhamento e controle servi??os terceirizados",
                "Despachos em processo/prestar informa????es"
        );

        ObservableList<String> assGeapo2 = FXCollections.observableArrayList(
                "Autua????o processos de compra direta",
                "Aquisi????o de meterial permente e consumo",
                "Rotinas no SIGEF (Certifica????o)",
                "Encaminhamento pagamento de compras diretas",
                "Despachos em processo/prestar informa????es",
                "Emiss??o de corrspond??ncias",
                "Rotinas no SIGEF (Demais Assuntos)",
                "Cadastro e baixa de materiais no Almoxarifado",
                "Atividades do Almoxarigado",
                "Manuten????o de bens m??veis"
        );

        ObservableList<String> assGeapo3 = FXCollections.observableArrayList(
                "Autua????o processos de pagamento de di??rias",
                "Rotinas no SIGEF (Certifica????o)",
                "An??lise e baixa de presta????o de contas",
                "Publica????o de relat??rios no DOE",
                "Emiss??o de Ordem de Tr??fego no GVE",
                "Controle e acompanhamento dos servi??os de transporte"
        );

        ObservableList<String> assGeapo4 = FXCollections.observableArrayList(
                "Autua????o processos de baixa e acertos patrimoniais",
                "Emiss??o de relat??rios",
                "Controle e confer??nciade bens",
                "Invent??rio anual de bens m??veis e almoxarifado"
        );

        ObservableList<String> assGeapo5 = FXCollections.observableArrayList(
                "Autua????o de processos relativos aos bens previdenci??rios",
                "Manuten????o de bens im??veis",
                "Rotinas no SIGEF (Demais Assuntos)",
                "Controle e pagamento de taxas e tributos",
                "Cobran??a e recebimentos de valores de permiss??o de uso dos im??veis",
                "Emiss??o de relat??rios/envio de informa????es",
                "Despachos em processo/prestar informa????es"
        );

        atividadeComboBox.setItems(atvGeapo);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "SEADC - Setor de Administra????o de Contratos"){
                assuntoComboBox.setItems(assGeapo1);
            } else if (newValue == "SECOD - Setor de Compras Diretas"){
                assuntoComboBox.setItems(assGeapo2);
            } else if (newValue == "SEDIA - Setor de Di??rias"){
                assuntoComboBox.setItems(assGeapo3);
            } else if (newValue == "SEPAT - Setor de Patrim??nio"){
                assuntoComboBox.setItems(assGeapo4);
            } else if (newValue == "SEBEN - Setor de Bens Previdenci??rios") {
                assuntoComboBox.setItems(assGeapo5);
            }

        });
    }

    public void gepen() {
        ObservableList<String> atvGepen = FXCollections.observableArrayList(
                "SETOR DE ATENDIMENTO - SEATE",
                "SETOR DE AN??LISE DE PROCESSOS DE PEDIDO DE PENS??O - SEPEP",
                "SETOR DE INCLUS??O E PAGAMENTO - SEIPA",
                "SETOR DE ATOS LEGAIS - SEATL",
                "SETOR DE TRANSMISS??O AO TCE - SETRA",
                "SETOR DE LEVANTAMENTO DE VALORES PENDENTES - SEVAP",
                "SETOR DE DEMANDAS JUDICIAIS - SEDJU",
                "SETOR DE REC??LCULO - SEREC",
                "OUTRAS DEMANDAS"
        );

        ObservableList<String> assGepen1 = FXCollections.observableArrayList(
                "TRIAGEM DAS DEMANDAS RECEBIDAS PELO SGP-E E POR E-MAIL",
                "AN??LISE DA DEMANDA RECEBIDA",
                "REGISTRO EM PLANILHAS ESPEC??FICAS",
                "ENCAMINHAMENTO AO SETOR RESPONS??VEL",
                "RESPOSTA AOS E-MAILS RECEBIDOS E/OU ENCAMINHAMENTO AO SETOR RESPONS??VEL, QUANDO FOR O CASO",
                "AN??LISE, TRATAMENTO E ENCAMINHAMENTO DOS PROCESSOS DE PEDIDO DE ISEN????O DE IMPOSTO DE RENDA E CONTRIBUI????O PREVIDENCI??RIA",
                "ENCAMINHAMENTO ?? GFPAG DOS PROCESSOS DE PEDIDOS DE ISEN????O PARA REALIZA????O C??LCULO E RESTITUI????O AO PENSIONISTA",
                "EMISS??O DE SENHA PARA ACESSO AO CONTRACHEQUE PARA OS NOVOS PENSIONISTAS",
                "ENVIO DE E-MAIL AOS NOVOS PENSIONISTAS COM SENHA E INFORMA????ES PARA ACESSO AO CONTRACHEQUE",
                "ALTERA????O DOS DADOS BANC??RIOS QUANDO SOLICITADO PELO PENSIONISTA",
                "CONFER??NCIA DOS RELAT??RIOS DE ??BITOS ENVIADOS PELOS CART??RIOS PARA IDENTIFCA????O DE PENSIONISTAS FALECIDOS",
                "SOLICITA????O DE C??PIA DE CERTID??O DE ??BITO AO CART??RIO, QUANDO NECESS??RIO",
                "ENVIO DOS TERMOS DE ADES??O AO SC SA??DE DOS NOVOS PENSIONISTAS",
                "ELABORA????O DE DECLARA????ES DIVERSAS POR SOLICITA????O DOS PENSIONISTAS E HERDEIROS"
        );

        ObservableList<String> assGepen2 = FXCollections.observableArrayList(
                "AN??LISE DOS PROCESSOS DE PEDIDOS DE PENS??O",
                "VERIFICA????O SE H?? OUTROS DEPENDENTES DO INSTITUIDOR",
                "ENCAMINHAMENTO ?? PER??CIA M??DICA NOS CASOS DE PEDIDO DE PENS??O POR INVALIDEZ",
                "SOLICITA????O DE DILIG??NCIAS ?? GERAT PARA A SOLU????O DE PEND??NCIAS NOS PROCESSOS DE PENS??O",
                "ELABORA????O DE DESPACHO E ENCAMINHAMENTO ?? GECAD QUANDO FOR NECESS??RIO",
                "ELBORA????O DE DEMONSTRATIVO FINANCEIRO DA PENS??O E TERMO DE REGULARIDADE",
                "ENCAMINHAMENTO AO GABINETE PARA AN??LISE",
                "ELABORA????O DE OF??CIOS PARA COMUNICAR O INDEFERIMENTO DA PENS??O, QUANDO FOR O CASO"
        );

        ObservableList<String> assGepen3 = FXCollections.observableArrayList(
                "CADASTRO DO NOVO PENSIONISTA NO SIGRH",
                "IMPLANTA????O DA PENS??O NO SIGRH",
                "APLICA????O DO REDUTOR NA PENS??O, QUANDO H?? AC??MULO DE BENEF??CIOS E O VALOR DA PENS??O ?? MENOR",
                "C??LCULO E LAN??AMENTO DOS VALORES RETROATIVOS DEVIDOS AO PENSIONISTA",
                "CONFER??NCIA DOS VALORES DO DEMONSTRATIVO FINANCEIRO E DO CONTRACHEQUE",
                "REGISTRO EM PLANILHA ESPEC??FICA DOS PROCESSOS DE PENS??O IMPLANTADOS NO M??S",
                "ENVIO DE E-MAIL AO NOVO PENSIONISTA COMUNICANDO O DEFERIMENTO DA PENS??O",
                "ENCAMINHAMENTO DE PROCESSOS ?? GEAFC PARA APLICAR REDUTOR NA APOSENTADORIA DO IPREV, NOS CASOS EM QUE H?? AC??MULO DE BENEF??CIOS E O VALOR DA APOSENTADORIA ?? MENOR",
                "ENCAMINHAMENTO DE PROCESSOS ?? SEATL PARA ELABORA????O DA PORTARIA"
        );

        ObservableList<String> assGepen4 = FXCollections.observableArrayList(
                "ELABORA????O E ENVIO DE OF??CIOS PARA INFORMAR AO RGPS E/OU AOS RPPS'S QUANDO H?? AC??MULO DE BENEF??CIOS",
                "ELABORA????O DE PORTARIA DAS PENS??ES CONCEDIDAS",
                "ENCAMINHAMENTO DO PROCESSO ?? UCI PARA AN??LISE",
                "VERIFICA????O E PROVID??NCIAS DAS DILIG??NCIAS DA UCI, QUANDO FOR O CASO",
                "PUBLICA????O DA PORTARIA NO DI??RIO OFICIAL DO ESTADO"
        );

        ObservableList<String> assGepen5 = FXCollections.observableArrayList(
                "SEPARA????O DOS DOCUMENTOS DAS NOVAS PENS??ES CONCEDIDAS PARA ENVIO AO TCE",
                "ENVIO DA DOCUMENTA????O E INFORMA????ES DA PENS??O AO TCE"
        );

        ObservableList<String> assGepen6 = FXCollections.observableArrayList(
                "BAIXA NO SIGRH DOS PENSIONISTAS FALECIDOS",
                "LEVANTAMENTO DE VALORES PENDENTES DO PENSIONISTA FALECIDO",
                "AN??LISE DA FICHA FINANCEIRA DO PENSIONISTA",
                "VERIFICA????O DE CONTRACHEQUE GERADO/N??O GERADO AP??S O ??BITO",
                "VERIFICA????O SE H?? PROCESSOS ATIVOS DE RESTITUI????O AO ER??RIO E PAGAMENTOS RETROATIVOS",
                "VERIFICA????O SE H?? PAGAMENTOS EM CR??DITOS REJEITADOS",
                "ENCAMINHAMENTO DE OF??CIO AO BANCO DO BRASIL SOLICITANDO ESTORNO DE VALORES QUANDO O ??BITO FOI INFORMADO AP??S FECHAMENTO DA FOLHA",
                "AN??LISE DOS PROCESSOS DE PEDIDO DE LEVANTAMENTO DE VALORES PENDENTES DE HERDEIROS",
                "ENCAMINHAMENTO AOS HERDEIROS DO RESULTADO DO LEVANTAMENTO DE VALORES PENDENTES",
                "ENCAMINHAMENTO DO PROCESSO ?? GECAD PARA AN??LISE DO ALVAR??/ESCRITURA P??BLICA DE INVENT??RIO E LEGALIDADE DO PAGAMENTO",
                "LAN??AMENTO NO SIGRH DO RES??DUO DE PENS??O DEVIDO AO PENSIONISTA AP??S DEFERIMENTO DO PAGAMENTO PELA GECAD E PELO PRESIDENTE",
                "ENCAMINHAMENTO ?? GEAFC PARA PAGAMENTO QUANDO O RES??DUO DE PENS??O DEVE SER PAGO A MAIS DE UM HERDEIRO",
                "COMUNICA????O POR E-MAIL AOS HERDEIROS A RESPEITO DO PAGAMENTO"
        );

        ObservableList<String> assGepen7 = FXCollections.observableArrayList(
                "AN??LISE DOS PROCESSOS JUDICIAIS E DILIG??NCIAS DO TCE",
                "TRATAMENTO DAS DEMANDAS JUDICIAIS",
                "ESCLARECIMENTO DE INFORMA????ES SOLICITADAS",
                "ALTERA????ES NA PENS??O NO SIGRH DEMANDADAS POR DECIS??ES JUDICIAIS",
                "TRATAMENTO DAS DILIG??NCIAS RECEBIDAS DO TCE",
                "ELABORA????O DE OF??CIOS PARA ENCAMINHAMENTO AO ??RG??O DE ORIGEM DO INSTITUIDOR, QUANDO NECESS??RIO",
                "ESCLARECIMENTO DE INFORMA????ES SOLICITADAS",
                "ALTERA????ES NA PENS??O DEMANDADAS PELO TCE"
        );

        ObservableList<String> assGepen8 = FXCollections.observableArrayList(
                "AN??LISE DOS PROCESSOS DE PEDIDOS DE REC??LCULO E REAUSTE DE PENS??ES",
                "ELABORA????O DE PLANILHAS DE C??LCULO",
                "IMPLANTA????O NO SIGRH DOS REAJUSTES CONCEDIDOS"
        );

        ObservableList<String> assGepen9 = FXCollections.observableArrayList(
                "CONFER??NCIA DA FOLHA ATUAL COMPARANDO COM A FOLHA ANTERIOR",
                "CONFER??NCIA DAS NOVAS PENS??ES IMPLANTADAS NO M??S",
                "CONFER??NCIA DOS ??BITOS REGISTRADOS",
                "CONFER??NCIA DE OUTRAS ALTERA????ES EFETUADAS NA FOLHA DE PAGAMENTO",
                "ENVIO ?? GEAFC DE ARQUIVOS GERADOS DE AUTORIZA????O DA FOLHA DE PAGAMENTO PARA ENVIO AO BANCO DO BRASIL",
                "VERIFICA????O DOS VALORES REJEITADOS POR ??BITO OU ALTERA????O DOS DADOS BANC??RIOS",
                "EMISS??O E ENVIO DE RELAT??RIO MENSAL ESPEC??FICO AO TCE/SC",
                "EMISS??O E ENVIO DE RELAT??RIO MENSAL ESPEC??FICO AO TJSC",
                "EMISS??O E ENVIO DE RELAT??RIO MENSAL ESPEC??FICO AO SINDFAZ",
                "EMISS??O E ENVIO DE RELAT??RIO MENSAL ESPEC??FICO AO SCSA??DE E OUTROS",
                "INCLUS??O NO SIGRH DE NOVOS FILIADOS DA APRASC E SINDFAZ PARA DESCONTO EM FOLHA DA MENSALIDADE",
                "VERIFICA????O E HOMOLOGA????O DE FINALIZA????O DO BENEF??CIO DOS PENSIONISTAS QUE COMPLETARAM 21 ANOS NO M??S",
                "VERIFICA????O E HOMOLOGA????O DE FINALIZA????O DO BENEF??CIO DOS PENSIONISTAS COM PENS??O TEMPOR??RIA",
                "EMISS??O DE RELAT??RIO MENSAL DOS PENSIONISTAS QUE POSSUEM BLOQUEIO JUDICIAL NA PENS??O PARA EMISS??O DE BOLETO E ENVIO ?? GEAFC PARA QUITA????O",
                "TRATAMENTO DOS PROCESSOS DE DEN??NCIAS",
                "TRATAMENTO DAS DEMANDAS ORIUNDAS DA OUVIDORIA"
        );

        atividadeComboBox.setItems(atvGepen);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "SETOR DE ATENDIMENTO - SEATE"){
                assuntoComboBox.setItems(assGepen1);
            } else if (newValue == "SETOR DE AN??LISE DE PROCESSOS DE PEDIDO DE PENS??O - SEPEP"){
                assuntoComboBox.setItems(assGepen2);
            } else if (newValue == "SETOR DE INCLUS??O E PAGAMENTO - SEIPA"){
                assuntoComboBox.setItems(assGepen3);
            } else if (newValue == "SETOR DE ATOS LEGAIS - SEATL"){
                assuntoComboBox.setItems(assGepen4);
            } else if (newValue == "SETOR DE TRANSMISS??O AO TCE - SETRA"){
                assuntoComboBox.setItems(assGepen5);
            } else if (newValue == "SETOR DE LEVANTAMENTO DE VALORES PENDENTES - SEVAP"){
                assuntoComboBox.setItems(assGepen6);
            } else if (newValue == "SETOR DE DEMANDAS JUDICIAIS - SEDJU"){
                assuntoComboBox.setItems(assGepen7);
            } else if (newValue == "SETOR DE REC??LCULO - SEREC"){
                assuntoComboBox.setItems(assGepen8);
            } else if (newValue == "OUTRAS DEMANDAS"){
                assuntoComboBox.setItems(assGepen9);
            }
        });


    }

    @FXML
    public void cadastrar() {

        if (!validarDados()) {
            exibirMensagem("Existem campos em branco, ou com dados inv??lidos, revise seus dados!");
            return;
        }

        if (!validadeDatas()){
            return;
        }

        boolean editando = true;

        if (movimentacao == null){
            movimentacao = new Movimentacao();
            editando = false;
        }

        movimentacao.setSetor(setorComboBox.getValue());
        movimentacao.setDataRegistro(dataRefDatePicker.getValue());
        movimentacao.setAtividade(atividadeComboBox.getValue());
        movimentacao.setAssunto(assuntoComboBox.getValue());
        movimentacao.setProcesso(processoTextField.getText());
        movimentacao.setOrgao(orgaoComboBox.getValue());
        movimentacao.setLocal(localidadeComboBox.getValue());
        movimentacao.setDataInicio(dataInicioDatePicker.getValue());
        movimentacao.setDataFim(dataFimDatePicker.getValue());
        //movimentacao.setHoraInicio(horaInicioComboBox.getValue());
        movimentacao.setTempoAtividade(tempoComboBox.getValue());
        movimentacao.setConclusao(conclusaoComboBox.getValue());
        movimentacao.setOberva????o(observacaoTextField.getText());
        movimentacao.setUsuario(usrTextField.getText());

        if (editando == false){
            principalController.adicionarMovimentacao(movimentacao);
            movimentacao = null;
            cadastrarButton.getScene().getWindow().hide();
        } else if (editando == true){
            principalController.attAtividadesTable();
            movDAO.attMovimentacao(movimentacao);
            cadastrarButton.getScene().getWindow().hide();
        }

        System.out.println(movimentacao);

    }

    public boolean validadeDatas() {
        /*long diferenca = ChronoUnit.DAYS.between(dataInicioDatePicker.getValue(), dataFimDatePicker.getValue());*/

        /*System.out.println(diferenca);*/

        if (dataInicioDatePicker.getValue() != null && dataFimDatePicker.getValue() != null ) {

            long diferenca = ChronoUnit.DAYS.between(dataInicioDatePicker.getValue(), dataFimDatePicker.getValue());

            if (diferenca < 0){
                exibirMensagem("A data fim n??o pode ser menor que a data inicio, revise seus dados!");
                return false;
            }


        } else if (dataFimDatePicker.getValue() == null){
            return true;
        }

        return true;
    }

    public boolean validarDados() {

        if (atividadeComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }

        if (orgaoComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }

        if (localidadeComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }

        if (dataInicioDatePicker.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }

        if (dataInicioDatePicker.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }

        /*if (dataFimDatePicker.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }*/

        /*if (horaInicioComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }*/

        /*if (horaFimComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
            return false;
        }*/

        if (conclusaoComboBox.getValue() == null){
            //JOptionPane.showMessageDialog(null, "Data menor que o validado");
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

    public void setPrincipalController(PrincipalController controller) {
        principalController = controller;
    }

}