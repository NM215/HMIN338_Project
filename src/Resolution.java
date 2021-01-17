import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Resolution {

    private List<Formule> clauses = new ArrayList<>();

    /*
     * List<Formule> clause1 signifie que chaque formule
     * de l'ArrayList sont liés par un v
     * */
    public static List<Formule> res(List<Formule> clause1, List<Formule> clause2){

        List<Formule> resolvant = new ArrayList<>(); //resolvant de la résolution

        HashMap<Formule.Proposition, Boolean> c1 = getPropNeg(clause1);
        HashMap<Formule.Proposition, Boolean> c2 = getPropNeg(clause2);

        System.out.println("Valeur de clause1 : "+clause1.toString());
        System.out.println("Valeur de clause2 : "+clause2.toString());

        // On regarde si il existe une proposition dans la première clause qui soit la négation
        // D'une proposition dans la seconde clause
            // Si c'est le cas on fait la resolution sur le resolvant
            // Sinon on cherche une substitution qui permette de faire la résolution
        for(Formule.Proposition p1 : c1.keySet()) {
            for (Formule.Proposition p2 : c2.keySet()) {

                if (p1.getNom()==p2.getNom()) {
                    // Si on trouve deux Prédicats P(...) et ~P(...):
                    if (c1.get(p1) == true && c2.get(p2) == false || c1.get(p1) == false && c2.get(p2) == true) {
                        // Si ils sont égaux par leurs termes
                        if (p1.equals(p2)) {
                            // Alors on les retire de leur clause respectives ...
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

                            System.out.println("Nouvelle valeur de clause1 : "+clause1.toString());
                            System.out.println("Nouvelle valeur de clause2 : "+clause2.toString());

                            // ... et on regroupe les deux clauses
                            for (Formule f : clause1){
                                resolvant.add(f);
                            }
                            for (Formule f : clause2){
                                resolvant.add(f);
                            }

                            return resolvant;

                        }else { // Sinon on cherche une substitution
                            Substitution sub = Unification.unifier(p1.getT(), p2.getT());
                            // Si elle existe on retire p1 et p2 de leur clause respectives...
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

                            System.out.println("Nouvelle valeur de clause1 : "+clause1.toString());
                            System.out.println("Nouvelle valeur de clause2 : "+clause2.toString());

                            // ... on substitue sub sur les clauses restantes si besoin ...
                            if(!sub.getFail() && !sub.getEmpty()){
                                // Idée 1 :

                                // On fait un nouveau HashMap sur la nouvelle clause1 (idem pour clause2)
                                HashMap<Formule.Proposition, Boolean> newC1 = getPropNeg(clause1);
                                HashMap<Formule.Proposition, Boolean> newC2 = getPropNeg(clause2);

                                // Pour chaque prédicat de chaque clause on met à jour les termes avec la substitution
                                for(Formule.Proposition newP1 : newC1.keySet()) {
                                    for (Terme t : newP1.getT())
                                        Substitution.substituer(sub.getSubstitution(), t);

                                    // On fait un remove du prédidcat concerné dans clause1 (idem pour clause2)
                                    // et on le remplace par le prédicat substitué
                                    clause1 = new ArrayList<Formule>();
                                    if(newC1.get(newP1)){ // si newP1 possède une négation
                                        clause1.add(new Formule.Non(newP1));
                                    }else {
                                        clause1.add(newP1);
                                    }
                                }
                                for(Formule.Proposition newP2 : newC2.keySet()) {
                                    for (Terme t : newP2.getT())
                                        Substitution.substituer(sub.getSubstitution(), t);

                                    clause2 = new ArrayList<Formule>();
                                    if(newC2.get(newP2)){ // si newP2 possède une négation
                                        clause2.add(new Formule.Non(newP2));
                                    }else {
                                        clause2.add(newP2);
                                    }
                                }

                            }

                            // ... et on regroupe les deux clauses
                            for (Formule f : clause1){
                                resolvant.add(f);
                            }
                            for (Formule f : clause2){
                                resolvant.add(f);
                            }

                            return resolvant;
                        }
                    }
                }
            }
        }
        return resolvant;
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

    public int find(Terme[] array, Terme.Variable value) {
        for(int i=0; i<array.length; i++)
            if(array[i].toString() == value.toString())
                return i;

        return -1;
    }

    public static void main(String[] args) {

        List<Formule> clause1 = new ArrayList<>();
        clause1.add(new Formule.Proposition("P", new Terme[]{new Terme.Variable("X")}));

        List<Formule> clause2 = new ArrayList<>();
        clause2.add(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Fonction('a')})));
        clause2.add(new Formule.Non(new Formule.Proposition("P", new Terme[]{new Terme.Fonction('b')})));

        HashMap<Formule.Proposition, Boolean> res2 = getPropNeg(clause1);
        System.out.println("HashMap PropNeg : "+res2.toString());

        HashMap<Formule.Proposition, Boolean> res = getPropNeg(clause2);
        System.out.println("HashMap PropNeg : "+res.toString());

        List<Formule> resolvant = res(clause1, clause2);
        System.out.println("Resolvant : "+resolvant.toString());

    }


}
