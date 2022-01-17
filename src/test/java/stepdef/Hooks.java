package stepdef;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;


public class Hooks {
	public static Scenario myScenario;
	
	@Before
	public void before(Scenario sc) throws KeyManagementException, NoSuchAlgorithmException{
		System.out.println("=======================================");
		System.out.println("============ Test Started =============");
		System.out.println("=======================================");
		System.out.println("Tag         : "+getTagFromScenario(sc));
		System.out.println("Scenario    : "+sc.getName());
		System.out.println("=======================================");
	}
	
	@After
	public void after(Scenario sc) {
		if (StepDefinitionMobile.appiumDriver != null) {
			StepDefinitionMobile.appiumDriver.quit();
			System.out.println(">>>Mobile Driver quit !");		
			SupportMethod.service.stop();
			System.out.println("=== THIS IS MOBILE UI TEST ===");
		}
		else {
			System.out.println("=========== THIS IS API TEST ==========");
		}
	}
	
	public String getTagFromScenario(Scenario sc) {
		String tag = null;
		tag = sc.getSourceTagNames().toString().replaceAll("[(\\W)]", "").replaceAll("[(a-z , A-Z)]", "");
		return tag;
	}

	public String getStatusScenario(Scenario sc) {
		String status = null;
		status = sc.getStatus();
		return status;
	}
	
	
	
	
}
