package com.lyft.data.baseapp.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;
import java.util.Set;

public class AppAuthenticator implements Authenticator<BasicCredentials, User>
{
    private static String user;
    private static Set<String> roles;
    private static String password;

    public  AppAuthenticator(AuthConfiguration authConfiguration) {
        this.user = authConfiguration.getUser();
        this.roles = authConfiguration.getRoles();
        this.password = authConfiguration.getPassword();
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        if (user.equals(credentials.getUsername()) && password.equals(credentials.getPassword()))
        {
            return Optional.of(new User(credentials.getUsername(), roles));
        }
        return Optional.empty();
    }
}
