package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class HomePage extends BasePage {

    private static final Logger logger = Logger.getLogger(HomePage.class.getName());
    private static final String BASE_URL = "https://www.agoda.com/";

    private final By searchBox    = By.id("textInput");
    private final By searchButton = By.cssSelector("[data-element-name='search-button']");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open() {
        driver.get(BASE_URL);
    }

    public void enterHotel(String hotelName) {
        WebElement input = waitForVisible(searchBox);
        input.clear();
        input.sendKeys(hotelName);

        WebElement firstSuggestion = waitForClickable(
                By.cssSelector("li[data-testid='topDestinationListItem'][data-element-index='0']")
        );
        firstSuggestion.click();
    }

    public void selectDates(LocalDate checkIn, LocalDate checkOut) {
        WebElement checkInBox = driver.findElement(By.id("check-in-box"));
        String expanded = checkInBox.getAttribute("aria-expanded");

        if (!"true".equals(expanded)) {
            logger.info("Calendar is CLOSED → opening");
            click(By.cssSelector("div[data-selenium='checkInBox']"));
        } else {
            logger.info("Calendar is already OPEN");
        }

        By checkInLocator  = By.cssSelector("[data-selenium-date='" + checkIn  + "']");
        By checkOutLocator = By.cssSelector("[data-selenium-date='" + checkOut + "']");

        click(checkInLocator);
        click(checkOutLocator);
    }

    public void setRoomOptions(int rooms, int adults, int children) {
        By popupLocator = By.cssSelector("[data-selenium='occupancyPicker']");

        boolean isPopupVisible = false;
        try {
            waitForVisible(popupLocator);
            isPopupVisible = true;
        } catch (TimeoutException e) {
            isPopupVisible = false;
        }

        if (!isPopupVisible) {
            logger.info("Occupancy popup CLOSED → opening");
            click(By.cssSelector("[data-element-name='occupancy-box']"));
            waitForVisible(popupLocator);
        } else {
            logger.info("Occupancy popup already OPEN");
        }

        By roomValue     = By.cssSelector("[data-selenium='desktop-occ-room-value'] p");
        By adultValue    = By.cssSelector("[data-selenium='desktop-occ-adult-value'] p");
        By childrenValue = By.cssSelector("[data-selenium='desktop-occ-children-value'] p");

        By roomPlus  = By.cssSelector("[data-selenium='occupancyRooms'] [data-selenium='plus']");
        By roomMinus = By.cssSelector("[data-selenium='occupancyRooms'] [data-selenium='minus']");
        By adultPlus = By.cssSelector("[data-selenium='occupancyAdults'] [data-selenium='plus']");
        By adultMinus= By.cssSelector("[data-selenium='occupancyAdults'] [data-selenium='minus']");
        By childPlus = By.cssSelector("[data-selenium='occupancyChildren'] [data-selenium='plus']");
        By childMinus= By.cssSelector("[data-selenium='occupancyChildren'] [data-selenium='minus']");

        setValue(roomValue,     roomPlus,  roomMinus,  rooms);
        setValue(adultValue,    adultPlus, adultMinus, adults);
        setValue(childrenValue, childPlus, childMinus, children);

        if (children > 0) {
            selectRandomChildAge();
        }

        Assert.assertEquals(getValue(roomValue),     rooms,    "Room count mismatch");
        Assert.assertEquals(getValue(adultValue),    adults,   "Adult count mismatch");
        Assert.assertEquals(getValue(childrenValue), children, "Children count mismatch");

        click(By.id("occupancy-box"));
    }

    public void clickSearch() {
        click(searchButton);
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private int getValue(By locator) {
        return Integer.parseInt(driver.findElement(locator).getText().trim());
    }

    private void setValue(By valueLocator, By plusBtn, By minusBtn, int target) {
        int current = getValue(valueLocator);
        int diff    = target - current;

        if (diff > 0) {
            WebElement plus = waitForClickable(plusBtn);
            for (int i = 0; i < diff; i++) plus.click();
        } else if (diff < 0) {
            WebElement minus = waitForClickable(minusBtn);
            for (int i = 0; i < Math.abs(diff); i++) minus.click();
        }
    }

    private void selectRandomChildAge() {
        click(By.cssSelector("button[data-element-name='occ-child-age-dropdown']"));
        List<WebElement> options = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector("ul[role='listbox'] li")
                )
        );
        options.get(new Random().nextInt(options.size())).click();
    }
}