use sae_secours;

DROP TABLE IF EXISTS Affectation;
DROP TABLE IF EXISTS ListCompSecouriste;
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
	idUser BIGINT Primary KEY AUTO_INCREMENT,
    mdpUser VARCHAR(30),
    nomUser VARCHAR(20),
    prenomUser VARCHAR(20),
    dateNaissance VARCHAR(20),
    emailUser VARCHAR(50),
    telUser VARCHAR(15),
    adresseUser VARCHAR(50),
    isAdmin Integer CHECK (isAdmin IN (0, 1))
);

CREATE TABLE Journee (
	idJournee BIGINT PRIMARY KEY AUTO_INCREMENT,
	jour int,
    mois int,
    annee int
);

CREATE TABLE Dispo (
    idSecouriste BIGINT,
    idJourneeDispo BIGINT,
    PRIMARY KEY (idSecouriste, idJourneeDispo),
    FOREIGN KEY (idSecouriste) REFERENCES User(idUser) ON DELETE CASCADE, 
    FOREIGN KEY (idJourneeDispo) REFERENCES Journee(idJournee) ON DELETE CASCADE
);


CREATE TABLE Competence (
	idComp BIGINT PRIMARY KEY AUTO_INCREMENT,
    intitule VARCHAR(40),
	abreviationIntitule VARCHAR(5)
);

CREATE TABLE PrerequisComp (
    idCompPre BIGINT,
    idPrerequis BIGINT,
    PRIMARY KEY (idCompPre, idPrerequis),
    FOREIGN KEY (idCompPre) REFERENCES Competence(idComp) ON DELETE CASCADE,
    FOREIGN KEY (idPrerequis) REFERENCES Competence(idComp) ON DELETE CASCADE
);

CREATE TABLE Site (
    codeSite BIGINT PRIMARY KEY AUTO_INCREMENT,
    nomSite VARCHAR(50),
    longitude FLOAT,
    latitude FLOAT
);

CREATE TABLE Sport (
    codeSport BIGINT PRIMARY KEY AUTO_INCREMENT,
    nomSport VARCHAR(50)
);

CREATE TABLE DPS (
    idDPS BIGINT PRIMARY KEY AUTO_INCREMENT,
    horaireDepart INT,
    horaireFin INT,
    
    codeSiteDPS BIGINT(10),
    codeSportDPS BIGINT(10),
    idJourneeDPS BIGINT(10),
    
    FOREIGN KEY (codeSiteDPS) REFERENCES Site(codeSite) ON DELETE CASCADE,
    FOREIGN KEY (codeSportDPS) REFERENCES Sport(codeSport) ON DELETE CASCADE,
    FOREIGN KEY (idJourneeDPS) REFERENCES Journee(idJournee) ON DELETE CASCADE
);

CREATE TABLE Besoin (
    idBesoinDPS BIGINT,
    idBesoinComp BIGINT,
    nombre INT CHECK (nombre >= 0),

    PRIMARY KEY (idBesoinDPS, idBesoinComp),
    FOREIGN KEY (idBesoinDPS) REFERENCES DPS(idDPS) ON DELETE CASCADE,
    FOREIGN KEY (idBesoinComp) REFERENCES Competence(idComp) ON DELETE CASCADE
);

CREATE TABLE ListCompSecouriste (
    idSecouCompList BIGINT,
    idCompList BIGINT,
    
    PRIMARY KEY (idSecouCompList, idCompList),
    FOREIGN KEY (idSecouCompList) REFERENCES User(idUser) ON DELETE CASCADE,
    FOREIGN KEY (idCompList) REFERENCES Competence(idComp) ON DELETE CASCADE
);

CREATE TABLE Affectation (
	idSecouAffect BIGINT,
	idCompAffect BIGINT,
	idDPSAffect BIGINT,
	
    PRIMARY KEY (idSecouAffect, idCompAffect, idDPSAffect),
    FOREIGN KEY (idSecouAffect) REFERENCES User(idUser) ON DELETE CASCADE,
    FOREIGN KEY (idCompAffect) REFERENCES Competence(idComp) ON DELETE CASCADE,
    FOREIGN KEY (idDPSAffect) REFERENCES DPS(idDPS) ON DELETE CASCADE
);

