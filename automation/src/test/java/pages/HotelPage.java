package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HotelPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HotelPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private By price = By.xpath("//span[contains(@class,'price')]");

    public boolean isPriceDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(price)).isDisplayed();
    }
}