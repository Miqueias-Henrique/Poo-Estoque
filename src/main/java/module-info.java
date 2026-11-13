module com.example.pooestoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    requires java.sql;
    requires org.postgresql.jdbc;

    exports com.example.pooestoque;

    opens com.example.pooestoque.model to javafx.base;
}