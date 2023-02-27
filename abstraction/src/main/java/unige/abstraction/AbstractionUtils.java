package unige.abstraction;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import unige.abstraction.beans.*;
import unige.parser.ATL;
import unige.parser.Automaton;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

public class AbstractionUtils {
	
    private static final String MODEL_JSON_FILE_NAME = "modelsimple.json"; // "modelVoter.json"; //"modelKR.json"; //"modelCards.json";
	private final static Log logger = LogFactory.getLog(AbstractionUtils.class);
	private static AtlModel must;

	public static List<StateCluster> getStateClusters(AtlModel atlModel) {
		List<StateCluster> stateClusters = new ArrayList<>();
		for (State state : atlModel.getStates()) {
			stateClusters.add(state.clone().toStateCluster());
		}

		//atlModel.getATL().getGroups() // the greatest coalition

		for (Agent agent : atlModel.getAgents()) {
			for (List<String> indistinguishableStateNameList : agent.getIndistinguishableStates()) {
				for (StateCluster stateCluster : stateClusters) {
					List<State> indistinguishableStateList = 
											indistinguishableStateNameList.parallelStream()
													.map(atlModel::getState).collect(Collectors.toList());
					if (stateCluster.containsAnyChildState(indistinguishableStateList)) {
						for (State state : indistinguishableStateList) {
							if (!stateCluster.containsChildState(state)) {
								stateCluster.addChildState(state);
							}
						}
					}
				}
			}
		}
		
		for (StateCluster stateCluster1 : stateClusters) {
			for (StateCluster stateCluster2 : stateClusters) {
				if (stateCluster1.containsAnyChildState(stateCluster2)) {
					stateCluster1.addChildStates(stateCluster2);
				}
			}
		}
		
		return stateClusters.stream().distinct().collect(Collectors.toList());
	}
	
	
	
	public static List<Transition> getMayTransitions(final AtlModel atlModel, final List<StateCluster> stateClusters) {
		List<Transition> transitions = new ArrayList<>();
		for (StateCluster fromStateCluster : stateClusters) {
			for (StateCluster toStateCluster : stateClusters) {
				List<List<AgentAction>> agentActions = fromStateCluster.hasMayTransition(toStateCluster, atlModel, must);
				createTransition(fromStateCluster, transitions, toStateCluster, agentActions);
			}
		}
		
		return transitions;
	}
	
	public static List<Transition> getMustTransitions(final AtlModel atlModel, final List<StateCluster> stateClusters) {
		List<Transition> transitions = new ArrayList<>();
		State sinkState = new State();
		sinkState.setName("sink");
		Set<String> aux = new HashSet<>();
		Set<String> aux1 = new HashSet<>();
		for(String l : atlModel.getStates().get(0).getLabels()) {
			aux.add(l.replace("_tt", "_uu").replace("_ff", "_uu"));
			if(!l.contains("_uu")) aux1.add(l);
		}
		sinkState.setLabels(new ArrayList<>(aux));
		sinkState.setFalseLabels(new ArrayList<>(aux1));
		sinkState = new StateCluster(sinkState);
		boolean sinkAdded = false;
		for (StateCluster fromStateCluster : stateClusters) {
			boolean found = false;
			for (StateCluster toStateCluster : stateClusters) {
				List<List<AgentAction>> agentActions = fromStateCluster.hasMustTransition(toStateCluster, atlModel);
				if(!agentActions.isEmpty()) found = true;
				createTransition(fromStateCluster, transitions, toStateCluster, agentActions);
			}
			if(!found) {
				List<AgentAction> acts = new ArrayList<>();
				for (Agent ag : atlModel.getAgents()) {
					AgentAction act = new AgentAction();
					act.setAgent(ag.getName());
					act.setAction(ag.getActions().get(0));
					acts.add(act);
				}
				List<List<AgentAction>> actsL = new ArrayList<>();
				actsL.add(acts);
//				List<State> newStates = new ArrayList<>(atlModel.getStates());
//				newStates.add(sinkState);
//				atlModel.setStates(newStates);
//				atlModel.setStateMap(null);
				createTransition(fromStateCluster, transitions, sinkState, actsL);
				sinkAdded = true;
			}
		}
		if(sinkAdded) {
			stateClusters.add((StateCluster) sinkState);
		}
		return transitions;
	}
	
	public static void removeDuplicates(List<List<AgentAction>> agentActions) {
		Map<String, List<AgentAction>> actionMap = new HashMap<>();
		for (List<AgentAction> actionList : agentActions) {
			actionMap.put(actionList.toString(), actionList);
		}
		agentActions.clear();
		agentActions.addAll(actionMap.values());
	}
	
	public static String generateDotGraph(AtlModel atlModel) {
		StringBuilder stringBuilder = new StringBuilder("digraph G {").append(System.lineSeparator());
		List<Transition> transitions = atlModel.getTransitions();
		for (Transition transition : transitions) {
			if (CollectionUtils.isEmpty(atlModel.getState(transition.getFromState()).getLabels())) {
				stringBuilder.append(transition.getFromState());
			} else {
				stringBuilder
						.append("\"").append(transition.getFromState()).append("(").append(String.join(", ", atlModel.getState(transition.getFromState()).getLabels())).append(")\"");
			}
			stringBuilder.append("->");
			if (CollectionUtils.isEmpty(atlModel.getState(transition.getToState()).getLabels())) {
				stringBuilder.append(transition.getToState());
			} else {
				stringBuilder
						.append("\"").append(transition.getToState()).append("(").append(String.join(", ", atlModel.getState(transition.getToState()).getLabels())).append(")\"");
			}
			List<String> list1 = new ArrayList<>();
			for(List<AgentAction> agentActionList: transition.getAgentActions()) {
				List<String> list2 = new ArrayList<>();
				for (AgentAction agentAction : agentActionList) {
					list2.add(agentAction.getAgent()+ "." +agentAction.getAction());
				}
				list1.add(MessageFormat.format("({0})", String.join(",", list2)));
			}
			stringBuilder.append("[ label = \"" + String.join("\\n", list1) + "\" ];").append(System.lineSeparator());
		}
		stringBuilder.append("}").append(System.lineSeparator());
		return stringBuilder.toString();
	}
	
	public static void validateAtlModel(AtlModel atlModel, boolean explicit) throws Exception {
		validateTransitions(atlModel);
		validateGroup(atlModel);
		if(explicit) {
			Set<String> labels = new HashSet<>();
			for (State s : atlModel.getStates()) {
				labels.addAll(s.getLabels());
				labels.addAll(s.getFalseLabels());
			}
			for (State s : atlModel.getStates()) {
				List<String> sLabels = new ArrayList<>();
				for (String l : labels) {
					if (s.getLabels().contains(l)) {
						sLabels.add(l + "_tt");
					} else {
						sLabels.add(l + "_ff");
					}
				}
				s.setLabels(sLabels);
				s.setFalseLabels(sLabels.stream().map(l -> l.endsWith("_tt") ? l.replace("_tt", "_ff") : l.replace("_ff", "_tt")).collect(Collectors.toList()));
			}
		}
	}
		