-- INSERT DE LA TABLE JOURNEE :
-- Juin 2025
INSERT INTO Journee VALUES
(1, 1, 6, 2025), (2, 2, 6, 2025), (3, 3, 6, 2025), (4, 4, 6, 2025), (5, 5, 6, 2025), (6, 6, 6, 2025), (7, 7, 6, 2025),
(8, 8, 6, 2025), (9, 9, 6, 2025), (10, 10, 6, 2025), (11, 11, 6, 2025), (12, 12, 6, 2025), (13, 13, 6, 2025), (14, 14, 6, 2025),
(15, 15, 6, 2025), (16, 16, 6, 2025), (17, 17, 6, 2025), (18, 18, 6, 2025), (19, 19, 6, 2025), (20, 20, 6, 2025),
(21, 21, 6, 2025), (22, 22, 6, 2025), (23, 23, 6, 2025), (24, 24, 6, 2025), (25, 25, 6, 2025), (26, 26, 6, 2025),
(27, 27, 6, 2025), (28, 28, 6, 2025), (29, 29, 6, 2025), (30, 30, 6, 2025);


-- Juillet 2025
INSERT INTO Journee VALUES
(31, 1, 7, 2025), (32, 2, 7, 2025), (33, 3, 7, 2025), (34, 4, 7, 2025), (35, 5, 7, 2025), (36, 6, 7, 2025), (37, 7, 7, 2025),
(38, 8, 7, 2025), (39, 9, 7, 2025), (40, 10, 7, 2025), (41, 11, 7, 2025), (42, 12, 7, 2025), (43, 13, 7, 2025), (44, 14, 7, 2025),
(45, 15, 7, 2025), (46, 16, 7, 2025), (47, 17, 7, 2025), (48, 18, 7, 2025), (49, 19, 7, 2025), (50, 20, 7, 2025),
(51, 21, 7, 2025), (52, 22, 7, 2025), (53, 23, 7, 2025), (54, 24, 7, 2025), (55, 25, 7, 2025), (56, 26, 7, 2025),
(57, 27, 7, 2025), (58, 28, 7, 2025), (59, 29, 7, 2025), (60, 30, 7, 2025), (61, 31, 7, 2025);


-- Août 2025
INSERT INTO Journee VALUES
(62, 1, 8, 2025), (63, 2, 8, 2025), (64, 3, 8, 2025), (65, 4, 8, 2025), (66, 5, 8, 2025), (67, 6, 8, 2025), (68, 7, 8, 2025),
(69, 8, 8, 2025), (70, 9, 8, 2025), (71, 10, 8, 2025), (72, 11, 8, 2025), (73, 12, 8, 2025), (74, 13, 8, 2025), (75, 14, 8, 2025),
(76, 15, 8, 2025), (77, 16, 8, 2025), (78, 17, 8, 2025), (79, 18, 8, 2025), (80, 19, 8, 2025), (81, 20, 8, 2025),
(82, 21, 8, 2025), (83, 22, 8, 2025), (84, 23, 8, 2025), (85, 24, 8, 2025), (86, 25, 8, 2025), (87, 26, 8, 2025),
(88, 27, 8, 2025), (89, 28, 8, 2025), (90, 29, 8, 2025), (91, 30, 8, 2025), (92, 31, 8, 2025);

-- INSERT DE LA TABLE SPORT 
-- Table Sport avec codes numériques
INSERT INTO Sport (codeSport, nomSport) VALUES
(1001, 'Ski alpin'),         -- SKIALP
(1002, 'Ski de fond'),       -- SKIFON
(1003, 'Biathlon'),          -- BIATHL
(1004, 'Saut à ski'),        -- SAUTSKI
(1005, 'Combiné nordique'),  -- COMBSKI
(1006, 'Snowboard cross'),   -- SNOWBX
(1007, 'Half-pipe snowboard'), -- HALFPN
(1008, 'Ski de bosses'),     -- BOSS
(1009, 'Ski cross'),         -- SKICRX
(1010, 'Patinage artistique'), -- PATART
(1011, 'Patinage de vitesse'), -- PATVIT
(1012, 'Short track'),       -- SHORTRK
(1013, 'Hockey sur glace'),  -- HOCKEY
(1014, 'Luge'),              -- LUGE
(1015, 'Bobsleigh'),         -- BOBSLEI
(1016, 'Skeleton');          -- SKELETN


