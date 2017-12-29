package com.pwang.kings.auth;

import com.pwang.kings.db.daos.KingsUserDao;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public final class KingsAuthenticator implements Authenticator<BasicCredentials, KingsUser> {

    private final KingsUserDao kingsUserDao;

    public KingsAuthenticator(KingsUserDao kingsUserDao) {
        this.kingsUserDao = kingsUserDao;
    }

    @Override
    public Optional<KingsUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return kingsUserDao.getByName(credentials.getUsername());

    }
}
