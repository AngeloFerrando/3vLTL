package unige.abstraction.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgentAction extends JsonObject implements Comparable<AgentAction>{

	@SerializedName("agent")
	@Expose
	private String agent;
	@SerializedName("action")
	@Expose
	private String action;

	public AgentAction() {
	}
	
	public AgentAction(String agent, String action) {
		this.agent = agent;
		this.action = action;
	}
	
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int compareTo(AgentAction anotherAgentAction) {
		return agent.compareTo(anotherAgentAction.agent);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AgentAction)) {
			return false;
		}
		return agent.equals(((AgentAction)obj).getAgent()) && action.equals(((AgentAction)obj).getAction());
	}
}

