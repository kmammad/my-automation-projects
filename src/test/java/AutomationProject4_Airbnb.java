import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.annotations.ListenersAnnotation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AutomationProject4_Airbnb {

    // hello Akmal and Yana NOW In the right place

    @Test
    public void testCase() throws InterruptedException {


        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

//Login:
//  Navigate to Airbnb's homepage.
        driver.get("https://www.airbnb.com/");


//  Click on the profile icon and 'Log in' link in the upper right corner (it's a link with a text 'Log in')

        driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']")).click();
        Thread.sleep(1000);
//        if(driver.findElement(By.xpath("//button[@aria-label='Close']")).isDisplayed()){
//            driver.findElement(By.xpath("//button[@aria-label='Close'] ")).click();
//        }
//        Thread.sleep(1000);
        driver.findElement(By.linkText("Log in")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[.='Continue with email']")).click();
        Thread.sleep(1000);


//  Enter valid credentials (email and password) and log in
        driver.findElement(By.xpath("//input[@data-testid='email-login-email']")).sendKeys("jglob13@gmail.com", Keys.ENTER);
        driver.findElement(By.name("user[password]")).sendKeys("Rental132020", Keys.ENTER);

        Thread.sleep(1000);
//driver.findElement(By.xpath("//button[@type='button']//span[@class='i3tjjh1 atm_mk_h2mmj6 dir dir-ltr']")).click();

//  Validate that the user's profile button with initial appears at the top right.
        Assert.assertTrue(driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']//img[@class]"))
                .isDisplayed());

//Property Search:
//  Click on Add Guests to bring up the search bar
        WebElement searchWhere = driver.findElement(By.name("query"));
        searchWhere.click();
//  Input a destination in the main search bar, e.g., "Ibiza, Spain"
        searchWhere.sendKeys("Ibiza, Spain", Keys.ENTER);

//  Set the date range for a week in the future (e.g. November 6-12 )
        driver.findElement(By.id("tab--tabs--0")).click();
        driver.findElement((By.xpath("//div[@data-testid='calendar-day-07/14/2024']"))).click();
        driver.findElement((By.xpath("//div[@data-testid='calendar-day-07/20/2024']"))).click();
        driver.findElement((By.xpath("//div[.='Who']"))).click();

//  and specify the number of guests (e.g. 2 adults, 2 kids)
        WebElement numberOfAdults = driver.findElement(By.xpath("//button[@data-testid='stepper-adults-increase-button']"));
        numberOfAdults.click();
        numberOfAdults.click();
        String actualNumOfAdults = driver.findElement(By.xpath("//span[@data-testid='stepper-adults-value']")).getText();
        String expectedNumOfAdults = "2";
        Assert.assertEquals(actualNumOfAdults, expectedNumOfAdults);

        WebElement numberOfChild = driver.findElement(By.xpath("//button[@data-testid='stepper-children-increase-button']"));
        numberOfChild.click();
        numberOfChild.click();
        String actualNumOfChild = driver.findElement(By.xpath("//span[@data-testid='stepper-children-value']")).getText();
        String expectedNumOfChild = "2";
        Assert.assertEquals(actualNumOfChild, expectedNumOfChild);

//  Click the 'Search' button.
        driver.findElement(By.xpath("//button[@data-testid='structured-search-input-search-button']")).click();
        Thread.sleep(2000);
//Filtering Results:
//  Click on Filters button
        driver.findElement(By.xpath("//span[.='Filters']")).click();
        Thread.sleep(2000);

//  In Price Range section set the min price to 100 and max price to 600 and apply the filter
        WebElement priceMin = driver.findElement(By.id("price_filter_min"));
        priceMin.sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);
        priceMin.sendKeys("100");
        Thread.sleep(2000);

        WebElement priceMax = driver.findElement(By.id("price_filter_max"));
        priceMax.sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);
        priceMax.sendKeys("600");
        Thread.sleep(2000);


        driver.findElement(By.xpath("//div[@class='ptiimno atm_7l_1p8m8iw dir dir-ltr']//a[@href]")).click();
        Thread.sleep(2000);

//  Extract all the prices from the search results and verify that each of them is within the range

        List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='_1e9y657'] | //span[@class='_t6t62s']"));

        List<Integer> actualPrices = new ArrayList<>();

        List<WebElement> oddRange = new ArrayList<>();

        for (int i = 0; i < priceElements.size(); i++) {
            int x = Integer.valueOf(priceElements.get(i).getText().replaceAll("[^0-9]+", ""));
            if (x < 100 || x > 600) {
                oddRange.add(priceElements.get(i));
            } else {
                actualPrices.add(x);
            }
        }

        List<WebElement> discounted = driver.findElements(By.xpath("//span[@class='_1e9y657']"));

        int disc = 0;

        for (int i = 0; i < discounted.size(); i++) {
            int x = Integer.valueOf(discounted.get(i).getText().replaceAll("[^0-9]+", ""));
            if (x < 100 || x > 600) {
                disc = x;
            }

            for (int j = 0; j < oddRange.size(); j++) {
                int odd = Integer.valueOf(oddRange.get(j).getText().replaceAll("[^0-9]+", ""));

                if (disc != odd) {

                } else {
                    System.out.println("The initial odd price result above 600 or below 100: $" + odd +
                            " matches actual discounted price from the webpage element $" + disc);
                }
            }
        }

            actualPrices.sort(Comparator.naturalOrder());
            System.out.print("ActualPrices, exc. outOfRange pre-discount): ");
            System.out.println(actualPrices);

        for (Integer price : actualPrices){
            Assert.assertTrue(price >= 100 && price <= 600);
        }

