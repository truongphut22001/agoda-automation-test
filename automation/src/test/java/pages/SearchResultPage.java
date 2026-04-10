package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchResultPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SearchResultPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForSearchPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. URL change
        wait.until(ExpectedConditions.urlContains("search"));

        // 2. Page load complete
        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );

        // 3. UI ready
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-selenium='hotel-item']")
        ));
    }

    public void selectFirstHotel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Handle Occupancy Box
        By occupancyBox = By.cssSelector("[data-element-name='occupancy-box']");

        List<WebElement> boxes = driver.findElements(occupancyBox);
        if (!boxes.isEmpty()) {
            WebElement box = wait.until(ExpectedConditions.elementToBeClickable(occupancyBox));
            box.click();
        }

        By hotelLink = By.cssSelector(
                "li[data-selenium='hotel-item'] a[data-selenium='hotel-name'][data-testid='property-name-link']"
        );

        String originalWindow = driver.getWindowHandle();
        int tabsBefore = driver.getWindowHandles().size();

        WebElement firstHotelLink = wait.until(
                ExpectedConditions.elementToBeClickable(hotelLink)
        );
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", firstHotelLink
        );
        firstHotelLink.click();

        wait.until(d -> d.getWindowHandles().size() > tabsBefore);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }
}
