import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class integrationTest {
    static seleniumBot testBot;

    @BeforeAll
    static void initialize() {
        String url = "https://www.karaca.com";
        testBot = new seleniumBot(url);
        testBot.run();
    }

    @Test
    void tenItemsWithHighestDiscountRate() {
        String[] expectedTitles = {
                "Rcr Tattoo Meşrubat Bardağı",
                "Jumbo Efes Red Düz Tabak 31 cm",
                "Kaşmir Halı Dekoratif Art Trend Iceberg 200x300 cm",
                "Silikomart Goccia Silikon Pasta Kalıbı",
                "Silikomart Lana Silikon Rulo Pasta Kalıbı",
                "Silikomart Scg50 3D N15 Tartufino - Silikon Çikolata Kalıbı",
                "Silikomart Scg53 3D Choco Drop - Silikon Çikolata Kalıbı",
                "Silikomart Inserto Buche Silikon Pasta Kalıbı",
                "Silikomart Batticuore Silikon Kalp Pasta Kalıbı",
                "Silikomart Frozen Buche - Set Silikon Yuvarlak Pasta Kalıbı"
        };

        //Note that since the discount rates of items on Karaca are updated
        //periodically, the item titles in the expectedTitles may vary.

        testBot.navigateToDiscount();
        testBot.suggestedSort();
        testBot.sortItemsByHighestDiscountRate();
        if (testBot.sortedItemsVisible()) {
            String[] actualTitles = testBot.get10Items();
            assertArrayEquals(expectedTitles, actualTitles);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "100, 200",
            "500, 600",
            "1000, 1350",
            "7500, 10000",
            "16000, 20000"
    })
    void displayExpectedItemsWithinPriceRange(String minValue, String maxValue) {
        List<String> actualPrices = testBot.pricesSearchedWithinRange(minValue, maxValue);
        int minPrice = Integer.parseInt(minValue);
        int maxPrice = Integer.parseInt(maxValue);

        for (String price : actualPrices) {
            double actualPrice = Double.parseDouble(price);
            System.out.println(actualPrice);
            assertTrue(actualPrice >= minPrice && actualPrice <= maxPrice,
                    "Price " + actualPrice + " is not within the range [" + minPrice + ", " + maxPrice + "]");
        }
    }

    @Test
    void areAllSearchedItemsFilterCoffeeMachine() {
        String expectedCategory = "Filtre Kahve Makinesi";
        String attribute = "data-productcategory4";
        List<String> actualCategories = testBot.expectedCategoryOfSearchedItem(expectedCategory, attribute);
        for (String category : actualCategories) {
            assertEquals(expectedCategory, category);
        }
    }

    @Test
    void areAllSearchedItemsToastMachine() {
        String expectedCategory = "Tost Makinesi";
        String attribute = "data-productcategory4";
        List<String> actualCategories = testBot.expectedCategoryOfSearchedItem(expectedCategory, attribute);
        for (String category : actualCategories) {
            assertEquals(expectedCategory, category);
        }
    }

    @Test
    void areAllSearchedItemsJumbo() {
        String expectedBrand = "Jumbo";
        String attribute = "data-productbrand";
        List<String> actualBrands = testBot.expectedCategoryOfSearchedItem(expectedBrand, attribute);
        for (String brand : actualBrands) {
            assertEquals(expectedBrand, brand);
        }
    }

    @Test
    void areAllSearchedItemsStanley() {
        String expectedBrand = "Stanley";
        String attribute = "data-productbrand";
        List<String> actualBrands = testBot.expectedCategoryOfSearchedItem(expectedBrand, attribute);
        for (String brand : actualBrands) {
            assertEquals(expectedBrand, brand);
        }
    }
}
