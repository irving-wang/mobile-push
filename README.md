#Mobile Push Notification#

- iOS
- Android

##Usage##

- config.properties:
	
	apns.certificate.path=<keyfile in classpath>
	apns.certificate.password=
	apns.push.enable=<true/false is push enable>
	gcm.apikey.path=<keyfile in classpath>
	gcm.push.enable=<true/false is push enable>

- MessageService: MessageService.getService()

- push() within message and token (APNS or Android registerid)

	
##Examples##

- Android:

		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.ANDROID, 
						new Message(modelId, message), token);
			}
		}).start();

- iOS:
		
		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.APPLE, 
						new Message(modelId, message), token);
			}
		}).start();
		
