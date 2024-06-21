import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutomationProject3_EdmundsCarSearch {

    @Test
    public void testCase() throws InterruptedException, IOException {

         WebDriver driver = new ChromeDriver();
         driver.manage().window().maximize();
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

// 1. Navigate to https://www.edmunds.com/
         driver.get("https://www.edmunds.com/");

// 2. Click on Shop Used
         driver.findElement(By.linkText("Shop Used")).click();


// 3. In the next page, clear the zipcode field and enter 22031
         driver.findElement(By.xpath("//input[@aria-label='Near ZIP']"))
                 .sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);

         driver.findElement(By.xpath("//input[@aria-label='Near ZIP']")).sendKeys("22031", Keys.ENTER);
         Thread.sleep(1000);

// 4. Check the checkbox -> only show local listings
         WebElement checkbox = driver.findElement(By.xpath("//label[@data-tracking-value='deliveryType~local~Only show local listings']//span[@class='checkbox checkbox-icon size-18 icon-checkbox-unchecked3 text-gray-form-controls']"));

         if (!checkbox.isSelected()) {
             checkbox.click();
             ;
         }
         Thread.sleep(1000);

// 5. Choose Tesla for a make
         Select makeDropdown = new Select(driver.findElement(By.id("usurp-make-select")));

         makeDropdown.selectByVisibleText("Tesla");
         Thread.sleep(1000);

//6. Verify that the default selected option in Models dropdown is Add Model for Model dropdown.
//   And the default years are 2011 and 2024 in year input fields.

         ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500)");
         Thread.sleep(1000);

         Select modelDropdown = new Select(driver.findElement(By.id("usurp-model-select")));
         Assert.assertEquals(modelDropdown.getFirstSelectedOption().getText(), "Add Model");

         WebElement yearMin = driver.findElement(By.xpath("//input[@id='min-value-input-Year']"));
         Assert.assertEquals(yearMin.getAttribute("value"), "2011");

         WebElement yearMax = driver.findElement(By.xpath("//input[@id='max-value-input-Year']"));
         Assert.assertEquals(yearMax.getAttribute("value"), "2024");

// 7. Verify that Model dropdown options are [Add Model, Model 3, Model S, Model X, Model Y, Cybertruck, Roadster]

         List<String> expectedModels = List.of("Add Model", "Model 3", "Model S", "Model X", "Model Y", "Cybertruck", "Roadster");

         List<WebElement> options = new Select(driver.findElement(By.id("usurp-model-select"))).getOptions();

         List<String> actualModels = new ArrayList<>();

         for (WebElement option : options) {
             actualModels.add(option.getText());
         }

         Assert.assertEquals(actualModels, expectedModels);

// 8. In Models dropdown, choose Model 3

         modelDropdown.selectByVisibleText("Model 3");
         Thread.sleep(2000);

// 9. Enter 2020 for year min field and hit enter (clear the existing year first)

         WebElement yearMin1 = driver.findElement(By.xpath("//input[@id='min-value-input-Year']"));

         yearMin1.sendKeys(Keys.chord(Keys.COMMAND, "A"), Keys.BACK_SPACE);
         yearMin1.sendKeys("2020", Keys.ENTER);
         Thread.sleep(1000);

// 10 In the results page, verify that there are 21 search results, excluding the sponsored result/s.
// And verify that each search result title contains ‘Tesla Model 3’

         List<WebElement> results = driver.findElements(By.xpath("//div[contains(text(), 'Tesla Model 3')]"));

         Assert.assertEquals(results.size(), 21);

         for (WebElement result : results) {
             Assert.assertTrue(result.getText().contains("Tesla Model 3"));
         }

//11. Extract the year from each search result title and verify that each year is within the selected range (2020-2023)

         List<String> resultsText = new ArrayList<>();
         results.forEach(s -> resultsText.add(s.getText()));
         System.out.println(resultsText);

         List<Integer> listYears = new ArrayList<>();
         resultsText.forEach(s -> listYears.add(Integer.valueOf(s.replace(" Tesla Model 3", ""))));
         listYears.sort(Comparator.reverseOrder());
         System.out.println(listYears);

         for (Integer listYear : listYears) {
             Assert.assertTrue(listYear >= 2020 && listYear <= 2023);
         }

