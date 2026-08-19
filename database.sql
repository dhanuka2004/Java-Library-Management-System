CREATE DATABASE LibraryDB;
GO

USE LibraryDB;
GO

CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    isAvailable BIT
);

CREATE TABLE members (
    memberId VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    password VARCHAR(50)
);