package de.haw;

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

        this.history.addHistoryStep("");
        assertEquals("", this.history.getLabelFormattedString());
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void nullPointerTest(){
        history = new SearchHistory();

        this.history.addHistoryStep(null);
        this.history.newHistory(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void nullInitializedTest(){
        history = new SearchHistory(null);
        history.newHistory("dfasd");
    }
}
