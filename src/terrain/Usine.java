package terrain;

import plateau.Joueur;
import ressources.Chemins;

public class Usine extends Propriete {

  public Usine(Joueur joueur) {
    super(
      Chemins.getCheminPropriete(Chemins.FICHIER_USINE, joueur.getId()),
      joueur
    );
  }

  protected String actualiserImage(Joueur joueur) {
    return Chemins.getCheminPropriete(Chemins.FICHIER_USINE, joueur.getId());
  }
}
