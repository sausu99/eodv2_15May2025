/**
 * The AccessToken class represents an access token with its corresponding expiration time.
 */
package com.wowcodes.supreme.model;

import com.google.gson.annotations.*;


public class AccessToken {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("expires_in")
    @Expose
    private String expiresIn;

    public AccessToken(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}