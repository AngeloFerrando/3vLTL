# 3vLTL

We present 3vLTL, a tool to generate Büchi automata from  formulas in Linear-time Temporal Logic (LTL), interpreted on a three-valued semantics.
Specifically, given an LTL formula, a set of atomic propositions as alphabet for the automaton, and a truth value, our procedure generates a Büchi automaton that accepts all the words that assign the chosen truth value to the LTL formula. Given the particular type of the output of the tool, it can also be seamlessly processed by third-party libraries in a natural way. That is, the Büchi automaton can then be used in the context of formal verification to check whether an LTL formula is true, false, or undefined on a given model.

To use the tool, first, download the JAR file from the release section.

To run the example as in the paper, open the terminal and run:

```bash
java -jar 3vLTL.jar -ltl "X a" -alphabet "a" -outcome uu
```

Then, to visualise the automaton so generated:

```bash
dot -Tps automaton.gv -o outfile.ps
```

Then, you can try with any other LTL property. Remember to update the alphabet and the outcome accordingly.
