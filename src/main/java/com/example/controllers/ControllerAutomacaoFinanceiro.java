package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoFinanceiro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class ControllerAutomacaoFinanceiro{

     @FXML private TableView<AutomacaoFinanceiro> tablesAutomacaoFinanceiro;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colIdAutFin;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colAutFin;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colSetorFin;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colCategoriaFin;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colDescricaoFin;
    @FXML private TableColumn<AutomacaoFinanceiro, String> colEstadoFin;
   
    //Inserção da automação Financeiro
    @FXML private TextField txtNomeFin;
    @FXML private TextArea txtDescricaoFin;
    @FXML private ComboBox<String> cmbSetorFin;   
    @FXML private ComboBox<String> cmbCategoriaFin;
    @FXML private ComboBox<String> cmbEstadoFin;
    


    //Atualização automação Financeiro
    @FXML private TextField txtAtuNomeFin;
    @FXML private TextArea txtAtuDescricaoFin;
    @FXML private ComboBox<String> cmbAtuSetorFin;
    @FXML private ComboBox<String> cmbAtuCategoriaFin;
    @FXML private ComboBox<String> cmbAtuEstadoFin;
   
   

    //Controle de tabs
    @FXML private TabPane tabPaneAutomacaoFinanceiro;
    @FXML private Tab tabAutomacaoFinanceiro;
    @FXML private Tab tabListarFinanceiro;
    @FXML private Tab tabAtualizarFinanceiro;    

    // Filtros
    @FXML private TextField txtFiltrarAutFin;
    @FXML private ComboBox<String> cmbFiltrarSetorFin;
    @FXML private ComboBox<String> cmbFiltrarCategoriaFin;
    @FXML private ComboBox<String> cmbFiltrarEstadoFin;
   
    public void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensagem");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void initialize(){
        colIdAutFin.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAutFin.setCellValueFactory(new PropertyValueFactory<>("nome_AutomacaoFin"));
        colSetorFin.setCellValueFactory(new PropertyValueFactory<>("setorFin"));
        colCategoriaFin.setCellValueFactory(new PropertyValueFactory<>("categoriaFin"));
        colDescricaoFin.setCellValueFactory(new PropertyValueFactory<>("descricaoFin"));
        colEstadoFin.setCellValueFactory(new PropertyValueFactory<>("estadoFin"));    
        
        cmbFiltrarSetorFin.getItems().addAll("RH", "Estoque", "Qualidade", "Produção", "Financeiro");
        cmbFiltrarCategoriaFin.getItems().addAll("Folha de pagamento", "Prestação de Serviços", "Compras e Vendas");
        cmbFiltrarEstadoFin.getItems().addAll("Em Andamento", "Finalizado");

        cmbSetorFin.getItems().addAll("RH", "Estoque", "Qualidade", "Produção", "Financeiro");
        cmbCategoriaFin.getItems().addAll("Folha de pagamento", "Prestação de Serviços", "Compras e Vendas");
        cmbEstadoFin.getItems().addAll("Em Andamento", "Finalizado");

        cmbAtuSetorFin.getItems().addAll("RH", "Estoque", "Qualidade", "Produção", "Financeiro");
        cmbAtuCategoriaFin.getItems().addAll("Folha de pagamento", "Prestação de Serviços", "Compras e Vendas");
        cmbAtuEstadoFin.getItems().addAll("Em Andamento", "Finalizado");
        
        carregarFinanceiro();

        tablesAutomacaoFinanceiro.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                PreencherCamposAtualizacaoFin();
                tabPaneAutomacaoFinanceiro.getSelectionModel().select(tabAtualizarFinanceiro);
            }
        });
    }
        

    public void PreencherCamposAtualizacaoFin() {
        AutomacaoFinanceiro automacaoSelecionada = tablesAutomacaoFinanceiro.getSelectionModel().getSelectedItem();
    
        if (automacaoSelecionada != null) {            
            txtAtuNomeFin.setText(automacaoSelecionada.getNome_AutomacaoFin() != null ? automacaoSelecionada.getNome_AutomacaoFin() : "");
            txtAtuDescricaoFin.setText(automacaoSelecionada.getDescricaoFin() != null ? automacaoSelecionada.getDescricaoFin() : "");   
           
            cmbAtuSetorFin.setValue(automacaoSelecionada.getSetorFin() != null ? automacaoSelecionada.getSetorFin() : "");
            cmbAtuCategoriaFin.setValue(automacaoSelecionada.getCategoriaFin() != null ? automacaoSelecionada.getCategoriaFin() : "");
            cmbAtuEstadoFin.setValue(automacaoSelecionada.getEstadoFin() != null ? automacaoSelecionada.getEstadoFin() : "");
    
            tabPaneAutomacaoFinanceiro.getSelectionModel().select(tabAtualizarFinanceiro);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para atualizar!");
        }
    }
    

