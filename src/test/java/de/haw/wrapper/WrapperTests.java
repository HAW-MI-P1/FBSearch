package de.haw.wrapper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.haw.model.types.ResultType;
import de.haw.model.types.UserType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.haw.model.WebPicture;
import de.haw.model.types.Type;

public class WrapperTests {

    Wrapper wrapper;

    @Before
    public void setUp() {
        wrapper = new WrapperImpl();
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchEmptyType() {
        List<String> test = new ArrayList<String>();
        test.add("Karl");
        Collection<Type> result = wrapper.searchForName("", test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchNullType() {
        List<String> test = new ArrayList<String>();
        test.add("Karl");
        Collection<Type> result = wrapper.searchForName(null, test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchWrongType() {
        List<String> test = new ArrayList<String>();
        test.add("Karl");
        Collection<Type> result = wrapper.searchForName("alien", test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchEmptyList() {
        List<String> test = new ArrayList<String>();
        Collection<Type> result = wrapper.searchForName("event", test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchNullList() {
        Collection<Type> result = wrapper.searchForName("event", null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchEmptyInList() {
        List<String> test = new ArrayList<String>();
        test.add("");
        Collection<Type> result = wrapper.searchForName("event", test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNameSearchNullInList() {
        List<String> test = new ArrayList<String>();
        test.add(null);
        Collection<Type> result = wrapper.searchForName("event", test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestPicSearchEmpty() {
        Collection<Type> test = new ArrayList<Type>();
        List<WebPicture> result = wrapper.getPicturesForPersons(test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestPicSearchNull() {
        List<WebPicture> result = wrapper.getPicturesForPersons(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInEmptyID() {
        wrapper.idLivesIn("", "Ohio");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInNullID() {
        wrapper.idLivesIn(null, "Ohio");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInTypoID() {
        wrapper.idLivesIn("Horst", "Ohio");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInWrongTypeID() {
        // ID of type group
        wrapper.idLivesIn("266757450046180", "Ohio");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInEmptyLoc() {
        wrapper.idLivesIn("842685159096321", "");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestLivesInNullLoc() {
        wrapper.idLivesIn("842685159096321", null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectEmpty() {
        JSONObject test = new JSONObject();
        Collection<Type> result = wrapper.collect(test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectNull() {
        Collection<Type> result = wrapper.collect(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectTypeNullInReq() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", null);
            test.append("name", "Marco Kloss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> result = wrapper.collect(test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectNameNullInReq() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", ResultType.User.getName());
            test.append("name", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> result = wrapper.collect(test);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedEmptyReq() {
        JSONObject test = new JSONObject();
        Collection<Type> test2 = new ArrayList<Type>();
        test2.add(new UserType("842685159096321", "Marco Kloss"));
        Collection<Type> result = wrapper.collectExtended(test, test2);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedNullReq() {
        Collection<Type> test2 = new ArrayList<Type>();
        test2.add(new UserType("842685159096321", "Marco Kloss"));
        Collection<Type> result = wrapper.collectExtended(null, test2);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedEmptyPOI() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", ResultType.User.getName());
            test.append("name", "Marco Kloss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> test2 = new ArrayList<Type>();
        Collection<Type> result = wrapper.collectExtended(test, test2);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedEmptyUserInPOI() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", ResultType.User.getName());
            test.append("name", "Marco Kloss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> test2 = new ArrayList<Type>();
        test2.add(new UserType("", ""));
        Collection<Type> result = wrapper.collectExtended(test, test2);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedNullUserInPOI() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", ResultType.User.getName());
            test.append("name", "Marco Kloss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> test2 = new ArrayList<Type>();
        test2.add(null);
        Collection<Type> result = wrapper.collectExtended(test, test2);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestCollectExtendedNullPOI() {
        JSONObject test = new JSONObject();
        try {
            test.append("type", ResultType.User.getName());
            test.append("name", "Marco Kloss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Type> result = wrapper.collectExtended(test, null);
    }
}
