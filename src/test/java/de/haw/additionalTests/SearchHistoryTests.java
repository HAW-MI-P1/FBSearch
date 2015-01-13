package de.haw.additionalTests;

import de.haw.model.SearchHistory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Created by Matthies on 12.01.2015.
 */
public class SearchHistoryTests {

    private SearchHistory history;

    @Test
    public void readoutTest() {
        history = new SearchHistory();

        this.history.addHistoryStep("Deine Mudda!");
        assertEquals("Deine Mudda!\n", this.history.getLabelFormattedString());
    }

    @Test
    public void readoutEmptyTest() {
        history = new SearchHistory();

        assertEquals("", this.history.getLabelFormattedString());
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void nullPointerTest1(){
        history = new SearchHistory();

        this.history.addHistoryStep(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void nullPointerTest2(){
        history = new SearchHistory();

        this.history.newHistory(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void EmptyArgTest1(){
        history = new SearchHistory();

        this.history.addHistoryStep("");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void EmptyArgTest2(){
        history = new SearchHistory();

        this.history.newHistory("");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void nullInitializedTest(){
        history = new SearchHistory(null);
    }
}
