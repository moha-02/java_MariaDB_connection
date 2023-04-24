# java_MariaDB_connection connection🔗


## MAIN OBJECTIVES 🎯

- Plain connection using JDBC
- Pool connection using JDBC
- Connection using JPA

## OUR EXAMPLE

Our project combines the jdbc and jpa methods to execute the different CRUD operations so we can have a quick look of how  both technologies work.


## PREREQUIREMENTS
- Java ☕
- IDE (we suggest IntelliJ) 💻
- MariaDB 🐋
- Maven 🐦


## MAIN DIFFERENCE BETWEEN JDBC AND JPA
1. JDBC allows us to write SQL commands to read data from and update data to a relational database. JPA, unlike JDBC, allows developers to construct database-driven Java programs utilizing object-oriented semantics.

2. With JDBC, we need to write out the full SQL query, while with JPA, we simply use annotations to create one-to-one, one-to-many, many-to-one, and many-to-many associations.

3. JDBC is database-dependent, which means that different scripts must be written for different databases. On the other side, JPA is database-agnostic, meaning that the same code can be used in a variety of databases with few (or no) modifications.

## Our Project
Our project revolves around a database that contains information about employees, and we have designed it using a single table approach provided by the company. To create this database, we utilized JPA (Java Persistence API), which allowed us to define entities and columns to represent the various data points that we wanted to store in the database.

We chose MySQL as the database management system for our project, and our primary focus was on establishing various connections between Java and the MySQL database. We used Plain JDBC, Connection Pool, and JPA to create these connections, and we explored several different ways to connect to the database.

In addition to establishing connections, we also delved into various concepts related to database management. We gained experience inserting, updating, deleting, and viewing content in the database, and we explored various techniques for optimizing these operations. For example, we learned how to use connection pools to minimize the overhead associated with establishing new connections and how to use batch updates to execute multiple operations in a single transaction.

To ensure that our project worked seamlessly, we also paid close attention to the dependencies required to make everything function properly. Specifically, we utilized the pom.xml file to manage all of the project's dependencies, and we leveraged the persistence.xml file to configure our JPA provider. With these files in place, we were able to easily manage all of the dependencies required for our project to function as intended.

Overall, our project was an excellent opportunity to gain hands-on experience working with databases and Java. We learned a great deal about how to create and manage connections, and we explored many essential concepts related to database management. Our work will undoubtedly serve us well as we continue to build our skills in these areas.


## CONCLUSION 

To sum up, The main benefit of JDBC is that it is easy to use, as well as having a very friendly syntax. However JPA is at a higher level, especially in the development of complex applications. JDBC is perfect for small programs and getting started, but JPA is superior and much more useful.


## By 👨‍💻

Fabian Ossai Ossai

Mohammed Salhi Biade


08_03_ASSI_java_mariaDB_connection

### References 📃

- MariaDb wth Java turotial : https://youtube.com/playlist?list=PLlm3ebwe5px-LA8qIqYb5aM4-zl5c1G95

- JDBC and JPA definition : https://www.baeldung.com/jpa-vs-jdbc#:~:text=JDBC%20is%20database%2Ddependent%2C%20which,few%20(or%20no)%20modifications.