	private static void validateTransitions(AtlModel atlModel) throws Exception {
		for(Transition transition : atlModel.getTransitions()) {
			if (!atlModel.getStateMap().containsKey(transition.getFromState())) {
				throw new Exception(MessageFormat.format("invalid state {0} in transition : {1} {2}", 
						transition.getFromState(), System.lineSeparator(), transition));
			}
			if (!atlModel.getStateMap().containsKey(transition.getToState())) {
				throw new Exception(MessageFormat.format("invalid state {0} in transition : {1} {2}", 
						transition.getToState(), System.lineSeparator(), transition));
			}
			if (transition.isDefaultTransition() && CollectionUtils.isNotEmpty(transition.getAgentActions())) {
				throw new Exception(MessageFormat.format("The transition cannot be a default one and have explicit agent actions : {0} {1}", 
								System.lineSeparator(), transition));
			}			
			for(List<AgentAction> agentActionList : transition.getAgentActions()) {
				validateAgentActionList(atlModel, transition, agentActionList);
			}
		}
	}
	
	private static void validateAgentActionList(AtlModel atlModel, Transition transition, List<AgentAction> agentActionList) throws Exception {
		for (AgentAction agentAction : agentActionList) {
			validateAgentAction(atlModel, transition, agentAction);
		}
		List<String> agents = agentActionList.parallelStream().map(AgentAction::getAgent).collect(Collectors.toList());
		Collection<String> agentNotDefinedList = CollectionUtils.subtract(agents, atlModel.getAgentMap().keySet());
		if (CollectionUtils.isNotEmpty(agentNotDefinedList)) {
			throw new Exception (MessageFormat.format("Some agents have not been defined : {0} for the transition : {1} {2}", 
					agentNotDefinedList, System.lineSeparator(), transition));
		}
		Collection<String> missingAgentActionsList = CollectionUtils.subtract(atlModel.getAgentMap().keySet(), agents);
		if (CollectionUtils.isNotEmpty(missingAgentActionsList)) {
			throw new Exception (MessageFormat.format("Some agent actions have not been defined : {0} for the transition : {1} {2}", 
					missingAgentActionsList, System.lineSeparator(), transition));
		}
	}
	
	private static void validateAgentAction(AtlModel atlModel, Transition transition, AgentAction agentAction) throws Exception {
		if (!atlModel.getAgentMap().containsKey(agentAction.getAgent())) {
			throw new Exception (MessageFormat.format("Invalid agent {0} in agentAction : {1} for the transition : {2} {3}", 
					agentAction.getAgent(), agentAction, System.lineSeparator(), transition));
		}
		Agent agent = atlModel.getAgentMap().get(agentAction.getAgent());
		if (!agent.getActions().contains(agentAction.getAction())) {
			throw new Exception (MessageFormat.format("Invalid action {0} in agentAction : {1} for the transition : {2} {3}", 
					agentAction.getAction(), agentAction, System.lineSeparator(), transition));
		}
	}
	
	private static void validateGroup(AtlModel atlModel) throws Exception {
		for(Group g : atlModel.getGroups()) {
			List<String> groupAgents = g.getAgents();
			Collection<String> agentNotDefinedList = CollectionUtils.subtract(groupAgents, atlModel.getAgentMap().keySet());
			if (CollectionUtils.isNotEmpty(agentNotDefinedList)) {
				throw new Exception(MessageFormat.format("Some agents in the group have not been defined : {0}",
						agentNotDefinedList, System.lineSeparator(), atlModel));
			}
		}
	}
	
	
	public static String readSampleFile() {
		try {
			File sampleFile = new ClassPathResource(MODEL_JSON_FILE_NAME).getFile();
			return new String(FileUtils.readFileToByteArray(sampleFile));
		} catch (IOException ioe) {
			logger.error("Error while trying to read the sample file.", ioe);
		}
		
		return null;
	}
	
	public static void processDefaultTransitions(AtlModel atlModel) throws Exception {
		for(Entry<String, List<Transition>> entry : atlModel.getTransitionMap().entrySet()) {
			List<Transition> transitions = entry.getValue();
			List<Transition> defaultTransitions = transitions.parallelStream().filter(transition -> transition.isDefaultTransition()).collect(Collectors.toList());
			if (CollectionUtils.isEmpty(defaultTransitions)) {
				continue;
			}
			if (defaultTransitions.size() > 1) {
				throw new Exception (MessageFormat.format("The state {0} has {1} default transition, only one is allowed. Transitions : {2} {3}", 
						entry.getKey(), defaultTransitions.size(), System.lineSeparator(), defaultTransitions));
			}
			Collection<Transition> explicitTransitions = CollectionUtils.subtract(transitions, defaultTransitions);
			List<List<List<AgentAction>>> existingActionLists = explicitTransitions.parallelStream().map(Transition::getAgentActions).collect(Collectors.toList());
			Set<List<AgentAction>> actions = defaultTransitions.get(0).getMultipleAgentActions()
												.parallelStream()
												.map(
													multipleAgentAction->multipleAgentAction.getActions()
																	.stream()
																	.map(action -> new AgentAction(multipleAgentAction.getAgent(), action))
												.collect(Collectors.toList()))
												.collect(Collectors.toSet()); 
			List<List<AgentAction>> possibleActions = Lists.cartesianProduct(actions.toArray(new ArrayList<?>[actions.size()])).parallelStream().map(list->list.stream().map(action->(AgentAction) action).collect(Collectors.toList())).collect(Collectors.toList());
			for (List<List<AgentAction>> agentActionsList : existingActionLists) {
				for (List<AgentAction> agentActionList : agentActionsList) {
					Iterator<List<AgentAction>> iterator = possibleActions.iterator();
					while (iterator.hasNext()) {
						if (CollectionUtils.isEqualCollection(iterator.next(), agentActionList)) {
							iterator.remove();
						}
					}
				}
			}
			atlModel.getTransitions().add(new Transition(entry.getKey(), defaultTransitions.get(0).getToState(), possibleActions));
			atlModel.setTransitions(Lists.newLinkedList(CollectionUtils.removeAll(atlModel.getTransitions(), defaultTransitions)));
		}
	}

