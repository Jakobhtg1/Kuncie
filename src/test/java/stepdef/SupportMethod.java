package stepdef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jayway.jsonpath.JsonPath;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class SupportMethod {

	static WebElement ele;
	static MobileElement mobileElement;
	static WebDriver driver;
	List<File> files;
	String result = null;
	String [] ext = {"json"};
	JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject;
	org.json.JSONArray jsonArray;
	File dir = new File("ObjectRepository");
	Object dataObject;

	@SuppressWarnings("rawtypes")
	static AppiumDriver appiumDriver;
	static AppiumDriverLocalService service;
	public DesiredCapabilities desiredCapabilities;
	public String postData;


	public String getLocatorFromPropFile(String keys) throws IOException{
		FileReader read = new FileReader("ObjectRepo.properties");
		Properties prop = new Properties();  
		prop.load(read);  
		String objLocator = prop.getProperty(keys);
		return objLocator;
	}   

	public String getDetectorFromPropFile(String keys) throws IOException{
		FileReader read = new FileReader("ObjectRepo.properties");
		Properties prop = new Properties();  
		prop.load(read);  
		String objDetector = prop.getProperty(keys+"_DETECT_BY");
		return objDetector;
	}


	public WebElement detectObject(String name) throws IOException, ParseException {
		switch (getValueFromJson(name, "detectBy")) {
		case "css":
			ele = driver.findElement(By.cssSelector(getValueFromJson(name, "locator")));
			break;
		case "xpath":
			ele = driver.findElement(By.xpath(getValueFromJson(name, "locator")));
			break;
		case "id":
			ele = driver.findElement(By.id(getValueFromJson(name, "locator")));	
			break;
		case "name":
			ele = driver.findElement(By.name(getValueFromJson(name, "locator")));
			break;
		default:
			System.out.println("ATTENTION! Object not found, please check your Object Repository !");
			break;
		}
		return ele;	
	}

	public MobileElement detectMobileObject(String name) throws IOException, ParseException {
		switch (getValueFromJson(name, "detectBy")) {
		case "id":
			mobileElement = (MobileElement) appiumDriver.findElement(By.id(getValueFromJson(name, "locator")));
			break;
		case "accessibility id":
			mobileElement = (MobileElement) appiumDriver.findElement(MobileBy.AccessibilityId(getValueFromJson(name, "locator")));
			break;
		case "name":
			mobileElement = (MobileElement) appiumDriver.findElement(By.name(getValueFromJson(name, "locator")));	
			break;
		case "xpath":
			mobileElement = (MobileElement) appiumDriver.findElement(By.xpath(getValueFromJson(name, "locator")));
			break;
		default:
			System.out.println("ATTENTION! Object not found, please check your Object Repository !");
			break;
		}
		return mobileElement;	
	}

	public String getValueFromJson(String name, String keys) throws FileNotFoundException, IOException, ParseException {	
		files = (List<File>) FileUtils.listFiles(dir, ext, true);
		for (int i=0; i < files.size(); i++) {	
			jsonObject = (JSONObject) jsonParser.parse(new FileReader(files.get(i)));
			dataObject = JsonPath.parse(jsonObject.toString()).read("$.object[?(@.name == \""+name+"\")]."+keys);
			result = dataObject.toString();	
			if (result.contains("\"")||result.contains("\\/")) { 
				result = result.replace("\\/", "/");
				result = result.substring(2, result.length()-2);
				break;	
			}
		}
		System.out.println("=============");
		return result;
		
	}

	public static void waitFor(long timeout) {
		long multipliedTimedOut = timeout * 1000;
		try {
			Thread.sleep(multipliedTimedOut);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getHeight(WebDriver driver) {
		Dimension initial_size = driver.manage().window().getSize();
		int height = initial_size.getHeight();
		return height;
	}

	public int getWidth(WebDriver driver) {
		Dimension initial_size = driver.manage().window().getSize();
		int width = initial_size.getWidth();
		return width;
	}

	public int getTopPosotion(WebDriver driver) {
		WebElement htmlElement = driver.findElement(By.tagName("html"));
		Point viewPortLocation = ((Locatable) htmlElement).getCoordinates().onScreen();
		int y = viewPortLocation.getY();
		return y;
	}

	public int getLeftPosotion(WebDriver driver) {
		WebElement htmlElement = driver.findElement(By.tagName("html"));
		Point viewPortLocation = ((Locatable) htmlElement).getCoordinates().onScreen();
		int x = viewPortLocation.getX();
		return x;
	}

	public String getUrlFromCurrentWindow(WebDriver driver) {
		String url = driver.getCurrentUrl();
		return url;
	}

	public int getIndexWindow(WebDriver driver) {
		int index = 0;	
		Set<String> handles = driver.getWindowHandles();
		List<String> windowStrings = new ArrayList<>(handles);
		for (int i = 0; i<windowStrings.size(); i++) {
			if(windowStrings.get(i) == driver.getWindowHandle()) {
				index = i;
				break;
			}
		}
		return index;
	}

	public String getTitleWindow(WebDriver driver) {
		String title = driver.getTitle();
		return title;
	}

	public boolean isAlertPresent(WebDriver driver){
		try{
			driver.switchTo().alert();
			return true;
		}catch (NoAlertPresentException e){
			return false;
		}
	}

	public  void verifyLink(String urlLink) {
		try {
			URL link = new URL(urlLink);
			HttpURLConnection httpConn =(HttpURLConnection)link.openConnection();
			httpConn.setConnectTimeout(2000);
			httpConn.connect();
			if(httpConn.getResponseCode()== 200) {  
				System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			}
			if(httpConn.getResponseCode()== 404) {
				System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			}
			if(httpConn.getResponseCode()== 400) { 
				System.out.println(urlLink+" - "+httpConn.getResponseMessage()); 
			}

			if(httpConn.getResponseCode()== 500) {
				System.out.println(urlLink+" - "+httpConn.getResponseMessage()); 
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public boolean isElementChecked(WebElement element) {
		Boolean result = false;
		if (element.isSelected() == true) {
			result = true;
		}
		return result;
	}

	public static boolean isClickable(WebElement element, WebDriver driver) 
	{
		try{
			WebDriverWait wait = new WebDriverWait(driver, 6);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	public boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				result = true;
			}
		} catch (Exception e) {}

		return result;
	}

	public static Boolean isVisibleInViewport(WebElement element) {
		WebDriver driver = ((RemoteWebElement)element).getWrappedDriver();
		return (Boolean)((JavascriptExecutor)driver).executeScript(
				"var elem = arguments[0],                 " +
						"  box = elem.getBoundingClientRect(),    " +
						"  cx = box.left + box.width / 2,         " +
						"  cy = box.top + box.height / 2,         " +
						"  e = document.elementFromPoint(cx, cy); " +
						"for (; e; e = e.parentElement) {         " +
						"  if (e === elem)                        " +
						"    return true;                         " +
						"}                                        " +
						"return false;                            "
						, element);
	}

	public boolean isElementPresent(WebElement element) {
		boolean condition = false;
		for (int i = 0; i < 150; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean isElementPresent(WebElement element, int timeout) {
		boolean condition = false;
		for (int i = 0; i < timeout; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean isElementPresent(MobileElement element) {
		boolean condition = false;
		for (int i = 0; i < 150; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean isElementPresent(MobileElement element, int timeout) {
		boolean condition = false;
		for (int i = 0; i < timeout; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean checkElement(WebElement element) {
		if (!isNoElementAvailable() || element.isDisplayed()){
			try {
				if(isNoElementAvailable()){
					waitFor(2);
				} else if(!isNoElementAvailable() || element.isDisplayed()){
					waitFor(2);
					return true;
				}
			}
			catch (Exception e){
			}
		}
		return false;
	}

	public boolean isNoElementAvailable() {
		return NoSuchElementException.class.desiredAssertionStatus();
	}

	public boolean isOptionLabelPresent(WebElement element, String value) {
		Boolean found = false;
		Select select = new Select(element);
		List<WebElement> allOptions = select.getOptions();
		for(int i=0; i<allOptions.size(); i++) {
			if(allOptions.get(i).getText().equalsIgnoreCase(value)) {
				found=true;
				break;
			}
		}
		return found;
	}

	public boolean isOptionValuePresent(WebElement element, String value) {
		Boolean found = false;
		Select select = new Select(element);
		List<WebElement> allOptions = select.getOptions();
		for(int i=0; i<allOptions.size(); i++) {
			if(allOptions.get(i).getAttribute("value").equalsIgnoreCase(value)) {
				found=true;
				break;
			}
		}
		return found;
	}

	public boolean isTextPresent(WebDriver driver, String text) {
		Boolean result = false;
		if (driver.getPageSource().contains(text)) {
			result = true;
			return result;
		}else {
			return result;
		}
	}

	public boolean isTextPresent(MobileDriver<WebElement> driver, String text) {
		Boolean result = false;
		if (driver.getPageSource().contains(text)) {
			result = true;
			return result;
		}else {
			return result;
		}
	}

	public void waitUntilExpectedCondition(WebDriver driver, ExpectedCondition<Boolean> expectation) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(expectation);
	}

	public boolean watiForElementAttributeValue(WebDriver driver, WebElement element, String attribute, String value) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBe(element, attribute, value));
		return result;
	}

	public boolean waitForElementNotHasAttribute(WebDriver driver, WebElement element, String attribute) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
		return result;
	}public void waitUntilExpectedCondition(AppiumDriver<?> driver, ExpectedCondition<Boolean> expectation) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(expectation);
	}

	public boolean watiForElementAttributeValue(AppiumDriver<?> driver, MobileElement element, String attribute, String value) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBe(element, attribute, value));
		return result;
	}

	public boolean waitForElementNotHasAttribute(AppiumDriver<?> driver, MobileElement element, String attribute) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
		return result;
	}



	public WebElement waitForElement(WebDriver driver, String key) {
		WebElement element = null;
		try {
			element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id(getValueFromJson(key, "locator"))));	
		} catch (Exception e) {
			try {
				element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(getValueFromJson(key, "locator"))));
			} catch (Exception e2) {
				try {
					element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.name(getValueFromJson(key, "locator"))));
				} catch (Exception e3) {
					try {
						element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = '"+getValueFromJson(key, "locator")+"']")));
					} catch (Exception e4) {
						try {
							element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getValueFromJson(key, "locator"))));
						} catch (IOException | ParseException e5) {		
							e5.printStackTrace();
						}
					}
				}
			}

		}	
		return element;
	}

	public MobileElement waitForMobileElement(AppiumDriver<?> driver, String key) {
		MobileElement element = null;
		try {
			element = (MobileElement) (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id(getValueFromJson(key, "locator"))));	
		} catch (Exception e) {
			try {
				element = (MobileElement) (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(getValueFromJson(key, "locator"))));
			} catch (Exception e2) {
				try {
					element = (MobileElement) (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.name(getValueFromJson(key, "locator"))));
				} catch (Exception e3) {
					try {
						element = (MobileElement) (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = '"+getValueFromJson(key, "locator")+"']")));
					} catch (Exception e4) {
						try {
							element = (MobileElement) (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getValueFromJson(key, "locator"))));
						} catch (IOException | ParseException e5) {		
							e5.printStackTrace();
						}
					}
				}
			}

		}	
		return element;
	}


	//=================================================================================================================//
	public void runAppiumServer() {
		if (driver == null) {
			if (SystemUtils.IS_OS_WINDOWS) {
				// TODO : WINDOWS
				//System.setProperty(AppiumServiceBuilder., "C:\\Program Files\\nodejs\\node.exe");
				System.setProperty(AppiumServiceBuilder.APPIUM_PATH, System.getenv("USER_HOME") + "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js");
			} else {
				// TODO : MAC
				//System.setProperty(AppiumServiceBuilder.NODE_PATH, "/usr/local/bin/node.sh");
				System.setProperty(AppiumServiceBuilder.APPIUM_PATH,"/usr/local/lib/node_modules/appium/build/lib/main.js");
			}
		}
		service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingAnyFreePort().withIPAddress("127.0.0.1"));
		service.start();
	}
	public URL getServiceURL() {
		return service.getUrl();
	}

	public void installApkInEmulator(String apkName, String deviceName) {
		runAppiumServer();
		if (SystemUtils.IS_OS_WINDOWS) {
			desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("platformName", "Android");
			desiredCapabilities.setCapability("app", System.getProperty("user.dir")+"\\APK\\"+apkName);
			desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180000);
			desiredCapabilities.setCapability("automationName", "uiautomator2");	
			appiumDriver = new AndroidDriver<>(getServiceURL(), desiredCapabilities);
		} else {
			desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("platformName", "Android");
			desiredCapabilities.setCapability("app", System.getProperty("user.dir")+"/APK/"+apkName);
			desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180000);
			desiredCapabilities.setCapability("automationName", "uiautomator2");
			desiredCapabilities.setCapability("appPackage", "io.flutter.demo.gallery");
			desiredCapabilities.setCapability("appActivity",  "io.flutter.demo.gallery.MainActivity");
			appiumDriver = new AndroidDriver<>(getServiceURL(), desiredCapabilities);
		}
	}


	public void quitDriver() {
		appiumDriver.quit();
	}

	public String getTextFromElement(WebElement element){
		String text = element.getText();
		return text;
	}
}