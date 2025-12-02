module com.example.pooestoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    // NOVOS REQUERIMENTOS
    requires java.sql;
    requires org.postgresql.jdbc;

    exports com.example.pooestoque;

    // Permite que o JavaFX leia os dados da classe Produto
    opens com.example.pooestoque.model to javafx.base;
}