	public static String generateMCMASProgram(AtlModel atlModel, boolean isMayModel) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(System.lineSeparator()).append("Agent Environment").append(System.lineSeparator());
		stringBuilder.append("\t").append("Vars :").append(System.lineSeparator());
		Set<String> alreadyAddedLabels = new HashSet<>();
		for (State state: atlModel.getStates()) {
			stringBuilder.append("\t").append("\t").append(state.getName()).append(" : boolean;").append(System.lineSeparator());
			for (String label: state.getLabels()) {
				if(label.endsWith("_tt") || label.endsWith("_ff")) {
					label = label.substring(0, label.length() - 3);
					if (!alreadyAddedLabels.contains(label)) {
						stringBuilder.append("\t").append("\t").append(label).append("_tt").append(" : boolean;").append(System.lineSeparator());
						stringBuilder.append("\t").append("\t").append(label).append("_ff").append(" : boolean;").append(System.lineSeparator());
						stringBuilder.append("\t").append("\t").append(label).append("_uu").append(" : boolean;").append(System.lineSeparator());
						alreadyAddedLabels.add(label);
					}
				} else {
					if (!alreadyAddedLabels.contains(label)) {
						stringBuilder.append("\t").append("\t").append(label).append(" : boolean;").append(System.lineSeparator());
						alreadyAddedLabels.add(label);
					}
				}
			}
			for (String label: state.getFalseLabels()) {
				if(!label.contains("atom")) {
					label = label.substring(0, label.length() - 3);
					if(label.endsWith("_tt") || label.endsWith("_ff")) {
						stringBuilder.append("\t").append("\t").append(label).append("_tt").append(" : boolean;").append(System.lineSeparator());
						stringBuilder.append("\t").append("\t").append(label).append("_ff").append(" : boolean;").append(System.lineSeparator());
						stringBuilder.append("\t").append("\t").append(label).append("_uu").append(" : boolean;").append(System.lineSeparator());
						alreadyAddedLabels.add(label);
					}
				} else {
					if (!alreadyAddedLabels.contains(label)) {
						stringBuilder.append("\t").append("\t").append(label).append("_tt").append(" : boolean;").append(System.lineSeparator());
						alreadyAddedLabels.add(label);
					}
				}
			}
		}
		stringBuilder.append("\t").append("end Vars").append(System.lineSeparator());
		stringBuilder.append("\t").append("Actions = {};").append(System.lineSeparator());
		stringBuilder.append("\t").append("Protocol :").append(System.lineSeparator());
		stringBuilder.append("\t").append("end Protocol").append(System.lineSeparator());
		stringBuilder.append("\t").append("Evolution :").append(System.lineSeparator());
		for (Transition transition: atlModel.getTransitions()) {
			State toState = atlModel.getState(transition.getToState());
			State fromState = atlModel.getState(transition.getFromState());
			stringBuilder.append("\t").append("\t");
			if (!toState.equals(fromState)) {
				stringBuilder.append(fromState.getName()).append(" = false ");
				if (CollectionUtils.isNotEmpty(fromState.getLabels())) {
					for (String label: fromState.getLabels()) {
						if(!toState.getLabels().contains(label)) {
							stringBuilder.append("and ").append(label).append(" = false ");
						}
					}
				}
				stringBuilder.append("and ");
			}

			stringBuilder.append(toState.getName()).append(" = true ");
			if (CollectionUtils.isNotEmpty(toState.getLabels())) {
				for (String label : toState.getLabels()) {
					stringBuilder.append("and ").append(label).append(" = true ");
				}
			}
			stringBuilder.append(" if ");

			stringBuilder.append(fromState.getName()).append(" = true ");
			if (CollectionUtils.isNotEmpty(fromState.getLabels())) {
				for (String label: fromState.getLabels()) {
					stringBuilder.append("and ").append(label).append(" = true ");
				}
			}

			if (!toState.equals(fromState)) {
				stringBuilder.append("and ").append(toState.getName()).append(" = false ");
				if (CollectionUtils.isNotEmpty(toState.getLabels())) {
					for (String label : toState.getLabels()) {
						if(!fromState.getLabels().contains(label)) {
							stringBuilder.append("and ").append(label).append(" = false ");
						}
					}
				}
			}

			stringBuilder.append("and ");
			if (transition.getAgentActions().size()>1)
				stringBuilder.append("(");
			for (int i = 0; i < transition.getAgentActions().size(); i++) {
				List<AgentAction> agentActionList = transition.getAgentActions().get(i);
				stringBuilder.append("(");
				for (int j = 0; j < agentActionList.size(); j++) {
					AgentAction agentAction = agentActionList.get(j);
					stringBuilder.append(agentAction.getAgent()).append(".Action").append(" = ").append(agentAction.getAction());
					if (j<agentActionList.size()-1)
						stringBuilder.append(" and ");
				}
				stringBuilder.append(")");
				if (i<transition.getAgentActions().size()-1)
					stringBuilder.append(" or ").append(System.lineSeparator()).append("\t\t\t\t\t");
			}
			if (transition.getAgentActions().size()>1)
				stringBuilder.append(")");

			if (transition.getMultipleAgentActions().size()>1)
				stringBuilder.append("(");
			for (int i = 0; i < transition.getMultipleAgentActions().size(); i++) {
				MultipleAgentAction multiAction = transition.getMultipleAgentActions().get(i);
				stringBuilder.append("(");
				for (int j = 0; j < multiAction.getActions().size(); j++) {
					String agentAction = multiAction.getActions().get(j);
					stringBuilder.append(multiAction.getAgent()).append(".Action").append(" = ").append(agentAction);
					if (j<multiAction.getActions().size()-1)
						stringBuilder.append(" or ");
				}
				stringBuilder.append(")");
				if (i<transition.getMultipleAgentActions().size()-1)
					stringBuilder.append(" and ").append(System.lineSeparator()).append("\t\t\t\t\t");
			}
			if (transition.getMultipleAgentActions().size()>1)
				stringBuilder.append(")");

			stringBuilder.append(";").append(System.lineSeparator());
		}
		stringBuilder.append("\t").append("end Evolution").append(System.lineSeparator());
		stringBuilder.append("end Agent").append(System.lineSeparator());

