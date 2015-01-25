package net.csforge.tools.mobile.push.gcm;


public class Payload {
	private String[] registration_ids;
	private Object data;
	private String collapse_key;
	private long time_to_live;
	private boolean delay_while_idle;
	private boolean dry_run;
	
	public Payload() {
		super();
	}
	
	public Payload(Object data) {
		super();
		this.data = data;
	}
	
	public Payload(String registration_id, Object data) {
		super();
		this.data = data;
	}

	public Payload(String[] registration_ids, Object data) {
		super();
		this.registration_ids = registration_ids;
		this.data = data;
	}

	public String[] getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(String[] registration_ids) {
		this.registration_ids = registration_ids;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCollapse_key() {
		return collapse_key;
	}

	public void setCollapse_key(String collapse_key) {
		this.collapse_key = collapse_key;
	}

	public long getTime_to_live() {
		return time_to_live;
	}

	public void setTime_to_live(long time_to_live) {
		this.time_to_live = time_to_live;
	}

	public boolean isDelay_while_idle() {
		return delay_while_idle;
	}

	public void setDelay_while_idle(boolean delay_while_idle) {
		this.delay_while_idle = delay_while_idle;
	}

	public boolean isDry_run() {
		return dry_run;
	}

	public void setDry_run(boolean dry_run) {
		this.dry_run = dry_run;
	}
	
}
