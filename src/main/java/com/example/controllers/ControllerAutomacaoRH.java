package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoRH;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.sql.*;

public class ControllerAutomacaoRH {
    @FXML private TextField txtNomeDaAutomacao;
    @FXML private TextField txtResponsavel;
    @FXML private TextArea txtDescricao;
    @FXML private TableView<AutomacaoRH> tablesAutomacaoRH;
    @FXML private TableColumn<AutomacaoRH, String> colId;
    @FXML private TableColumn<AutomacaoRH, String> colNomeAutomacao;
    @FXML private TableColumn<AutomacaoRH, String> colResponsavel;
    @FXML private TableColumn<AutomacaoRH, String> colSetor;
    @FXML private TableColumn<AutomacaoRH, String> colDescricao;
    @FXML private TableColumn<AutomacaoRH, String> colLocalizacao;
    @FXML private TableColumn<AutomacaoRH, String> colCategoria;
    @FXML private TableColumn<AutomacaoRH, String> colOperacao;
    @FXML private TableColumn<AutomacaoRH, String> colPrioridade;
    @FXML private TableColumn<AutomacaoRH, String> colSituacao;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private ComboBox<String> cmbPrioridade;
    @FXML private ComboBox<String> cmbSituacao;
    @FXML private TextField txtLocalizacao;
    @FXML private TextField txtOperacao;
    @FXML private TextField txtSetor;

    //import dos filtros
    @FXML private TextField filtroNomeAut;
    @FXML private TextField filtroSetorAut;
    @FXML private TextField filtroResponsavelAut;
    @FXML private TextField filtroLocalizacaoAut;
    @FXML private TextField filtroOperacaoAut;
    @FXML private ComboBox<String> cmbFiltrarSituacao;
    @FXML private Button btnLimparFiltro;


    //import da atualização
    @FXML private TextField txtAtuNomeDaAutomacao;
    @FXML private TextField txtAtuResponsavel;
    @FXML private TextArea txtAtuDescricao;
    @FXML private ComboBox<String> cmbAtuCategoria;
    @FXML private ComboBox<String> cmbAtuPrioridade;
    @FXML private ComboBox<String> cmbAtuSituacao;
    @FXML private TextField txtAtuLocalizacao;
    @FXML private TextField txtAtuOperacao;
    @FXML private TextField txtAtuSetor;

    //controle de tabs
    @FXML private TabPane tabPaneAutomacaoRH;
    @FXML private Tab TabAutomacaoRH;
    @FXML private Tab TabAtualizarAutomacao;
    @FXML private Tab TabListarAutomacao;

    private ObservableList<AutomacaoRH> listaAutomacaoRH = FXCollections.observableArrayList();

    @FXML
    public void salvarAutomacaoRH() {
  
       

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO automacaoRH (nome_automacao, responsavel, categoria, descricao, operacao, setor, localizacao, situacao, prioridade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, txtNomeDaAutomacao.getText());
            stmt.setString(2, txtResponsavel.getText());
            stmt.setString(3, cmbCategoria.getValue());                  
            stmt.setString(4, txtDescricao.getText());
            stmt.setString(5, txtOperacao.getText());
            stmt.setString(6, txtSetor.getText());
            stmt.setString(7, txtLocalizacao.getText());
            stmt.setString(8, cmbSituacao.getValue());
            stmt.setString(9, cmbPrioridade.getValue());

            stmt.executeUpdate();

            carregarAutomacaoRH();

            limparCadastro();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação salva com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
             mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar a automação!" +  e.getMessage());
        }
    }



    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeAutomacao.setCellValueFactory(new PropertyValueFactory<>("nomeAutomacao"));
        colResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        colSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colOperacao.setCellValueFactory(new PropertyValueFactory<>("operacao"));
        colSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
        colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        

        cmbFiltrarSituacao.getItems().addAll("1-ativo", "2-inativo");
        cmbAtuCategoria.getItems().addAll( "Recrutamento e Seleção", "Treinamento e Desenvolvimento", "Comunicação Interna");
        cmbAtuPrioridade.getItems().addAll("Baixa","Média","Alta");
        cmbAtuSituacao.getItems().addAll("1-ativo", "2-inativo");
        cmbCategoria.getItems().addAll( "Recrutamento e Seleção", "Treinamento e Desenvolvimento", "Comunicação Interna");
        cmbPrioridade.getItems().addAll("Baixa","Média","Alta");
        cmbSituacao.getItems().addAll("ativo", "inativo");

        carregarAutomacaoRH();

        tablesAutomacaoRH.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
                tabPaneAutomacaoRH.getSelectionModel().select(TabAtualizarAutomacao);
            }
        });
    }
    public void preencherCamposAtualizacao() {
        AutomacaoRH automacaoSelecionada = tablesAutomacaoRH.getSelectionModel().getSelectedItem();
        if (automacaoSelecionada != null) {
            txtAtuNomeDaAutomacao.setText(automacaoSelecionada.getNomeAutomacao());
            txtAtuResponsavel.setText(automacaoSelecionada.getResponsavel());
            cmbAtuCategoria.setValue(automacaoSelecionada.getCategoria());
            txtAtuDescricao.setText(automacaoSelecionada.getDescricao());
            txtAtuOperacao.setText(automacaoSelecionada.getOperacao());
            txtAtuSetor.setText(automacaoSelecionada.getSetor());
            txtAtuLocalizacao.setText(automacaoSelecionada.getLocalizacao());
            cmbAtuSituacao.setValue(automacaoSelecionada.getSituacao());
            cmbAtuPrioridade.setValue(automacaoSelecionada.getPrioridade());

            tabPaneAutomacaoRH.getSelectionModel().select(TabAtualizarAutomacao);
        }
    }
    @FXML
