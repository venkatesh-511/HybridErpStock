package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary 
{
	public static WebDriver driver;
	public static Properties conpro;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("PropertyFiles\\Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver= new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is Not Matching",true);
		}
		return driver;
		
	}
	//method for launching url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for wait to any element
	public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
	}
	//method for any textbox
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
	}
	//method for buttons,checkbox,radiobutton,images,links
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//method for validate page title
	public static void validateTitle(String Expected_Title)
	{
		String Actual_title = driver.getTitle();
		try {
		Assert.assertEquals(Actual_title, Expected_Title,"Title is Not Matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
	//method for close browser
	public static void closeBrowser()
	{
		driver.quit();
	}
	// method for list boxes
		public static void dropDownAction(String LocatorType, String LocatorValue, String TestData)
		{
			if(LocatorType.equalsIgnoreCase("xpath"))
			{
				// convert testdata cell into int
				int value = Integer.parseInt(TestData);
				Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
				element.selectByIndex(value);
			}
			if(LocatorType.equalsIgnoreCase("name"))
			{
				// convert testdata cell into int
				int value = Integer.parseInt(TestData);
				Select element = new Select(driver.findElement(By.name(LocatorValue)));
				element.selectByIndex(value);
			}
			if(LocatorType.equalsIgnoreCase("id"))
			{
				// convert testdata cell into int
				int value = Integer.parseInt(TestData);
				Select element = new Select(driver.findElement(By.id(LocatorValue)));
				element.selectByIndex(value);
			}
		}
	// method to capture stock number into notepad
		public static void captureStock(String LocatorType, String LocatorValue) throws Throwable
		{
			String stockNumber = "";
			if(LocatorType.equalsIgnoreCase("xpath"))
			{
				stockNumber = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("name"))
			{
				stockNumber = driver.findElement(By.name(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("id"))
			{
				stockNumber = driver.findElement(By.id(LocatorValue)).getAttribute("value");
			}
			// wriring stock number into notepad
			FileWriter fw = new FileWriter("./CaptureData/Stocknumber.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(stockNumber);
			bw.flush();
			bw.close();
			
		}
		// verify stock number in stock table
			public static void stockTable() throws Throwable 
			{
				FileReader fr = new FileReader("./CaptureData/Stocknumber.txt");
				BufferedReader br = new BufferedReader(fr);
				String Exp_Data = br.readLine();
				// click search panel if search box is not displayed
				if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
					driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
				driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
				Thread.sleep(3000);
				String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr/td[8]/div/span/span")).getText();
				Reporter.log(Exp_Data + "  " + Act_Data,true);
				try {
					Assert.assertEquals(Exp_Data, Act_Data,"stock number not found in table");
					
				} catch (Exception a) {
					System.out.println(a.getMessage());
				}
			}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}