		for (Agent agent : atlModel.getAgents()) {
			stringBuilder.append("Agent ").append(agent.getName()).append(System.lineSeparator());
			alreadyAddedLabels.clear();
			List<String> lobsvars = new ArrayList<>();
			for (State state : atlModel.getStates()) {
				boolean consider = true;
				for(List<String> lIndS : agent.getIndistinguishableStates()){
					if(lIndS.contains(state.getName())) {
						consider = false;
						break;
					}
				}
				if(consider) {
					lobsvars.add(state.getName());
				}
				for (String label : state.getLabels()) {
					if(!alreadyAddedLabels.contains(label)) {
						lobsvars.add(label);
						alreadyAddedLabels.add(label);
					}
				}
			}
			stringBuilder.append("\t").append("Lobsvars = {").append(String.join(", ", lobsvars)).append("};").append(System.lineSeparator());
			stringBuilder.append("\t").append("Vars : ").append(System.lineSeparator());
			if(CollectionUtils.isNotEmpty(agent.getIndistinguishableStates())) {
//				boolean first = true;
				for (List<String> lIndS : agent.getIndistinguishableStates()) {
//					if (first) {
//						first = false;
//					} else {
//						stringBuilder.append(",").append(System.lineSeparator());
//					}
					stringBuilder.append("\t").append("\t").append("imp_").append(String.join("_", lIndS)).append(": boolean;");
				}
			}
			stringBuilder.append(System.lineSeparator()).append("\t").append("\t").append("play : boolean;");

			stringBuilder.append(System.lineSeparator()).append("\t").append("end Vars").append(System.lineSeparator());
			stringBuilder.append("\t").append("Actions = {").append(String.join(",", agent.getActions())).append("};");
			Map<String, List<String>> availableActionMap = getAvailableActions(atlModel, agent);
			stringBuilder.append(System.lineSeparator()).append("\t").append("Protocol : ").append(System.lineSeparator());
//            stringBuilder.append("Other : {").append(String.join(",", agent.getActions())).append("};");
			for (Entry<String, List<String>> availableActionsEntry: availableActionMap.entrySet()) {
				stringBuilder.append("\t").append("\t");
				if(!availableActionsEntry.getKey().startsWith("imp_")) {
					stringBuilder.append("Environment.");
				}
				stringBuilder.append(availableActionsEntry.getKey()).append(" = true");
				if(!availableActionsEntry.getKey().startsWith("imp_")) {
					State state = atlModel.getState(availableActionsEntry.getKey());
					if (CollectionUtils.isNotEmpty(state.getLabels())) {
						for (String label : state.getLabels())
							stringBuilder.append(" and ").append("Environment.").append(label).append(" = true");
					}
					stringBuilder.append(" : {")
							.append(String.join(",", availableActionsEntry.getValue())).append("};").append(System.lineSeparator());
				} else {
					for(String s : availableActionsEntry.getKey().substring(4).split("_")) {
						State state = atlModel.getState(s);
						if (CollectionUtils.isNotEmpty(state.getLabels())) {
							for (String label : state.getLabels())
								stringBuilder.append(" and ").append("Environment.").append(label).append(" = true");
						}
					}
					stringBuilder.append(" : {")
							.append(String.join(",", availableActionsEntry.getValue())).append("};").append(System.lineSeparator());
				}
			}
			stringBuilder.append("\t").append("end Protocol").append(System.lineSeparator());
			stringBuilder.append("\t").append("Evolution : ").append(System.lineSeparator());
			stringBuilder.append("\t").append("\t").append("play = true if play = true;").append(System.lineSeparator());
			if(CollectionUtils.isNotEmpty(agent.getIndistinguishableStates())) {
				for (Transition transition : atlModel.getTransitions()) {
					State toState = atlModel.getState(transition.getToState());
					State fromState = atlModel.getState(transition.getFromState());
					State toStateAux = toState, fromStateAux = fromState;
					for (List<String> lIndS : agent.getIndistinguishableStates()) {
						if (lIndS.contains(toState.getName())) {
							toState = new State();
							toState.setName("imp_" + String.join("_", lIndS));
							toState.setInitial(atlModel.getStates().stream().anyMatch(s -> lIndS.contains(s.getName()) && s.isInitial()));
							toState.setLabels(new ArrayList<>());
							for (List<String> labels : atlModel.getStates().stream().filter(s -> lIndS.contains(s.getName())).map(State::getLabels).collect(Collectors.toSet())) {
								toState.getLabels().addAll(labels);
							}
						}
						if (lIndS.contains(fromState.getName())) {
							fromState = new State();
							fromState.setName("imp_" + String.join("_", lIndS));
							fromState.setInitial(atlModel.getStates().stream().anyMatch(s -> lIndS.contains(s.getName()) && s.isInitial()));
							fromState.setLabels(new ArrayList<>());
							for (List<String> labels : atlModel.getStates().stream().filter(s -> lIndS.contains(s.getName())).map(State::getLabels).collect(Collectors.toSet())) {
								fromState.getLabels().addAll(labels);
							}
						}
					}
					if (toState == toStateAux) { // && fromState == fromStateAux) {
						continue;
					}
//                if(toState == toStateAux) {
//                toState.setName("Environment." + toState.getName());
//                }
					if (fromState == fromStateAux) {
						fromState.setName("Environment." + fromState.getName());
					}
					stringBuilder.append("\t").append("\t");
//                if (!fromState.equals(toState)) {
//                    stringBuilder.append("(").append(fromState.getName()).append(" = false ").append(")");
//                    if (CollectionUtils.isNotEmpty(fromState.getLabels())) {
//                        for (String label: fromState.getLabels()) {
//                            if(!toState.getLabels().contains(label)) {
//                                stringBuilder.append("and ").append("(").append(label).append(" = false ").append(")");
//                            }
//                        }
//                    }
//                    stringBuilder.append("and ");
//                }

					stringBuilder.append("(").append(toState.getName()).append(" = true ").append(")");
//                    if (CollectionUtils.isNotEmpty(toState.getLabels())) {
//                        for (String label : toState.getLabels()) {
//                            stringBuilder.append("and ").append("(").append(label).append(" = true ").append(")");
//                        }
//                    }
					stringBuilder.append(" if ");

					stringBuilder.append("(").append(fromState.getName()).append(" = true ").append(")");
					if (CollectionUtils.isNotEmpty(fromState.getLabels())) {
						for (String label : fromState.getLabels()) {
							stringBuilder.append("and ").append("(").append("Environment.").append(label).append(" = true ").append(")");
						}
					}

					if (!toState.equals(fromState)) {
						stringBuilder.append("and ").append("(").append(toState.getName()).append(" = false ").append(")");
						if (CollectionUtils.isNotEmpty(toState.getLabels())) {
							for (String label : toState.getLabels()) {
								if (!fromState.getLabels().contains(label)) {
									stringBuilder.append("and ").append("(").append("Environment.").append(label).append(" = false ").append(")");
								}
							}
						}
					}

					stringBuilder.append("and ");
					if (transition.getAgentActions().size() > 1)
						stringBuilder.append("(");
					for (int i = 0; i < transition.getAgentActions().size(); i++) {
						List<AgentAction> agentActionList = transition.getAgentActions().get(i);
						stringBuilder.append("(");
						for (int j = 0; j < agentActionList.size(); j++) {
							AgentAction agentAction = agentActionList.get(j);
							stringBuilder.append(agentAction.getAgent()).append(".Action").append(" = ").append(agentAction.getAction());
							if (j < agentActionList.size() - 1)
								stringBuilder.append(" and ");
						}
						stringBuilder.append(")");
						if (i < transition.getAgentActions().size() - 1)
							stringBuilder.append(" or ").append(System.lineSeparator()).append("\t\t\t\t\t");
					}
					if (transition.getAgentActions().size() > 1)
						stringBuilder.append(")");

					if (transition.getMultipleAgentActions().size() > 1)
						stringBuilder.append("(");
					for (int i = 0; i < transition.getMultipleAgentActions().size(); i++) {
						MultipleAgentAction multiAction = transition.getMultipleAgentActions().get(i);
						stringBuilder.append("(");
						for (int j = 0; j < multiAction.getActions().size(); j++) {
							String agentAction = multiAction.getActions().get(j);
							stringBuilder.append(multiAction.getAgent()).append(".Action").append(" = ").append(agentAction);
							if (j < multiAction.getActions().size() - 1)
								stringBuilder.append(" or ");
						}
						stringBuilder.append(")");
						if (i < transition.getMultipleAgentActions().size() - 1)
							stringBuilder.append(" and ").append(System.lineSeparator()).append("\t\t\t\t\t");
					}
					if (transition.getMultipleAgentActions().size() > 1)
						stringBuilder.append(")");

					stringBuilder.append(";").append(System.lineSeparator());
//                if(toState == toStateAux) {
//                toState.setName(toState.getName().substring(12));
//                }
					if (fromState == fromStateAux) {
						fromState.setName(fromState.getName().substring(12));
					}
				}
			}
			stringBuilder.append("\t").append("end Evolution").append(System.lineSeparator());
			stringBuilder.append("end Agent").append(System.lineSeparator());
		}

