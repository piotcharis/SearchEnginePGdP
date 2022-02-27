package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;
import pgdp.searchengine.util.WordCount;

import java.util.Iterator;

/**
 * Stellt eine Collection von Document-Objekten dar. Diese werden intern in
 * einem Array aus Buckets (welche wiederum intern doubly-linked lists sind)
 * gespeichert. In welchem Bucket ein Document letzten Endes landet, hängt
 * lediglich von dessen ID ab.
 */
public class DocumentCollection implements Iterable<Document> {
    private final int numberOfBuckets;
    private final Bucket[] buckets;

    public DocumentCollection(int numberOfBuckets) {
        this.numberOfBuckets = numberOfBuckets > 0 ? numberOfBuckets : 1;
        buckets = new Bucket[this.numberOfBuckets];
        for (int i = 0; i < this.numberOfBuckets; ++i) {
            buckets[i] = new Bucket();
        }
    }

    /**
     * Fügt das übergebene Document der Collection hinzu. Ändert nichts am Zustand
     * der Collection, falls das Document 'null' ist oder eine ungültige ID (<0) hat
     * oder wenn ein Document mit der gleichen ID bereits vorhanden ist.
     *
     * @param document Das Document-Objekt, das in die Collection mit aufgenommen
     *                 werden soll.
     * @return true gdw. 'document' tatsächlich eingefügt werden konnte
     */
    public boolean add(Document document) {
        if (document == null || document.getDocumentId() < 0) {
            return false;
        }

        int bucketIndex = indexFunction(document.getDocumentId());

        return buckets[bucketIndex].add(document);
    }

    /**
     * Gibt das Document mit der übergebenen ID zurück, falls ein solches in der
     * Collection vorhanden ist. Falls nicht, wird 'null' zurückgegeben.
     *
     * @param documentId Die gesuchte ID
     * @return Das gefundene Document-Objekt oder 'null', wenn nichts gefunden wurde
     */
    public Document find(int documentId) {
        if (documentId < 0) {
            return null;
        }

        int bucketIndex = indexFunction(documentId);

        DocumentListElement foundListElement = buckets[bucketIndex].find(documentId);

        return foundListElement != null ? foundListElement.getDocument() : null;
    }

    /**
     * Entfernt das Document-Objekt mit der gegebenen ID aus der Collection, falls
     * vorhanden.
     *
     * @param documentId Die zu entfernende ID
     * @return true gdw. tatsächlich ein Objekt entfernt wurde
     */
    public boolean removeDocument(int documentId) {
        if (documentId < 0) {
            return false;
        }

        int bucketIndex = indexFunction(documentId);

        DocumentListElement listElementToRemove = buckets[bucketIndex].find(documentId);

        return buckets[bucketIndex].remove(listElementToRemove);
    }

    /**
     * Entfernt alle Document-Objekte mit dem übergebenen Author aus der Collection
     *
     * @param author Der Author, dessen Documents entfernt werden sollen
     * @return true gdw. mindestens ein Document entfernt wurde
     */
    public boolean removeDocumentsFromAuthor(Author author) {
        if (author == null) {
            return false;
        }

        DocumentListElement currentListElement;
        boolean removedSomething = false;

        for (int i = 0; i < numberOfBuckets; ++i) {
            currentListElement = buckets[i].getHead();
            while (currentListElement != null) {
                if (currentListElement.getDocument().getAuthor().equals(author)) {
                    DocumentListElement listElementToRemove = currentListElement;
                    currentListElement = currentListElement.getNext();
                    removedSomething |= buckets[i].remove(listElementToRemove);
                } else {
                    currentListElement = currentListElement.getNext();
                }
            }
        }
        return removedSomething;
    }

    /**
     * Leert die gesamte Collection
     *
     * @return true gdw. die Collection nicht schon vorher leer war
     */
    public boolean removeAll() {
        if (this.isEmpty()) {
            return false;
        }

        for (int i = 0; i < numberOfBuckets; ++i) {
            buckets[i] = new Bucket();
        }
        return true;
    }

