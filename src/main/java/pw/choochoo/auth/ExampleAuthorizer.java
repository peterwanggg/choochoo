package pw.choochoo.auth;

import pw.choochoo.core.User;
import io.dropwizard.auth.Authorizer;

public class ExampleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
