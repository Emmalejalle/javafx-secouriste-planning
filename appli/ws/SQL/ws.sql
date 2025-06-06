use sae_secours;

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Dispo;

CREATE TABLE User (
	idUser BIGINT Primary key,
    mdpUser VARCHAR(20),
    nomUser VARCHAR(20),
    prenomUser VARCHAR(20),
    dateNaissance DATE,
    emailUser VARCHAR(50),
    telUser VARCHAR(10),
    adresseUser VARCHAR(50),
    isAdmin Integer CHECK (isAdmin IN (0, 1))
);

CREATE TABLE Journee (
	jour int,
    mois int,
    annee int,
	PRIMARY KEY (jour, mois, annee)
);

CREATE TABLE Dispo (
    idSecouriste BIGINT,
    jour INT,
    mois INT,
    annee INT,
    PRIMARY KEY (idSecouriste, jour, mois, annee),
    FOREIGN KEY (idSecouriste) REFERENCES User(idUser),
    FOREIGN KEY (jour, mois, annee) REFERENCES Journee(jour, mois, annee)
);


CREATE TABLE Competence (
	idComp INT,
    intitule VARCHAR(40),
	abreviationIntitule VARCHAR(5)
);

CREATE TABLE PrerequisComp (
    idCompP INT,
    idPrerequis INT,
    PRIMARY KEY (idComp, idPrerequis),
    FOREIGN KEY (idComp) REFERENCES Competence(idComp),
    FOREIGN KEY (idPrerequis) REFERENCES Competence(idComp)
);

CREATE TABLE Site (
    codeSite VARCHAR(10) PRIMARY KEY,
    nomSite VARCHAR(50),
    longitude FLOAT,
    latitude FLOAT
);

CREATE TABLE Sport (
    codeSport VARCHAR(10) PRIMARY KEY,
    nomSport VARCHAR(50)
);

CREATE TABLE DPS (
    idDPS BIGINT PRIMARY KEY,
    horaireDepart INT,
    horaireFin INT,
    
    codeSiteDPS VARCHAR(10),
    codeSportDPS VARCHAR(10),
    idCompDPS INT,
    jourDPS INT,
    moisDPS INT,
    anneeDPS INT,
    
    FOREIGN KEY (codeSiteDPS) REFERENCES Site(codeSite),
    FOREIGN KEY (codeSportDPS) REFERENCES Sport(codeSport),
    FOREIGN KEY (idCompDPS) REFERENCES Competence(idComp),
    FOREIGN KEY (jourDPS, moisDPS, anneeDPS) REFERENCES Journee(jour, mois, annee)
);

CREATE TABLE Besoin (
    idBesoinDPS BIGINT,
    idBesoinComp INT,
    nombre INT CHECK (nombre >= 1),

    PRIMARY KEY (idDPS, idComp),
    FOREIGN KEY (idBesoinDPS) REFERENCES DPS(idDPS),
    FOREIGN KEY (idBesoinComp) REFERENCES Competence(idComp)
);

CREATE TABLE ListCompUser (
    idSecouCompList BIGINT,
    idCompList INT,
    
    PRIMARY KEY (idSecouriste, idComp),
    FOREIGN KEY (idSecouCompList) REFERENCES User(idUser),
    FOREIGN KEY (idCompList) REFERENCES Competence(idComp)
);

CREATE TABLE Affectation (
	idSecouAffect BIGINT,
    idCompAffect int,
	idDPSAffect BIGINT,
	
    PRIMARY KEY (idSecouAffect, idCompAffect, idDPSAffect),
    FOREIGN KEY (idSecouAffect) REFERENCES User(idUser),
    FOREIGN KEY (idCompAffect) REFERENCES Competence(idComp),
    FOREIGN KEY (idDPSAffect) REFERENCES DPS(idDPS)
);





	