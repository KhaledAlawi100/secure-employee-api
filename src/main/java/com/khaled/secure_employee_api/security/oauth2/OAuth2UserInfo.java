package com.khaled.secure_employee_api.security.oauth2;

public abstract class OAuth2UserInfo {

    protected final Object attributes;

    protected OAuth2UserInfo(Object attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getFullName();

    public abstract String getImageUrl();
}