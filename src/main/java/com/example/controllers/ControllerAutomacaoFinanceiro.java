package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoFinanceiro;


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
   


    private void mostrarAlerta(String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensagem");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
}

    @FXML
    public void initialize(){
        colAutFin.setCellValueFactory(new PropertyValueFactory<>("Aut. Financeiro"));
        colSetorFin.setCellValueFactory(new PropertyValueFactory<>("Setor"));
        colCategoriaFin.setCellValueFactory(new PropertyValueFactory<>("Categoria"));
        colEstadoFin.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        colDescricaoFin.setCellValueFactory(new PropertyValueFactory<>("Descrição"));

        cmbAtuCategoriaFin.getItems().addAll("Folha de pagamento", "", "");

    

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


public void CriarAutFin(){


}

public void FiltrarFin(){

}

public void LimparFiltroFin(){

}












}

