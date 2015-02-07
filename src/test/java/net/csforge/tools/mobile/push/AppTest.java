package net.csforge.tools.mobile.push;

import net.csforge.tools.mobile.push.Message;
import net.csforge.tools.mobile.push.MessageService;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest {
	
	@Test
	public void pushMessage() {
		sendGCMPushMessage("1", "push Test!", "162-length registration_id");
		sendAPNSPushMessage("1", "push Test!", "64-length token");
	}
	
	public void sendGCMPushMessage(final String modelId, final String message, final String token) {
		if (!MessageService.getService().isAndroidPushEnabled())
			return;
		if (token == null || token.length() < 162)
			return;
		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.ANDROID, 
						new Message(modelId, message), token);
			}
		}).start();
	}
	
	public void sendAPNSPushMessage(final String modelId, final String message, final String token) {
		if (!MessageService.getService().isApplePushEnabled())
			return;
		if (token == null || token.length() < 64)
			return;
		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.IOS, 
						new Message(modelId, message), token);
			}
		}).start();
	}

    /**
     * Rigourous Test :-)
     */
//    public void testApp()
//    {
//        assertTrue( true );
//    }
}
