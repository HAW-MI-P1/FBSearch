package de.haw.db;

import de.haw.model.WebPicture;
import de.haw.model.types.*;

import java.net.URL;
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
                " CITY              TEXT," +
                " PICTURE           TEXT)";
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

        sql = "INSERT INTO USER (ID,NAME,FIRST_NAME,LAST_NAME,CITY,PICTURE) " +
                "VALUES ('2304827788937294', 'Paul', '', '', 'Hamburg','' )," +
                "('7492947203875464', 'Lotte', 'Charlotte Anna', 'Schmidt', 'Hamburg',''  )," +
                "('2304821118937294', 'Tommy', 'Thomas', 'Schmidt', 'Berlin',''  )," +
                "('2344827788937398', 'Lena', 'Anna Lena', 'Schmidt', 'Bayern','http://www.stevescottsite.com/elephant.jpg'  )," +
                "('2344827780087398', 'Lena', 'Alena', 'Sauertopf', 'Hamburg','http://graphics8.nytimes.com/images/2012/09/08/opinion/08revkin-elephant/08revkin-elephant-blog480.jpg'  )," +
                "('2004827788137398', 'Anna', 'Annabelle', 'Schulz', 'Bayern','http://i2.cdn.turner.com/cnnnext/dam/assets/130618175109-elephant-ivory-03-story-top.jpg'  )," +
                "('2344827788937118', 'Bella', 'Annabell', 'Maier', 'Berlin',''  )," +
                "('1044827328937398', 'Leni', 'Lenelies', 'Furt', 'Bremen',''  )," +
                "('9844111889374881', 'Felix', 'Felix', 'Schulz', 'Hamburg',''  )," +
                "('1344827889373958', 'Guido', 'Guido', 'Hans', 'Brandenburg',''  )," +
                "('3448270089237398', 'Anna', 'Anna', 'Klaus', 'Schleswig Holstein',''  )," +
                "('5564482758937398', 'Hänschen', 'Hans', 'Maier', 'Hamburg',''  )," +
                "('2341275459373982', 'Lina', 'Alina', 'Hoffmann', 'Altona','http://www.stevescottsite.com/elephant.jpg'  )," +
                "('4539878811937398', 'Peter', 'Jan Peter', 'Schmidt', 'Barmbek',''  )," +
                "('2344833788937312', 'Jan', 'Jannis', 'Krause', 'Hamburg',''  )," +
                "('2114827899937398', 'Klaus', 'Klaus Dieter', 'Mayer', 'Sachsen',''  )," +
                "('1534837889373398', 'Peter', '', '', 'Hamburg',''  )," +
                "('2348271788937111', 'Jan', 'Jan', 'Krause', 'Bayern',''  )," +
                "('7492998113875464', 'Lotte', 'Karlotta', 'Hoffmann', 'Hamburg',''  )," +
                "('2327821118911294', 'Anna', 'Annelise', 'Schmidt', 'Berlin','http://www.hack4fun.org/h4f/sites/default/files/bindump/lena.bmp'  )," +
                "('2675827788937668', 'Marlena', 'Marlena', '', 'Hessen','http://www.stevescottsite.com/elephant.jpg'  )," +
                "('2344437780087398', 'Lena', 'Alena', '', 'Altona','http://graphics8.nytimes.com/images/2012/09/08/opinion/08revkin-elephant/08revkin-elephant-blog480.jpg'  )," +
                "('2204827288136398', 'Sascha', '', '', 'Altona',''  )," +
                "('2342778389374118', 'Lena', 'Charlene', 'Rodriguez', 'Berlin','http://www.hack4fun.org/h4f/sites/default/files/bindump/lena.bmp'  );";
        execute(sql);

        sql = "INSERT INTO PAGE (ID,NAME,CATEGORY) " +
                "VALUES ('1234567591113150', 'cats', 'animals')," +
                "('1434567892223150', 'elephants', 'animals')," +
                "('1344567893413167', 'lena', 'people')," +
                "('5344567111593167', 'dogs', 'animals');";
        execute(sql);
        sql = "INSERT INTO PLACE (ID,NAME,LOCATION_ID,STREET,CITY,STATE,COUNTRY) " +
                "VALUES ('4749078274284673', 'HAW Hamburg', '3846584748596078', 'Berliner Tor 5', 'Hamburg', 'Hamburg', 'Germany');";
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
                    String picture = rs.getString("picture");
                    UserType user = new UserType(id, nickName, firstName, lastName, city);
                    if(picture != null && !picture.isEmpty()) {
                        List<WebPicture> pictures = new ArrayList<WebPicture>();
                        pictures.add(new WebPicture(new URL(picture)));
                        user.setPictures(pictures);
                    }
                    results.add(user);
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
