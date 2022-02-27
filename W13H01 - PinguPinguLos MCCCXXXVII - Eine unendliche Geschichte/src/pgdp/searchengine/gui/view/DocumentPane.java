package pgdp.searchengine.gui.view;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/** Stellt ein Panel dar, in dem ein einzelnes LinkedDocument-Objekt in der Admin View angezeigt wird.
 */
public class DocumentPane extends AbstractDocumentPane {
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel linksToLabel;

    private static final int CHARACTERS_OF_CONTENT_TO_SHOW = 50;

    public DocumentPane(int id, String address, String title, String content, int[] linksTo) {
        super(id, address);

        titleLabel = new JLabel();
        contentLabel = new JLabel();
        linksToLabel = new JLabel();

        setTitle(title);
        setContent(content);
        setLinksTo(linksTo);

        add(titleLabel);
        add(contentLabel);
        add(linksToLabel);
    }

    public void setTitle(String title) {
        titleLabel.setText("Title: " + title);
    }

    public void setContent(String content) {
        contentLabel.setText("Content: " + content.substring(0, Math.min(content.length(), CHARACTERS_OF_CONTENT_TO_SHOW)));
    }

    public void setLinksTo(int[] linksTo) {
        linksToLabel.setText("Links To: " + Arrays.stream(linksTo).mapToObj(n -> "" + n).collect(Collectors.joining(", ")));
    }
}
