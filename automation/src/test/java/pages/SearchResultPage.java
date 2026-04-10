package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResultPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SearchResultPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private By firstHotel = By.xpath("(//div[@data-selenium='hotel-item'])[1]");

    public void selectFirstHotel() {
        wait.until(ExpectedConditions.elementToBeClickable(firstHotel)).click();
    }
}
