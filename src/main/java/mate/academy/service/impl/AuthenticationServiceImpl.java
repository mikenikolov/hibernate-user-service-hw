package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userByEmail = userService.findByEmail(email);
        if (userByEmail.isPresent() && HashUtil.hashPassword(password, userByEmail.get().getSalt())
                .equals(userByEmail.get().getPassword())) {
            return userByEmail.get();
        }
        throw new AuthenticationException("Can't login a user with email " + email);
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        if (email.matches("^([a-zA-Z0-9_\\-\\.]+)"
                + "@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
                && userService.add(user).getId() != null) {
            return user;
        }
        throw new RegistrationException("Can't register a user with email " + email);
    }
}
