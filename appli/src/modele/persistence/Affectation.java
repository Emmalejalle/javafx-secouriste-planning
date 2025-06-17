package modele.persistence;

/**
 * La classe Affectation représente le lien entre un Secouriste, un DPS,
 * et la Compétence spécifique pour laquelle le secouriste est mobilisé.
 * C'est un objet de persistance simple qui représente une ligne de la table 'Affectation'.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class Affectation {

    /**
     * Le secouriste affecté à ce DPS.
     * Il ne peut pas être nul.
     */
    private Secouriste secouriste;

    /**
     * Le DPS sur lequel le secouriste est affecté.
     * Il ne peut pas être nul.
     */
    private DPS dps;

    /**
     * La compétence que le secouriste utilise sur ce DPS.
     * Il ne peut pas être nul.
     */
    private Competence competenceRemplie;

    /**
     * Constructeur pour créer une nouvelle affectation.
     * @param secouriste Le secouriste qui est affecté.
     * @param dps Le DPS sur lequel il est affecté.
     * @param competenceRemplie La compétence que le secouriste utilise sur ce DPS.
     */
    public Affectation(Secouriste secouriste, DPS dps, Competence competenceRemplie) {
        if (secouriste == null || dps == null || competenceRemplie == null) {
            throw new IllegalArgumentException("Le secouriste, le DPS et la compétence ne peuvent pas être nuls.");
        }
        this.secouriste = secouriste;
        this.dps = dps;
        this.competenceRemplie = competenceRemplie;
    }

    // --- Getters ---
    /**
     * Le getter du secouriste affecté.
     * @return Le secouriste associé à cette affectation.
     */
    public Secouriste getSecouriste() {
        return secouriste;
    }

    /**
     * Le getter du DPS sur lequel le secouriste est affecté.
     * @return Le DPS associé à cette affectation.
     */
    public DPS getDps() {
        return dps;
    }

    /**
     * Le getter de la compétence remplie par le secouriste dans cette affectation.
     * @return La compétence remplie par le secouriste.
     */
    public Competence getCompetenceRemplie() {
        return competenceRemplie;
    }

    /**
     * NOUVELLE MÉTHODE DE TRAITEMENT :
     * Vérifie la validité de l'affectation en s'assurant que le secouriste
     * a bien la compétence requise et est disponible le jour du DPS.
     * @return true si l'affectation est valide, false sinon.
     */
    public boolean estValide() {
        // On utilise les méthodes de traitement des objets Secouriste et DPS.
        boolean aLaCompetence = this.secouriste.possedeCompetence(this.competenceRemplie);
        boolean estDisponibleLeJourJ = this.secouriste.estDisponible(this.dps.getJournee());
        
        // L'affectation n'est valide que si les deux conditions sont remplies.
        return aLaCompetence && estDisponibleLeJourJ;
    }
    
    /**
     * Méthode toString pour afficher les informations de l'affectation.
     * @return Une chaîne de caractères représentant l'affectation.
     */
    @Override
    public String toString() {
        return "Affectation{" +
               "Secouriste=" + secouriste.getPrenom() + " " + secouriste.getNom() +
               ", sur DPS ID=" + dps.getId() +
               ", en tant que=" + competenceRemplie.getIntitule() +
               '}';
    }
}
