package org.constants;

public enum APIHttpStatus {
    OK(200, "OK");


    private final int code;
    private final String message;

    APIHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Code: " + code + " message: " + message;
    }

}
