package com.innovent.erp.model;

public class RequestedParams{
	private String S;
	private String userId;
	private String type;
	private String key;

	public void setS(String S){
		this.S = S;
	}

	public String getS(){
		return S;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	@Override
 	public String toString(){
		return 
			"RequestedParams{" + 
			"s = '" + S + '\'' + 
			",user_id = '" + userId + '\'' + 
			",type = '" + type + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}
