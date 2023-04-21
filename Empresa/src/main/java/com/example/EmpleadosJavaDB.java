package com.example;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "empleados_java_db")
public class EmpleadosJavaDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer id;
    @Column(name = "emp_name")
    private  String name;
    @Column(name="emp_apellido")
    private String apellido;
    @Column(name = "emp_departamento")
    private String departamento;
    @Column(name = "emp_salario")
    private Integer salario;

    //Constructor
    public EmpleadosJavaDB() {
    }

    public EmpleadosJavaDB(String name, String apellido, String departamento, Integer salario) {
        this.name = name;
        this.apellido = apellido;
        this.departamento = departamento;
        this.salario = salario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpleadosJavaDB that = (EmpleadosJavaDB) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
//Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Integer getSalario() {
        return salario;
    }

    public void setSalario(Integer salario) {
        this.salario = salario;
    }
}
