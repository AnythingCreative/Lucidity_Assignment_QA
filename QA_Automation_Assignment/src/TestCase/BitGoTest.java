package TestCase;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BitGoTest {

  public static void main(String[] args) {
    //System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // Commenting out as Selenium Webdriver will call ChromeDriver on its own
    WebDriver driver = new ChromeDriver();

    String baseUrl = "https://blockstream.info/block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732";

    // Test case 1: Validate block details div section heading
    validateBlockDetailsDiv(driver, baseUrl);

    // Test case 2: Parse transactions with one input and two outputs
    parseTransactions(driver, baseUrl);

    driver.quit();
  }

  
  //TestCase1
  public static void validateBlockDetailsDiv(WebDriver driver, String url) {
	  
	System.out.println("Starting Test Case 1: ");
    driver.get(url);
    driver.manage().window().maximize();

    try {
    	
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement blockDetails = wait.until(
          ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='active']"))
      );

      WebElement headingElement = driver.findElement(By.cssSelector("h3.font-h3"));
      String headingText = headingElement.getText();

      if (headingText.equals("25 of 2875 Transactions")) {
        System.out.println("Heading of block details div is validated as '25 of 2875 Transactions'");
      } else {
        System.out.println("Transaction list header text is incorrect: " + headingText);
      }
      System.out.println("Ending Test Case 1: ");
    } catch (Exception e) {
      System.out.println("Error occurred while validating block details div: " + e.getMessage());
    }
    
  }
  
  //TestCase2
  public static void parseTransactions(WebDriver driver, String url) {
	
	System.out.println("Starting Test Case 2: ");
    driver.get(url);
    driver.manage().window().maximize();

    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement transactionList = wait.until(
          ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.transactions"))
      );

      List<WebElement> transactions = transactionList.findElements(By.id("transaction-box"));
      System.out.println(transactions.size());
      for (WebElement transaction : transactions) {
        List<WebElement> inputs = transaction.findElements(By.cssSelector("div.vin"));
        //System.out.println(inputs); //used for debug
        List<WebElement> outputs = transaction.findElements(By.cssSelector("div.vout"));
        //System.out.println(outputs); //used for debug

        if (inputs.size() == 1 && outputs.size() == 2) {
          WebElement hashElement = transaction.findElement(By.xpath("//div[@class='vin']//span/a"));
          System.out.println("Transaction hash with 1 input and 2 outputs: " + hashElement.getText());
        }
      }
      System.out.println("Ending Test Case 2: ");
    } catch (Exception e) {
      System.out.println("Error occurred while parsing transactions: " + e.getMessage());
    }
    
  }
  
}