package modele.persistence;
/**
 * La classe Site représente un site géographique.
 * Elle contient des informations sur le nom, le code, la longitude et la latitude du site.
 * Elle permet de calculer la distance entre deux sites.
 * @author Elie Tardy
 * @version 1.0
 */

public class Site {

    /**
     * Le nom du site
     */
    private String nom;

    /**
     * Le code du site
     */
    private String code;

    /**
     * La longitude du site
     */
    private float longitude;

    /**
     * La latitude du site
     */
    private float latitude;


    /**
     * Constructeur de la classe Site
     * @param nom le nom du site
     * @param code le code du site
     * @param longitude la longitude du site
     * @param latitude la latitude du site
     */
    public Site(String nom, String code, float longitude, float latitude) {
        if (code == null || code.length() > 10) {
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.code = code;
        }

        if (nom == null || nom.length() > 50) {
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.nom = nom;
        }

        if(longitude < 0 || latitude < 0){
            throw new IllegalArgumentException("Le nom ou le code du site est null");
        } else {
            this.longitude = longitude;
            this.latitude = latitude;
        }
        
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
    public String getCode() {
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
    public void setCode(String code) {
        if (code == null || code.length() > 10) {
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
        if(latitude < 0){ {
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
    }

    /**
     * Calcule la distance entre deux sites
     * @param autreSite le site dont on veut calculer la distance
     * @return la distance entre les deux sites
     */
    public float calculerDistance(Site autreSite) {
        if (autreSite == null) {
            throw new IllegalArgumentException("Le site est null");
        }
        double lat1 = Double.parseDouble(this.latitude);
        double lon1 = Double.parseDouble(this.longitude);
        double lat2 = Double.parseDouble(autreSite.getLatitude());
        double lon2 = Double.parseDouble(autreSite.getLongitude());

        double dx = lat2 - lat1;
        double dy = lon2 - lon1;

        float ret = (float) Math.sqrt(dx * dx + dy * dy);

        return ret;
    }



}