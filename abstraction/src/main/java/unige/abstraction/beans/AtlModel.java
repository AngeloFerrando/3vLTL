package unige.abstraction.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import unige.abstraction.AbstractionUtils;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.collections4.MapUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.collections4.map.MultiKeyMap;
import unige.parser.*;

public class AtlModel extends JsonObject implements Cloneable {

	@SerializedName("states")
	@Expose
	private List<? extends State> states = null;
	@SerializedName("agents")
	@Expose
	private List<Agent> agents = new ArrayList<>();
	@SerializedName("transitions")
	@Expose
	private List<Transition> transitions = new ArrayList<>();
	@SerializedName("groups")
	@Expose
	private List<Group> groups = new ArrayList<>();
	@SerializedName("formula")
	@Expose
	private String formula;
	private ATL atl;

	private transient Map<String, State> stateMap;

	private transient Map<String, Agent> agentMap;

	private transient Map<String, List<Transition>> transitionMap;

	private transient MultiKeyMap<String, List<List<AgentAction>>> agentActionsByStates;

	public List<? extends State> getStates() {
		return states;
	}

	public void setStates(List<? extends State> states) {
		this.states = states;
	}

	public List<Agent> getAgents() {
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getFormula() {return formula; }
	public void setFormula(String formula) { this.formula = formula; }

	public ATL getATL() {
		if(atl == null) {
			CharStream codePointCharStream = CharStreams.fromString(formula);
			ATLLexer lexer = new ATLLexer(codePointCharStream);
			ATLParser parser = new ATLParser(new CommonTokenStream(lexer));
			ParseTree tree = parser.atlExpr();
			ATLVisitorImpl visitor = new ATLVisitorImpl();
			atl = visitor.visit(tree);
		}
		return atl;
	}

	public void setATL(ATL formula) {
		this.atl = formula;
	}

	public Map<String, State> getStateMap() {
		if (stateMap == null) {
			stateMap = new HashMap<>();
			for (State state : getStates()) {
				stateMap.put(state.getName(), state);
			}
		}
		return stateMap;
	}

	public void setStateMap(Map<String, State> map) {
		this.stateMap = map;
	}

	public State getState(String stateName) {
		return getStateMap().get(stateName);
	}

	public Map<String, Agent> getAgentMap() {
		if (agentMap == null) {
			agentMap = new HashMap<>();
			for (Agent agent : getAgents()) {
				agentMap.put(agent.getName(), agent);
			}
		}
		return agentMap;
	}

	public MultiKeyMap<String, List<List<AgentAction>>> getAgentActionsByStates() {
		if (agentActionsByStates == null) {
			agentActionsByStates = new MultiKeyMap<>();
			for (Transition transition : getTransitions()) {
				if (!agentActionsByStates.containsKey(transition.getFromState(), transition.getToState())) {
					agentActionsByStates.put(transition.getFromState(), transition.getToState(), new ArrayList<>());
				}
				if(!transition.getAgentActions().isEmpty()) {
					agentActionsByStates.get(transition.getFromState(), transition.getToState()).addAll(transition.getAgentActions());
				}
				if(!transition.getMultipleAgentActions().isEmpty()) {
					int n = transition.getMultipleAgentActions().size();
					Integer[] aux = new Integer[n];
					for(int i = 0; i < n; i++) {
						aux[i] = 0;
					}
					int m = 1;
					for(MultipleAgentAction mAct : transition.getMultipleAgentActions()) {
						m *= mAct.getActions().size();
					}
					List<List<AgentAction>> agActsL = new ArrayList<>();
					for(int j = 0; j < m; j++) {
						List<AgentAction> agActs = new ArrayList<>();
						boolean inc = true;
						for(int i = 0; i < n; i++) {
							String agent = transition.getMultipleAgentActions().get(i).getAgent();
							String action = transition.getMultipleAgentActions().get(i).getActions().get(aux[i]);
							agActs.add(new AgentAction(agent, action));
							if(inc) {
								aux[i] = (aux[i] + 1) % transition.getMultipleAgentActions().get(i).getActions().size();
								if(aux[i] != 0) {
									inc = false;
								}
							}
						}
						agActsL.add(agActs);
					}
					agentActionsByStates.get(transition.getFromState(), transition.getToState()).addAll(agActsL);
				}

			}
		}

		return agentActionsByStates;
	}

	public void setAgentActionsByStates(MultiKeyMap<String, List<List<AgentAction>>> map) {
		this.agentActionsByStates = map;
	}

	public Map<String, List<Transition>> getTransitionMap() {
		if (MapUtils.isEmpty(transitionMap)) {
			transitionMap = new HashMap<>();
			for (Transition transition : getTransitions()) {
				if (!transitionMap.containsKey(transition.getFromState())) {
					transitionMap.put(transition.getFromState(), new ArrayList<>());
				}
				transitionMap.get(transition.getFromState()).add(transition);
			}
		}

		return transitionMap;
	}

	public static AtlModel product(AtlModel model, Automaton automaton) {
		AtlModel result = model.clone();

		result.setAgents(model.getAgents());

		List<State> states = new ArrayList<>();

		for(String s : model.getStates().stream().map(State::getName).collect(Collectors.toList())) {
			for(String q : automaton.getStates()) {
				State state = new State();
				state.setName(s + "_x_" + q);
				state.setLabels(new ArrayList<>());
				state.setFalseLabels(new ArrayList<>());
				for(ATL ATL : automaton.getStateLTLMap().get(q)) {
					if(ATL instanceof ATL.Atom) {
						state.getLabels().add(ATL.toString());
					} else if(ATL instanceof ATL.Not) {
						state.getFalseLabels().add(((ATL.Not) ATL).getSubFormula().toString());
					}
				}
				states.add(state);
			}
		}
		result.setStates(states);

		for(State initialState : model.getStates().stream().filter(State::isInitial).collect(Collectors.toList())) {
			Set<ATL> event = new HashSet<>();
			initialState.getLabels().forEach(l -> event.add(new ATL.Atom(l)));
			initialState.getFalseLabels().forEach(l -> event.add(new ATL.Not(new ATL.Atom(l))));
			for (String q0 : automaton.getInitialStates()) {
				for (String next : automaton.next(q0, event)) {
					result.getState(initialState.getName() + "_x_" + next).setInitial(true);
//					State state = new State();
//					state.setName(initialState.getName() + "_x_" + next);
//					state.setLabels(new ArrayList<>());
//					state.setFalseLabels(new ArrayList<>());
//					for(LTL ltl : automaton.getStateLTLMap().get(next)) {
//						if(ltl instanceof LTL.Atom) {
//							state.getLabels().add(ltl.toString());
//						} else if(ltl instanceof LTL.Not) {
//							state.getFalseLabels().add(((LTL.Not) ltl).getSubFormula().toString());
//						}
//					}
//					states.add(state);
				}
			}
		}
		result.setTransitions(new ArrayList<>());
		for (Transition tr : model.getTransitions()) {
			Set<ATL> e = new HashSet<>();
			State toState = model.getState(tr.getToState());
			toState.getLabels().forEach(l -> e.add(new ATL.Atom(l)));
			toState.getFalseLabels().forEach(l -> e.add(new ATL.Not(new ATL.Atom(l))));
			for (String q : automaton.getStates()) {
				Set<String> qs = automaton.next(q, e);
				for (String qs1 : qs) {
					Transition newTr = new Transition();
					newTr.setFromState(tr.getFromState() + "_x_" + q);
					newTr.setToState(tr.getToState() + "_x_" + qs1);
					newTr.setAgentActions(tr.getAgentActions());
					newTr.setDefaultTransition(tr.isDefaultTransition());
					newTr.setMultipleAgentActions(tr.getMultipleAgentActions());
					result.getTransitions().add(newTr);
				}
			}
		}
		return result;
	}

	@Override
	public AtlModel clone() {
		AtlModel clone;
		try {
			clone = (AtlModel) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			throw new RuntimeException("Superclass messed up", ex);
		}

		List<State> statesAuxList = new ArrayList<>();
		for (State state : states) {
			State newState = new State(state.getName(), state.isInitial());
			newState.setLabels(new ArrayList<>(state.getLabels()));
			newState.setFalseLabels(new ArrayList<>(state.getFalseLabels()));
			statesAuxList.add(newState);
		}
		clone.states = statesAuxList;
		List<Agent> agentsAuxList = new ArrayList<>();
		for(Agent agent : agents) {
			Agent newAgent = new Agent();
			newAgent.setName(agent.getName());
			newAgent.setActions(new ArrayList<>(agent.getActions()));
			newAgent.setIndistinguishableStates(new ArrayList<>());
			for(List<String> indS : agent.getIndistinguishableStates()) {
				newAgent.getIndistinguishableStates().add(new ArrayList<>(indS));
			}
			agentsAuxList.add(newAgent);
		}
		clone.agents = agentsAuxList;
		List<Transition> transitionsAuxList = new ArrayList<>();
		for(Transition tr : transitions) {
			Transition newTransition = new Transition();
			newTransition.setFromState(tr.getFromState());
			newTransition.setToState(tr.getToState());
			newTransition.setAgentActions(new ArrayList<>());
			for(List<AgentAction> aal : tr.getAgentActions()) {
				List<AgentAction> aalAux = new ArrayList<>();
				for(AgentAction aa : aal) {
					AgentAction newAa = new AgentAction();
					newAa.setAgent(aa.getAgent());
					newAa.setAction(aa.getAction());
					aalAux.add(newAa);
				}
				newTransition.getAgentActions().add(aalAux);
			}
			List<MultipleAgentAction> maalAux = new ArrayList<>();
			for(MultipleAgentAction maa : tr.getMultipleAgentActions()) {
				MultipleAgentAction newMaa = new MultipleAgentAction();
				newMaa.setAgent(maa.getAgent());
				newMaa.setActions(new ArrayList<>(maa.getActions()));
				maalAux.add(newMaa);
			}
			newTransition.setMultipleAgentActions(maalAux);
			newTransition.setDefaultTransition(tr.isDefaultTransition());
			transitionsAuxList.add(newTransition);
		}
		clone.transitions = transitionsAuxList;
		clone.groups = new ArrayList<>();
		for(Group g : groups) {
			Group ng = new Group();
			ng.setName(g.getName());
			ng.setAgents(new ArrayList<>(g.getAgents()));
			clone.groups.add(ng);
		}
		clone.formula = formula;
		clone.atl = getATL().clone();
		clone.agentMap = null;
		clone.stateMap = null;
		clone.agentActionsByStates = null;
		return clone;
	}

	public Automaton toAutomaton() {
		Set<String> states = this.states.stream().map(State::getName).collect(Collectors.toSet());
		Set<String> initialStates = this.states.stream().filter(State::isInitial).map(State::getName).collect(Collectors.toSet());
//		String init = "init";
//		while(states.contains(init)) {
//			init = init + "_init";
//		}
//		states.add(init);
//		initialStates.add(init);
		Map<String, Map<Set<ATL>, Set<String>>> transitions = new HashMap<>();
		for(State s : this.states) {
			transitions.put(s.getName(), new HashMap<>());
		}
		for(State from : this.states) {
			Set<ATL> event = new HashSet<>();
			for(String l : from.getLabels()) {
				if(!l.endsWith("_uu")) {
					event.add(new ATL.Atom(l));
				}
			}
//			for(String l : s.getFalseLabels()) {
//				event.add(new ATL.Not(new ATL.Atom(l)));
//			}
			for(String to : this.transitions.stream().filter(t -> t.getFromState().equals(from.getName())).map(Transition::getToState).collect(Collectors.toSet())) {
				if(!transitions.containsKey(from.getName())) {
					transitions.put(from.getName(), new HashMap<>());
				}
				if(!transitions.get(from.getName()).containsKey(event)) {
					transitions.get(from.getName()).put(event, new HashSet<>());
				}
				transitions.get(from.getName()).get(event).add(to);
			}
//			if(s.isInitial()) {
//				if(!transitions.containsKey(init)) {
//					transitions.put(init, new HashMap<>());
//				}
//				if(!transitions.get(init).containsKey(event)) {
//					transitions.get(init).put(event, new HashSet<>());
//				}
//				transitions.get(init).get(event).add(s.getName());
//			}
		}
		Set<Set<String>> finalStates = new HashSet<>();
		finalStates.add(this.states.stream().map(State::getName).collect(Collectors.toSet()));
		return new Automaton(states, initialStates, transitions, finalStates);
	}

	public enum Abstraction { May, Must }

	public AtlModel createAbstraction(Abstraction kind) {
		if(kind == Abstraction.Must) {
			AtlModel mustAtlModel = this.clone();
			List<StateCluster> mustStateClusters = AbstractionUtils.getStateClusters(mustAtlModel);
			List<Transition> mustTransitions = AbstractionUtils.getMustTransitions(mustAtlModel, mustStateClusters);
			mustAtlModel.setStates(mustStateClusters);
			mustAtlModel.setTransitions(mustTransitions);
			mustAtlModel.setStateMap(null);
			mustAtlModel.setAgentActionsByStates(null);
//			mustAtlModel.setATL(this.getATL().transl(true));
			return mustAtlModel;
		} else {
			AtlModel mayAtlModel = this.clone();
			List<StateCluster> mayStateClusters = AbstractionUtils.getStateClusters(mayAtlModel);
			List<Transition> mayTransitions = AbstractionUtils.getMayTransitions(mayAtlModel, mayStateClusters);
			mayAtlModel.setStates(mayStateClusters);
			mayAtlModel.setTransitions(mayTransitions);
			mayAtlModel.setStateMap(null);
			mayAtlModel.setAgentActionsByStates(null);
//			mayAtlModel.setATL(this.getATL().transl(false));
			return mayAtlModel;
		}
	}

	public void makeTransitionsUnique() {
		int id = 0;
		for (Transition tr : transitions) {
			for (List<AgentAction> acts : tr.getAgentActions()) {
				for (AgentAction act : acts) {
					String actAux = act.getAction();
					act.setAction(act.getAction() + "_" + id++);
					for (Agent ag : agents) {
						if (ag.getName().equals(act.getAgent())) {
							ag.getActions().remove(actAux);
							ag.getActions().add(act.getAction());
						}
					}
				}
			}
			List<MultipleAgentAction> multActs = new ArrayList<>();
			for (MultipleAgentAction acts : tr.getMultipleAgentActions()) {
				MultipleAgentAction newAct = new MultipleAgentAction();
				newAct.setAgent(acts.getAgent());
				newAct.setActions(new ArrayList<>());
				for (String act : acts.getActions()) {
					newAct.getActions().add(act + "_" + id++);
				}
				multActs.add(newAct);
			}
			tr.setMultipleAgentActions(multActs);
		}
	}

	public static Automaton.Outcome modelCheck(AtlModel model) throws IOException {
		String mcmasProgram = AbstractionUtils.generateMCMASProgram(model, false);
		String fileName = "/tmp/st" + System.currentTimeMillis() + ".ispl";
		while (Files.exists(Paths.get(fileName))) {
			fileName = "/tmp/st" + System.currentTimeMillis() + ".ispl";
		}
		Files.write(Paths.get(fileName), mcmasProgram.getBytes());
		String mcmasOutputMustAtlModel = AbstractionUtils.modelCheck(fileName);
		if (AbstractionUtils.getMcmasResult(mcmasOutputMustAtlModel)) {
			return Automaton.Outcome.True;
		} else {
			return Automaton.Outcome.False;
		}
	}

	public static Automaton.Outcome modelCheck(ATL property, AtlModel mustAtlModel, AtlModel mayAtlModel) throws IOException {
		Automaton.Outcome result;
		if(auxiliary1(property.transl(false), mustAtlModel, mayAtlModel)) {
			result = Automaton.Outcome.False;
		} else if(auxiliary1(property.transl(true), mustAtlModel, mayAtlModel)) {
			result = Automaton.Outcome.True;
		} else {
			result = Automaton.Outcome.Unknown;
		}
		return result;
	}

	private static boolean auxiliary1(ATL property, AtlModel mustAtlModel, AtlModel mayAtlModel) throws IOException {
		String atom = "";
		int i = 0;
		while(!(property instanceof ATL.Atom || (property instanceof ATL.Not && ((ATL.Not) property).getSubFormula() instanceof ATL.Atom))) {
			ATL property1 = property.innermostFormula();
			atom = "auxiliary_atom_" + i++;
			if(property instanceof ATL.Not && ((ATL.Not) property).getSubFormula() instanceof ATL.Existential) {
				auxiliary2(property1, atom, mayAtlModel, mustAtlModel);
			} else {
				auxiliary2(property1, atom, mustAtlModel, mayAtlModel);
			}
//			if(property1 instanceof ATL.Existential) {
//				auxiliary2(property1, atom, mustAtlModel, mayAtlModel);
//			} else {
//				auxiliary2(property1, atom, mayAtlModel, mustAtlModel);
//			}
			property = property.updateInnermostFormula(atom);
		}
		String finalAtom = atom;
		boolean res;
		if(mustAtlModel.states.stream().anyMatch(s -> s.isInitial() && s.getLabels().contains(finalAtom))) {
			res = property instanceof ATL.Atom;
		} else {
			res = !(property instanceof ATL.Atom);
		}
		mustAtlModel.getStates().forEach(s -> s.setLabels(s.getLabels().stream().filter(l -> !l.startsWith("auxiliary_atom_")).collect(Collectors.toList())));
		mayAtlModel.getStates().forEach(s -> s.setLabels(s.getLabels().stream().filter(l -> !l.startsWith("auxiliary_atom_")).collect(Collectors.toList())));
		return res;
	}

	private static void auxiliary2(ATL property, String atom, AtlModel model1, AtlModel model2) throws IOException {
		State initialState = model1.getStates().stream().filter(State::isInitial).findFirst().get();
		for(State state : model1.getStates()) {
			model1.getStates().forEach(s -> s.setInitial(s.getName().equals(state.getName())));
			model1.setATL(property);
			String mcmasProgram = AbstractionUtils.generateMCMASProgram(model1, false);
			String fileName = "/tmp/model.ispl";
			Files.write(Paths.get(fileName), mcmasProgram.getBytes());
			String mcmasOutputAtlModel = AbstractionUtils.modelCheck(fileName);
			if(AbstractionUtils.getMcmasResult(mcmasOutputAtlModel) && !state.getLabels().contains(atom)) {
				state.getLabels().add(atom);
				model2.getState(state.getName()).getLabels().add(atom);
			}
		}
		model1.getStates().forEach(s -> s.setInitial(s.getName().equals(initialState.getName())));
	}


//	public static Automaton.Outcome modelCheck(AtlModel mustAtlModel, AtlModel mayAtlModel) throws IOException {
//		String mustMcmasProgram = AbstractionUtils.generateMCMASProgram(mustAtlModel, false);
//		String fileName = "/tmp/must" + System.currentTimeMillis() + ".ispl";
//		while (Files.exists(Paths.get(fileName))) {
//			fileName = "/tmp/must" + System.currentTimeMillis() + ".ispl";
//		}
//		Files.write(Paths.get(fileName), mustMcmasProgram.getBytes());
//		String mcmasOutputMustAtlModel = AbstractionUtils.modelCheck(fileName);
//		if (AbstractionUtils.getMcmasResult(mcmasOutputMustAtlModel)) {
//			return Automaton.Outcome.True;
//		} else {
//			String mayMcmasProgram = AbstractionUtils.generateMCMASProgram(mayAtlModel, true);
//			fileName = "/tmp/may" + System.currentTimeMillis() + ".ispl";
//			while (Files.exists(Paths.get(fileName))) {
//				fileName = "/tmp/may" + System.currentTimeMillis() + ".ispl";
//			}
//			Files.write(Paths.get(fileName), mayMcmasProgram.getBytes());
//			String mcmasOutputMayAtlModel = AbstractionUtils.modelCheck(fileName);
//			if (AbstractionUtils.getMcmasResult(mcmasOutputMayAtlModel)) {
//				return Automaton.Outcome.False;
//			}
//		}
//		return Automaton.Outcome.Unknown;
//	}

	public void updateModel(String atom_tt, String atom_ff, List<? extends State> states) {
		for(State s : this.states) {
			if(states.stream().anyMatch(s1 -> s1.getName().equals(s.getName()))) {
				if(s instanceof StateCluster) {
					for(State child : ((StateCluster) s).getChildStates()) {
						child.getLabels().add(atom_tt);
						child.getFalseLabels().add(atom_ff);
					}
				}
				s.getLabels().add(atom_tt);
				s.getFalseLabels().add(atom_ff);
			} else {
				if(s instanceof StateCluster) {
					for(State child : ((StateCluster) s).getChildStates()) {
						child.getLabels().add(atom_ff);
						child.getFalseLabels().add(atom_tt);
					}
				}
				s.getLabels().add(atom_ff);
				s.getFalseLabels().add(atom_tt);
			}
		}
	}

}
