package terrain;

import ressources.Chemins;

public class Eau extends Terrain {
	
	public Eau() {
		super(Chemins.getCheminTerrain(Chemins.FICHIER_EAU));
	}

}
