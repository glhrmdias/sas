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
        observacaoTextField.setText(movimentacao.getObervação());
    }



    @FXML
    public void initialize() {
        dataRefDatePicker.setValue(LocalDate.now());

        orgaoComboBox.setItems(orgaos);
        orgaos.addAll(bd.getOrgao());

        localidadeComboBox.setItems(localidades);
        localidades.addAll(bd.getLocal());

        //horaInicioComboBox.setItems(horario);
        tempoComboBox.setItems(tempo);

        /*definirAssunto();*/

        processoCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                processoTextField.setDisable(false);
            } else {
                processoTextField.setDisable(true);
                processoTextField.setText("");
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
        usuarioStage.setTitle("Lista Secretarias e Órgãos");
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
            JOptionPane.showMessageDialog(null, "Você não pode editar uma atividade que pertence a outro usuário");
        } else {
            localidadeComboBox.setVisibleRowCount(0);
        }
    }

    @FXML
    public void fecharJanela() {
        fecharButton.getScene().getWindow().hide();
    }

    // Atividades e Assuntos CORAFI
    ObservableList<String> atvCoraf = FXCollections.observableArrayList(
            "APURAÇÃO DE DÉBITOS PREVIDENCIÁRIOS(Contrib. Prev)",
            "APURAÇÃO DE DÉBITOS PREVIDENCIÁRIOS(Benef. Prev)",
            "ATENDIMENTO DEMANDA OUTROS SETORES",
            "AUDITORIA"
    );

    ObservableList<String> assCorafi1 = FXCollections.observableArrayList(
            "Licença Sem Remuneração LSR", "À Disposição",
            "Mandato Eletivo", "Cartorário", "Outros"
    );

    ObservableList<String> assCorafi2 = FXCollections.observableArrayList(
            "Resíduos de Aposentadoria", "Resíduos de Pensão",
            "Pensões Irregulares", "Outros"
    );

    ObservableList<String> assCorafi3 = FXCollections.observableArrayList(
            "Averiguação de débitos de contribuição previdenciária",
            "Averiguação de débitos de contribuição previdenciária de instituidores de pensão e pensionistas",
            "Suspender/cancelar/sobrestar cobrança administrativa de contribuição previdenciária por decisão judicial",
            "Proceder Busca em Cartórios por Bens de Notificados",
            "Proceder Busca de endereços de Notificados",
            "Responder Diligências do TCE/SC",
            "Outros"
    );

    ObservableList<String> assCorafi4 = FXCollections.observableArrayList(
            "Auditoria em Benefícios Previdenciários(Aposentadoria)",
            "Auditoria em Benefícios Previdenciários(Pensão)",
            "Auditoria em Contribuições Previdenciárias(Servidor)",
            "Auditoria em Contribuições Previdenciárias(Patronal)"
    );

    public void corafi() {
        atividadeComboBox.setItems(atvCoraf);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "APURAÇÃO DE DÉBITOS PREVIDENCIÁRIOS(Contrib. Prev)"){
                assuntoComboBox.setItems(assCorafi1);
            } else if (newValue == "APURAÇÃO DE DÉBITOS PREVIDENCIÁRIOS(Benef. Prev)") {
                assuntoComboBox.setItems(assCorafi2);
            } else if (newValue == "ATENDIMENTO DEMANDA OUTROS SETORES") {
                assuntoComboBox.setItems(assCorafi3);
            } else if (newValue == "AUDITORIA") {
                assuntoComboBox.setItems(assCorafi4);
            }
        });
    }

    // Atividades e assuntos GECOMP
    ObservableList<String> atvGecomp = FXCollections.observableArrayList(
            "ANALISE DE COMP. (RO) IPREV--->RGPS/INSS",
            "ANALISE DE COMP. (RO) IPREV SOLICITANTE RGPS/INSS",
            "ANALISE DE COMP.(RO) DAS PENSÕES",
            "ANALISE DE COMP. (RI) SENDO O INSS SOLICITANTE",
            "CORREÇÃO DOS FORMULÁRIOS DE ANALISE(SIST. ANTIGO)",
            "DIGITALIZAÇÃO DE DOC. FÍSICOS AOS ANALISTAS E PERITOS DO COMPREV",
            "EMISSÃO DE RELATÓRIOS",
            "REGISTRO DE ANÁLISE DE DEMANDAS(SIST. RAFAEL)",
            "ANALISE DAS APOSENT. POR INVALIDEZ POR MÉDICO PERITO NO SISTEMA COMPREV.",
            "ANALISE DE PENSÃO FILHO MAIOR INVÁLIDO POR MÉDICO PERITO NO SISTEMA COMPREV"
    );

    ObservableList<String> assGecomp1 = FXCollections.observableArrayList(
            "Registro de análise e cadastro de demandas(IPREV--->RGPS/INSS)",
            "AConfecção de documentos(DPT e Certidões Específicas)(IPREV--->RGPS/INSS)",
            "Diligencia para setorial(IPREV--->RGPS/INSS)",
            "Requerimento no COMPREV e envio das imagens de documentos(IPREV--->RGPS/INSS)",
            "Cadastro de DTC no COMPREV(IPREV--->RGPS/INSS)",
            "Solicitação de processo ou documento ao setorial(IPREV--->RGPS/INSS)",
            "Encaminhamento de laudo médico das aposent. por invalidez aos peritos(IPREV--->RGPS/INSS)"
    );

    ObservableList<String> assGecomp2 = FXCollections.observableArrayList(
            "Registro de análise e cadastro de demandas(IPREV--->RPPS)",
            "Confecção de documentos(DPT e Certidões Específicas)(IPREV--->RPPS)",
            "Diligencia para setorial(IPREV--->RPPS)",
            "Requerimento no COMPREV e envio das imagens de documentos(IPREV--->RPPS)",
            "Respostas de diligencias ao COMPREV(IPREV--->RPPS)",
            "Cadastro de DTC no COMPREV(IPREV--->RPPS)",
            "Solicitação de processo ou documento ao setorial(IPREV--->RPPS)",
            "Encaminhamento de laudo médico das aposent. por invalidez aos peritos(IPREV--->RPPS)"
    );

    ObservableList<String> assGecomp3 = FXCollections.observableArrayList(
            "Requerimento comp.(RO) pensão",
            "Envios de documentos comp.(RO) pensão",
            "Acompanhamento e cadastro de DCB comp(RO) Pensão",
            "Disponibilização de laudos mádicos filho maior invalido e dependentes"
    );

    ObservableList<String> assGecomp4 = FXCollections.observableArrayList(
            "Registro no COMPREV(Deferimento) ",
            "Registro no COMPREV(Indeferimento) ",
            "Registro no COMPREV(Exigências)",
            "Registro no COMPREV(Suspensão de Analise)",
            "Solicitação de informações ao setorial do ex-servidor",
            "Solicitação de ficha financeira a SEF"
    );

    ObservableList<String> assGecomp5 = FXCollections.observableArrayList(
            "Contábeis", "Extração de óbitos no SISOB",
            "Busca de inconsistências no COMPREV",
            "Extração de novas exigências",
            "Levantamento de prioridades(Processos antigos ou sobrestado)",
            "DOE (ESTADUAL/TJSC/ALESC/TCE)"
    );

    ObservableList<String> assGecomp6 = FXCollections.observableArrayList(
            "Sem assunto para esta atividade"
    );

    public void gecomp() {
        atividadeComboBox.setItems(atvGecomp);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "ANALISE DE COMP. (RO) IPREV--->RGPS/INSS"){
                assuntoComboBox.setItems(assGecomp1);
            } else if (newValue == "ANALISE DE COMP. (RO) IPREV SOLICITANTE RGPS/INSS") {
                assuntoComboBox.setItems(assGecomp2);
            } else if (newValue == "ANALISE DE COMP.(RO) DAS PENSÕES") {
                assuntoComboBox.setItems(assGecomp3);
            } else if (newValue == "ANALISE DE COMP. (RI) SENDO O INSS SOLICITANTE") {
                assuntoComboBox.setItems(assGecomp4);
            } else if (newValue == "CORREÇÃO DOS FORMULÁRIOS DE ANALISE(SIST. ANTIGO)") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "DIGITALIZAÇÃO DE DOC. FÍSICOS AOS ANALISTAS E PERITOS DO COMPREV") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "EMISSÃO DE RELATÓRIOS") {
                assuntoComboBox.setItems(assGecomp5);
            } else if (newValue == "REGISTRO DE ANÁLISE DE DEMANDAS(SIST. RAFAEL)") {
                assuntoComboBox.setItems(assGecomp6);
            } else if (newValue == "ANALISE DAS APOSENT. POR INVALIDEZ POR MÉDICO PERITO NO SISTEMA COMPREV.") {
                assuntoComboBox.setItems(assGecomp6);
            } else if (newValue == "ANALISE DE PENSÃO FILHO MAIOR INVÁLIDO POR MÉDICO PERITO NO SISTEMA COMPREV") {
                assuntoComboBox.setItems(assGecomp6);
            }
        });
    }

    ObservableList<String> atvGfpag = FXCollections.observableArrayList(
            "Conferência final da folha de pagamento dos inativos",
            "Ressarcimento de remuneração por óbito de servidor inativo",
            "Afastar da folha de pagamento os servidores inativos que vieram a óbito",
            "Receber e distribuir os processos com demandas judiciais e administrativas",
            "Elaborar relatório pensão"
    );

    ObservableList<String> assGfpag1 = FXCollections.observableArrayList(
            "Gerar Resumo para  Comparação entre os arquivos da Folha no SIGRH",
            "Gerar relatório de óbitos", "Gerar relatório de aposentadoria do período",
            "Conferir a folha do mês anterior com a folha que está em desenvolvimento no mês, gerando relatório específico para cada rubrica",
            "Encaminhamento dos arquivos gerados da folha de pagamento, via SGPE, ao Banco do Brasil",
            "Verificação dos valores rejeitados por motivos de mudança de cadastro do servidor ou óbito",
            "Acompanhar arquivos de folha suplementar se necessário",
            "Acompanhamento do cadastro e do recadastramento de servidores inativos",
            "Acompanhamento e levantamento de valores mensalmente bloqueados por falta de recadastramento",
            "Gerar o arquivo com relatório do banco mensal",
            "Gerar arquivos de pagamento por recadastramento, via arquivos bancários",
            "Busca de inativos que não se recadastraram no período certo, para evitar o bloqueio e cancelamento do cadastro"
    );

    ObservableList<String> assGfpag2 = FXCollections.observableArrayList(
            "Analisar processos relativos ao auxílio funeral",
            "Identificar os servidores falecidos que receberam valores indevidos",
            "Fazer o cálculo dos valores pagos indevidamente em razão de óbito de servidor inativo",
            "Encaminhar ofício ao Banco do Brasil solicitando estorno de pagamento",
            "Solicitar cópia da certidão de óbito junto aos cartórios",
            "Providenciar abertura de processo"
    );

    ObservableList<String> assGfpag3 = FXCollections.observableArrayList(
            "Identificar e selecionar os servidores inativos, que vieram a óbito, nos relatórios enviados pelos cartórios",
            "Acompanhar os pedidos de pensão para identificar servidores aposentados que faleceram",
            "Encerrar (Afastar), no SIGRH, os servidores inativos que faleceram (Cartórios e SISOB)"
    );

    ObservableList<String> assGfpag4 = FXCollections.observableArrayList(
            "Providenciar a solução para as demandas judiciais",
            "Providenciar a solução para as demandas administrativas",
            "Calcular o redutor da aposentadoria nos casos de acúmulo com pensão",
            "Lançar o redutor da aposentadoria no SIGRH",
            "Comunicar o servidor acerca do redutor de sua aposentadoria",
            "Comunicar a gerência de compensação previdenciária as aposentadorias que incidiu o redutor"
    );


    ObservableList<String> assGfpag5 = FXCollections.observableArrayList(
            "Calcular os valores de contribuição previdenciária e/ou de imposto de renda a serem restituídos a(o) pensionista",
            "Lançar no SIGRH os valores da contribuição previdenciária e/ou do imposto de renda a serem restituídos a(o) pensionista"
    );

    ObservableList<String> assGfpag6 = FXCollections.observableArrayList(
            "Calcular o valor da pensão relativa ao IPALESC",
            "Elaborar o relatório com o resumo da folha da pensão paga pelos Poderes",
            "Gerar o Relatório 41",
            "Encaminhar email à Fazenda, à Controladoria e, à Diretoria de Previdência, à Presidência, à GEAFC disponibilizando os relatórios"
    );

    public void gfpag() {
        atividadeComboBox.setItems(atvGfpag);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Conferência final da folha de pagamento dos inativos"){
                assuntoComboBox.setItems(assGfpag1);
            } else if (newValue == "Ressarcimento de remuneração por óbito de servidor inativo") {
                assuntoComboBox.setItems(assGfpag2);
            } else if (newValue == "Afastar da folha de pagamento os servidores inativos que vieram a óbito") {
                assuntoComboBox.setItems(assGfpag3);
            } else if (newValue == "Receber e distribuir os processos com demandas judiciais e administrativas") {
                assuntoComboBox.setItems(assGfpag4);
            } else if (newValue == "Restituir valores de contribuição previdenciária e de IRRF aprovados pela Perícia pensionistas") {
                assuntoComboBox.setItems(assGfpag5);
            } else if (newValue == "Elaborar relatório pensão") {
                assuntoComboBox.setItems(assGfpag6);
            }
        });

    }

    ObservableList<String> atvGetig = FXCollections.observableArrayList(
            "Suporte em software", "Suporte em hardware",
            "Cadastramento e manutenção de usuários", "Desenvolvimento", "Serviços administrativos",
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
            "TV", "Cameras", "Catraca", "Ponto de rede", "Rede elétrica"
    );

    ObservableList<String> assGetig3 = FXCollections.observableArrayList(
            "E-Mail", "Rede Iprev", "Senhas de rede", "Pastas de rede", "Catraca",
            "CIASC ETERNAL", "CIASC(PROD)", "Ramais", "Outro sistema"
    );

    ObservableList<String> assGetig4 = FXCollections.observableArrayList(
            "Sistemas", "Bancos de dados", "Scripts", "API'S", "Ferramentas analíticas",
            "Query Boa Vista", "Processos de ETL", "Dashboards", "Migração entre sistemas",
            "Planilhas e Formulários"
    );

    ObservableList<String> assGetig5 = FXCollections.observableArrayList(
            "Certificação de notas fiscais", "Coordenar Dev. e aquisição de sistemas",
            "Levantamento de orçamento", "Elaboração de contratos", "Participação em reuniões"
    );

    ObservableList<String> assGetig6 = FXCollections.observableArrayList(
            "Recuperação de arquivos Acervo"
    );

    public void getig() {
        atividadeComboBox.setItems(atvGetig);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Suporte em software"){
                assuntoComboBox.setItems(assGetig1);
            } else if (newValue == "Suporte em hardware") {
                assuntoComboBox.setItems(assGetig2);
            } else if (newValue == "Cadastramento e manutenção de usuários") {
                assuntoComboBox.setItems(assGetig3);
            } else if (newValue == "Desenvolvimento") {
                assuntoComboBox.setItems(assGetig4);
            } else if (newValue == "Serviços administrativos") {
                assuntoComboBox.setItems(assGetig5);
            } else if (newValue == "Outros") {
                assuntoComboBox.setItems(assGetig6);
            }
        });
    }

    ObservableList<String> atvPres = FXCollections.observableArrayList(
            "Análise de processo"
    );

    ObservableList<String> assPres = FXCollections.observableArrayList(
            "Pensão", "Averbação", "Retificação", "Aposentadoria", "Habitacional",
            "CTC", "Pareceres", "Portaria", "Levantamento de débito", "Valores pendentes",
            "Restituição de Contribuição Previdenciária / Devolução de valores", "Encaminhamentos"
    );

    public void presidencia() {
        atividadeComboBox.setItems(atvPres);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Análise de processo"){
                assuntoComboBox.setItems(assPres);
            }
        });
    }

    ObservableList<String> tempo = FXCollections.observableArrayList(
            "10 minutos", "30 minutos", "1 hora", "3 horas", "5 horas", "7 horas", "Indefinido"
    );

    ObservableList<String> conclusao = FXCollections.observableArrayList(
            "Deferido", "Suspenso", "Diligenciado", "Parecer jurídico", "Aguardando análise",
            "Aguardando compensação", "Compensado", "Criado", "Em análise", "Indeferido", "Incluído",
            "Alterado", "Bloqueado", "Encaminhado", "Emitido"
    );

    @FXML
    public void cadastrar() {

        if (!validarDados()) {
            exibirMensagem("Existem campos em branco, ou com dados inválidos, revise seus dados!");
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
        movimentacao.setObervação(observacaoTextField.getText());
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
                exibirMensagem("A data fim não pode ser menor que a data inicio, revise seus dados!");
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
