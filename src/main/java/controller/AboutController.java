package controller;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides methods for handling actions on controls in the About window
 */
public class AboutController {
    /**
     * Forwards the user to CamposmDev's Github
     * @param e
     * @throws URISyntaxException
     * @throws IOException
     */
    public void github(ActionEvent e) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://github.com/CamposmDev"));
        }
    }

    /**
     * Forwards the user to the author of the repair icon
     * @param e
     * @throws URISyntaxException
     * @throws IOException
     */
    public void attributeIcon(ActionEvent e) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://www.flaticon.com/free-icons/repair"));
        }
    }
}
