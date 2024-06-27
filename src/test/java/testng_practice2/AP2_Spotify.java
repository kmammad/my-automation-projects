package testng_practice2;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
        Practice2Utils.writeInvalidLoginDataToFile("src/test/java/testng_practice2/invalidLogin.csv", 1);
    }

    @AfterTest
    public void deleteFile() {
        signUpFile.delete();
        validLoginFile.delete();
        invalidLoginFile.delete();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://open.spotify.com/");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 1, dataProvider = "getSignUpData")
    public void testSignUpAndLogin(String email, String password, String fullName, String month, String day, String year) throws InterruptedException {

        driver.findElement(By.xpath("//button[@data-testid='signup-button']")).click();
        driver.findElement(By.cssSelector("input#username")).sendKeys(email);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@data-testid='submit']")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("input#new-password")).sendKeys(password);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@data-testid='submit']")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("input#displayName")).sendKeys(fullName);
        Thread.sleep(1000);

        Select monthDropdown = new Select(driver.findElement(By.id("month")));
        monthDropdown.selectByVisibleText(month);

        driver.findElement(By.id("day")).sendKeys(day, Keys.TAB, year);

        List<WebElement> radioList = driver.findElements(By.cssSelector("div[class='Radio-sc-tr5kfi-0 hLLKvs']"));
        for (WebElement button : radioList) {
             Random rand = new Random();
            radioList.get(rand.nextInt( radioList.size())).click();
        }
        driver.findElement(By.xpath("//button[@data-testid='submit']")).click();
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("label[class='Label-sc-cpoq-0 havZVP']")).click();
        driver.findElement(By.xpath("//span[.='Sign up']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).click();

        Assert.assertTrue(driver.getPageSource().contains("Log out"));

        driver.findElement(By.xpath("//button[@data-testid='user-widget-dropdown-logout']")).click();

        Assert.assertTrue(driver.getPageSource().contains("Log in"));
    }

    @Test(priority = 2, dataProvider = "getValidLogin")
    public void testValidLogin(String email, String password) throws InterruptedException {

        driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("login-username")).sendKeys(email, Keys.TAB, password, Keys.ENTER);
        Thread.sleep(1000);

        driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).click();

        Assert.assertTrue(driver.getPageSource().contains("Log out"));

        driver.findElement(By.xpath("//button[@data-testid='user-widget-dropdown-logout']")).click();
        Thread.sleep(1000);

        Assert.assertTrue(driver.getPageSource().contains("Log in"));
    }

    @Test(priority = 3, dataProvider = "getInvalidLogin")
    public void testInvalidLogin(String email, String password) throws InterruptedException {

        driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("login-username")).sendKeys(email, Keys.TAB, password, Keys.ENTER);
        Thread.sleep(2000);

        Assert.assertTrue(driver.getPageSource().contains("Incorrect username or password."));

        Assert.assertTrue(driver.getPageSource().contains("Log in to Spotify"));
    }

    @Test(priority = 4, dataProvider = "getValidLogin")
    public void testSearchMusic(String email, String password) throws InterruptedException {

        driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("login-username")).sendKeys(email, Keys.TAB, password, Keys.ENTER);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[@data-testid='username-first-letter']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Log out"));
        driver.findElement(By.xpath("//button[@data-testid='user-widget-dropdown-logout']")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("Log in"));

        driver.findElement(By.linkText("Search")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("What do you want to play?"));
        driver.findElement(By.xpath("//input[@data-testid='search-input']")).sendKeys("Adele Hello", Keys.ENTER);
        Thread.sleep(3000);
        driver.findElement (By.linkText("Hello")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//button[@data-testid='control-button-playpause']")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//button[@data-testid='control-button-playpause']")).click();
        String actualSongName = driver.findElement(By.linkText("Hello")).getText();
        Thread.sleep(1000);
        Assert.assertEquals(actualSongName, "Hello");
        String actualArtistName = driver.findElement(By.linkText("Adele")).getText();
        Thread.sleep(1000);
        Assert.assertEquals(actualArtistName, "Adele");
    }



        @DataProvider
    public static Object[][] getSignUpData() {
        return readData("src/test/java/testng_practice2/signUpData.csv");
    }

    @DataProvider
    public static Object[][] getValidLogin() {
        return readData("src/test/java/testng_practice2/validLogin.csv");
    }

    @DataProvider
    public static Object[][] getInvalidLogin() {
        return readData("src/test/java/testng_practice2/invalidLogin.csv");
    }
}