		stringBuilder.append("Evaluation").append(System.lineSeparator());
		for (String term: atlModel.getATL().getTerms()) {
			stringBuilder.append("\t").append(term).append(" if (Environment.").append(term).append(" = true);").append(System.lineSeparator());
		}
		stringBuilder.append("\t").append("end Evaluation").append(System.lineSeparator());

		alreadyAddedLabels.clear();
		HashMap<String, Boolean> initialLabels = new HashMap<>();
//        HashMap<String, Boolean> initialLabelsAgents = new HashMap<>();
		stringBuilder.append("\t").append("InitStates").append(System.lineSeparator());
		for (int i = 0; i < atlModel.getStates().size(); i++) {
			State state = atlModel.getStates().get(i);
			stringBuilder.append("\t").append("\t").append("Environment.").append(state.getName()).append(" = ").append(state.isInitial());
			if (CollectionUtils.isNotEmpty(state.getLabels())) {
				for (int j = 0; j < state.getLabels().size(); j++) {
					String label = state.getLabels().get(j);
					if(initialLabels.containsKey(label)) {
						if(state.isInitial()) {
							initialLabels.put(label, state.isInitial());
						}
					} else {
						initialLabels.put(label, state.isInitial());
					}
				}
			}

			if (CollectionUtils.isNotEmpty(state.getFalseLabels())) {
				for (int j = 0; j < state.getFalseLabels().size(); j++) {
					String label = state.getFalseLabels().get(j);
					initialLabels.putIfAbsent(label, false);
				}
			}

			if (i<atlModel.getStates().size()-1) {
				stringBuilder.append(" and ").append(System.lineSeparator());
			}
		}

		for(Agent agent : atlModel.getAgents()) {
			for(List<String> lIndS : agent.getIndistinguishableStates()) {
				stringBuilder.append(" and ").append(System.lineSeparator());
				stringBuilder.append("\t").append("\t").append(agent.getName()).append(".").append("imp_").append(String.join("_", lIndS)).append(" = ").append(atlModel.getStates().stream().anyMatch(s -> lIndS.contains(s.getName()) && s.isInitial()));
//                for(String s : lIndS) {
//                    List<String> l = atlModel.getState(s).getLabels();
//                    if(CollectionUtils.isNotEmpty(l)) {
//                        for (String label : l) {
//                            if (initialLabelsAgents.containsKey(label)) {
//                                if (atlModel.getState(s).isInitial()) {
//                                    initialLabelsAgents.put(agent.getName() + "." + label, atlModel.getState(s).isInitial());
//                                }
//                            } else {
//                                initialLabelsAgents.put(agent.getName() + "." + label, atlModel.getState(s).isInitial());
//                            }
//                        }
//                    }
//                }
			}
		}

		for(Entry<String, Boolean> initialLabel : initialLabels.entrySet()) {
			stringBuilder.append(" and ").append(System.lineSeparator());
			stringBuilder.append("\t").append("\t").append("Environment.").append(initialLabel.getKey()).append(" = ").append(initialLabel.getValue());
		}
//        for(Entry<String, Boolean> initialLabel : initialLabelsAgents.entrySet()) {
//            stringBuilder.append(" and ").append(System.lineSeparator());
//            stringBuilder.append("\t").append("\t").append(initialLabel.getKey()).append(" = ").append(initialLabel.getValue());
//        }

//        if (CollectionUtils.isNotEmpty(atlModel.getAgents())) {
//            stringBuilder.append(" and ").append(System.lineSeparator());
//            for (int i = 0; i < atlModel.getAgents().size(); i++) {
//                Agent agent = atlModel.getAgents().get(i);
//                stringBuilder.append("\t").append("\t").append(agent.getName()).append(".play = true");
//                if (i<atlModel.getAgents().size()-1)
//                    stringBuilder.append(" and ").append(System.lineSeparator());
//            }
//        }

		stringBuilder.append(";").append(System.lineSeparator()).append("\t").append("end InitStates").append(System.lineSeparator());

		stringBuilder.append("Groups").append(System.lineSeparator());
		for(Group g : atlModel.getGroups()) {
			stringBuilder.append("\t").append(g.getName()).append("=").append("{").append(String.join(",", g.getAgents())).append("};").append(System.lineSeparator());
		}
		stringBuilder.append("end Groups").append(System.lineSeparator());

		stringBuilder.append("Formulae").append(System.lineSeparator());
		stringBuilder.append("\t");
//		if (isMayModel)
//			stringBuilder.append("!(");
		stringBuilder.append(atlModel.getATL());
//		stringBuilder.append("<").append(atlModel.getGroup().getName()).append(">").append(atlModel.getFormula().getSubformula());
//		if (isMayModel)
//			stringBuilder.append(")");
		stringBuilder.append(";").append(System.lineSeparator());
		stringBuilder.append("end Formulae").append(System.lineSeparator());



