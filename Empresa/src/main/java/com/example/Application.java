package com.example;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class Application {
    //Para plain jdbc
    private static Connection connection;
    //Para el pool
    private static HikariDataSource dataSource;
    //Para el JPA
    private static JpaService jpaService = JpaService.getInstance();
    public static void main(String[] args) throws SQLException {

                try{
                    //Establezco conexion de las 3 maneras
                    createProgrammingLanguage();
                    //JPA, me mostrará lo que hay en la base de datos.
                    printProgrammingLanguage();
                    openDatabaseConnection();
                    initDatabaseConnectionPool();
                    //Inserto data en la base de datos con el plain y con el pool
                    createData("Manu","perez","musica",5000);
                    createData(JOptionPane.showInputDialog("Introduce nombre"),JOptionPane.showInputDialog("Introduce apellido"), JOptionPane.showInputDialog("Introduce departamento"),Integer.parseInt(JOptionPane.showInputDialog("Introduce salario")));
                    createDataPool(JOptionPane.showInputDialog("Introduce nombre"),JOptionPane.showInputDialog("Introduce apellido"), JOptionPane.showInputDialog("Introduce departamento"),Integer.parseInt(JOptionPane.showInputDialog("Introduce salario")));
                    //visualizo con el plain lo que hay en la base de datos despues de insertar datos
                    readData();
                    //Actualizo con el plain
                    updateData("Manu",4000);
                    //Leo con el pool
                    readDataPool();
                    //Ahora actualizo con el pool
                    updateDataPool(JOptionPane.showInputDialog("Nombre de la persona que cambiaras el salario: "),Integer.parseInt(JOptionPane.showInputDialog("Introduce nuevo salario")));
                    //Leo con el JPA
                    printProgrammingLanguage();
                    //Delete con jain y pool
                    deleteData(JOptionPane.showInputDialog("Introduce nombre del empleado al que desea despedir: "));
                    deleteDataPool(JOptionPane.showInputDialog("Introduce nombre del empleado al que desea despedir: "));
                    //VEO reaultados con pool
                    readDataPool();
                } finally {
                    //Cierro las conexiones establecidas
                    jpaService.shutDown();
                    closeDatabaseConnectionPool();
                    closeDatabaseConnection();
                }
    }

    //pega los metodos aqui**********************************************abajo
    //ahi van

    //metodo que permite crear data
    //recibe por parametro el nombre del lengiaje de programacion String y la valoracion de este int
    private static void createData(String name,String apellido,String departamento,int salario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
				    /*query que reserva 2 huecos donde posteriormente con el metodo set rellenaremos con los datos*/
                    INSERT INTO empleados_java_db( emp_name,emp_apellido,emp_departamento,emp_salario)
                    VALUES (?,?,?,?)
				""")) {
            //mediante los metodos set. se añaden los datos al objeto statement(objeto que "almacena" los datos)
            statement.setString(1, name);
            statement.setString(2, apellido);
            statement.setString(3, departamento);
            statement.setInt(4, salario);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        }
    }

    //este metodo nos permite leer e imptimir los datos
    private static void readData() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA IMPRIMIR NOBRE Y VALORACION DEL LENGUAJE(este lee linea por linea), ORDENADOS POR VALOR DESCENDIENTE*/
                        SELECT emp_name ,emp_apellido,emp_departamento,emp_salario
                        FROM empleados_java_db
                        ORDER BY  emp_name ASC 
				""")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean empty = true; //booleano que ejerce de interruptor para poder salir del bucle
                //bucle para iterar el query para cubrir todas las lineas
                while (resultSet.next()) {
                    empty = false;
                    //getters del nombre y valoracion
                    String name=resultSet.getString(1);
                    String apellido=resultSet.getString(2);
                    String departamento=resultSet.getString(3);
                    int salario = resultSet.getInt(4);
                    System.out.println("\t>" + name + " " + apellido +""+ " - Departamento:"+" "+departamento +"-"+ " - Salario:"+" "+salario);
                }
                if (empty) {/*condicional para en caso de  bo estar la base de datos vacia imprimir mensage de no data*/
                    System.out.println("\t (no data)");
                }
            }
        }
    }

    private static void updateData(String name, int salarioNew) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA ACTUALIZAR VALORACION BUSCANDO POR NOMBRE DE  LENGUAJE DE PROGRAMACION*/
                        UPDATE empleados_java_db
                        SET emp_salario = ?
                        WHERE emp_name = ?
				""")) {
            //setters del objeto que cubren los huecos de las "?"
            statement.setInt(1,salarioNew);
            statement.setString(2,name);
            int rowsUpdated = statement.executeUpdate(); // devuelve la cantidad de filas actualizadas
            System.out.println("Rows updated: " + rowsUpdated);//imprimir la cantidad de filas actualizadas
        }
    }
    //metodo para eliminar datos
    private static void deleteData(String nameExpression) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
                    /*QUERY PARA ENCONTRAR FILA A ELIMINAR SEGUN NOMBRE*/
                        DELETE FROM empleados_java_db
                        WHERE emp_name LIKE ?
				""")) {
            statement.setString(1, nameExpression);//setter con el nombre que corresponde al "?"
            int rowsDeleted = statement.executeUpdate();//devuelve el numero de filas eliminadas
            System.out.println("Rows deleted: " + rowsDeleted);//imprimir la cantidad de filas eliminadas
        }
    }



    ///////////////////////POOOLLLLL
    private static void createDataPool(String name,String apellido,String departamento,int salario) throws SQLException {
        System.out.println("Creating data........");
        int rowsInserted;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO empleados_java_db(emp_name,emp_apellido,emp_departamento,emp_salario)
                    VALUES (?,?,?,?)
                    """)) {
                statement.setString(1, name);
                statement.setString(2, apellido);
                statement.setString(3, departamento);
                statement.setInt(4, salario);
                rowsInserted = statement.executeUpdate();
                //TU PANTALLA NO DEJA DE TEMBLAR BRO
            }
        }
        System.out.println("Rows inserted........");
    }

    private static void readDataPool() throws SQLException {
        System.out.println("Reading data....");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT emp_name ,emp_apellido,emp_departamento,emp_salario
                        FROM empleados_java_db
                        ORDER BY  emp_name ASC 
                    """)) {
                ResultSet resultSet= statement.executeQuery();
                boolean empty = true;
                while(resultSet.next()){
                    String name= resultSet.getString(1);
                    String apellido=resultSet.getString(2);
                    String departamento=resultSet.getString(3);
                    int salario = resultSet.getInt(4);
                    System.out.println("\t>" + name + " " + apellido +" "+ " - Departamento:"+" "+departamento +" "+ " - Salario:"+" "+salario);
                    empty=false;
                }
                if (empty){
                    System.out.println("\t (no data)");
                }
            }
        }

    }

    private static void updateDataPool(String name, int salarioNew) throws SQLException {
        System.out.println("Updating data......");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        UPDATE empleados_java_db
                        SET emp_salario = ?
                        WHERE emp_name = ?
                    """)) {
                statement.setInt(1,salarioNew);
                statement.setString(2,name);
                int rowsUpdated = statement.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
            }
        }
    }

    private static void deleteDataPool(String nameExpression) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        DELETE FROM empleados_java_db
                        WHERE emp_name LIKE ?
                    """)) {
                statement.setString(1,nameExpression);
                int rowsDeleted = statement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }
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
                        "select e from EmpleadosJavaDB e where e.salario > 1000",
                        EmpleadosJavaDB.class
                ).getResultList());
        list.stream()
                .map(emp -> emp.getName() + " " + emp.getApellido() + " - Departamento: " + emp.getDepartamento() + " - Salario: " + emp.getSalario())
                .forEach(System.out::println);
    }
}
