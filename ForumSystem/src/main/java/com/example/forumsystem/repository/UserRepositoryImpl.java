package com.example.forumsystem.repository;

import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.models.FilterUserOptions;
import com.example.forumsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll(FilterUserOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder("FROM User");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getFirstName().ifPresent(value -> {
                filters.add("firstName like :firstName");
                params.put("firstName", String.format("%%%s%%", value));
            });

            filterOptions.getLastName().ifPresent(value -> {
                filters.add("lastName like :lastName");
                params.put("lastName", String.format("%%%s%%", value));
            });

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            if(!filters.isEmpty()) {
                sb.append(" WHERE ").append(String.join(" AND ", filters));
            }

            sb.append(createOrderBy(filterOptions));
            Query<User> query = session.createQuery(sb.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }

            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where firstName = :firstName", User.class);
            query.setParameter("firstName", firstName);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "first name", firstName);
            }

            return result.get(0);
        }
    }

    @Override
    public void createUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    private String createOrderBy(FilterUserOptions filterOptions) {
        if(filterOptions.getSortBy().isEmpty()){
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "lastName" -> "lastName";
            case "username" -> "username";
            case "email" -> "email";
            default -> "firstName";
        };

        orderBy = String.format(" order by %s", orderBy);
        if(filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")){
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
