package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.view.SearchView;

public class SearchController {
    private SearchEngineController mainController;
    private SearchView searchView;

    public SearchController(SearchEngineController mainController) {
        this.mainController = mainController;
    }

    /**
     * Teilt dem 'mainController' mit, dass eine Suche mit dem übergebenen String ausgeführt werden soll.
     *
     * @param query Der Such-String
     */
    public void executeSearch(String query) {
        // TODO: Implementieren
        mainController.processQuery(query);
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}
