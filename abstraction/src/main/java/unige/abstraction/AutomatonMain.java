package unige.abstraction;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.cli.*;
import unige.parser.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AutomatonMain {

    public static class ThrowingErrorListener extends BaseErrorListener {

        public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
                throws ParseCancellationException {
            throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        Option propertyOpt = new Option("ltl", "ltl", true, "the LTL property");
        propertyOpt.setRequired(true);
        options.addOption(propertyOpt);
        Option alphabetOpt = new Option("a", "alphabet", true, "the alphabet of the property");
        alphabetOpt.setRequired(true);
        options.addOption(alphabetOpt);
        Option outcomeOpt = new Option("o", "outcome", true, "the outcome of the automaton");
        outcomeOpt.setRequired(true);
        options.addOption(outcomeOpt);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String ltl = cmd.getOptionValue("ltl");
            if(cmd.getOptionValue("alphabet").isEmpty()) {
                throw new ParseException("alphabet cannot be empty");
            }
            String[] alphabet = cmd.getOptionValue("alphabet").split(",");
            String outcome = cmd.getOptionValue("outcome");
            Automaton.Outcome realOutcome;
            if(outcome.equals("tt")) {
                realOutcome = Automaton.Outcome.True;
            } else if(outcome.equals("ff")) {
                realOutcome = Automaton.Outcome.False;
            } else {
                realOutcome = Automaton.Outcome.Unknown;
            }
            System.out.println("Parsing LTL property..");
            CharStream codePointCharStream = CharStreams.fromString(ltl);
            ATLLexer lexer = new ATLLexer(codePointCharStream);
            lexer.removeErrorListeners();
            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
            ATLParser atlParser = new ATLParser(new CommonTokenStream(lexer));
            atlParser.removeErrorListeners();
            atlParser.addErrorListener(ThrowingErrorListener.INSTANCE);
            ParseTree tree = atlParser.atlExpr();
            ATLVisitorImpl visitor = new ATLVisitorImpl();
            System.out.println("LTL property successfully parsed");
            ATL property = visitor.visit(tree).normalForm();
            ATL pt = property.transl(true);
            System.out.println("Duplicate atomic propositions: " + pt.toString());
            pt = pt.normalForm();
            System.out.println("NNF: " + pt.toString());
//            ATLVisitorImpl ptVisitor = new ATLVisitorImpl();
//            ptVisitor.visit(new ATLParser(new CommonTokenStream(new ATLLexer(CharStreams.fromString(pt.toString())))).atlExpr());
            Set<String> alphabetSet = new HashSet<>();
            for(String label : alphabet) {
                alphabetSet.add(label + "_tt");
                alphabetSet.add(label + "_ff");
            }
            System.out.println("Alphabet with duplicates: [ " + String.join(", ", alphabetSet) + " ]");
            Automaton automaton = new Automaton(pt, pt.getClosure(), realOutcome, alphabetSet, true);
            FileWriter fw = new FileWriter("automaton.gv");
            fw.write(automaton.toDot());
            fw.close();
            System.out.println("DOT output file generated (automaton.gv)");
            fw = new FileWriter("automaton.hoa");
            fw.write(automaton.toHOA());
            fw.close();
            System.out.println("HOA output file generated (automaton.hoa)");
        } catch (ParseException|RuntimeException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Three-valued Automaton Generator: ", options);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
