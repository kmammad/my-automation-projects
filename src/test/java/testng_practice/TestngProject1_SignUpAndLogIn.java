package testng_practice;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestngProject1_SignUpAndLogIn {

    @Test
    public void test1() throws InterruptedException {

            WebDriver driver = null;

            try {

                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.navigate().to("http://duotify.us-east-2.elasticbeanstalk.com/register.php");

                Thread.sleep(1000);

                String expectedTitle = "Welcome to Duotify!";
                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, expectedTitle);

                driver.findElement(By.xpath(" //span[@id='hideLogin']")).click();

                Thread.sleep(1000);

                Faker faker = new Faker();

               String userName = faker.name().username();
               String firstName = faker.name().firstName();
               String lastName = faker.name().lastName();
               String email = faker.internet().emailAddress();
               String password = faker.internet().password();

                driver.findElement(By.id("username")).sendKeys(userName);
                driver.findElement(By.id("firstName")).sendKeys(firstName);
                driver.findElement(By.id("lastName")).sendKeys(lastName);
                driver.findElement(By.id("email")).sendKeys(email);
                driver.findElement(By.id("email2")).sendKeys(email);
                driver.findElement(By.id("password")).sendKeys(password);
                driver.findElement(By.id("password2")).sendKeys(password);

                driver.findElement(By.xpath("//button[@name='registerButton']")).click();

                Thread.sleep(1000);

                String expectedUrl = "http://duotify.us-east-2.elasticbeanstalk.com/browse.php?";
                String actualUrl = driver.getCurrentUrl();

                Assert.assertEquals(actualUrl, expectedUrl);

               String fullName = driver.findElement(By.id("nameFirstAndLast")).getText();
               Assert.assertEquals(fullName, firstName + " " + lastName);

               driver.findElement(By.id("nameFirstAndLast")).click();
               Thread.sleep(1000);

               String userNameCenter = driver.findElement(By.xpath("//div[@class='userInfo']")).getText();
               Assert.assertEquals(userNameCenter, firstName + " " + lastName);

               //logout
               driver.findElement(By.id("rafael")).click();
                Thread.sleep(1000);

                //logout URL
                String expectedUrl1 = "http://duotify.us-east-2.elasticbeanstalk.com/register.php";
                String actualUrl1 = driver.getCurrentUrl();

                Assert.assertEquals(actualUrl1, expectedUrl1);


                driver.findElement(By.id("loginUsername")).sendKeys(userName);
                driver.findElement(By.id("loginPassword")).sendKeys(password);
                driver.findElement(By.name("loginButton")).click();

                Thread.sleep(1000);

               String actualText = driver.findElement(By.xpath("//h1[@class='pageHeadingBig']")).getText();
               String expectedText = "You Might Also Like";
               Assert.assertEquals(actualText, expectedText);

                //logout
                driver.findElement(By.id("nameFirstAndLast")).click();
                Thread.sleep(1000);
                driver.findElement(By.id("rafael")).click();
                Thread.sleep(1000);

                //logout URL
                String expectedUrl2 = "http://duotify.us-east-2.elasticbeanstalk.com/register.php";
                String actualUrl2 = driver.getCurrentUrl();

                Assert.assertEquals(actualUrl2, expectedUrl2);


            } finally {
                driver.quit();
            }
        }
    }