-- Table Site avec codes numériques
INSERT INTO Site (codeSite, nomSite, longitude, latitude) VALUES
(2001, 'Tignes', 6.9247, 45.4690),                       -- TIGNES
(2002, 'Val d''Isère', 6.9798, 45.4482),                 -- VALDISER
(2003, 'La Plagne', 6.6770, 45.5075),                    -- LA_PLAGNE
(2004, 'Les Arcs', 6.8285, 45.5724),                     -- LESARCS
(2005, 'Chamonix', 6.8694, 45.9237),                     -- CHAMONIX
(2006, 'Montgenèvre', 6.7258, 44.9337),                  -- MONTGENV
(2007, 'Albertville Halle Olympique', 6.3894, 45.6753),  -- ALBERTVL
(2008, 'Lausanne Vaudoise Arena', 6.6045, 46.5286);      -- LAUSANNE


-- INSERT DE LA TABLE COMPETENCE :
INSERT INTO Competence (idComp, intitule, abreviationIntitule) VALUES
(1, 'Cadre Opérationnel', 'CO'),
(2, 'Chef de Poste', 'CP'),
(3, 'Chef d’Equipe', 'CE'),
(4, 'Pilote d’hélicoptère', 'PHL'),
(5, 'Pilote de moto neige', 'PMN'),
(6, 'VPSP', 'VPSP'),
(7, 'PSE1', 'PSE1'),
(8, 'PSE2', 'PSE2'),
(9, 'Secouriste de montagne', 'SDM');

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
(11112222, 'admin123', 'Durand', 'Claire', '12/06/1980', 'claire.durand@example.com', '06-12-34-56-78', '12 rue des Lilas, Paris', 1),
(11113333, 'admin456', 'Martin', 'Jean', '03/11/1978', 'jean.martin@example.com', '06-23-45-67-89', '18 avenue Victor Hugo, Lyon', 1);

SELECT * FROM Affectation;

