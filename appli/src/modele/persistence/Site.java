package modele.persistence;
/**
 * La classe Site représente un site géographique.
 * Elle contient des informations sur le nom, le code, la longitude et la latitude du site.
 * Elle permet de calculer la distance entre deux sites.
 * @author Emilien EMERIAU et Elie Tardy
 * @version 2.0
 */

public class Site {

    /**
     * Le code du site
     */
    private long code;

    /**
     * Le nom du site
     */
    private String nom;

    /**
     * La longitude du site
     */
    private float longitude;

    /**
     * La latitude du site
     */
    private float latitude;

    /**
     * Rayon moyen de la Terre en kilomètres.
     */
    private static final double RAYON_TERRE_KM = 6371.0;


    /**
     * Constructeur de la classe Site
     * @param nom le nom du site
     * @param code le code du site
     * @param longitude la longitude du site
     * @param latitude la latitude du site
     */
    public Site(long code ,String nom, float longitude, float latitude) {
        if (nom == null || nom.length() > 50) {
            throw new IllegalArgumentException("Le nom du site est null");
        } else {
            this.nom = nom;
        }

        if (code == 0 || code < 0 ) {
            throw new IllegalArgumentException("Le code du site est null");
        } else {
            this.code = code;
        }

        this.longitude = longitude;
        this.latitude = latitude;
    }
        

    /**
     * Retourne le nom du site
     * @return le nom du site
     */
    public String getNom() {
        String ret = this.nom;
        return ret;
    }

    /**
     * Retourne le code du site
     * @return le code du site
     */
    public long getCode() {
        return code;
    }

    /**
     * Retourne la longitude du site
     * @return la longitude du site
     */
    public float getLongitude() {
        float ret = this.longitude;
        return ret;
    }

    /**
     * Retourne la latitude du site
     * @return la latitude du site
     */
    public float getLatitude() {
        float ret = this.latitude;
        return ret;
    }

    /**
     * Met le nom du site
     * @param nom le nom du site
     */
    public void setNom(String nom) {
        if (nom == null || nom.length() > 50) {
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.nom = nom;
        }
    }

    /**
     * Met le code du site
     * @param code le code du site
     */
    public void setCode(long code) {
        if (code <= -1) {
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.code = code;
        }
    }
    public void setLongitude(float longitude) {
        if(longitude < 0){
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.longitude = longitude;
        }
    }

    /**
     * Met la latitude du site
     * @param latitude la latitude du site
     */
    public void setLatitude(float latitude) {
        if(latitude < 0){
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.latitude = latitude;
        }
    }
    
    /**
     * Retourne une chaine de caractères décrivant le site
     * @return une chaine de caractères décrivant le site
     */
    public String toString() {
        String ret = "Site{" + "nom=" + nom + ", code=" + code + ", longitude=" + longitude + ", latitude=" + latitude + '}';
        return ret;
    }

    /**
     * Calcule la distance à vol d'oiseau entre ce site et un autre en utilisant la formule de Haversine.
     * @param autreSite Le site avec lequel calculer la distance.
     * @return La distance en kilomètres.
     */
    public double calculerDistance(Site autreSite) {
        if (autreSite == null) {
            throw new IllegalArgumentException("L'autre site ne peut pas être nul pour calculer la distance.");
        }

        // Conversion des degrés en radians pour les calculs trigonométriques
        double lat1Rad = Math.toRadians(this.latitude);
        double lon1Rad = Math.toRadians(this.longitude);
        double lat2Rad = Math.toRadians(autreSite.latitude);
        double lon2Rad = Math.toRadians(autreSite.longitude);

        // Différences de coordonnées en radians
        double dLon = lon2Rad - lon1Rad;
        double dLat = lat2Rad - lat1Rad;

        // Formule de Haversine
        double a = Math.pow(Math.sin(dLat / 2), 2)
                 + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                 * Math.pow(Math.sin(dLon / 2), 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance finale en kilomètres
        return RAYON_TERRE_KM * c;
    }
}