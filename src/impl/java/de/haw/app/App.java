package de.haw.app;

import java.util.Arrays;

import de.haw.controller.Controller;
import de.haw.controller.ControllerImpl;
import de.haw.db.DB;
import de.haw.db.MockUpDBImpl;
import de.haw.detector.Detector;
import de.haw.detector.DetectorImpl;
import de.haw.gui.GUI;
import de.haw.gui.GUIImpl;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.taxonomy.Taxonomy;
import de.haw.taxonomy.TaxonomyImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;

/**
 * Created by Fenja on 02.12.2014.
 */
public class App {

    public static void main (String args[]){
        Wrapper wrapper = new WrapperImpl();
        DB dbcontrol = new MockUpDBImpl();
        Parser parser = new ParserImpl();
        Detector detector = new DetectorImpl();
        Taxonomy taxonomy = new TaxonomyImpl(Arrays.asList("place"));
        Controller controller = new ControllerImpl(parser, wrapper, dbcontrol, detector, taxonomy);
        GUI gui = new GUIImpl();
        gui.setController(controller);
        gui.run();
    }
}
