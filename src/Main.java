public class Main {

    /*
        Ce qu'il reste à faire :
            • Substitution -> structure de données + fonction qui va remplacer en fonction de l'unification
            • Unification (Robinson) :
                -> faire sur des termes du premier ordre après des prédicats
                -> fct qui prends deux termes qui s'appelle récursivement sur chacun des sous termes
                -> donne une substitution qui unifie les deux termes
                -> ensuite on prends la substitution et on remplace avec la fonction qui substitue
            • Résolution :
                -> clauses totologiques = chiant du coup on est pas obligé de traiter cette condition
    */

    //Programme principal
    public static void main(String[] args){

        //1-   ∃x.  P(x) => P(a) ∧ P(b)
        //2-   ∀x.  P(x) => ∃y. P(y) v Q(y)
        //3-  (∃x.  P(x) v Q(x)) => (∃x. P(x)) v (∃x. Q(x))
        //4-  (∀x.  P(x)) ∧ (∀x. Q(x)) => ∀x. P(x) ∧ Q(x)
        //5-  (∀x.  P(x) ∧ Q(x)) => (∀x. P(x)) ∧ (∀x. Q(x))
        //6-  (∀x. ¬P(x)) => ¬(∃x. P(x))
        //7- ¬(∀x.  P(x)) => ∃x. ¬P(x)

    }

}
