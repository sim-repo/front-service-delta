package com.simple.server.domain.contract;

public class IncomingBufferMsg extends AContract{
	@Override
	public String getClazz() {
		return this.getClass().getName();
	}

	@Override
	public void copyFrom(IContract _msg) throws Exception{
		if(_msg instanceof UniMsg){
			UniMsg um = (UniMsg)_msg;
			this.setMessageBodyValue(um.getBody());			
		}
		this.setEventId(_msg.getEventId());
		this.setJuuid(_msg.getJuuid());
	}
}