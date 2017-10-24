package dbViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static File configFile;
	private static Properties prop;
	public static void init(){
		configFile=new File("config.properties");
		try {
			FileReader reader=new FileReader(configFile);
			prop=new Properties();
			prop.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Config file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("err");
		}
		
	}
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	//Function: create - for create table
	//			getInfo - for get info about existing tables
	//			insert - for insert row into table
	public static String getURL(String function){
		String serverAddress="http://";
		serverAddress+=getProperty("serverAddress");
		serverAddress+=":";
		serverAddress+=getProperty("port");
		serverAddress+="/";
		serverAddress+=getProperty(function);
		return serverAddress;
	}

}
