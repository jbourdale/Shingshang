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
import shingshang.utils.Coord;
import static shingshang.Shingshang.isPionHere;

/**
 * Class héritante de Pion, défini un singe.
 * @author jules et hugo
 */
public class Singe extends Pion {
    
    /**
     * Constructeur complet d'un singe, appel celui de Pion.java
     * @param joueur Joueur possédant le pion
     * @param x Abscisse
     * @param y Ordonnée
     */
    public Singe(int joueur, int x, int y){
        super(0,joueur,x,y);
    }

    /**
     * Rédéfinition de la méthode getDpl de Pion.java afin de convenir au règles du jeu pour les singes.
     * @param joueurs tableau des deux joueurs
     */
    @Override
    public void getDpl(Joueur[] joueurs) {
        this.getDplPossible().clear();
        this.getSautPossible().clear();
        
        getDplPossibleInRange(1, joueurs);
        getDplPossibleInRange(2, joueurs);
    }
    

    /**
     * Méthode responsable du remplissage de la liste sautPossible qui référencie toutes les cases
     * accessible par des sauts depuis les coordonnées du pion. 
     * @param pionSaute pion que l'on saute
     * @param joueurs listes des deux joueurs
     */
    @Override
    protected void getSaut(Pion pionSaute,Joueur[] joueurs){
        Coord cSaut = new Coord();
        int directionSaut = cSaut.calculDirection(pionSaute.getCoord());
        //Calcul des coordonées où l'on va attérir après avoir sauté la case cMilieu
        cSaut.setX(pionSaute.getCoord().getX() + (pionSaute.getCoord().getX() - this.getCoord().getX()));
        cSaut.setY(pionSaute.getCoord().getY() + (pionSaute.getCoord().getY() - this.getCoord().getY()));

        Coord cMilieu = this.getCoord().getMilieu(cSaut);
        
        //Si la case où l'on atterira après le saut est dans les limites et si elle est vide et qu'elle n'est pas celle choisit précédement
        if(cSaut.isInEdge() && isPionHere(cSaut)==null && !this.lastCoordAvantSautAllie.equals(cSaut)){
            //Si le pion choisi est plus grand ou de même taille que le pion sauté
            if(this.isTallerOrSameThan(pionSaute)){
                
                //System.out.println("Direction actuelle : "+this.getDirection()+" direction calculé : "+this.getCoord().calculDirection(cMilieu));
                if(this.getDirection() == this.getCoord().calculDirection(cMilieu) || this.getDirection() == -1){
                    this.getSautPossible().add(cSaut.clone());
                }
            }
        }
    }
    /**
     * Méthode remplissant les listes de déplacements possibles du pion en fonction de la distance (r) passé en paramètre
     * @param r
     * @param joueurs 
     */
    private void getDplPossibleInRange(int r, Joueur[] joueurs){
        Coord coordTmp = new Coord();
        Pion pionTmp;
        
        //Voir algo de ben
        int[] t = {-1*r,0,1*r};
        int i,j;
        
        //Double parcourt du tableau
        for (i=0; i<t.length;i++){
            for (j=0; j<t.length;j++){
                
                //Calcul des coordonnées de la case testé
                coordTmp.setX(this.getCoord().getX() + t[i]);
                coordTmp.setY(this.getCoord().getY() + t[j]);
                //System.out.print("Coord tmp : "+coordTmp);
                //Si on est dans la carte
                if(coordTmp.isInEdge()){
                    
                    if(r==2){
                        //On récupère le pion (ou pas) situé au milieu entre le pion et les coordonées testées
                        if (isPionHere(this.getCoord().getMilieu(coordTmp))!=null) continue;
                        //System.out.print("Après le continue");
                    }
                    
                    //System.out.print(" Dans isInMap");
                    //On récupère le pion qu'il y a sur la case (ou pas)
                    pionTmp = isPionHere(coordTmp);
                    //Si la case est vide, on l'ajoute au déplacement possible
                    if (pionTmp==null){
                        //System.out.print(" Dans pionTmp == null");
                        //System.out.print(" add : "+coordTmp+" ");
                        if(!(this.isaSauter()))
                            getDplPossible().add(coordTmp.clone());
                    }else{
                        if(r==1){
                            //System.out.print("getsaut");
                            getSaut(pionTmp, joueurs);
                        }
                    }
                }
                //System.out.println("");
            }
        }
    }
}