@FXML
public void CriarAutFin(){
    try (Connection conn = Database.getConnection();
    PreparedStatement stmt = conn.prepareStatement("INSERT INTO automacaoFinanceiro (nome_automacaoFin, descricaoFin, setorFin, categoriaFin, estadoFin) VALUES (?, ?, ?, ?, ?)")) {

   stmt.setString(1, txtNomeFin.getText());
   stmt.setString(2, txtDescricaoFin.getText());
   stmt.setString(3, cmbSetorFin.getValue());
   stmt.setString(4, cmbCategoriaFin.getValue());
   stmt.setString(5, cmbEstadoFin.getValue());

   stmt.executeUpdate();

   carregarFinanceiro();

   txtNomeFin.clear();
   txtDescricaoFin.clear();
   cmbSetorFin.getSelectionModel().clearSelection();
   cmbCategoriaFin.getSelectionModel().clearSelection();
   cmbEstadoFin.getSelectionModel().clearSelection();

   mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item criado com sucesso!");
} catch (SQLException | NumberFormatException e) {
   mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar item! " + e.getMessage());
}

}

public void carregarFinanceiro() {
        ObservableList<AutomacaoFinanceiro> listaAutomacaoFinanceiros = FXCollections.observableArrayList();
       try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoFinanceiro")) {

            while (rs.next()) {
                listaAutomacaoFinanceiros.add(new AutomacaoFinanceiro(rs.getInt("id"), rs.getString("nome_automacaoFin"), rs.getString("descricaoFin"), rs.getString("setorFin"), rs.getString("categoriaFin"), rs.getString("estadoFin")));
            }            
        }catch (SQLException e) {
            e.printStackTrace();
        }
        tablesAutomacaoFinanceiro.setItems(listaAutomacaoFinanceiros);
    }
