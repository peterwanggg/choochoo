package com.pwang.kings.auth;

import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Authorizer;

public class KingsAuthorizer implements Authorizer<KingsUser> {

    @Override
    public boolean authorize(KingsUser user, String role) {
        return user.getRoles().contains(role);
    }
}
