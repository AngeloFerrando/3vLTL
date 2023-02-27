package unige.abstraction.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transition extends JsonObject {

	public Transition() {
	}

	public Transition(String fromState, String toState, List<List<AgentAction>> agentActions) {
		this.fromState = fromState;
		this.toState = toState;
		this.agentActions = agentActions;
	}

	@SerializedName("fromState")
	@Expose
	private String fromState;
	
	@SerializedName("toState")
	@Expose
	private String toState;
	
	@SerializedName("agentActions")
	@Expose
	private List<List<AgentAction>> agentActions = new ArrayList<>();

	@SerializedName("multipleAgentActions")
	@Expose
	private List<MultipleAgentAction> multipleAgentActions = new ArrayList<>();

	@SerializedName("defaultTransition")
	@Expose
	private boolean defaultTransition;

	public String getFromState() {
		return fromState;
	}

	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	
	public String getToState() {
		return toState;
	}

	public void setToState(String toState) {
		this.toState = toState;
	}

	public List<List<AgentAction>> getAgentActions() {
		return agentActions;
	}

	public void setAgentActions(List<List<AgentAction>> agentActions) {
		for (List<AgentAction> agentActionList : agentActions) {
			Collections.sort(agentActionList);
		}
		
		this.agentActions = agentActions;
	}

	public boolean isDefaultTransition() {
		return defaultTransition;
	}

	public void setDefaultTransition(boolean defaultTransition) {
		this.defaultTransition = defaultTransition;
	}

	public List<MultipleAgentAction> getMultipleAgentActions() {
		return multipleAgentActions;
	}

	public void setMultipleAgentActions(List<MultipleAgentAction> multipleAgentActions) {
		this.multipleAgentActions = multipleAgentActions;
	}
}
