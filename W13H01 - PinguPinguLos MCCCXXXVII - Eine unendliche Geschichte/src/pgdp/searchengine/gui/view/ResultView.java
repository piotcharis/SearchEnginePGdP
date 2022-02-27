package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.ResultController;

import javax.swing.*;
import java.awt.*;

/**
 * Stellt die Result View dar. Sie ist als JScrollPane implementiert und zeigt
 * 1. alle bisher vom Result Controller geladenen Ergebnisse der letzten Suchanfrage untereinander
 * in Form von ResultPane-Objekten
 * 2. darunter noch einen Button zum Laden weiterer Dokumente
 * an.
 */
public class ResultView extends JScrollPane {
    // TODO: Komponenten hinzufügen (und evtl. noch weitere Attribute ??)
    private ResultController resultController;
    private JPanel panel;
    private JButton loadMore;
    private JPanel docs;
    private int index;

    /**
     * Erzeugt
     * 1. ein Panel (o.Ä.) für die AbstractDocumentPanes
     * 2. den Load-More-Button
     */
    public ResultView() {
        // TODO: Implementieren
        super();

        index = 0;

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JPanel button = new JPanel();
        button.setLayout(new BorderLayout());

        loadMore = new JButton("Load More");
        loadMore.addActionListener(e -> resultController.loadNextBatch());

        button.add(loadMore, BorderLayout.NORTH);

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new BorderLayout());

        bigPanel.add(button, BorderLayout.PAGE_END);
        bigPanel.add(panel, BorderLayout.CENTER);

        setViewportView(bigPanel);

        setVisible(true);
    }

    public void setResultController(ResultController resultController) {
        this.resultController = resultController;
    }

    /**
     * Fügt das übergebene ResultPane-Objekt unten an die bereits vorhandenen an
     * und updatet dann die Anzeige (mit einem Call der Methode 'updateUI()').
     */
    public void addResultPane(ResultPane resultPane) {
        // TODO: Implementieren
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        c.gridy = index++;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 0, 0);

        resultPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPane.setPreferredSize(new Dimension(350, 85));
        resultPane.setMaximumSize(new Dimension(550, 100));

        panel.add(resultPane, c);
        updateUI();
    }

    /**
     * Löscht alle angezeigten ResultPane-Objekte aus der View
     * (nicht aber den Load-More-Button).
     */
    public void clear() {
        // TODO: Implementieren
        panel.removeAll();
    }

}
