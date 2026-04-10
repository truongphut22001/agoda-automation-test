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
        //    1. Open Agoda homepage
        //    2. Enter hotel name "Muong Thanh Saigon Centre Hotel"
        //    3. Select check-in date = current date + 2
        //    4. Select check-out date = current date + 3
        //    5. Set room: 1 room, 4 adults, 2 children
        //    6. Click Search
        //    7. Select the first available hotel
        //    8. Verify that the hotel price is displayed

        HomePage home = new HomePage(driver, wait);
        SearchResultPage result = new SearchResultPage(driver, wait);
        HotelPage hotel = new HotelPage(driver, wait);

        home.open();
        home.enterHotel("Muong Thanh Saigon Centre Hotel");
        home.selectDates(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
        home.setGuests(4, 2);
        home.clickSearch();

        result.selectFirstHotel();

        Assert.assertTrue(hotel.isPriceDisplayed(), "Price is not displayed!");
    }
}
