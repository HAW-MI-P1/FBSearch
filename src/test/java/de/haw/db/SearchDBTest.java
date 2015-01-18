package de.haw.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Fenja on 06.01.2015.
 */
public class SearchDBTest {

    SearchDB db;

    @Before
    public void setUpDB(){
        this.db = new SearchDBImpl();
    }

    @Test
    public void testInstantiation(){
        Assert.assertTrue(db != null);
    }

    @Test
    public void testTable(){
        String sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        Assert.assertTrue(db.execute(sql));
        sql = "DROP TABLE COMPANY";
        Assert.assertTrue(db.execute(sql));
    }

    @Test
    public void testInserts(){
        String sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        db.execute(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        Assert.assertTrue(db.execute(sql));
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
        Assert.assertTrue(db.execute(sql));
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        Assert.assertTrue(db.execute(sql));
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        Assert.assertTrue(db.execute(sql));

        sql = "DROP TABLE COMPANY";
        db.execute(sql);
    }

    @Test
    public void testDontCreateTableTwice(){
        String sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        Assert.assertTrue(db.execute(sql));
        sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        Assert.assertTrue(db.execute(sql));
        sql = "DROP TABLE COMPANY";
        Assert.assertTrue(db.execute(sql));
    }

    @Ignore
    @Test
    //I need this for testing
    public void dropTable(){
        String sql = "DROP TABLE COMPANY";
        Assert.assertTrue(db.execute(sql));
    }
}
