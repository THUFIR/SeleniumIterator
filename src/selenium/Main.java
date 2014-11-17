package selenium;


import java.util.logging.Logger;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class.getName());
    private final SeleniumWebPageIterator webPage = new SeleniumWebPageIterator();

    public static void main(String... args) {
        new Main().getLinks();
    }

    private void getLinks() {
        log.fine("getting links..");
        webPage.processLinks();
    }
}
