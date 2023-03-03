package terrain;

import plateau.Joueur;
import ressources.Chemins;

public class QG extends Propriete {

  public QG(Joueur joueur) {
    super(
      Chemins.getCheminPropriete(Chemins.FICHIER_QG, joueur.getId()),
      joueur
    );
  }

  protected String actualiserImage(Joueur joueur) {
    return Chemins.getCheminPropriete(Chemins.FICHIER_QG, joueur.getId());
  }
}
