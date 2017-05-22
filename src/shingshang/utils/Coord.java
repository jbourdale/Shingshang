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
package shingshang.utils;

import java.io.Serializable;

/**
 * Classe Coord représentant des coordonées 2D (x et y)
 * @author jules et hugo
 */
public class Coord implements Serializable {
    
    private int x;
    private int y;

    /**
     * Constructeur par défaut, initialisation à 0,0. 
     */
    public Coord(){
        this.x=0;
        this.y=0;
    }
    
    /**
     * Constrcuteur, initialise objet coord à x;y.
     * @param x Abscisse.
     * @param y Ordonnée.
     */
    public Coord(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    
    /**
     * Getter de l'attribut x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Setter de l'attribut x
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter de l'attribut y
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter de l'attribut y
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(Object a){
        if (this==a) return true;
        if (a==null) return false;
        if (a.getClass() !=this.getClass()) return false;
        else {
            Coord an = (Coord)a;
            return ((this.getX()==an.getX()) && (this.getY()== an.getY()));
        }
    }
    
    /**
     * Methode clone renvoyant un objet Coord o correspond à celui-ci.
     * @return o
     */
    @Override
    public Coord clone(){
        Coord o = new Coord();
        o.setX(this.getX());
        o.setY(this.getY());
        return o;
    }
    
    @Override
    public String toString(){
        return "Coord : ("+getX()+";"+getY()+")";
    }
    
    
    /**
     * Méthode renvoyant true si les coordonnées sont atteignables par un pion, sinon false.
     * @return true/false
     */
    public boolean isInEdge(){
        if(this.getX()>=0 && this.getX()<10 && this.getY()>=0 && this.getY()<10){
            return !((this.getX()<4 || this.getX() >5) && (this.getY()==0 || this.getY()==9));
        }
        return false;
    }
    
    /**
     * Méthode renvoyant un objet coord correspondant aux coordonées entre cet objet et celui passé en paramètre.
     * @param c Coordonnées de la 2ème cases.
     * @return Coord
     */
    public Coord getMilieu(Coord c){
        Coord cMilieu = new Coord();
        cMilieu.setX((this.getX() + c.getX())/2);
        cMilieu.setY((this.getY() + c.getY())/2);
        return cMilieu;
    }
    
    /**
     * Méthode renvoyant un entier correspondant à la direction prise lors d'un mouvement sous forme du code :
     * 1 2 3
     * 8 X 4
     * 7 6 5.
     * @param coordSaute Coordonnées de la case vers laquelle pointe la direction
     * @return code de retour
     */
    public int calculDirection(Coord coordSaute){
        
        //Fonction qui renvoit la direction du entre le pion de coordonées (xPion;yPion) et celui de coordonées (xPionSauter, yPionSauter) d'après le schéma qui suit
        // 1 2 3
        // 8   4
        // 7 6 5

        int direction = -1;
        Coord cTmp = new Coord();
        cTmp.setX(this.getX() - coordSaute.getX()); // 1 2 3  ceci represente les directions possibles
        cTmp.setY(this.getY() - coordSaute.getY()); // 8   4
                                                    // 7 6 5

        switch (cTmp.getY()) { //savoir quelle est la direction du pion
            case 1 :
                if(cTmp.getX()==1){
                    direction=1; //retourner direction diagonale haut gauche
                }
                if(cTmp.getX()==-1){
                    direction=7;//direction diagonale bas gauche
                }
                if(cTmp.getX()==0){
                    direction=8;//direction gauche
                }
                break;
            case 0 :
                if(cTmp.getX()==1){
                    direction=2;//direction haut
                }
                if(cTmp.getX()==-1){
                    direction=6;//direction bas
                }
                break;
            case -1 :
                if(cTmp.getX()==1){
                    direction=3;//direction diagonale haut droit
                }
                if(cTmp.getX()==0){
                    direction=4;//direction droit
                }
                if(cTmp.getX()==-1){
                    direction=5;//direction diagonale bas droite
                }
                break;
                default : direction=-1; // en cas de probleme de la fonction remettre la direction sur -1
            }
        return direction;    
    }
    
}
