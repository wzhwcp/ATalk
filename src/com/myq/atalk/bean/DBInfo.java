package com.myq.atalk.bean;

public class DBInfo {

	private String fromwho;		//发送人
	private String towho;		//接收人	
	private String content;		//消息内容
	private long time;			//发送时间
	
	public DBInfo(){
		
	}
	
	public DBInfo(String fromwho,String towho,String content,long time){
		this.fromwho=fromwho;
		this.towho=towho;
		this.content=content;
		this.time=time;
	}
	public String getFromwho() {
		return fromwho;
	}
	public void setFromwho(String fromwho) {
		this.fromwho = fromwho;
	}
	public String getTowho() {
		return towho;
	}
	public void setTowho(String towho) {
		this.towho = towho;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "ChatDBInfo [fromwho=" + fromwho + ", towho=" + towho
				+ ", content=" + content + ", time=" + time + "]";
	}
	
	
}
