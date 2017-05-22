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
 * Class héritante de Pion, défini un lion.
 * @author jules et hugo
 */
public class Lion extends Pion  {
    
    /**
     * Constructeur de la classe 
     * @param joueur Joueur possédant le pion
     * @param x Abscisse du pion
     * @param y Ordonnée du pion
     */
    public Lion(int joueur, int x, int y){
        super(1,joueur,x,y);
    }
    
    /**
     * Redéfinition de la méthoden responsable des déplacements de la class Pion afin
     * de correspondre aux règles.
     * @param joueurs Liste des deux joueurs
     */
    @Override
    public void getDpl(Joueur[] joueurs) {
        //On clear les déplacements
        this.getDplPossible().clear();
        this.getSautPossible().clear();
        Coord coordTmp = new Coord();
        Pion pionTmp;
        
        //Voir algo de ben : http://pastebin.com/KbFR1D2A
        int[] t = {-1,0,1};
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
                        //System.out.print("getsaut");
                        getSaut(pionTmp, joueurs);
                        
                    }
                }
                //System.out.println("");
            }
        }
    }


}
