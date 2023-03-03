package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class Convoi extends Chenille {

  public Convoi(Joueur joueur) {
    super(joueur);
    this.prix = 5000;
    this.armes.add(new Desarme());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_GENIE);
    this.nbDep = 6;
  }

  public String getName() {
    return Noms.Convoi;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_GENIE
      );
  }
}
