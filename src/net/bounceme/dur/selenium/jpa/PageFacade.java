package net.bounceme.dur.selenium.jpa;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PageFacade extends AbstractFacade {
    
    private final static Logger log = Logger.getLogger(PageFacade.class.getName());
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SeleniumIteratorPU");
//    @PersistenceContext(unitName = "SeleniumReaderPU")
    
    public PageFacade() {
        super(Page.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}