    /**
     * Entfernt alle Document-Objekte, deren 'releaseDate' UND 'lastUpdateDate' vor
     * dem jeweils übergebenen liegen. Wenn einer der beiden Parameter 'null' ist,
     * wird er ignoriert, wenn beide 'null' sind, wird die Collection geleert.
     *
     * @param releaseDate Das releaseDate, nach dem kein Document entfernt werden
     *                    soll
     * @param lastUpdated Das lastUpdateDate, nach dem kein Document entfernt werden
     *                    soll
     * @return true gdw. etwas entfernt wurde
     */
    public boolean removeOldDocuments(Date releaseDate, Date lastUpdated) {
        if (releaseDate == null && lastUpdated == null) {
            if (!removeAll()) {
                return false;
            }
            return true;
        }

        if (releaseDate != null && lastUpdated != null && releaseDate.daysUntil(lastUpdated) < 0) {
            return false;
        }

        DocumentListElement currentListElement;
        boolean removedSomething = false;

        for (int i = 0; i < numberOfBuckets; ++i) {
            currentListElement = buckets[i].getHead();
            while (currentListElement != null) {
                if (releaseDate != null
                        && currentListElement.getDocument().getReleaseDate().daysUntil(releaseDate) > 0) {
                    if (lastUpdated != null
                            && currentListElement.getDocument().getLastUpdateDate().daysUntil(lastUpdated) > 0) {
                        DocumentListElement listElementToRemove = currentListElement;
                        removedSomething |= buckets[i].remove(listElementToRemove);
                    } else if (lastUpdated == null) {
                        DocumentListElement listElementToRemove = currentListElement;
                        removedSomething |= buckets[i].remove(listElementToRemove);
                    }
                } else if (releaseDate == null
                        && currentListElement.getDocument().getLastUpdateDate().daysUntil(lastUpdated) > 0) {
                    DocumentListElement listElementToRemove = currentListElement;
                    removedSomething |= buckets[i].remove(listElementToRemove);
                }
                currentListElement = currentListElement.getNext();
            }
        }
        return removedSomething;
    }

    // Die Funktion, die aus der ID eines Dokuments den Bucket berechnet, in dem es
    // landen soll.
    private int indexFunction(int documentId) {
        return documentId % numberOfBuckets;
    }

    /**
     * Berechnet ein WordCount-Array mit einem WordCount-Objekt für jedes Wort, das
     * in mindestens einem Document in der Collection vorhanden ist. Die
     * Count-Attribute der WordCount-Objekte sind dabei egal. Das zurückgegebene
     * WordCount-Array ist dabei bereits lexikographisch aufsteigend sortiert.
     *
     * @return Das berechnete WordCount-Array
     */
    public WordCount[] getCompleteWordCountArray() {
        WordCount[] allWords = new WordCount[0];

        for (int i = 0; i < numberOfBuckets; i++) {
            DocumentListElement dle = buckets[i].getHead();
            while (dle != null) {
                allWords = Document.equalizeWordCountArray(allWords, dle.getDocument().getWordCountArray());
                dle = dle.getNext();
            }
        }
        Document.sort(allWords);
        return allWords;
    }

    /**
     * Füllt alle WordCount-Arrays der Documents in der Collection auf, sodass jedes
     * einen Eintrag für jedes Wort hat, das in irgendeinem Document in der
     * Collection vorkommt und alle WordCount-Arrays lexikographisch aufsteigen
     * sortiert sind. Somit können alle Paare von Documents in der Collection
     * miteinander verglichen werden.
     *
     */
    public void equalizeAllWordCountArrays() {
        WordCount[] allWords = this.getCompleteWordCountArray();

        for (int i = 0; i < buckets.length; i++) {
            DocumentListElement currentDle = buckets[i].getHead();
            while (currentDle != null) {
                WordCount[] equalizedArray = Document
                        .equalizeWordCountArray(currentDle.getDocument().getWordCountArray(), allWords);
                Document.sort(equalizedArray);
                currentDle.setWordCountArray(equalizedArray);
                currentDle = currentDle.getNext();
            }
        }
    }

