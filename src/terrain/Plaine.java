package terrain;

import ressources.Chemins;

public class Plaine extends Terrain {
	
	public Plaine() {
		super(Chemins.getCheminTerrain(Chemins.FICHIER_PLAINE));
	}

}
