package shingshang;

import shingshang.gameobjects.Joueur;
import shingshang.gameobjects.Pion;
import shingshang.utils.Sauvegarde;
import shingshang.out.RepartiteurGUIorNOGUI;
import shingshang.utils.ShutdownHook;
import shingshang.utils.Coord;
import shingshang.out.Output;
import shingshang.in.Input;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

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
 * Classe principale définissant la fonction main du projet, les boucles de jeu (en mode GUI ou NOGUI)...
 * @author jules et hugo
 */

public class Shingshang extends Application {
  
    private GridPane grille;
    private int[] map;
    private static boolean GUI;
    private static int tour;
    private static Output out;
    private static Joueur[] joueurs;
    private static Pion pionSelectionne;
    private static Input in;
    private static Pion dernierPionJoue;
    
    /**
     * Méthode obligatoire de l'objet Application de javaFX, elle définit l'initialisation de l'application.
     * @param stage stage javafx
     */
    @Override
    public void start(Stage stage){
        
        if (Shingshang.isGUI() == false){
            this.gameLoopNoGui();
        }else{   
            //System.out.println("Working Directory = "+System.getProperty("user.dir"));            
            //System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
            
            stage.getIcons().add(new Image("shingshang/medias/icon.png"));
            Shingshang.setOut(new RepartiteurGUIorNOGUI().getOut(stage));
            //Plus besoin du plateau, on générera la matrice necessaire a l'affichage no GUI
            //et on randomisera le fond du plateau GUI dans OutGUI
        }
    }
    
    public static void initJeu(){
        Joueur.setNbJoueurs(0);
        Shingshang.setJoueurs(new Joueur[2]);
        for(int i=0;i<Shingshang.getJoueurs().length;i++){
            Shingshang.getJoueurs()[i] = new Joueur();
        }
        Shingshang.setDernierPionJoue(null);
    }
    
    /**
     * Méthode main du projet, gère les arguments passé à l'exécution
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ShutdownHook shutdownHook = new ShutdownHook();
        //Traitement des arguments de lancement
        int i;
        
        //Par default, l'application est en mode GUI
        setGUI(true);
        for (i=0;i<args.length;i++){
            //System.out.println("args : "+args[i]);
            //Si on trouve "-nogui" dans les arguments
            if(args[i].equals("-nogui")){
                setGUI(false);
            }
        }
        //On initialise le tour
        Shingshang.setTour(0);
        launch(args);
    }
    
    /**
     * Méthode static du jeu permettant de faire le lien entre un pion et les coordonnées. 
     * @param coord Cooordoné du pion a vérifié
     * @return Pion si y'a un pion, null sinon
     */
    public static Pion isPionHere(Coord coord){
        int i;
        int j;
        //Parcourt des joueurs
        for (i=0; i<Shingshang.joueurs.length;i++){
            //Parcourt de la liste de pion de chaque joueur
            for(j=0; j<Shingshang.joueurs[i].getListePion().size();j++){
                //Si on trouve un pion qui a les mêmes coordonées
                if(Shingshang.joueurs[i].getListePion().get(j).getCoord().equals(coord))
                    //On return le pion trouvé
                    return Shingshang.joueurs[i].getListePion().get(j);
            }
        }
        //Sinon on return un pion null
        return null;
    }

    
    /**
     * Méthode gérant la boucle de jeu en mode nogui.
     */
    public void gameLoopNoGui(){

        //Shingshang.initJeu();
        Shingshang.setOut(new RepartiteurGUIorNOGUI().getOut());
        Shingshang.setIn(new Input());
        do{
            Shingshang.getOut().menuDebut();
            Shingshang.getOut().afficher();

            int selection;
            Boolean isFinTour = true;
            do{
                do{
                    //Test pour éviter de deselectionné un pion que l'on aurait pas bougé
                    if (isFinTour){
                        Shingshang.setPionSelectionne(null);
                    }

                    selection = Shingshang.getIn().selection();
                    switch (selection) {
                        case 1:
                            //Si on a bougé, on regarde les conditions de fin de tour
                            if(Shingshang.aGagner() != null) isFinTour = true;
                            else isFinTour = Shingshang.isTourEnded();

                            break;
                        case 0:
                            //Si 0 on a juste selectionné un pion, on refait un tour de boucle
                            isFinTour = false;
                            break;
                        case -1:
                            //Si -1 c'est qu'on a appuyer sur fin de tour
                            isFinTour = true;
                            break;
                    }
                    Shingshang.getOut().afficher();
                //Si ce n'est pas la fin du tour
                }while(!isFinTour);
                Shingshang.finTour();
                Shingshang.setPionSelectionne(null);
                Shingshang.getOut().afficher();
            }while(Shingshang.aGagner()==null);    
        }while(Shingshang.getOut().menuFin(Shingshang.aGagner()));
    }
    
