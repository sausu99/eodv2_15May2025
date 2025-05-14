/**
 * The ChargeRequest class is a Java class that represents a request to charge a certain value with a
 * given correlation ID and comment.
 */
package com.wowcodes.supreme.RetrofitUtils;

public class ChargeRequest {
    private String correlationID;
    private int value;
    private String comment;

    public ChargeRequest(String correlationID, int value, String comment) {
        this.correlationID = correlationID;
        this.value = value;
        this.comment = comment;
    }
}
