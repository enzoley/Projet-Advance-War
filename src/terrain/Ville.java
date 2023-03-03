package terrain;

import plateau.Joueur;
import ressources.Chemins;

public class Ville extends Propriete {

  public Ville(Joueur joueur) {
    super(
      Chemins.getCheminPropriete(Chemins.FICHIER_VILLE, joueur.getId()),
      joueur
    );
  }

  protected String actualiserImage(Joueur joueur) {
    return Chemins.getCheminPropriete(Chemins.FICHIER_VILLE, joueur.getId());
  }
}
