import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;

public class responseTimeTest {
    static seleniumBot testBot;
    private static ArrayList<Long> responseTimes = new ArrayList<>();
    private int currentIndex = 0;

    @BeforeAll
    static void initialize() {
        String url = "https://www.karaca.com";
        testBot = new seleniumBot(url);
        testBot.run();
    }

    @RepeatedTest(3)
    void timeToSearchItem() {
        testBot.navigateToBestSelling();
        testBot.enterPrices("200", "250");
        long startTime = System.nanoTime();
        testBot.clickSearchButton();
        long endTime = 0;
        if (testBot.searchedItemsVisible()) {
            endTime = System.nanoTime();
        }
        long executionTimeInMilliSeconds = (endTime - startTime) / 1000000;
        responseTimes.add(currentIndex, executionTimeInMilliSeconds);
        currentIndex++;
        System.out.println(executionTimeInMilliSeconds + "ms");
        testBot.waitBetweenActions();
    }

    @RepeatedTest(value = 114, failureThreshold = 15)
    void timeToNavigateToMenu() {
        long executionTime = testBot.timeToNavigateToCarpets();
        responseTimes.add(currentIndex, executionTime);
        currentIndex++;
        System.out.println(executionTime + "ms");
        testBot.waitBetweenActions();
    }

    @RepeatedTest(value = 114, failureThreshold = 15)
    void timeToSortItems() {
        testBot.navigateToDiscount();
        testBot.suggestedSort();
        long startTime = System.nanoTime();
        testBot.sortItemsByHighestDiscountRate();
        long endTime = 0;
        if (testBot.sortedItemsVisible()) {
            endTime = System.nanoTime();
        }
        long executionTimeInMilliSeconds = (endTime - startTime) / 1000000;
        responseTimes.add(currentIndex, executionTimeInMilliSeconds);
        currentIndex++;
        System.out.println(executionTimeInMilliSeconds + "ms");
        testBot.waitBetweenActions();
    }

    @AfterAll
    public static void calculateAvgAndMaxResponseTimes() {
        int count = 0;
        long sum = 0;
        long max = 0;
        for (long time : responseTimes) {
            sum += time;
            count++;
            if (time > max) {
                max = time;
            }
            if (count == 100) {
                break;
            }
        }
        long averageResponseTime = sum / responseTimes.size();
        System.out.println("Average RT = " + averageResponseTime + "ms");
        System.out.println("Maximum RT = " + max + "ms");
    }
}
