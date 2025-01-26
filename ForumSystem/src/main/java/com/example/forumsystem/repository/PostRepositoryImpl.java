package com.example.forumsystem.repository;

import com.example.forumsystem.exeptions.EntityNotFoundExeption;
import com.example.forumsystem.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post ", Post.class);
            return query.list();
        }
    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundExeption("Post", id);
            }

            return post;
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> querry = session.createQuery("from Post where title = :title", Post.class);
            querry.setParameter("title", title);
            List<Post> result = querry.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundExeption("Post", "title", title);
            }

            return result.get(0);
        }
    }

    @Override
    public void createPost(Post post) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.persist(post);
                session.getTransaction().commit();
            }
    }

    @Override
    public void updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deletePost(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }
}
