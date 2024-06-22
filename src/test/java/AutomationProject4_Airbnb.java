import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AutomationProject4_Airbnb {

    @Test
    public void testCase() throws InterruptedException {

        // hello Akmal and Yana

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

//Login:
//  Navigate to Airbnb's homepage.
        driver.get("https://www.airbnb.com/");

//  Click on the profile icon and 'Log in' link in the upper right corner (it's a link with a text 'Log in')
        driver.findElement(By.xpath("//button[@data-testid='cypress-headernav-profile']")).click();
        Thread.sleep(1000);
        driver.findElement(By.linkText("Log in")).click();

//  Enter valid credentials (email and password) and log in
       driver.findElement(By.xpath("//div[.='Continue with email']")).click();
       driver.findElement(By.xpath("user[email]")).sendKeys("jglob13@gmail.com", Keys.ENTER);
       driver.findElement(By.name("user[password]")).sendKeys("Rental132020", Keys.ENTER);

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

//Filtering Results:
//  Click on Filters button
        driver.findElement(By.xpath("//button[@data-testid='category-bar-filter-button']")).click();
        Thread.sleep(2000);

//  In Price Range section set the min price to 100 and max price to 600 and apply the filter
       WebElement priceMin = driver.findElement(By.id("price_filter_min"));
       priceMin.sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);
       priceMin.sendKeys("100");

        WebElement priceMax = driver.findElement(By.id("price_filter_max"));
        priceMax.sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);
        priceMax.sendKeys("600");

        driver.findElement(By.xpath("//div[@class='ptiimno atm_7l_1p8m8iw dir dir-ltr']//a[@href]")).click();

//  Extract all the prices from the search results and verify that each of them is within the range

        List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='_1e9y657'] | //span[@class='_t6t62s']"));

        List<Integer> actualPrices = new ArrayList<>();

        for (WebElement price : priceElements) {
            actualPrices.add(Integer.valueOf(price.getText().replace("$", "")));
        }

        System.out.println(actualPrices);



//  Note: Some prices are discounted, some of them are not, so you need 2 separate locators to extract both and combine them with or (|)
//  For example: //span[@class='_tyxjp1'] | //span[@class='_1y74zjx']



    }
}
