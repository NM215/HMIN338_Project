import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unification {

    static int counter = 0;
    static HashMap<Terme, Terme.Variable> substitutions = new HashMap<Terme,Terme.Variable>();

    /*
    Pour l'unification nous avons utilisé l'agorithme suivant : http://www.cs.trincoll.edu/~ram/cpsc352/notes/unification.html
     */

    //Méthode pour l'unification
    public static Substitution unifier(Terme[] terme1, Terme[] terme2){

        Substitution sub = new Substitution();

        if(terme1[0] instanceof Terme.Fonction && terme2[0] instanceof Terme.Fonction && counter == 1) {
            // On teste si terme1 et terme2 sont des constantes
            if (((Terme.Fonction) terme1[0]).getParametres().isEmpty() && ((Terme.Fonction) terme2[0]).getParametres().isEmpty()) {
                System.out.println("Cas où terme1 et terme2 sont des constantes");
                if (((Terme.Fonction) terme1[0]).getNom() == ((Terme.Fonction) terme2[0]).getNom()) {
                    System.out.println("Cas où terme1 et terme2 sont des constantes et égaux");
                    sub.setEmpty(true);
                    return sub;
                }else {
                    sub.setFail(true);
                    return sub;
                }

            }

        } else if(terme1[0] instanceof Terme.Variable && counter == 1){ //Si terme1 est une variable

            if(terme2[0].contains((Terme.Variable)terme1[0])){ // Si terme1 existe dans terme2 on return FAIL
                System.out.println("Si terme1 existe dans terme2 on return FAIL");
                sub.setFail(true);
                return sub;
            }else { // sinon on return {terme2/terme1}
                substitutions.put(terme2[0], (Terme.Variable) terme1[0]);
                sub.setSubstitution(substitutions);
                return sub;
            }

        }else if(terme2[0] instanceof Terme.Variable && counter == 1){ //Si t2 est une variable

            if(terme1[0].contains((Terme.Variable)terme2[0])){ // Si terme2 existe dans terme1 on return FAIL
                System.out.println("Si terme2 existe dans terme1 on return FAIL");
                sub.setFail(true);
                return sub;
            }else { // sinon on return {terme1/terme2}
                substitutions.put(terme1[0], (Terme.Variable) terme2[0]);
                sub.setSubstitution(substitutions);
                return sub;
            }

        } else if (terme1[0] == null || terme2[0] == null){

            sub.setFail(true);
            return sub;

        } else {

            Terme[] HE1 = new Terme[1];
            Terme[] HE2 = new Terme[1];
            for (int j = 0; j < 1; j++)
            {
                HE1[j] = terme1[j];
                HE2[j] = terme2[j];
            }

            counter = 1;

            Substitution SUBS1 = new Substitution();
            Substitution SUBS2 = new Substitution();
            Terme[] TE1;
            Terme[] TE2;

            // On enlève HE1 et HE2 de E1 et E2
            Terme[] E1 = null;
            Terme[] E2 = null;
            if (terme1.length>1){
                E1 = new Terme[terme1.length-1];
                E2 = new Terme[terme2.length-1];
                for (int j = 1; j < terme1.length; j++)
                {
                    for (int i = 0; i<terme1.length-1; i++){
                        E1[i] = terme1[j];
                        E2[i] = terme2[j];
                    }
                }
            }

            // SUBS1:= unify(HE1,HE2);
            SUBS1 = unifier(HE1,HE2);
            // Si SUBS1:=FAIL alors return FAIL;
            if(SUBS1.getFail()) {
                System.out.println("FAIL : " + SUBS1.toString());
                return SUBS1;
            }
            if(SUBS1.getEmpty()){
                System.out.println("EMPTY : " + SUBS1.toString());
                counter = 0;
            }

            if (E1!=null && E2!=null) { // il y'a encore des elements à traiter
                // TE1:= apply(SUBS1, rest of E1);
                TE1 = replaceAll(E1, SUBS1.getSubstitution());
                // TE2:= apply(SUBS1, rest of E2);
                TE2 = replaceAll(E2, SUBS1.getSubstitution());

                // SUBS2:= unify(TE1,TE2);
                SUBS2 = unifier(TE1, TE2);

                // Si SUBS2=FAIL alors return FAIL
                if (SUBS2.getFail()) {
                    System.out.println("FAIL : " + SUBS2.toString());
                    return SUBS2;
                } else {  // sinon return composition(SUBS1,SUBS2)
                    sub.setSubstitution(substitutions);
                    System.out.println(sub.toString());
                }

            }else{ // sinon on a terminé on renvoie notre substitution
                sub.setSubstitution(substitutions);
                System.out.println(sub.toString());
            }

        }

        return sub;
    }

    public static Terme[] replaceAll(Terme[] t, HashMap<Terme, Terme.Variable> subs){

        for (Terme terme : t){
            Substitution.substituer(subs, terme);
        }
        return t;
    }

    //Main

    public static void main(String[] args) {

        //E1 : p(X,a)
        //E2 : p(b,Y)

        System.out.println();
        System.out.println("-----------------------------p(X,a) & p(b,Y)----------------------------");
        System.out.println();

        Formule.Proposition P1 = new Formule.Proposition("P", new Terme[]{new Terme.Variable("X"),new Terme.Fonction('a')});
        Formule.Proposition P2 = new Formule.Proposition("P", new Terme[]{new Terme.Fonction('b'),new Terme.Variable("Y")});

        Substitution sub = new Substitution();

        sub = unifier(P1.getT(), P2.getT());


        //E1 : P(g(X))
        //E2 : P(Y)

        System.out.println();
        System.out.println("----------------------------P(g(X)) & P(Y)-----------------------------");
        System.out.println();

        //Pour remettre à zero le HashMap sinon tout s'accumule
        counter = 0;
        substitutions = new HashMap<Terme,Terme.Variable>();

        List<Terme.Variable> param = new ArrayList<>();
        param.add(new Terme.Variable("X"));

        Formule.Proposition P3 = new Formule.Proposition("P", new Terme[]{new Terme.Fonction('g', param)});
        Formule.Proposition P4 = new Formule.Proposition("P", new Terme[]{new Terme.Variable("Y")});

        Substitution sub1 = new Substitution();
        sub1 = unifier(P3.getT(), P4.getT());


        //E1 : P(a)
        //E2 : P(b)

        System.out.println();
        System.out.println("-----------------------------P(a) & P(b)----------------------------");
        System.out.println();

        //Pour remettre à zero le HashMap et counter sinon tout s'accumule
        counter = 0;
        substitutions = new HashMap<Terme,Terme.Variable>();

        Formule.Proposition P5 = new Formule.Proposition("P", new Terme[]{new Terme.Fonction('a')});
        Formule.Proposition P6 = new Formule.Proposition("P", new Terme[]{new Terme.Fonction('b')});

        Substitution sub2 = new Substitution();
        sub2 = unifier(P5.getT(), P6.getT());


        //E1 : P(g(X),X)
        //E2 : P(Y,a)

        System.out.println();
        System.out.println("-----------------------------P(g(X),X) & P(Y,a)----------------------------");
        System.out.println();

        //Pour remettre à zero le HashMap et counter sinon tout s'accumule
        counter = 0;
        substitutions = new HashMap<Terme,Terme.Variable>();

        Formule.Proposition P7 = new Formule.Proposition("P", new Terme[]{new Terme.Fonction('g', param), new Terme.Variable("X")});
        Formule.Proposition P8 = new Formule.Proposition("P", new Terme[]{new Terme.Variable("Y"), new Terme.Fonction('a')});

        Substitution sub3 = new Substitution();
        sub3 = unifier(P7.getT(), P8.getT());


    }
}
