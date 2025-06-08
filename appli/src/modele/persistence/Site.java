package model;


public class Site {

    private String nom;
    private String code;
    private String longitude;
    private String latitude;


    //constructeur
    public Site(String nom, String code, String longitude, String latitude) {
        this.nom = nom;
        this.code = code;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getNom() {
        return nom;
    }
    public String getCode() {
        return code;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String toString() {
        return "Site{" +
                "nom='" + nom + '\n' +
                ", code='" + code + '\n' +
                ", longitude='" + longitude + '\n' +
                ", latitude='" + latitude + '\n' +
                '}';
    }

    public float calculerDistance(Site autreSite) {

        double lat1 = Double.parseDouble(this.latitude);
        double lon1 = Double.parseDouble(this.longitude);
        double lat2 = Double.parseDouble(autreSite.getLatitude());
        double lon2 = Double.parseDouble(autreSite.getLongitude());

        double dx = lat2 - lat1;
        double dy = lon2 - lon1;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }



}