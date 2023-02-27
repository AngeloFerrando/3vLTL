package unige.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ATL implements Cloneable {

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ATL)) { return false; }
        return o.toString().equals(this.toString());
    }

    public abstract boolean isLTL();
    public abstract List<String> getTerms();
    public ATL transl(boolean v) {
        return transl(v, false);
    }
    public abstract ATL transl(boolean v, boolean check);
    @Override
    public abstract ATL clone();
    public abstract Set<ATL> getClosure();
    public abstract ATL innermostFormula();
    public abstract ATL updateInnermostFormula(String atom);
    public ATL normalForm() {
        return normalForm(true);
    }
    public abstract ATL normalForm(boolean v);

    public static class Atom extends ATL {
        private String atom;

        public Atom(String atom) {
            this.atom = atom;
        }

        @Override
        public String toString() {
            return atom;
        }

        public String getAtom() {
            return atom;
        }

        public void setAtom(String atom) {
            this.atom = atom;
        }

        @Override
        public boolean isLTL() {
            return true;
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.add(atom);
            return list;
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(atom.equals("true")) {
                return v ? new Atom("true") : new Atom("false");
            } else if (atom.equals("false")) {
                return v ? new Atom("false") : new Atom("true");
            } else if(check){
                if(v) {
                    return new Or(new Atom(atom + "_tt"), new Atom(atom + "_uu"));
                } else {
                    return new Or(new Atom(atom + "_ff"), new Atom(atom + "_uu"));
                }
            } else {
                return new Atom(atom + (v ? "_tt" : "_ff"));
            }
        }

        @Override
        public Atom clone() {
            return new Atom(atom);
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = new HashSet<>();
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            return null;
        }

        @Override
        public Atom updateInnermostFormula(String atom) {
            return this;
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return this;
            } else {
                return new Not(this);
            }
        }
    }

    public static class Next extends ATL {
        private ATL subFormula;

        public Next(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public String toString() {
            return "X(" + subFormula.toString() + ")";
        }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public boolean isLTL() {
            return subFormula.isLTL();
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public Next transl(boolean v, boolean check) {
            return new Next(subFormula.transl(v, check));
        }

        @Override
        public Next clone() {
            return new Next(this.subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = subFormula.getClosure();
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            return subFormula.innermostFormula();
        }

        @Override
        public Next updateInnermostFormula(String atom) {
            return new Next(subFormula.updateInnermostFormula(atom));
        }

        @Override
        public ATL normalForm(boolean v) {
            return new Next(this.subFormula.normalForm(v));
        }
    }

    public static class And extends ATL {
        private ATL left;
        private ATL right;

        public And(ATL left, ATL right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left.toString() + " and " + right.toString() + ")";
        }

        public ATL getLeft() {
            return left;
        }

        public void setLeft(ATL left) {
            this.left = left;
        }

        public ATL getRight() {
            return right;
        }

        public void setRight(ATL right) {
            this.right = right;
        }

        @Override
        public boolean isLTL() {
            return left.isLTL() && right.isLTL();
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.addAll(left.getTerms());
            list.addAll(right.getTerms());
            return list;
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
                return new And(left.transl(true, check), right.transl(true, check));
            } else {
                return new Or(left.transl(false, check), right.transl(false, check));
            }
        }

        @Override
        public And clone() {
            return new And(left.clone(), right.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = left.getClosure();
            aux.addAll(right.getClosure());
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            if(left.innermostFormula() != null) {
                return left.innermostFormula();
            } else {
                return right.innermostFormula();
            }
        }

        @Override
        public And updateInnermostFormula(String atom) {
            if(left.innermostFormula() != null) {
                return new And(left.updateInnermostFormula(atom), right);
            } else {
                return new And(left, right.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new And(this.left.normalForm(true), this.right.normalForm(true));
            } else {
                return new Or(this.left.normalForm(false), this.right.normalForm(false));
            }
        }
    }

    public static class Or extends ATL {
        private ATL left;
        private ATL right;

        public Or(ATL left, ATL right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left.toString() + " or " + right.toString() + ")";
        }

        public ATL getLeft() {
            return left;
        }

        public void setLeft(ATL left) {
            this.left = left;
        }

        public ATL getRight() {
            return right;
        }

        public void setRight(ATL right) {
            this.right = right;
        }

        @Override
        public boolean isLTL() {
            return left.isLTL() && right.isLTL();
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.addAll(left.getTerms());
            list.addAll(right.getTerms());
            return list;
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
                return new Or(left.transl(true, check), right.transl(true, check));
            } else {
                return new And(left.transl(false, check), right.transl(false, check));
            }
        }

        @Override
        public Or clone() {
            return new Or(left.clone(), right.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = left.getClosure();
            aux.addAll(right.getClosure());
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            if(left.innermostFormula() != null) {
                return left.innermostFormula();
            } else {
                return right.innermostFormula();
            }
        }

        @Override
        public Or updateInnermostFormula(String atom) {
            if(left.innermostFormula() != null) {
                return new Or(left.updateInnermostFormula(atom), right);
            } else {
                return new Or(left, right.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new Or(this.left.normalForm(true), this.right.normalForm(true));
            } else {
                return new And(this.left.normalForm(false), this.right.normalForm(false));
            }
        }
    }

    public static class Implies extends ATL {
        private ATL left;
        private ATL right;

        public Implies(ATL left, ATL right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left.toString() + " -> " + right.toString() + ")";
        }

        public ATL getLeft() {
            return left;
        }

        public void setLeft(ATL left) {
            this.left = left;
        }

        public ATL getRight() {
            return right;
        }

        public void setRight(ATL right) {
            this.right = right;
        }

        @Override
        public boolean isLTL() {
            return left.isLTL() && right.isLTL();
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.addAll(left.getTerms());
            list.addAll(right.getTerms());
            return list;
        }

        @Override
        public Implies transl(boolean v, boolean check) {
            return new Implies(left.transl(v, check), right.transl(v, check));
        }

        @Override
        public Implies clone() {
            return new Implies(left.clone(), right.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = left.getClosure();
            aux.addAll(right.getClosure());
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            if(left.innermostFormula() != null) {
                return left.innermostFormula();
            } else {
                return right.innermostFormula();
            }
        }

        @Override
        public Implies updateInnermostFormula(String atom) {
            if(left.innermostFormula() != null) {
                return new Implies(left.updateInnermostFormula(atom), right);
            } else {
                return new Implies(left, right.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            return new Or(new Not(this.left), this.right).normalForm(v);
        }
    }

    public static class Eventually extends ATL {
        private ATL subFormula;

        public Eventually(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public String toString() {
            return "F(" + subFormula.toString() + ")";
        }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public boolean isLTL() {
            return subFormula.isLTL();
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
                return new Eventually(subFormula.transl(true, check));
            } else {
                return new Globally(subFormula.transl(false, check));
            }

        }

        @Override
        public Eventually clone() {
            return new Eventually(subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = subFormula.getClosure();
            aux.add(new Until(new Atom("true"), this.subFormula));
            aux.add(new ATL.Not(new Until(new Atom("true"), this.subFormula)));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            return subFormula.innermostFormula();
        }

        @Override
        public Eventually updateInnermostFormula(String atom) {
            return new Eventually(subFormula.updateInnermostFormula(atom));
        }

        @Override
        public ATL normalForm(boolean v) {
            return new Until(new Atom("true"), this.subFormula).normalForm(v);
        }
    }

    public static class Globally extends ATL {
        private ATL subFormula;

        public Globally(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public String toString() {
            return "G(" + subFormula.toString() + ")";
        }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public boolean isLTL() {
            return subFormula.isLTL();
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
                return new Globally(subFormula.transl(true, check));
            } else {
                return new Eventually(subFormula.transl(false, check));
            }
        }

        @Override
        public Globally clone() {
            return new Globally(subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = subFormula.getClosure();
            aux.add(new ATL.Release(new Atom("false"), this.subFormula));
            aux.add(new Not(new ATL.Release(new Atom("false"), this.subFormula)));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            return subFormula.innermostFormula();
        }

        @Override
        public Globally updateInnermostFormula(String atom) {
            return new Globally(subFormula.updateInnermostFormula(atom));
        }

        @Override
        public ATL normalForm(boolean v) {
            return new Release(new Atom("false"), this.subFormula).normalForm(v);
        }
    }

    public static class Until extends ATL {
        private ATL left;
        private ATL right;

        public Until(ATL left, ATL right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left.toString() + " U " + right.toString() + ")";
        }

        public ATL getLeft() {
            return left;
        }

        public void setLeft(ATL left) {
            this.left = left;
        }

        public ATL getRight() {
            return right;
        }

        public void setRight(ATL right) {
            this.right = right;
        }

        @Override
        public boolean isLTL() {
            return left.isLTL() && right.isLTL();
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.addAll(left.getTerms());
            list.addAll(right.getTerms());
            return list;
        }

        @Override
        public ATL transl(boolean v, boolean check) {
//            return v ? new Until(left.transl(true), right.transl(true)) : new Release(left.transl(false), right.transl(false));
            return v ? new Until(left.transl(true, check), right.transl(true, check)) : new Or(new Until(right.transl(false, check), new ATL.And(left.transl(false, check), right.transl(false))), new Globally(right.transl(false)));
        }

        @Override
        public Until clone() {
            return new Until(left.clone(), right.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = left.getClosure();
            aux.addAll(right.getClosure());
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            if(left.innermostFormula() != null) {
                return left.innermostFormula();
            } else {
                return right.innermostFormula();
            }
        }

        @Override
        public Until updateInnermostFormula(String atom) {
            if(left.innermostFormula() != null) {
                return new Until(left.updateInnermostFormula(atom), right);
            } else {
                return new Until(left, right.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new Until(this.left.normalForm(true), this.right.normalForm(true));
            } else {
                return new Release(this.left.normalForm(false), this.right.normalForm(false));
            }
        }
    }

    // to be removed
    public static class Release extends ATL {
        private ATL left;
        private ATL right;

        public Release(ATL left, ATL right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left.toString() + " R " + right.toString() + ")";
        }

        public ATL getLeft() {
            return left;
        }

        public void setLeft(ATL left) {
            this.left = left;
        }

        public ATL getRight() {
            return right;
        }

        public void setRight(ATL right) {
            this.right = right;
        }

        @Override
        public boolean isLTL() {
            return left.isLTL() && right.isLTL();
        }

        @Override
        public List<String> getTerms() {
            List<String> list = new ArrayList<>();
            list.addAll(left.getTerms());
            list.addAll(right.getTerms());
            return list;
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            return v ? new Release(left.transl(true, check), right.transl(true, check)) : new Until(left.transl(false, check), right.transl(false, check));
        }

        @Override
        public Release clone() {
            return new Release(left.clone(), right.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = left.getClosure();
            aux.addAll(right.getClosure());
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            if(left.innermostFormula() != null) {
                return left.innermostFormula();
            } else {
                return right.innermostFormula();
            }
        }

        @Override
        public Release updateInnermostFormula(String atom) {
            if(left.innermostFormula() != null) {
                return new Release(left.updateInnermostFormula(atom), right);
            } else {
                return new Release(left, right.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new Release(this.left.normalForm(true), this.right.normalForm(true));
            } else {
                return new Until(this.left.normalForm(false), this.right.normalForm(false));
            }
        }
    }

    public static class Not extends ATL {
        private ATL subFormula;

        public Not(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public String toString() {
            return "!(" + subFormula.toString() + ")";
        }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        @Override
        public boolean isLTL() {
            return subFormula.isLTL();
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            return subFormula.transl(!v, check);
        }

        @Override
        public Not clone() {
            return new Not(subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            Set<ATL> aux = subFormula.getClosure();
            aux.add(this);
            aux.add(new ATL.Not(this));
            return aux;
        }

        @Override
        public ATL innermostFormula() {
            ATL atl = subFormula.innermostFormula();
//            if(atl == subFormula) {
//                return this;
//            } else {
                return atl;
//            }
        }

        @Override
        public ATL updateInnermostFormula(String atom) {
//            ATL atl = subFormula.updateInnermostFormula(atom);
//            if(atl instanceof Atom && ((Atom) atl).atom.equals(atom)) {
//                return atl;
//            } else {
                return new Not(subFormula.updateInnermostFormula(atom));
//            }
        }

        @Override
        public ATL normalForm(boolean v) {
            return this.subFormula.normalForm(!v);
        }
    }

    public static class Existential extends ATL {
        private String group;
        private ATL subFormula;

        public Existential(String group, ATL subFormula) {
            this.group = group;
            this.subFormula = subFormula;
        }

        @Override
        public String toString() {
            String s = subFormula.toString();
            if(s.startsWith("(") && s.endsWith(")")) {
                s = s.substring(1, s.length()-1);
            }
            return "<" + group + ">" + s; }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        public String getGroup() { return group; }

        @Override
        public boolean isLTL() {
            return false;
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
                return new Existential(group, subFormula.transl(true, check));
            } else {
//                return new Universal(group, subFormula.transl(false, check));
                return new Not(new Existential(group, subFormula.transl(true, true)));
//                return new Existential(group, subFormula.transl(false, check));
            }
        }

        @Override
        public Existential clone() {
            return new Existential(group, subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            return subFormula.getClosure();
        }

        @Override
        public ATL innermostFormula() {
            if(subFormula.innermostFormula() == null) {
                return this;
            } else {
                return subFormula.innermostFormula();
            }
        }

        @Override
        public ATL updateInnermostFormula(String atom) {
            if(subFormula.innermostFormula() == null) {
                return new Atom(atom);
            } else {
                return new Existential(group, subFormula.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new Existential(this.group, this.subFormula.normalForm(true));
            } else {
                return new Universal(this.group, this.subFormula.normalForm(false));
            }
        }
    }

    public static class Universal extends ATL {
        private String group;
        private ATL subFormula;

        public Universal(String group, ATL subFormula) {
            this.group = group;
            this.subFormula = subFormula;
        }

        @Override
        public String toString() { return "!<" + group + ">!" + this.subFormula.toString(); }//(new Not(subFormula).normalForm(true)).toString(); }

        public ATL getSubFormula() {
            return subFormula;
        }

        public void setSubFormula(ATL subFormula) {
            this.subFormula = subFormula;
        }

        public String getGroup() { return group; }

        @Override
        public boolean isLTL() {
            return false;
        }

        @Override
        public List<String> getTerms() {
            return subFormula.getTerms();
        }

        @Override
        public ATL transl(boolean v, boolean check) {
            if(v) {
//                return new Universal(group, subFormula.transl(true, check));
//                return new Not(new Existential(group, subFormula.transl(true, true)));
                return new Existential(group, subFormula.transl(false, true));
            } else {
                return new Existential(group, subFormula.transl(false, check));
            }
        }

        @Override
        public Universal clone() {
            return new Universal(group, subFormula.clone());
        }

        @Override
        public Set<ATL> getClosure() {
            return subFormula.getClosure();
        }

        @Override
        public ATL innermostFormula() {
            if(subFormula.innermostFormula() == null) {
                return this;
            } else {
                return subFormula.innermostFormula();
            }
        }

        @Override
        public ATL updateInnermostFormula(String atom) {
            if(subFormula.innermostFormula() == null) {
                return new Atom(atom);
            } else {
                return new Universal(group, subFormula.updateInnermostFormula(atom));
            }
        }

        @Override
        public ATL normalForm(boolean v) {
            if(v) {
                return new Universal(this.group, this.subFormula.normalForm(true));
            } else {
                return new Existential(this.group, this.subFormula.normalForm(false));
            }
        }
    }

}
