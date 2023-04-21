package com.example;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.*;
import java.sql.*;

public class Application {


    private static HikariDataSource dataSource;


    public static void main(String[] args) throws SQLException {
        try {
            initDatabaseConnectionPool();
            deleteData("%");
            readData();
            createData(JOptionPane.showInputDialog("Introduce the programming language"), Integer.parseInt(JOptionPane.showInputDialog("Set the rating of the prgraming language")));
            createData(JOptionPane.showInputDialog("Introduce the programming language"), Integer.parseInt(JOptionPane.showInputDialog("Set the rating of the prgraming language")));
            createData(JOptionPane.showInputDialog("Introduce the programming language"), Integer.parseInt(JOptionPane.showInputDialog("Set the rating of the prgraming language")));
            readData();
            updateData(JOptionPane.showInputDialog("Introduce the programming language you want to update, we recomende C++ "), Integer.parseInt(JOptionPane.showInputDialog("Set the rating of the prgraming language")));
            readData();
            deleteData(JOptionPane.showInputDialog("Introduce the programming language you want to delete, we recomende C++ "));
            readData();
        } finally {
            closeDatabaseConnectionPool();
        }
    }

    //metodo que permite crear data
    //recibe por parametro el nombre del lengiaje de programacion String y la valoracion de este int
    private static void createData(String name, int rating) throws SQLException {

        //crear el objeto con el nombre connection qe almacena la conexion mediante dataSource
        //añadimos un try para autocerrar la conexion
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("""
				    /*query que reserva 2 huecos donde posteriormente con el metodo set rellenaremos con los datos*/
				    INSERT INTO programming_language(name, rating)
				    VALUES (?, ?)
				""")) {
                //mediante los metodos set. se añaden los datos al objeto statement(objeto que "almacena" los datos)
                statement.setString(1, name);
                statement.setInt(2, rating);
                int rowsInserted = statement.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }
        }
    }

    //este metodo nos permite leer e imptimir los datos
    private static void readData() throws SQLException {

        //instanciar objeto connection que permite la conexion y pasarlo com variable del objeto statement
        try (Connection connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA IMPRIMIR NOBRE Y VALORACION DEL LENGUAJE(este lee linea por linea), ORDENADOS POR VALOR DESCENDIENTE*/
				    SELECT name, rating
				    FROM programming_language
				    ORDER BY rating DESC
				""")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true; //booleano que ejerce de interruptor para poder salir del bucle
                    //bucle para iterar el query para cubrir todas las lineas
                    while (resultSet.next()) {
                        empty = false;
                        //getters del nombre y valoracion
                        String name = resultSet.getString("name");
                        int rating = resultSet.getInt("rating");
                        System.out.println("\t> " + name + ": " + rating);
                    }
                    if (empty) {/*condicional para en caso de  bo estar la base de datos vacia imprimir mensage de no data*/
                        System.out.println("\t (no data)");
                    }
                }
            }

        }
    }

    private static void updateData(String name, int newRating) throws SQLException {

        try (Connection connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA ACTUALIZAR VALORACION BUSCANDO POR NOMBRE DE  LENGUAJE DE PROGRAMACION*/
				    UPDATE programming_language
				    SET rating = ?
				    WHERE name = ?
				""")) {
                //setters del objeto que cubren los huecos de las "?"
                statement.setInt(1, newRating);
                statement.setString(2, name);
                int rowsUpdated = statement.executeUpdate(); // devuelve la cantidad de filas actualizadas
                System.out.println("Rows updated: " + rowsUpdated);//imprimir la cantidad de filas actualizadas
            }

        }
    }
    //metodo para eliminar datos
    private static void deleteData(String nameExpression) throws SQLException {

        try (Connection connection = dataSource.getConnection()){

            try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA ENCONTRAR FILA A ELIMINAR SEGUN NOMBRE*/
				    DELETE FROM programming_language
				    WHERE name LIKE ?
				""")) {
                statement.setString(1, nameExpression);//setter con el nombre que corresponde al "?"
                int rowsDeleted = statement.executeUpdate();//devuelve el numero de filas eliminadas
                System.out.println("Rows deleted: " + rowsDeleted);//imprimir la cantidad de filas eliminadas
            }

        }
    }

    //metodo para conectarnos a la base de datos
    private static void initDatabaseConnectionPool() {

        //objeto que almacena la url de conexion y el usuario y conraseña
        dataSource = new HikariDataSource();
        //metodos para introducir url , usuario y contraseña
        dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/jdbc_demo");
        dataSource.setUsername("root");
        dataSource.setPassword("simo123");


    }
    //metodo para cerrar la conexion
    private static void closeDatabaseConnectionPool() {

        dataSource.close();
    }
}