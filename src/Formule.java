import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Formule {

    public List<Terme.Variable> termeLibre = new ArrayList<>();
    public HashMap<Terme.Variable, Terme> condition;
    public static List<Proposition> propositions = new ArrayList<>();
    public Formule miseEnForme;
    //Liste de couple Proposition-Boolean pour savoir si une Proposition est précédé directement d'un Non
    public HashMap<Proposition, Boolean> propositionNegation = new HashMap<Proposition, Boolean>();

    //méthodes abstraites
    @Override
    public abstract String toString();
    public abstract Formule skolem();
    public abstract Formule herbrand();
    public abstract Formule impliqueSuppr();
    public abstract Formule nonDev(int i);
    public abstract List<Formule> CNF();

    public abstract List<Formule> ouSuppr();
    public abstract Proposition getProposition();
    public abstract HashMap<Proposition, Boolean> getPropositionNegation(int i);
    public abstract boolean hasNegation(int i);
    public abstract boolean isOpposite(Formule compared);

    /******************************* NON ******************************************/

    public static class Non extends Formule {

        protected Formule f; // sous-formule

        public Non (Formule f) {
            this.f = f;
        }

        public Formule skolem(){
            System.out.println("Skolémisation avec ¬");
            return new Non(f.herbrand());
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation avec ¬");
            return new Non(f.skolem());
        }

        public String toString(){
            return "¬("+f.toString()+")";
        }

        @Override
        public Formule impliqueSuppr() {
            return new Non(f.impliqueSuppr());
        }

        @Override
        public Formule nonDev(int i) {
            if(i==2){
                return f.nonDev(0);
            }
            return f.nonDev(i+1);
            // On compte le nombre de non précédents :
                // If i = 1 ----> Il y'avait un NON avant ---> le et -> ou et iversement
                        //  + A l'étape suivante :
                        //  si f1.nonDev(i+1) --> si le 2 est atteint ---> f1.nonDev(0)
                        //  Sinon on fait new Non(f1.nonDev(0))
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            return null;
        }

        //On va l'utiliser qu'une fois qu'on aura fait la mise sous forme clausale
        //Si on a un non on sait qu'une proposition va suivre vu qu'on a développé le non avant
        @Override
        public boolean hasNegation(int i) {
            return f.hasNegation(1);
        }

        @Override
        public boolean isOpposite(Formule compared) {
            boolean b = f.hasNegation(1); //On met à jour le hasNegation
            return f.isOpposite(compared);
        }

        @Override
        public Proposition getProposition() {
            return f.getProposition();
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return f.getPropositionNegation(1);
        }
    }

    /******************************** PourTout *****************************************/

    public static class PourTout extends Formule {

        protected Formule f; // sous-formule
        protected Terme.Variable t; //Variable sur laquelle porte le quantificateur

        public PourTout (Formule f, Terme.Variable t) {
            this.f = f;
            this.t = t;
        }

        public Formule skolem(){
            System.out.println("Skolémisation avec ∀");
            termeLibre.add(t);
            return f.skolem();
        }

        public Formule herbrand(){ //---------------->Forme particulière
            System.out.println("Herbrandisation avec ∀");

            // Si on a une variable libre x a disposition dans la propposition concerné par y : s(∀y. ...)[f(x)/y]
            // Sinon on fait rentrer une contante : s(∀y ...)[cste/y]
            if(Libre()){
                System.out.println("PourTout : termeLibre non vide");
                //condition = new Condition(new Terme.Fonction('f', termeLibre), t);
                condition = new HashMap<>();
                Terme terme2 = new Terme.Fonction('f', termeLibre);
                Terme.Variable v = t;
                condition.put(v,terme2);
                return f.herbrand();
            }else {
                System.out.println("PourTout : termeLibre vide");
                //condition = new Condition(new Terme.Fonction('f', termeLibre), t);
                condition = new HashMap<>();
                Terme terme2 = new Terme.Fonction('f', termeLibre);
                Terme.Variable v = t;
                condition.put(v,terme2);
                return f.herbrand();
            }
        }

        public boolean Libre(){ //On vérifie s'il exite une variable libre dans la proposition contenant le terme du quantificateur
            for (Proposition p : propositions){
                List<Terme> list = Arrays.asList(p.getT());
                if(list.contains(t)){
                    for (Terme tl : list){
                        if (termeLibre.contains(tl)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public String toString(){
            return "∀"+t.toString()+"."+f.toString();
        }

        @Override
        public Formule impliqueSuppr() {
            return null;
        }

        @Override
        public Formule nonDev(int i) {
            return null;
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            return null;
        }

        @Override
        public Proposition getProposition() {
            return null;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return null;
        }

        @Override
        public boolean hasNegation(int i) {
            return false;
        }

        @Override
        public boolean isOpposite(Formule compared) {
            return false;
        }
    }

    /******************************** IlExiste *****************************************/

    public static class IlExiste extends Formule {

        protected Formule f; // sous-formule
        protected Terme.Variable t; //Variable sur laquelle porte le quantificateur

        public IlExiste (Formule f, Terme.Variable t) {
            this.f = f;
            this.t = t;
        }

        public Formule skolem(){ //---------------->Forme particulière
            System.out.println("Skolémisation avec ∃");
            if(Libre()){
                System.out.println("IlExiste : termeLibre non vide");
                //condition = new Condition(new Terme.Fonction('f', termeLibre), t);
                condition = new HashMap<>();
                Terme terme2 = new Terme.Fonction('f', termeLibre);
                Terme.Variable v = t;
                condition.put(v,terme2);
                return f.skolem();
            }else {
                System.out.println("IlExiste : termeLibre vide");
                //condition = new Condition(new Terme.Fonction('f', termeLibre), t);
                condition = new HashMap<>();
                Terme terme2 = new Terme.Fonction('f', termeLibre);
                Terme.Variable v = t;
                condition.put(v,terme2);
                return f.skolem();
            }
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation avec ∃");
            termeLibre.add(t);
            return f.herbrand();
        }

        public String toString(){
            return "∃"+t.toString()+"."+f.toString();
        }

        @Override
        public Formule impliqueSuppr() {
            return null;
        }

        @Override
        public Formule nonDev(int i) {
            return null;
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            return null;
        }

        @Override
        public Proposition getProposition() {
            return null;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return null;
        }

        @Override
        public boolean hasNegation(int i) {
            return false;
        }

        @Override
        public boolean isOpposite(Formule compared) {
            return false;
        }

        public boolean Libre(){ //On vérifie s'il exite une variable libre dans la proposition contenant le terme du quantificateur
            for (Proposition p : propositions){
                List<Terme> list = Arrays.asList(p.getT());
                if(list.contains(t)){
                    for (Terme tl : list){
                        if (termeLibre.contains(tl)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /********************************** Et ***************************************/

    public static class Et extends Formule {

        protected Formule f1; // sous-formule gauche
        protected Formule f2; // sous-formule droite

        public Et (Formule f1, Formule f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public Formule skolem(){
            System.out.println("Skolémisation avec ∧");
            return new Et(f1.skolem(), f2.skolem());
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation avec ∧");
            return new Et(f1.herbrand(), f2.herbrand());
        }

        public String toString(){
            return f1.toString()+"∧"+f2.toString();
        }

        @Override
        public Formule impliqueSuppr() {
            return new Et(f1.impliqueSuppr(), f2.impliqueSuppr());
        }

        @Override
        public Formule nonDev(int i) {
            if(i==1){
                return new Ou(f1.nonDev(1), f2.nonDev(1));
            }else if (i==2){
                return new Et(f1.nonDev(0), f2.nonDev(0));
            }else return null;
        }

        @Override
        public List<Formule> CNF() {
            List<Formule> res = new ArrayList<>();
            res.add(f1);
            res.add(f2);
            return res;
        }

        @Override
        public List<Formule> ouSuppr() {
            return null;
        }

        @Override
        public Proposition getProposition() {
            return null;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return null;
        }

        @Override
        public boolean hasNegation(int i) {
            return false;
        }

        @Override
        public boolean isOpposite(Formule compared) {
            return false;
        }

    }

    /********************************** Ou ***************************************/

    public static class Ou extends Formule {

        protected Formule f1; // sous-formule gauche
        protected Formule f2; // sous-formule droite

        public Ou (Formule f1, Formule f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public Formule skolem(){
            System.out.println("Skolémisation avec v");
            return new Ou(f1.skolem(), f2.skolem());
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation avec v");
            return new Ou(f1.herbrand(), f2.herbrand());
        }

        public String toString(){
            return "("+f1.toString()+"v"+f2.toString()+")";
        }

        @Override
        public Formule impliqueSuppr() {
            return new Ou(f1.impliqueSuppr(), f2.impliqueSuppr());
        }

        @Override
        public Formule nonDev(int i) {
            if(i==1){
                return new Et(f1.nonDev(1), f2.nonDev(1));
            }else if (i==2){
                return new Ou(f1.nonDev(0), f2.nonDev(0));
            } else return null;
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            List<Formule> res = new ArrayList<>();
            res.add(f1);
            res.add(f2);
            return res;
        }

        @Override
        public Proposition getProposition() {
            return null;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return null;
        }

        @Override
        public boolean hasNegation(int i) {
            return false;
        }

        @Override
        public boolean isOpposite(Formule compared) {
            return false;
        }

    }

    /******************************** Implique *****************************************/

    public static class Implique extends Formule {

        protected Formule f1; // sous-formule gauche
        protected Formule f2; // sous-formule droite

        public Implique (Formule f1, Formule f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public Formule skolem(){
            System.out.println("Skolémisation avec =>");
            //miseEnForme = miseEnFormeSkolemisee(new Implique(f1.herbrand(), f2.skolem()),c,termesLibres);
            return new Implique(f1.herbrand(), f2.skolem());
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation avec =>");
            return new Implique(f1.skolem(), f2.herbrand());
        }

        public String toString(){
            return f1.toString()+"=>"+f2.toString();
        }

        @Override
        public Formule impliqueSuppr() {
            return new Ou(new Non(f1), f2);
        }

        @Override
        public Formule nonDev(int i) {
            return null;
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            return null;
        }

        @Override
        public Proposition getProposition() {
            return null;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            return null;
        }

        @Override
        public boolean hasNegation(int i) {
            return false;
        }

        @Override
        public boolean isOpposite(Formule compared){
            return false;
        }
    }

    /********************************** Proposition ***************************************/

    public static class Proposition extends Formule {

        protected String nom; // nom de la proposition
        protected Terme[] t;
        protected boolean hasNegation;

        public Proposition (String nom, Terme[] t) {
            this.nom = nom;
            this.t = t;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public Terme[] getT() {
            return t;
        }

        public void setT(Terme[] t) {
            this.t = t;
        }

        public void setHasNegation(boolean hasNegation) {
            this.hasNegation = hasNegation;
        }

        public Formule skolem(){
            System.out.println("Skolémisation si atomique");
            this.propositions.add(this);
            return this;
        }

        public Formule herbrand(){
            System.out.println("Herbrandisation si atomique");
            this.propositions.add(this);
            return this;
        }

        public String toString(){
            String termes = null;
            for(Terme terme : t){
                if(termes==null){
                    termes = terme.toString();
                }else {
                    termes = termes + " " +terme.toString();
                }
            }
            return nom+"("+termes+")";
        }

        public boolean equals(Proposition p){
            if(this.getNom()==p.getNom()){
                if(this.getT().length==p.getT().length){
                    for (int i=0; i<this.getT().length; i++){
                        if (!this.getT()[i].equals(p.getT()[i])){
                            return false;
                        }
                    }
                }else {
                    return false;
                }
            }else {
                return false;
            }
            return true;
        }

        @Override
        public Formule impliqueSuppr() {
            return this;
        }

        @Override
        public Formule nonDev(int i) {
            if(i==1){
                return new Non(this);
            }else if (i==2){
                return this;
            }else return null;
        }

        @Override
        public List<Formule> CNF() {
            return null;
        }

        @Override
        public List<Formule> ouSuppr() {
            List<Formule> l = new ArrayList<>();
            l.add(this);
            return l;
        }

        @Override
        public Proposition getProposition() {
            return this;
        }

        @Override
        public HashMap<Proposition, Boolean> getPropositionNegation(int i) {
            if(i==0){ //Pas de négation précedemment
                propositionNegation.put(this, false);
            }else if(i==1){ //Précédé d'une négation
                propositionNegation.put(this, true);
            }else {
                System.out.println("Problème avec la méthode getPropositionNegation(int i)");
            }
            return propositionNegation;
        }

        @Override
        public boolean hasNegation(int i) {
            if (i==1){
                this.hasNegation = true;
            }else {
                this.hasNegation = true;
            }
            return this.hasNegation;
        }

        @Override
        public boolean isOpposite(Formule compared) {
            if(this.hasNegation==true && compared.hasNegation(0)==true){
                //Les deux sont des négations
                return false;
            }else if(this.hasNegation==false && compared.hasNegation(0)==false){
                //Les deux ne sont pas opposés
                return false;
            }else if(this.hasNegation==true && compared.hasNegation(0)==false){
                //Les deux sont opposés
                if (this.getNom()==compared.getProposition().getNom())
                    return true;
                else
                    return false;
            }else if(this.hasNegation==false && compared.hasNegation(0)==true){
                //Les deux sont opposés
                if (this.getNom()==compared.getProposition().getNom())
                    return true;
                else
                    return false;
            }else {
                return false;
            }
        }
    }

    /********************************* Méthodes de FORMULE ***************************************/

    public Formule miseEnFormeSkolemisee(Formule f, HashMap<Terme.Variable, Terme> c, List<Terme.Variable> termesLibres, List<Proposition> propositions){
        Formule f1 = f;
        if(c==null && termesLibres.size()==0){
            //On a terminé ---> on renvoie le calcul c'est à dire f
            return f1;
        }else if(termesLibres.size()>0) {
            //On rajoute les quantificateurs des variables libres
            for (Terme.Variable t : termesLibres) {
                f1 = new PourTout(f1, t);
            }
        }

        //Terme terme2 = new Terme.Fonction('f', termeLibre);
        //Terme.Variable v = t;
        //condition.put(v, terme2);

        if (c!=null) { // Une condition existe on remplace son contenu
            for (Terme.Variable t : c.keySet()) {
                if (c.get(t).getAllVariables().size() == 0) {  // Les fonctions ne possèdant pas de termes (liste vide) serront considérés comme des constantes
                    for (Proposition p : propositions) {
                        List<Terme> list = Arrays.asList(p.getT());
                        for(Terme tv : list){
                            if (tv.toString()==t.toString()) {
                                int index = find(p.getT(), t);
                                if (index == -1) {
                                    System.out.println("Problème au niveau du find !");
                                    break;
                                }
                                p.getT()[index].setTerme("c");
                                System.out.println("miseEnFormeSkolemisee() dans le cas avec constante : "+p.getT()[index]);
                            }
                        }
                    }
                } else { // Si on est dans le cas d'une fonction (pareil sauf qu'on remplace par la fonction)
                    for (Proposition p : propositions) {
                        List<Terme> list = Arrays.asList(p.getT()); //On récupère la liste des variables de chaque prop
                        for (Terme tv : list) { // Pour chq var
                            if (tv.toString() == t.toString()) { // Si elle correspond au terme à remplacer
                                int index = find(p.getT(), t); // On récupère sont index
                                if (index == -1) {
                                    System.out.println("Problème au niveau du find !");
                                    break;
                                }
                                System.out.println("miseEnFormeSkolemisee() dans le cas avec fonction : " + c.get(t).toString());
                                p.getT()[index] = new Terme.Fonction(); //On instancie une fonction à la place du terme
                                p.getT()[index].setTerme(c.get(t).toString()); //On remplace la variable par la fonction
                                System.out.println("miseEnFormeSkolemisee() dans le cas avec fonction après substitution : " + p.getT()[index].toString());
                            }
                        }
                    }
                }
            }
        }
        return f1;
    }

    public int find(Terme[] array, Terme.Variable value) {
        for(int i=0; i<array.length; i++)
            if(array[i].toString() == value.toString())
                return i;

        return -1;
    }

    /********************************* MAIN ***************************************/

    public static void main(String[] args) {

        // 1 - ∃x.P(x)=>P(a)∧P(b)
        Formule F1 = new IlExiste(new Implique( new Proposition("P", new Terme[]{new Terme.Variable("x")}), new Et(new Proposition("P", new Terme[]{new Terme.Variable("a")}), new Proposition("P", new Terme[]{new Terme.Variable("b")}) )), new Terme.Variable("x"));
        Formule F1Skolem = new Non(F1).skolem();
        //Formule F1Skolem = F1.skolem();
        System.out.println();
        System.out.println("La formule initiale : "+F1.toString());
        System.out.println("La formule skolemisée : "+F1Skolem.toString());
        //System.out.println("La condition de la formule : "+F1Skolem.condition.toString()); //null car il n'y a pas de condition dans ce cas
        Formule F1SkolemFinal = F1.miseEnFormeSkolemisee(F1Skolem, F1.condition, F1.termeLibre, F1Skolem.propositions);
        System.out.println("La formule skolemisée avec quantificateurs : "+ F1SkolemFinal.toString());

        Formule impliqueDel = F1Skolem.impliqueSuppr();
        System.out.println("Élimination du implique : " + impliqueDel.toString());

        Formule nonDel = impliqueDel.nonDev(0);
        // A ou (B et C) --> new ou (A , new et (B,C)) ----> NullPointerException
        //Formule nonDel = new Ou(new Proposition("P", new Terme[]{new Terme.Variable("x")}), new Et(new Proposition("P", new Terme[]{new Terme.Variable("a")}), new Proposition("P", new Terme[]{new Terme.Variable("b")})));
        System.out.println("Développement du non : " + nonDel.toString());

        List<Formule> CNF = nonDel.CNF();
        System.out.println();
        System.out.println("Les clauses CNF de " + nonDel.toString()+" : ");
        for(Formule f : CNF){
            System.out.println("Clause : " + f.toString());
        }

        System.out.println();
        for(Formule f : CNF){
            List<Formule> clause = f.ouSuppr();
            for (Formule c : clause)
                System.out.println("Négation : " + c.getPropositionNegation(0).toString());
        }

    }

}
