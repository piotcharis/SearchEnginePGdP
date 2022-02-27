package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.WordCount;

/**
 * Stellt einen Bucket einer DocumentCollection dar. Implementiert als doubly
 * linked list.
 */
public class Bucket {
    private DocumentListElement head;
    private DocumentListElement tail;
    private int size;

    public Bucket() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Fügt das übergebene Document dem Bucket hinzu. Falls der Bucket dabei bereits
     * aufsteigend nach IDs sortiert ist, ist garantiert, dass diese Ordnung
     * beibehalten wird. In allen anderen Fällen ist nichts garantiert. Wenn das
     * übergebene Document 'null' oder bereits im Bucket vorhanden ist oder es eine
     * ungültige ID (<0) hat, wird nichts am Zustand des Bucket geändert.
     *
     * @param document Das Document, das in den Bucket eingefügt werden soll
     * @return true gdw. das Einfügen erfolgreich war
     */
    public boolean add(Document document) {
        if (document == null || document.getDocumentId() < 0) {
            return false;
        }

        if (this.head == null) {
            this.head = new DocumentListElement(document);
            this.tail = this.head;
            size++;
            return true;
        } else if (this.head.getDocumentId() > document.getDocumentId()) {
            DocumentListElement newListElement = new DocumentListElement(document);
            newListElement.setNext(this.head);
            this.head.setPre(newListElement);
            this.head = newListElement;
            size++;
            return true;
        } else if (this.head.getDocumentId() == document.getDocumentId()) {
            return false;
        } else {
            DocumentListElement currentListElement = this.head;
            while (currentListElement.getNext() != null
                    && currentListElement.getNext().getDocumentId() < document.getDocumentId()) {
                if (currentListElement.getNext().getDocumentId() == document.getDocumentId()) {
                    return false;
                }
                currentListElement = currentListElement.getNext();
            }

            if (currentListElement.getNext() != null
                    && currentListElement.getNext().getDocumentId() == document.getDocumentId()) {
                return false;
            }

            DocumentListElement newListElement = new DocumentListElement(document, currentListElement,
                    currentListElement.getNext());

            if (currentListElement.getNext() == null) {
                this.tail = newListElement;
            } else {
                currentListElement.getNext().setPre(newListElement);
            }
            currentListElement.setNext(newListElement);
            size++;
            return true;
        }
    }

    /**
     * Entfernt das übergebene DocumentListElement (nur dasselbe Objekt) aus der
     * Liste, indem sichergestellt wird, dass weder 'pre' noch 'next' eines anderen
     * DocumentListElement noch 'head' noch 'tail' des Buckets auf es zeigen.
     *
     * @param listElementToRemove Das DocumentListElement-Objekt, das entfernt
     *                            werden soll
     * @return true gdw. 'listElementToRemove' nicht 'null' ist
     */
    public boolean remove(DocumentListElement listElementToRemove) {
        if (listElementToRemove == null) {
            return false;
        }

        if (listElementToRemove.getPre() == null) {
            this.head = listElementToRemove.getNext();
        } else {
            listElementToRemove.getPre().setNext(listElementToRemove.getNext());
        }

        if (listElementToRemove.getNext() == null) {
            this.tail = listElementToRemove.getPre();
        } else {
            listElementToRemove.getNext().setPre(listElementToRemove.getPre());
        }

        size--;
        return true;
    }

    /**
     * Falls im Bucket 'this' ein DocumentListElement mit einem Document mit ID
     * 'documentId' als Inhalt vorhanden ist, wird dieses zurückgegeben, sonst
     * 'null'.
     *
     * @param documentId Die gesuchte Dokumenten-ID
     * @return Das DocumentListElement, dessen 'document' die übergebene ID hat,
     *         falls vorhanden, sonst 'null'
     */
    public DocumentListElement find(int documentId) {
        if (documentId < 0) {
            return null;
        }

        if (this.head == null) {
            return null;
        }

        DocumentListElement currentListElement = this.head;
        while (currentListElement.getDocumentId() != documentId) {
            if (currentListElement.getNext() == null) {
                return null;
            }
            currentListElement = currentListElement.getNext();
        }
        return currentListElement;

    }

    /**
     * Tauscht die Inhalte (alle Attribute außer 'pre' und 'next') der übergebenen
     * DocumentListElement-Objekte aus. Damit bleiben die Objekte in der Liste in
     * Place, die 'document's, 'wordCountArray's etc. werden aber vertauscht.
     *
     * @param first  Ein DocumentListElement
     * @param second Noch ein DocumentListElement
     * @return true gdw. keines der Parameter 'null' ist, also tatsächlich geswappt
     *         wurde
     */
    public boolean swapListElements(DocumentListElement first, DocumentListElement second) {
        if (first == null || second == null) {
            return false;
        }
        Document tmp = first.getDocument();
        first.setDocument(second.getDocument());
        second.setDocument(tmp);

        WordCount[] tmpWordCount = first.getWordCountArray();
        first.setWordCountArray(second.getWordCountArray());
        second.setWordCountArray(tmpWordCount);

        double tmpD = first.getSimilarity();
        first.setSimilarity(second.getSimilarity());
        second.setSimilarity(tmpD);

        tmpD = first.getPageRank();
        first.setPageRank(second.getPageRank());
        second.setPageRank(tmpD);

        tmpD = first.getRelevance();
        first.setRelevance(second.getRelevance());
        second.setRelevance(tmpD);

        return true;
    }

    // Getter, Setter etc.
    public DocumentListElement getHead() {
        return head;
    }

    public void setHead(DocumentListElement head) {
        this.head = head;
    }

    public DocumentListElement getTail() {
        return tail;
    }

    public void setTail(DocumentListElement tail) {
        this.tail = tail;
    }

    public int size() {
        return this.size;
    }
}
