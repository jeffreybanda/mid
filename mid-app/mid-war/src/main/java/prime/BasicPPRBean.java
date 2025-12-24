package prime;

import javax.faces.view.ViewScoped;

import javax.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class BasicPPRBean implements Serializable {
	private String value;
	public String updateValue() {
		value = String.valueOf(System.currentTimeMillis());
		return null;
		}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
		
	
}
