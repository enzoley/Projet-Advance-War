package deplacement;

import java.util.ArrayList;
import java.util.LinkedList;
import librairies.AssociationTouches;
import plateau.Case;
import plateau.Coord;
import plateau.Grille;
import ressources.Affichage;
import ressources.Chemins;
import ressources.Config;
import terrain.*;
import unite.*;

public class DeplacementUnite {
	private LinkedList<Case> chemin;
	private LinkedList<Case> uniteDeplace;
	private boolean actived;

	public DeplacementUnite() {
		this.chemin = new LinkedList<Case>();
		this.uniteDeplace = new LinkedList<Case>();
		setDesactived();
	}

	public LinkedList<Case> getChemin() {
		return this.chemin;
	}

	public void setChemin(LinkedList<Case> chemin) {
		this.chemin = chemin;
	}

	public LinkedList<Case> getCaseUniteDeplace() {
		return this.uniteDeplace;
	}

	public void setUniteteDeplace(LinkedList<Case> uniteDeplace) {
		this.uniteDeplace = uniteDeplace;
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
	 * Fonction qui renvoie le premier élément de type Case de la liste chemin tel
	 * que x et y soient les mêmes que ceux de la Case en paramètre si il existe
	 * sinon null
	 *
	 * @param Case caseCherche
	 * @return la permiere Case tq x et y similaire sinon null
	 */
	private Case getPremiereCase(Case caseCherche) {
		for (Case c : chemin) {
			if (c.coordEquals(caseCherche)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Construction du deplacement des unités sous forme d'une liste qui permet de
	 * conserver en mémoire les cases par lesquelles le curseur est passé pour
	 * éviter les boucles
	 *
	 * @param Case
	 */
	private void addCase(Case c) {
		if (getPremiereCase(c) == null) {
			this.chemin.addLast(c);
		} else if (this.chemin.getLast().equals(c)) {
			// ne riens faire
		} else {
			while (this.chemin.getLast() != getPremiereCase(c)) {
				this.chemin.remove(this.chemin.getLast());
			}
		}
	}

	/**
	 * Deplacer une unitée de la case de depart à la case d'arrivée
	 *
	 * @param caseDepart  de type Case
	 * @param caseArrivee de type Case
	 */
	public void deplacerUnite(Case caseDepart, Case caseArrivee) {
		if (!caseDepart.equals(caseArrivee)) {
			caseArrivee.setUnite(caseDepart.getUnite());
			caseDepart.setUnite(null);
		}
	}

	/**
	 * Deplace l'unité, stocke la case de l'unité deplacée et vide la liste de chemin
	 */
	public void deplacementConfirmer() {
		deplacerUnite(this.chemin.getFirst(), this.chemin.getLast());
		this.uniteDeplace.add(this.chemin.getLast());
		this.chemin.clear();
	}

	/**
	 * Indique le cout du déplacement vers la case1 sachant la caseDepart et donc
	 * l'unité selectionnée
	 *
	 * @param Case caseDepart
	 * @param Case case1
	 * @return int
	 */
	private int coutDeplacement(Case caseDepart, Case case1) {
		if ((case1.getTerrain() instanceof Montagne && caseDepart.getUnite() instanceof Pied)
				|| (case1.getTerrain() instanceof Foret && caseDepart.getUnite() instanceof Chenille)) {
			return 2;
		}
		return 1;
	}

	/**
	 * Indique le nombre de point(s) de déplacement restant(s), il est posible de se
	 * déplacer ssi pointRestant(Case caseSuivante) >= 0
	 *
	 * @param caseSuivante
	 * @return int
	 */
	private int pointRestant(Case caseSuivante) {
		int pointRestant = chemin.getFirst().getUnite().getNbDep() - coutDeplacement(chemin.getFirst(), caseSuivante);
		for (int i = 1; i < chemin.size(); i++) {
			pointRestant -= coutDeplacement(chemin.getFirst(), chemin.get(i));
		}
		return pointRestant;
	}

	/**
	 * Dans le cas ou la caseSuivante possède une unité appartenant au joueur actuel il
	 * peut la traverser si il peut continuer son chemin sans revenir en arrière
	 *
	 * @param caseSuivante
	 * @return
	 */
	private boolean survoleUneUnite(Case caseSuivante, Grille grille) {
		if (caseSuivante.getUnite() != null && !chemin.isEmpty()
				&& caseSuivante.getUnite().getJoueur().equals(chemin.getFirst().getUnite().getJoueur())) {
			ArrayList<Case> casesSuivanteSuivantes = grille.getCasesAdjacentes(caseSuivante);
			casesSuivanteSuivantes.remove(chemin.getLast());
			for (Case caseSuivanteSuivante : casesSuivanteSuivantes) {
				if (pointRestant(caseSuivante) - coutDeplacement(caseSuivante, caseSuivanteSuivante) >= 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Vérification que le déplacement vers la Case caseSuivante est possible
	 *
	 * @param caseSuivante
	 * @return boolean
	 */
	private boolean deplacementPosible(Case caseSuivante, Grille grille) {
		if (this.chemin.isEmpty()) {
			return true;
		} else if (this.chemin.contains(caseSuivante)) {
			return true; // peut revenir sur son chemin
		} else if (pointRestant(caseSuivante) < 0) {
			return false; // il ne reste plus de point de deplacement
		} else if (caseSuivante.getTerrain() instanceof Montagne
				&& this.chemin.getFirst().getUnite() instanceof Chenille) {
			return false; // Montagne, environement infranchisable par une unitée qui se deplace avec des
							// chenilles
		} else if (caseSuivante.getTerrain() instanceof Eau && this.chemin.getFirst().getUnite() instanceof Terrestre) {
			return false; // Eau, environement infranchisable par une unitée Terrestre
		} else if (caseSuivante.getUnite() != null
				&& this.chemin.getFirst().getUnite().getJoueur().estEnnemi(caseSuivante.getUnite().getJoueur())) {
			return false;
		} else if (caseSuivante.getUnite() != null
				&& caseSuivante.getUnite().getJoueur().equals(chemin.getFirst().getUnite().getJoueur())) {
			return survoleUneUnite(caseSuivante, grille);
		}
		return true;
	}

	/**
	 * Implémentation du trajet sous forme d'une liste qui permet de conserver en
	 * mémoire les cases par lesquelles le curseur est passé pour éviter les boucles
	 *
	 * @param toucheSuivante
	 * @param longueurCarteXCases
	 * @param longueurCarteYCases
	 * @param curseur
	 * @param grille
	 */
	public void deplacer(AssociationTouches toucheSuivante, Coord curseur, Grille grille) {
		if (isActived()) {
			if (toucheSuivante.isHaut() && curseur.getY() < Config.longueurCarteYCases - 1
					&& deplacementPosible(grille.getCase(new Coord(curseur.getX(), curseur.getY() + 1)), grille)) {
				curseur.setY(curseur.getY() + 1);
			} else if (toucheSuivante.isBas() && curseur.getY() > 0
					&& deplacementPosible(grille.getCase(new Coord(curseur.getX(), curseur.getY() - 1)), grille)) {
				curseur.setY(curseur.getY() - 1);
			} else if (toucheSuivante.isGauche() && curseur.getX() > 0
					&& deplacementPosible(grille.getCase(new Coord(curseur.getX() - 1, curseur.getY())), grille)) {
				curseur.setX(curseur.getX() - 1);
			} else if (toucheSuivante.isDroite() && curseur.getX() < Config.longueurCarteXCases - 1
					&& deplacementPosible(grille.getCase(new Coord(curseur.getX() + 1, curseur.getY())), grille)) {
				curseur.setX(curseur.getX() + 1);
			}
			addCase(grille.getCase(curseur));
		}
	}

	/**
	 * Liste les cases où l'unité selectionnée peut se deplacer
	 *
	 * @param grille
	 * @return
	 */
	public ArrayList<Case> getDeplacements(Grille grille) {
		ArrayList<Case> deplacements = new ArrayList<Case>();

		if (actived && !chemin.isEmpty()) {
			getDeplacements(deplacements, grille, chemin.getFirst(), chemin.getFirst(),
					chemin.getFirst().getUnite().getNbDep());
		}
		return deplacements;
	}

	/**
	 * Fonction récursive intermediaire
	 *
	 * @param deplacements
	 * @param grille
	 * @param caseCible
	 * @param caseDepart
	 * @param pointRestant
	 */
	private void getDeplacements(ArrayList<Case> deplacements, Grille grille, Case caseCible, Case caseDepart,
			int pointRestant) {
		for (Case case1 : grille.getCasesAdjacentes(caseCible)) {
			if (deplacementPosible(case1, grille) && pointRestant - coutDeplacement(caseDepart, case1) >= 0) {
				if (case1.getUnite() == null) {
					deplacements.add(case1);
				}
				if (pointRestant - coutDeplacement(caseDepart, case1) > 0) {
					getDeplacements(deplacements, grille, case1, caseDepart,
							pointRestant - coutDeplacement(caseDepart, case1));
				}
			}
		}
	}

	/**
	 * Liste des cases que l'unité selectionnée peut attaquer
	 *
	 * @param grille
	 * @return
	 */
	public ArrayList<Case> getCasesAttaquables(Grille grille, ArrayList<Case> casesDeplacementsPossible) {
		ArrayList<Case> casesAttaquables = new ArrayList<Case>();
		if (!chemin.isEmpty()) {
			for (Case case1 : grille.getCasesAdjacentes(chemin.getFirst())) {
				if (case1.getUnite() != null && chemin.getFirst().getUnite() != null
						&& case1.getUnite().getJoueur().estEnnemi(chemin.getFirst().getUnite().getJoueur())
						&& chemin.getFirst().getUnite().peutAttaquer(case1.getUnite())) {
					casesAttaquables.add(case1);
				}
			}
			for (Case ca : casesDeplacementsPossible) {
				for (Case case1 : grille.getCasesAdjacentes(ca)) {
					if (case1.getUnite() != null && chemin.getFirst().getUnite() != null
							&& case1.getUnite().getJoueur().estEnnemi(chemin.getFirst().getUnite().getJoueur())
							&& chemin.getFirst().getUnite().peutAttaquer(case1.getUnite())) {
						casesAttaquables.add(case1);
					}
				}
			}
		}
		return casesAttaquables;
	}

	/**
	 * Liste des cases que l'unité selectionnée peut attaquer avec la position
	 * actuel
	 *
	 * @param grille
	 * @return
	 */
	public ArrayList<Case> getCasesAttaquablesMnt(Grille grille, ArrayList<Case> casesDeplacementsPossible) {
		ArrayList<Case> casesAttaquables = new ArrayList<Case>();
		if (!chemin.isEmpty()) {
			for (Case case1 : grille.getCasesAdjacentes(chemin.getLast())) {
				if (case1.getUnite() != null && chemin.getFirst().getUnite() != null
						&& case1.getUnite().getJoueur().estEnnemi(chemin.getFirst().getUnite().getJoueur())
						&& chemin.getFirst().getUnite().peutAttaquer(case1.getUnite())) {
					casesAttaquables.add(case1);
				}
			}
		}
		return casesAttaquables;
	}

	/**
	 * Fonction qui a pour but de récuperer le chemin de l'image correspondant au
	 * trajet pour une case donnée
	 *
	 * @param Case c
	 * @return String qui permet de récuperer le chemin de l'image correspondant à la
	 *         situation
	 */
	private String getCheminImage(Case c) {
		int i = this.chemin.indexOf(c);
		if (this.chemin.get(i).coordEquals(this.chemin.getFirst())) {
			if (this.chemin.get(i).coordEquals(this.chemin.getLast())) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DEBUT, Chemins.DIRECTION_FIN);
			} else if (this.chemin.get(i + 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DEBUT, Chemins.DIRECTION_GAUCHE);
			} else if (this.chemin.get(i + 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DEBUT, Chemins.DIRECTION_DROITE);
			} else if (this.chemin.get(i + 1).getCoord().getY() < this.chemin.get(i).getCoord().getY()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DEBUT, Chemins.DIRECTION_BAS);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DEBUT, Chemins.DIRECTION_HAUT);
			}
		} else if (this.chemin.get(i) == this.chemin.getLast()) {
			if (this.chemin.get(i - 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_GAUCHE, Chemins.DIRECTION_FIN);
			} else if (this.chemin.get(i - 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DROITE, Chemins.DIRECTION_FIN);
			} else if (this.chemin.get(i - 1).getCoord().getY() < this.chemin.get(i).getCoord().getY()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_BAS, Chemins.DIRECTION_FIN);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_HAUT, Chemins.DIRECTION_FIN);
			}
		} else if (this.chemin.get(i - 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
			if (this.chemin.get(i + 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_GAUCHE, Chemins.DIRECTION_DROITE);
			} else if (this.chemin.get(i + 1).getCoord().getY() < this.chemin.get(i).getCoord().getY()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_GAUCHE, Chemins.DIRECTION_BAS);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_GAUCHE, Chemins.DIRECTION_HAUT);
			}
		} else if (this.chemin.get(i - 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
			if (this.chemin.get(i + 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DROITE, Chemins.DIRECTION_GAUCHE);
			} else if (this.chemin.get(i + 1).getCoord().getY() < this.chemin.get(i).getCoord().getY()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DROITE, Chemins.DIRECTION_BAS);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_DROITE, Chemins.DIRECTION_HAUT);
			}
		} else if (this.chemin.get(i - 1).getCoord().getY() < this.chemin.get(i).getCoord().getY()) {
			if (this.chemin.get(i + 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_BAS, Chemins.DIRECTION_GAUCHE);
			} else if (this.chemin.get(i + 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_BAS, Chemins.DIRECTION_DROITE);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_BAS, Chemins.DIRECTION_HAUT);
			}
		} else {
			if (this.chemin.get(i + 1).getCoord().getX() < this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_HAUT, Chemins.DIRECTION_GAUCHE);
			} else if (this.chemin.get(i + 1).getCoord().getX() > this.chemin.get(i).getCoord().getX()) {
				return Chemins.getCheminFleche(Chemins.DIRECTION_HAUT, Chemins.DIRECTION_DROITE);
			} else {
				return Chemins.getCheminFleche(Chemins.DIRECTION_HAUT, Chemins.DIRECTION_BAS);
			}
		}
	}

	/**
	 * Affiche le trajet, le deplacement sous forme d’une flèche parcourant les
	 * cases. Utilisée dans la fonction void display() dans main.Jeu
	 */
	public void affiche() {
		if (isActived()) {
			for (Case c : this.chemin) {
				Affichage.dessineImageDansCase(c.getCoord().getX(), c.getCoord().getY(), getCheminImage(c));
			}
		}
	}
}
