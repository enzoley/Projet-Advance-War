package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class DCA extends Chenille {

  public DCA(Joueur joueur) {
    super(joueur);
    this.prix = 6000;
    this.armes.add(new MitrailleuseLourde());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_ANTIAIR);
    this.nbDep = 6;
  }

  public String getName() {
    return Noms.DCA;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_ANTIAIR
      );
  }
}
