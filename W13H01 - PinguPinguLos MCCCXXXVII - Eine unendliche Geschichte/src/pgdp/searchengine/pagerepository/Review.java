package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

/**
 * @author Jan Wagener (wagener@in.tum.de)
 */
public class Review {
    // Id als eindeutiger Bezeichner eines 'Review'-Objektes
    private final int postId;
    private static int nextPostId = 0;

    // Restliche Attribute
    private String title;
    private String content;
    private final Date postDate;
    private final Author reviewer;
    private final Document reviewedDocument;

    // Konstruktor
    public Review(String title, String content, Date postDate, Author reviewer, Document reviewedDocument) {
        postId = nextPostId++;
        setTitle(title);
        setContent(content);
        this.postDate = postDate;
        this.reviewer = reviewer;
        this.reviewedDocument = reviewedDocument;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public Author getReviewer() {
        return reviewer;
    }

    public Document getReviewedDocument() {
        return reviewedDocument;
    }

    // Setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAgeText() {
        return "Vor " + postDate.daysUntil(Date.today()) + " Tagen gepostet";
    }

    public boolean equals(Review other) {
        return postId == other.postId;
    }

    @Override
    public String toString() {
        return "[" + postId + ": " + reviewedDocument + "]";
    }

    public String toPrintText() {
        return "<|==== Review of " + reviewedDocument + " ====|>)" + "\nId: " + postId + "\nTitle: " + title + "\n"
                + content + "\nPosted at " + postDate + "\nPosted by " + reviewer;
    }
}
