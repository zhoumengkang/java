package me.topit.site;

import java.io.IOException;
import java.util.Properties;

/**
 * config
 */
public class Config {
	
	private static Properties properties = new Properties();
	//配置文件名称
	private static final String configName = "/app.properties";
	private static Config instance;

	/**
	 * 构造方法 加载配置文件
	 */
	private Config() {
		try {
			properties.load(getClass().getResourceAsStream(configName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取单例对象
	 * @return EmpConfig
	 */
	private synchronized static Config getInstance() {
		if (null == instance) {
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * 将属性值获取为int型
	 * @param str 属性名
	 * @return
	 */
	public static int getInt( String str){
		try {
			if (null == instance) {
				getInstance();
			}
			return Integer.parseInt(properties.getProperty( str ));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 将属性值获取为long型
	 * @param str 属性名
	 * @return
	 */
	public static long getLong( String str){
		try {
			if (null == instance) {
				getInstance();
			}
			return Long.parseLong( properties.getProperty( str ) );
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 将属性值获取为double型
	 * @param str 属性名
	 * @return
	 */
	public static double getDouble( String str){
		try {
			if (null == instance) {
				getInstance();
			}
			return Double.parseDouble(properties.getProperty( str ));
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 将属性值获取为String型
	 * @param str 属性名
	 * @return
	 */
	public static String getString( String str){
		try {
			if (null == instance) {
				getInstance();
			}
			return properties.getProperty( str );
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 将属性值获取为boolean型
	 * @param str 属性名
	 * @return
	 */
	public static boolean getBoolean( String str){
		try {
			if (null == instance) {
				getInstance();
			}
			return Boolean.parseBoolean( properties.getProperty( str ));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

