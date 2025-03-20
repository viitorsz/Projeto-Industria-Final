package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoEst;
import com.example.models.AutomacaoRH;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.sql.*;

public class AutomacaoEstoque {
    @FXML private TableView<AutomacaoEst> tablesAutomacaoEstoque;
    @FXML private TableColumn<AutomacaoEst, String> colMaterial;
    @FXML private TableColumn<AutomacaoEst, String> colDescricaoEst;
    @FXML private TableColumn<AutomacaoEst, String> colQuantidade;
    @FXML private TableColumn<AutomacaoEst, String> colEstado;
    @FXML private TextField txtMaterial;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtQuantidade;
    @FXML private ComboBox<String> cmbEstado;


    @FXML private TextField filtrarMaterial;
    @FXML private TextField filtrarQuantidade;
    @FXML private TextField filtrarEstado;

    ObservableList<AutomacaoEst> listaAutomacaoEst = FXCollections.observableArrayList();
    

@FXML
private void criarEstoque() {
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO automacaoEst (material, quantidade, descricao, estado) VALUES (?, ?, ?, ?)")) {

        stmt.setString(1, txtMaterial.getText());
        stmt.setInt(2, Integer.parseInt(txtQuantidade.getText()));
        stmt.setString(3, txtDescricao.getText());
        stmt.setString(4, cmbEstado.getValue());

        stmt.executeUpdate();

        carregarEstoque();

        limparEstoque();

        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item criado com sucesso!");
    } catch (SQLException | NumberFormatException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar item! " + e.getMessage());
    }
}

@FXML
private void editarEstoque() {
    AutomacaoEst itemSelecionado = tablesAutomacaoEstoque.getSelectionModel().getSelectedItem();
    if (itemSelecionado == null) {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para editar.");
        return;
    }

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoEst SET material=?, quantidade=?, descricao=?, estado=? WHERE id=?")) {

        stmt.setString(1, txtMaterial.getText());
        stmt.setInt(2, Integer.parseInt(txtQuantidade.getText()));
        stmt.setString(3, txtDescricao.getText());
        stmt.setString(4, cmbEstado.getValue());
        stmt.setInt(5, itemSelecionado.getId());

        stmt.executeUpdate();

        carregarEstoque();

        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item atualizado com sucesso!");
    } catch (SQLException | NumberFormatException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar item! " + e.getMessage());
    }
}

@FXML
private void deletarEstoque() {
    AutomacaoEst itemSelecionado = tablesAutomacaoEstoque.getSelectionModel().getSelectedItem();
    if (itemSelecionado == null) {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para excluir.");
        return;
    }

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoEst WHERE id=?")) {

        stmt.setInt(1, itemSelecionado.getId());
        stmt.executeUpdate();
        
        carregarEstoque();

        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item excluído com sucesso!");
    } catch (SQLException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir item! " + e.getMessage());
    }
}

@FXML
public void initialize() {
    colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
    colDescricaoEst.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

    cmbEstado.getItems().addAll("Disponível", "Indisponível", "Reservado");

    carregarEstoque();
}

private void carregarEstoque() {
    

    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoEst")) {

        while (rs.next()) {
            listaAutomacaoEst.add(new AutomacaoEst(
                rs.getInt("id"),
                rs.getString("material"),
                rs.getString("descricao"),
                rs.getInt("quantidade"),
                rs.getString("estado")
            ));
        }

        tablesAutomacaoEstoque.setItems(listaAutomacaoEst);

    } catch (SQLException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar estoque! " + e.getMessage());
    }
}

@FXML
    private void filtrarEstoque() {
                FilteredList<AutomacaoEst> dadosFiltrados = new FilteredList<>(listaAutomacaoEst, p -> true);

        dadosFiltrados.setPredicate(produto -> {
            if (!filtrarMaterial.getText().isEmpty() && !produto.getMaterial().toLowerCase().contains(filtrarMaterial.getText().toLowerCase())) {
                return false;
            }
            if (!filtrarQuantidade.getText().isEmpty() && !String.valueOf(produto.getQuantidade()).toLowerCase().contains(filtrarQuantidade.getText().toLowerCase())) {
                return false;
            }
            if (!filtrarEstado.getText().isEmpty() && !String.valueOf(produto.getEstado()).toLowerCase().contains(filtrarEstado.getText().toLowerCase())) {
                return false;
            }
                        return false;           
        });

        tablesAutomacaoEstoque.setItems(dadosFiltrados);
    }
    
 private void limparEstoque(){
    txtMaterial.clear();
    txtQuantidade.clear();
    txtDescricao.clear();
    cmbEstado.setValue(null);
 }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensagem);
            alerta.show();
        });
    }
}