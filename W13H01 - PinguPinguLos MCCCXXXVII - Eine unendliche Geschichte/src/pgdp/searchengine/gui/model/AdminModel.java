package pgdp.searchengine.gui.model;

import pgdp.searchengine.networking.PageCrawling;
import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import java.util.LinkedList;
import java.util.List;

/** Stellt den Inhalt der Admin View intern dar.
 *  Unterhält dabei eine Referenz auf die den Berechnungen zugrunde liegende 'LinkedDocumentCollection',
 *  eine Liste mit allen Dokumenten darin, sortiert nach ID und die Anzahl an derzeit in der Admin View
 *  angezeigten Dokumenten aus dieser Liste.
 */
public class AdminModel {
    private final LinkedDocumentCollection documentCollection;

    private List<AbstractLinkedDocument> documentsSortedById;
    private int numberOfLoadedDocuments;

    private static final int BATCH_SIZE = 10;

    public AdminModel(LinkedDocumentCollection documentCollection) {
        this.documentCollection = documentCollection;
    }

    /** Berechnet die Liste mit den BATCH_SIZE nächsten AbstractLinkedDocument-Objekte,
     *  welche noch nicht in der Admin View angezeigt werden.
     *
     * @return Liste mit BATCH_SIZE AbstractLinkedDocument-Objekten
     */
    public List<AbstractLinkedDocument> getNextBatch() {
        if(documentsSortedById == null) {
            return new LinkedList<>();
        }

        int newNumber = Math.min(numberOfLoadedDocuments + BATCH_SIZE, documentsSortedById.size());

        var output = documentsSortedById.subList(numberOfLoadedDocuments, newNumber);
        numberOfLoadedDocuments = newNumber;
        return output;
    }

    /** Berechnet die Liste mit den ersten 'numberOfLoadedDocuments' AbstractLinkedDocument-Objekten
     *  in 'documentsSortedById' insgesamt.
     *
     * @return Liste mit 'numberOfLoadedDocuments' AbstractLinkedDocument-Objekten
     */
    public List<AbstractLinkedDocument> getDocumentsSoFar() {
        return documentsSortedById.subList(0, Math.min(numberOfLoadedDocuments, documentsSortedById.size()));
    }

    /** Lädt eine nach ID sortierte Liste aller Dokumente in 'documentCollection'
     *  in die Variable 'documentsSortedById'
     */
    public void loadListOfAllDocuments() {
        documentsSortedById = documentCollection.toListSortedById();
    }

    /** Ruft 'PageCrawling.crawlPage()' auf der übergebenen Adresse auf und lädt dann die Dokumente neu.
     *
     * @param address Zu crawlende Adresse
     */
    public void crawlPageWithAddress(String address) {
        PageCrawling.crawlPage(documentCollection, address);
        loadListOfAllDocuments();
    }

    /** Ruft 'PageCrawling.crawlPages()' auf der übergebenen Adresse mit der übergebenen
     *  Anzahl an zu crawlenden Seiten auf und lädt dann die Dokumente neu.
     *
     * @param amount Anzahl an zu crawlenden Seiten
     * @param address Zu crawlende Adresse
     */
    public void crawlPageWithAddress(int amount, String address) {
        PageCrawling.crawlPages(documentCollection, amount, address);
        loadListOfAllDocuments();
    }
}
