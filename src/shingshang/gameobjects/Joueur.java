/*
 * Copyright 2016 Hugo Da Roit - Bourdalé Jules.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shingshang.gameobjects;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class decrivant les joueurs du Shingshang.
 * @author jules et hugo
 */
public class Joueur implements Serializable {

    
    private final LinkedList<Pion> listePion;
    private static int nbJoueurs=0;
    private final int idJoueur;
    private String nom;

    /**
     * Constructeur de la classe Joueur, initalise son id, ses pions etc..
     */
    public Joueur() {
        this.listePion = new LinkedList<>(); // http://stackoverflow.com/questions/4166966/what-is-the-point-of-the-diamond-operator-in-java-7
        this.idJoueur = Joueur.nbJoueurs;
        Joueur.nbJoueurs++;
        this.initListePion();
    }

    /**
     * getter de la liste contenant les pions du joueur
     * @return Liste de pion du joueur
     */
    public LinkedList<Pion> getListePion() {
        return listePion;
    }

    /**
     * getter de l'ID du joueur
     * @return ID du joueur
     */
    public int getIdJoueur() {
        return idJoueur;
    } 
    
    /**
     * Méthode responsable de l'intialisation des pions, de leur positions etc.
     */
    private void initListePion(){
        
        /*
        Initialisation d'un tableau qui contiendra toutes les coordonnées par défaut des pions 
        classé dans l'ordre : Dragon, Lion, Singes    
        */
        this.listePion.clear();
        int t[][] = new int[12][2];
        if (this.getIdJoueur()==0){
            int t0[][] = {{0,1},{0,8},{1,8},{0,7},{1,1},{0,2},{2,8},{1,7},{0,6},{2,1},{1,2},{0,3}};
            t=t0;
        }else{
            int t1[][] = {{9,8},{9,1},{8,8},{9,7},{8,1},{9,2},{7,8},{8,7},{9,6},{7,1},{8,2},{9,3}};
            t=t1;
        }
        
        //Parcours du tableau de coordonnées
        for(int i=0; i<12;i++){
            //Si c'est un dragon
            if (i<2)
                this.listePion.add(new Dragon(this.getIdJoueur(), t[i][0], t[i][1]));
            //Si c'est un lion
            else if(i<6)
                this.listePion.add(new Lion(this.getIdJoueur(), t[i][0], t[i][1]));
            //Si c'est un singe
            else if(i<12)
                this.listePion.add(new Singe(this.getIdJoueur(), t[i][0], t[i][1]));
        }
        
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    
    /**
     * @return the nbJoueurs
     */
    public static int getNbJoueurs() {
        return nbJoueurs;
    }

    /**
     * @param aNbJoueurs the nbJoueurs to set
     */
    public static void setNbJoueurs(int aNbJoueurs) {
        nbJoueurs = aNbJoueurs;
    }
}
