package unite;

import arme.*;
import plateau.Joueur;
import ressources.Chemins;
import ressources.Noms;

public class Bombardier extends Avion {

  public Bombardier(Joueur joueur) {
    super(joueur);
    this.prix = 20000;
    this.armes.add(new Bombe());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_BOMBARDIER);
    this.nbDep = 7;
  }

  public String getName() {
    return Noms.Bombardier;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_BOMBARDIER
      );
  }
}
