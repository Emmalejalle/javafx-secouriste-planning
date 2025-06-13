use sae_secours;

DROP TABLE IF EXISTS Affectation;
DROP TABLE IF EXISTS ListCompUser;
DROP TABLE IF EXISTS Besoin;
DROP TABLE IF EXISTS DPS;
DROP TABLE IF EXISTS Sport;
DROP TABLE IF EXISTS Site;
DROP TABLE IF EXISTS PrerequisComp;
DROP TABLE IF EXISTS Competence;
DROP TABLE IF EXISTS Dispo;
DROP TABLE IF EXISTS Journee;
DROP TABLE IF EXISTS User;

CREATE TABLE User (
	idUser BIGINT Primary key,
    mdpUser VARCHAR(20),
    nomUser VARCHAR(20),
    prenomUser VARCHAR(20),
    dateNaissance DATE,
    emailUser VARCHAR(50),
    telUser VARCHAR(15),
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
	idComp INT PRIMARY KEY,
    intitule VARCHAR(40),
	abreviationIntitule VARCHAR(5)
);

CREATE TABLE PrerequisComp (
    idCompPre INT,
    idPrerequis INT,
    PRIMARY KEY (idCompPre, idPrerequis),
    FOREIGN KEY (idCompPre) REFERENCES Competence(idComp),
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
    jourDPS INT,
    moisDPS INT,
    anneeDPS INT,
    
    FOREIGN KEY (codeSiteDPS) REFERENCES Site(codeSite),
    FOREIGN KEY (codeSportDPS) REFERENCES Sport(codeSport),
    FOREIGN KEY (jourDPS, moisDPS, anneeDPS) REFERENCES Journee(jour, mois, annee)
);

CREATE TABLE Besoin (
    idBesoinDPS BIGINT,
    idBesoinComp INT,
    nombre INT CHECK (nombre >= 1),

    PRIMARY KEY (idBesoinDPS, idBesoinComp),
    FOREIGN KEY (idBesoinDPS) REFERENCES DPS(idDPS),
    FOREIGN KEY (idBesoinComp) REFERENCES Competence(idComp)
);

