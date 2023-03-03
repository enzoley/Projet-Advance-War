package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class Infanterie extends Pied {

  public Infanterie(Joueur joueur) {
    super(joueur);
    this.prix = 1500;
    this.armes.add(new MitrailleuseLegere());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_INFANTERIE);
    this.nbDep = 3;
  }

  public String getName() {
    return Noms.Infanterie;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_INFANTERIE
      );
  }
}
