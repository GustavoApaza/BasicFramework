package runner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OnlyTestNG {
    WebDriver driver;
    // ========================================================================
    // LOCATORS
    // ========================================================================
    By samsungGalaxyS6Link = By.xpath("//a[normalize-space()='Samsung galaxy s6']");
    By priceElement = By.xpath("//h3[@class='price-container']");
    By addToCartButton = By.xpath("//a[contains(text(),'Add to cart')]");
    By homeNavLink = By.xpath("/html[1]/body[1]/nav[1]/div[1]/div[1]/ul[1]/li[1]/a[1]");
    By nokiaLumia1520Link = By.linkText("Nokia lumia 1520");
    By cartNavLink = By.id("cartur");
    By totalPrice = By.cssSelector("h3[id='totalp']");
    // ========================================================================
    // CONFIGURATION OF THE NAVIGATOR CHROME TO ENABLE IT
    // ========================================================================
    @BeforeTest
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
    }
    // ========================================================================
    // AUTOMATED TEST CASES
    // ========================================================================
    @Test
    public void totalPriceOfTheCart() throws InterruptedException {
        ArrayList<Integer> listNumbers = new ArrayList<>();
        // Implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        // I add the first product to the cart
        WebElement samsungGalaxyS6 = driver.findElement(samsungGalaxyS6Link);
        samsungGalaxyS6.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        listNumbers.add(getPrice(priceElement));
        WebElement addButton = driver.findElement(addToCartButton);
        addButton.click();
        acceptAlert(driver);
        // I add the second product to the cart
        driver.findElement(homeNavLink).click();
        driver.findElement(nokiaLumia1520Link).click();
        listNumbers.add(getPrice(priceElement));
        WebElement addButton2 = driver.findElement(addToCartButton);
        addButton2.click();
        acceptAlert(driver);
        // I navigate to the cart page
        driver.findElement(cartNavLink).click();
        WebElement totalPriceWebElement = driver.findElement(totalPrice);
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(d -> totalPriceWebElement.isDisplayed());
        // Compare the total value of the array with the element of total price of the page
        String totalPriceOfArray = String.valueOf(listNumbers.stream().mapToInt(Integer::intValue).sum());
        String totalPriceOfThePage = totalPriceWebElement.getText();
//        System.out.println("This is totalPriceOfArray: " + totalPriceOfArray);
//        System.out.println("This is totalPriceOfThePage: " + totalPriceOfThePage);
        // Assert.Method(actual, expected)
        Assert.assertEquals(totalPriceOfArray, totalPriceOfThePage);
    }
    // ========================================================================
    // CLOSE THE NAVIGATOR CHROME
    // ========================================================================
    @AfterTest
    public void tearDown(){
        driver.quit();
    }
    // ========================================================================
    // METHODS
    // ========================================================================
    public int getPrice(By priceElement){
        WebElement priceWebElement = driver.findElement(priceElement);
        String priceText = priceWebElement.getText();
        // Definir la expresión regular para encontrar números
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(priceText);

        // Iterar sobre las coincidencias y concatenarlas en una cadena
        StringBuilder numbers = new StringBuilder();
        while (matcher.find()) {
            numbers.append(matcher.group());
        }
        return Integer.parseInt(numbers.toString());
    }
    public boolean isAlertPresent() {
        boolean presentFlag = false;
        try {
            Alert alert = driver.switchTo().alert();
            presentFlag = true;
        } catch (NoAlertPresentException ex) {
            ex.printStackTrace();
        }
        return presentFlag;
    }
    public void acceptAlert(WebDriver driver) throws InterruptedException {
        int i = 0;
        while(i++ < 5) {
            boolean flag = isAlertPresent();
            if(flag) {
                driver.switchTo().alert().accept();
                break;
            }
            if(i == 5) {
                throw new java.lang.Error("Alert isn't display");
            } else {
                Thread.sleep(500);
                continue;
            }
        }
    }
}