CREATE TABLE ListCompUser (
    idSecouCompList BIGINT,
    idCompList INT,
    
    PRIMARY KEY (idSecouCompList, idCompList),
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

-- INSERT DE LA TABLE JOURNEE :
-- Juin 2025
INSERT INTO Journee VALUES
(1, 6, 2025), (2, 6, 2025), (3, 6, 2025), (4, 6, 2025), (5, 6, 2025), (6, 6, 2025), (7, 6, 2025),
(8, 6, 2025), (9, 6, 2025), (10, 6, 2025), (11, 6, 2025), (12, 6, 2025), (13, 6, 2025), (14, 6, 2025),
(15, 6, 2025), (16, 6, 2025), (17, 6, 2025), (18, 6, 2025), (19, 6, 2025), (20, 6, 2025), (21, 6, 2025),
(22, 6, 2025), (23, 6, 2025), (24, 6, 2025), (25, 6, 2025), (26, 6, 2025), (27, 6, 2025), (28, 6, 2025),
(29, 6, 2025), (30, 6, 2025);

-- Juillet 2025
INSERT INTO Journee VALUES
(1, 7, 2025), (2, 7, 2025), (3, 7, 2025), (4, 7, 2025), (5, 7, 2025), (6, 7, 2025), (7, 7, 2025),
(8, 7, 2025), (9, 7, 2025), (10, 7, 2025), (11, 7, 2025), (12, 7, 2025), (13, 7, 2025), (14, 7, 2025),
(15, 7, 2025), (16, 7, 2025), (17, 7, 2025), (18, 7, 2025), (19, 7, 2025), (20, 7, 2025), (21, 7, 2025),
(22, 7, 2025), (23, 7, 2025), (24, 7, 2025), (25, 7, 2025), (26, 7, 2025), (27, 7, 2025), (28, 7, 2025),
(29, 7, 2025), (30, 7, 2025), (31, 7, 2025);

-- Août 2025
INSERT INTO Journee VALUES
(1, 8, 2025), (2, 8, 2025), (3, 8, 2025), (4, 8, 2025), (5, 8, 2025), (6, 8, 2025), (7, 8, 2025),
(8, 8, 2025), (9, 8, 2025), (10, 8, 2025), (11, 8, 2025), (12, 8, 2025), (13, 8, 2025), (14, 8, 2025),
(15, 8, 2025), (16, 8, 2025), (17, 8, 2025), (18, 8, 2025), (19, 8, 2025), (20, 8, 2025), (21, 8, 2025),
(22, 8, 2025), (23, 8, 2025), (24, 8, 2025), (25, 8, 2025), (26, 8, 2025), (27, 8, 2025), (28, 8, 2025),
(29, 8, 2025), (30, 8, 2025), (31, 8, 2025);

-- INSERT DE LA TABLE SPORT 
INSERT INTO Sport (codeSport, nomSport) VALUES
('SKIALP', 'Ski alpin'),
('SKIFON', 'Ski de fond'),
('BIATHL', 'Biathlon'),
('SAUTSKI', 'Saut à ski'),
('COMBSKI', 'Combiné nordique'),
('SNOWBX', 'Snowboard cross'),
('HALFPN', 'Half-pipe snowboard'),
('BOSS', 'Ski de bosses'),
('SKICRX', 'Ski cross'),
('PATART', 'Patinage artistique'),
('PATVIT', 'Patinage de vitesse'),
('SHORTRK', 'Short track'),
('HOCKEY', 'Hockey sur glace'),
('LUGE', 'Luge'),
('BOBSLEI', 'Bobsleigh'),
('SKELETN', 'Skeleton');

-- INSERT DE LA TABLE SITE :
INSERT INTO Site (codeSite, nomSite, longitude, latitude) VALUES
('TIGNES', 'Tignes', 6.9247, 45.4690),
('VALDISER', 'Val d\'Isère', 6.9798, 45.4482),
('LA_PLAGNE', 'La Plagne', 6.6770, 45.5075),
('LESARCS', 'Les Arcs', 6.8285, 45.5724),
('CHAMONIX', 'Chamonix', 6.8694, 45.9237),
('MONTGENV', 'Montgenèvre', 6.7258, 44.9337),
('ALBERTVL', 'Albertville Halle Olympique', 6.3894, 45.6753),
('LAUSANNE', 'Lausanne Vaudoise Arena', 6.6045, 46.5286); -- (Suisse, proche, ex-JOJ 2020)

-- INSERT DE LA TABLE COMPETENCE :
INSERT INTO Competence (idComp, intitule, abreviationIntitule) VALUES
(1, 'Cadre Opérationnel', 'CADRE'),
(2, 'Chef de Poste', 'CDPOS'),
(3, 'Chef d’Equipe', 'CDEQP'),
(4, 'Pilote d’hélicoptère', 'HELIC'),
(5, 'Pilote de moto neige', 'MOTON'),
(6, 'VPSP', 'VPSP'),
(7, 'PSE1', 'PSE1'),
(8, 'PSE2', 'PSE2'),
(9, 'Secouriste de montagne', 'MNTAG');

-- INSERT DE LA TABLE PREREQUIS COMPETENCE
INSERT INTO PrerequisComp (idCompPre, idPrerequis) VALUES
(6, 8),  -- VPSP nécessite PSE2
(8, 7),  -- PSE2 nécessite PSE1
(9, 7),  -- Secouriste de montagne nécessite PSE1
(3, 8),  -- Chef d’Équipe nécessite PSE2
(2, 3),  -- Chef de Poste nécessite Chef d’Équipe
(1, 2),  -- Cadre Opérationnel nécessite Chef de Poste
(4, 5);  -- Pilote hélico nécessite Pilote de motoneige

-- INSERT DE LA TABLE USER :
-- Administrateurs
INSERT INTO User (idUser, mdpUser, nomUser, prenomUser, dateNaissance, emailUser, telUser, adresseUser, isAdmin) VALUES
(11112222, 'admin123', 'Durand', 'Claire', '1980-06-12', 'claire.durand@example.com', '06-12-34-56-78', '12 rue des Lilas, Paris', 1),
(11113333, 'admin456', 'Martin', 'Jean', '1978-11-03', 'jean.martin@example.com', '06-23-45-67-89', '18 avenue Victor Hugo, Lyon', 1);

-- Secouristes
INSERT INTO User (idUser, mdpUser, nomUser, prenomUser, dateNaissance, emailUser, telUser, adresseUser, isAdmin) VALUES
(12345678, '12345678', 'Bernard', 'Alice', '1995-03-15', 'alice.bernard@example.com', '06-11-12-22-33', '5 rue des Fleurs, Lille', 0),
(22334455, 'azerty12', 'Petit', 'Lucas', '1994-08-22', 'lucas.petit@example.com', '06-11-22-33-44', '14 place Bellecour, Lyon', 0),
(33445566, 'pass1234', 'Robert', 'Emma', '1998-01-05', 'emma.robert@example.com', '06-22-33-44-55', '3 avenue Jean Jaurès, Marseille', 0),
(44556677, 'secure45', 'Lemoine', 'Thomas', '1993-07-09', 'thomas.lemoine@example.com', '06-33-44-55-66', '8 boulevard Haussmann, Paris', 0),
(55667788, 'motdep45', 'Dubois', 'Sophie', '1997-10-30', 'sophie.dubois@example.com', '06-44-55-66-77', '22 rue Nationale, Bordeaux', 0),
(66778899, 'qwerty78', 'Leroy', 'Nicolas', '1992-06-18', 'nicolas.leroy@example.com', '06-55-66-77-88', '9 impasse des Violettes, Nice', 0),
(77889900, 'azAZ12', 'Garcia', 'Julie', '1996-04-27', 'julie.garcia@example.com', '06-66-77-88-99', '2 chemin des Dunes, Nantes', 0),
(88990011, 'pswd2025', 'Faure', 'Hugo', '1991-12-11', 'hugo.faure@example.com', '06-77-88-99-00', '11 rue Paul Valéry, Montpellier', 0),
(99001122, 'hello22', 'Roux', 'Laura', '1995-09-03', 'laura.roux@example.com', '06-88-99-00-11', '17 rue des Acacias, Strasbourg', 0),
(10111223, 'abcd1234', 'Marchand', 'Maxime', '1999-05-08', 'maxime.marchand@example.com', '06-99-00-11-22', '20 rue Lafayette, Toulouse', 0),
(12131415, 'pass7890', 'Noël', 'Camille', '2000-02-14', 'camille.noel@example.com', '06-10-10-10-10', '7 rue du Faubourg, Reims', 0),
(13141516, 'secour88', 'Carpentier', 'Antoine', '1989-11-23', 'antoine.carpentier@example.com', '06-12-12-12-12', '21 rue Alsace, Dijon', 0),
(14151617, 'azerty77', 'Renard', 'Manon', '1997-01-31', 'manon.renard@example.com', '06-13-13-13-13', '10 allée des Chênes, Brest', 0),
(15161718, 'monpass1', 'Paris', 'Jules', '1994-04-19', 'jules.paris@example.com', '06-14-14-14-14', '15 boulevard Carnot, Metz', 0),
(16171819, 'safeWord', 'Blanc', 'Eva', '1990-08-16', 'eva.blanc@example.com', '06-15-15-15-15', '6 rue de la République, Grenoble', 0),
(17181920, 'pwdpass', 'Roy', 'Alexis', '1996-07-12', 'alexis.roy@example.com', '06-16-16-16-16', '13 rue du Stade, Tours', 0),
(18192021, 'motpasse', 'Leclerc', 'Chloé', '1993-06-02', 'chloe.leclerc@example.com', '06-17-17-17-17', '1 route de Paris, Nancy', 0),
(19202122, 'easy789', 'Bertrand', 'Louis', '1998-10-28', 'louis.bertrand@example.com', '06-18-18-18-18', '4 place Saint-Michel, Orléans', 0),
(20212223, 'passabc', 'Fernandez', 'Anaïs', '1991-03-07', 'anais.fernandez@example.com', '06-19-19-19-19', '19 rue des Vosges, Le Havre', 0),
(21222324, 'mdp321', 'Gauthier', 'Hugo', '1999-12-20', 'hugo.gauthier@example.com', '06-20-20-20-20', '30 avenue République, Amiens', 0),
(22232425, 'mdp222', 'Rodriguez', 'Inès', '2000-01-18', 'ines.rodriguez@example.com', '06-21-21-21-21', '45 chemin Vert, Clermont-Ferrand', 0),
(23242526, 'test456', 'Lopez', 'Nathan', '1995-05-25', 'nathan.lopez@example.com', '06-22-22-22-22', '16 rue des Écoles, Limoges', 0),
(24252627, 'pass910', 'Henry', 'Léa', '1997-03-17', 'lea.henry@example.com', '06-23-23-23-23', '27 avenue Gustave Eiffel, Besançon', 0),
(25262728, 'mdp5566', 'Perrot', 'Tom', '1992-10-11', 'tom.perrot@example.com', '06-24-24-24-24', '8 rue des Peupliers, Saint-Étienne', 0),
(26272829, 'azqswx12', 'Guillaume', 'Lina', '1996-08-05', 'lina.guillaume@example.com', '06-25-25-25-25', '12 boulevard des Alpes, Annecy', 0),
(27282930, 'open1234', 'Moreau', 'Lucas', '1994-09-29', 'lucas.moreau@example.com', '06-26-26-26-26', '3 chemin des Bois, Chambéry', 0);

-- INSERT DE LA TABLE DPS :
INSERT INTO DPS (idDPS, horaireDepart, horaireFin, codeSiteDPS, codeSportDPS, jourDPS, moisDPS, anneeDPS) VALUES
(59745529, 15, 18, 'ALBERTVL', 'LUGE', 22, 6, 2025),
(83283564, 9, 14, 'TIGNES', 'BIATHL', 22, 6, 2025),
(98884217, 19, 23, 'VALDISER', 'LUGE', 22, 6, 2025),
(22214528, 15, 19, 'LAUSANNE', 'BIATHL', 22, 6, 2025),
(27082804, 19, 22, 'VALDISER', 'SAUTSKI', 22, 6, 2025),
(61961487, 11, 14, 'CHAMONIX', 'BIATHL', 23, 6, 2025),
(19759657, 9, 14, 'ALBERTVL', 'PATVIT', 23, 6, 2025),
(80658124, 9, 13, 'VALDISER', 'SNOWBX', 23, 6, 2025),
(52459582, 10, 14, 'LA_PLAGNE', 'PATART', 23, 6, 2025),
(72616091, 15, 18, 'MONTGENV', 'SKIALP', 25, 6, 2025),
(84427660, 10, 13, 'VALDISER', 'SNOWBX', 25, 6, 2025),
(20412536, 18, 22, 'TIGNES', 'SHORTRK', 25, 6, 2025),
(12814297, 13, 17, 'CHAMONIX', 'SKIALP', 25, 6, 2025),
(59922115, 13, 17, 'CHAMONIX', 'SKIFON', 25, 6, 2025),
(88898217, 15, 19, 'CHAMONIX', 'SKIFON', 25, 6, 2025);

-- INSERT DE LA TABLE Besoin	
-- DPS 59745529 (5 personnes sur 3 compétences)
INSERT INTO Besoin VALUES 
(59745529, 7, 2),
(59745529, 6, 2),
(59745529, 5, 1);

-- DPS 83283564 (1 compétence = 5 personnes)
INSERT INTO Besoin VALUES 
(83283564, 8, 5);

-- DPS 98884217 (5 compétences à 1 personne)
INSERT INTO Besoin VALUES 
(98884217, 7, 1),
(98884217, 5, 1),
(98884217, 4, 1),
(98884217, 6, 1),
(98884217, 3, 1);

-- DPS 22214528 (2+1+1+1 = 5)
INSERT INTO Besoin VALUES 
(22214528, 8, 2),
(22214528, 6, 1),
(22214528, 5, 1),
(22214528, 9, 1);

-- DPS 27082804 (2+1+1+1 = 5)
INSERT INTO Besoin VALUES 
(27082804, 7, 2),
(27082804, 5, 1),
(27082804, 4, 1),
(27082804, 3, 1);

-- DPS 61961487 (5 compétences à 1)
INSERT INTO Besoin VALUES 
(61961487, 7, 1),
(61961487, 6, 1),
(61961487, 5, 1),
(61961487, 2, 1),
(61961487, 1, 1);

-- DPS 19759657 (1 compétence à 5)
INSERT INTO Besoin VALUES 
(19759657, 3, 5);

-- DPS 80658124 (2+1+1+1 = 5)
INSERT INTO Besoin VALUES 
(80658124, 7, 2),
(80658124, 8, 1),
(80658124, 2, 1),
(80658124, 4, 1);

-- DPS 52459582 (1+1+1+2 = 5)
INSERT INTO Besoin VALUES 
(52459582, 7, 2),
(52459582, 6, 1),
(52459582, 3, 1),
(52459582, 2, 1);

-- DPS 72616091 (5 à 1)
INSERT INTO Besoin VALUES 
(72616091, 7, 1),
(72616091, 6, 1),
(72616091, 1, 1),
(72616091, 4, 1),
(72616091, 2, 1);

-- DPS 84427660 (1 compétence à 5)
INSERT INTO Besoin VALUES 
(84427660, 9, 5);

-- DPS 20412536 (2+1+1+1)
INSERT INTO Besoin VALUES 
(20412536, 7, 2),
(20412536, 3, 1),
(20412536, 6, 1),
(20412536, 1, 1);

-- DPS 86533228 (1+1+1+2)
INSERT INTO Besoin VALUES 
(12814297, 8, 2),
(12814297, 7, 1),
(12814297, 6, 1),
(12814297, 5, 1);

-- DPS 51791063 (1 compétence à 5)
INSERT INTO Besoin VALUES 
(59922115, 8, 5);

-- DPS 96409150 (5 à 1)
INSERT INTO Besoin VALUES 
(88898217, 1, 1),
(88898217, 2, 1),
(88898217, 3, 1),
(88898217, 4, 1),
(88898217, 5, 1);
