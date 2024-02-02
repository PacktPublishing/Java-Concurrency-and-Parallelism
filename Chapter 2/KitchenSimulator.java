import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KitchenSimulator {
    private static final ExecutorService kitchen = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        String dishToPrepare = "Spaghetti Bolognese";
        String menuToUpdate = "Today's Specials";

        kitchen.submit(() -> {
            prepareDish(dishToPrepare);
        });
        kitchen.submit(() -> {
            searchRecipes("Italian");
        });
        kitchen.submit(() -> {
            updateMenu(menuToUpdate, "Risotto alla Milanese");
        });
        kitchen.shutdown();
    }

    private static void prepareDish(String dish) {
        System.out.println("Preparing " + dish);
    }

    private static void searchRecipes(String cuisine) {
        System.out.println("Searching for " + cuisine + " recipes");
    }

    private static void updateMenu(String menu, String dishToAdd) {
        System.out.println("Updating " + menu + " with " + dishToAdd);
    }
}
