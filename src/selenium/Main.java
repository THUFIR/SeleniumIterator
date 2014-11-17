package selenium;

import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private SeleniumWebPage webPage = new SeleniumWebPage();

    public static void main(String... args) {
        new Main().getLinks();
    }

    private void getLinks() {
        log.info("getting links..");
        webPage.processLinks();
    }
}
