package de.haw.app;

import de.haw.controller.Controller;
import de.haw.controller.ControllerImpl;
import de.haw.db.DB;
import de.haw.db.DBImpl;
import de.haw.detector.Detector;
import de.haw.detector.DetectorImpl;
import de.haw.fuzzy.Fuzzy;
import de.haw.fuzzy.FuzzyImpl;
import de.haw.gui.GUI;
import de.haw.gui.GUIImpl;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.taxonomy.Taxonomy;
import de.haw.taxonomy.TaxonomyImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;

import java.util.Arrays;

/**
 * Created by Fenja on 02.12.2014.
 */
public class App {

    public static void main (String args[]){
        Wrapper wrapper = new WrapperImpl();
        DB dbcontrol = new DBImpl();
        Parser parser = new ParserImpl();
        Detector detector = new DetectorImpl();
        Taxonomy taxonomy = new TaxonomyImpl(Arrays.asList("place"));
        Fuzzy fuzzy = new FuzzyImpl();
        Controller controller = new ControllerImpl(parser, wrapper, dbcontrol, detector, taxonomy, fuzzy);
        GUI gui = new GUIImpl();
        gui.setController(controller);
        gui.run();
    }
}
