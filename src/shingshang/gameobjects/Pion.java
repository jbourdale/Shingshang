package shingshang.gameobjects;

import shingshang.utils.Coord;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import shingshang.Shingshang;
import static shingshang.Shingshang.isPionHere;

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

/**
 * Class définissant les comportements communs à tous les pions du jeu,
 * cette classe est spécialisé par Singe.java, Lion.java, Dragon.java 
 * @author jules et hugo
 */
public abstract class Pion implements Serializable {
    protected int type;
    private Coord coord;
    private boolean jouable;
    private boolean mort;
    private int joueur;
    private List dplPossible;
    private List sautPossible;
    private boolean aSauter;
    private int direction;
    protected int lastDirection;
    protected Coord lastCoordAvantSautAllie;
    private boolean inEnchainement;
    
    /**
     * Constructeur par défault d'un pion.
     */
    public Pion() {
        //System.out.println("Création d'un pion !");
        this.coord = new Coord();
    }
    
    /**
     * Constructeur d'un pion avec tous les champs necessaires initialisé.
     * @param type type du pion
     * @param joueur Joueur possédant le pion
     * @param x Abscisse
     * @param y Ordonnée
     */
    public Pion(int type, int joueur, int x, int y){
        this.type = type;
        this.joueur = joueur;
        this.coord = new Coord(x,y);
        this.mort = false;
        this.jouable = true;
        this.dplPossible = new LinkedList();
        this.sautPossible = new LinkedList();
        this.lastCoordAvantSautAllie = new Coord(-1,-1);
        this.lastDirection = -1;
        this.inEnchainement = false;
    }
    
    /**
     * Méthode responsable du remplissage de la liste dplPossible qui correspond à toutes
     * les cases où le pion peut se déplacer, les règles n'étant pas commune à tous les types
     * de pion, cette méthode est abstraite
     * @param joueurs listes des deux joueurs
     */
    public abstract void getDpl(Joueur[] joueurs);
    
    @Override
    public String toString(){
        return "Pion de type : "+getType()+" du joueur : "+getJoueur()+" de coord ("+getCoord().getX()+";"+getCoord().getY()+")";
    }

    /**
     * Méthode renvoit true si le pion est plus grand ou de même taille que celui passé en paramètre,
     * sinon false.
     * @param p Pion à comparer.
     * @return true si oui, false sinon
     */
    public boolean isTallerOrSameThan(Pion p){
        return this.getType() >= p.getType();
    }
    
    /**
     * Méthode renvoyant true si le pion aurait des sauts disponibles si il était selectionné. 
     * @param joueurs listes des deux joueurs
     * @return true si il pourrait sauté, false sinon
     */
    public boolean peutSauter(Joueur[] joueurs){
        this.getDpl(joueurs);
        //System.out.println("Saut possible dans peutSauter : "+this.getSautPossible());
        boolean retour = this.getSautPossible().size()>0;
        this.getSautPossible().clear();
        this.getDplPossible().clear();
        return retour;
    }
    
    /**
     * Méthode responsable du remplissage de la liste sautPossible qui référencie toutes les cases
     * accessible par des sauts depuis les coordonnées du pion. 
     * @param pionSaute pion que l'on saute
     * @param joueurs listes des deux joueurs
     */
    protected void getSaut(Pion pionSaute,Joueur[] joueurs){
        Coord cSaut = new Coord();
        int directionSaut = cSaut.calculDirection(pionSaute.getCoord());
        //Calcul des coordonées où l'on va attérir après avoir sauté la case cMilieu
        cSaut.setX(pionSaute.getCoord().getX() + (pionSaute.getCoord().getX() - this.getCoord().getX()));
        cSaut.setY(pionSaute.getCoord().getY() + (pionSaute.getCoord().getY() - this.getCoord().getY()));
        //Si la case où l'on atterira après le saut est dans les limites et si elle est vide et qu'elle n'est pas celle choisit précédement
 
        if(cSaut.isInEdge() && isPionHere(cSaut)==null && !lastCoordAvantSautAllie.equals(cSaut) && ((directionSaut == lastDirection || lastDirection == -1 || Shingshang.getPionSelectionne().type!=0))){
            //Si le pion choisi est plus grand ou de même taille que le pion sauté
            if(this.isTallerOrSameThan(pionSaute)){
                this.getSautPossible().add(cSaut.clone());
                lastDirection = directionSaut;
            }
        }
    }
    
