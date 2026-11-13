# Poo-Estoque

Aplicação desktop em JavaFX para controle de estoque com persistência em PostgreSQL utilizando JDBC e o padrão DAO.

## Stack

- Java 21
- JavaFX 21
- PostgreSQL (JDBC)
- Lombok
- Maven Wrapper

## Estrutura

```text
├── schema.sql
└── src/main/java/com/example/pooestoque/
    ├── Launcher.java
    ├── Tela.java
    ├── dao/ProdutoDAO.java
    └── model/Produto.java
```

## Configuração e execução

1. Crie o banco `poo_estoque` no PostgreSQL e execute [`schema.sql`](schema.sql):

```sql
CREATE DATABASE poo_estoque;
```

2. Opcionalmente, defina as credenciais via variáveis de ambiente (padrão: `localhost:5432/poo_estoque`, usuário/senha `postgres`):

```bash
export DB_URL=jdbc:postgresql://localhost:5432/poo_estoque
export DB_USER=postgres
export DB_PASSWORD=postgres
```

3. Execute a aplicação:

```bash
./mvnw javafx:run
```
