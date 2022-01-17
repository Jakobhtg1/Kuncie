package stepdef;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

public class StepDefinitionMobile extends SupportMethod{

	MobileElement element;
	String responseBody;
	@SuppressWarnings("rawtypes")
	static TouchAction action;
	MultiTouchAction multiAction;
	ScreenOrientation orientation;
	String manufacturer;
	String osVersion;
	Point location;
	MultiTouchAction multiTouchAction;
	Boolean result;


	@Given(".*?start application \"(.*?)\" in.*?\"(.*?)\"$")
	public void startApplication(String apkName, String deviceName) {
		installApkInEmulator(apkName, deviceName);
	}	

	@SuppressWarnings({ "rawtypes", "static-access" })
	@And(".*?swipe mobile from (\\d+) (\\d+) to (\\d+) (\\d+)$")
	public void swipeMobile(int startX, int startY, int endX, int endY) {
		action = new TouchAction<>(appiumDriver);
		action.longPress(new PointOption().point(startX, startY)).moveTo(new PointOption().point(endX, endY)).release().perform();
	}	

	@And(".*?tap \"(.*?)\"$")
	public void tap(String key) throws IOException, ParseException {
		element = detectMobileObject(key);
		element.click();
	}
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	@And(".*?tap at position (\\d+) and (\\d+)$")
	public void tapAtPosition(int x, int y) {
		action = new TouchAction(appiumDriver);
		action.press(new PointOption().point(x, y)).release().perform();
	}

	@And(".*?verify mobile element \"(.*?)\" exist$")
	public void verifyMobileElementExist(String key) throws IOException, ParseException {
		element = detectMobileObject(key);
		assertTrue("Element is not exist !", element.isDisplayed());
	}

	@And(".*?wait for.*?(\\d+).*?seconds$")
	public void waitInSecond(long seconds) {
		waitFor(seconds);
	}
	
}
