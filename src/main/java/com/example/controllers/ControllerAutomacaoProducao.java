package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoEst;
import com.example.models.AutomacaoProducao;
import com.example.models.AutomacaoRH;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.sql.*;

public class ControllerAutomacaoProducao{
    @FXML private TableView<AutomacaoEst> tablesAutomacaoProducao;
    @FXML private TableColumn<AutomacaoEst, String> colProduto;
    @FXML private TableColumn<AutomacaoEst, String> colPreco;
    @FXML private TableColumn<AutomacaoEst, String> colLote;
    @FXML private TableColumn<AutomacaoEst, String> colCodigo;
    @FXML private TextField txtProduto;
    @FXML private TextField txtPreco;
    @FXML private TextField txtLote;
    @FXML private TextField txtCodigo;

    // Comando dos filtros
    @FXML private TextField filtroProduto;
    @FXML private TextField filtroPreco;
    @FXML private TextField filtroLote;
    @FXML private TextField filtroCodigo;

    // Controle de Tabs
    @FXML private TabPane tabPaneAutomacaoProducao;
    @FXML private Tab tabAutomacaoProducao;
    @FXML private Tab tabListarProducao;
    @FXML private Tab tabAtualizarProducao;

    // Controle filtro
    @FXML private TextField txtAtualizarProduto;
    @FXML private TextField txtAtualizarPreco;
    @FXML private TextField txtAtualizarLote;
    @FXML private TextField txtAtualizarCodigo;


    ObservableList<AutomacaoProducao> listaAutomacaoProducao = FXCollections.observableArrayList();
    

    @FXML
    private void saveProducao() {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO automacaoProducao (produto, preco, lote, codigo) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, txtProduto.getText());
            stmt.setString(2, txtPreco.getText());
            stmt.setInt(3, Integer.parseInt(txtLote.getText()));
            stmt.setInt(4, Integer.parseInt(txtCodigo.getText()));
            

            stmt.executeUpdate();

            carregarProducao();

            limparProducao();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item criado com sucesso!");
        } catch (SQLException | NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar item! " + e.getMessage());
        }
    }

    @FXML
    private void editarProducao() {
        AutomacaoProducao itemSelecionado = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (itemSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para editar.");
            return;
        }

        try {
            int preco = Integer.parseInt(txtPreco.getText());
            int lote = Integer.parseInt(txtLote.getText());
            int codigo = Integer.parseInt(txtCodigo.getText());

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoProducao SET nome_produto=?, preco=?, lote=?, codigo=? WHERE id=?")) {

                stmt.setString(1, txtProduto.getText());
                stmt.setInt(2, preco);
                stmt.setInt(3, lote);
                stmt.setInt(4, codigo);
                stmt.setInt(5, itemSelecionado.getId());

                stmt.executeUpdate();
                carregarProducao();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item atualizado com sucesso!");

            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar item: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Os campos de preço, lote e código devem ser numéricos.");
        }
    }

    @FXML
     private void DeletarProducao() {
        AutomacaoProducao itemSelecionado = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoProducao WHERE id=?")) {

                stmt.setInt(1, itemSelecionado.getId());
                stmt.executeUpdate();

                carregarProducao();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item excluído com sucesso!");

            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir item: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para excluir!");
        }
    }

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        tablesAutomacaoProducao.setItems(listaAutomacaoProducao);

        carregarProducao();

        // Configuração do clique duplo para edição de itens
        tablesAutomacaoProducao.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
            }
        });
    }

    private void carregarProducao() {
        ObservableList<AutomacaoProducao> listaAutomacaoProducao = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoProducao")) {

            while (rs.next()) {
                listaAutomacaoProducao.add(new AutomacaoProducao(
                        rs.getInt("id"),
                        rs.getString("produto"),
                        rs.getString("preco"),
                        rs.getInt("lote"),
                        rs.getInt("codigo")
                ));
            }
            tablesAutomacaoProducao.setItems(listaAutomacaoProducao);
            this.listaAutomacaoProducao = listaAutomacaoProducao;
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar estoque! " + e.getMessage());
        }
    }

    @FXML
    private void filtrarProduto() {
        FilteredList<AutomacaoProducao> dadosFiltrados = new FilteredList<>(listaAutomacaoProducao, p -> true);

        dadosFiltrados.setPredicate(produto -> {
            if (!filtroProduto.getText().isEmpty() && !produto.getNome_produto().toLowerCase().contains(filtroProduto.getText().toLowerCase())) {
                return false;
            }
            if (!filtroPreco.getText().isEmpty() && !String.valueOf(produto.getPreco()).toLowerCase().contains(filtroPreco.getText().toLowerCase())) {
                return false;
            }
            if (!filtroLote.getText().isEmpty() && !String.valueOf(produto.getLote()).toLowerCase().contains(filtroLote.getText().toLowerCase())) {
                return false;
            }
            if (!filtroCodigo.getText().isEmpty() && !String.valueOf(produto.getCodigo()).toLowerCase().contains(filtroCodigo.getText().toLowerCase())) {
                return false;
            }
            return true;
        });

        tablesAutomacaoProducao.setItems(dadosFiltrados);
    }

    private void limparAutomacaoProducao() {
        txtProduto.clear();
        txtPreco.clear();
        txtLote.clear();
        txtCodigo.clear();
    }

    @FXML
    private void limparProducao() {
        filtroProduto.clear();
        filtroPreco.clear();
        filtroLote.clear();
        filtroCodigo.clear();
        tablesAutomacaoProducao.setItems(listaAutomacaoProducao);
    }

    private void preencherCamposAtualizacao() {
        AutomacaoProducao automacaoSelecionada = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (automacaoSelecionada != null) {
            txtAtualizarProduto.setText(automacaoSelecionada.getNome_produto());
            txtAtualizarPreco.setText(automacaoSelecionada.getPreco());            
            txtAtualizarLote.setText(String.valueOf(automacaoSelecionada.getLote()));
            txtAtualizarCodigo.setText(String.valueOf(automacaoSelecionada.getCodigo()));

            tabPaneAutomacaoProducao.getSelectionModel().select(tabAtualizarProducao);
        }
    }

    public void atualizarProducao() {
        AutomacaoProducao automacaoSelecionada = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();

        if (automacaoSelecionada != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoProducao SET produto = ?, preco = ?, lote = ?, codigo = ? WHERE id = ?")) {

                stmt.setString(1, txtAtualizarProduto.getText());
                stmt.setString(2, txtAtualizarPreco.getText());
                stmt.setString(3, txtAtualizarLote.getText());
                stmt.setString(4, txtAtualizarCodigo.getText());
                stmt.setInt(5, automacaoSelecionada.getId());

                stmt.executeUpdate();

                carregarProducao();  // Atualiza a tabela após a atualização

                tabPaneAutomacaoProducao.getSelectionModel().select(tabListarProducao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação atualizada com sucesso!");
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar automação: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para atualizar!");
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensagem);
            alerta.showAndWait();
        });
        
    }


