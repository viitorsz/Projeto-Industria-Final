package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoEst;
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

public class ControllerAutomacaoEstoque {
    @FXML private TableView<AutomacaoEst> tablesAutomacaoEstoque;
    @FXML private TableColumn<AutomacaoEst, String> colMaterial;
    @FXML private TableColumn<AutomacaoEst, String> colDescricaoEst;
    @FXML private TableColumn<AutomacaoEst, String> colQuantidade;
    @FXML private TableColumn<AutomacaoEst, String> colEstado;
    @FXML private TextField txtMaterial;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtQuantidade;
    @FXML private ComboBox<String> cmbEstado;


    @FXML private TextField filtroMaterialEst;
    @FXML private TextField filtroQuantidadeEst;
    @FXML private TextField filtroEstadoEst;
    @FXML private Button btnLimparEstoque;

   //controle de tabs
   @FXML private TabPane tabPaneAutomacaoEstoque;
   @FXML private Tab tabAutomacaoEstoque;
   @FXML private Tab tabListarEstoque;
   @FXML private Tab tabAtualizarEstoque;

   //controle filtro
   @FXML private TextField txtAtualizarMaterial;
   @FXML private TextField txtAtualizarQuantidade;
   @FXML private TextArea txtAtualizarDescricao;
   @FXML private ComboBox<String> cmbAtualizarEstado;
   @FXML private Button btnAtualizarEstoque;
   @FXML private Button btnDeletarEstoque;



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
private void DeletarEstoque() {
    AutomacaoEst itemSelecionado = tablesAutomacaoEstoque.getSelectionModel().getSelectedItem();
    if (itemSelecionado != null) {     
    

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoEst WHERE id=?")) {

        stmt.setInt(1, itemSelecionado.getId());
        stmt.executeUpdate();
        
        carregarEstoque();
        

        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item excluído com sucesso!");
        tabPaneAutomacaoEstoque.getSelectionModel().select(tabListarEstoque);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir item: " + e.getMessage());
        }
    } else {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um item para excluir!");
    }
}

@FXML
public void initialize() {
    colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
    colDescricaoEst.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

    cmbEstado.getItems().addAll("Disponível", "Indisponível", "Reservado");
    cmbAtualizarEstado.getItems().addAll("Disponível", "Indisponível", "Reservado");
    carregarEstoque();

    tablesAutomacaoEstoque.setOnMouseClicked((MouseEvent event) -> {
        if (event.getClickCount() > 1) {
            preencherCamposAtualizacao();
            
        }
    });
}

private void carregarEstoque() {
    ObservableList<AutomacaoEst> listaAutomacaoEst = FXCollections.observableArrayList();

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
    private void filtrarAutomacaoEst() {
        FilteredList<AutomacaoEst> dadosFiltrados = new FilteredList<>(listaAutomacaoEst, p -> true);

        dadosFiltrados.setPredicate(produto -> {
            if (!filtroMaterialEst.getText().isEmpty() && !produto.getMaterial().toLowerCase().contains(filtroMaterialEst.getText().toLowerCase())) {
                return false;
            }
            if (!filtroQuantidadeEst.getText().isEmpty() && !String.valueOf(produto.getQuantidade()).toLowerCase().contains(filtroQuantidadeEst.getText().toLowerCase())) {
                return false;
            }
            if (!filtroEstadoEst.getText().isEmpty() && !String.valueOf(produto.getEstado()).toLowerCase().contains(filtroEstadoEst.getText().toLowerCase())) {
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

 @FXML
    private void limparFiltroEstoque() {
        filtroMaterialEst.clear();
        filtroQuantidadeEst.clear();
        filtroEstadoEst.clear();    
        tablesAutomacaoEstoque.setItems(listaAutomacaoEst); 
    }
    
    private void preencherCamposAtualizacao() {
        AutomacaoEst automacaoSelecionada = tablesAutomacaoEstoque.getSelectionModel().getSelectedItem();
        if (automacaoSelecionada != null) {
            txtAtualizarMaterial.setText(automacaoSelecionada.getMaterial());
            txtAtualizarDescricao.setText(automacaoSelecionada.getDescricao());
            cmbAtualizarEstado.setValue(automacaoSelecionada.getEstado());
            txtAtualizarQuantidade.setText(String.valueOf(automacaoSelecionada.getQuantidade()));

            tabPaneAutomacaoEstoque.getSelectionModel().select(tabAtualizarEstoque);
        }
    }

    public void atualizarEstoque() {
    AutomacaoEst automacaoSelecionada = tablesAutomacaoEstoque.getSelectionModel().getSelectedItem();
    
    if (automacaoSelecionada != null) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoEst SET material = ?, descricao = ?, quantidade = ? estado = ?, WHERE id = ?")) {
            
            stmt.setString(1, txtAtualizarMaterial.getText());
            stmt.setString(2, txtAtualizarDescricao.getText());
            stmt.setString(3, txtAtualizarQuantidade.getText());
            stmt.setString(4, cmbAtualizarEstado.getValue());            
            stmt.setInt(5, automacaoSelecionada.getId());
            
            stmt.executeUpdate();
            
            carregarEstoque(); // Atualiza a tabela após a atualização

            tabPaneAutomacaoEstoque.getSelectionModel().select(tabListarEstoque);
            
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
            alerta.show();
        });
    }
}