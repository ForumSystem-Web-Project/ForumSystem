package com.example.forumsystem.repository;

import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneNumberRepositoryImpl implements PhoneNumberRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PhoneNumberRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<PhoneNumber> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> query = session.createQuery("from PhoneNumber ", PhoneNumber.class);
            return query.list();
        }
    }

    @Override
    public PhoneNumber getByUserId(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> phone = session.createQuery(
                    "From PhoneNumber Where createdBy.id = :user_id", PhoneNumber.class);
            phone.setParameter("user_id", user.getId());
            return phone
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Phone", "user ID",
                            String.valueOf(user.getId())));
        }
    }

    @Override
    public PhoneNumber getByPhoneNumber (PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> phone = session.createQuery("From PhoneNumber where phoneNumber = :phone_number", PhoneNumber.class);
            phone.setParameter("phone_number", phoneNumber.getPhoneNumber());
            return phone
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "phone number",
                            String.valueOf(phoneNumber.getPhoneNumber())));
        }
    }

    @Override
    public void create(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(phoneNumber);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(phoneNumber);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(phoneNumber);
            session.getTransaction().commit();
        }
    }
}
