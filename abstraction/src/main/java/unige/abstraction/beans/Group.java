package unige.abstraction.beans;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group extends JsonObject {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("agents")
	@Expose
	private List<String> agents = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAgents() {
		return agents;
	}

	public void setAgents(List<String> agents) {
		this.agents = agents;
	}

}
