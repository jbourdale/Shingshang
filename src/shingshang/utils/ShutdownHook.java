/*
 * Copyright 2016 Hugo & Jules.
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

import shingshang.Shingshang;

/**
 * Cette classe va attrapé le signal de fin de programme et lancer une save du jeu
 * @author Hugo et Jules
 */
public class ShutdownHook {
    
    /**
     * Constructeur de la classe
     */
    public ShutdownHook() {
        this.attachShutDownHook();
    }
    
    /**
     * Méthode qui va créer un "listener" de shutdown
     * Quand l'utilisateur va quitter (croix rouge, Ctrl + C ou autres) le
     * programme va passer dans run avant de quitter. Run va donc sauvegarder
     * notre partie.
     */    
    private void attachShutDownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Shingshang.sauvegarder("lastGame.ser");
            }
        });
    }
}
