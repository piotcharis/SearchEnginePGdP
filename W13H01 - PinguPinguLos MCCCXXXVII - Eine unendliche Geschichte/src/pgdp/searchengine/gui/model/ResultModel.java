package pgdp.searchengine.gui.model;

import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import java.util.LinkedList;
import java.util.List;

/** Stellt den Inhalt der Result-Page intern dar.
 *  Eine Referenz auf die 'LinkedDocumentCollection', die den Berechnungen zugrunde liegt,
 *  das aktuelle Suchergebnis als vollständige Liste aller 'LinkedDocument's nach Relevanz
 *  bzgl. des Suchbegriffs sortiert, sowie die Anzahl derzeit angezeigter Dokumente werden
 *  unterhalten.
 */
public class ResultModel {
    private final LinkedDocumentCollection documentCollection;

    private List<LinkedDocument> resultsSortedByRelevance;
    private int numberOfLoadedDocuments;

    private static final int BATCH_SIZE = 10;

    public ResultModel(LinkedDocumentCollection documentCollection) {
        this.documentCollection = documentCollection;
        clear();
    }

    /** Berechnet die Liste der nächsten BATCH_SIZE LinkedDocument-Objekte im Ranking.
     *  Das heißt die nächsten BATCH_SIZE von all denen, die noch nicht angezeigt werden.
     *
     * @return Liste mit BATCH_SIZE LinkedDocument-Objekten
     */
    public List<LinkedDocument> getNextBatch() {
        if(resultsSortedByRelevance == null) {
            return new LinkedList<>();
        }

        int number = Math.min(numberOfLoadedDocuments + BATCH_SIZE, resultsSortedByRelevance.size());

        var output = resultsSortedByRelevance.subList(numberOfLoadedDocuments, number);
        numberOfLoadedDocuments = number;
        return output;
    }

    /** Berechnet die Attribute 'resultsSortedByRelevance' und 'numberOfLoadedDocuments' bzgl. einer neuen
     *  Suchanfrage neu. Letzteres wird insb. auf 0 gesetzt.
     *
     * @param query Such-String
     */
    public void computeResult(String query) {
        clear();
        for(AbstractLinkedDocument document : documentCollection.query(query, 0.85, 0.6, 50)) {
            if(document instanceof LinkedDocument linkedDocument) {
                resultsSortedByRelevance.add(linkedDocument);
            }
        }
    }

    /** Löscht 'resultsSortedByRelevance' (= setzt es auf eine leere Liste)
     *  und setzt 'numberOfLoadedDocuments' auf 0.
     */
    public void clear() {
        resultsSortedByRelevance = new LinkedList<>();
        numberOfLoadedDocuments = 0;
    }
}
