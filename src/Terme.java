import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLetter;

public abstract class Terme {

    //méthodes abstraites
    public abstract List<Variable> getAllVariables();
    public abstract String toString();
    public abstract boolean contains(Variable v);

    public abstract void setTerme(String terme);

    public static class Variable extends Terme {

        private String variable;

        public Variable(){}

        public Variable(String variable){
            this.variable = variable;
        }

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public boolean equalsVariable(Variable v){
            if (this.variable==v.getVariable()){
                return true;
            }
            return false;
        }

        @Override
        public boolean contains(Variable v) {
            return false;
        }

        @Override
        public List<Variable> getAllVariables() {
            List<Variable> l = new ArrayList<Variable>();
            l.add(this);
            return l;
        }

        @Override
        public String toString() {
            return variable;
        }

        @Override
        public void setTerme(String terme) {
            this.variable = terme;
        }
    }

    public static class Fonction extends Terme {

        private char nom;
        // Les fonctions ne possèdant pas de termes (liste vide) serront considérés comme des constantes
        private List<Variable> parametres = new ArrayList<>();

        public Fonction(){}

        public Fonction(char nom, List<Variable> parametres) {
            this.nom = nom;
            this.parametres = parametres;
        }
        public Fonction(char nom) {
            this.nom = nom;
            this.parametres = new ArrayList<>();
        }

        public char getNom() {
            return nom;
        }

        public void setNom(char nom) {
            this.nom = nom;
        }

        public List<Variable> getParametres() {
            return parametres;
        }

        public void setParametres(List<Variable> parametres) {
            this.parametres = parametres;
        }

        public boolean contains(Variable v){
            if (this.parametres.contains(v)){
                return true;
            }else {
                return false;
            }
        }

        public boolean isConstante(){
            if(this.parametres.isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public List<Variable> getAllVariables() {
            return parametres;
        }

        @Override
        public String toString() {
            if (this.parametres.isEmpty()){
                String c = String.valueOf(this.nom);
                return c;
            }else {
                String f = String.valueOf(this.nom)+"(";
                String chaine = null;
                for(Terme.Variable v : this.parametres){
                    if(chaine==null){
                        chaine = v.toString();
                    }else {
                        chaine = chaine + ", " +v.toString();
                    }
                }
                f = f + chaine + ")";
                return f;
            }
        }

        @Override
        public void setTerme(String terme) {

            if(isLetter(terme.charAt(0)) && terme.charAt(1)=='(' && terme.charAt(3)==')' && terme.length()<5){
                this.setNom(terme.charAt(0));
                parametres.add(new Variable(String.valueOf(terme.charAt(2))));
            }else {
                System.out.println("Problème avec setTerme() !");
            }

        }
    }

}
