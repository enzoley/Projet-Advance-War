package unite;

import arme.Arme;
import java.util.ArrayList;
import plateau.*;
import terrain.Foret;
import terrain.Montagne;
import terrain.QG;
import terrain.Usine;
import terrain.Ville;

public abstract class Unite {
	protected double PV;
	protected ArrayList<Arme> armes;
	protected int prix;
	protected String image;
	protected Joueur joueur;
	protected int nbDep;

	public Unite(Joueur joueur) {
		this.PV = 10;
		this.joueur = joueur;
		this.armes = new ArrayList<Arme>();
	}

	public abstract String getName();

	/**
	 * Arrondi un double au dixième (un chiffre après la virgule)
	 *
	 * @param d
	 * @return
	 */
	private double arrondiDixieme(double d) {
		return Math.round(d * 10.0) / 10.0;
	}

	/**
	 * Retire les PV de l'unite en fonction du nombre de degat PV double avec 1
	 * décimale
	 *
	 * @param double degat
	 */
	private void retirerPV(double degat) {
		this.PV = (this.PV - degat < 0) ? 0 : arrondiDixieme(this.PV - degat);
	}

	/**
	 * Arrondi les PV à l'entier superieur
	 *
	 * @return int
	 */
	public int getPVsup() {
		return (int) Math.ceil(this.PV);
	}

	/**
	 * Verifi que l'unitée qui attaque possède une arme en capacitée de porter des
	 * degats a l'unité ennemi
	 *
	 * @param ennemi
	 * @return
	 */
	public boolean peutAttaquer(Unite ennemi) {
		return (getArmePerformante(ennemi).getTableEfficacite().get(ennemi.getName()) != 0);
	}

	/**
	 * Fonction d'attaque, les dégâts infligés par l'unité (A) à la unité ennemi (B)
	 * dépendent de l’arme de l’unité A, du type de l’unité B et des PV de l’unité
	 * A. Sans couverture de terrain
	 *
	 * @param unite ennemi
	 */
	public void attaquer(Unite ennemi) {
		if (this.joueur.estEnnemi(ennemi.getJoueur()) && peutAttaquer(ennemi)) {
			ennemi.retirerPV(
					arrondiDixieme(getPVsup() * getArmePerformante(ennemi).getTableEfficacite().get(ennemi.getName())));
		}
	}

	/**
	 * Fonction d'attaque, les dégâts infligés par l'unité (A) à la unité ennemi (B)
	 * dépendent de l’arme de l’unité A, du type de l’unité B et des PV de l’unité
	 * A. Ils diffères d'un terain à un autre Avec couverture de terrain
	 *
	 * @param unite ennemi
	 */
	public void attaquer(Case caseUniteEnnemi, Unite ennemi) {
		if (this.joueur.estEnnemi(ennemi.getJoueur()) && peutAttaquer(ennemi)) {
			double coef = 1.0;
			if (!(this instanceof Avion)) {
				if (caseUniteEnnemi.getTerrain() instanceof Foret || caseUniteEnnemi.getTerrain() instanceof Usine) {
					coef = 0.8;
				} else if (caseUniteEnnemi.getTerrain() instanceof Ville) {
					coef = 0.7;
				} else if (caseUniteEnnemi.getTerrain() instanceof Montagne
						|| caseUniteEnnemi.getTerrain() instanceof QG) {
					coef = 0.6;
				}
			}
			ennemi.retirerPV(arrondiDixieme(
					getPVsup() * getArmePerformante(ennemi).getTableEfficacite().get(ennemi.getName()) * coef));
		}
	}

	/**
	 * Permet d'obtenir l'arme la plus performante pour un ennemi parmit toutes les
	 * armes que possède l'unitée
	 *
	 * @param Unite ennemi
	 * @return Arme
	 */
	public Arme getArmePerformante(Unite ennemi) {
		Arme armePerformante = this.armes.get(0);
		for (Arme arme : armes) {
			if (arme.getTableEfficacite().get(ennemi.getName()) > armePerformante.getTableEfficacite()
					.get(ennemi.getName())) {
				armePerformante = arme;
			}
		}
		return armePerformante;
	}

	/**
	 * Fonction qui permet de savoir si l'unitée est en vit ou non
	 *
	 * @return boolean
	 */
	public boolean isAlive() {
		return this.PV > 0;
	}

	public double getPV() {
		return PV;
	}

	public void setPV(double pV) {
		PV = pV;
	}

	public ArrayList<Arme> getArmes() {
		return this.armes;
	}

	public void setArmes(ArrayList<Arme> armes) {
		this.armes = armes;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public String getImage() {
		return image;
	}

	/**
	 * Modifier l'image en fonction de la disponibilité de l'unite (true : unité
	 * colorée, false : unité grisée)
	 * 
	 * @param isDisponible
	 */
	public abstract void setImage(Boolean isDisponible);

	public Joueur getJoueur() {
		return this.joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public int getNbDep() {
		return nbDep;
	}

	public void setNbDep(int nbDep) {
		this.nbDep = nbDep;
	}
}
