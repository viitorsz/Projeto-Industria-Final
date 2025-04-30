package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoQA;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ControllerAutomacaoQA {

    @FXML private TableView<AutomacaoQA> tablesAutomacaoQA;
    @FXML private TableColumn<AutomacaoQA, String> colProduto;
    @FXML private TableColumn<AutomacaoQA, String> colSelo;
    @FXML private TableColumn<AutomacaoQA, String> colDescricaoQA;
    @FXML private TableColumn<AutomacaoQA, String> colCaso;
    @FXML private TableColumn<AutomacaoQA, String> colChegada;
    @FXML private TableColumn<AutomacaoQA, String> colSaida;
    @FXML private TableColumn<AutomacaoQA, String> colPorcentagem;
    
    @FXML private TextField txtProduto;
    @FXML private TextField txtSelo;
    @FXML private TextArea txtDescricaoQA;    
    @FXML private ComboBox<String> cmbCaso;
    @FXML private TextField txtChegada;
    @FXML private TextField txtSaida;
    @FXML private TextField txtPorcentagem;

    @FXML private TextField txtAtualizarProduto;
    @FXML private TextField txtAtualizarSelo;
    @FXML private TextArea txtAtualizarDescricaoQA;
    @FXML private ComboBox<String> cmbAtualizarCaso;
    @FXML private TextField txtAtualizarChegada;
    @FXML private TextField txtAtualizarSaida;
    @FXML private TextField txtAtualizarPorcentagem;

    @FXML private TabPane tabPaneAutomacaoQA;
    @FXML private Tab tabAutomacaoQA;
    @FXML private Tab tabListarQA;
    @FXML private Tab tabAtualizarQA;    

    // Filtros
    @FXML private TextField filtroProduto;
    @FXML private TextField filtroSelo;
    @FXML private ComboBox<String> cmbfiltroCaso;

    ObservableList<AutomacaoQA> listaAutomacaoQA = FXCollections.observableArrayList();

    // Método para gerar QA
    @FXML
    public void gerarQA() {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO automacaoQA (produto, selo, descricao, caso, chegada, saida, porcentagem) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, Integer.parseInt(txtProduto.getText()));
            stmt.setString(2, txtSelo.getText());
            stmt.setString(3, txtDescricaoQA.getText());
            stmt.setString(4, cmbCaso.getValue());
            stmt.setString(5, txtChegada.getText());
            stmt.setString(6, txtSaida.getText());
            stmt.setString(7, txtPorcentagem.getText());

            stmt.executeUpdate();

            carregarQA();
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item criado com sucesso!");
        } catch (SQLException | NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar item! " + e.getMessage());
        }
    }

    // Método para carregar dados de QA na tabela
    public void carregarQA() {
        ObservableList<AutomacaoQA> listaAutomacaoQA = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoQA")) {

            while (rs.next()) {
                listaAutomacaoQA.add(new AutomacaoQA(
                        rs.getInt("id"),
                        rs.getInt("produto"),
                        rs.getString("selo"),
                        rs.getString("descricao"),
                        rs.getString("caso"),
                        rs.getString("chegada"),
                        rs.getString("saida"),
                        rs.getString("porcentagem")
                ));
            }

            tablesAutomacaoQA.setItems(listaAutomacaoQA);
            this.listaAutomacaoQA = listaAutomacaoQA;

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar QA! " + e.getMessage());
        }
    }

    @FXML
    public void editarQA() {
        AutomacaoQA itemSelecionado = tablesAutomacaoQA.getSelectionModel().getSelectedItem();
        if (itemSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para editar.");
            return;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoQA SET produto=?, selo=?, descricao=?, caso=?, chegada=?, saida=?, porcentagem=? WHERE id=?")) {

            stmt.setInt(1, Integer.parseInt(txtProduto.getText()));
            stmt.setString(2, txtSelo.getText());            
            stmt.setString(3, txtDescricaoQA.getText());
            stmt.setString(4, cmbCaso.getValue());
            stmt.setString(5, txtChegada.getText());
            stmt.setString(6, txtSaida.getText());
            stmt.setString(7, txtPorcentagem.getText());
            stmt.setInt(8, itemSelecionado.getId());

            stmt.executeUpdate();

            carregarQA();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item atualizado com sucesso!");
        } catch (SQLException | NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar item! " + e.getMessage());
        }
    }

    @FXML
    public void DeletarQA() {
        AutomacaoQA itemSelecionado = tablesAutomacaoQA.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoQA WHERE id=?")) {

                stmt.setInt(1, itemSelecionado.getId());
                stmt.executeUpdate();
                
                txtAtualizarProduto.clear();
                txtAtualizarSelo.clear();
                txtAtualizarDescricaoQA.clear();
                cmbAtualizarCaso.getSelectionModel().clearSelection(); 
                txtAtualizarChegada.clear();
                txtAtualizarSaida.clear();
                txtAtualizarPorcentagem.clear();                    

                carregarQA();

                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item excluído com sucesso!");
                tabPaneAutomacaoQA.getSelectionModel().select(tabListarQA);
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir item: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para excluir!");
        }
    }

    // Método para filtrar os itens de QA
    @FXML
    public void filtrarQA() {
        ObservableList<AutomacaoQA> itensFiltrados = FXCollections.observableArrayList();

        String textoProduto = filtroProduto.getText().toLowerCase();
        String textoSelo = filtroSelo.getText().toLowerCase();
        String casoSelecionado = cmbfiltroCaso.getSelectionModel().getSelectedItem();

        for (AutomacaoQA item : listaAutomacaoQA) {
            boolean filtrarProduto = textoProduto.isEmpty() || String.valueOf(item.getProduto()).toLowerCase().contains(textoProduto);
            boolean filtrarSelo = textoSelo.isEmpty() || item.getSelo().toLowerCase().contains(textoSelo);
            boolean filtrarCaso = casoSelecionado == null || casoSelecionado.isEmpty() || item.getCaso().equalsIgnoreCase(casoSelecionado);

            if (filtrarProduto && filtrarSelo && filtrarCaso) {
                itensFiltrados.add(item);
            }
        }

        tablesAutomacaoQA.setItems(itensFiltrados.isEmpty() ? listaAutomacaoQA : itensFiltrados);
    }
    
    public void preencherCamposAtualizacao() {
        AutomacaoQA automacaoSelecionada = tablesAutomacaoQA.getSelectionModel().getSelectedItem();
        if (automacaoSelecionada != null) {
            txtAtualizarProduto.setText(String.valueOf(automacaoSelecionada.getProduto()));
            txtAtualizarSelo.setText(automacaoSelecionada.getSelo());
            txtAtualizarDescricaoQA.setText(automacaoSelecionada.getDescricao());
            cmbAtualizarCaso.setValue(automacaoSelecionada.getCaso());
            txtAtualizarChegada.setText(automacaoSelecionada.getChegada());
            txtAtualizarSaida.setText(automacaoSelecionada.getSaida());
            txtAtualizarPorcentagem.setText(automacaoSelecionada.getPorcentagem());            

            tabPaneAutomacaoQA.getSelectionModel().select(tabAtualizarQA);
        }
    }

    public void atualizarQA() {
        AutomacaoQA automacaoSelecionada = tablesAutomacaoQA.getSelectionModel().getSelectedItem();

        if (automacaoSelecionada != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoQA SET produto = ?, selo = ?, descricao = ?, caso = ?, chegada = ?, saida = ?, porcentagem = ? WHERE id = ?")) {

                stmt.setString(1, txtAtualizarProduto.getText());
                stmt.setString(2, txtAtualizarSelo.getText());
                stmt.setString(3, txtAtualizarDescricaoQA.getText());
                stmt.setString(4, cmbAtualizarCaso.getValue());                
                stmt.setString(5, txtAtualizarChegada.getText());
                stmt.setString(6, txtAtualizarSaida.getText());
                stmt.setString(7, txtAtualizarPorcentagem.getText());                
                stmt.setInt(8, automacaoSelecionada.getId());

                stmt.executeUpdate();
                carregarQA();  // Atualiza a tabela após a atualização

                tabPaneAutomacaoQA.getSelectionModel().select(tabListarQA);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação atualizada com sucesso!");
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar automação: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para atualizar!");
        }
    }
    // Método para limpar os campos do formulário
    public void limparCampos() {
        txtProduto.clear();
        txtSelo.clear();
        txtDescricaoQA.clear();
        cmbCaso.setValue(null);
        txtChegada.clear();
        txtSaida.clear();
        txtPorcentagem.clear();
    }

    public void limparQA() {
        filtroProduto.clear();
        filtroSelo.clear();
        cmbfiltroCaso.getSelectionModel().clearSelection();
        tablesAutomacaoQA.setItems(listaAutomacaoQA);
    }

    // Método para mostrar alertas
    public void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensagem);
            alerta.showAndWait();
        });
    }

    // Método de inicialização
    @FXML
    public void initialize() {
        // Configuração das colunas da tabela
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colSelo.setCellValueFactory(new PropertyValueFactory<>("selo"));
        colDescricaoQA.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCaso.setCellValueFactory(new PropertyValueFactory<>("caso"));
        colChegada.setCellValueFactory(new PropertyValueFactory<>("chegada"));
        colSaida.setCellValueFactory(new PropertyValueFactory<>("saida"));
        colPorcentagem.setCellValueFactory(new PropertyValueFactory<>("porcentagem"));

        // Preenchendo os ComboBoxes
        cmbCaso.getItems().addAll("Disponível", "Indisponível", "Reservado", "Em Manutenção", "Em Operação");
        cmbfiltroCaso.getItems().addAll("Disponível", "Indisponível", "Reservado", "Em Manutenção", "Em Operação");
        cmbAtualizarCaso.getItems().addAll("Disponível", "Indisponível", "Reservado", "Em Manutenção", "Em Operação");
         
        tablesAutomacaoQA.setItems(listaAutomacaoQA);

        // Carregar dados ao inicializar
        carregarQA();

        tablesAutomacaoQA.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
            }
        });
    }
}




