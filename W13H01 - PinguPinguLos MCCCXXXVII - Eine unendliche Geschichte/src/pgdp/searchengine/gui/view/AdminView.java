package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;

/**
 * Stellt die Admin View dar. Sie ist als JScrollPane implementiert und zeigt
 * 1. alle bisher vom Admin Controller geladenen Dokumente untereinander in Form von AbstractDocumentPane-Objekten
 * 2. darunter noch einen Button zum Laden weiterer Dokumente
 * an.
 */
public class AdminView extends JScrollPane {
    // TODO: Komponenten hinzufügen (und evtl. noch weitere Attribute ??)
    private AdminController adminController;
    private JPanel panel;
    private JPanel docs;
    private JButton loadMore;
    private int index;

    /**
     * Erzeugt
     * 1. ein Panel (o.Ä.) für die AbstractDocumentPanes
     * 2. den Load-More-Button
     */
    public AdminView() {
        // TODO: Implementieren
        super();

        index = 0;

        JPanel button = new JPanel();
        button.setLayout(new BorderLayout());

        loadMore = new JButton("Load More");
        loadMore.addActionListener(e -> adminController.loadNextBatch());

        button.add(loadMore, BorderLayout.NORTH);

        panel = new JPanel();
        docs = new JPanel();
        docs.setLayout(new GridBagLayout());
        docs.setAlignmentX(Component.CENTER_ALIGNMENT);
        docs.setAlignmentY(Component.CENTER_ALIGNMENT);

        panel.setLayout(new BorderLayout());

        panel.add(docs, BorderLayout.CENTER);
        panel.add(button, BorderLayout.PAGE_END);

        setViewportView(panel);

        setVisible(true);
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    /**
     * Fügt das übergebene AbstractDocumentPane-Objekt unten an die bereits vorhandenen an
     * und updatet dann die Anzeige (mit einem Call der Methode 'updateUI()').
     */
    public void addDocumentPane(AbstractDocumentPane documentPane) {
        // TODO: Implementieren
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        c.gridy = index++;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 0, 0);

        documentPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        documentPane.setPreferredSize(new Dimension(350, 85));
        documentPane.setMaximumSize(new Dimension(550, 100));

        docs.add(documentPane, c);
        updateUI();
    }

    /**
     * Löscht alle angezeigten AbstractDocumentPane-Objekte aus der View
     * (nicht aber den Load-More-Button).
     */
    public void clear() {
        // TODO: Implementieren
        docs.removeAll();
    }
}
