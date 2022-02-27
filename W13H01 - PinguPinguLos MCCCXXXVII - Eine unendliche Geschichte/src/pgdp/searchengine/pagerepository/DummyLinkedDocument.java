package pgdp.searchengine.pagerepository;

/**
 * Ein Dummy für ein Document, dessen URL bereits bekannt ist (z.B. weil andere,
 * bereits präsente Documents darauf verlinken), das aber noch nicht gecrawlt
 * wurde.
 */
public class DummyLinkedDocument extends AbstractLinkedDocument {

    public DummyLinkedDocument(String address, int collectionSize) {
        super("", "", "", null, null, address, collectionSize);
    }

}
