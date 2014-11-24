package de.haw;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.haw.db.DBTest;
import de.haw.fuzzy.FuzzyTest;

@RunWith(Suite.class)
@SuiteClasses({FuzzyTest.class, DBTest.class})
public class AllTests {}
