package unige.parser;

public class ATLVisitorImpl extends ATLBaseVisitor<ATL> {
//    private Set<ATL> closure = new HashSet<>();

    public ATLVisitorImpl() {
        //closure.add(new LTL.Atom("true"));
    }

//    public Set<ATL> getClosure() {
//        return closure;
//    }

    @Override
    public ATL visitImplies(ATLParser.ImpliesContext ctx) {
        ATL.Implies implies = new ATL.Implies(visit(ctx.left), visit(ctx.right));
        ATL.Not not = new ATL.Not(implies);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(implies);
//        closure.add(not);
//        closure.add(notNot);
        return implies;
    }

    @Override
    public ATL visitNegation(ATLParser.NegationContext ctx) {
        ATL.Not not = new ATL.Not(visit(ctx.getChild(1)));
        ATL.Not notNot = new ATL.Not(not);
//        closure.add(not);
//        closure.add(notNot);
        return not;
    }

    @Override
    public ATL visitNext(ATLParser.NextContext ctx) {
        ATL.Next next = new ATL.Next(visit(ctx.getChild(1)));
        ATL.Not not = new ATL.Not(next);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(next);
//        closure.add(not);
//        closure.add(notNot);
        return next;
    }

    @Override
    public ATL visitEventually(ATLParser.EventuallyContext ctx) {
        ATL subFormula = visit(ctx.getChild(1));
        ATL.Eventually eventually = new ATL.Eventually(subFormula);
//        ATL.Not not = new ATL.Not(eventually);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(new ATL.Until(new ATL.Atom("true"), subFormula));
//        closure.add(new ATL.Not(new ATL.Until(new ATL.Atom("true"), subFormula)));
//        closure.add(notNot);
        return eventually;
    }

    @Override
    public ATL visitConjunction(ATLParser.ConjunctionContext ctx) {
        ATL.And and = new ATL.And(visit(ctx.left), visit(ctx.right));
        ATL.Not not = new ATL.Not(and);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(and);
//        closure.add(not);
//        closure.add(notNot);
        return and;
    }

    @Override
    public ATL visitDisjunction(ATLParser.DisjunctionContext ctx) {
        ATL.Or or = new ATL.Or(visit(ctx.left), visit(ctx.right));
        ATL.Not not = new ATL.Not(or);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(and);
//        closure.add(not);
//        closure.add(notNot);
        return or;
    }

    @Override
    public ATL visitAlways(ATLParser.AlwaysContext ctx) {
        ATL subFormula = visit(ctx.getChild(1));
        ATL.Globally globally = new ATL.Globally(subFormula);
//        ATL.Not not = new ATL.Not(globally);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(new ATL.Not(new ATL.Until(new ATL.Atom("true"), new ATL.Not(subFormula))));
//        closure.add(new ATL.Until(new ATL.Atom("true"), new ATL.Not(subFormula)));
//        closure.add(notNot);
        return globally;
    }

    @Override
    public ATL visitUntil(ATLParser.UntilContext ctx) {
        ATL.Until until = new ATL.Until(visit(ctx.left), visit(ctx.right));
        ATL.Not not = new ATL.Not(until);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(until);
//        closure.add(not);
//        closure.add(notNot);
        return until;
    }

    @Override
    public ATL visitRelease(ATLParser.ReleaseContext ctx) {
        ATL.Release release = new ATL.Release(visit(ctx.left), visit(ctx.right));
        ATL.Not not = new ATL.Not(release);
//        LTL.Not notNot = new LTL.Not(not);
//        closure.add(release);
//        closure.add(not);
//        closure.add(notNot);
        return release;
    }

    @Override
    public ATL visitAtomExpr(ATLParser.AtomExprContext ctx) {
        ATL.Atom atom = new ATL.Atom(ctx.getText());
        ATL.Not notAtom = new ATL.Not(atom);
//        LTL.Not notNotAtom = new LTL.Not(notAtom);
//        closure.add(atom);
//        closure.add(notAtom);
//        closure.add(notNotAtom);
        return atom;
    }

    @Override
    public ATL visitExistential(ATLParser.ExistentialContext ctx) {
        return new ATL.Existential(ctx.group.getText(), visit(ctx.getChild(3)));
    }

    @Override
    public ATL visitUniversal(ATLParser.UniversalContext ctx) {
        return new ATL.Universal(ctx.group.getText(), visit(ctx.getChild(3)));
    }

    @Override
    public ATL visitGrouping(ATLParser.GroupingContext ctx) {
        return visit(ctx.getChild(1));
    }

}
