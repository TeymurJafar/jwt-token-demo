IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'jwt_db')
BEGIN
    CREATE DATABASE jwt_db;
END