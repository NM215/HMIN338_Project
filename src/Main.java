import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Formule> skolemiser(Formule F1){

        System.out.println();
        Formule.init();
        Formule F1Skolem = new Formule.Non(F1).skolem();

        System.out.println();
        System.out.println("La formule initiale : "+F1.toString());
        System.out.println("La formule skolemisée : "+F1Skolem.toString());

        Formule F1SkolemFinal = F1.miseEnFormeSkolemisee(F1Skolem, F1.condition, F1.termeLibre, F1Skolem.propositions);
        System.out.println("La formule skolemisée avec quantificateurs : "+ F1SkolemFinal.toString());

        Formule impliqueDel = F1Skolem.impliqueSuppr();
        System.out.println("Élimination du implique : " + impliqueDel.toString());

        Formule nonDel = impliqueDel.nonDev(0);
        System.out.println("Développement du non : " + nonDel.toString());

        List<Formule> CNF = nonDel.CNF();
        System.out.println();
        System.out.println("Les clauses CNF de " + nonDel.toString()+" : ");
        for(Formule f : CNF){
            System.out.println("Clause : " + f.toString());
        }

        return CNF;
    }

    public static String resoudre(List<Formule> clauses) {

        List<List<Formule>> listeClauses = new ArrayList<>();

        for (Formule c : clauses){
            List<Formule> cl1 = c.ouSuppr();
            listeClauses.add(cl1);
        }

        System.out.println("Liste des clauses : "+listeClauses.toString());
        System.out.println();

        String resolution = Resolution.procedureDeResolution(listeClauses);

        return resolution;
    }

    //Programme principal
    public static void main(String[] args){

        //1-   ∃x.  P(x) => P(a) ∧ P(b)
        Formule F1 = new Formule.IlExiste(new Formule.Implique( new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}), new Formule.Et(new Formule.Proposition("P", new Terme[]{new Terme.Variable("a")}), new Formule.Proposition("P", new Terme[]{new Terme.Variable("b")}) )), new Terme.Variable("x"));
        List<Formule> CNF1 = skolemiser(F1);
        System.out.println("D'après la résolution la proposition est : "+resoudre(CNF1));

        //2-   ∀x.  P(x) => ∃y. P(y) v Q(y)
        Formule F2 = new Formule.PourTout(new Formule.Implique(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}), new Formule.IlExiste(new Formule.Ou(new Formule.Proposition("P", new Terme[]{new Terme.Variable("y")}), new Formule.Proposition("Q", new Terme[]{new Terme.Variable("y")})), new Terme.Variable("y"))), new Terme.Variable("x"));
        //List<Formule> CNF2 = skolemiser(F2);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF2));

        //3-  (∃x.  P(x) v Q(x)) => (∃x. P(x)) v (∃x. Q(x))
        Formule F3 = new Formule.Implique( new Formule.IlExiste(new Formule.Ou(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}), new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")})),new Terme.Variable("x")) , new Formule.Ou( new Formule.IlExiste(new Formule.Proposition("Q", new Terme[]{new Terme.Variable("y")}), new Terme.Variable("y")), new Formule.IlExiste(new Formule.Proposition("Q", new Terme[]{new Terme.Variable("y")}), new Terme.Variable("y"))));
        //List<Formule> CNF3 = skolemiser(F3);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF3));

        //4-  (∀x.  P(x)) ∧ (∀x. Q(x)) => ∀x. P(x) ∧ Q(x)
        Formule F4 = new Formule.Implique(new Formule.Et(new Formule.PourTout(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x")), new Formule.PourTout(new Formule.Proposition("Q", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x"))), new Formule.PourTout(new Formule.Et(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Formule.Proposition("Q", new Terme[]{new Terme.Variable("x")})), new Terme.Variable("x")));
        //List<Formule> CNF4 = skolemiser(F4);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF4));

        //5-  (∀x.  P(x) ∧ Q(x)) => (∀x. P(x)) ∧ (∀x. Q(x))
        Formule F5 = new Formule.Implique(new Formule.IlExiste(new Formule.Et(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Formule.Proposition("Q", new Terme[]{new Terme.Variable("x")})),new Terme.Variable("x")), new Formule.Et(new Formule.PourTout(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x")), new Formule.PourTout(new Formule.Proposition("Q", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x"))));
        //List<Formule> CNF5 = skolemiser(F5);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF5));

        //6-  (∀x. ¬P(x)) => ¬(∃x. P(x))
        Formule F6 = new Formule.Implique(new Formule.PourTout(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")})),new Terme.Variable("x")), new Formule.Non(new Formule.IlExiste(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x"))));
        //List<Formule> CNF6 = skolemiser(F6);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF6));

        //7- ¬(∀x.  P(x)) => ∃x. ¬P(x)
        Formule F7 = new Formule.Implique(new Formule.Non(new Formule.PourTout(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")}),new Terme.Variable("x"))), new Formule.IlExiste(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Variable("x")})),new Terme.Variable("x")));
        //List<Formule> CNF7 = skolemiser(F7);
        //System.out.println("D'après la résolution la proposition est : "+resoudre(CNF7));

    }

}
