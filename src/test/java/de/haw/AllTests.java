package de.haw;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.haw.controller.ControllerTests;
import de.haw.db.DBTests;
import de.haw.fuzzy.FuzzyTests;
import de.haw.gui.GUITests;
import de.haw.parser.ParserTests;
import de.haw.wrapper.WrapperTests;

@RunWith(Suite.class)
@SuiteClasses({ControllerTests.class, DBTests.class, FuzzyTests.class, GUITests.class, ParserTests.class, WrapperTests.class })
public class AllTests {}
