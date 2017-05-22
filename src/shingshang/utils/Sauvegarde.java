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
import shingshang.gameobjects.Joueur;
import shingshang.Shingshang;

/**
 * Class responsable de la sauvegarde et du chargement
 * @author jules et hugo
 */
public class Sauvegarde implements Serializable {

    private int tour;
    private Joueur[] joueurs;
    
    /**
     * Constructeur vide pour le chargement.
     */
    public Sauvegarde() {}
    
    /**
     * Constructeur pour la sauvegarde
     * @param tour Le tour
     */
    public Sauvegarde(int tour) {
        this.joueurs = Shingshang.getJoueurs();
        this.tour = tour;
    }

    /**
     * Getter de tour
     * @return tour
     */
    public int getTour() {
        return tour;
    }

    /**
     * Setter de tour
     * @param tour Le tour
     */
    public void setTour(int tour) {
        this.tour = tour;
    }

    /**
     * Getter de Joueur[]
     * @return joueurs
     */
    public Joueur[] getJoueurs() {
        return joueurs;
    }

    /**
     * Setter de Joueurs
     * @param joueurs Tableau de joueurs
     */
    public void setJoueurs(Joueur[] joueurs) {
        this.joueurs = joueurs;
    }
    
    
    
}
