package unige.abstraction.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MultipleAgentAction extends JsonObject implements Comparable<MultipleAgentAction>{

	@SerializedName("agent")
	@Expose
	private String agent;
	@SerializedName("actions")
	@Expose
	private List<String> actions = new ArrayList<>();

	public MultipleAgentAction() {
	}

	public MultipleAgentAction(String agent, List<String> actions) {
		this.agent = agent;
		this.actions = actions;
	}
	
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	@Override
	public int compareTo(MultipleAgentAction anotherAgentAction) {
		return agent.compareTo(anotherAgentAction.agent);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MultipleAgentAction)) {
			return false;
		}
		return agent.equals(((MultipleAgentAction)obj).getAgent()) && CollectionUtils.isEqualCollection(actions, ((MultipleAgentAction)obj).getActions());
	}

	public List<AgentAction> toAgentActions() {
		List<AgentAction> result = new ArrayList<>();
		for(String action : actions) {
			result.add(new AgentAction(agent, action));
		}
		return result;
	}
}

