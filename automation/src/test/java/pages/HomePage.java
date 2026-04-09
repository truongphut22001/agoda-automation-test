package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private By searchBox = By.id("textInput");
    private By searchButton = By.xpath("//button[@data-selenium='searchButton']");

    public void open() {
        driver.get("https://www.agoda.com/");
    }

    public void enterHotel(String hotelName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(hotelName);
    }

    public void selectDates(LocalDate checkIn, LocalDate checkOut) {
        driver.findElement(By.cssSelector("div[data-selenium='checkInBox']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("td[data-date='" + checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("td[data-date='" + checkOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "']"))).click();
    }

    public void setGuests(int adults, int children) {
        driver.findElement(By.cssSelector("button[data-selenium='guestBox']")).click();
        for (int i = 2; i < adults; i++) {
            driver.findElement(By.cssSelector("button[data-selenium='adultIncrease']")).click();
        }
        for (int i = 0; i < children; i++) {
            driver.findElement(By.cssSelector("button[data-selenium='childIncrease']")).click();
        }
        driver.findElement(By.cssSelector("button[data-selenium='guestApply']")).click();
    }

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

}
