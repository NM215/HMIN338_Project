# HMIN338_Project

Projet d'implantation de la méthode de résolution au premier ordre réalisé par Nadia MEDOUAZ & Sandra DJAFER pour l'unité d'enseignement HMIN338-Méthodes formelles pour le génie logiciel. Réalisé en Java et avec l'IDE IntelliJ.

# Remarques

- Chaque classe du projet représente une étape décrite dans l'énoncé du projet et pour chaque classe du projet et donc chaque étape nous allons avoir un ou plusieurs exemples qui montrent comment elle permet de répondre à la problématique.
- La classe Main.java reprend chacune des étapes suivantes sur un ensemble de propositions du cours.

# Étapes - Les Classes

### 1 - Terme.java

Définition de la structure de données correspondant aux termes du premier ordre, puis celle correspondant aux propositions du premier ordre.

### 2 - Formule.java

Écriture de la fonction qui skolémise (chaque classe représentant un connecteur ou un quantificateur va avoir une méthode de skolémisation et de herbrandisation de la même façon que spécifié dans le cours) une proposition (elle rend ainsi une proposition universelle). Puis, écriture de la fonction qui met une formule universelle en forme cnf (dans notre cas c'est *CNF()* qui s'en charge). D'autres méthodes ont été définies dans cette classe pour nous aider dans la suite du projet.

### 3 - Substitution.java et Unification.java

Substitution.java : Définition la structure de données pour les substitutions (nous avons aussi écrit une méthode qui exécute la substitution sur un terme). 
Unification.java : Écriture de la fonction qui unifie deux propositions atomiques (elle renvoie une substitution).

### 4 et 5 - Résolution.java

Écriture de la fonction qui implantent la règle d’inférence *res* de la résolution (dans notre cas c'est la méthode *res()*), c’est-à-dire la résolution elle-même.
Implanter l’algorithme de résolution au premier ordre. L’algorithme est exactement le même que
celui vu en cours pour la logique propositionnelle (dans notre cas c'est la méthode *procedureDeResolution()*).

### Main.java

Programme principal avec exemple de résolution sur plusieurs propositions.

