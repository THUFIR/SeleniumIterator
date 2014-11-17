package selenium;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.bounceme.dur.selenium.jpa.Link;
import net.bounceme.dur.selenium.jpa.LinkFacade;
import net.bounceme.dur.selenium.jpa.Page;
import net.bounceme.dur.selenium.jpa.PageFacade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumWebPageIterator {

    private final static Logger log = Logger.getLogger(SeleniumWebPageIterator.class.getName());
    private final PageFacade pageFacade = new PageFacade();
    private final LinkFacade linkFacade = new LinkFacade();

    public SeleniumWebPageIterator() {

    }

    public void processLinks() {
        List<Link> links = linkFacade.findAll();
        for (Link l : links) {
            processLink(l);
        }
    }

    private void processLink(Link l) {
        log.fine(l.toString());
        WebDriver driver = new FirefoxDriver();  //don't display
        driver.get(l.getLink());
        driver.manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
        String s = driver.getPageSource();
        createPage(l, s);
        driver.close();
    }

    private void createPage(Link l, String s) {
        Page p = new Page();
        p.setCreated(new Date());
        p.setLinkId(l.getId());
        p.setPage(s);
        pageFacade.create(p);  //page has no id..
    }

}
