package com.example.forumsystem.repository;

import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl( SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.list();
        }
    }

    @Override
    public List<Comment> getByPost(Post post) {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Comment WHERE postId = :id";
            Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("id", post.getId());

            return query.getResultList();
        }
    }

    @Override
    public List<Comment> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Comment WHERE createdBy.id = :id";
            Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("id", user.getId());

            return query.getResultList();
        }
    }

    @Override
    public Comment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);

            if (comment == null) {
                throw new EntityNotFoundException("Comment", id);
            }

            return comment;
        }
    }

    @Override
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }
}