public void atualizarAutomacao() {
    AutomacaoRH automacaoSelecionada = tablesAutomacaoRH.getSelectionModel().getSelectedItem();
    
    if (automacaoSelecionada != null) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoRH SET nome_automacao = ?, responsavel = ?, categoria = ?, descricao = ?, operacao = ?, setor = ?, localizacao = ?, situacao = ?, prioridade = ? WHERE id = ?")) {
            
            stmt.setString(1, txtAtuNomeDaAutomacao.getText());
            stmt.setString(2, txtAtuResponsavel.getText());
            stmt.setString(3, cmbAtuCategoria.getValue());
            stmt.setString(4, txtAtuDescricao.getText());
            stmt.setString(5, txtAtuOperacao.getText());
            stmt.setString(6, txtAtuSetor.getText());
            stmt.setString(7, txtAtuLocalizacao.getText());
            stmt.setString(8, cmbAtuSituacao.getValue());
            stmt.setString(9, cmbAtuPrioridade.getValue());
            stmt.setInt(10, automacaoSelecionada.getId());
            
            stmt.executeUpdate();
            
            carregarAutomacaoRH(); // Atualiza a tabela após a atualização

            tabPaneAutomacaoRH.getSelectionModel().select(TabListarAutomacao);
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação atualizada com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar automação: " + e.getMessage());
        }
    } else {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para atualizar!");
    }
}

    @FXML
    public void carregarAutomacaoRH() {
        listaAutomacaoRH.clear();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoRH")) {

            while (rs.next()) {
                listaAutomacaoRH.add(new AutomacaoRH(rs.getInt("id"), rs.getString("nome_automacao"), rs.getString("responsavel"), rs.getString("categoria"), rs.getString("descricao"), rs.getString("operacao"), rs.getString("setor"), rs.getString("localizacao"), rs.getString("situacao"), rs.getString(("prioridade"))));
            }
            tablesAutomacaoRH.setItems(listaAutomacaoRH);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//função para filtrar os produtos
   @FXML
    public void filtrarAutomacao() {
        FilteredList<AutomacaoRH> dadosFiltrados = new FilteredList<>(listaAutomacaoRH, p -> true);

        dadosFiltrados.setPredicate(produto -> {
            if (!filtroNomeAut.getText().isEmpty() && !produto.getNomeAutomacao().toLowerCase().contains(filtroNomeAut.getText().toLowerCase())) {
                return false;
            }
            if (!filtroSetorAut.getText().isEmpty() && !String.valueOf(produto.getSetor()).toLowerCase().contains(filtroSetorAut.getText().toLowerCase())) {
                return false;
            }
            if (!filtroOperacaoAut.getText().isEmpty() && !String.valueOf(produto.getOperacao()).toLowerCase().contains(filtroOperacaoAut.getText().toLowerCase())) {
                return false;
            }
            if (!filtroLocalizacaoAut.getText().isEmpty() && !produto.getLocalizacao().toLowerCase().contains(filtroLocalizacaoAut.getText().toLowerCase())) {
                return false;
            }
            if (!filtroResponsavelAut.getText().isEmpty() && !produto.getResponsavel().toLowerCase().contains(filtroResponsavelAut.getText().toLowerCase())) {
                return false;
            }
            if (cmbFiltrarSituacao.getValue() != null && !cmbFiltrarSituacao.getValue().isEmpty() && !produto.getSituacao().toLowerCase().contains(cmbFiltrarSituacao.getValue().toLowerCase())) {
                return false;
            }
         
            return true;
        });

        tablesAutomacaoRH.setItems(dadosFiltrados);
    }

    @FXML
    public void limparFiltro() {
        filtroNomeAut.clear();
        filtroResponsavelAut.clear();
        filtroSetorAut.clear();
        filtroLocalizacaoAut.clear();
        filtroOperacaoAut.clear();
        cmbFiltrarSituacao.setValue(null);
        tablesAutomacaoRH.setItems(listaAutomacaoRH);
    }





public void limparCadastro() {
        txtNomeDaAutomacao.clear();
        txtResponsavel.clear();
        txtSetor.clear();
        txtDescricao.clear();
        txtLocalizacao.clear();
        cmbCategoria.setValue(null);
        cmbPrioridade.setValue(null);
        cmbSituacao.setValue(null);
        txtOperacao.clear();
        tablesAutomacaoRH.setItems(listaAutomacaoRH);
    }

    @FXML
public void deleteArh() {
    AutomacaoRH automacaoSelecionada = tablesAutomacaoRH.getSelectionModel().getSelectedItem();
    
    if (automacaoSelecionada != null) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoRH WHERE id = ?")) {
            
            stmt.setInt(1, automacaoSelecionada.getId());
            stmt.executeUpdate();
            
            txtAtuNomeDaAutomacao.clear();
            txtAtuSetor.clear();
            txtAtuDescricao.clear();
            txtAtuOperacao.clear();
            txtAtuLocalizacao.clear();  
            txtAtuResponsavel.clear();
            cmbAtuPrioridade.getSelectionModel().clearSelection();
            cmbAtuCategoria.getSelectionModel().clearSelection();
            cmbAtuSituacao.getSelectionModel().clearSelection();
            
            carregarAutomacaoRH(); // Atualiza a tabela após a exclusão
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação excluída com sucesso!");
            tabPaneAutomacaoRH.getSelectionModel().select(TabListarAutomacao);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir automação: " + e.getMessage());
        }
    } else {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para excluir!");
    }
}

    public void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
    Platform.runLater(() -> {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.show();
    });
}
}
