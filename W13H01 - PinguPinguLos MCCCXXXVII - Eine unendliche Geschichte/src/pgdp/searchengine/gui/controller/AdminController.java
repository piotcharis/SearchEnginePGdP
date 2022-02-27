package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.model.AdminModel;
import pgdp.searchengine.gui.view.AdminView;
import pgdp.searchengine.gui.view.DocumentPane;
import pgdp.searchengine.gui.view.DummyDocumentPane;
import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.DummyLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocument;

import java.util.List;

public class AdminController {
    private AdminView adminView;
    private AdminModel adminModel;

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    public void setAdminModel(AdminModel adminModel) {
        this.adminModel = adminModel;
    }

    /**
     * Teilt dem 'adminModel' mit, dass es alle Dokumente neu laden soll
     * und lädt dann das erste Batch (AdminModel.BATCH_SIZE Stück) in die Admin View.
     */
    public void loadDocuments() {
        // TODO: Implementieren
        adminModel.loadListOfAllDocuments();
        loadIntoView(adminModel.getNextBatch());
    }

    /**
     * Lädt das nächste Batch (die nächsten AdminModel.BATCH_SIZE Stück) an Dokumenten in die Admin View.
     */
    public void loadNextBatch() {
        // TODO: Implementieren
        loadIntoView(adminModel.getNextBatch());
    }

    /**
     * Lädt alle Dokumente in der übergebenen Liste in die Admin View, indem es für jeden davon
     * die korrekte 'AbstractDocumentPane' erstellt und diese dann in die Admin View einfügt.
     *
     * @param documents Liste mit in die Admin View zu ladenden Dokumenten
     */
    public void loadIntoView(List<AbstractLinkedDocument> documents) {
        // TODO: Implementieren
        for (AbstractLinkedDocument document : documents) {
            if (document instanceof DummyLinkedDocument) {
                adminView.addDocumentPane(
                        new DummyDocumentPane(document.getDocumentId(), document.getAddress(), this));
            } else {
                List<AbstractLinkedDocument> outgoingLinks =
                        ((LinkedDocument) document).getOutgoingLinks().toListSortedById();

                int[] linksTo = new int[outgoingLinks.size()];
                for (int j = 0; j < outgoingLinks.size(); j++) {
                    linksTo[j] = outgoingLinks.get(j).getDocumentId();
                }

                adminView.addDocumentPane(
                        new DocumentPane(document.getDocumentId(), document.getAddress(),
                                document.getTitle(), document.getContent(), linksTo));
            }
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn in der 'DummyDocumentPane' eines Dummy-Dokumentes der Crawl-Button
     * gedrückt wurde.
     * Crawlt diese Adresse und lädt dann die Admin View neu, da sich die Dokumente ja (eventuell) geändert haben.
     * Es sollen genauso viele Dokumente wieder in die Admin View geladen werden, wie davor angezeigt wurden,
     * also 'adminModel.numberOfLoadedDocuments' Stück.
     *
     * @param address
     */
    public void crawlButtonPressedForAddress(String address) {
        // TODO: Implementieren
        adminView.clear();
        adminModel.crawlPageWithAddress(address);
        loadIntoView(adminModel.getDocumentsSoFar());
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Crawl-Button in der Top-Bar gedrückt und der daraufhin aufpoppende
     * Dialog ausgefüllt und "abgeschickt" wurde.
     * Crawlt von der Adresse 'address' aus 'amount' Seiten und lädt dann die Dokumente neu. (Wie viele ist egal,
     * nur nicht keine.)
     *
     * @param amount  Anzahl an zu crawlenden Seiten
     * @param address Startadresse zum Crawlen
     */
    public void crawlFromAddress(int amount, String address) {
        // TODO: Implementieren
        adminView.clear();
        adminModel.crawlPageWithAddress(amount, address);
        loadDocuments();
    }
}
