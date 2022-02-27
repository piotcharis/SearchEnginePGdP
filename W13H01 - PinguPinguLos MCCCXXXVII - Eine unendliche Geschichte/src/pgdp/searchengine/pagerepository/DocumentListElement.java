package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.WordCount;

public class DocumentListElement {
    private Document document;
    private DocumentListElement pre;
    private DocumentListElement next;
    private WordCount[] wordCountArray;
    private double similarity;
    private double pageRank;
    private double relevance;

    public DocumentListElement(Document document) {
        this.document = document;
        this.pre = null;
        this.next = null;
        setup();
    }

    public DocumentListElement(Document document, DocumentListElement pre) {
        this.document = document;
        this.pre = pre;
        this.next = null;
        setup();
    }

    public DocumentListElement(Document document, DocumentListElement pre, DocumentListElement next) {
        this.document = document;
        this.pre = pre;
        this.next = next;
        setup();
    }

    private void setup() {
        this.wordCountArray = this.document.getWordCountArray();
        this.similarity = 0;
    }

    public int getDocumentId() {
        return document.getDocumentId();
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentListElement getPre() {
        return pre;
    }

    public void setPre(DocumentListElement pre) {
        this.pre = pre;
    }

    public DocumentListElement getNext() {
        return next;
    }

    public void setNext(DocumentListElement next) {
        this.next = next;
    }

    public WordCount[] getWordCountArray() {
        return wordCountArray;
    }

    public void setWordCountArray(WordCount[] wordCount) {
        this.wordCountArray = wordCount;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }
}
