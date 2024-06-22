import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.time.Duration;

public class Calendars {


    @Test

    public void test() throws InterruptedException {


        WebDriver driver = null; //div[@class='vehicle-info d-flex flex-column pos-r p-1']
try {
   driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

//Login:
//  Navigate to Airbnb's homepage.
    driver.get("https://www.airbnb.com/");
    driver.findElement(By.xpath("//div[.='Check in']")).click();
//        driver.findElement(By.xpath("//div[@data-testid='calendar-day-07/14/2024']")).click();
//        driver.findElement(By.xpath("//div[@data-testid='calendar-day-07/21/2024']")).click();

    try {
        clickOnDates(driver, "07/12/2024", "07/23/2024");
    } catch (ElementClickInterceptedException e) {
//        System.out.println("Pop up"); //click on x
        driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
        Thread.sleep(1000);
        clickOnDates(driver, "07/12/2024", "07/23/2024");
    }


}
finally {
    //driver.quit();
}
    }


    public static void clickOnDates(WebDriver driver, String start, String end){

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",driver.findElement(By.xpath("//div[@data-testid='calendar-day-"+start+"']")));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",driver.findElement(By.xpath("//div[@data-testid='calendar-day-"+end+"']")));
    }
}
