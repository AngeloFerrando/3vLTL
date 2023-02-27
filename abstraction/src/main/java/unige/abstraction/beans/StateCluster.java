package unige.abstraction.beans;

import java.util.*;
import java.util.stream.Collectors;

import unige.parser.ATL;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

public class StateCluster extends State {
	
	private transient List<State> childStates = new ArrayList<>();
	
	public StateCluster(State... states) {
		if (ArrayUtils.isNotEmpty(states)){
			childStates.addAll(Arrays.asList(states));
			Collections.sort(childStates);
			setLabels();
			setName();
			for (State state:states) {
				if (state.isInitial()) {
					setInitial(true);
					break;
				}
			}
		}
	}
	
	public void addChildStates(StateCluster stateCluster) {
		for (State state : stateCluster.childStates) {
			if (!childStates.contains(state)) {
				childStates.add(state);
			}
		}
		Collections.sort(childStates);
		setName();
		setLabels();
	}
	
	public void addChildState(State childState) {
		childStates.add(childState);
		Collections.sort(childStates);
		setName();
		setLabels();
	}
	
	public boolean containsChildState(State childState) {
		return childStates.contains(childState);
	}

	public void setName() {
		setName(childStates.stream().map(State::getName).collect(Collectors.joining("")));
	}

	private void setLabels() {
		List<String> posLabels = new ArrayList<>();
		List<String> negLabels = new ArrayList<>();
		if (!childStates.isEmpty()) {
			List<String> labels =
					childStates.get(0).getLabels()
					.stream().map(l -> l.substring(0, l.length()-3)).collect(Collectors.toList());

			posLabels.addAll(childStates.get(0).getLabels());
//			negLabels.addAll(childStates.get(0).getFalseLabels());
			for (int i = 1; i < childStates.size(); i++) {
				posLabels = ListUtils.intersection(posLabels, childStates.get(i).getLabels());
//				negLabels = ListUtils.intersection(negLabels, childStates.get(i).getFalseLabels());
			}
			for(String l : labels) {
				if(posLabels.contains(l + "_tt")) {
					negLabels.add(l + "_ff");
				} else if(posLabels.contains(l + "_ff")) {
					negLabels.add(l + "_tt");
				}
				else {
					posLabels.add(l + "_uu");
				}
			}
		}
		setLabels(posLabels);
		setFalseLabels(negLabels);
	}
	
//	private void setLabels() {
//		List<String> labels = new ArrayList<>();
//		List<String> allLabels = new ArrayList<>();
//		if (!childStates.isEmpty()) {
//			labels.addAll(childStates.get(0).getLabels());
//			allLabels.addAll(childStates.get(0).getLabels());
//			for (int i = 1; i < childStates.size() && !labels.isEmpty(); i++) {
//				labels = ListUtils.intersection(labels, childStates.get(i).getLabels());
//				allLabels = ListUtils.union(allLabels, childStates.get(i).getLabels());
//			}
//		}
//		List<String> res = new ArrayList<>();
//		res.addAll(labels.stream().filter(l -> l.endsWith("_tt")).distinct().collect(Collectors.toList()));
//		res.addAll(ListUtils.subtract(allLabels, labels.stream().filter(l -> l.endsWith("_tt")).map(l -> l.replace("_tt", "_ff")).collect(Collectors.toList())).stream().filter(l -> l.endsWith("_ff")).distinct().collect(Collectors.toList()));
//		setLabels(res);
//		setFalseLabels(res.stream().map(l -> l.endsWith("_tt") ? l.replace("_tt", "_ff") : l.replace("_ff", "_tt")).collect(Collectors.toList()));
//	}
	
	public State toState() {
		State state = new State(getName(), isInitial(), getLabels().toArray(new String[getLabels().size()]));
		state.setFalseLabels(getFalseLabels());
		return state;
	}
	
	public boolean containsAnyChildState(List<State> states) {
		return CollectionUtils.containsAny(childStates, states);
	}
	
	public boolean containsAnyChildState(StateCluster stateCluster) {
		return CollectionUtils.containsAny(childStates, stateCluster.childStates);
	}

//	public List<List<AgentAction>> hasMustTransition(StateCluster toStateCluster, AtlModel atlModel) {
//		List<List<AgentAction>> agentActions = new ArrayList<>();
//		for (State fromChildState : childStates) {
//			boolean toStateFound = false;
//			for (State toChildState : toStateCluster.childStates) {
//				if (atlModel.getAgentActionsByStates().containsKey(fromChildState.getName(), toChildState.getName())) {
//					agentActions.addAll(atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName()));
//					toStateFound = true;
//				}
//			}
//
//			if (!toStateFound) {
//				agentActions.clear();
//				break;
//			}
//		}
//
//		return agentActions;
//	}

