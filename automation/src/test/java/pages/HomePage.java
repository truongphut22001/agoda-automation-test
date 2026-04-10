package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By searchBox = By.id("textInput");
    private final By searchButton = By.cssSelector("[data-element-name='search-button']");
    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open() {
        driver.get("https://www.agoda.com/");
    }

    public void enterHotel(String hotelName) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        input.clear();
        input.sendKeys(hotelName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("li[data-testid='topDestinationListItem'][data-element-index='0']")
        ));
        firstSuggestion.click();
    }

    public void selectDates(LocalDate checkIn, LocalDate checkOut) {
        WebElement checkInBox = driver.findElement(By.id("check-in-box"));

        String expanded = checkInBox.getAttribute("aria-expanded");

        if ("true".equals(expanded)) {
            System.out.println("Calendar is OPEN");

        } else {
            System.out.println("Calendar is CLOSED");
            driver.findElement(By.cssSelector("div[data-selenium='checkInBox']")).click();
        }
        By checkInDateLocator = By.cssSelector("[data-selenium-date='" + checkIn.toString() + "']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement checkInDateElement = wait.until(
                ExpectedConditions.elementToBeClickable(checkInDateLocator)
        );
        checkInDateElement.click();

        By checkOutDateLocator = By.cssSelector("[data-selenium-date='" + checkOut.toString() + "']");

        WebElement checkOutDateElement = wait.until(
                ExpectedConditions.elementToBeClickable(checkOutDateLocator)
        );
        checkOutDateElement.click();
    }

    public void setRoomOptions(int room, int adults, int children) {
        By popupLocator = By.cssSelector("[data-selenium='occupancyPicker']");

        boolean isPopupVisible = false;
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
            isPopupVisible = true;
        } catch (TimeoutException e) {
            isPopupVisible = false;
        }

        if (!isPopupVisible) {
            WebElement occupancyBox = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("[data-element-name='occupancy-box']")
                    )
            );
            occupancyBox.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));

            System.out.println("Popup was CLOSED → now OPENED");
        } else {
            System.out.println("Popup already OPEN");
        }

        By roomValue = By.cssSelector("[data-selenium='desktop-occ-room-value'] p");
        By adultValue = By.cssSelector("[data-selenium='desktop-occ-adult-value'] p");
        By childrenValue = By.cssSelector("[data-selenium='desktop-occ-children-value'] p");

        By roomPlus = By.cssSelector("[data-selenium='occupancyRooms'] [data-selenium='plus']");
        By roomMinus = By.cssSelector("[data-selenium='occupancyRooms'] [data-selenium='minus']");

        By adultPlus = By.cssSelector("[data-selenium='occupancyAdults'] [data-selenium='plus']");
        By adultMinus = By.cssSelector("[data-selenium='occupancyAdults'] [data-selenium='minus']");

        By childrenPlus = By.cssSelector("[data-selenium='occupancyChildren'] [data-selenium='plus']");
        By childrenMinus = By.cssSelector("[data-selenium='occupancyChildren'] [data-selenium='minus']");

        setValue(roomValue, roomPlus, roomMinus, room);
        setValue(adultValue, adultPlus, adultMinus, adults);
        setValue(childrenValue, childrenPlus, childrenMinus, children);
        selectRandomChildAge();

        Assert.assertEquals(getValue(roomValue), 1);
        Assert.assertEquals(getValue(adultValue), 4);
        Assert.assertEquals(getValue(childrenValue), 2);

        WebElement occupancyBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("occupancy-box"))
        );

        occupancyBox.click();
    }

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    private void clickPlus(By plusBtn, int times) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(plusBtn));

        for (int i = 0; i < times; i++) {
            btn.click();
        }
    }

    private int getValue(By locator) {
        return Integer.parseInt(driver.findElement(locator).getText().trim());
    }

    private void setValue(By valueLocator, By plusBtn, By minusBtn, int target) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int current = getValue(valueLocator);
        int diff = target - current;

        if (diff > 0) {
            WebElement plus = wait.until(ExpectedConditions.elementToBeClickable(plusBtn));
            for (int i = 0; i < diff; i++) {
                plus.click();
            }
        } else if (diff < 0) {
            WebElement minus = wait.until(ExpectedConditions.elementToBeClickable(minusBtn));
            for (int i = 0; i < Math.abs(diff); i++) {
                minus.click();
            }
        }
    }

    public void selectRandomChildAge() {
        // Mở dropdown
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[data-element-name='occ-child-age-dropdown']")
        ));
        dropdown.click();

        // Lấy tất cả option trong listbox
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("ul[role='listbox'] li")
        ));

        // Chọn random 1 option
        Random rand = new Random();
        WebElement randomOption = options.get(rand.nextInt(options.size()));
        randomOption.click();
    }

    public void handleChildAgeWarningIfPresent() {
        By warningLocator = By.cssSelector("[data-selenium='childAgeWarning']");
        By occupancyBoxLocator = By.cssSelector("[data-element-name='occupancy-box']");

        try {
            // chờ tối đa 3s xem warning có xuất hiện không
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            WebElement warning = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(warningLocator)
            );

            if (warning.isDisplayed()) {
                System.out.println("Warning appeared → handling it");

                WebElement occupancyBox = wait.until(
                        ExpectedConditions.elementToBeClickable(occupancyBoxLocator)
                );

                occupancyBox.click(); // đóng

                wait.until(ExpectedConditions.invisibilityOfElementLocated(warningLocator));
            }

        } catch (TimeoutException e) {
            // Không có warning xuất hiện → bỏ qua
            System.out.println("No child age warning");
        }
    }

}
