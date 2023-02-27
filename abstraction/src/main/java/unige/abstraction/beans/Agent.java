package unige.abstraction.beans;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agent extends JsonObject {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("actions")
	@Expose
	private List<String> actions = new ArrayList<>();
	@SerializedName("indistinguishableStates")
	@Expose
	private List<List<String>> indistinguishableStates = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public List<List<String>> getIndistinguishableStates() {
		return indistinguishableStates;
	}

	public void setIndistinguishableStates(List<List<String>> indistinguishableStates) {
		this.indistinguishableStates = indistinguishableStates;
	}

}