-- Secouristes
INSERT INTO User (idUser, mdpUser, nomUser, prenomUser, dateNaissance, emailUser, telUser, adresseUser, isAdmin) VALUES
(12345678, '12345678', 'Bernard', 'Alice', '15/03/1995', 'alice.bernard@example.com', '06-11-12-22-33', '5 rue des Fleurs, Lille', 0),
(22334455, 'azerty12', 'Petit', 'Lucas', '22/08/1994', 'lucas.petit@example.com', '06-11-22-33-44', '14 place Bellecour, Lyon', 0),
(33445566, 'pass1234', 'Robert', 'Emma', '05/01/1998', 'emma.robert@example.com', '06-22-33-44-55', '3 avenue Jean Jaurès, Marseille', 0),
(44556677, 'secure45', 'Lemoine', 'Thomas', '09/07/1993', 'thomas.lemoine@example.com', '06-33-44-55-66', '8 boulevard Haussmann, Paris', 0),
(55667788, 'motdep45', 'Dubois', 'Sophie', '30/10/1997', 'sophie.dubois@example.com', '06-44-55-66-77', '22 rue Nationale, Bordeaux', 0),
(66778899, 'qwerty78', 'Leroy', 'Nicolas', '18/06/1992', 'nicolas.leroy@example.com', '06-55-66-77-88', '9 impasse des Violettes, Nice', 0),
(77889900, 'azAZ12', 'Garcia', 'Julie', '27/04/1996', 'julie.garcia@example.com', '06-66-77-88-99', '2 chemin des Dunes, Nantes', 0),
(88990011, 'pswd2025', 'Faure', 'Hugo', '11/12/1991', 'hugo.faure@example.com', '06-77-88-99-00', '11 rue Paul Valéry, Montpellier', 0),
(99001122, 'hello22', 'Roux', 'Laura', '03/09/1995', 'laura.roux@example.com', '06-88-99-00-11', '17 rue des Acacias, Strasbourg', 0),
(10111223, 'abcd1234', 'Marchand', 'Maxime', '08/05/1999', 'maxime.marchand@example.com', '06-99-00-11-22', '20 rue Lafayette, Toulouse', 0),
(12131415, 'pass7890', 'Noël', 'Camille', '14/02/2000', 'camille.noel@example.com', '06-10-10-10-10', '7 rue du Faubourg, Reims', 0),
(13141516, 'secour88', 'Carpentier', 'Antoine', '23/11/1989', 'antoine.carpentier@example.com', '06-12-12-12-12', '21 rue Alsace, Dijon', 0),
(14151617, 'azerty77', 'Renard', 'Manon', '31/01/1997', 'manon.renard@example.com', '06-13-13-13-13', '10 allée des Chênes, Brest', 0),
(15161718, 'monpass1', 'Paris', 'Jules', '19/04/1994', 'jules.paris@example.com', '06-14-14-14-14', '15 boulevard Carnot, Metz', 0),
(16171819, 'safeWord', 'Blanc', 'Eva', '16/08/1990', 'eva.blanc@example.com', '06-15-15-15-15', '6 rue de la République, Grenoble', 0),
(17181920, 'pwdpass', 'Roy', 'Alexis', '12/07/1996', 'alexis.roy@example.com', '06-16-16-16-16', '13 rue du Stade, Tours', 0),
(18192021, 'motpasse', 'Leclerc', 'Chloé', '02/06/1993', 'chloe.leclerc@example.com', '06-17-17-17-17', '1 route de Paris, Nancy', 0),
(19202122, 'easy789', 'Bertrand', 'Louis', '28/10/1998', 'louis.bertrand@example.com', '06-18-18-18-18', '4 place Saint-Michel, Orléans', 0),
(20212223, 'passabc', 'Fernandez', 'Anaïs', '07/03/1991', 'anais.fernandez@example.com', '06-19-19-19-19', '19 rue des Vosges, Le Havre', 0),
(21222324, 'mdp321', 'Gauthier', 'Hugo', '20/12/1999', 'hugo.gauthier@example.com', '06-20-20-20-20', '30 avenue République, Amiens', 0),
(22232425, 'mdp222', 'Rodriguez', 'Inès', '18/01/2000', 'ines.rodriguez@example.com', '06-21-21-21-21', '45 chemin Vert, Clermont-Ferrand', 0),
(23242526, 'test456', 'Lopez', 'Nathan', '25/05/1995', 'nathan.lopez@example.com', '06-22-22-22-22', '16 rue des Écoles, Limoges', 0),
(24252627, 'pass910', 'Henry', 'Léa', '17/03/1997', 'lea.henry@example.com', '06-23-23-23-23', '27 avenue Gustave Eiffel, Besançon', 0),
(25262728, 'mdp5566', 'Perrot', 'Tom', '11/10/1992', 'tom.perrot@example.com', '06-24-24-24-24', '8 rue des Peupliers, Saint-Étienne', 0),
(27282930, 'open1234', 'Moreau', 'Lucas', '29/09/1994', 'lucas.moreau@example.com', '06-26-26-26-26', '3 chemin des Bois, Chambéry', 0);


