package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoProducao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class ControllerAutomacaoProducao {

    @FXML private TextField txtProduto, txtPreco, txtLote, txtCodigo;
    @FXML private TextField txtAtualizarProduto, txtAtualizarPreco, txtAtualizarLote, txtAtualizarCodigo;
    @FXML private TextField filtroProduto, filtroPreco, filtroLote, filtroCodigo;
    @FXML private TableView<AutomacaoProducao> tablesAutomacaoProducao;
    @FXML private TableColumn<AutomacaoProducao, String> colProduto;
    @FXML private TableColumn<AutomacaoProducao, Integer> colPreco, colLote, colCodigo;
    @FXML private TabPane tabPaneAutomacaoProducao;
    @FXML private Tab tabAtualizarProducao;

    public void mostrarAlerta(String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensagem");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        listarDados();

        tablesAutomacaoProducao.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
            }
        });
    }

    public void preencherCamposAtualizacao() {
        AutomacaoProducao automacaoSelecionada = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (automacaoSelecionada != null) {
            txtAtualizarProduto.setText(automacaoSelecionada.getNome_produto());
            txtAtualizarPreco.setText(automacaoSelecionada.getPreco());
            txtAtualizarLote.setText(String.valueOf(automacaoSelecionada.getLote()));
            txtAtualizarCodigo.setText(String.valueOf(automacaoSelecionada.getCodigo()));

            tabPaneAutomacaoProducao.getSelectionModel().select(tabAtualizarProducao);
        }
    }

    public void listarDados() {
        ObservableList<AutomacaoProducao> dados = FXCollections.observableArrayList();
        String sql = "SELECT * FROM automacaoProducao";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dados.add(new AutomacaoProducao(
                    rs.getInt("id"),
                    rs.getString("nome_produto"),
                    rs.getString("preco"),
                    rs.getInt("lote"),
                    rs.getInt("codigo")
                ));
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao listar dados: " + e.getMessage(), AlertType.ERROR);
        }
        tablesAutomacaoProducao.setItems(dados);
    }

    @FXML
    public void saveProducao() {
        String sql = "INSERT INTO automacaoProducao (nome_produto, preco, lote, codigo) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtProduto.getText());
            stmt.setString(2, txtPreco.getText());
            stmt.setInt(3, Integer.parseInt(txtLote.getText()));
            stmt.setInt(4, Integer.parseInt(txtCodigo.getText()));
            stmt.executeUpdate();
            mostrarAlerta("Produção salva com sucesso!", AlertType.INFORMATION);
            limparCamposInsercao();
            listarDados();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao salvar: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public void limparCamposInsercao() {
        txtProduto.clear();
        txtPreco.clear();
        txtLote.clear();
        txtCodigo.clear();
    }

    @FXML
    public void filtrarProducao() {
        ObservableList<AutomacaoProducao> dados = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM automacaoProducao WHERE 1=1");

        if (!filtroProduto.getText().isEmpty())
            sql.append(" AND nome_produto LIKE '%").append(filtroProduto.getText()).append("%'");
        if (!filtroPreco.getText().isEmpty())
            sql.append(" AND preco LIKE '%").append(filtroPreco.getText()).append("%'");
        if (!filtroLote.getText().isEmpty())
            sql.append(" AND lote = ").append(filtroLote.getText());
        if (!filtroCodigo.getText().isEmpty())
            sql.append(" AND codigo = ").append(filtroCodigo.getText());

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql.toString())) {
            while (rs.next()) {
                dados.add(new AutomacaoProducao(
                    rs.getInt("id"),
                    rs.getString("nome_produto"),
                    rs.getString("preco"),
                    rs.getInt("lote"),
                    rs.getInt("codigo")
                ));
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao filtrar: " + e.getMessage(), AlertType.ERROR);
        }
        tablesAutomacaoProducao.setItems(dados);
    }

    @FXML
    public void limparProducao() {
        filtroProduto.clear();
        filtroPreco.clear();
        filtroLote.clear();
        filtroCodigo.clear();
        listarDados();
    }

    @FXML
    public void atualizarProducao() {
        AutomacaoProducao selecionado = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um item para atualizar.", AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Deseja realmente atualizar este produto?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            String sql = "UPDATE automacaoProducao SET nome_produto=?, preco=?, lote=?, codigo=? WHERE id=?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, txtAtualizarProduto.getText());
                stmt.setString(2, txtAtualizarPreco.getText());
                stmt.setInt(3, Integer.parseInt(txtAtualizarLote.getText()));
                stmt.setInt(4, Integer.parseInt(txtAtualizarCodigo.getText()));
                stmt.setInt(5, selecionado.getId());
                stmt.executeUpdate();
                mostrarAlerta("Produto atualizado com sucesso!", AlertType.INFORMATION);

                txtAtualizarProduto.clear();
                txtAtualizarPreco.clear();
                txtAtualizarLote.clear();
                txtAtualizarCodigo.clear();

                listarDados();
                
            } catch (SQLException e) {
                mostrarAlerta("Erro ao atualizar: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    public void DeletarProducao() {
        AutomacaoProducao selecionado = tablesAutomacaoProducao.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um item para deletar.", AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Deseja realmente deletar este produto?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM automacaoProducao WHERE id = ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, selecionado.getId());
                stmt.executeUpdate();
                mostrarAlerta("Produto deletado com sucesso!", AlertType.INFORMATION);

                txtAtualizarProduto.clear();
                txtAtualizarPreco.clear();
                txtAtualizarLote.clear();
                txtAtualizarCodigo.clear();

                listarDados();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao deletar: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }
}
