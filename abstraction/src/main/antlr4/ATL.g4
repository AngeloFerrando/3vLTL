grammar ATL;

atlExpr : ('!' | 'not') child=atlExpr                                  # Negation
     //temporal operators
     | ('next' | 'X') child=atlExpr                                    # Next
     | ('eventually' | 'F') child=atlExpr                              # Eventually
     | ('always' | 'G') child=atlExpr                                  # Always
     | left=atlExpr ('until' | 'U') right=atlExpr                      # Until
     | left=atlExpr ('release' | 'R') right=atlExpr                    # Release
     // boolean operators
     | left=atlExpr ('&&' | 'and') right=atlExpr                       # Conjunction
     | left=atlExpr ('||' | 'or') right=atlExpr                        # Disjunction
     | left=atlExpr ('->' | 'implies') right=atlExpr                   # Implies
     // strategic operators
     | '<' group=ATOM '>' child=atlExpr                                # Existential
     | '[' group=ATOM ']' child=atlExpr                                # Universal
     | '(' atlExpr ')'                                                 # Grouping
     | child=atomExpr                                                  # Evaluation
     ;

atomExpr : ATOM;

ATOM : [_a-z][_a-z0-9]*;

WS        : [ \t\r\n\u000C]+ -> skip;

