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

import javafx.event.*;
import shingshang.utils.Coord;
import shingshang.gameobjects.Pion;
/**
 * Classe comportant le comportement lors d'un clic sur la grille de jeu,
 * Sert d'input pour la version GUI du jeu.
 * @author jules et hugo
 */
public class GridHandler implements EventHandler{
    private final Coord coord;
    
    /**
     * Constructeur prenant en argument les positions de la case cliqué
     * @param coord Coordonnées de la case cliqué
     */
    public GridHandler(Coord coord){
        this.coord = coord;
    }
    
    /**
     * Handler du clic sur la grille
     * @param event Evenement à prendre en charge
     */
        @Override
    public void handle(Event event) {
       Pion.deplacement(this.coord);
    }
}
