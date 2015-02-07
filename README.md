#Mobile Push Notification#

- **iOS** 

	Apple Push Notification Service

- **Android**
 
	Google Cloud Messaging

##Usage##

- ***config.properties*** file in classpath:
	
		apns.certificate.path=<keyfile in classpath>
		apns.certificate.password=
		apns.push.enable=<true/false is iOS push enable>
		gcm.apikey.path=<keyfile in classpath>
		gcm.push.enable=<true/false is Android push enable>

- MessageService: ***MessageService.getService()***

- ***push()*** within message and token (APNS token/Android registerid)

	
##Examples##

- see ***AppTest.java***

- iOS:
		
		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.IOS, 
						new Message("custom_id", "push message!"), "64-length token");
			}
		}).start();

- Android:

		new Thread(new Runnable(){
			public void run() {
				MessageService.getService().push(MessageService.ANDROID, 
						new Message("custom_id", "push message!"), "162-length registration_id");
			}
		}).start();

		