		return stringBuilder.toString();
	}


	private static Map<String, List<String>> getAvailableActions(AtlModel atlModel, Agent agent) {
		Map<String, List<String>> availableActionMap = new HashMap<>();
		for (Transition transition : atlModel.getTransitions()) {
			if (!availableActionMap.containsKey(transition.getFromState())) {
				availableActionMap.put(transition.getFromState(), new ArrayList<>());
			}

			for (List<AgentAction> agentActionList : transition.getAgentActions()) {
				for (AgentAction agentAction : agentActionList) {
					if (agentAction.getAgent().equals(agent.getName())
							&&
								!availableActionMap.get(transition.getFromState()).contains(agentAction.getAction())) {
						availableActionMap.get(transition.getFromState()).add(agentAction.getAction());
					}

				}
			}
			for(MultipleAgentAction multAgentAction : transition.getMultipleAgentActions()) {
				if(multAgentAction.getAgent().equals(agent.getName())) {
					for(String action : multAgentAction.getActions()) {
						if(!availableActionMap.get(transition.getFromState()).contains(action)) {
							availableActionMap.get(transition.getFromState()).add(action);
						}
					}
				}
			}
		}

		return availableActionMap;
	}

	public static String modelCheckBR(String mcmasFilePath, int bound) throws IOException {
		try(Scanner scanner = new Scanner(Runtime.getRuntime().exec("python /media/angelo/WorkData/mcmas-1.3.0/mcmas_br/atl3valued.py " + bound + " " + mcmasFilePath).getInputStream()).useDelimiter("\\A")) {
			return scanner.hasNext() ? scanner.next() : "";
		}
	}

	public static String modelCheck(String mcmasFilePath) throws IOException {
		try(Scanner scanner = new Scanner(Runtime.getRuntime().exec("/media/angelo/WorkData/mcmas-1.3.0/mcmas " + mcmasFilePath).getInputStream()).useDelimiter("\\A")) {
			return scanner.hasNext() ? scanner.next() : "";
		}
	}

	public static boolean getMcmasResult(String mcmasOutput) {
//		if(mcmasOutput.contains("error")) throw new RuntimeException("MCMAS error: \n\n" + mcmasOutput);
		return  (mcmasOutput.contains("is TRUE in the model"));
	}

	public static Automaton.Outcome getMcmasResultBR(String mcmasOutput) {
//		if(mcmasOutput.contains("error")) throw new RuntimeException("MCMAS error: \n\n" + mcmasOutput);
		if(mcmasOutput.contains("Verification Result: TRUE")) {
			return Automaton.Outcome.True;
		} else if(mcmasOutput.contains("Verification Result: FALSE")) {
			return Automaton.Outcome.False;
		} else {
			return Automaton.Outcome.Unknown;
		}
	}

	public static boolean refinement(AtlModel abstractModelMust, AtlModel abstractModelMay, AtlModel model, StateCluster failureState) { //, Abstraction kind) {
		Map<State, Map<State, Boolean>> m = new HashMap<>();
		for(State s1 : failureState.getChildStates()) {
			Map<State, Boolean> auxMap = new HashMap<>();
			for(State s2 : failureState.getChildStates()) {
				auxMap.put(s2, true);
			}
			m.put(s1, auxMap);
		}
		check1(abstractModelMay, model, failureState, m);
		boolean update = true;
		while(update) {
			update = check2(failureState, m);
		}
		AtlModel model1 = model.clone();
		for(State s1 : failureState.getChildStates()) {
			for (State s2 : failureState.getChildStates()) {
				//if(s1.equals(s2)) continue;
				if(m.get(s1).get(s2)) {
					abstractModelMay.getStates().remove(failureState);
					abstractModelMust.getStates().remove(failureState);
					abstractModelMay.getTransitions().removeIf(t -> t.getFromState().equals(failureState.getName()) || t.getToState().equals(failureState.getName()));
					abstractModelMust.getTransitions().removeIf(t -> t.getFromState().equals(failureState.getName()) || t.getToState().equals(failureState.getName()));
					StateCluster v = new StateCluster(s1.clone());
					StateCluster w = new StateCluster(s2.clone());
					v.setInitial(failureState.isInitial());
					w.setInitial(failureState.isInitial());
					List<State> auxS1 = new ArrayList<>();
					for(State s : abstractModelMay.getStates()) {
						auxS1.add(s.clone());
					}
					List<State> auxS2 = new ArrayList<>();
					for(State s : abstractModelMust.getStates()) {
						auxS2.add(s.clone());
					}
					for(State t : failureState.getChildStates()) {
						if(t.equals(s1) || t.equals(s2)) continue;
						if(m.get(s1).get(t)) {
							w.addChildState(t.clone());
						} else {
							v.addChildState(t.clone());
						}
					}
					auxS1.add(v.clone());
					auxS2.add(v.clone());
					auxS1.add(w.clone());
					auxS2.add(w.clone());
					abstractModelMay.setStates(auxS1);
					abstractModelMust.setStates(auxS2);
//					for(State sAux : abstractModelMay.getStates()) {
//						StateCluster s = (StateCluster) sAux;
//						createTransition(v, abstractModelMay.getTransitions(), s,
//								kind == Abstraction.May ?
//									v.hasMayTransition(s, model) :
//									v.hasMustTransition(s, model));
//						createTransition(s, abstractModelMay.getTransitions(), v,
//								kind == Abstraction.May ?
//										s.hasMayTransition(v, model) :
//										s.hasMustTransition(v, model));
//						createTransition(w, abstractModelMay.getTransitions(), s,
//								kind == Abstraction.May ?
//										w.hasMayTransition(s, model) :
//										w.hasMustTransition(s, model));
//						createTransition(s, abstractModelMay.getTransitions(), w,
//								kind == Abstraction.May ?
//										s.hasMayTransition(w, model) :
//										s.hasMustTransition(w, model));
//					}
					for(State sAux : abstractModelMay.getStates()) {
						StateCluster s = (StateCluster) sAux;
						createTransition(v, abstractModelMay.getTransitions(), s, v.hasMayTransition(s, model, null));
						createTransition(s, abstractModelMay.getTransitions(), v, s.hasMayTransition(v, model, null));
						createTransition(w, abstractModelMay.getTransitions(), s, w.hasMayTransition(s, model, null));
						createTransition(s, abstractModelMay.getTransitions(), w, s.hasMayTransition(w, model, null));
					}
					for(State sAux : abstractModelMust.getStates()) {
						StateCluster s = (StateCluster) sAux;
						createTransition(v, abstractModelMust.getTransitions(), s, v.hasMustTransition(s, model1));
						createTransition(s, abstractModelMust.getTransitions(), v, s.hasMustTransition(v, model1));
						createTransition(w, abstractModelMust.getTransitions(), s, w.hasMustTransition(s, model1));
						createTransition(s, abstractModelMust.getTransitions(), w, s.hasMustTransition(w, model1));
					}
					abstractModelMay.setStateMap(null);
					abstractModelMust.setStateMap(null);
					if(new Random().nextBoolean()) {
						failureState.setName(v.getName());
					} else {
						failureState.setName(w.getName());
					}
					return true;
				}
			}
		}
		return false;
	}

	private static void createTransition(State v, List<Transition> transitions, State s, List<List<AgentAction>> agentActions) {
		if(!agentActions.isEmpty() && transitions.stream().noneMatch(t -> t.getFromState().equals(v.getName()) && t.getToState().equals(s.getName()))) {
			removeDuplicates(agentActions);
			Transition transition = new Transition();
			transition.setFromState(v.getName());
			transition.setToState(s.getName());
			transition.setAgentActions(agentActions);
			transitions.add(transition);
		}
	}

	public static void check1(AtlModel abstractModel, AtlModel model, StateCluster failureState, Map<State, Map<State, Boolean>> m) {
		for(String preName : abstractModel.getTransitions().stream().filter(t -> t.getToState().equals(failureState.getName())).map(Transition::getFromState).collect(Collectors.toList())) {
			StateCluster pre = (StateCluster) abstractModel.getState(preName);
			for(State s1 : failureState.getChildStates()) {
				for(State s2 : failureState.getChildStates()) {
					//if(s1.equals(s2)) continue;
					boolean cont = false;
					for(State t1 : pre.getChildStates()) {
						for(State t2 : pre.getChildStates()) {
							//if(t1.equals(t2)) continue;
							Optional<Transition> tr1 =
									model.getTransitions().stream().filter(t ->
											t.getFromState().equals(t1.getName()) &&
											t.getToState().equals(s1.getName())).findAny();
							Optional<Transition> tr2 =
									model.getTransitions().stream().filter(t ->
											t.getFromState().equals(t2.getName()) &&
													t.getToState().equals(s2.getName())).findAny();
							if(tr1.isPresent() && tr2.isPresent()) {
								// to be checked (here, we are assuming a simple ATL property with only one strategic operator. What to do with formulae like: <A>F<B>Xp?)
								Group group = new Group();
								for(Group g : abstractModel.getGroups()) {
									if(abstractModel.getATL() instanceof ATL.Existential && g.getName().equals(((ATL.Existential)(abstractModel.getATL())).getGroup())) {
										group = g;
										break;
									}
									if(abstractModel.getATL() instanceof ATL.Universal && g.getName().equals(((ATL.Universal)(abstractModel.getATL())).getGroup())) {
										group = g;
										break;
									}
								}
								//
								for(String a : group.getAgents()) {
									if(s1.equals(s2) || model.getAgentMap().get(a).getIndistinguishableStates().stream().anyMatch(ind -> ind.contains(s1.getName()) && ind.contains(s2.getName()))) {
										List<List<AgentAction>> auxL1 = tr1.get().getAgentActions();
										List<List<AgentAction>> auxL2 = tr2.get().getAgentActions();
										if(!auxL1.isEmpty() && !auxL2.isEmpty()) {
											Optional<AgentAction> act1 = auxL1.get(0).stream().filter(act -> act.getAgent().equals(a)).findAny();
											Optional<AgentAction> act2 = auxL2.get(0).stream().filter(act -> act.getAgent().equals(a)).findAny();
											if(act1.isPresent() && act2.isPresent() && act1.get().getAction().equals(act2.get().getAction())) {
												m.get(s1).put(s2, false);
												cont = true;
												break;
											}
										}
									}
								}
							}
							if(cont) {
								break;
							}
						}
						if(cont) {
							break;
						}
					}
				}
			}
		}
	}

	public static boolean check2(StateCluster failureState, Map<State, Map<State, Boolean>> m) {
		boolean update = false;
		for(State s1 : failureState.getChildStates()) {
			for (State s2 : failureState.getChildStates()) {
				if(s1.equals(s2)) continue;
				if(m.get(s1).get(s2)) {
					for (State t : failureState.getChildStates()) {
						if(!m.get(s1).get(t) && !m.get(s2).get(t)) {
							m.get(s1).put(s2, false);
							update = true;
							break;
						}
					}
				}
			}
		}
		return update;
	}

	public static class FailureState {
		private StateCluster state;
		private ATL formula;

		public FailureState(StateCluster state, ATL formula) {
			this.state = state;
			this.formula = formula;
		}

		public StateCluster getState() {
			return state;
		}

		public void setState(StateCluster state) {
			this.state = state;
		}

		public ATL getFormula() {
			return formula;
		}

		public void setFormula(ATL formula) {
			this.formula = formula;
		}
	}

	public static FailureState failureState(AtlModel atlModelMust, AtlModel atlModelMay, AtlModel atlModel, StateCluster state, ATL formula) throws IOException {
		if(formula instanceof ATL.Atom) {
			return new FailureState(state, formula);
		} else if(formula instanceof ATL.Not) {
			return failureState(atlModelMust, atlModelMay, atlModel, state, ((ATL.Not) formula).getSubFormula());
		} else if(formula instanceof ATL.And) {
//			atlModel.setATL(((ATL.And) formula).getLeft());
			if(AtlModel.modelCheck(((ATL.And) formula).getLeft(), atlModelMust, atlModelMay) == Automaton.Outcome.Unknown) {
				return failureState(atlModelMust, atlModelMay, atlModel, state, ((ATL.And) formula).getLeft());
			} else {
				return failureState(atlModelMust, atlModelMay, atlModel, state, ((ATL.And) formula).getRight());
			}
		} else { // strategic
			ATL subFormula = ((ATL.Existential)formula).getSubFormula().transl(true);
			Set<String> alphabet = new HashSet<>();
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getFalseLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			// To be updated
			ATL normalForm = subFormula.normalForm();
			Automaton automaton = new Automaton(normalForm, normalForm.getClosure(), Automaton.Outcome.Unknown, alphabet, true);

//			Automaton aux =  atlModelMust.toAutomaton();

			Automaton productMust = atlModelMust.toAutomaton().product(automaton);
			Automaton productMay = atlModelMay.toAutomaton().product(automaton);
			Automaton path = productMust.getPath();
			if(path == null) {
				path = productMay.getPath();
				if(path == null) {
					return new FailureState(state, formula);
				} else {
					return failurePath(atlModelMust, atlModelMay, atlModel, path, normalForm);
				}
			} else {
				return failurePath(atlModelMust, atlModelMay, atlModel, path, normalForm);
			}
			// To be updated
		}
	}

	public static FailureState failurePath(AtlModel atlModelMust, AtlModel atlModelMay, AtlModel atlModel, Automaton path, ATL formula) throws IOException {
		if(formula instanceof ATL.Atom) {
			String p1 = path.getInitialStates().stream().findFirst().get();
			String p1aux = p1.substring(0, p1.indexOf("_"));
			StateCluster state;
			if(atlModelMust.getState(p1aux) instanceof StateCluster) {
				state = (StateCluster) atlModelMust.getState(p1aux);
			} else {
				state = new StateCluster(atlModelMust.getState(p1aux));
			}
			return failureState(atlModelMust, atlModelMay, atlModel, state, formula);
		} else if(formula instanceof ATL.Not) {
			return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.Not) formula).getSubFormula());
		} else if(formula instanceof ATL.And) {
			Set<String> alphabet = new HashSet<>();
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getFalseLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			Automaton automaton = new Automaton(((ATL.And) formula).getLeft(), ((ATL.And) formula).getLeft().getClosure(), Automaton.Outcome.Unknown, alphabet, true);
//			Automaton product = atlModel.toAutomaton().product(automaton);
			if(path.product(automaton).getPath() != null) {
				return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.And) formula).getLeft());
			} else {
				return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.And) formula).getRight());
			}
		} else if(formula instanceof ATL.Or) {
			Set<String> alphabet = new HashSet<>();
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getFalseLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			Automaton automaton = new Automaton(((ATL.Or) formula).getLeft(), ((ATL.Or) formula).getLeft().getClosure(), Automaton.Outcome.Unknown, alphabet, true);
//			Automaton product = atlModel.toAutomaton().product(automaton);
			if(path.product(automaton).getPath() != null) {
				return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.Or) formula).getLeft());
			} else {
				return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.Or) formula).getRight());
			}
		} else if(formula instanceof ATL.Next) {
			path.moveInitialStateof(1);
			return failurePath(atlModelMust, atlModelMay, atlModel, path, ((ATL.Next) formula).getSubFormula());
		} else if(formula instanceof ATL.Until) {
			boolean check1 = true;
			boolean check2 = true;
			Set<String> alphabet = new HashSet<>();
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			for(List<String> labels : atlModelMust.getStates().stream().map(State::getFalseLabels).collect(Collectors.toList())) {
				alphabet.addAll(labels);
			}
			boolean first = true;
			while(check1 && check2) {
				if(!first) {
					path.moveInitialStateof(1); // check here!
				} else {
					first = false;
				}
				Automaton automaton = new Automaton(((ATL.Until) formula).getRight(), ((ATL.Until) formula).getRight().getClosure(), Automaton.Outcome.Unknown, alphabet, true);
//				Automaton product = atlModel.toAutomaton().product(automaton);
				if(path.product(automaton).getPath() != null) {
					check2 = false;
				} else {
					automaton = new Automaton(((ATL.Until) formula).getLeft(), ((ATL.Until) formula).getLeft().getClosure(), Automaton.Outcome.Unknown, alphabet, true);
//					product = atlModel.toAutomaton().product(automaton);
//					path.moveInitialStateof(1);
					if(path.product(automaton).getPath() != null) {
						check1 = false;
					}
				}

			}
			if(!check2) {
				return failurePath(atlModelMust, atlModelMay, atlModel, path,((ATL.Until) formula).getRight());
			} else {
				return failurePath(atlModelMust, atlModelMay, atlModel, path,((ATL.Until) formula).getLeft());
			}
		} else {
			throw new RuntimeException("Case not handled yet");
		}
	}

	public static Automaton.Outcome modelCheckingProcedureBR(AtlModel atlModel, int bound) throws IOException {
		String mcmasProgram = AbstractionUtils.generateMCMASProgram(atlModel, false);
		String fileName = "/tmp/model.ispl";
		Files.write(Paths.get(fileName), mcmasProgram.getBytes());
		String mcmasOutputAtlModel = AbstractionUtils.modelCheckBR(fileName, bound);
		return AbstractionUtils.getMcmasResultBR(mcmasOutputAtlModel);
	}

	public static Automaton.Outcome modelCheckingProcedure(AtlModel atlModel) throws IOException {
		atlModel.getAgentActionsByStates();
		AtlModel atlModelMust = atlModel.createAbstraction(AtlModel.Abstraction.Must);
		must = atlModelMust;
		AtlModel atlModelMay = atlModel.createAbstraction(AtlModel.Abstraction.May);
		State state = atlModel.getStates().stream().filter(State::isInitial).findAny().get();
		atlModelMust.getStates().forEach(s -> s.setInitial(s instanceof StateCluster && ((StateCluster) s).containsChildState(state)));
		atlModelMay.getStates().forEach(s -> s.setInitial(s instanceof StateCluster && ((StateCluster) s).containsChildState(state)));
		Automaton.Outcome outcome = AtlModel.modelCheck(atlModel.getATL(), atlModelMust, atlModelMay);
		if (outcome == Automaton.Outcome.Unknown) {
//			good = true;
			int i = 0;
			ATL innermostFormula = atlModel.getATL().innermostFormula();
			while (innermostFormula != atlModel.getATL()) {
//				atlModelMust.setATL(innermostFormula.transl(true));
//				atlModelMay.setATL(innermostFormula.transl(false));
				List<StateCluster> goodStates = new ArrayList<>();
				for (StateCluster stateCluster : AbstractionUtils.getStateClusters(atlModel)) {
					String stateClusterName = stateCluster.getName();
					if(stateCluster.getChildStates().size() == 1 ||
						atlModelMust.getStates().stream().noneMatch(s -> s.getName().equals(stateClusterName)) ||
						atlModelMay.getStates().stream().noneMatch(s -> s.getName().equals(stateClusterName))) {
						continue;
					}
					atlModelMust.getStates().forEach(s -> s.setInitial(s.getName().equals(stateClusterName)));
					atlModelMay.getStates().forEach(s -> s.setInitial(s.getName().equals(stateClusterName)));

					outcome = AtlModel.modelCheck(innermostFormula, atlModelMust, atlModelMay);
					boolean split = true;
					while (outcome == Automaton.Outcome.Unknown && split) {
						FailureState failureState = failureState(atlModelMust, atlModelMay, atlModel, stateCluster, innermostFormula);
						boolean getVorW = false;
						if(failureState.getState().getName().equals(stateCluster.getName())) {
							getVorW = true;
						}
						split = refinement(atlModelMust, atlModelMay, atlModel, failureState.getState());
						if(getVorW) {
							stateCluster = (StateCluster) atlModelMust.getState(failureState.getState().getName());
						}
						outcome = AtlModel.modelCheck(innermostFormula, atlModelMust, atlModelMay);
					}
					if(outcome != Automaton.Outcome.Unknown) {
						goodStates.add(stateCluster);
					}
				}
				String atom = "atom" + i;
				String atom_tt = atom + "_tt";
				String atom_ff = atom + "_ff";
//				atlModel.getState(state.getName()).getLabels().add(atom); // add(atom, s) from paper (removed because it seems pointless)
				atlModelMust.updateModel(atom_tt, atom_ff, goodStates);
				atlModelMay.updateModel(atom_tt, atom_ff, goodStates);
				atlModel.setATL(atlModel.getATL().updateInnermostFormula(atom));
				i++;
				innermostFormula = atlModel.getATL().innermostFormula();
			}
			atlModelMust.getStates().forEach(s -> s.setInitial(s instanceof StateCluster && ((StateCluster) s).containsChildState(state)));
			atlModelMay.getStates().forEach(s -> s.setInitial(s instanceof StateCluster && ((StateCluster) s).containsChildState(state)));
			StateCluster sCluster = (StateCluster) atlModelMust.getStates().stream().filter(s -> s instanceof StateCluster && ((StateCluster) s).containsChildState(state)).findAny().get();
//			atlModelMust.setATL(innermostFormula);
//			atlModelMay.setATL(innermostFormula);
			outcome = AtlModel.modelCheck(atlModel.getATL(), atlModelMust, atlModelMay);
			boolean split = true;
			while (outcome == Automaton.Outcome.Unknown && split) {
//				sCluster = (StateCluster) atlModelMust.getStates().stream().filter(s -> s instanceof StateCluster && ((StateCluster) s).getChildStates().size()>1).findAny().get();
				FailureState failureState = failureState(atlModelMust, atlModelMay, atlModel, sCluster, innermostFormula);
				boolean getVorW = false;
				if(failureState.getState().getName().equals(sCluster.getName())) {
					getVorW = true;
				}
				split = refinement(atlModelMust, atlModelMay, atlModel, failureState.getState());
				if(getVorW) {
					sCluster = (StateCluster) atlModelMust.getState(failureState.getState().getName());
				}
				outcome = AtlModel.modelCheck(atlModel.getATL(), atlModelMust, atlModelMay);
			}
		}
		return outcome;
	}
}
