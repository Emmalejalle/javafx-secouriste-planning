package modele;

import modele.persistence.User;

/**
 * Classe de gestion de session utilisateur.
 * Permet de stocker et récupérer l'utilisateur actuellement connecté.
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    /**
     * Retourne l'instance unique de SessionManager.
     * Si l'instance n'a pas encore été créée, elle est créée.
     * 
     * @return L'instance unique de SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Renvoie l'utilisateur actuellement connecté.
     * @return L'utilisateur actuellement connecté, ou null si personne n'est connecté.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Met à jour l'utilisateur actuellement connecté.
     * 
     * @param currentUser L'utilisateur à stocker en tant qu'utilisateur actuellement connecté.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}