    /**
     * Méthode déplaçant le pion aux coordonnées passé en paramètres
     * @param coord coordonnées cibles
     */
    public void moveTo(Coord coord){
        this.getCoord().setX(coord.getX());
        this.getCoord().setY(coord.getY());
    }

    /**
     * Methode renvoyant true si le pion est entrain de faire un shingshang (enchainement de saut). 
     * @param joueurs Liste des deux joueurs
     * @return true si shingshang, false sinon
     */
    public boolean enShingshang(Joueur [] joueurs){
        //System.out.println("aSauter : "+this.isaSauter()+" jouable : "+this.isJouable()+" peutSauter : "+this.peutSauter(joueurs));
        return this.isaSauter() && this.isJouable() && this.peutSauter(joueurs);
    };
    
    public static boolean deplacement(Coord coordSelec){
        Pion p = isPionHere(new Coord(coordSelec.getX(),coordSelec.getY()));
       
        //Si on a cliqué sur une case contenant un pion
        if (p!=null) {
            //Ca veut dire qu'on essaye de selectionné un pion
            //Si on selectionne un pion jouable                    
            //Rajouter : Si on a pas dejà un pion entrain de sauté
            /*if (Shingshang.getDernierPionJoue()!=null){
                System.out.println("dernier pion joué : "+Shingshang.getDernierPionJoue());
                System.out.println("En shingshang de ce pion : "+Shingshang.getDernierPionJoue().enShingshang(Shingshang.getJoueurs()));
            }*/
            if (Shingshang.getDernierPionJoue()!=null && (!(Shingshang.getDernierPionJoue().enShingshang(Shingshang.getJoueurs())) || Shingshang.getDernierPionJoue().equals(p))){
                if (p.getJoueur() == Shingshang.getTour()%2 && p.isJouable()){
                    Shingshang.setPionSelectionne(p);
                    Shingshang.getPionSelectionne().getDpl(Shingshang.getJoueurs());
                }
            }else if(Shingshang.getDernierPionJoue() == null){
                if (p.getJoueur() == Shingshang.getTour()%2 && p.isJouable()){
                    Shingshang.setPionSelectionne(p);
                    Shingshang.getPionSelectionne().getDpl(Shingshang.getJoueurs());
                }
            } 
 
        //Si on a cliqué sur une case vide
        }else{
            //System.out.println("Clic sur une case vide, pion selec = "+Shingshang.getPionSelectionne());
            //Et qu'on a un pion de selectionné
            if(Shingshang.getPionSelectionne() != null){
                //Si on a cliqué sur une case qui est surligné en vert
                if(Shingshang.getPionSelectionne().getDplPossible().contains(coordSelec) || Shingshang.getPionSelectionne().getSautPossible().contains(coordSelec)){
               
                    /* ====GLISSEMENT==== */
                    //Si les coordonées de la case cliqué sont dans la liste de déplacement possible du pion selectionné ou dans celle des sauts
                    if(Shingshang.getPionSelectionne().getDplPossible().contains(coordSelec)){
                        //Si le pion n'a pas déjà sauté
                        if(!(Shingshang.getPionSelectionne().isaSauter())){
                            //On le bouge et on note le fait qu'il sera plus jouable ce tour
                            Shingshang.getPionSelectionne().moveTo(coordSelec);
                            Shingshang.getPionSelectionne().setJouable(false);
                        }
 
                    /* ====SAUT==== */
                    }else if(Shingshang.getPionSelectionne().getSautPossible().contains(coordSelec)){
                        //On regarde le pion que l'on saute et on compare son joueur a celui qui joue actuellement
                        Coord cTmp = Shingshang.getPionSelectionne().getCoord().getMilieu(coordSelec);
                        if (isPionHere(cTmp).getJoueur() != Shingshang.getTour()%2) {                            
                            if(Shingshang.getPionSelectionne().enShingshang(Shingshang.getJoueurs())){
                                isPionHere(cTmp).setMort(true);
                            }
                            //Si on saute un ennemi, on ne pourra plus joué avec ce pion ce tour
                            Shingshang.getPionSelectionne().setJouable(false);
                        } else { // AJOUT : Si saute un allié on ne pourra plus revenir sur la case précédente : FAIT
                            Shingshang.getPionSelectionne().setLastCoordAvantSautAllie(Shingshang.getPionSelectionne().getCoord());
                            Shingshang.getPionSelectionne().setInEnchainement(true);
                        }
                        //On calcul la direction grace aux coordonnées du pion selectionné et celles de la case cliqué
                        //Et on enregistre cette direction dans le pion selectionné
                        Coord cMilieu = Shingshang.getPionSelectionne().getCoord().getMilieu(coordSelec);
                        Shingshang.getPionSelectionne().setDirection(Shingshang.getPionSelectionne().getCoord().calculDirection(cMilieu));
                        Shingshang.getPionSelectionne().setaSauter(true);
                        Shingshang.getPionSelectionne().moveTo(coordSelec);
                    }
 
                    /* ====APRES LE MOUVEMENT ==== */
                    if(Shingshang.isGUI()){
                        //On test la fin de tour, si c'est le cas on fini le tour
                        if(Shingshang.isTourEnded())
                            Shingshang.finTour();

                        if(Shingshang.aGagner() != null)
                            Shingshang.getOut().menuFin(Shingshang.aGagner());
                       
                        //On reset les déplacements possibles
                        Shingshang.getPionSelectionne().getDplPossible().clear();
                        Shingshang.getPionSelectionne().getSautPossible().clear();
                        Shingshang.setDernierPionJoue(Shingshang.getPionSelectionne());
                        Shingshang.setPionSelectionne(null);
                    }else{
                        Shingshang.getPionSelectionne().getDplPossible().clear();
                        Shingshang.getPionSelectionne().getSautPossible().clear();
                        Shingshang.setDernierPionJoue(Shingshang.getPionSelectionne());
                    }
                    Shingshang.getOut().afficher();
                    //System.out.println("A BOUGER");
                    //On return le fait que le pion ait bougé
                    return true;
                }
            }
        }
        Shingshang.getOut().afficher();
        //System.out.println("A PAS BOUGER");
        return false;
    }
    