    /**
     * Ermittelt die Anzahl an Documents, die das übergebene Wort enthalten.
     * Groß-/Kleinschreibung wird dabei nicht beachtet.
     *
     * @param word Das Wort, nachdem gesucht wird
     * @return Die ermittelte Anzahl
     */
    public int getNumberOfDocumentsContaining(String word) {
        if (word == null) {
            return 0;
        }

        int count = 0;
        word = word.replaceAll("\\.|,|\\!|\\?|;|\\*|\\(|\\)", "");
        word = word.toLowerCase();

        for (int i = 0; i < buckets.length; i++) {
            DocumentListElement currentDle = buckets[i].getHead();
            while (currentDle != null) {
                WordCount[] currentWordCountArray = currentDle.getWordCountArray();
                for (int j = 0; j < currentWordCountArray.length; j++) {
                    if (currentWordCountArray[j].getWord().equals(word) && currentWordCountArray[j].getCount() != 0) {
                        count++;
                        break;
                    }
                }
                currentDle = currentDle.getNext();
            }
        }

        return count;
    }

    /**
     * Berechnet die 'weight' und die 'normalizedWeight' für das übergebene
     * WordCount-Array und schreibt sie in die entsprechenden WordCount-Attribute.
     * Die beiden Größen berechnen sich nach den Formeln von Woche 07.
     *
     * @param wordCount Das Array, für das die Gewichte bestimmt werden sollen.
     */
    protected void calculateWeights(WordCount[] wordCount) {
        if (wordCount == null) {
            return;
        }

        double weightSum = 0;

        for (int i = 0; i < wordCount.length; i++) {
            double invertedFrequency = Math.log(
                    (this.size() + 1.0) / this.getNumberOfDocumentsContaining(wordCount[i].getWord())) / Math.log(2);
            double weight = wordCount[i].getCount() * invertedFrequency;
            weightSum += weight * weight;
            wordCount[i].setWeight(weight);
        }

        if (weightSum == 0) {
            weightSum = 1;
        } else {
            weightSum = Math.sqrt(weightSum);
        }

        for (int i = 0; i < wordCount.length; i++) {
            wordCount[i].setNormalizedWeight(wordCount[i].getWeight() / weightSum);
        }
    }

    /**
     * Berechnet für alle Document-Objekte in der Collection die Ähnlichkeit zu dem
     * übergebenen String nach dem Maß der komplexen Vektorähnlichkeit. Schreibt
     * diesen Wert jeweils in das Attribut 'similarity' des zugehörigen
     * DocumentListElement.
     *
     * @param query String, der mit allen Documents der Collection verglichen werden
     *              soll
     * @return Ein Array mit Referenzen auf alle Document-Objekte in der Collection
     *         absteigend nach 'similarity' sortiert
     */
    public Document[] query(String query) {
        if (query == null) {
            return new Document[] {};
        }

        Document queryDoc = new Document("", "", query, null, null);
        int queryId = queryDoc.getDocumentId();
        this.add(queryDoc);
        this.equalizeAllWordCountArrays();
        WordCount[] queryWordCount = Document.equalizeWordCountArray(queryDoc.getWordCountArray(),
                getCompleteWordCountArray());
        Document.sort(queryWordCount);
        this.calculateWeights(queryWordCount);

        for (int i = 0; i < numberOfBuckets; i++) {
            DocumentListElement dle = buckets[i].getHead();
            while (dle != null) {
                this.calculateWeights(dle.getWordCountArray());
                dle.setSimilarity(Document.complexSimilarity(queryWordCount, dle.getWordCountArray()));
                dle = dle.getNext();
            }
        }

        this.removeDocument(queryId);

        return this.similarityRanking();
    }

