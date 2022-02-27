package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.SearchEngineController;

import javax.swing.*;
import java.awt.*;

/**
 * Das Haupt-Frame. Besteht aus einer Top-Bar oben, in der der Titel der aktuellen View und einige Buttons angezeigt
 * werden, sowie das Haupt-Fenster darunter, in das die jeweils angezeigte View (Admin View, Result View oder
 * Search View) geladen wird.
 */
public class SearchEngineView extends JFrame {
    private SearchEngineController searchEngineController;

    private TopBar topBar;
    private JPanel body;

    private CardLayout cLayout;

    // TODO: Evtl. mehr Attribute ?? (nicht unbedingt nötig)

    public SearchEngineView() {
        super("PinguPinguLos");
    }

    public void setSearchEngineController(SearchEngineController searchEngineController) {
        this.searchEngineController = searchEngineController;
    }

    /**
     * Initialisiert die Search Engine View mit einer neuen Top-Bar in 'topBar' und einem Panel in 'body',
     * in dem, je nach Programmzustand, je eines der drei übergebenen Views angezeigt werden kann.
     * Als Erstes wird die dabei Search View angezeigt.
     * Setzt außerdem die vier Attribute 'searchEngineController', 'adminView', 'resultView'
     * und 'searchView' auf die entsprechenden Parameter.
     *
     * @param searchEngineController Ein Search Engine Controller
     * @param adminView              Eine Admin View
     * @param resultView             Eine Result View
     * @param searchView             Eine Search View
     */
    public void init(SearchEngineController searchEngineController, AdminView adminView, ResultView resultView,
                     SearchView searchView) {
        // TODO: Implementieren
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        topBar = new TopBar(searchEngineController);
        add(topBar, BorderLayout.PAGE_START);

        body = new JPanel(new CardLayout());

        this.searchEngineController = searchEngineController;

        cLayout = new CardLayout();
        body.setLayout(cLayout);
        body.add(adminView, "admin");
        body.add(resultView, "result");
        body.add(searchView, "search");

        add(body, BorderLayout.CENTER);

        displaySearchView();
        setVisible(true);
    }

    /**
     * Diese drei Methoden zeigen die dem Namen entsprechende View an.
     * Dabei wird der Titel in der Top-Bar korrekt gesetzt und die Buttons in ihr, die in dieser View nicht angezeigt
     * werden sollen, werden versteckt.
     */
    public void displayAdminView() {
        // TODO: Implementieren
        topBar.setTitle("Admin View");
        topBar.setAllButtonsVisible();
        topBar.hideToAdminViewButton();

        try {
            cLayout.show(body, "admin");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void displayResultView() {
        // TODO: Implementieren
        topBar.setTitle("Search Results");
        topBar.setAllButtonsVisible();
        topBar.hideCrawlButton();

        try {
            cLayout.show(body, "result");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void displaySearchView() {
        // TODO: Implementieren
        topBar.setTitle("Search");
        topBar.setAllButtonsVisible();
        topBar.hideCrawlButton();
        topBar.hideToSearchViewButton();

        try {
            cLayout.show(body, "search");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