public void FiltrarFin(){
    ObservableList<AutomacaoFinanceiro> listaFiltrada = FXCollections.observableArrayList();

    String filtroTexto = txtFiltrarAutFin.getText().toLowerCase();
    String filtroSetor = cmbFiltrarSetorFin.getValue() != null ? cmbFiltrarSetorFin.getValue() : "Todos";
    String filtroCategoria = cmbFiltrarCategoriaFin.getValue() != null ? cmbFiltrarCategoriaFin.getValue() : "Todos";
    String filtroEstado = cmbFiltrarEstadoFin.getValue() != null ? cmbFiltrarEstadoFin.getValue() : "Todos";
    
    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM automacaoFinanceiro")) {
    
        while (rs.next()) {
            String nome = rs.getString("nome_automacaoFin");
            String descricao = rs.getString("descricaoFin");
            String setor = rs.getString("setorFin");
            String categoria = rs.getString("categoriaFin");
            String estado = rs.getString("estadoFin");
    
            boolean correspondeTexto = nome.toLowerCase().contains(filtroTexto) || descricao.toLowerCase().contains(filtroTexto);
            boolean correspondeSetor = filtroSetor.equals("Todos") || setor.equalsIgnoreCase(filtroSetor);
            boolean correspondeCategoria = filtroCategoria.equals("Todos") || categoria.equalsIgnoreCase(filtroCategoria);
            boolean correspondeEstado = filtroEstado.equals("Todos") || estado.equalsIgnoreCase(filtroEstado);
    
            if (correspondeTexto && correspondeSetor && correspondeCategoria && correspondeEstado) {
                listaFiltrada.add(new AutomacaoFinanceiro(
                        rs.getInt("id"),
                        nome,
                        descricao,
                        setor,
                        categoria,
                        estado
                ));
            }
        }
    
        tablesAutomacaoFinanceiro.setItems(listaFiltrada);
    
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void LimparFiltroFin(){
    txtFiltrarAutFin.clear();
    cmbFiltrarSetorFin.setValue(null);
    cmbFiltrarCategoriaFin.setValue(null);
    cmbFiltrarEstadoFin.setValue(null);

    carregarFinanceiro(); // Recarrega todos os dados sem filtro
}

@FXML
public void atualizarFin() {
    AutomacaoFinanceiro automacaoSelecionada = tablesAutomacaoFinanceiro.getSelectionModel().getSelectedItem();

    if (automacaoSelecionada != null) {
        // Get the values from the UI components and ensure they are not null or empty
        String nome = txtAtuNomeFin.getText();
        String descricao = txtAtuDescricaoFin.getText();
        String setor = cmbAtuSetorFin.getValue() != null ? cmbAtuSetorFin.getValue() : "Todos";
        String categoria = cmbAtuCategoriaFin.getValue() != null ? cmbAtuCategoriaFin.getValue() : "Todos";
        String estado = cmbAtuEstadoFin.getValue() != null ? cmbAtuEstadoFin.getValue() : "Todos";

        // Check if any mandatory fields are empty
        if (nome.isEmpty() || descricao.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Nome e Descrição não podem ser vazios!");
            return; // Stop execution if fields are empty
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE automacaoFinanceiro SET nome_automacaoFin = ?, descricaoFin = ?, setorFin = ?, categoriaFin = ?, estadoFin = ? WHERE id = ?")) {

            // Set the values for the prepared statement
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, setor);
            stmt.setString(4, categoria);
            stmt.setString(5, estado);
            stmt.setInt(6, automacaoSelecionada.getId());

            // Execute the update
            stmt.executeUpdate();

            // Reload the data and update the table view
            carregarFinanceiro(); // Atualiza a tabela após a atualização
            tablesAutomacaoFinanceiro.refresh(); // Refresh the table view

            txtAtuNomeFin.clear();
            txtAtuDescricaoFin.clear();
            cmbAtuSetorFin.setValue(null);
            cmbAtuCategoriaFin.setValue(null);
            cmbAtuEstadoFin.setValue(null);

            // Inform the user that the update was successful
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação atualizada com sucesso!");

            // Select the "Listar Financeiro" tab after updating
            tabPaneAutomacaoFinanceiro.getSelectionModel().select(tabListarFinanceiro);

        } catch (SQLException e) {
            // Handle the exception if the update fails
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar automação: " + e.getMessage());
        }
    } else {
        // Inform the user if no item is selected
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para atualizar!");
    }
}

public void deleteFin() {
    AutomacaoFinanceiro automacaoSelecionada = tablesAutomacaoFinanceiro.getSelectionModel().getSelectedItem();
    
    if (automacaoSelecionada != null) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM automacaoFinanceiro WHERE id = ?")) {
            
            stmt.setInt(1, automacaoSelecionada.getId());
            stmt.executeUpdate();
            
            txtAtuNomeFin.clear();            
            txtAtuDescricaoFin.clear();
            cmbAtuSetorFin.setValue(null);
            cmbAtuCategoriaFin.setValue(null);
            cmbAtuEstadoFin.setValue(null);
            
            
            carregarFinanceiro(); // Atualiza a tabela após a exclusão
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Automação excluída com sucesso!");
            tabPaneAutomacaoFinanceiro.getSelectionModel().select(tabListarFinanceiro);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir automação: " + e.getMessage());
        }
    } else {
        mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma automação para excluir!");
    }
}
}



