package selenium;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.bounceme.dur.selenium.jpa.Link;
import net.bounceme.dur.selenium.jpa.LinkJpaController;
import net.bounceme.dur.selenium.jpa.Page;
import net.bounceme.dur.selenium.jpa.PageJpaController;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumWebPage {

    private final static Logger log = Logger.getLogger(SeleniumWebPage.class.getName());
    private LinkJpaController linksController = null;
    private PageJpaController pagesController = null;

    public SeleniumWebPage() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SeleniumReaderPU");
        linksController = new LinkJpaController(emf);
        pagesController = new PageJpaController(emf);
    }

    public void processLinks() {
        List<Link> links = linksController.findLinkEntities();
        for (Link l : links) {
            processLink(l);
        }
    }

    private void processLink(Link l) {
        WebDriver driver = new FirefoxDriver();  //don't display
        driver.get(l.getLink());
        driver.manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
        String s = driver.getPageSource();
        foo(s);
        driver.close();
    }

    private void foo(String s) {
        log.info(s);
        Page p = new Page();
        p.setCreated(new Date());
        pagesController.create(p);
        
        
        
        
        /*
                    log.fine(entry.getTitle());
            link = new Link();
            link.setCreated(new Date());
            link.setLink(entry.getLink());
            linksController.create(link);

        */
        
        
    }

}
