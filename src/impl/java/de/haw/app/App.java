package de.haw.app;

import de.haw.controller.*;
import de.haw.gui.*;
import de.haw.wrapper.*;
import de.haw.parser.*;
import de.haw.db.*;

/**
 * Created by Fenja on 02.12.2014.
 */
public class App {

    public static void main (String args[]){
        Wrapper wrapper = new WrapperImpl();
        DB dbcontrol = new DBImpl();
        Parser parser = new ParserImpl();
        Controller controller = new ControllerImpl(parser, wrapper, dbcontrol);
        GUI gui = new GUIImpl();
        gui.setController(controller);
        gui.run();
    }
}
