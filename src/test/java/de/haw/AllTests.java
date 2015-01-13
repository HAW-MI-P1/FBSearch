package de.haw;

import de.haw.additionalTests.SearchHistoryTests;
import de.haw.additionalTests.UserTypeTests;
import de.haw.controller.ControllerTests;
import de.haw.db.DBTests;
import de.haw.fuzzy.FuzzyTests;
import de.haw.parser.ParserTest;
import de.haw.taxonomy.TaxonomyTests;
import de.haw.wrapper.WrapperTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TaxonomyTests.class, SearchHistoryTests.class, ControllerTests.class, DBTests.class, FuzzyTests.class, UserTypeTests.class, ParserTest.class, WrapperTests.class })
public class AllTests {}
