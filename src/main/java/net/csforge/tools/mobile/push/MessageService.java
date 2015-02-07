package net.csforge.tools.mobile.push;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.csforge.tools.mobile.push.gcm.Payload;
import net.csforge.tools.mobile.push.gcm.PushUtil;
import net.csforge.tools.mobile.push.gcm.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to._2v.tools.Config;
import to._2v.tools.util.StringUtil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

public class MessageService {
	private static Logger logger = LoggerFactory.getLogger(MessageService.class);
	public static int IOS = 1;
	public static int ANDROID = 2;
	public static String APNS_CERTIFICATE_PATH = "apns.certificate.path";
	public static String APNS_CERTIFICATE_PASSWORD = "apns.certificate.password";
	public static String GCM_APIKEY_PATH = "gcm.apikey.path";
	public static String APNS_PUSH_ENABLE = "apns.push.enable";
	public static String GCM_PUSH_ENABLE = "gcm.push.enable";
	private static MessageService service;
	private boolean enabledAPNS;
	private boolean enabledGCM;
	private boolean isSandbox;
	static{
		PushUtil.resetKey(Config.getProperty(GCM_APIKEY_PATH));
	}
	
	private MessageService(){
		super();
		enabledAPNS = Config.getBooleanProperty(APNS_PUSH_ENABLE);
		enabledGCM = Config.getBooleanProperty(GCM_PUSH_ENABLE);
	}
	
	public static MessageService getService(){
		if (service == null) {
			synchronized (MessageService.class) {
				if (service == null)
					service = new MessageService();
				if (logger.isDebugEnabled())
					logger.debug("MessageService instance: " + service);
			}
		}
		return service;
	}
	
	public void push(int mobile, boolean sandbox, String message, String modelId, String... tokens) {
		this.isSandbox = sandbox;
		push(mobile, message, modelId, tokens);
		this.isSandbox = false;
	}
	
	public void push(int mobile, String message, String modelId, String... tokens) {
		push(mobile, new Message(modelId, message), tokens);
	}
	
	public void push(int mobile, boolean sandbox, String alert, Object data, String... tokens) {
		this.isSandbox = sandbox;
		push(mobile, alert, data, tokens);
		this.isSandbox = false;
	}
	
	public void push(int mobile, String message, Object data, String... tokens) {
		if (mobile == IOS) {
			if (enabledAPNS) {
//				sendAPNS(new Message(message, 1, "default", data), tokens);
				sendAPNS(new Message(message, 1, "default", null), data, tokens);
			}
		} else {
			if (enabledGCM) {
//				sendGCM(new Message(message, data), tokens);
				sendGCM(message, new Payload(tokens, data));
			}
		}
	}
	
	public void push(int mobile, Message message, String... tokens) {
		if (mobile == IOS) {
			if (enabledAPNS){
				sendAPNS(message, tokens);
			}
		} else {
			if (enabledGCM)
				sendGCM(message, tokens);
		}
	}
	
	public void push(int mobile, boolean sandbox, Message message, String... tokens) {
		this.isSandbox = sandbox;
		push(mobile, message, tokens);
		this.isSandbox = false;
	}
	
	public int sendGCM(String message, String... reg_ids) {
		return sendGCM(new Message(message), reg_ids);
	}
	
	public int sendGCM(Message message, String... reg_ids){
		return sendGCM(new Payload(reg_ids, message));
	}

	public int sendGCM(Payload payload) {
		return sendGCM(null, payload);
	}
	/**
	 * 
	 * @param payload must has both registration_id(s) and data at least.
	 * @return
	 */
	public int sendGCM(String message, Payload payload){
		if (!enabledGCM)
			return 0;
		ObjectMapper mapper = new ObjectMapper();
//		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		try {
			String json = mapper.writeValueAsString(payload);
			if (message != null) {
				JsonNode root = mapper.readTree(json);
				((ObjectNode) root).with("data").put("message", message);
				json = mapper.writeValueAsString(root);
			}
			logger.info(json);
			
			Response response = mapper.readValue(PushUtil.post(json), Response.class);
			printGCMResponse(response);
			
			return response.getSuccess();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void sendAPNS(String message, String... tokens) {
		sendAPNS(message, 1, "default", null, tokens);
	}
	
	public void sendAPNS(Message message, Object data, String... tokens){
		ObjectMapper mapper = new ObjectMapper();
//		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		HashMap<String,Object> map = null;
		try {
			String json = mapper.writeValueAsString(data);
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
	        map = mapper.readValue(json, typeRef);
			if (message.getId() != null)
				map.put("id", message.getId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (message.getBadge() <= 0)
			message.setBadge(1);
		if (message.getSound() == null)
			message.setSound("default");
		sendAPNS(message.getMessage(), message.getBadge(), message.getSound(), map, tokens);
	}
	
	public void sendAPNS(Message message, String... tokens) {
		if (message.getBadge() <= 0)
			message.setBadge(1);
		if (message.getSound() == null)
			message.setSound("default");
		sendAPNS(message.getMessage(), message.getBadge(), message.getSound(), putCustomFields(message), tokens);
	}

	public void sendAPNS(String alert, int badge, String sound, Map<String,Object> customFields, String... tokens) {
		if (!enabledAPNS)
			return;
		if (tokens == null || tokens.length <= 0)
			return;
		
		PayloadBuilder builder = APNS.newPayload().alertBody(alert).sound(sound).badge(badge);
		if (customFields != null)
			builder.customFields(customFields);
		String payload = builder.build();
		logger.info(payload);
		getApnsService().push(Arrays.asList(tokens), payload);
	}
	
	Map<String,Object> putCustomFields(Message message){
		Map<String,Object> customFields = null;
		if (message.getId() != null){
			customFields = new HashMap<String,Object>();
			customFields.put("id", message.getId());
			if (message.getExtras() != null) {
				customFields.put("extras", message.getExtras());
			}
		}
		return customFields;
	}
	
	public boolean isApplePushEnabled() {
		return enabledAPNS;
	}

	public boolean isAndroidPushEnabled() {
		return enabledGCM;
	}

	public boolean isSandbox() {
		return isSandbox;
	}

	public void setSandbox(boolean isSandbox) {
		this.isSandbox = isSandbox;
	}

	void printGCMResponse(Response response) {
		if (logger.isDebugEnabled()) {
			logger.debug("response ==> {}", StringUtil.toString(response));
			if (response.getResults() != null)
				for (Object result : response.getResults())
					logger.debug("  result ==> {}", StringUtil.toString(result));
		}
	}

	private static ApnsService apnsService;
	private static ApnsService apnsSandboxService;
	ApnsService getApnsService(){
		if(isSandbox){
			if(apnsSandboxService==null){
				apnsSandboxService = APNS.newService()
				    .withCert(Config.getProperty(APNS_CERTIFICATE_PATH), 
				    		Config.getProperty(APNS_CERTIFICATE_PASSWORD))
				    .withSandboxDestination()
				    .build();
			}
			return apnsSandboxService;
		}
		if(apnsService==null){
			apnsService = APNS.newService()
			    .withCert(Config.getProperty(APNS_CERTIFICATE_PATH), 
			    		Config.getProperty(APNS_CERTIFICATE_PASSWORD))
			    .withProductionDestination()
			    .build();
		}
		return apnsService;
	}

}
