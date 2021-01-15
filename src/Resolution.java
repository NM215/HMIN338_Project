import java.util.ArrayList;
import java.util.List;

public class Resolution {

    private List<Formule> clauses = new ArrayList<>();

    /*
    * Formule[] clause1 signifie que chaque formule
    * du tableau sont liés par un v
    * */
    public static List<Formule> res(List<Formule> clause1, List<Formule> clause2){

        List<Formule> resolvant = new ArrayList<>(); //resolvant de la résolution

        // On regarde si il existe une proposition dans la première clause qui soit la négation
        // D'une proposition dans la seconde clause
            // Si c'est le cas on fait la resolution sur le resolvant
            // Sinon on cherche une substitution qui permette de faire la résolution
        for(Formule p1 : clause1){
            for (Formule p2 : clause2){

                //On a créé une méthode dans Formule pour le Non et Proposition : public boolean isOpposite(Formule f){}
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

    public List<Formule> removeFromList(List<Formule> list, Formule f){
        List<Formule> res = new ArrayList<>();
        for (Formule form : list){
            if (!form.equals(f)){
                res.add(form);
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

        List<Formule> resolvant = res(clause1, clause2);

    }


}