-- INSERT DE LA TABLE DPS :
-- Insertion dans la table DPS avec ID numériques
INSERT INTO DPS (idDPS, horaireDepart, horaireFin, codeSiteDPS, codeSportDPS, idJourneeDPS) VALUES
(59745529, 15, 18, 2007, 1014, 22),  -- Albertville, Luge
(83283564, 9, 14, 2001, 1003, 22),   -- Tignes, Biathlon
(98884217, 19, 23, 2002, 1014, 22),  -- Val d'Isère, Luge
(22214528, 15, 19, 2008, 1003, 22),  -- Lausanne, Biathlon
(27082804, 19, 22, 2002, 1004, 22),  -- Val d'Isère, Saut à ski
(61961487, 11, 14, 2005, 1003, 23),  -- Chamonix, Biathlon
(19759657, 9, 14, 2007, 1011, 23),   -- Albertville, Patinage vitesse
(80658124, 9, 13, 2002, 1006, 23),   -- Val d'Isère, Snowboard cross
(52459582, 10, 14, 2003, 1010, 23),  -- La Plagne, Patinage artistique
(72616091, 15, 18, 2006, 1001, 25),  -- Montgenèvre, Ski alpin
(84427660, 10, 13, 2002, 1006, 25),  -- Val d'Isère, Snowboard cross
(20412536, 18, 22, 2001, 1012, 25),  -- Tignes, Short track
(12814297, 13, 17, 2005, 1001, 25),  -- Chamonix, Ski alpin
(59922115, 13, 17, 2005, 1002, 25),  -- Chamonix, Ski de fond
(88898217, 15, 19, 2005, 1002, 25);  -- Chamonix, Ski de fond

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

-- INSERT DE LA TABLE Dispo
-- Disponibilités des secouristes les 22, 23 et 25 juin (idJournee)
INSERT INTO Dispo VALUES
('10111223', 22),
('10111223', 23),
('10111223', 25),

('12131415', 22),
('12131415', 23),
('12131415', 25),

('12345678', 22),
('12345678', 23),
('12345678', 25),

('13141516', 22),
('13141516', 23),
('13141516', 25),

('14151617', 22),
('14151617', 23),
('14151617', 25),

('15161718', 22),
('15161718', 23),
('15161718', 25),

('16171819', 22),
('16171819', 23),
('16171819', 25),

('17181920', 22),
('17181920', 23),
('17181920', 25),

('18192021', 22),
('18192021', 23),
('18192021', 25),

('19202122', 22),
('19202122', 23),
('19202122', 25),

('20212223', 22),
('20212223', 23),
('20212223', 25),

('21222324', 22),
('21222324', 23),
('21222324', 25),

('22232425', 22),
('22232425', 23),
('22232425', 25),

('22334455', 22),
('22334455', 23),
('22334455', 25),

('23242526', 22),
('23242526', 23),
('23242526', 25),

('24252627', 22),
('24252627', 23),
('24252627', 25),

('25262728', 22),
('25262728', 23),
('25262728', 25),

('27282930', 22),
('27282930', 23),
('27282930', 25),

('33445566', 22),
('33445566', 23),
('33445566', 25),

('44556677', 22),
('44556677', 23),
('44556677', 25),

('55667788', 22),
('55667788', 23),
('55667788', 25),

('66778899', 22),
('66778899', 23),
('66778899', 25),

('77889900', 22),
('77889900', 23),
('77889900', 25),

('88990011', 22),
('88990011', 23),
('88990011', 25),

('99001122', 22),
('99001122', 23),
('99001122', 25);

-- INSERT DE LA TABLE SECOURISTE :
-- Secouriste 1 : '12345678' → Cadre Opérationnel (1)
INSERT INTO ListCompSecouriste VALUES 
('12345678', 7), ('12345678', 8), ('12345678', 3), ('12345678', 2), ('12345678', 1);

-- Secouriste 2 : '10111223' → Chef de Poste (2)
INSERT INTO ListCompSecouriste VALUES 
('10111223', 7), ('10111223', 8), ('10111223', 3), ('10111223', 2);

-- Secouriste 3 : '12131415' → Chef d’Équipe (3)
INSERT INTO ListCompSecouriste VALUES 
('12131415', 7), ('12131415', 8), ('12131415', 3);

-- Secouriste 4 : '13141516' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('13141516', 7), ('13141516', 8), ('13141516', 6);

-- Secouriste 5 : '14151617' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('14151617', 7), ('14151617', 8), ('14151617', 6);

-- Secouriste 6 : '15161718' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('15161718', 7), ('15161718', 8), ('15161718', 6);

