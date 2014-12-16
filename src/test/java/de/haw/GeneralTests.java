package de.haw;

import de.haw.model.types.Type;
import de.haw.model.types.UserType;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by Fenja on 15.12.2014.
 */
public class GeneralTests {

    @Test
    public void equalsTest(){
        UserType user1 = new UserType("12345678","Max Mustermann");
        UserType user2 = new UserType("12345678","Max Mustermann");
        UserType user3 = new UserType("99999999","Maxi");
        UserType user4 = new UserType("12345678","Susi Schulz");

        assertEquals(user1, user2);
        assertNotSame(user1, user3);
        assertEquals(user1, user4);
    }

    @Test
    public void retainTest(){
        Collection<Type> collection1 = new HashSet<Type>();
        collection1.add(new UserType("12345678","Max Mustermann"));
        collection1.add(new UserType("99999999","Maxi"));

        Collection<Type> collection2 = new HashSet<Type>();
        collection2.add(new UserType("12345678","Max Mustermann"));
        collection2.add(new UserType("23455334","Amy Winehouse"));
        collection2.add(new UserType("56435787","Ozzy"));
        collection2.add(new UserType("99999999","Maxi"));

        collection1.retainAll(collection2);
        assertEquals(1, collection1.size());
    }

}
