CREATE TABLE Products (
  ID INTEGER NOT NULL PRIMARY KEY,
  Name VARCHAR(20),
  Nutrition INTEGER,
  Proteins REAL,
  Fat REAL,
  Carbohydrates REAL,
  Measure INTEGER NOT NULL
);

CREATE TABLE Measures (
  ID INTEGER NOT NULL PRIMARY KEY,
  Name VARCHAR(20),
  Weight REAL
);

CREATE TABLE Consumption (
  ID INTEGER NOT NULL PRIMARY KEY,
  --UserID
  Year SMALLINT,
  Month TINYINT,
  Day TINYINT,
  Product INTEGER NOT NULL,
  Value REAL
);

CREATE TABLE Users (
  ID INTEGER NOT NULL PRIMARY KEY,
  Firstname VARCHAR(20),
  Lastname VARCHAR(40),
  Year SMALLINT,
  Month TINYINT,
  Day TINYINT,
  Height SMALLINT,
  Weight SMALLINT
);