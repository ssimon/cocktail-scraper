import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scraper {

    public static final String START_URL = "http://www.tasteline.com/drinkar/";
    private int count = 0;
    private List<Cocktail> cocktails;

    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        scraper.start();
    }

    private void start() throws IOException {
        cocktails = new ArrayList<>();

        boolean hasNextPage = true;
        String linksPageUrl = START_URL;
        while (hasNextPage) {
            Document linksPage = Jsoup.connect(linksPageUrl).get();

            System.out.println("First cocktail on page: " + linksPage.getElementsByTag("h2").first().text());

            Elements cocktails = linksPage.getElementsByClass("recipe-description");

            for (Element cocktailElement : cocktails) {
                String cocktailLink = cocktailElement.getElementsByTag("a").first().attr("href");

                Optional<Cocktail> cocktail = getCocktail(cocktailLink);
                if (cocktail.isPresent()) {
                    this.cocktails.add(cocktail.get());
                }
            }

            Elements nextButton = linksPage.getElementsByClass("list-paging__button--next");
            if (nextButton == null || nextButton.isEmpty()) {
                hasNextPage = false;
            } else {
                linksPageUrl = nextButton.first().attr("href");
            }

        }

        Gson gson = new Gson();

        String json = gson.toJson(cocktails);

        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("jsonDump.json"), "UTF-8"))) {
            out.write(json);
        }

    }


    private Optional<Cocktail> getCocktail(String cocktailLink) throws IOException {

        Document cocktailPage = Jsoup.connect(cocktailLink).get();
        Element drinkContent = cocktailPage.getElementsByClass("ingredient-group").first();


        Cocktail cocktail = new Cocktail(cocktailLink);

        Elements ingredients = drinkContent.getElementsByTag("li");

        if (ingredients == null || ingredients.isEmpty()) {
            return Optional.empty();
        }

        for (Element ingredient : ingredients) {
            cocktail.addIngredient(ingredient.text());

        }

        Elements howTo = cocktailPage.getElementsByClass("step-group");
        cocktail.setHowTo(howTo.text());

        String title;
        Elements titleElements = cocktailPage.getElementsByTag("h1");
        if (titleElements == null || titleElements.text().isEmpty()) {
            title = "Unknown title";
        } else {
            title = titleElements.first().text();
        }
        cocktail.setTitle(title);
        count++;
        System.out.println("Fetched so far: " + count);
        return Optional.of(cocktail);
    }

    public String parseTitle(String html) {
        Elements title = Jsoup.parse(html).getElementsByTag("title");

        return title.text();
    }
}
