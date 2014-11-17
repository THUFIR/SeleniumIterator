/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.dur.selenium.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import net.bounceme.dur.selenium.jpa.exceptions.IllegalOrphanException;
import net.bounceme.dur.selenium.jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author thufir
 */
public class FeedJpaController implements Serializable {

    public FeedJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Feed feed) {
        if (feed.getLinkCollection() == null) {
            feed.setLinkCollection(new ArrayList<Link>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Link> attachedLinkCollection = new ArrayList<Link>();
            for (Link linkCollectionLinkToAttach : feed.getLinkCollection()) {
                linkCollectionLinkToAttach = em.getReference(linkCollectionLinkToAttach.getClass(), linkCollectionLinkToAttach.getId());
                attachedLinkCollection.add(linkCollectionLinkToAttach);
            }
            feed.setLinkCollection(attachedLinkCollection);
            em.persist(feed);
            for (Link linkCollectionLink : feed.getLinkCollection()) {
                Feed oldFeedIdOfLinkCollectionLink = linkCollectionLink.getFeedId();
                linkCollectionLink.setFeedId(feed);
                linkCollectionLink = em.merge(linkCollectionLink);
                if (oldFeedIdOfLinkCollectionLink != null) {
                    oldFeedIdOfLinkCollectionLink.getLinkCollection().remove(linkCollectionLink);
                    oldFeedIdOfLinkCollectionLink = em.merge(oldFeedIdOfLinkCollectionLink);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Feed feed) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Feed persistentFeed = em.find(Feed.class, feed.getId());
            Collection<Link> linkCollectionOld = persistentFeed.getLinkCollection();
            Collection<Link> linkCollectionNew = feed.getLinkCollection();
            List<String> illegalOrphanMessages = null;
            for (Link linkCollectionOldLink : linkCollectionOld) {
                if (!linkCollectionNew.contains(linkCollectionOldLink)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Link " + linkCollectionOldLink + " since its feedId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Link> attachedLinkCollectionNew = new ArrayList<Link>();
            for (Link linkCollectionNewLinkToAttach : linkCollectionNew) {
                linkCollectionNewLinkToAttach = em.getReference(linkCollectionNewLinkToAttach.getClass(), linkCollectionNewLinkToAttach.getId());
                attachedLinkCollectionNew.add(linkCollectionNewLinkToAttach);
            }
            linkCollectionNew = attachedLinkCollectionNew;
            feed.setLinkCollection(linkCollectionNew);
            feed = em.merge(feed);
            for (Link linkCollectionNewLink : linkCollectionNew) {
                if (!linkCollectionOld.contains(linkCollectionNewLink)) {
                    Feed oldFeedIdOfLinkCollectionNewLink = linkCollectionNewLink.getFeedId();
                    linkCollectionNewLink.setFeedId(feed);
                    linkCollectionNewLink = em.merge(linkCollectionNewLink);
                    if (oldFeedIdOfLinkCollectionNewLink != null && !oldFeedIdOfLinkCollectionNewLink.equals(feed)) {
                        oldFeedIdOfLinkCollectionNewLink.getLinkCollection().remove(linkCollectionNewLink);
                        oldFeedIdOfLinkCollectionNewLink = em.merge(oldFeedIdOfLinkCollectionNewLink);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = feed.getId();
                if (findFeed(id) == null) {
                    throw new NonexistentEntityException("The feed with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Feed feed;
            try {
                feed = em.getReference(Feed.class, id);
                feed.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The feed with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Link> linkCollectionOrphanCheck = feed.getLinkCollection();
            for (Link linkCollectionOrphanCheckLink : linkCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Feed (" + feed + ") cannot be destroyed since the Link " + linkCollectionOrphanCheckLink + " in its linkCollection field has a non-nullable feedId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(feed);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Feed> findFeedEntities() {
        return findFeedEntities(true, -1, -1);
    }

    public List<Feed> findFeedEntities(int maxResults, int firstResult) {
        return findFeedEntities(false, maxResults, firstResult);
    }

    private List<Feed> findFeedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Feed.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Feed findFeed(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Feed.class, id);
        } finally {
            em.close();
        }
    }

    public int getFeedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Feed> rt = cq.from(Feed.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
