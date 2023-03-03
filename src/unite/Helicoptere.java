package unite;

import arme.*;
import plateau.Joueur;
import ressources.*;

public class Helicoptere extends Unite {

  public Helicoptere(Joueur joueur) {
    super(joueur);
    this.prix = 12000;
    this.armes.add(new Missile());
    this.armes.add(new MitrailleuseLourde());
    this.image =
      Chemins.getCheminUnite(joueur.getId(), true, Chemins.FICHIER_HELICOPTERE);
    this.nbDep = 6;
  }

  public String getName() {
    return Noms.Helicoptere;
  }

  public void setImage(Boolean isDisponible) {
    this.image =
      Chemins.getCheminUnite(
        joueur.getId(),
        isDisponible,
        Chemins.FICHIER_HELICOPTERE
      );
  }
}