    /**
     * Méthode renvoyant le joueur gagnant si il y a, null sinon.
     * @return le joueur gagnant, null sinon
     */
    public static Joueur aGagner(){
        for(int i = 0; i<joueurs.length; i++){
            int nbDragKilled = 0;
            Pion pTmp;
            LinkedList coordPortails = new LinkedList();
            for(int j = 0; j<joueurs[i].getListePion().size();j++){
                pTmp = joueurs[i].getListePion().get(j);
                
                if (pTmp.getType() == 2 && pTmp.isMort())
                    nbDragKilled++;
                
                
                if (pTmp.getType()== 2){
                    if(pTmp.getJoueur() == 0){
                        coordPortails.add(new Coord(8,4));
                        coordPortails.add(new Coord(8,5));
                    }else{
                        coordPortails.add(new Coord(1,4));
                        coordPortails.add(new Coord(1,5));
                    }
                    
                    if (coordPortails.contains(pTmp.getCoord())) return joueurs[pTmp.getJoueur()];
                    coordPortails.clear();
                }
            }
            if (nbDragKilled==2) return joueurs[(joueurs.length-1)-i];
        }
        
        return null;
    }
    
    /**
     * Méthode static du jeu permettant de tester la fin de tour,
     * renvoit true si le tour est fini, sinon false.
     * @return true si le tour est finit, false sinon
     */
    public static boolean isTourEnded(){
        
        Pion p = Shingshang.getPionSelectionne();
        //Si le dernier pion selectionné n'est plus jouable et qu'il a sauté, c'est que l'on a sauté un ennemi, donc le tour ne s'arrete pas
        if(!(p.isJouable()) && p.isaSauter()){
            return false;
        }
        //Si le dernier pion selectionné est jouable, a déjà sauté (donc qu'il a sauté un pion allié) mais qu'il ne peut plus sauter, le tour s'arrete
        if((p.isJouable() && p.isaSauter()) && !(p.peutSauter(joueurs))){
            return true;
        }
   
        //Si le dernier pion n'a pas sauté et qu'on est dans cette fonction, c'est qu'il a glissé, donc le tour s'arrete
        return !(p.isaSauter());
    }
    
    /**
     * Methode static au jeu qui permet de finir un tour de jeu. 
     */
    public static void finTour(){
        Shingshang.setTour(getTour()+1);
        Shingshang.out.message("Fin de tour");
        //On reset tous les pions du jeu
        for (Joueur joueur : Shingshang.joueurs) {
            for (int j = 0; j < joueur.getListePion().size(); j++) {
                joueur.getListePion().get(j).getDplPossible().clear();
                joueur.getListePion().get(j).getSautPossible().clear();
                joueur.getListePion().get(j).setJouable(true);
                joueur.getListePion().get(j).setaSauter(false);
                joueur.getListePion().get(j).setDirection(-1);
                joueur.getListePion().get(j).setLastCoordAvantSautAllie(new Coord(-1,-1));
                joueur.getListePion().get(j).setLastDirection(-1);
                joueur.getListePion().get(j).setInEnchainement(false);
            }
            //Shingshang.getOut().message("C'est le tour de "+Shingshang.getJoueurs()[Shingshang.getTour()%2].getNom());
        }     
    }

    
    /**
     * @return the tour
     */
    public static int getTour() {
        return tour;
    }

