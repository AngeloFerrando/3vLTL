package unige.abstraction.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Formula1 extends JsonObject implements Cloneable {

	@SerializedName("group")
	@Expose
	private String name;
	@SerializedName("sub-formula")
	@Expose
	private String subformula;
	@SerializedName("terms")
	@Expose
	private List<String> terms = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubformula() {
		return subformula;
	}

	public void setSubformula(String subformula) {
		this.subformula = subformula;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	@Override
	public Formula1 clone() {
		Formula1 formula = new Formula1();
		formula.name = name;
		formula.terms = new ArrayList<>(this.terms);
		formula.subformula = subformula;
		return formula;
	}

}
