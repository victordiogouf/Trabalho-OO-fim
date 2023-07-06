package trabalho;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Database {
  public static SessionFactory sessionFactory;

  static {
    sessionFactory = new Configuration().configure().buildSessionFactory();
  }

  public static <T> void create(T entity) throws HibernateException {
    Session session = sessionFactory.openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.save(entity);
      transaction.commit();
    } 
    catch (HibernateException e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    } 
    finally {
      session.close();
    }
  }

  public static <T, ID extends Serializable> T read(Class<T> entityClass, ID id) throws HibernateException {
    Session session = sessionFactory.openSession();
    try {
      return entityClass.cast(session.get(entityClass, id));
    } 
    finally {
      session.close();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> readAll(Class<T> entityClass) throws HibernateException {
    Session session = sessionFactory.openSession();
    try {
      String queryString = "FROM " + entityClass.getSimpleName();
      Query query = session.createQuery(queryString);
      return query.list();
    } 
    finally {
      session.close();
    }
  }

  public static <T> void update(T entity) throws HibernateException {
    Session session = sessionFactory.openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.update(entity);
      transaction.commit();
    } 
    catch (HibernateException e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    } 
    finally {
      session.close();
    }
  }

  public static <T> void delete(T entity) throws HibernateException {
    Session session = sessionFactory.openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.delete(entity);
      transaction.commit();
    } 
    catch (HibernateException e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    } 
    finally {
      session.close();
    }
  }
}