    /**
     * @param aTour the tour to set
     */
    public static void setTour(int aTour) {
        tour = aTour;
    }

    /**
     * @return the grille
     */
    public GridPane getGrille() {
        return grille;
    }

    /**
     * @param grille the grille to set
     */
    public void setGrille(GridPane grille) {
        this.grille = grille;
    }

    /**
     * @return the map
     */
    public int[] getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(int[] map) {
        this.map = map;
    }

    /**
     * @return the joueurs
     */
    public static Joueur[] getJoueurs() {
        return Shingshang.joueurs;
    }

    /**
     * @param joueurs the joueurs to set
     */
    public static void setJoueurs(Joueur[] joueurs) {
        Shingshang.joueurs = joueurs;
    }
    
    /**
     * @return the pionSelectionne
     */
    public static Pion getPionSelectionne() {
        return pionSelectionne;
    }

    /**
     * @param aPionSelectionne the pionSelectionne to set
     */
    public static void setPionSelectionne(Pion aPionSelectionne) {
        pionSelectionne = aPionSelectionne;
    }

    /**
     * @return the GUI
     */
    public static boolean isGUI() {
        return GUI;
    }

    /**
     * @param aGUI the GUI to set
     */
    public static void setGUI(boolean aGUI) {
        GUI = aGUI;
    }

    /**
     * @return the out
     */
    public static Output getOut() {
        return out;
    }

    /**
     * @param aOut the out to set
     */
    public static void setOut(Output aOut) {
        out = aOut;
    }

    /**
     * @return the in
     */
    public static Input getIn() {
        return in;
    }

    /**
     * @param aIn the in to set
     */
    public static void setIn(Input aIn) {
        in = aIn;
    }    
    /**
     * Méthode permettant de charger une partie spéciale
     * On va lire le fichier sérialisé (.ser) puis on va le mettre dans un objet
     * Sauvegarde. On remet ensuite les valeurs dans le jeu.
     * @param file nom du fichier (avec .ser)
     * @return ture si la charge c'est bien passé, false sinon
     */
    public static boolean charger(String file) {
        try {
            //System.out.println("\nDEBUT CHARGEMENT");
            System.out.println(file);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Sauvegarde chargement = new Sauvegarde();
            chargement = (Sauvegarde) in.readObject();
            in.close();
            fileIn.close();

            Shingshang.setJoueurs(chargement.getJoueurs());
            Shingshang.setTour(chargement.getTour());
        } catch(IOException i) {
            System.out.println("IOException " + i.getMessage());
            return false;
        } catch(ClassNotFoundException c) {
            System.out.println("ClassNotFoundException " + c.getMessage());
            return false;
        }
        return true;
    }    
    
    /**
     * Méthode permettant de sauvegarder à un état T
     * On va sérialisé une classe Sauvegarde qui contient les joueurs (pions
     * etc.) et la variable tour.
     * @param nom Nom de la sauvegarde (avec le .ser)
     */
    public static void sauvegarder(String nom) {
        Sauvegarde sauvegarde = new Sauvegarde(Shingshang.getTour());
        try {
            //System.out.println("DEBUT SAUVEGARDE");
            FileOutputStream fileOut = new FileOutputStream("./save/" + nom);
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(sauvegarde);
            output.close();
            fileOut.close();
            //System.out.println("FIN SAUVEGARDE");
        } catch(IOException i) { 
            System.out.println(i.getLocalizedMessage() + "\n" + i.toString());
        }  
    }
    
    /**
     * @return the dernierPionJoue
     */
    public static Pion getDernierPionJoue() {
        return dernierPionJoue;
    }
    
    /**
     * @param aDernierPionJoue the dernierPionJoue to set
     */
    public static void setDernierPionJoue(Pion aDernierPionJoue) {
        dernierPionJoue = aDernierPionJoue;
    }
}
