package de.haw.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ConnectionTest.class, PutTest.class, GetTest.class, SaveLoadTest.class})
public class DBTest {}
