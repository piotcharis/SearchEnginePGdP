package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.SearchController;

import javax.swing.*;
import java.awt.*;

/**
 * Stellt die Search View dar. Sie enthält
 * 1. den Text "PinguPinguLos" fett über dem Suchfeld und dem Search-Button.
 * 2. das Suchfeld
 * 3. den Search-Button
 */
public class SearchView extends JPanel {
    // TODO: Evtl. Attribute ??

    private SearchController searchController;
    private JLabel title;
    private JPanel searchPanel;
    private JButton searchButton;
    private JTextArea searchText;

    /**
     * Erzeugt
     * 1. Den Text "PinguPinguLos" in einer großen, fetten Schrift über den beiden anderen Komponenten.
     * 2. Ein Text-Feld, in das man Suchanfragen eingeben kann.
     * 3. Rechts neben 2. einen Search-Button, auf dessen Klick hin die Suche mit dem aktuell in 2. stehenden
     * String abgeschickt wird.
     */
    public SearchView() {
        // TODO: Implementieren
        setLayout(new GridLayout(0, 1));

        title = new JLabel("PinguPinguLos", JLabel.CENTER);
        title.setFont(new Font("Title", Font.BOLD, 36));
        title.setVerticalAlignment(JLabel.BOTTOM);

        searchPanel = new JPanel(new FlowLayout());

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchController.executeSearch(searchText.getText()));

        searchText = new JTextArea(1, 10);
        searchText.setSize(200, 100);

        searchPanel.setSize(500, 200);
        searchPanel.add(searchText);
        searchPanel.add(searchButton);

        add(title);
        add(searchPanel);

        setVisible(true);
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

}
