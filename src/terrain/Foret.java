package terrain;

import ressources.Chemins;

public class Foret extends Terrain {
	
	public Foret() {
		super(Chemins.getCheminTerrain(Chemins.FICHIER_FORET));
	}

}