	public List<List<AgentAction>> hasMustTransition(StateCluster toStateCluster, AtlModel atlModel) {
		// to be checked (here, we are assuming a simple ATL property with only one strategic operator. What to do with formulae like: <A>F<B>Xp?)
		Group group = new Group();
		for(Group g : atlModel.getGroups()) {
			if(g.getName().equals(((ATL.Existential)(atlModel.getATL())).getGroup())) {
				group = g;
				break;
			}
		}
		//
		List<String> coalition = group.getAgents();
		Map<String, List<String>> mustActions = new HashMap<>();
		for(String agent : coalition) {
			mustActions.put(agent, new ArrayList<>());
			for(String action : atlModel.getAgentMap().get(agent).getActions()) {
				boolean toStateFound = false;
				for(State fromChildState : childStates) {
					toStateFound = false;
					for(State toChildState : toStateCluster.childStates) {
						if(atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName()) == null) {
							continue;
						}
						for(List<AgentAction> agentActionList : atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName())) {
							for(AgentAction agentAction : agentActionList) {
								if(agentAction.getAgent().equals(agent) && agentAction.getAction().equals(action)) {
									toStateFound = true;
									break;
								}
							}
							if(toStateFound) {
								break;
							}
						}
						if(toStateFound) {
							break;
						}
					}
					if(!toStateFound) {
						break;
					}
				}
				if(toStateFound) {
					if(mustActions.containsKey(agent)) {
						mustActions.get(agent).add(action);
					} else {
						List<String> aux = new ArrayList<>();
						aux.add(action);
						mustActions.put(agent, aux);
					}
				}
			}
		}

		for(String agent : mustActions.keySet()) {
			if(mustActions.get(agent).isEmpty()) {
				return new ArrayList<>();
			}
		}

		List<List<AgentAction>> agentActions = new ArrayList<>();
		for (State fromChildState : childStates) {
			for (State toChildState : toStateCluster.childStates) {
				if (atlModel.getAgentActionsByStates().containsKey(fromChildState.getName(), toChildState.getName())) {
					List<List<AgentAction>> aux = new ArrayList<>();
					for(List<AgentAction> agentActionList : atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName())) {
						boolean valid = true;
						for(String agent : mustActions.keySet()) {
							Optional<AgentAction> optAct = agentActionList.stream().filter(a -> a.getAgent().equals(agent) && mustActions.get(agent).contains(a.getAction())).findAny();
							if(!optAct.isPresent()) {
								valid = false;
								break;
							}
						}
						if(valid) {
							agentActions.add(agentActionList);
						}
					}
					agentActions.addAll(atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName()));
				}
			}
		}

		return agentActions;
	}

	public List<List<AgentAction>> hasMayTransition1(StateCluster toStateCluster, AtlModel atlModel) {
		List<List<AgentAction>> agentActions = new ArrayList<>();
		for (State fromChildState : childStates) {
			for (State toChildState : toStateCluster.childStates) {
				if (atlModel.getAgentActionsByStates().containsKey(fromChildState.getName(), toChildState.getName())) {
					agentActions.addAll(atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName()));
				}
			}
		}

		return agentActions;
	}
	public List<List<AgentAction>> hasMayTransition(StateCluster toStateCluster, AtlModel atlModel, AtlModel must) {
		List<List<AgentAction>> agentActions = new ArrayList<>();
		for (State fromChildState : childStates) {
			for (State toChildState : toStateCluster.childStates) {
				if (atlModel.getAgentActionsByStates().containsKey(fromChildState.getName(), toChildState.getName())) {
					for (List<AgentAction> acts : atlModel.getAgentActionsByStates().get(fromChildState.getName(), toChildState.getName())) {
						for (AgentAction act : acts) {
							act.setAction(act.getAction() + "_" + fromChildState.getName());
							for(Agent ag : atlModel.getAgents()) {
								if(ag.getName().equals(act.getAgent())) {
									if(!ag.getActions().contains(act.getAction())) {
										ag.getActions().add(act.getAction());
									}
									break;
								}
							}
						}
						agentActions.add(acts);
					}
				}
			}
		}
		return agentActions;
	}

	public List<State> getChildStates() {
		return childStates;
	}

	@Override
	public StateCluster clone() {
		StateCluster state;
		state = (StateCluster) super.clone();
		state.childStates = new ArrayList<>();
		for(State s : this.childStates) {
			state.childStates.add(s.clone());
		}
		return state;
	}

	
}
