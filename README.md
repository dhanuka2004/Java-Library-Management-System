# Java Library Management System

A Java console application that manages library operations such as adding books, registering members, and borrowing/returning books. All data is saved permanently to a local Microsoft SQL Server database.

## Features
* **Add Books:** Store book details (ISBN, Title, Author).
* **Register Members:** Keep track of library members.
* **Borrow & Return:** Update book availability in real-time.
* **Database Integration:** Uses JDBC to sync all actions with SQL Server.

## Prerequisites
* Java Development Kit (JDK 11 or higher)
* Microsoft SQL Server
* SQL Server JDBC Driver (`mssql-jdbc`)

## How to Run
1. Open SQL Server Management Studio (SSMS) and create a database named `LLLibraryDB`.
2. Clone this repository to your local machine.
3. Open `LibraryDatabase.java` and update the `PASS` variable with your own SQL Server password.
4. Download the SQL Server JDBC Driver and place it in the project folder.
5. Compile the program using:
   `javac -cp '.;mssql-jdbc-13.4.0.jre11.jar' *.java`
6. Run the program using:
   `java -cp '.;mssql-jdbc-13.4.0.jre11.jar' LibraryManagementSystem`
   
*(Note: The database tables will be created automatically upon running the system for the first time!)*