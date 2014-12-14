package de.haw.model.types;

/**
 * Created by Fenja on 13.12.2014.
 */
public enum ResultType {

    Category("category"),
    Event("event"),
    Group("group"),
    Location("location"),
    Page("page"),
    Place("place"),
    User("user");

    public String name;

     ResultType(String name) {
         this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
