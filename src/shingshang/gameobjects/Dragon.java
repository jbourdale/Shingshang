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
 * Class héritante de Pion qui gère un pion de type dragon.
 * @author jules et hugo
 */
public class Dragon extends Pion  {
    
    /**
     * Constructeur d'un pion dragon
     * @param joueur joueur possédant ce pion
     * @param x abscisse du pion
     * @param y ordonnée du pion
     */
    public Dragon(int joueur, int x, int y){
        super(2,joueur,x,y);
    }

    /**
     * Réécriture de getDpl provenant de Pion.java afin de ne géré que les déplacements du dragon.
     * @param joueurs listes des deux joueurs
     */
    @Override
    public void getDpl(Joueur[] joueurs) {
        this.getDplPossible().clear();
        this.getSautPossible().clear();
        
        Coord coordTmp = new Coord();
        Pion pionTmp;
        
        //Voir algo de ben
        int[] t = {-1,0,1};
        int i,j;
        
        //Double parcourt du tableau
        for (i=0; i<t.length;i++){
            for (j=0; j<t.length;j++){
                
                //Calcul des coordonnées de la case testé
                coordTmp.setX(this.getCoord().getX() + t[i]);
                coordTmp.setY(this.getCoord().getY() + t[j]);
                //Si on est dans la carte
                if(coordTmp.isInEdge()){
                    //On récupère le pion qu'il y a sur la case (ou pas)
                    pionTmp = isPionHere(coordTmp);
                    //Si la case est occupé, on regarde si on peut sauté
                    if (pionTmp!=null){
                        getSaut(pionTmp, joueurs);    
                    }
                }
                //System.out.println("");
            }
        }
    }

 
    
}