// 12. From the dropdown on the right corner choose “Price: Low to High” option and verify that the results are sorted
//     from lowest to highest price.

         Select priceDropdown = new Select(driver.findElement(By.id("sort-by")));
         priceDropdown.selectByVisibleText("Price: Low to High");
         Thread.sleep(2000);

         List<WebElement> prices = driver.findElements(By.xpath("//span[@class='heading-3']"));

         List<Integer> actualPrices = new ArrayList<>();

         for (WebElement price : prices) {
             actualPrices.add(Integer.valueOf(price.getText().replaceAll("[$,]", "")));
         }
         System.out.println("Actual Prices Low to High: " + actualPrices);

         List<Integer> expectedPrices = new ArrayList<>(actualPrices);
         expectedPrices.sort(Comparator.naturalOrder());
         System.out.println("Expected Prices Low to High: " + expectedPrices);

         Assert.assertEquals(actualPrices, expectedPrices);

// 13. From the dropdown menu, choose “Price: High to Low” option and verify that the results are sorted from highest to lowest price.

         Select priceDropdown2 = new Select(driver.findElement(By.id("sort-by")));
         priceDropdown2.selectByVisibleText("Price: High to Low");
         Thread.sleep(2000);

         List<WebElement> prices2 = driver.findElements(By.xpath("//span[@class='heading-3']"));

         List<Integer> actualPrices2 = new ArrayList<>();

         for (WebElement price : prices2) {
             actualPrices2.add(Integer.valueOf(price.getText().replaceAll("[$,]", "")));
         }
         System.out.println("Actual Prices High to Low: " + actualPrices2);

         List<Integer> expectedPrices2 = new ArrayList<>(actualPrices2);
         expectedPrices2.sort(Comparator.reverseOrder());
         System.out.println("Expected Prices High to Low: " + expectedPrices2);

         Assert.assertEquals(actualPrices2, expectedPrices2);

// 14. From the dropdown menu, choose “Mileage: Low to High” option and verify that the results are sorted from highest to lowest mileage.

         Select mileageDropdown = new Select(driver.findElement(By.id("sort-by")));
         priceDropdown2.selectByVisibleText("Mileage: Low to High");
         Thread.sleep(2000);

         List<WebElement> miles = driver.findElements(By.xpath("//span[contains(text(), 'miles')][@class='text-cool-gray-30']"));

         List<Integer> actualMiles = new ArrayList<>();

         for (WebElement mile : miles) {
             actualMiles.add(Integer.valueOf(mile.getText().replaceAll("[, miles]", "")));
         }
         System.out.println("Actual  miles Low to High: " + actualMiles);

         List<Integer> expectedMiles = new ArrayList<>(actualMiles);
         expectedMiles.sort(Comparator.naturalOrder());
         System.out.println("Expected miles Low to High: " + expectedMiles);

         Assert.assertEquals(actualMiles, expectedMiles);

// 15. Find the last result and store its title, price and mileage (get the last result dynamically,
// i.e., your code should click on the last result regardless of how many results are there).
// Click on it to open the details about the result.

         List<WebElement> titleElements = driver.findElements(By.xpath("//div[@class='size-16 text-cool-gray-10 font-weight-bold mb-0_5']"));
         String lastTitle = titleElements.get(titleElements.size() - 1).getText();
         System.out.println("Last Title: " + lastTitle);

         List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='heading-3']"));
         String lastPrice = priceElements.get(priceElements.size() - 1).getText();
         System.out.println("Last Price: " + lastPrice);

         List<WebElement> mileageElements = driver.findElements(By.xpath("//span[contains(text(), 'miles')][@class='text-cool-gray-30']"));
         String lastMileage = mileageElements.get(mileageElements.size() - 1).getText().replace(" miles", "");
         System.out.println("Last Mileage: " + lastMileage);

         List<WebElement> elements = driver.findElements(By.xpath("//div[@class='vehicle-info d-flex flex-column pos-r p-1']"));
         elements.get(elements.size() - 1).click();

// 16. Verify the title price and mileage matches the info from the previous step.

         String expectedTitle = driver.findElement(By.xpath("//h1[.='2023 Tesla Model 3']")).getText();
         String expectedPrice = driver.findElement(By.xpath("//span[@data-testid='vdp-price-row']")).getText();
         String expectedMileage = driver.findElement(By.xpath("//div[.='Mileage']/following-sibling::div")).getText();

         Assert.assertEquals(lastTitle, expectedTitle);
         Assert.assertEquals(lastPrice, expectedPrice);
         Assert.assertEquals(lastMileage, expectedMileage);

// 17. Go back to the results page and verify that the clicked result has “Viewed” element on it.

         driver.navigate().back();
         Assert.assertTrue(elements.get(elements.size() - 1).getText().contains("Viewed"));


         Thread.sleep(3000);
         driver.quit();


     }
}
