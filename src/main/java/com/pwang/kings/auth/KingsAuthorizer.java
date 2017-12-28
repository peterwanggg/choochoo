package com.pwang.kings.auth;

import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Authorizer;

public class KingsAuthorizer implements Authorizer<KingsUser> {

    @Override
    public boolean authorize(KingsUser user, String role) {
//        return user.getRoles() != null && user.getRoles().contains(role);
        return true;
//        return user.getRoles() != null && Arrays.stream(user.getRoles()).anyMatch(role::equals);
    }
}
