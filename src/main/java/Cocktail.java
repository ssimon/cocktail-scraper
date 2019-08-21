import java.util.ArrayList;
import java.util.List;

public class Cocktail {
    private List<String> ingredients;
    private String howTo;
    private String title;
    private String id;

    public Cocktail(String cocktailLink) {
        id = cocktailLink;
        ingredients = new ArrayList<>();
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void setHowTo(String text) {
        howTo = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + ": " + ingredients.toString();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getHowTo() {
        return howTo;
    }

    public String getId() {
        return id;
    }
}
