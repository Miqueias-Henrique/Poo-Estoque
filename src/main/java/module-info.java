module com.example.pooestoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok; // Necessário para o Lombok funcionar

    // Exporta o pacote principal para o JavaFX iniciar
    exports com.example.pooestoque;

    // Permite que o JavaFX (TableView) leia os atributos de Produto via reflexão
    opens com.example.pooestoque.model to javafx.base;
}