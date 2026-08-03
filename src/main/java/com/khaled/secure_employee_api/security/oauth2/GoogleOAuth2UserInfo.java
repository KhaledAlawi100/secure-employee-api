package com.khaled.secure_employee_api.security.oauth2;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public GoogleOAuth2UserInfo(Object attributes) {

        super(attributes);

        this.attributes = (Map<String, Object>) attributes;
    }

    @Override
    public String getId() {

        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {

        return (String) attributes.get("email");
    }

    @Override
    public String getFirstName() {

        return (String) attributes.get("given_name");
    }

    @Override
    public String getLastName() {

        return (String) attributes.get("family_name");
    }

    @Override
    public String getFullName() {

        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {

        return (String) attributes.get("picture");
    }
}