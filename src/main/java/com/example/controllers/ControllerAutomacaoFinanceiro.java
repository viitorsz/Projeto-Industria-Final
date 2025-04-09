package com.example.controllers;

import com.example.database.Database;
import com.example.models.AutomacaoEst;
import com.example.models.AutomacaoFinanceiro;
import com.example.models.AutomacaoProducao;
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
            tablesAutomacaoFinanceiro.setItems(listaAutomacaoFinanceiros);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
public void FiltrarFin(){
ObservableList<AutomacaoProducao> dados = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM automacaoProducao WHERE 1=1");

        if (!txtAtuDescricaoFin.getText().isEmpty())
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

public void LimparFiltroFin(){

}












}

