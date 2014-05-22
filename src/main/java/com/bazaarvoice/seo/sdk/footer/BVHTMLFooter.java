package com.bazaarvoice.seo.sdk.footer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVConstant;

/**
 * Implementation class for adding bazaarvoice footer in the bazaarvoice seo content.
 * This class is designed to display footer in the bazaarvoice seo content in HTML formatted tags.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVHTMLFooter implements BVFooter {

	private static final String VE_LOGGER = "ve_logger";
	private static final Logger veLogger = LoggerFactory.getLogger(VE_LOGGER);
	
	private static final String LOG4J_CHUTE = "org.apache.velocity.runtime.log.Log4JLogChute";
	private static final String LOG4J_LOGGER = "runtime.log.logsystem.log4j.logger";
	private static final String FOOTER_FILE = "footer.txt"; 
	
	private BVConfiguration _bvConfiguration;
	private BVParameters _bvParameters;
	private BVSeoSdkUrl _bvSeoSdkUrl;
	
	private VelocityEngine _velocityEngine;

	private long executionTime;
	private List<String> messageList;
	
	public BVHTMLFooter(BVConfiguration bvConfiguration, BVParameters bvParameters) {
		if (bvConfiguration == null) {
			throw new BVSdkException("ERR0007");
		}
		
		_bvConfiguration = bvConfiguration;
		_bvParameters = bvParameters;
		
		_velocityEngine = new VelocityEngine();
		_velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, LOG4J_CHUTE);
		_velocityEngine.setProperty(LOG4J_LOGGER, VE_LOGGER);
		_velocityEngine.addProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		_velocityEngine.addProperty("file.resource.loader.cache", true);
		veLogger.debug("Initialized velocity engine..");
		
		messageList = new ArrayList<String>();
	}
	
	/**
	 * returns the footer based on the configuration that is set.
	 * @return String footer.
	 */
	public String displayFooter(String accessMethod) {
		Template template = _velocityEngine.getTemplate(FOOTER_FILE);
		VelocityContext context = new VelocityContext();
		
		if (_bvParameters !=null && _bvParameters.getPageURI() != null && 
				_bvParameters.getPageURI().contains(BVConstant.BVREVEAL_DEBUG)) {
			Map<String, String> revealMap = null;
			revealMap = new HashMap<String, String>();
			String configName = null;
			for (BVCoreConfig bvCoreConfig : BVCoreConfig.values()) {
				configName = bvCoreConfig.getPropertyName();
				revealMap.put(configName, _bvConfiguration.getProperty(configName));
			}
			
			for (BVClientConfig bvClientConfig : BVClientConfig.values()) {
				configName = bvClientConfig.getPropertyName();
				revealMap.put(configName, _bvConfiguration.getProperty(configName));
			}
			
			context.put("revealMap", revealMap);
		}
		
		String methodType = Boolean.parseBoolean(
				_bvConfiguration.getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName())) ? "LOCAL" : "CLOUD";
		context.put("sdk_enabled", _bvConfiguration.getProperty(BVClientConfig.SEO_SDK_ENABLED.getPropertyName()));
		context.put("_bvParameters", _bvParameters);
		context.put("methodType", methodType);
		context.put("executionTime", executionTime);
		context.put("accessMethod", accessMethod);
		context.put("isBd", _bvConfiguration.getProperty(BVClientConfig.BOT_DETECTION.getPropertyName()));
		
		String message = null;
		if (messageList != null && !messageList.isEmpty()) {
			message = "";
			for (String messageStr : messageList) {
				message += messageStr;
			}
		}
		context.put("message", message);
		
		String url = null;
		if (!Boolean.valueOf(_bvConfiguration.getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName())) && _bvSeoSdkUrl != null) {
			url = _bvSeoSdkUrl.seoContentUri().toString();
		}
		context.put("url", url);
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}

	public void addMessage(String message) {
		if (!StringUtils.isBlank(message)) {
			messageList.add(message);
		}
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public void setBvSeoSdkUrl(BVSeoSdkUrl _bvSeoSdkUrl) {
		this._bvSeoSdkUrl = _bvSeoSdkUrl;
	}
	
}
