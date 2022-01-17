package extentReport;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
	public String getReportConfigPath() throws IOException{
		FileReader read = new FileReader(System.getProperty("user.dir")+"/src/test/java/extentReport/Configuration.properties");
		Properties prop = new Properties();  
	    prop.load(read);
		String reportConfigPath = prop.getProperty("reportConfigPath");
		if(reportConfigPath!= null) return reportConfigPath;
		else throw new RuntimeException("Report Config Path not specified in the Configuration.properties file for the Key:reportConfigPath");		
	}
}
