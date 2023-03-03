package deplacement;

import plateau.Case;
import plateau.Coord;

public class Deplacement {
	private DeplacementUnite unite;
	private DeplacementCurseur curseur;

	public Deplacement(Coord curseur) {
		this.unite = new DeplacementUnite();
		this.curseur = new DeplacementCurseur(curseur);
	}

	public DeplacementUnite getUnite() {
		return this.unite;
	}

	public void setUnite(DeplacementUnite unite) {
		this.unite = unite;
	}

	public DeplacementCurseur getCurseur() {
		return this.curseur;
	}

	public void setCurseur(DeplacementCurseur curseur) {
		this.curseur = curseur;
	}

	/**
	 * Renvoie un objet de type Coord <=> les coordonées x et y du curseur
	 *
	 * @return Coord
	 */
	public Coord getCoordCurseur() {
		return this.curseur.getCurseur();
	}

	/**
	 * Modifie les coordonnées du curseur
	 * 
	 * @param Coord coordCurseur
	 */
	public void setCoordCurseur(Coord coordCurseur) {
		this.curseur.setCurseur(coordCurseur);
	}

	public void dontMove(Case case1) {
		getCurseur().setDesactived();
		getUnite().setDesactived();
		setCoordCurseur(new Coord(case1.getCoord().getX(), case1.getCoord().getY()));
	}

	/*
	 * Configuration dans laquelle on peut changer le curseur de case
	 */
	public void MoveCurseur() {
		getCurseur().setActived();
		getUnite().setDesactived();
	}

	/*
	 * Configuration dans laquelle on peut changer l'unité de case
	 */
	public void MoveUnite() {
		getCurseur().setDesactived();
		getUnite().setActived();
	}

	/*
	 * On peut changer le curseur de case
	 */
	public boolean canMoveCurseur() {
		return getCurseur().isActived() && !getUnite().isActived();
	}

	/*
	 * On peut changer l'unité de case
	 */
	public boolean canMoveUnite() {
		return getUnite().isActived() && !getCurseur().isActived();
	}

	/*
	 * Deselectionner l'unité <=> suprimer le chemin parcouru par l'unité (flèche
	 * bleue) + mode curseur + mettre le curseur à sa position avant l'implementation
	 * du chemin de l'unité
	 */
	public void deselectionerUnite() {
		MoveCurseur();
		setCoordCurseur(new Coord(getUnite().getChemin().getFirst().getCoord().getX(),
				getUnite().getChemin().getFirst().getCoord().getY()));
		getUnite().getChemin().clear();
	}
}
