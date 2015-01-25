package net.csforge.tools.mobile.push.gcm;

import java.util.List;

public class Response {
	private String multicast_id;
	private int success;
	private int failure;
	private int canonical_ids;
	private List<Result> results;
	
	public static class Result{
		private String message_id;
		private String registration_id;
		private String error;
		public String getMessage_id() {
			return message_id;
		}
		public void setMessage_id(String message_id) {
			this.message_id = message_id;
		}
		public String getRegistration_id() {
			return registration_id;
		}
		public void setRegistration_id(String registration_id) {
			this.registration_id = registration_id;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
	}
	
	public String getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(String multicast_id) {
		this.multicast_id = multicast_id;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public int getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(int canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
}


