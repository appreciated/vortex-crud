package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.function.Supplier;

public class DatabaseUserProvider<T extends User> implements UserProvider<T> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> userClass;
    private final Supplier<T> userSupplier;

    public DatabaseUserProvider(Class<T> userClass, Supplier<T> userSupplier) {
        this.userClass = userClass;
        this.userSupplier = userSupplier;
    }

    @Override
    public Optional<T> findByUsername(String username) {
        try {
            T user = entityManager.createQuery("SELECT u FROM " + userClass.getSimpleName() + " u WHERE u.username = :username", userClass)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public T registerUser(String username, String password) {
        T user = userSupplier.get();
        // The user object needs to be a mutable implementation of the User interface.
        // This is a bit of a hack, but it's necessary to avoid having to create a new interface
        // with setters.
        ((MutableUser) user).setUsername(username);
        ((MutableUser) user).setPassword(password); // In a real application, hash the password
        entityManager.persist(user);
        return user;
    }
}