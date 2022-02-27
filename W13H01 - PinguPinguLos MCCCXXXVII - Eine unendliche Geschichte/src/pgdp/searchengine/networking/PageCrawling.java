package pgdp.searchengine.networking;

import pgdp.searchengine.pagerepository.LinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import java.util.List;

public final class PageCrawling {

    // Useless constructor of uselessness
    private PageCrawling() {

    }

    public static void crawlPages(LinkedDocumentCollection collection, int number) {
        crawlPages(collection, number, collection.getNextUncrawledAddress());
    }

    public static void crawlPages(LinkedDocumentCollection collection, int number, String startingAddress) {
        String currentAddress = startingAddress;
        for (int i = 0; i < number; i++) {
            if (!crawlPage(collection, currentAddress)) {
                collection.removeDocument(currentAddress);
                i--;
            }
            currentAddress = collection.getNextUncrawledAddress();
            if (currentAddress == null) {
                break;
            }
        }
    }

    public static boolean crawlPage(LinkedDocumentCollection collection, String address) {
        int splitIndex = address.indexOf(".de/") + 4;
        String host = address.substring(0, splitIndex - 1);
        String path = address.substring(splitIndex);

        HTTPRequest request = new HTTPRequest(host, path);
        HTTPResponse response = request.send(443);

        if (response.getStatus() != HTTPStatus.OK) {
            return false;
        }

        List<HTMLToken> tokens = HTMLProcessing.tokenize(response.getHtml());
        String[] outlinks = HTMLProcessing.filterLinks(tokens, host);
        String content = HTMLProcessing.filterText(tokens);
        String title = HTMLProcessing.filterTitle(tokens);

        LinkedDocument linkedDocument = new LinkedDocument(title, "", content, null, null, host + "/" + path, 4);
        collection.addToResultCollection(linkedDocument, outlinks);

        return true;
    }

    // -------- main() zum Testen -------- //

    public static void main(String... args) {
        HTTPRequest request = new HTTPRequest("man1.pgdp.sse.in.tum.de", "");
        HTTPResponse response = request.send(443);
        int x = 0;

        test1();
    }

    public static void test1() {
        String host = "man7.pgdp.sse.in.tum.de";
        String path = "iso_8859_1.7.html";

        LinkedDocumentCollection ldc = new LinkedDocumentCollection(7);
        crawlPages(ldc, 5, host + "/" + path);
        int x = 0;
    }

    public static void test2() {
        String host = "pgdp.sse.in.tum.de";
        String path = "index.html";

        LinkedDocumentCollection ldc = new LinkedDocumentCollection(10);
        crawlPages(ldc, 1, host + "/" + path);
        int x = 0;
    }

}
