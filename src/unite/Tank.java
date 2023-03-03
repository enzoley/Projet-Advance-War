package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class Tank extends Chenille {

  public Tank(Joueur joueur) {
    super(joueur);
    this.prix = 7000;
    this.armes.add(new Canon());
    this.armes.add(new MitrailleuseLegere());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_TANK);
    this.nbDep = 6;
  }

  public String getName() {
    return Noms.Tank;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_TANK
      );
  }
}
