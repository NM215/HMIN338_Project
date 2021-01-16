import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Resolution {

    private List<Formule> clauses = new ArrayList<>();

    /*
     * Formule[] clause1 signifie que chaque formule
     * du tableau sont liés par un v
     * */
    public static List<Formule> res(List<Formule> clause1, List<Formule> clause2){

        List<Formule> resolvant = new ArrayList<>(); //resolvant de la résolution

        HashMap<Formule.Proposition, Boolean> c1 = getPropNeg(clause1);
        HashMap<Formule.Proposition, Boolean> c2 = getPropNeg(clause2);

        for(Formule.Proposition p1 : c1.keySet()) {
            for (Formule.Proposition p2 : c2.keySet()) {

                if (p1.getNom()==p2.getNom()) {
                    //Si on trouve deux Prédicats P(...) et ~P(...):
                    if (c1.get(p1) == true && c2.get(p2) == false || c1.get(p1) == false && c2.get(p2) == true) {
                        //Si ils sont égaux par leurs termes
                        if (p1.equals(p2)) {
                            //Alors on les retire de leur clause respectives ...
                            if(c1.get(p1)){ // si p1 possède une négation
                                clause1.remove(new Formule.Non(p1));
                            }else {
                                clause1.remove(p1);
                            }
                            if(c2.get(p2)){ // si p2 possède une négation
                                clause2.remove(new Formule.Non(p2));
                            }else {
                                clause2.remove(p2);
                            }
                            // ... et on regroupe les deux clauses
                            for (Formule f : clause1){
                                resolvant.add(f);
                            }
                            for (Formule f : clause2){
                                resolvant.add(f);
                            }
                        }else { // Sinon on cherche une substitution
                            Substitution sub = Unification.unifier(p1.getT(), p2.getT());
                            //Si elle existe on substitue
                            if(!sub.getFail() && !sub.getEmpty()){
                                //Substitution.substituer(sub.getSubstitution(), );
                            }
                        }
                    }
                }
            }
        }

        // On regarde si il existe une proposition dans la première clause qui soit la négation
        // D'une proposition dans la seconde clause
        // Si c'est le cas on fait la resolution sur le resolvant
        // Sinon on cherche une substitution qui permette de faire la résolution
        for(Formule p1 : clause1){
            for (Formule p2 : clause2){

                //On a créé une méthode dans Formule seulement pour Non et Proposition : public boolean isOpposite(Formule f){}
                // public boolean isOpposite(Formule f){} et on teste
                // if(p1.isOpposite(p2))
                if (p1.isOpposite(p2)){ //Si on trouve deux Prédicats P(...) et ~P(...):
                    //Si ils sont aussi égaux par leur éléments
                    //Alors on les retire de leur clause respectives et on regroupe les deux clauses

                    //Sinon on va chercher une substitution (unification)
                    //Si elle existe on substitue
                }
            }
        }

        return resolvant;
    }

    public static List<Formule> removeFromList(List<Formule> list, Formule f){
        List<Formule> res = new ArrayList<>();
        for (Formule form : list){
            if (!form.equals(f)){
                res.add(form);
            }
        }
        return res;
    }

    public static HashMap<Formule.Proposition, Boolean> getPropNeg(List<Formule> l){
        HashMap<Formule.Proposition, Boolean> res = new HashMap<>();
        for (Formule f : l){
            for (Formule.Proposition i : f.getPropositionNegation(0).keySet()) {
                res.put(i, f.getPropositionNegation(0).get(i));
            }
        }
        return res;
    }

    public static void main(String[] args) {

        List<Formule> clause1 = new ArrayList<>();
        clause1.add(new Formule.Proposition("P", new Terme[]{new Terme.Variable("X")}));

        List<Formule> clause2 = new ArrayList<>();
        clause2.add(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Fonction('a')})));
        clause2.add(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Fonction('b')})));

        HashMap<Formule.Proposition, Boolean> res = getPropNeg(clause2);
        System.out.println("HashMap PropNeg : "+res.toString());

        HashMap<Formule.Proposition, Boolean> res2 = getPropNeg(clause1);
        System.out.println("HashMap PropNeg : "+res2.toString());

        //List<Formule> resolvant = res(clause1, clause2);

    }


}
