package pgdp.searchengine.networking;

public class HTMLToken {
    private TokenType tokenType;
    private StringBuilder content;

    public HTMLToken(TokenType tokenType) {
        this.tokenType = tokenType;
        this.content = new StringBuilder();
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getContentAsString() {
        return content.toString();
    }

    public void addCharacter(char c) {
        content.append(c);
    }

    @Override
    public String toString() {
        return (tokenType == TokenType.TAG ? "Tag: " : "Text: ") + content;
    }

    public enum TokenType {
        TAG, TEXT
    }
}
