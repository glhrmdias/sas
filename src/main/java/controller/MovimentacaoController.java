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

        conclusaoComboBox.setItems(conclusao);

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
            JOptionPane.showMessageDialog(null,
                    "Você não pode editar uma atividade que pertence a outro usuário");
        } else {
            localidadeComboBox.setVisibleRowCount(0);
        }
    }

    @FXML
    public void fecharJanela() {
        fecharButton.getScene().getWindow().hide();
    }

    public void corafi() {
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

    public void gecomp() {
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

    public void gfpag() {
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

    public void getig() {
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

    public void presidencia() {
        ObservableList<String> atvPres = FXCollections.observableArrayList(
                "Análise de processo"
        );

        ObservableList<String> assPres = FXCollections.observableArrayList(
                "Pensão", "Averbação", "Retificação", "Aposentadoria", "Habitacional",
                "CTC", "Pareceres", "Portaria", "Levantamento de débito", "Valores pendentes",
                "Restituição de Contribuição Previdenciária / Devolução de valores", "Encaminhamentos"
        );

        atividadeComboBox.setItems(atvPres);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Análise de processo"){
                assuntoComboBox.setItems(assPres);
            }
        });
    }

    public void gerac() {
        ObservableList<String> atvGerac = FXCollections.observableArrayList(
                "TRIAR DEMANDAS RECEBIDAS",
                "ANALISAR PROCESSO JUDICIAL PARA ELABORAÇÃO DE CÁLCULO",
                "SOLICITAR ESCLARECIMENTOS",
                "SOLICITAÇÃO DE DOCUMENTOS COMPLEMENTARES",
                "REALIZAR CÁLCULOS",
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
                "ANALISAR SOLICITAÇÃO DO PROCURADOR DO IPREV, PETIÇÃO JUDICIAL E RUBRICAS SOLICITADAS",
                "CONSULTAR SIGRH OU RH SETORIAL E A FICHA FINANCEIRA  DO SERVIDOR",
                "PESQUISAR SE PARTE AUTORA JÁ RECEBEU ADM  (SIGRH E SGPE)",
                "PESQUISAR SE PARTE AUTORA POSSUI LITISPENDÊNCIAS (PGNET e TJSC)",
                "SELECIONAR DOCUMENTOS PARA COMPROVAR JUDICIALMENTE SE A PARTE AUTORA JÁ RECEBEU RUBRICAS"
        );

        ObservableList<String> assGerac3 = FXCollections.observableArrayList(
                "SOLICITAR (SE NECESSÁRIO) ESCLARECIMENTOS AO PROCURADOR RESPONSÁVEL (GECOJ)",
                "SOLICITAR (SE NECESSÁRIO)  INFORMAÇÕES PARA OUTROS SETORES DO IPREV",
                "SOLICITAR (SE NECESSÁRIO)  INFORMAÇÕES AOS RH SETORIAIS"
        );

        ObservableList<String> assGerac4 = FXCollections.observableArrayList(
                "SOLICITAR (SE NECESSÁRIO) CERTIDÃO (SE VIVO FOSSE) PARA CÁLCULO DE PENSÃO",
                "SOLICITAR  (SE NECESSÁRIO) EXTRATO DE VALORES RECEBIDOS / PAGOS (DE PREFEITURAS)",
                "SOLICITAR (SE NECESSÁRIO)  CONTRACHEQUES, FICHA FINANCEIRA E/OU FUNCIONAL DE ÓRGÃOS QUE NÃO ESTÃO NO SIGRH",
                "SOLICITAR OUTROS DOCUMENTOS CONFORME NECESSIDADE ESPECÍFICA DOS PROCESSOS"
        );

        ObservableList<String> assGerac5 = FXCollections.observableArrayList(
                "CONFERIR  CÁLCULO DA PARTE AUTORA",
                "REALIZAR CÁLCULOS",
                "VERIFICAR SE DEVE HAVER A RETENÇÃO DA CONTRIBUIÇÃO PREVIDENCIÁRIA",
                "ELABORAR FUNDAMENTAÇÃO PARA A IMPUGNAÇÃO DE VALORES  (PARECER TÉCNICO)",
                "ANEXAR (SE NECESSÁRIO) DOCUMENTAÇÃO COMPROBATÓRIA",
                "ANEXAR  PROCESSO AO PGNET E/OU SGPE"
        );

        ObservableList<String> assGerac6 = FXCollections.observableArrayList(
                "CONFERIR SE PLANILHA DE CÁLCULO E DOCUMENTOS COMPLEMENTARES FORAM CORRETAMENTE ANEXADOS AO PROCESSO",
                "PRESTAR INFORMAÇÕES RELEVANTES QUE NÃO CONSTAM NA PLANILHA DE CÁLCULO",
                "EMITIR PARECER DE ENCAMINHAMENTO ",
                "ENVIAR O PROCESSO AO SETOR DE ORIGEM"
        );

        ObservableList<String> assGerac7 = FXCollections.observableArrayList(
                "ACOMPANHAR FLUXO DE TRABALHO E PLANEJAR ATIVIDADES",
                "ACOMPANHAR E DOCUMENTAR TRABALHO DO SETOR (RESULTADOS, VALORES, ETC)",
                "CADASTRAR DADOS EM SISTEMAS PARA ACOMPANHAMENTO DE RESULTADOS OU REGISTRO DE TRABALHO",
                "MANUALIZAR ROTINAS E ATIVIDADES DO SETOR",
                "ACOMPANHAR INFORMATIZAÇÃO DO SETOR",
                "PARTICIPAR DE REUNIÕES E/OU TREINAMENTOS CONFORME DEMANDA DOS GESTORES",
                "ANALISAR E/OU ELABORAR RELATÓRIOS, PARECERES, PLANILHAS OU OUTROS DOCUMENTOS SOLICITADOS PELOS GESTORES",
                "ELABORAR/LEVANTAR NECESSIDADES/DADOS E/OU COMPILAR INFORMAÇÕES SOLICITADAS PELOS GESTORES"
        );

        atividadeComboBox.setItems(atvGerac);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "TRIAR DEMANDAS RECEBIDAS"){
                assuntoComboBox.setItems(assGerac1);
            } else if (newValue == "ANALISAR PROCESSO JUDICIAL PARA ELABORAÇÃO DE CÁLCULO"){
                assuntoComboBox.setItems(assGerac2);
            } else if (newValue == "SOLICITAR ESCLARECIMENTOS"){
                assuntoComboBox.setItems(assGerac3);
            } else if (newValue == "SOLICITAÇÃO DE DOCUMENTOS COMPLEMENTARES"){
                assuntoComboBox.setItems(assGerac4);
            } else if (newValue == "REALIZAR CÁLCULOS"){
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
                "RECEBER E RESPONDER E-MAILS, WHATSAAP E TELEFÔNICO",
                "RECEBER E-MAILS PARA AUTUAÇÃO PROCESSOS",
                "ATENDIMENTO PRESENCIAL",
                "OUTRAS DEMANDAS"
        );

        ObservableList<String> assGerat1 = FXCollections.observableArrayList(
                "DÚVIDAS SOBRE RECADASTRAMENTO",
                "EMISSÃO DE SENHA PORTAL SERVIDOR - PENSIONISTA",
                "ORIENTAÇÃO SOBRE DOCUMENTOS NECESSÁRIOS PEDIDO DE PENSÃO POR MORTE",
                "ORIENTAÇÃO SOBRE DOCUMENTOS NECESSÁRIOS PEDIDO DE PROCESSOS DE VALORES PENDENTES, " +
                "REAJUSTE DE PENSÃO, COMUNICAÇÃO DE ÓBITO, ISENÇÃO DE IMPOSTO DE RENDA E DEDUÇÃO PREVIDÊNCIARIA, REATIVAÇÃO PENSÃO",
                "DÚVIDAS DIVERSAS - PENSÃO , APOSENTADORIA, CERTIDÃO DE TEMPO DE CONTRIBUIÇÃO, AVERBAÇÃO",
                "ORIENTAÇÃO SOBRE SOLICITAÇÃO CÓPIA PROCESSOS",
                "ORIENTAÇÃO COMO SOLICITAR DECLARAÇÃO DE NÃO RECEBIMENTO DE BENEFÍCIO PREVIDENCIÁRIO"
        );

        ObservableList<String> assGerat2 = FXCollections.observableArrayList(
                "DISTRIBUIR E-MAILS PARA AUTUAÇÃO PROCESSOS PENSÃO INCIAL",
                "DISTRIBUIR E-MAILS PARA AUTUAÇÃO PROCESSOS VALORES PENDENTES , REAJUSTE DE PENSÕES E INSENÇÃO DE IMPOSTO DE RENDA E DEDUÇÃO PREVIDENCIÁRIA - PENSIONISTA"
        );

        ObservableList<String> assGerat3 = FXCollections.observableArrayList(
                "AGENDAMENTO  - AUTUAÇÃO PROCESSO PENSÃO INICIAL "
        );

        ObservableList<String> assGerat4 = FXCollections.observableArrayList(
                "ACOMPANHAR FLUXO DE TRABALHO E PLANEJAR ATIVIDADES",
                "ACOMPANHAR E DOCUMENTAR TRABALHO DO SETOR (RESULTADOS, VALORES, ETC)",
                "CADASTRAR DADOS EM SISTEMAS PARA ACOMPANHAMENTO DE RESULTADOS OU REGISTRO DE TRABALHO",
                "MANUALIZAR ROTINAS E ATIVIDADES DO SETOR",
                "ACOMPANHAR INFORMATIZAÇÃO DO SETOR",
                "PARTICIPAR DE REUNIÕES E/OU TREINAMENTOS CONFORME DEMANDA DOS GESTORES",
                "ANALISAR E/OU ELABORAR RELATÓRIOS, PARECERES, PLANILHAS OU OUTROS DOCUMENTOS SOLICITADOS PELOS GESTORES",
                "ELABORAR/LEVANTAR NECESSIDADES/DADOS E/OU COMPILAR INFORMAÇÕES SOLICITADAS PELOS GESTORES"
        );

        atividadeComboBox.setItems(atvGerat);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "RECEBER E RESPONDER E-MAILS, WHATSAAP E TELEFÔNICO"){
                assuntoComboBox.setItems(assGerat1);
            } else if (newValue == "RECEBER E-MAILS PARA AUTUAÇÃO PROCESSOS"){
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
                "Executar, acompanhar, operacionalizar e controlar todas as atividades da gerência",
                "Emitir parecer jurídico, solicitar diligências, prestar informações técnicas de matérias jurídicas afetas a sua competência, em processos de interesse do IPREV",
                "Realizar estudo social com a finalidade de elaboração do Relatório Social",
                "Emitir parecer jurídico, solicitar diligências, prestar informações técnicas de " +
                "matérias jurídicas que demandam a elaboração de manifestação orientativa, que servirá de base para aplicação em diversas demandas que envolvam a mesma matéria analisada"
        );

        ObservableList<String> assGecad1 = FXCollections.observableArrayList(
                "Receber citações e intimações, na ausência do Gerente do Contencioso Judicial",
                "Coordenar e supervisionar as atividades do serviço social, diligência, elaboração de laudo social e " +
                        "perícias técnico-administrativas na concessão e revisão do benefício previdenciário",
                "Emitir parecer jurídico, solicitar diligências, prestar informações técnicas de matérias afetas a sua competência, " +
                        "despachar encaminhamento de processos administrativos de sua competência",
                "Analisar pareceres, diligências e informações produzidas pelos advogados, assistentes jurídicos e técnicos da Gerência",
                "Responder emails direcionados à Gerência"
        );

        ObservableList<String> assGecad2 = FXCollections.observableArrayList(
                "Analisar processos para a concessão e revisão de benefícios previdenciários, " +
                        "de licitação, projetos normativos, dentre outros determinados pela Gerência do Contencioso Administrativo, " +
                        "emitindo parecer jurídico, diligências e informações"
        );

        ObservableList<String> assGecad3 = FXCollections.observableArrayList(
                "Leitura e análise do conteúdo processual/documental",
                "Entrevistas sociais (remoto/presencial)",
                "Visitas domiciliares",
                "Visitas institucionais",
                "Contatos telefônicos/por e-mail/whatsapp",
                "Emissão de Relatório Social após a constatação da situação",
                "Inserir o Relatório Social e documentos no SGPE",
                "Registro e acompanhamento da planilha de processos no google drive",
                "Verificação do SGPE e da planilha dos processos no google drive",
                "Verificação/leitura do e-mail institucional e respostas às mensagens, caso necessário",
                "Gravação das entrevistas por áudio/chamada de vídeo, quando realizadas no trabalho remoto",
                "Reunião de equipe, com a gestão e advogado/a vinculado ao processo a fim de avaliar a metodologia de trabalho, demanda de processos, entre outros",
                "Consulta bibliográfica para a realização de documentos escritos",
                "Prestar orientação para atendimento da rede de saúde e ou socioassistencial do município onde o/a requerente reside",
                "Pesquisa Social"
        );

        ObservableList<String> assGecad4 = FXCollections.observableArrayList(
                "Analisar processos para a concessão e revisão de benefícios previdenciários, de licitação, projetos normativos, " +
                        "dentre outros determinados pela Gerência do Contencioso Administrativo, emitindo parecer jurídico, diligências e informações"
        );

        atividadeComboBox.setItems(atvGecad);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Executar, acompanhar, operacionalizar e controlar todas as atividades da gerência"){
                assuntoComboBox.setItems(assGecad1);
            } else if (newValue == "Emitir parecer jurídico, solicitar diligências, prestar informações técnicas de " +
                    "matérias jurídicas afetas a sua competência, em processos de interesse do IPREV"){
                assuntoComboBox.setItems(assGecad2);
            } else if (newValue == "Realizar estudo social com a finalidade de elaboração do Relatório Social"){
                assuntoComboBox.setItems(assGecad3);
            } else if (newValue == "Emitir parecer jurídico, solicitar diligências, prestar informações técnicas de matérias jurídicas que demandam a elaboração de manifestação orientativa, " +
                    "que servirá de base para aplicação em diversas demandas que envolvam a mesma matéria analisada"){
                assuntoComboBox.setItems(assGecad4);
            }
        });


    }

    public void gerin() {
        ObservableList<String> atvGerin = FXCollections.observableArrayList(
                "ANALISE DE PROCESSO APOSENTADORIA",
                "INATIVAÇÃO DO SERVIDOR NO SIGRH",
                "ANALISE DE AVERBAÇÃO",
                "INCLUSÃO DE AVERBAÇÃO NO SIGRH",
                "ANÁLISE DE REVISÃO DE PROVENTOS",
                "DEMANDAS JUDICIAIS",
                "ANÁLISE DE REQUERIMENTO DE CTC",
                "EMISSÃO DE RELATÓRIO SALÁRIOS DE CONTRIBUIÇÃO",
                "EMISSÃO DE INFORMAÇÃO DE CONTRIBUIÇÃO DE PERÍODOS LTIP",
                "ANÁLISE DE PEDIDO DE ISENÇÃO DE CONTRIB PREVIDENCIÁRIA",
                "CONFECÇÃO DE PORTARIAS",
                "CONFERÊNCIA DAS PUBLICAÇÕES",
                "COMUNICAÇÃO DE PUBLICAÇÕES",
                "DILIGÊNCIAS DO TCE",
                "ANÁLISE DE PEDIDO DE APOSENTADORIA CARTORÁRIOS"
        );

        ObservableList<String> assGerin1 = FXCollections.observableArrayList(
                "ANALISE DOCUMENTAL",
                "VERIFICAÇÃO DE REQUISITOS",
                "VERIFICAÇÃO DA INCORPORAÇÃO DE RUBRICAS NOS PROVENTOS DE APOSENTADORIA",
                "ENCAMINHAMENTO DE DILIGÊNCIAS",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin2 = FXCollections.observableArrayList(
                "CONFERÊNCIA  DOS ATOS DE APOSENTADORIA PUBLICADOS",
                "INCLUSÃO DA MODALIDADE DE APOSENTADORIA NO SISTEMA",
                "INCLUSÃO DAS RUBRICAS DO INATIVO",
                "EMISSÃO E CONFERÊNCIA CONTRACHEQUE INATIVO"
        );

        ObservableList<String> assGerin3 = FXCollections.observableArrayList(
                "ANÁLISE DE CTC/DTC",
                "CONFERÊNCIA DOS PERÍODOS SOLICITADOS",
                "ENCAMINHAMENTO DE DILIGÊNCIAS",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin4 = FXCollections.observableArrayList(
                "INCLUSÃO DAS AVERBAÇÕES PUBLICADAS NO SIGRH",
                "INCLUSÃO DOS SALÁRIOS DE CONTRIBUIÇÃO A PARTIR DE JULHO/1994"
        );

        ObservableList<String> assGerin5 = FXCollections.observableArrayList(
                "VERIFICAÇÃO DO PEDIDO",
                "ANÁLISE DA DOCUMENTAÇÃO",
                "DEFERIMENTO/INDEFERIMENTO DO PEDIDO"
        );

        ObservableList<String> assGerin6 = FXCollections.observableArrayList(
                "IMPLANTAÇÃO DE DEMANDAS JUDICIAIS",
                "SUBSIDIOS PARA DEFESAS JUDICIAIS"
        );

        ObservableList<String> assGerin7 = FXCollections.observableArrayList(
                "ANÁLISE DOCUMENTAL",
                "EMISSÃO DE CTC"
        );

        ObservableList<String> assGerin8 = FXCollections.observableArrayList(
                "EMISSÃO DE RELATÓRIOS A PARTIR JULHO/1994 PARA ANEXAR AS CTC"
        );

        ObservableList<String> assGerin9 = FXCollections.observableArrayList(
                "INFORMAÇÃO UTILIZADA PARA COMPROVAR PERÍODOS AVERBADOS"
        );

        ObservableList<String> assGerin10 = FXCollections.observableArrayList(
                "ANÁLISE LAUDO PERICIAL",
                "DESPACHO DEFERIMENTO/INDEFERIMENTO DO PEDIDO",
                "EMISSÃO DE PLANILHA DE CÁLCULO DE RESTITUIÇÃO DAS CONTRIBUIÇÕES PREV",
                "IMPLANTAÇÃO DOS RETROATIVOS NO SIGRH",
                "OFICIAR  OS  DEMAIS PODERES"
        );

        ObservableList<String> assGerin11 = FXCollections.observableArrayList(
                "EMISSÃO DE ATOS DE APOSENTADORIA E AVERBAÇÕES"
        );

        ObservableList<String> assGerin12 = FXCollections.observableArrayList(
                "LIBERAÇÃO PARA PUBLICAÇÃO NO DOE E CONFERÊNCIA DAS PUBLICAÇÕES"
        );

        ObservableList<String> assGerin13 = FXCollections.observableArrayList(
                "COMUNICAR O SERVIDOR INTERESSADO QUANTO A APUBLICAÇÃO DA APOSENTADORIA"
        );

        ObservableList<String> assGerin14 = FXCollections.observableArrayList(
                "ANALISAR O SOLICITADO EM AUDIÊNCIA PELO TCE",
                "ENCAMINHAR RESPOSTA À GEDIL",
                "SOLICITAR RETIFICAÇÃO DE ATOS DE APOSENTADORIA VISANDO HOMOLOGAÇÃO PELO TCE"
        );

        ObservableList<String> assGerin15 = FXCollections.observableArrayList(
                "ABERTURA PROCESSO/RECEBIMENTO DO PEDIDO",
                "ENCAMINHAMENTO GEAFC PARA RELATÓRIO",
                "VERIFICAÇÃO DO TEMPO DE CONTRIBUIÇÃO",
                "ENCAMINHAMENTO GECAD VERIFICAÇÃO DE DECISÕES JUDICAIS",
                "RETORNO DE INFORMAÇÕES AO TJSC"
        );

        atividadeComboBox.setItems(atvGerin);
        conclusaoComboBox.setItems(conclusao);

        atividadeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "ANALISE DE PROCESSO APOSENTADORIA"){
                assuntoComboBox.setItems(assGerin1);
            } else if (newValue == "INATIVAÇÃO DO SERVIDOR NO SIGRH"){
                assuntoComboBox.setItems(assGerin2);
            } else if (newValue == "ANALISE DE AVERBAÇÃO"){
                assuntoComboBox.setItems(assGerin3);
            } else if (newValue == "INCLUSÃO DE AVERBAÇÃO NO SIGRH"){
                assuntoComboBox.setItems(assGerin4);
            } else if (newValue == "ANÁLISE DE REVISÃO DE PROVENTOS"){
                assuntoComboBox.setItems(assGerin5);
            } else if (newValue == "DEMANDAS JUDICIAIS"){
                assuntoComboBox.setItems(assGerin6);
            } else if (newValue == "ANÁLISE DE REQUERIMENTO DE CTC"){
                assuntoComboBox.setItems(assGerin7);
            } else if (newValue == "EMISSÃO DE RELATÓRIO SALÁRIOS DE CONTRIBUIÇÃO"){
                assuntoComboBox.setItems(assGerin8);
            } else if (newValue == "EMISSÃO DE INFORMAÇÃO DE CONTRIBUIÇÃO DE PERÍODOS LTIP"){
                assuntoComboBox.setItems(assGerin9);
            } else if (newValue == "ANÁLISE DE PEDIDO DE ISENÇÃO DE CONTRIB PREVIDENCIÁRIA"){
                assuntoComboBox.setItems(assGerin10);
            } else if (newValue == "CONFECÇÃO DE PORTARIAS"){
                assuntoComboBox.setItems(assGerin11);
            } else if (newValue == "CONFERÊNCIA DAS PUBLICAÇÕES"){
                assuntoComboBox.setItems(assGerin12);
            } else if (newValue == "COMUNICAÇÃO DE PUBLICAÇÕES"){
                assuntoComboBox.setItems(assGerin13);
            } else if (newValue == "DILIGÊNCIAS DO TCE"){
                assuntoComboBox.setItems(assGerin14);
            } else if (newValue == "ANÁLISE DE PEDIDO DE APOSENTADORIA CARTORÁRIOS"){
                assuntoComboBox.setItems(assGerin15);
            }
        });


    }

    ObservableList<String> tempo = FXCollections.observableArrayList(
            "10 minutos", "30 minutos", "1 hora", "3 horas", "5 horas", "7 horas", "Indefinido"
    );

    ObservableList<String> conclusao = FXCollections.observableArrayList(
            "Deferido", "Suspenso", "Diligenciado", "Parecer jurídico", "Aguardando análise",
            "Aguardando compensação", "Compensado", "Criado", "Em análise", "Indeferido", "Incluído",
            "Alterado", "Bloqueado", "Encaminhado", "Emitido", "Concluído", "Em andamento"
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
