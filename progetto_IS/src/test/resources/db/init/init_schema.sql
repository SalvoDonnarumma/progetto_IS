-- Table `storageprogetto`.`utente`
CREATE TABLE IF NOT EXISTS `utente` (
  `idutente` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(130) NULL,
  `nome` VARCHAR(45) NULL,
  `cognome` VARCHAR(45) NULL,
  `telefono` VARCHAR(11) NULL,
  `ruolo` VARCHAR(45) NULL,
  PRIMARY KEY (`email`)
);

-- Table `storageprogetto`.`carta`
CREATE TABLE IF NOT EXISTS `carta` (
  `idcarta` INT NOT NULL,
  `proprietario` VARCHAR(50) NULL,
  `numero_carta` VARCHAR(25) NULL,
  `data_scadenza` VARCHAR(20) NULL,
  PRIMARY KEY (`idcarta`)
  -- FOREIGN KEY (`idcarta`) REFERENCES `utente` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table `storageprogetto`.`ordine`
CREATE TABLE IF NOT EXISTS `ordine` (
  `idOrdine` INT NOT NULL AUTO_INCREMENT,
  `idUtente` VARCHAR(45) NULL,
  `data` VARCHAR(45) NULL,
  `stato` VARCHAR(45) NULL,
  `prezzototale` FLOAT NULL,
  `indirizzo` VARCHAR(45) NULL,
  CONSTRAINT `fk_ordine_utente_idx` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Table `storageprogetto`.`prodotto`
CREATE TABLE IF NOT EXISTS `prodotto` (
  `idProdotto` INT NOT NULL AUTO_INCREMENT,
  `categoria` VARCHAR(45) NULL,
  `nome` VARCHAR(45) NULL,
  `price` FLOAT NULL,
  `descrizione` TEXT NULL,
  `photo` LONGBLOB NULL,
  `stats` TEXT NULL,
  `image` VARCHAR(255) NULL,
  PRIMARY KEY (`idProdotto`)
);

-- Table `storageprogetto`.`prodottocarrello`
CREATE TABLE IF NOT EXISTS `prodottocarrello` (
  `idcarrello` INT NOT NULL,
  `idprodottoc` INT NOT NULL,
  PRIMARY KEY (`idcarrello`, `idprodottoc`),
  CONSTRAINT `fk_prodottocarrello_utente` FOREIGN KEY (`idcarrello`) REFERENCES `utente` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_prodottocarrello_prodotto` FOREIGN KEY (`idprodottoc`) REFERENCES `prodotto` (`idProdotto`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table `storageprogetto`.`prodottoordinato`
CREATE TABLE IF NOT EXISTS `prodottoordinato` (
  `idOrdine` INT NOT NULL,
  `idProdotto` INT NOT NULL,
  `nome` VARCHAR(45) NULL,
  `categoria` VARCHAR(45) NULL,
  `prezzo` FLOAT NULL,
  `quantita` INT NULL,
  `size` VARCHAR(5) NULL,
  PRIMARY KEY (`idOrdine`, `idProdotto`),
  CONSTRAINT `fk_prodottoordinato_ordine` FOREIGN KEY (`idOrdine`) REFERENCES `ordine` (`idOrdine`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_prodottoordinato_prodotto` FOREIGN KEY (`idProdotto`) REFERENCES `prodotto` (`idProdotto`) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Table `storageprogetto`.`taglie`
CREATE TABLE IF NOT EXISTS `taglie` (
  `idProdotto` INT NOT NULL,
  `tagliaM` INT NULL,
  `tagliaL` INT NULL,
  `tagliaXL` INT NULL,
  `tagliaXXL` INT NULL,
  PRIMARY KEY (`idProdotto`),
  CONSTRAINT `fk_taglie_prodotto` FOREIGN KEY (`idProdotto`) REFERENCES `prodotto` (`idProdotto`) ON DELETE CASCADE ON UPDATE CASCADE
);

