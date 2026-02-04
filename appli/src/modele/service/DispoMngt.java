package modele.service;

import modele.DAO.JourneeDAO;
import modele.DAO.UserDAO;
import modele.persistence.Journee;
import modele.persistence.Secouriste;
import modele.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de gestion des disponibilités.
 * Cette classe est responsable de la gestion des disponibilités des secouristes.
 * Elle permet de vérifier les disponibilités, d'ajouter ou de supprimer des disponibilités
 * et de gérer les conflits de disponibilités.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class DispoMngt {
    
    private final JourneeDAO journeeDAO;
    private final UserDAO userDAO;
    
    public DispoMngt() {
        this.journeeDAO = new JourneeDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Charge les disponibilités du secouriste connecté pour un mois donné
     * @param annee L'année du mois à charger
     * @param mois Le mois à charger (1-12)
     * @return Map avec les dates en clé et les disponibilités en valeur
     */
    public Map<LocalDate, Boolean> chargerDisponibilitesMois(int annee, int mois) throws SQLException {
        Map<LocalDate, Boolean> disponibilites = new HashMap<>();
        
        // Récupérer le secouriste connecté
        Secouriste secouristeConnecte = getSecouristeConnecte();
        if (secouristeConnecte == null) {
            throw new IllegalStateException("Aucun secouriste connecté");
        }
        
        // Recharger le secouriste avec ses disponibilités depuis la base
        Secouriste secouristeComplet = (Secouriste) userDAO.findByID(secouristeConnecte.getId());
        
        // Convertir en Map pour le mois demandé
        for (Journee journee : secouristeComplet.getDisponibilites()) {
            if (journee.getAnnee() == annee && journee.getMois() == mois) {
                LocalDate date = LocalDate.of(journee.getAnnee(), journee.getMois(), journee.getJour());
                disponibilites.put(date, true); // Si la journée existe dans ses dispos, il est disponible
            }
        }
        
        return disponibilites;
    }
    
    /**
     * Sauvegarde les disponibilités modifiées du secouriste connecté
     * @param disponibilites Map des disponibilités à sauvegarder
     */
    public void sauvegarderDisponibilites(Map<LocalDate, Boolean> disponibilites) throws SQLException {
        Secouriste secouristeConnecte = getSecouristeConnecte();
        if (secouristeConnecte == null) {
            throw new IllegalStateException("Aucun secouriste connecté");
        }
        
        // Recharger le secouriste avec ses données complètes
        Secouriste secouristeComplet = (Secouriste) userDAO.findByID(secouristeConnecte.getId());
        
        // Modifier les disponibilités en fonction de la map reçue
        for (Map.Entry<LocalDate, Boolean> entry : disponibilites.entrySet()) {
            LocalDate date = entry.getKey();
            boolean estDisponible = entry.getValue();
            
            // Trouver ou créer la journée
            Journee journee = journeeDAO.findByDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            if (journee == null && estDisponible) {
                // Créer la journée si elle n'existe pas et que le secouriste veut être disponible
                journee = new Journee(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                journeeDAO.create(journee);
            }
            
            if (journee != null) {
                // Créer des variables finales pour la lambda
                final int jour = journee.getJour();
                final int mois = journee.getMois();
                final int annee = journee.getAnnee();
                
                if (estDisponible) {
                    // Ajouter la disponibilité si elle n'existe pas déjà
                    boolean dejaPresent = secouristeComplet.getDisponibilites().stream()
                        .anyMatch(j -> j.getJour() == jour && j.getMois() == mois && j.getAnnee() == annee);
                    if (!dejaPresent) {
                        secouristeComplet.getDisponibilites().add(journee);
                    }
                } else {
                    // Supprimer la disponibilité si elle existe
                    secouristeComplet.getDisponibilites().removeIf(j -> 
                        j.getJour() == jour && 
                        j.getMois() == mois && 
                        j.getAnnee() == annee);
                }
            }
        }
        
        // Sauvegarder avec la méthode update du UserDAO
        userDAO.update(secouristeComplet);
    }
    
    /**
     * Met à jour une disponibilité spécifique
     * @param date La date concernée
     * @param estDisponible true si disponible, false sinon
     */
    public void mettreAJourDisponibilite(LocalDate date, boolean estDisponible) throws SQLException {
        Map<LocalDate, Boolean> dispo = new HashMap<>();
        dispo.put(date, estDisponible);
        sauvegarderDisponibilites(dispo);
    }
    
    /**
     * Récupère le secouriste actuellement connecté
     * @return Le secouriste connecté ou null
     */
    private Secouriste getSecouristeConnecte() {
        try {
            return (Secouriste) SessionManager.getInstance().getCurrentUser();
        } catch (ClassCastException e) {
            return null; // L'utilisateur connecté n'est pas un secouriste
        }
    }
    
    /**
     * Vérifie si un secouriste est disponible à une date donnée
     * @param secouristeId L'ID du secouriste
     * @param date La date à vérifier
     * @return true si disponible, false sinon
     */
    public boolean estDisponible(long secouristeId, LocalDate date) throws SQLException {
        Secouriste secouriste = (Secouriste) userDAO.findByID(secouristeId);
        if (secouriste == null) {
            return false;
        }
        
        for (Journee journee : secouriste.getDisponibilites()) {
            if (journee.getJour() == date.getDayOfMonth() && 
                journee.getMois() == date.getMonthValue() && 
                journee.getAnnee() == date.getYear()) {
                return true;
            }
        }
        return false;
    }
}