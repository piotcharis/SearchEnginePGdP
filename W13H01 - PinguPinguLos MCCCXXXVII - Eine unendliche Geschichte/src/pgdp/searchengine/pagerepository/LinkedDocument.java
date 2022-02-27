package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;
import pgdp.searchengine.util.WordCount;

/**
 * Ein Dokument mit Links von und zu anderen Dokumenten. LinkedDocuments sind im
 * Gegensatz zu DummyLinkedDocuments bereits gecrawlt und lokal gespeichert
 * worden. Es sind nicht nur deren URL und incoming Links, sondern auch die
 * outgoing Links und der Inhalt etc. bekannt.
 */
public class LinkedDocument extends AbstractLinkedDocument {
    private final LinkedDocumentCollection outgoingLinks;

    public LinkedDocument(String title, String description, String content, Date releaseDate, Author author,
            String address, int collectionSize) {
        super(title, description, content, releaseDate, author, address, collectionSize);
        outgoingLinks = new LinkedDocumentCollection(collectionSize);
    }

    /**
     * Durchsucht den Inhalt des Dokumentes nach Wörtern, die mit "link::" anfangen.
     * Sammelt diese Wörter, den Präfix "link::" jeweils abgeschnitten, in einem
     * Array und gibt dieses zurück.
     *
     * @return Ein Array mit allen "Links", also allen Wörtern mit Präfix "link::"
     *         im Inhalt, diesen Präfix jeweils abgeschnitten
     */
    public String[] getOutgoingAddresses() {
        String[] words = this.getContent().split(" ");
        String[] addresses = new String[words.length];
        int size = 0;

        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("link::") && words[i].length() > 6) {
                addresses[size++] = words[i].substring(6);
            }
        }

        if (size != words.length) {
            String[] tmp = new String[size];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = addresses[i];
            }
            addresses = tmp;
        }
        return addresses;
    }

    /**
     * Fügt das übergebene AbstractLinkedDocument der Collection an outgoing Links
     * hinzu. Falls ein AbstractLinkedDocument mit der gleichen URL bereits
     * vorhanden ist, soll dieses genau dann durch das neue ersetzt werden, wenn es
     * ein DummyLinkedDocument ist.
     *
     * @param linkedDocument Das in die outgoing Links einzufügende
     *                       AbstractLinkedDocument
     * @return true gdw. tatsächlich ein neues Objekt eingefügt wurde (also auch,
     *         wenn ein Dummy ersetzt wurde)
     */
    public boolean addOutgoingLink(AbstractLinkedDocument linkedDocument) {
        if (linkedDocument == null) {
            return false;
        }
        if (linkedDocument.getAddress().equals(this.getAddress())) {
            return false;
        }
        if (outgoingLinks.contains(linkedDocument)
                && !(outgoingLinks.find(linkedDocument.getAddress()) instanceof DummyLinkedDocument)) {
            return false;
        }
        if (outgoingLinks.contains(linkedDocument)
                && outgoingLinks.find(linkedDocument.getAddress()) instanceof DummyLinkedDocument dld) {
            outgoingLinks.removeDummy(dld);
        }
        outgoingLinks.add(linkedDocument);
        return true;
    }

    @Override
    public WordCount[] getWordCountArray() {
        return super.getWordCountHelp(getContent().replaceAll("link::\\S+", ""));
    }

    public LinkedDocumentCollection getOutgoingLinks() {
        return outgoingLinks;
    }
}
