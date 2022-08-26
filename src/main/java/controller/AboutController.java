package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController {
    public void github(ActionEvent e) throws URISyntaxException, IOException {
        if (e.getSource() instanceof Hyperlink) {
            Hyperlink link = (Hyperlink) e.getSource();
            final String s = link == null ? "" : link.getText();
            System.out.println(s);
            switch (s) {
                case "Michael Campos":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://github.com/CamposmDev"));
                    }
                    break;
            }
        }
    }

    public void attributeIcon(ActionEvent e) throws URISyntaxException, IOException {
        if (e.getSource() instanceof Hyperlink) {
            Hyperlink link = (Hyperlink) e.getSource();
            final String s = link == null ? "" : link.getText();
            System.out.println(s);
            switch (s) {
                case "Flaticon":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://www.flaticon.com/free-icons/repair"));
                    }
                    break;
            }
        }
    }
}
