package com.example;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Application {
    //Para plain jdbc
    private static Connection connection;
    //Para el pool
    private static HikariDataSource dataSource;
    //Para el JPA
    private static JpaService jpaService = JpaService.getInstance();
    public static void main(String[] args) {

                try{
                    createProgrammingLanguage();
                    printProgrammingLanguage();

                } finally {
                    jpaService.shutDown();
                }
    }


    // Conexion PLAIN JDBC

    private static void openDatabaseConnection() throws SQLException {
        System.out.println("Connecting to database.....");
        connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/empresajdbc",
                "root","2200");
                System.out.println("Connection valid: " + connection.isValid(5));
    }
    private static void closeDatabaseConnection() throws SQLException {
        System.out.println("Closing database connection ....");
        connection.close();
        System.out.println("Connection valid: " + connection.isValid(5));
    }


    //Connection JDBC POOL
    private static void initDatabaseConnectionPool() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/empresajdbc");
        dataSource.setUsername("root");
        dataSource.setPassword("2200");


    }
    private static void closeDatabaseConnectionPool() {

        dataSource.close();
    }

    // Conexion JPA
    private static void createProgrammingLanguage(){
        jpaService.runInTransaction(entityManager -> {
            Arrays.stream(new String[][]{
                    {"Juan", "Pérez", "Ventas", "2500"},
                    {"Ana", "Gómez", "Marketing", "3000"},
                    {"Luis", "García", "Finanzas", "3500"},
                    {"María", "Torres", "Ventas", "2800"}
            }).map(data -> new EmpleadosJavaDB(data[0], data[1], data[2], Integer.parseInt(data[3]))).forEach(entityManager::persist);
            return null;
        });
    }
    private static void printProgrammingLanguage() {
        List<EmpleadosJavaDB> list = jpaService.runInTransaction(entityManager ->
                entityManager.createQuery(
                        "select e from EmpleadosJavaDB e where e.salario < 3000",
                        EmpleadosJavaDB.class
                ).getResultList());
        list.stream()
                .map(emp -> emp.getName() + " " + emp.getApellido() + " - Departamento: " + emp.getDepartamento() + " - Salario: " + emp.getSalario())
                .forEach(System.out::println);
    }
}
