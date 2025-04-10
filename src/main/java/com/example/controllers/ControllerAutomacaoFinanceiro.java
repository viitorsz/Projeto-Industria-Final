package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoEst;
import com.example.models.AutomacaoFinanceiro;
import com.example.models.AutomacaoRH;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    @FXML private ComboBox<String> cmbfiltrarSetorFin;
    @FXML private ComboBox<String> cmbfiltrarCategoriaFin;
    @FXML private ComboBox<String> cmbfiltrarEstadoFin;
   
    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
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

        cmbSetorFin.getItems().addAll("RH", "Estoque", "Qualidade", "Produção", "Financeiro");
        cmbCategoriaFin.getItems().addAll("Folha de pagamento", "Prestação de Serviços", "Compras e Vendas");
        cmbEstadoFin.getItems().addAll("Em Andamento", "Finalizado");

        carregarFinanceiro();

        tablesAutomacaoFinanceiro.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1){
                PreencherCamposAtualizacaoFin();
                tabPaneAutomacaoFinanceiro.getSelectionModel().select(tabAtualizarFinanceiro);
            }
        });
    }

    public void PreencherCamposAtualizacaoFin(){
        AutomacaoFinanceiro AutomacaoFinanceiro = tablesAutomacaoFinanceiro.getSelectionModel().getSelectedItem();
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

   mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Item criado com sucesso!");
} catch (SQLException | NumberFormatException e) {
   mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar item! " + e.getMessage());
}

}

private void carregarFinanceiro() {
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
    String setorSelecionado = cmbfiltrarSetorFin.getValue();
    String categoriaSelecionada = cmbfiltrarCategoriaFin.getValue();
    String estadoSelecionado = cmbfiltrarEstadoFin.getValue();

    for (AutomacaoFinanceiro item : listaFiltrada) {
        boolean corresponde = true;

        if (filtroTexto != null && !filtroTexto.isEmpty() && 
            !item.getNome_AutomacaoFin().toLowerCase().contains(filtroTexto)) {
            corresponde = false;
        }
        if (setorSelecionado != null && !setorSelecionado.isEmpty() && 
            !item.getSetorFin().equals(setorSelecionado)) {
            corresponde = false;
        }
        if (categoriaSelecionada != null && !categoriaSelecionada.isEmpty() &&
            !item.getCategoriaFin().equals(categoriaSelecionada)) {
            corresponde = false;
        }
        if (estadoSelecionado != null && !estadoSelecionado.isEmpty() &&
            !item.getEstadoFin().equals(estadoSelecionado)) {
            corresponde = false;
        }

        if (corresponde) {
            listaFiltrada.add(item);
        }
    }

    tablesAutomacaoFinanceiro.setItems(listaFiltrada);
}



public void LimparFiltroFin(){
txtFiltrarAutFin.clear();
cmbfiltrarSetorFin.getSelectionModel().clearSelection();
cmbfiltrarCategoriaFin.getSelectionModel().clearSelection();
cmbfiltrarEstadoFin.getSelectionModel().clearSelection();
}












}

