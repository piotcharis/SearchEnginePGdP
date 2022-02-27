package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

/**
 * Abstrakte Oberklasse aller Dokumente, die in eine LinkedDocumentCollection
 * aufgenommen werden können.
 */
public abstract class AbstractLinkedDocument extends Document {
    private final String address;
    private final LinkedDocumentCollection incomingLinks;

    public AbstractLinkedDocument(String title, String description, String content, Date releaseDate, Author author,
            String address, int collectionSize) {
        super(title, description, content, releaseDate, author);
        this.address = address;
        incomingLinks = new LinkedDocumentCollection(collectionSize);
    }

    /**
     * Fügt das übergebene AbstractLinkedDocument der Collection von incomingLinks
     * hinzu. Wenn das übergebene AbstractLinkedDocument 'null' oder bereits
     * enthalten ist oder die gleiche Adresse wie 'this' hat, geschieht nichts.
     *
     * @param linkedDocument Ein AbstractLinkedDocument, welches einen Link auf
     *                       'this' hat
     * @return true gdw. das Einfügen erfolgreich war
     */
    public boolean addIncomingLink(AbstractLinkedDocument linkedDocument) {
        if (linkedDocument == null) {
            return false;
        }
        if (linkedDocument.getAddress().equals(this.getAddress())) {
            return false;
        }
        if (incomingLinks.contains(linkedDocument)) {
            return false;
        }
        return incomingLinks.add(linkedDocument);
    }

    // Getter
    public String getAddress() {
        return address;
    }

    public LinkedDocumentCollection getIncomingLinks() {
        return incomingLinks;
    }

}