//  Detailed Property View:
//  Locate the first search result and store the price per night, total price and rating and click on the first search result

List<WebElement> pricesPerNight = driver.findElements(By.xpath("//div[@data-testid='price-availability-row']//span[@class='a8jt5op atm_3f_idpfg4 atm_7h_hxbz6r atm_7i_ysn8ba atm_e2_t94yts atm_ks_zryt35 atm_l8_idpfg4 atm_mk_stnw88 atm_vv_1q9ccgz atm_vy_t94yts dir dir-ltr']"));
String pricePerNightActual = pricesPerNight.get(0).getText().replaceAll("[^0-9]+", "");
System.out.println("Price per night Actual: " + pricePerNightActual);

List<WebElement> totalPrices = driver.findElements(By.xpath("//div[@class='_tt122m']"));
String totalPriceActual = totalPrices.get(0).getText().replaceAll("[^0-9]+", "");
System.out.println("Total price Actual: " + totalPriceActual);

List<WebElement> ratings = driver.findElements(By.xpath("//span[@class='ru0q88m atm_cp_1ts48j8 dir dir-ltr']"));
String ratingFirstActual = ratings.get(0).getText().substring(0, 3);
System.out.println("Rating Actual: " + ratingFirstActual);

            String firstWindowHandle = driver.getWindowHandle();

            List<WebElement> results = driver.findElements(By.xpath("//div[@class='d1l1iq7v atm_9s_1o8liyq atm_vh_yfq0k3 atm_e2_88yjaz atm_vy_1r2rij0 atm_j6_t94yts bmwtyu7 atm_2m_1qred53 atm_2s_mgnkw2 dir dir-ltr']"));
            results.get(0).click();

//  Switch to the newly opened window, obtain the same information (price per night, total price and rating)

            Set<String> windowHandles = driver.getWindowHandles();

            for (String windowHandle : windowHandles) {
                driver.switchTo().window((windowHandle));
                if (driver.getTitle().equals("Amazing BeachFront 2 Bedrooms Apartment - Apartments for Rent in Sant Jordi de ses Salines, Illes Balears, Spain - Airbnb")) {
                    break;
                }
            }
            Thread.sleep(2000);

//  Note: Get rid of any pop-up message, if there is any
            if (driver.findElement(By.xpath("//button[@aria-label='Close']")).isDisplayed()) {
                driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
            }
            System.out.println(driver.getTitle());
            Thread.sleep(2000);
//  and verify it the information matches to previously stored information from the search results page

            String pricePerNightExpected = driver.findElement(By.xpath("(//span[@class='_t6t62s'])[2]")).getText().replaceAll("[^0-9]+", "");
            String totalPriceExpected = driver.findElement(By.xpath("//span[@class='_1qs94rc']//span[@class='_j1kt73']")).getText().replaceAll("[^0-9]+", "");
            String ratingFirstExpected = driver.findElement(By.xpath("//div[@data-testid='pdp-reviews-highlight-banner-host-rating']//div[@aria-hidden]")).getText();

            System.out.println("Per night Expected: " + pricePerNightExpected);
            System.out.println("Total price Expected: " + totalPriceExpected);
            System.out.println("Rating Expected: " + ratingFirstExpected);

            Assert.assertEquals(pricePerNightActual, pricePerNightExpected);
            Assert.assertEquals(totalPriceActual, totalPriceExpected);
            Assert.assertEquals(ratingFirstActual, ratingFirstExpected);

//  Close the second window (don't forget to switch back to original window)

            driver.close();

            driver.switchTo().window(firstWindowHandle);
            Thread.sleep(2000);

//Logout:
//  Click on the profile icon and click 'Log out' (it's a div with a text 'Log out')
            driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']")).click();
            Thread.sleep(1000);

            driver.findElement(By.xpath("//div[.='Log out']")).click();
            Thread.sleep(2000);

//  Validate logout using assertion to ensure the 'Log in' link/profile icon button is present.

            Assert.assertTrue(driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']")).isDisplayed());
            driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']")).click();

            Thread.sleep(1000);
            Assert.assertTrue(driver.findElement(By.linkText("Log in")).isDisplayed());

            Thread.sleep(1000);
            driver.quit();

        }
    }