-- Secouriste 7 : '16171819' → Secouriste montagne (9)
INSERT INTO ListCompSecouriste VALUES 
('16171819', 7), ('16171819', 9);

-- Secouriste 8 : '17181920' → Secouriste montagne (9)
INSERT INTO ListCompSecouriste VALUES 
('17181920', 7), ('17181920', 9);

-- Secouriste 9 : '18192021' → Pilote motoneige (5)
INSERT INTO ListCompSecouriste VALUES 
('18192021', 5);

-- Secouriste 10 : '19202122' → Pilote hélico (4)
INSERT INTO ListCompSecouriste VALUES 
('19202122', 5), ('19202122', 4);

-- Secouriste 11 : '20212223' → Chef d’Équipe (3)
INSERT INTO ListCompSecouriste VALUES 
('20212223', 7), ('20212223', 8), ('20212223', 3);

-- Secouriste 12 : '21222324' → Chef de Poste (2)
INSERT INTO ListCompSecouriste VALUES 
('21222324', 7), ('21222324', 8), ('21222324', 3), ('21222324', 2);

-- Secouriste 13 : '22232425' → Cadre Opérationnel (1)
INSERT INTO ListCompSecouriste VALUES 
('22232425', 7), ('22232425', 8), ('22232425', 3), ('22232425', 2), ('22232425', 1);

-- Secouriste 14 : '22334455' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('22334455', 7), ('22334455', 8), ('22334455', 6);

-- Secouriste 15 : '23242526' → Secouriste montagne (9)
INSERT INTO ListCompSecouriste VALUES 
('23242526', 7), ('23242526', 9);

-- Secouriste 16 : '24252627' → Secouriste montagne (9)
INSERT INTO ListCompSecouriste VALUES 
('24252627', 7), ('24252627', 9);

-- Secouriste 17 : '25262728' → Chef de Poste (2)
INSERT INTO ListCompSecouriste VALUES 
('25262728', 7), ('25262728', 8), ('25262728', 3), ('25262728', 2);

-- Secouriste 18 : '27282930' → Chef d’Équipe (3)
INSERT INTO ListCompSecouriste VALUES 
('27282930', 7), ('27282930', 8), ('27282930', 3);

-- Secouriste 19 : '33445566' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('33445566', 7), ('33445566', 8), ('33445566', 6);

-- Secouriste 20 : '44556677' → Chef de Poste (2)
INSERT INTO ListCompSecouriste VALUES 
('44556677', 7), ('44556677', 8), ('44556677', 3), ('44556677', 2);

-- Secouriste 21 : '55667788' → Pilote hélico (4)
INSERT INTO ListCompSecouriste VALUES 
('55667788', 5), ('55667788', 4);

-- Secouriste 22 : '66778899' → Pilote motoneige (5)
INSERT INTO ListCompSecouriste VALUES 
('66778899', 5);

-- Secouriste 23 : '77889900' → Cadre Opérationnel (1)
INSERT INTO ListCompSecouriste VALUES 
('77889900', 7), ('77889900', 8), ('77889900', 3), ('77889900', 2), ('77889900', 1);

-- Secouriste 24 : '88990011' → Chef de Poste (2)
INSERT INTO ListCompSecouriste VALUES 
('88990011', 7), ('88990011', 8), ('88990011', 3), ('88990011', 2);

-- Secouriste 25 : '99001122' → VPSP (6)
INSERT INTO ListCompSecouriste VALUES 
('99001122', 7), ('99001122', 8), ('99001122', 6);

-- INSERT DE LA TABLE AFFECTATION
INSERT INTO Affectation VALUES (14151617, 8, 59922115); -- PSE2
INSERT INTO Affectation VALUES (15161718, 8, 59922115); -- PSE2
INSERT INTO Affectation VALUES (16171819, 8, 59922115); -- PSE2
INSERT INTO Affectation VALUES (17181920, 8, 59922115); -- PSE2
INSERT INTO Affectation VALUES (18192021, 8, 59922115); -- PSE2

INSERT INTO Affectation VALUES (14151617, 8,22214528 ); -- PSE2

SELECT * FROM Affectation;