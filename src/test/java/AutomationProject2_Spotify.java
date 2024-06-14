import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class AutomationProject2_Spotify {

    @Test
    public void testCase() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://open.spotify.com/");

// 1. Login
        driver.findElement(By.xpath("//button[@data-testid='login-button']")).click();

        driver.findElement(By.xpath("//input[@id='login-username']")).clear();
        driver.findElement(By.xpath("//input[@id='login-username']")).sendKeys ("jglob13@gmail.com", Keys.TAB,
                "Jonglobe132020");
        driver.findElement(By.xpath("//button[@id='login-button']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).isDisplayed());

// 2. Music search
        driver.findElement(By.linkText("Search")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("What do you want to play?"));

        driver.findElement(By.xpath("//input[@data-testid='search-input']")).sendKeys("Adele Hello", Keys.ENTER);

        Thread.sleep(3000);

        driver.findElement (By.linkText("Hello")).click();

       driver.findElement(By.xpath("//button[@data-testid='control-button-playpause']")).click();

       Thread.sleep(5000);

       driver.findElement(By.xpath("//button[@data-testid='control-button-playpause']")).click();

        String actualSongName = driver.findElement(By.linkText("Hello")).getText();
        Thread.sleep(1000);
        Assert.assertEquals(actualSongName, "Hello");

        String actualArtistName = driver.findElement(By.linkText("Adele")).getText();
        Thread.sleep(1000);
        Assert.assertEquals(actualArtistName, "Adele");

// 3. Logout

        driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[.='Log out']")).click();

        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("Log in"));

        Assert.assertTrue(driver.findElement(By.xpath("//button[@data-testid='login-button']")).isDisplayed());

//      Thread.sleep(5000);
        driver.quit();



    }
}
