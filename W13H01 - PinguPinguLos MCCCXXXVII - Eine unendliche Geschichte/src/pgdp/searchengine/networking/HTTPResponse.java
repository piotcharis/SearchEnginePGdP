package pgdp.searchengine.networking;

public class HTTPResponse {
    private final HTTPStatus status;
    private final String html;

    public HTTPResponse(String responseText) {
        int statusCode = Integer.parseInt(responseText.split(" ")[1]);
        this.status = HTTPStatus.getByCode(statusCode);

        if(this.status == HTTPStatus.OK) {
            int startIndexOfHTML = responseText.indexOf("<html");
            if(startIndexOfHTML == -1) {
                startIndexOfHTML = responseText.indexOf("<HTML");
            }
            int endIndexOfHTML = responseText.indexOf("</html");
            if(endIndexOfHTML == -1) {
                endIndexOfHTML = responseText.indexOf("</HTML");
            }
            endIndexOfHTML += 7;
            this.html = responseText.substring(startIndexOfHTML, endIndexOfHTML);
        } else {
            this.html = "";
        }
    }

    public HTTPStatus getStatus() {
        return status;
    }

    public String getHtml() {
        return html;
    }
}
