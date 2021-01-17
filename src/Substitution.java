import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Substitution {

    private HashMap<Terme, Terme.Variable> substitution;
    private Boolean fail = false;
    private Boolean empty = false;

    public Substitution() {
        this.substitution = new HashMap<Terme,Terme.Variable>();
    }

    public Substitution(HashMap<Terme, Terme.Variable> substitution) {
        this.substitution = substitution;
    }

    // Méthode pour la substitution

    //fonction qui prend cette liste en paramètre et un terme, puis applique la substitution au terme
    public static void substituer(HashMap<Terme, Terme.Variable> sub, Terme terme){

        //Par exemple : [t1/x,t2/y] signifie qu'à x on associe t1 et à y on associe t2
        //Si j'appelle subst cette fonction, subst [t1/x,t2/y] f(x,y) rendra f(t1,t2) (où x et y ont été remplacés par t1 et t2).

        for(Terme.Variable v : terme.getAllVariables()){ //On récupère toutes les variables du Terme à modifier
            for (Terme t : sub.keySet()) { //Pour chaque Terme (key du hashMap)
                if(sub.get(t).equalsVariable(v)){ // Si la valeur de la clé (qui est une variable) = la variable du terme
                    v.setVariable(t.toString()); // On substitu
                    System.out.println();
                    System.out.println("Après substitution : ");
                    System.out.println("Une fois modifié : "+ terme.toString());
                }
            }
        }
    }

    //Accesseurs

    public HashMap<Terme, Terme.Variable> getSubstitution() {
        return this.substitution;
    }

    public void setSubstitution(HashMap<Terme, Terme.Variable> substitution) {
        this.substitution = substitution;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "Substitution{" +
                "substitution=" + substitution +
                ", fail=" + fail +
                ", empty=" + empty +
                '}';
    }

    //Main

    public static void main(String[] args) {

        HashMap<Terme, Terme.Variable> substitution = new HashMap<>();
        Terme terme2 = new Terme.Variable("y");
        Terme.Variable v = new Terme.Variable("x");
        substitution.put(terme2, v);

        char c = 'f';
        List<Terme.Variable> l = new ArrayList<>();
        l.add(new Terme.Variable("x"));
        Terme.Fonction f = new Terme.Fonction(c, l);

        Substitution s = new Substitution(substitution);

        s.substituer(substitution, f);

    }

}
