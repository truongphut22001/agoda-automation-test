package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

public class HotelPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HotelPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isPriceDisplayed() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            int scrollStep = 50;
            int totalScrolled = 0;
            while (totalScrolled < 700) {
                js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);
                totalScrolled += scrollStep;
                Thread.sleep(50);
            }

            WebElement container = driver.findElement(
                    By.cssSelector("div[data-element-name='cheapest-room-price-property-nav-bar']")
            );

            List<WebElement> spans = container.findElements(
                    By.cssSelector(".StickyNavPrice__priceDetail span")
            );

            String priceText = spans.stream()
                    .map(e -> e.getAttribute("textContent"))
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .map(t -> t.replaceAll("[^\\d]", ""))
                    .filter(t -> !t.isEmpty())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Price not found"));

            long price = Long.parseLong(priceText);
            return price > 0;

        } catch (NoSuchElementException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}