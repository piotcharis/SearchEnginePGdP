package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;
import pgdp.searchengine.util.WordCount;

public class Document {
    private final int documentId;
    private static int nextDocumentId = 0;

    private String title;
    private String description;
    private String content;

    private final Date releaseDate;
    private Date lastUpdateDate;

    private final Author author;

    public Document(String title, String description, String content, Date releaseDate, Author author) {
        documentId = nextDocumentId++;
        this.title = title;
        this.description = description;
        this.content = content;
        this.releaseDate = releaseDate;
        this.lastUpdateDate = releaseDate;
        this.author = author;
    }

    /**
     * Berechnet den Ähnlichkeitswert der Worte von dem Inhalt des Dokument 'this'
     * mit dem des übergebenen Dokumentes 'other' nach dem Maß aus Woche 05 (simples
     * Skalarprodukt)
     *
     * @param other Dokument, zu dem die Ähnlichkeit berechnet werden soll
     * @return Der Ähnlichkeitswert
     */
    public double computeSimilarity(Document other) {
        WordCount[] thisDocument = getWordCountArray();
        WordCount[] otherDocument = other.getWordCountArray();

        thisDocument = equalizeWordCountArray(thisDocument, otherDocument);
        otherDocument = equalizeWordCountArray(otherDocument, thisDocument);

        sort(thisDocument);
        sort(otherDocument);

        return similarity(thisDocument, otherDocument);
    }

    /**
     * Berechnet ein Array mit WordCount-Objekten für alle in dem Inhalt des
     * Dokumentes vorkommenden Wörter und der jeweiligen Anzahl an Vorkommen eines
     * Wortes.
     *
     * @return Das WordCount-Array, das die Anzahlen der Vorkommen der Wörter im
     *         Inhalt des Dokumentes 'this' sammelt
     */
    public WordCount[] getWordCountArray() {
        return getWordCountHelp(content);
    }

    protected WordCount[] getWordCountHelp(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("\\.|,|\\!|\\?|;|\\*|\\(|\\)", "");

        String words[] = text.split(" ");

        int size = 0;

        WordCount[] wordCount = new WordCount[words.length];

        for (String word : words) {
            boolean contains = false;
            for (int i = 0; i < size; ++i) {
                if (word.equals(wordCount[i].getWord())) {
                    wordCount[i].increment();
                    contains = true;
                    break;
                }
            }
            if (!contains && !word.isBlank()) {
                wordCount[size] = new WordCount(word.toLowerCase());
                wordCount[size++].increment();
            }
        }

        WordCount[] tmp = new WordCount[size];
        for (int i = 0; i < size; ++i) {
            tmp[i] = wordCount[i];
        }

        return tmp;
    }

    // ==================================================================================
    // //
    // ------------------------------- STATISCHE METHODEN
    // ------------------------------- //
    // ==================================================================================
    // //

    /**
     * Berechnet ein WordCount-Array, das ein WordCount-Objekt für jedes Wort
     * enthält, für das es ein WordCount-Objekt in mindestens einem der beiden
     * Parameter 'first' und 'second' gibt. Die Counts von 'first' werden dabei
     * übernommen, die aus second ignoriert. D.h., dass die Counts aller Wörter, die
     * nur in 'second' vorkommen, auf 0 gesetzt werden.
     *
     * @param first  Ein WordCount-Array
     * @param second Ein weiteres WordCount-Array
     * @return Ein WordCount-Array mit einem Eintrag für jedes Wort, das in 'first'
     *         oder 'second' vorkommt
     */
    public static WordCount[] equalizeWordCountArray(WordCount[] first, WordCount[] second) {
        WordCount[] tmp = new WordCount[first.length + second.length];

        for (int i = 0; i < first.length; ++i) {
            tmp[i] = first[i];
        }

        int size = first.length;
        for (int i = 0; i < second.length; ++i) {
            boolean contains = false;
            for (int j = 0; j < first.length; ++j) {
                if (second[i].getWord().equals(first[j].getWord())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                tmp[size++] = new WordCount(second[i].getWord());
            }
        }

        if (size < first.length + second.length) {
            WordCount[] tmp2 = new WordCount[size];
            for (int i = 0; i < tmp2.length; ++i) {
                tmp2[i] = tmp[i];
            }
            tmp = tmp2;
        }

        return tmp;
    }

    /**
     * Sortiert das übergebene WordCount-Array 'unsorted' nach den Wörtern der
     * WordCount-Objekte lexikographisch aufsteigend
     *
     * @param unsorted Ein beliebiges (im Allgemeinen noch nicht lexikographisch
     *                 sortiertes) WordCount-Array
     */
    public static void sort(WordCount[] unsorted) {
        for (int i = 0; i < unsorted.length; ++i) {
            for (int j = 0; j < unsorted.length - i - 1; ++j) {
                if (unsorted[j].getWord().compareTo(unsorted[j + 1].getWord()) > 0) {
                    WordCount tmp = unsorted[j];
                    unsorted[j] = unsorted[j + 1];
                    unsorted[j + 1] = tmp;
                }
            }
        }
    }

    /**
     * Berechnet den Ähnlichkeitswert der WordCount-Arrays 'a' und 'b' nach dem Maß
     * aus Woche 05 (einfaches Skalarprodukt)
     *
     * @param a Ein WordCount-Array
     * @param b Noch eins
     * @return Der Ähnlichkeitswert
     */
    public static double similarity(WordCount[] a, WordCount[] b) {
        if (a.length != b.length) {
            return -1;
        }
        double result = 0;
        for (int i = 0; i < a.length; ++i) {
            result += a[i].getCount() * b[i].getCount();
        }
        return result / (a.length * b.length);
    }

    /**
     * Berechnet den Ähnlichkeitswert der WordCount-Arrays 'a' und 'b' nach dem Maß
     * aus Woche 07 (komplexe Vektorähnlichkeit)
     *
     * @param a Ein WordCount-Array
     * @param b Noch eins
     * @return Der Ähnlichkeitswert
     */
    public static double complexSimilarity(WordCount[] a, WordCount[] b) {
        if (a == null || b == null || a.length != b.length) {
            return -1;
        }
        double result = 0;
        for (int i = 0; i < a.length; ++i) {
            result += a[i].getNormalizedWeight() * b[i].getNormalizedWeight();
        }
        return result;
    }

    // =================================================================================
    // //
    // ------------------------------ GETTER, SETTER ETC.
    // ------------------------------ //
    // =================================================================================
    // //

    // Getter
    public int getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Author getAuthor() {
        return author;
    }

    // Setter
    public void setTitle(String title) {
        setLastUpdateDate(Date.today());
        this.title = title;
    }

    public void setDescription(String description) {
        setLastUpdateDate(Date.today());
        this.description = description;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int yearsSinceRelease() {
        return releaseDate.yearsUntil(Date.today());
    }

    public int daysSinceLastUpdate() {
        return lastUpdateDate.daysUntil(Date.today());
    }

    public boolean equals(Document other) {
        return documentId == other.documentId;
    }

    @Override
    public String toString() {
        return title + ", by " + author + ", released " + releaseDate;
    }

    public String toPrintText() {
        return "<|==== " + title + " ====|>" + "\nBy " + author + "\n" + description + "\nLast updated at "
                + lastUpdateDate;
    }

    public static int numberOfCreatedDocuments() {
        return nextDocumentId;
    }
}
