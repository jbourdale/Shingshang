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
package shingshang.in;

import java.util.NoSuchElementException;
import java.util.Scanner;
import shingshang.utils.Coord;
import shingshang.gameobjects.Joueur;
import shingshang.gameobjects.Pion;
import shingshang.Shingshang;

/**
 * Class gérant les entrées de l'utilisateur en mode nogui du jeu.
 * @author jules et hugo
 */
public class Input {
    
    /**
     * Constructeur par défault
     */
    public Input(){}
    
    /**
     * Parallèle à la méthode handle de GridHandler.java mais en version nogui.
     * @return 1 si mouvement, 2 si sélection, -1 si fin de tour
     */
    public int selection(){
        //Initialisation des variables
        Scanner sc = new Scanner(System.in);        
        Coord coordTmp = new Coord();
        Pion pionTmp;
        String input; 
        String tabMsg [] = {"la ligne","la colonne"};
        //Lecture de l'entrée
        System.out.println("Veuillez entrer les coordonnées d'une case.");
        
        for(int i = 0;i<tabMsg.length;i++){
            System.out.print("Entrez "+tabMsg[i]+" (S : sauvegarder, F : fin de tour, Q : quitter) : ");
            input = sc.nextLine();
            try {
                if(i==0) coordTmp.setX(Integer.parseInt(input));
                else coordTmp.setY(Integer.parseInt(input));
            } catch (NumberFormatException n) {
                // Si on est là l'input n'est pas un integer
                switch (input.toUpperCase()) {
                    case "S":
                        Shingshang.getOut().menuSauvegarder();
                        i--;
                        continue;
                    case "F":
                        Shingshang.finTour();
                        return -1;
                    case "Q":
                        System.exit(0);
                    default:
                        i--;
                }
            }
        }
        

        //Récupération de ce qu'il y a sur la case selectionnée (pion ou null)
        pionTmp = Shingshang.isPionHere(coordTmp);
        //Si on bouge, on return 1
        //Sinon on return 0
        if(Pion.deplacement(coordTmp)) return 1;
        return 0;
    }
    
    public void setNomJoueurs() {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n");
        
                //Initialisation des variables
        Scanner sc = new Scanner(System.in);        
        String input; 
        String tabMsg [] = {"joueur 1","joueur 2"};
        //Lecture de l'entrée
        
        for(int i = 0;i<tabMsg.length;i++){
            System.out.print("Entrez le nom du "+tabMsg[i]+" (Q : quitter) : ");
            input = sc.nextLine();
            try {
                if(!input.isEmpty()) {
                    if("Q".equals(input.toUpperCase())) System.exit(0);
                    if(i==0) Shingshang.getJoueurs()[0].setNom(input);
                    else Shingshang.getJoueurs()[1].setNom(input);
                } else {
                    i--;
                }
            } catch (NoSuchElementException | IllegalStateException t) {
                System.out.println(t.getMessage());
                i--;
            }
        }
    }
}
