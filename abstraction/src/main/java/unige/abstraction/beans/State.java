package unige.abstraction.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State extends JsonObject implements Comparable<State>, Cloneable {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("labels")
	@Expose
	private List<String> labels = new ArrayList<>();
	@SerializedName("initial")
	@Expose
	private boolean initial;

	private List<String> falseLabels = new ArrayList<>();

	public State() {
	}
	
	public State(String name, boolean initial, String... labels) {
		this.name = name;
		this.initial = initial;
		if (ArrayUtils.isNotEmpty(labels)) {
			this.labels.addAll(Arrays.asList(labels));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public StateCluster toStateCluster() {
		return new StateCluster(this);
	}

	public boolean isInitial() {
		return initial;
	}

	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	public List<String> getFalseLabels() {
		return falseLabels;
	}

	public void setFalseLabels(List<String> falseLabels) {
		this.falseLabels = falseLabels;
	}

	@Override
	public int compareTo(State anotherState) {
		return name.compareTo(anotherState.getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof State)) {
			return false;
		}
		return name.equals(((State)obj).getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public State clone() {
		State state;
		try {
			state = (State) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			throw new RuntimeException("Superclass messed up", ex);
		}
		state.labels = new ArrayList<>(this.labels);
		state.falseLabels = new ArrayList<>(this.falseLabels);
		return state;
	}
}
