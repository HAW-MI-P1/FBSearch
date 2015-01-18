package de.haw.db;

import de.haw.model.types.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fenja on 06.01.2015.
 *
 * Additional Search Results stored in DB to improve Wrapper-Results
 * Works on SQLite
 */
//http://www.tutorialspoint.com/sqlite/sqlite_java.htm
public class SearchDBImpl implements SearchDB{

    public SearchDBImpl()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        //init();
        //dropAll();
    }

    private void init(){
        String sql = "CREATE TABLE IF NOT EXISTS USER " +
                "(ID STRING PRIMARY KEY     NOT NULL," +
                " NAME              TEXT    NOT NULL, " +
                " FIRST_NAME        TEXT, " +
                " LAST_NAME         TEXT, " +
                " CITY              TEXT)";
        execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS PAGE " +
                "(ID STRING PRIMARY KEY     NOT NULL," +
                " NAME              TEXT    NOT NULL, " +
                " CATEGORY          TEXT)";
        execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS PLACE " +
                "(ID STRING PRIMARY KEY     NOT NULL," +
                " NAME              TEXT    NOT NULL, " +
                " LOCATION_ID       TEXT, " +
                " STREET            TEXT, " +
                " CITY              TEXT," +
                " STATE             TEXT," +
                " COUNTRY           TEXT)";
        execute(sql);

        sql = "INSERT INTO USER (ID,NAME,FIRST_NAME,LAST_NAME,CITY) " +
                "VALUES ('2304827788937294', 'Paul', '', '', 'Hamburg' )," +
                "('7492947203875464', 'Lotte', 'Charlotte Anna', 'Schmidt', 'Hamburg' )," +
                "('2304821118937294', 'Tommy', 'Thomas', 'Schmidt', 'Berlin' )," +
                "('2344827788937398', 'Lena', 'Anna Lena', 'Schmidt', 'Bayern' )," +
                "('2344827780087398', 'Lena', 'Alena', 'Sauertopf', 'Hamburg' )," +
                "('2004827788137398', 'Anna', 'Annabelle', 'Schulz', 'Bayern' )," +
                "('2344827788937118', 'Bella', 'Annabell', 'Maier', 'Berlin' )," +
                "('1044827328937398', 'Leni', 'Lenelies', 'Furt', 'Bremen' )," +
                "('9844111889374881', 'Felix', 'Felix', 'Schulz', 'Hamburg' )," +
                "('1344827889373958', 'Guido', 'Guido', 'Hans', 'Brandenburg' )," +
                "('3448270089237398', 'Anna', 'Anna', 'Klaus', 'Schleswig Holstein' )," +
                "('5564482758937398', 'Hänschen', 'Hans', 'Maier', 'Hamburg' )," +
                "('2341275459373982', 'Lina', 'Alina', 'Hoffmann', 'Altona' )," +
                "('4539878811937398', 'Peter', 'Jan Peter', 'Schmidt', 'Barmbek' )," +
                "('2344833788937312', 'Jan', 'Jannis', 'Krause', 'Hamburg' )," +
                "('2114827899937398', 'Klaus', 'Klaus Dieter', 'Mayer', 'Sachsen' )," +
                "('1534837889373398', 'Peter', '', '', 'Hamburg' )," +
                "('2348271788937111', 'Jan', 'Jan', 'Krause', 'Bayern' );";
        execute(sql);

        sql = "INSERT INTO PAGE (ID,NAME,CATEGORY) " +
                "VALUES ('1234567891113150', 'cats', 'animals')," +
                "('1234567892223150', 'elephants', 'animals')," +
                "('1344567891113167', 'lena', 'people')," +
                "('5344567111333167', 'dogs', 'animals');";
        execute(sql);
        sql = "INSERT INTO PLACE (ID,NAME,LOCATION_ID,STREET,CITY,STATE,COUNTRY) " +
                "VALUES ('4749578274284673', 'HAW Hamburg', '3846584748596078', 'Berliner Tor 5', 'Hamburg', 'Hamburg', 'Germany');";
        execute(sql);
    }

    private void dropAll(){
        //keep this up to date
        String sql = "DROP TABLE IF EXISTS USER";
        execute(sql);
        sql = "DROP TABLE IF EXISTS PLACE";
        execute(sql);
        sql = "DROP TABLE IF EXISTS PAGE";
        execute(sql);
    }

    public boolean execute(String sql){
        boolean success = false;
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            success = true;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
        return success;
    }

    @Override
    public Collection<Type> getEntries(ResultType type, List<String> names) {
        List<Type> results = new ArrayList<Type>();
        String sql = "SELECT * FROM "+type.getName()+" WHERE ";
        Iterator iterator = names.iterator();
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stmt = c.createStatement();
            if(type == ResultType.User) {
                while(iterator.hasNext()) {
                    String name = iterator.next().toString();
                    sql += "(NAME LIKE'" + name + "' OR FIRST_NAME LIKE'" + name + "' OR LAST_NAME LIKE'" + name + "')";
                    if(iterator.hasNext()) sql += " AND ";
                    else sql += ";";
                }
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nickName = rs.getString("name");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String city = rs.getString("city");
                    results.add(new UserType(id, nickName, firstName, lastName, city));
                    System.out.println("Fake DB: " + nickName + "; city: "+city);
                }
                rs.close();
            }else if(type == ResultType.Page){
                while(iterator.hasNext()) {
                    String name = iterator.next().toString();
                    sql += "NAME LIKE'" + name + "'";
                    if(iterator.hasNext()) sql += " AND ";
                    else sql += ";";
                }
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    results.add(new PageType(id, name, category));
                    System.out.println("Fake DB: " + name);
                }
                rs.close();
            }else if(type == ResultType.Place){
                while(iterator.hasNext()) {
                    String name = iterator.next().toString();
                    sql += "NAME LIKE'" + name + "'";
                    if(iterator.hasNext()) sql += " AND ";
                    else sql += ";";
                }
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    String location_id = rs.getString("location_id");
                    String street = rs.getString("street");
                    String city = rs.getString("city");
                    String state = rs.getString("state");
                    String country = rs.getString("country");
                    results.add(new PlaceType(id, name, category, new LocationType(location_id,street, city, state, country)));
                    System.out.println("Fake DB: " + name);
                }
                rs.close();
            }
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
        return results;
    }
}
