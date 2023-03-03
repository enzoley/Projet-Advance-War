package deplacement;

import librairies.AssociationTouches;
import plateau.Coord;
import ressources.Config;

public class DeplacementCurseur {
	private Coord curseur;
	private boolean actived;

	public DeplacementCurseur(Coord curseur) {
		this.curseur = curseur;
		setActived();
	}

	public Coord getCurseur() {
		return this.curseur;
	}

	public void setCurseur(Coord curseur) {
		this.curseur = curseur;
	}

	public boolean isActived() {
		return this.actived;
	}

	public void setActived() {
		this.actived = true;
	}

	public void setDesactived() {
		this.actived = false;
	}

	/**
	 * Permet de parcourir toutes les cases de la grille avec le curseur
	 *
	 * @param toucheSuivante
	 * @param longueurCarteXCases
	 * @param longueurCarteYCases
	 */
	public void deplacer(AssociationTouches toucheSuivante) {
		if (isActived()) {
			if (toucheSuivante.isHaut() && this.curseur.getY() < Config.longueurCarteYCases - 1) {
				this.curseur.setY(this.curseur.getY() + 1);
			} else if (toucheSuivante.isBas() && this.curseur.getY() > 0) {
				this.curseur.setY(this.curseur.getY() - 1);
			} else if (toucheSuivante.isGauche() && this.curseur.getX() > 0) {
				this.curseur.setX(this.curseur.getX() - 1);
			} else if (toucheSuivante.isDroite() && this.curseur.getX() < Config.longueurCarteXCases - 1) {
				this.curseur.setX(this.curseur.getX() + 1);
			}
		}
	}
}