    /**
     * Berechnet ein Array mit Referenzen auf alle Document-Objekte in der
     * Collection absteigend nach 'similarity' sortiert
     *
     * @return Das absteigend nach 'similarity' sortierte Array aller Documents
     */
    public Document[] similarityRanking() {
        this.sortBuckets();
        Document[] ranked = new Document[this.size()];
        DocumentListElement[] allHeads = new DocumentListElement[buckets.length];
        double highestSimilarity;
        int highestSimIndex;

        for (int i = 0; i < allHeads.length; i++) {
            allHeads[i] = buckets[i].getHead();
        }

        for (int i = 0; i < ranked.length; i++) {
            highestSimilarity = 0;
            highestSimIndex = -1;

            for (int j = 0; j < allHeads.length; j++) {
                if (highestSimIndex == -1 && allHeads[j] != null) {
                    highestSimilarity = allHeads[j].getSimilarity();
                    highestSimIndex = j;
                }
                if (allHeads[j] != null && allHeads[j].getSimilarity() > highestSimilarity) {
                    highestSimilarity = allHeads[j].getSimilarity();
                    highestSimIndex = j;
                }
                if (allHeads[j] != null && allHeads[j].getSimilarity() == highestSimilarity
                        && allHeads[j].getDocumentId() < allHeads[highestSimIndex].getDocumentId()) {
                    highestSimilarity = allHeads[j].getSimilarity();
                    highestSimIndex = j;
                }
            }

            ranked[i] = allHeads[highestSimIndex].getDocument();

            allHeads[highestSimIndex] = allHeads[highestSimIndex].getNext();
        }
        return ranked;
    }

    /**
     * Sortiert die einzelnen Buckets absteigend nach 'similarity'
     */
    public void sortBuckets() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i].getHead() != null) {
                boolean sorted = false;
                while (!sorted) {
                    DocumentListElement currentListElement = buckets[i].getHead();
                    sorted = true;
                    while (currentListElement.getNext() != null) {
                        if (currentListElement.getSimilarity() < currentListElement.getNext().getSimilarity()) {
                            buckets[i].swapListElements(currentListElement, currentListElement.getNext());
                            sorted = false;
                        }
                        if (currentListElement.getSimilarity() == currentListElement.getNext().getSimilarity()
                                && currentListElement.getDocumentId() > currentListElement.getNext().getDocumentId()) {
                            buckets[i].swapListElements(currentListElement, currentListElement.getNext());
                            sorted = false;
                        }
                        currentListElement = currentListElement.getNext();
                    }
                }
            }
        }
    }

    // Getter, Setter und andere simple Methoden
    public DocumentListElement getHead(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex >= numberOfBuckets) {
            return null;
        }
        return buckets[bucketIndex].getHead();
    }

    public DocumentListElement getTail(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex >= numberOfBuckets) {
            return null;
        }
        return buckets[bucketIndex].getTail();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean contains(Document document) {
        if (document == null) {
            return false;
        }
        return this.find(document.getDocumentId()) != null;
    }

    public int getNumberOfBuckets() {
        return numberOfBuckets;
    }

    public Bucket[] getBuckets() {
        return buckets;
    }

    public int size() {
        int size = 0;
        for (int i = 0; i < numberOfBuckets; ++i) {
            size += buckets[i].size();
        }
        return size;
    }

    // Die iterator()-Methode. Wir lassen DocumentCollection das Iterable-Interface
    // implementieren,
    // um leichter durch sie iterieren zu können.
    @Override
    public Iterator<Document> iterator() {

        return new Iterator<Document>() {
            private int bucket = 0;
            private DocumentListElement currentDoc = null;

            {
                while (bucket < buckets.length && buckets[bucket].getHead() == null) {
                    bucket++;
                }
                if (bucket < buckets.length) {
                    currentDoc = buckets[bucket].getHead();
                }
            }

            @Override
            public boolean hasNext() {
                return bucket < buckets.length;
            }

            @Override
            public Document next() {
                if (bucket >= buckets.length) {
                    return null;
                }

                Document toReturnDoc = currentDoc.getDocument();

                if (currentDoc.getNext() != null) {
                    currentDoc = currentDoc.getNext();
                } else {
                    do {
                        bucket++;
                    } while (bucket < buckets.length && buckets[bucket].getHead() == null);

                    if (bucket < buckets.length) {
                        currentDoc = buckets[bucket].getHead();
                    }
                }

                return toReturnDoc;
            }
        };
    }

}
