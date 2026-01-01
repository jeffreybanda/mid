package com.mid.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mid.app.data.model.Data;

public class MotorPolicyDTO {
	
	@JsonProperty("data")
    private Data data;

	public MotorPolicyDTO(Data data) {
		
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	
	
	

}
