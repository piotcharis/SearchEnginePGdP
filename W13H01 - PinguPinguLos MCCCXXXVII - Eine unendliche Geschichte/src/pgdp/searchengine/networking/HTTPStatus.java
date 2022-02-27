package pgdp.searchengine.networking;

public enum HTTPStatus {
    OK(200, "OK"), BAD_REQUEST(400, "Bad Request"), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"), REQUEST_TIMEOUT(408, "Request Timeout");

    private int code;
    private String text;

    HTTPStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static HTTPStatus getByCode(int code) {
        return switch (code) {
            case 200 -> OK;
            case 400 -> BAD_REQUEST;
            case 403 -> FORBIDDEN;
            case 404 -> NOT_FOUND;
            case 405 -> METHOD_NOT_ALLOWED;
            case 408 -> REQUEST_TIMEOUT;
            default -> null;
        };
    }
}
