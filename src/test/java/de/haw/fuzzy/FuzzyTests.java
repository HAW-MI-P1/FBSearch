package de.haw.fuzzy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.fail;

public class FuzzyTests {

    Fuzzy fuzzy;

    @Before
    public void setUp() throws Exception {
        this.fuzzy = new FuzzyImpl();
    }

    @Test
    public void testGetSynonym() {
        Collection<String> response = this.fuzzy.getSynonym("lion", 0x0F);

        if(response.size() <= 1){ fail(); }
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void testGetSynonymWordNull() {
        this.fuzzy.getSynonym(null, 0x0F);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void testGetSynonymWordEmpty() {
        this.fuzzy.getSynonym("", 0x0F);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void testGetSynonymOptZero() {
        this.fuzzy.getSynonym("boar", 0x00);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void testGetSynonymOptInvalid() {
        this.fuzzy.getSynonym("boar", -1);
    }

    @Test
    public void testGetAllSynonymsForTypo() {
        Collection<String> response = fuzzy.getSynonym("dfsgdfgdf", 0x0F);
        Assert.assertEquals(0, response.size());
    }

    @Test
    public void testGetAllSynonymsForLion() {
        Collection<String> synonyms = fuzzy.getSynonym("lion", 0x01);
        Assert.assertEquals(13, synonyms.size());
    }

}
