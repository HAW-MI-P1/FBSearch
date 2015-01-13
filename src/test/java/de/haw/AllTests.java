package de.haw;

import de.haw.controller.ControllerTests;
import de.haw.db.DBTests;
import de.haw.fuzzy.FuzzyTests;
import de.haw.parser.ParserTest;
import de.haw.wrapper.WrapperTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({SearchHistoryTests.class, ControllerTests.class, DBTests.class, FuzzyTests.class, GeneralTests.class, ParserTest.class, WrapperTests.class })
public class AllTests {}
