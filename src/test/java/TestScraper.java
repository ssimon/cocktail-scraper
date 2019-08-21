import org.junit.Assert;
import org.junit.Test;

public class TestScraper {
    private Scraper scraper = new Scraper();

    @Test
    public void testParseTitle() {
        String html = "<html><head><title>my title</title></head></html>";

        String result = scraper.parseTitle(html);

        Assert.assertEquals("my title", result);
    }

    @Test
    public void testParseTitle_brokenHtml() {
        String html = "<html><head><title>my title</html>";

        String result = scraper.parseTitle(html);

        Assert.assertEquals("my title", result);
    }

    @Test
    public void testParseTitle_missingTitle() {
        String html = "<html><head></head></html>";

        String result = scraper.parseTitle(html);

        Assert.assertEquals("", result);
    }
}
