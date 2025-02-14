package com.example.forumsystem.repository;

import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll(FilterPostOptions filterPostOptions) {
        try (
                Session session = sessionFactory.openSession()) {

            StringBuilder queryString = new StringBuilder("from Post ");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterPostOptions.getTitle().ifPresent(value -> {
                filters.add(" title like :title ");
                params.put("title", String.format("%%%s%%", value));
            });

            filterPostOptions.getContent().ifPresent(value -> {
                filters.add(" content like :content ");
                params.put("content", String.format("%%%s%%", value));
            });

            filterPostOptions.getCreatedBy().ifPresent(value -> {
                filters.add(" createdBy.username like :createdBy ");
                params.put("createdBy", String.format("%%%s%%", value));
            });


            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }


            queryString.append(createOrderBy(filterPostOptions));
            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }

            return post;
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return result.get(0);
        }
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p " +
                    "FROM Post p " +
                    "LEFT JOIN FETCH p.comments c " +
                    "GROUP BY p.id, p.title, p.content, p.createdAt " +
                    "ORDER BY COUNT(c.id) DESC";

            Query<Post> query = session.createQuery(hql, Post.class);
            query.setMaxResults(10);

            return query.list();
        }
    }

    @Override
    public List<Post> getMostRecentPosts() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p " +
                    "FROM Post p " +
                    "ORDER BY p.createdAt DESC";

            Query<Post> query = session.createQuery(hql, Post.class);
            query.setMaxResults(10);

            return query.list();
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

    private String createOrderBy(FilterPostOptions filterOptions) {
        if(filterOptions.getSortBy().isEmpty()){
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "content" -> "content";
            case "createdBy" -> "createdBy.username";
            default -> "title";
        };

        orderBy = String.format(" order by %s", orderBy);
        if(filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")){
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
