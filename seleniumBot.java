import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class seleniumBot {
    private WebDriver driver;
    private String url;
    private final int timeout = 5;
    private WebDriverWait wait;
    private Actions action;

    public seleniumBot(String url) {
        this.url = url;
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        action = new Actions(driver);
    }

    public void run() {
        driver.get(url);
        driver.manage().window().maximize();
        removeCookie();
    }

    private void removeCookie() {
        String shadowHostXPath = "/html/body/efilli-layout-dynamic";
        WebElement shadowHost = driver.findElement(By.xpath(shadowHostXPath));
        JavascriptExecutor jsExecuter = (JavascriptExecutor) driver;
        jsExecuter.executeScript("arguments[0].parentNode.removeChild(arguments[0]);", shadowHost);
    }

    public void searchItem(String minPrice, String maxPrice) {
        navigateToBestSelling();
        enterPrices(minPrice, maxPrice);
        clickSearchButton();
    }

    public void navigateToBestSelling() {
        String bestSellingXPath = "/html/body/header/div[15]/nav/a[11]";
        WebElement bestSellingButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bestSellingXPath)));
        bestSellingButton.click();
        removeCookie();
    }

    public void navigateToDiscount() {
        String discountXPath = "/html/body/header/div[15]/nav/a[13]";
        WebElement discountButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(discountXPath)));
        discountButton.click();
        removeCookie();
    }

    public void searchItemByCategory(String item) {
        String searchFieldXPath = "/html/body/header/div[15]/div[2]/div[2]/form/input";
        String searchButtonXPath = "/html/body/header/div[15]/div[2]/div[2]/a";

        WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchFieldXPath)));
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchButtonXPath)));

        searchField.sendKeys(item);
        action.moveToElement(searchButton).perform();
        searchButton.click();
    }

    public long timeToNavigateToCarpets() {
        String carpetsXPath = "/html/body/header/div[15]/nav/a[6]";
        long endTime = 0;
        WebElement carpetsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(carpetsXPath)));
        long startTime = System.nanoTime();
        carpetsButton.click();
        if (carpetsTitleVisible()) {
            endTime = System.nanoTime();
        }
        long executionTimeInMilliSeconds = (endTime - startTime) / 1000000;
        return executionTimeInMilliSeconds;
    }

    private boolean carpetsTitleVisible() {
        String carpetsTitleXPath = "/html/body/main/section[2]/div[2]/div[1]/h1";
        try {
            WebElement priceRange = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(carpetsTitleXPath)));
            return true;
        } catch (NoSuchElementException | TimeoutException timeout) {
            return false;
        }
    }

    public void suggestedSort() {
        String suggestedSortXPath = "/html/body/main/section[2]/div[4]/div[3]/div[1]/div/div[3]";
        WebElement suggestedSortButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(suggestedSortXPath)));
        action.moveToElement(suggestedSortButton).perform();
        suggestedSortButton.click();
    }

    public void sortItemsByHighestDiscountRate() {
        String lowToHighXPath = "/html/body/main/section[2]/div[4]/div[3]/div[1]/div/div[3]/div/a[6]";
        WebElement lowToHighButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(lowToHighXPath)));
        lowToHighButton.click();
    }

    public String[] get10Items() {
        String[] titles = new String[10];
        for (int i = 1; i <= 10; i++) {
            String xPath = "/html/body/main/section[2]/div[4]/div[3]/div[2]/div[" + i + "]/div/a[2]";
            WebElement item = driver.findElement(By.xpath(xPath));
            titles[i - 1] = item.getAttribute("title");
        }
        return titles;
    }

    public List<String> pricesSearchedWithinRange(String minValue, String maxValue) {
        searchItem(minValue, maxValue);
        List<String> prices = new ArrayList<>();
        int count = 1;
        waitBetweenActions();
        List<WebElement> itemList = driver.findElements(By.cssSelector(".productWrap"));
        for (WebElement item : itemList) {
            String priceXPath = "/html/body/main/section[2]/div[4]/div[3]/div[2]/div[" + count + "]/div/a[2]";
            WebElement price = driver.findElement(By.xpath(priceXPath));
            prices.add(price.getAttribute("data-productprice"));
            count++;
        }
        return prices;
    }

    public List<String> expectedCategoryOfSearchedItem(String item, String attribute) {
        searchItemByCategory(item);
        removeCookie();
        List<String> categories = new ArrayList<>();
        int count = 1;
        List<WebElement> categoryList = driver.findElements(By.cssSelector(".productWrap"));
        for (WebElement category : categoryList) {
            String categoryXPath = "/html/body/div[1]/main/section[1]/div[4]/div[3]/div[2]/div[" + count + "]/div/a[2]";
            WebElement categoryName = driver.findElement(By.xpath(categoryXPath));
            categories.add(categoryName.getAttribute(attribute));
            count++;
        }
        return categories;
    }

    public void enterPrices(String minPrice, String maxPrice) {
        String minPriceXPath = "//*[@id=\"product-filter-price-min\"]";
        String maxPriceXPath = "//*[@id=\"product-filter-price-max\"]";

        WebElement minPriceTextField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(minPriceXPath)));
        WebElement maxPriceTextField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(maxPriceXPath)));

        action.moveToElement(minPriceTextField).perform();
        minPriceTextField.click();
        minPriceTextField.sendKeys(minPrice);
        maxPriceTextField.click();
        maxPriceTextField.sendKeys(maxPrice);
    }

    public void clickSearchButton() {
        String searchButtonXPath = "//*[@id=\"apply-prices-button\"]/i";
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchButtonXPath)));
        searchButton.click();
    }

    public void waitBetweenActions() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String priceRangeSearched() {
        String priceRangeXPath = "/html/body/main/section[2]/div[4]/div[3]/div[1]/ul/div/div[2]/div/span";
        WebElement priceRange = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(priceRangeXPath)));
        return priceRange.getText();
    }

    public boolean priceRangeVisible() {
        String priceRangeXPath = "/html/body/main/section[2]/div[4]/div[3]/div[1]/ul/div/div[2]/div/span";
        try {
            WebElement priceRange = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(priceRangeXPath)));
            return true;
        } catch (NoSuchElementException | TimeoutException timeout) {
            return false;
        }
    }

    public boolean sortedItemsVisible() {
        String sortedItemXPath = "//*[@id=\"page-block\"]";
        try {
            WebElement sortedItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sortedItemXPath)));
            return true;
        } catch (NoSuchElementException | TimeoutException timeout) {
            return false;
        }
    }

    public boolean itemNotFound() {
        String itemNotFoundXPath = "//*[@id=\"catchNoResult\"]/div[2]";
        try {
            WebElement itemNotFoundText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemNotFoundXPath)));
            return true;
        } catch (NoSuchElementException | TimeoutException timeout) {
            return false;
        }
    }

    public boolean searchedItemsVisible() {
        String itemForkXPath = "/html/body/main/section[2]/div[4]/div[3]/div[2]/div[1]/div/a[2]";
        try {
            WebElement itemForkFoundText = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(itemForkXPath)));
            return true;
        } catch (NoSuchElementException | TimeoutException timeout) {
            return false;
        }
    }
}