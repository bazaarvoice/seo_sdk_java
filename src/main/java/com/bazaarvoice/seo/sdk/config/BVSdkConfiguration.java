package com.bazaarvoice.seo.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;

/**
 * Default implementation of configuration settings.
 * This loads the Bazaarvoice specific configuration
 * and also user specific setting.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkConfiguration implements BVConfiguration {
	
	private static Logger _logger = LoggerFactory.getLogger(BVSdkConfiguration.class);
	private static Map<String, String> _baseConfiguration;
	
	private Map<String, String> _instanceConfiguration;
	
	/**
	 * Static initializers to load the configuration properties when class loads.
	 */
	static {
		InputStream inputStream = BVSdkConfiguration.class.getClassLoader().getResourceAsStream("bvconfig.properties");
		
		Properties bvConfigProperties = null;
		try {
			bvConfigProperties = new Properties();
			bvConfigProperties.load(inputStream);
			
			_baseConfiguration = new HashMap<String, String>();
			
			for (String key: bvConfigProperties.stringPropertyNames()) {
				_baseConfiguration.put(key, bvConfigProperties.getProperty(key));
            }
		} catch (IOException e) {
			_logger.error(BVMessageUtil.getMessage("ERR0004"), e);
			throw new BVSdkException("ERR0004", e);
		}
		
		_logger.info(BVMessageUtil.getMessage("MSG0000"));
		
		 // Optionally merge in client defined properties if present
		loadBvClient();
	}

	/**
	 * Default constructor.
	 * If configuration should be overwritten or if you need to 
	 * load properties/configuration every time please use the add method.
	 */
	public BVSdkConfiguration() {
		_instanceConfiguration = new HashMap<String, String>(_baseConfiguration);
	}
	
	public BVConfiguration addProperty(BVClientConfig bvConfig, String propertyValue) {
		if (StringUtils.isBlank(propertyValue)) {
			throw new BVSdkException("ERR0006");
		}
		
		this._instanceConfiguration.put(bvConfig.getPropertyName(), propertyValue);
		return this;
	}
	
	public String getProperty(String propertyName) {
		return this._instanceConfiguration.get(propertyName);
	}
	
	private static void loadBvClient() {
		File bvClientFile = new File("bvclient.properties");
		if (!bvClientFile.exists() || !bvClientFile.isFile()) {
			_logger.info(BVMessageUtil.getMessage("MSG0002"));
			return;
		}
		
		_logger.info(BVMessageUtil.getMessage("MSG0001"));
        try {
        	FileInputStream inputStream = new FileInputStream(bvClientFile);
        	Properties bvConfigProperties = new Properties();
        	bvConfigProperties.load(inputStream);
        	
        	for (String key: bvConfigProperties.stringPropertyNames()) {
                _baseConfiguration.put(key, bvConfigProperties.getProperty(key));
            }
        } catch (IOException e) {
        	_logger.error(BVMessageUtil.getMessage("ERR0005"), e);
			throw new BVSdkException("ERR0005", e);
        }
	}
	
}
