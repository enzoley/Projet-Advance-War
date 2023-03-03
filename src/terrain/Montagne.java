package terrain;

import ressources.Chemins;

public class Montagne extends Terrain {
	
	public Montagne() {
		super(Chemins.getCheminTerrain(Chemins.FICHIER_MONTAGNE));
	}

}
