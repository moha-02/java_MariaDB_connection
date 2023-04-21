package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.Arrays;
import java.util.List;

public class Application {
    //Static para poder utilizarlo en el metodo main
    private static JpaService jpaService = JpaService.getInstance();
    public static void main(String[] args) {
           try{
               createProgrammingLanguage();
               printProgrammingLanguage();

        } finally {
            jpaService.shutDown();
        }

    }


    private static void createProgrammingLanguage(){
        jpaService.runInTransaction(entityManager -> {
            Arrays.stream( "Java,JavaSccript,C++,C#,Python,Go,Rust,PHP".split(","))
                    .map(name ->  new ProgrammingLanguage(name,(int)(Math.random()* 10)))
                    .forEach(entityManager::persist);
            return null;
        });
    }
    private static void printProgrammingLanguage() {
        List<ProgrammingLanguage> list = jpaService.runInTransaction(entityManager ->
            entityManager.createQuery(
                    "select name, rating from ProgrammingLanguage where rating > 5",
                    ProgrammingLanguage.class
            ).getResultList());
            list.stream()
                    .map(pl -> pl.getName() + ": " + pl.getRating())
                    .forEach(System.out::println);

    }

}
