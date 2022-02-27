package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/** Stellt den Dialog dar, der aufpoppt, wenn man in der Admin View den Crawl-Button drückt.
 *  Es wird erstens nach einer Start-Adresse zum crawlen und zweitens nach der Anzahl an zu crawlenden
 *  Seiten gefragt.
 *  Der Nutzer hat die Möglichkeit, auf einen von zwei Buttons zu drücken:
 *   1. Cancel: Bricht das Crawling ab, bevor es begonnen hat.
 *   2. Crawl: Crawlt mit den aktuell eingegebenen Parametern
 *
 *  Der Dialog zeigt sich nicht selbst an (ruft also nicht 'setVisible(true)' auf), sondern wartet,
 *  bis er von außen her (= in SearchEngineController.crawlButtonPressed() dann) sichtbar gesetzt wird.
 */
public class CrawlDialog extends JDialog {

    /** Erzeugt den Dialog wie oben beschrieben.
     *  Mittels des 'adminController's können die Buttons nach außen hin kommunizieren.
     *
     * @param adminController Ein Admin Controller
     */
    public CrawlDialog(AdminController adminController) {
        // TODO: Implementieren
        setLayout(new BorderLayout());

        JPanel pop = new JPanel();
        pop.setLayout(new GridLayout(0, 1));

        JLabel amount = new JLabel("Amount");
        JLabel address = new JLabel("Address");

        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setSize(400, 60);

        JTextField addressText = new JTextField("", 20);
        addressText.setSize(400, 50);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        JButton crawl = new JButton("Crawl");
        crawl.addActionListener(e -> {
            adminController.crawlFromAddress((Integer) spinner.getValue(), addressText.getText());
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        buttons.add(cancel);
        buttons.add(crawl);

        pop.add(amount);
        pop.add(spinner);
        pop.add(address);
        pop.add(addressText);

        pop.setSize(500, 250);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.insets = new Insets(0,20,0,20);

        panel.add(pop, c);

        setSize(500, 200);
        add(panel);
        add(buttons, BorderLayout.PAGE_END);
    }

}
