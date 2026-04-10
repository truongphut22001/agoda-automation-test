package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.HotelPage;
import pages.SearchResultPage;

import java.time.LocalDate;

public class AgodaTest extends BaseTest {

    @Test
    public void testHotelPriceDisplayed() {
        HomePage home = new HomePage(driver, wait);
        SearchResultPage result = new SearchResultPage(driver, wait);
        HotelPage hotel = new HotelPage(driver, wait);
        // 1. Open Agoda homepage
        home.open();
        // 2. Enter hotel name "Muong Thanh Saigon Centre Hotel"
        String hotelName = "Muong Thanh Saigon Centre Hotel";
        home.enterHotel(hotelName);
        // 3. Select check-in date = current date + 2
        // 4. Select check-out date = current date + 3
        home.selectDates(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
        // 5. Set room: 1 room, 4 adults, 2 children
        home.setRoomOptions(1, 4, 2);
        // 6. Click Search
        home.clickSearch();
        result.waitForSearchPage();
        // 7. Select the first available hotel
        result.selectFirstHotel();
        // 8. Verify that the hotel price is displayed
        Assert.assertTrue(hotel.isPriceDisplayed(), "Price is not displayed! No rooms are available for the selected options.");
    }
}
