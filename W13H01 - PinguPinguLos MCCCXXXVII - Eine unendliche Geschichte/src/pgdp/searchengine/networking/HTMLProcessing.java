package pgdp.searchengine.networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HTMLProcessing {

    // Useless constructor of uselessness
    private HTMLProcessing() {

    }

    public static List<HTMLToken> tokenize(String rawHTML) {
        List<HTMLToken> sequenceOfTokens = new ArrayList<>();
        HTMLToken currentToken = null;
        TokenizingState currentState = TokenizingState.INITIAL;

        for (char c : rawHTML.toCharArray()) {
            switch (currentState) {
                case INITIAL -> {
                    if (c == '<') {
                        currentToken = new HTMLToken(HTMLToken.TokenType.TAG);
                        sequenceOfTokens.add(currentToken);
                        currentState = TokenizingState.TAG;
                    } else {
                        currentToken = new HTMLToken(HTMLToken.TokenType.TEXT);
                        currentToken.addCharacter(Character.toLowerCase(c));
                        sequenceOfTokens.add(currentToken);
                        currentState = TokenizingState.TEXT;
                    }
                }
                case TAG -> {
                    currentState = switch (c) {
                        case '>' -> TokenizingState.INITIAL;
                        case '\'' -> TokenizingState.SINGLE_QUOTE_STRING;
                        case '\"' -> TokenizingState.DOUBLE_QUOTE_STRING;
                        default -> TokenizingState.TAG;
                    };
                    if (c != '>') {
                        currentToken.addCharacter(Character.toLowerCase(c));
                    }
                }
                case TEXT -> {
                    if (c == '<') {
                        currentToken = new HTMLToken(HTMLToken.TokenType.TAG);
                        sequenceOfTokens.add(currentToken);
                        currentState = TokenizingState.TAG;
                    } else {
                        currentToken.addCharacter(Character.toLowerCase(c));
                    }
                }
                case SINGLE_QUOTE_STRING -> {
                    if (c == '\'') {
                        currentState = TokenizingState.TAG;
                    }
                    currentToken.addCharacter(c);
                }
                case DOUBLE_QUOTE_STRING -> {
                    if (c == '\"') {
                        currentState = TokenizingState.TAG;
                    }
                    currentToken.addCharacter(c);
                }
                default -> {
                    throw new IllegalArgumentException(currentToken.toString());
                }
            }
        }
        return sequenceOfTokens;
    }

    private enum TokenizingState {
        INITIAL, TAG, TEXT, SINGLE_QUOTE_STRING, DOUBLE_QUOTE_STRING
    }

    public static String[] filterLinks(List<HTMLToken> tokens, String host) {
        return tokens.stream()
                .filter(token -> token.getTokenType() == HTMLToken.TokenType.TAG)
                .map(HTMLToken::getContentAsString)
                .filter(s -> s.startsWith("a "))
                .map(s -> {
                    String[] splitBySpace = s.split(" ");
                    return Arrays.stream(splitBySpace)
                            .filter(t -> t.startsWith("href="))
                            .map(t -> t.substring(6, t.length() - 1))
                            .reduce("", (s1, s2) -> s1 + s2);
                })
                .filter(s -> s.startsWith("https://") || s.startsWith("/"))
                .map(s -> s.startsWith("https://") ? s.substring(8) : host + s)
                .distinct()
                .toArray(String[]::new);
    }

    public static String filterText(List<HTMLToken> tokens) {
        return tokens.stream()
                .filter(token -> token.getTokenType() == HTMLToken.TokenType.TEXT)
                .map(HTMLToken::getContentAsString)
                .reduce(
                        new StringBuilder(),
                        (stringBuilder, str) -> stringBuilder.append(str).append(' '),
                        StringBuilder::append)
                .toString();
    }

    public static String filterTitle(List<HTMLToken> tokens) {
        boolean currentlyInHead = false;
        boolean currentlyInTitle = false;
        List<HTMLToken> tokensInsideTitle = new ArrayList<>();

        for (HTMLToken token : tokens) {
            String tokenContent = token.getContentAsString();
            if (token.getTokenType() == HTMLToken.TokenType.TAG) {
                if (tokenContent.startsWith("head") && tokenContent.charAt(tokenContent.length() - 1) != '/') {
                    currentlyInHead = true;
                } else if (currentlyInHead && tokenContent.startsWith("/head")) {
                    currentlyInHead = false;
                } else if (currentlyInHead && tokenContent.startsWith("title")
                        && tokenContent.charAt(tokenContent.length() - 1) != '/') {
                    currentlyInTitle = true;
                } else if (currentlyInTitle && tokenContent.startsWith("/title")) {
                    currentlyInTitle = false;
                    break;
                }
            } else if (currentlyInTitle) {
                tokensInsideTitle.add(token);
            }
        }

        return filterText(tokensInsideTitle);
    }

}
