package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;

/** Stellt ein Panel dar, in dem ein einzelnes DummyLinkedDocument-Objekt in der Admin View angezeigt wird.
 */
public class DummyDocumentPane extends AbstractDocumentPane {

    public DummyDocumentPane(int id, String address, AdminController adminController) {
        super(id, address);
        setBackground(Color.RED);

        JButton crawlButton = new JButton("Crawl");
        crawlButton.addActionListener(actionEvent -> adminController.crawlButtonPressedForAddress(address));
        add(crawlButton);
    }

}
