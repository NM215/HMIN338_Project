import java.util.ArrayList;
import java.util.List;

public class Resolution {

    private List<Formule> clauses = new ArrayList<>();

    /*
    * Formule[] clause1 signifie que chaque formule
    * du tableau sont liés par un v
    * */
    public Formule[] res(Formule[] clause1, Formule[] clause2){

        Formule[] resolvant = new Formule[0]; //resolvant de la résolution

        // On regarde si il existe une proposition dans la première clause qui soit la négation
        // D'une proposition dans la seconde clause
            // Si c'est le cas on fait la resolution sur le resolvant
            // Sinon on cherche une substitution qui permette de faire la résolution
        for(Formule p1 : clause1){
            for (Formule p2 : clause2){

                //On a créé une méthode dans Formule pour le Non et Proposition : public boolean isOpposite(Formule f){}
                    // public boolean isOpposite(Formule f){} et on teste
                    // if(p1.isOpposite(p2))

                //Si on trouve deux Prédicats P(...) et ~P(...):
                //Si ils sont aussi égaux par leur éléments
                //Alors on les retire de leur clause respectives et on regroupe les deux clauses

                //Sinon on va chercher une substitution (unification)
                //Si elle existe on substitue

            }
        }


        return resolvant;
    }

    public List removeFromList(List l){
        return l;
    }

    public void factPos(){

    }

    public void factNeg(){

    }

}
