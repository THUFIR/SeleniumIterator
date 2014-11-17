package selenium;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.bounceme.dur.selenium.jpa.Link;
import net.bounceme.dur.selenium.jpa.Page;
import net.bounceme.dur.selenium.jpa.PageFacade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumWebPage {

    private final static Logger log = Logger.getLogger(SeleniumWebPage.class.getName());
    private final PageFacade pf = new PageFacade();

    public SeleniumWebPage() {

    }

    public void processLinks() {
        List<Link> links = pf.findAll();
        for (Link l : links) {
            processLink(l);
        }
    }

    private void processLink(Link l) {
        WebDriver driver = new FirefoxDriver();  //don't display
        driver.get(l.getLink());
        driver.manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
        String s = driver.getPageSource();
        createPage(s);
        driver.close();
    }

    private void createPage(String s) {
        log.info(s);
        Page p = new Page();
        p.setCreated(new Date());
        pf.create(p);
    }

}