    /* ====== ACCESSEURS ====== */
    
    /**
     * @return Le pion a sauté
     */
    public boolean isaSauter() {
        return aSauter;
    }

    /**
     * @param aSauter set Le pion a sauté
     */
    public void setaSauter(boolean aSauter) {
        this.aSauter = aSauter;
    }

    /**
     * @return jouable
     */
    public boolean isJouable() {
        return jouable;
    }

    /**
     * @param jouable the jouable to set
     */
    public void setJouable(boolean jouable) {
        this.jouable = jouable;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    public void setInEnchainement(boolean bool) {
        inEnchainement = bool;
    }
   
    public boolean isInEnchainement() {
        return inEnchainement;
    }
 
    public void setLastCoordAvantSautAllie(Coord coords) {
        this.lastCoordAvantSautAllie.setX(coords.getX());
        this.lastCoordAvantSautAllie.setY(coords.getY());
    }
   
    public Coord getLastCoordAvantSautAllie() {
        return lastCoordAvantSautAllie;
    }
    
    /**
     * @return the coord
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * @param coord the coord to set
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     * @return the mort
     */
    public boolean isMort() {
        return mort;
    }

    /**
     * @param mort the mort to set
     */
    public void setMort(boolean mort) {
        if(mort == true){
            this.coord.setX(-1);
            this.coord.setY(-1);
        }
        this.mort = mort;
    }

    /**
     * @return the joueur
     */
    public int getJoueur() {
        return joueur;
    }

    /**
     * @param joueur the joueur to set
     */
    public void setJoueur(int joueur) {
        this.joueur = joueur;
    }

    /**
     * @return the dplPossible
     */
    public List getDplPossible() {
        return dplPossible;
    }

    /**
     * @param dplPossible the dplPossible to set
     */
    public void setDplPossible(List dplPossible) {
        this.dplPossible = dplPossible;
    }

    /**
     * @return the sautPossible
     */
    public List getSautPossible() {
        return sautPossible;
    }

    /**
     * @param sautPossible the sautPossible to set
     */
    public void setSautPossible(List sautPossible) {
        this.sautPossible = sautPossible;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setLastDirection(int i) {
        this.lastDirection = i;
    }
    
    public int getLastDirection(){
        return this.lastDirection;
    }
    
}
