package testng_practice2;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import testng_practice1.Practice1Utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import static testng_practice1.Practice1Utils.readData;

public class AP2_Spotify {

    File signUpFile;
    File validLoginFile;
    File invalidLoginFile;

    WebDriver driver;

    @BeforeTest
    public void createFile() {
        try {
            signUpFile = new File("src/test/java/testng_practice2/signUpData.csv"); signUpFile.createNewFile();
            validLoginFile = new File("src/test/java/testng_practice2/validLogin.csv"); validLoginFile.createNewFile();
            invalidLoginFile = new File("src/test/java/testng_practice2/invalidLogin.csv"); invalidLoginFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Practice2Utils.writeSignUpDataToFile("src/test/java/testng_practice2/signUpData.csv",
                "src/test/java/testng_practice2/validLogin.csv", 1);
       // Practice2Utils.writeInvalidLoginDataToFile("src/test/java/testng_practice/invalidLogin.csv", 1);
    }

    @AfterTest
    public void deleteFile() {
//        signUpFile.delete();
//        validLoginFile.delete();
//        invalidLoginFile.delete();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 0, dataProvider = "getSignUpData")
    public void testSignUpAndLogin(String email, String password, String fullName, String month, String day, String year) throws InterruptedException {

        driver.get("https://open.spotify.com/");
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[@data-testid='signup-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("input#username")).sendKeys(email, Keys.ENTER);
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("input#new-password")).sendKeys(password, Keys.ENTER);
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("input#displayName")).sendKeys(fullName);
        Thread.sleep(1000);

        Select monthDropdown = new Select(driver.findElement(By.id("month")));
        monthDropdown.selectByVisibleText(month);
        Thread.sleep(1000);

        driver.findElement(By.id("day")).sendKeys(day, Keys.TAB, year);
        Thread.sleep(1000);

        List<WebElement> radioList = driver.findElements(By.cssSelector("input[type='radio']"));

        for (WebElement button : radioList) {
            Random rand = new Random();
            radioList.get(rand.nextInt( radioList.size())).click();
        }
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[@data-testid='submit']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@id='checkbox-privacy']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@id='checkbox-privacy']")).sendKeys(Keys.ENTER);
        Thread.sleep(1000);

        driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).click();
        Thread.sleep(2000);

        Assert.assertTrue(driver.getPageSource().contains("Log out"));

        driver.findElement(By.xpath("//button[@data-testid='user-widget-dropdown-logout']")).click();
        Thread.sleep(1000);

        Assert.assertTrue(driver.getPageSource().contains("Log in"));
    }

    @DataProvider
    public static Object[][] getSignUpData() {

        return Practice1Utils.readData("src/test/java/testng_practice2/signUpData.csv");
    }

}
