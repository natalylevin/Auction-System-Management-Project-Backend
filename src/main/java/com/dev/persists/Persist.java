package com.dev.persists;

import com.dev.objects.Bid;
import com.dev.objects.Product;
import com.dev.objects.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import com.dev.utils.Utils;


@Component
public class Persist {


    private Utils utils = new Utils();


    private final SessionFactory sessionFactory;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
        createDefaultAdmin();
    }

    public User getUserByUsername (String username) {
        User found;
        Session session = sessionFactory.openSession();
        found = (User) session.createQuery("FROM User WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();
        return found;
    }

    public void saveUser (User user) {
        Session session = sessionFactory.openSession();
        session.save(user);
        session.close();
    }

    public void saveProduct (Product product) {
        Session session = sessionFactory.openSession();
        session.save(product);
        session.close();
    }

    public List<Bid> getAllBidsByProductId(int productId) {
        String hql = "FROM Bid WHERE product.id =: productId";
        Session session = sessionFactory.openSession();
        Query<Bid> query = session.createQuery(hql);
        query.setParameter("productId", productId);
        List<Bid> results = query.list();
        session.close();
        return results;
    }

    public Double getSecondHighestBidByUserId(int productId, int userId , Double maxBid) {
        String hql = "SELECT MAX(newBid) FROM Bid WHERE product.id =: productId  " +
                     "AND user.id =: userId AND newBid < :maxBid ";

        Session session = sessionFactory.openSession();
        Query<Double> query = session.createQuery(hql);
        query.setParameter("productId", productId);
        query.setParameter("userId", userId);
        query.setParameter("maxBid", maxBid);
        Double bidToReturn = query.uniqueResult();
        session.close();

        return bidToReturn;
    }

    public void updateBidsValueToZero(int productId, Double maxBid) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();


        String hql = "update Bid set newBid = 0 where product.id =: productId and newBid < :maxBid"; //and date < :maxBidDate
        Query query = session.createQuery(hql);
        query.setParameter("productId", productId);
        query.setParameter("maxBid", maxBid);
        query.executeUpdate();
        tx.commit();
        session.close();
    }



    public User getUserByUsernameAndToken (String username, String token) {
        User found;
        Session session = sessionFactory.openSession();
        found = (User) session.createQuery("FROM User WHERE username = :username " +
                        "AND token = :token")
                .setParameter("username", username)
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        return found;
    }

    public List<User> getAllUsers () {
        Session session = sessionFactory.openSession();
        List<User> allUsers = session.createQuery("FROM User ").list();
        session.close();
        return allUsers;
    }

    public boolean setNewBalanceById(int id, Double newBalance) {

        boolean balanceUpdated = false;
        Session session = sessionFactory.openSession();

        // Begin a transaction
        Transaction transaction = session.beginTransaction();

        try {
            // Get the user entity by id
            Query<User> query = session.createQuery("from User u where u.id = :id", User.class);
            query.setParameter("id", id);
            User user = query.getSingleResult();

            // Update the user's balance
            user.setBalance(newBalance);

            // Save the changes to the database
            session.update(user);

            // Commit the transaction
            transaction.commit();
            balanceUpdated = true;
        } catch (Exception ex) {
            // Rollback the transaction in case of any error
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }

        return balanceUpdated;

    }

    public List<Product> getAllProducts() {
        Session session = sessionFactory.openSession();
        List<Product> allProducts = session.createQuery("FROM Product ").list();
        session.close();
        return allProducts;
    }

    public User getUserByToken (String token) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("From User WHERE token = :token")
                        .setParameter("token", token)
                                .uniqueResult();
        session.close();
        return user;
    }


    public User getUserById (int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("FROM User WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return user;
    }

    public Product getProductById (int id) {
        Session session = sessionFactory.openSession();
        Product product = (Product) session.createQuery("FROM Product WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return product;
    }

    private void createDefaultAdmin() {
        Session session = sessionFactory.openSession();
        List<User> allUsers = getAllUsers();
        boolean isAdminExist = false;
        for (User user : allUsers) {
            if (user.isAdmin()) {
                isAdminExist = true;
                break;
            }
        }

        if (!isAdminExist) {

            String adminUsername = "Admin";
            String token = utils.createHash(adminUsername, "Admin1234");
            User admin = new User(adminUsername, token, 1000d);
            admin.setAdmin(true);
            saveUser(admin);
        }
        session.close();

    }

    public void updateProductMinPrice(int productId, Double newPrice) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "UPDATE Product SET minPrice = :newPrice WHERE id = :productId";
        Query query = session.createQuery(hql);
        query.setParameter("productId", productId);
        query.setParameter("newPrice", newPrice);
        query.executeUpdate();
        transaction.commit();
        session.close();

    }

    public void addNewBid(Bid bid) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(bid);
        tx.commit();
        session.close();
    }

    public List<Bid> getAllBids() {
        Session session = sessionFactory.openSession();
        List<Bid> allBids = session.createQuery("FROM Bid").list();
        session.close();
        return allBids;
    }


    public void sellProductById(int productId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "UPDATE Product SET sold = true WHERE id = :productId";

        Query query = session.createQuery(hql);
        query.setParameter("productId", productId);
        query.executeUpdate();
        transaction.commit();
        session.close();


    }

    public Bid getHighestBid(int productId) {
        Session session = sessionFactory.openSession();
        String hql = "FROM Bid WHERE product.id = :productId " +
                "AND newBid = (SELECT MAX(newBid) FROM Bid WHERE product.id = :productId) " +
                "AND date = (SELECT MAX(date) FROM Bid WHERE product.id = :productId)";

        Query<Bid> query = session.createQuery(hql);
        query.setParameter("productId", productId);
        Bid maxBid = query.uniqueResult();
        return maxBid;
    }
}
