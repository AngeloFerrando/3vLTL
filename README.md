# 3vLTL

First, download the JAR file from the release section.



To run the example as in the paper, open the terminal and run:

```bash
java -jar 3vLTL.jar -ltl "X a" -alphabet "a" -outcome uu
```

Then, to visualise the automaton so generated:

```bash
dot -Tps automaton.gv -o outfile.ps
```

Then, you can try with any other LTL property. Remember to update the alphabet and the outcome accordingly.
