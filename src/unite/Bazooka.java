package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class Bazooka extends Pied {

  public Bazooka(Joueur joueur) {
    super(joueur);
    this.prix = 3500;
    this.armes.add(new Canon());
    this.armes.add(new MitrailleuseLegere());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_BAZOOKA);
    this.nbDep = 2;
  }

  public String getName() {
    return Noms.Bazooka;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_BAZOOKA
      );
  }
}
