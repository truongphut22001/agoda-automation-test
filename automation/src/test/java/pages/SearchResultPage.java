package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SearchResultPage extends BasePage {

    private final By occupancyBox = By.cssSelector("[data-element-name='occupancy-box']");
    private final By hotelItem    = By.cssSelector("[data-selenium='hotel-item']");
    private final By hotelLink    = By.cssSelector(
            "li[data-selenium='hotel-item'] a[data-selenium='hotel-name'][data-testid='property-name-link']"
    );

    public SearchResultPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void waitForSearchPage() {
        wait.until(ExpectedConditions.urlContains("search"));
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        waitForVisible(hotelItem);
    }

    public void selectFirstHotel() {
        List<WebElement> boxes = driver.findElements(occupancyBox);
        if (!boxes.isEmpty()) {
            click(occupancyBox);
        }

        String originalWindow = driver.getWindowHandle();
        int tabsBefore = driver.getWindowHandles().size();

        WebElement firstHotelLink = waitForClickable(hotelLink);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", firstHotelLink
        );
        firstHotelLink.click();

        // Chờ tab mới xuất hiện rồi switch sang
        wait.until(d -> d.getWindowHandles().size() > tabsBefore);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }
}