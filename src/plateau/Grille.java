package plateau;

import deplacement.Deplacement;
import java.util.*;
import ressources.Affichage;
import ressources.Config;
import terrain.*;
import unite.*;

public class Grille {
	private ArrayList<Case> grille;

	public Grille() {
		this.grille = new ArrayList<Case>();
	}

	public void add(Case c) {
		this.grille.add(c);
	}

	public ArrayList<Case> getGrille() {
		return grille;
	}

	public void setGrille(ArrayList<Case> grille) {
		this.grille = grille;
	}

	/**
	 * Return la case ayant les coord fournit en paramêtre
	 *
	 * @param Coord : coord
	 * @return Case : case ou null
	 */
	public Case getCase(Coord coord) {
		for (Case p_case : grille) {
			if (p_case.getCoord().equals(coord)) {
				return p_case;
			}
		}
		return null;
	}

	public ArrayList<Case> getCasesAdjacentes(Case case1) {
		ArrayList<Case> casesVoisines = new ArrayList<Case>();
		for (Case p_case : grille) {
			if ((p_case.getCoord().equals(new Coord(case1.getCoord().getX() - 1, case1.getCoord().getY()))
					&& case1.getCoord().getX() > 0)
					|| (p_case.getCoord().equals(new Coord(case1.getCoord().getX() + 1, case1.getCoord().getY()))
							&& case1.getCoord().getX() < Config.longueurCarteXCases - 1)
					|| (p_case.getCoord().equals(new Coord(case1.getCoord().getX(), case1.getCoord().getY() - 1))
							&& case1.getCoord().getY() > 0)
					|| (p_case.getCoord().equals(new Coord(case1.getCoord().getX(), case1.getCoord().getY() + 1))
							&& case1.getCoord().getY() < Config.longueurCarteYCases - 1)) {
				casesVoisines.add(p_case);
			}
		}
		return casesVoisines;
	}

	/**
	 * Calcul le nombre de proprietées détenues par un joueur
	 *
	 * @param Joueur joueur
	 * @return in
	 */
	private int nbPropriete(Joueur joueur) {
		int nbPropriete = 0;
		for (Case case1 : grille) {
			if (case1.getTerrain() instanceof Propriete
					&& ((Propriete) case1.getTerrain()).getJoueur().equals(joueur)) {
				nbPropriete++;
			}
		}
		return nbPropriete;
	}

	/**
	 * Toute propriété possédée par un joueur lui rapporte 1000 crédits
	 * suplémentaire
	 *
	 * @param joueur
	 */
	public void actualiserCredits(Joueur joueur) {
		joueur.setNbCredit(joueur.getNbCredit() + nbPropriete(joueur) * 1000);
	}

	/**
	 * Liste toute les cases où le joueur à une unité qui peut être deplacée
	 * 
	 * @param Joueur joueur
	 * @return ArrayList<Case>
	 */
	public ArrayList<Case> getCasesUniteJoueurNonDeplace(Joueur joueur, Deplacement deplacement) {
		ArrayList<Case> res = new ArrayList<Case>();
		for (Case case1 : grille) {
			if (case1.getUnite() != null && case1.getUnite().getJoueur().equals(joueur)
					&& !deplacement.getUnite().getCaseUniteDeplace().contains(case1)) {
				res.add(case1);
			}
		}
		return res;
	}

	/**
	 * True si il n'est pas possible de produire d'autre unitée avec les usines du
	 * joueur
	 * 
	 * @param joueur
	 * @return
	 */
	public boolean usinesPleines(Joueur joueur) {
		for (Case case1 : grille) {
			if (case1.getTerrain() instanceof Usine && case1.getUnite() == null
					&& ((Usine) case1.getTerrain()).getJoueur().equals(joueur) && joueur.getNbCredit() >= 1500) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Renvoi un case où il est possible pour le joueur de produir une unitée
	 *
	 * @param joueur
	 * @return Case
	 */
	public Case getCaseProduirUnite(Joueur joueur) {
		for (Case case1 : grille) {
			if (case1.getTerrain() instanceof Usine && case1.getUnite() == null
					&& ((Usine) case1.getTerrain()).getJoueur().equals(joueur) && joueur.getNbCredit() >= 1500) {
				return case1;
			}
		}
		return null;
	}

	/**
	 * Modeliser le tableau 2D de String en Objet, selon notre structure Utilisé
	 * dans le constructeur Jeu() dans main.Jeu
	 *
	 * @param carteString
	 */
	public void afficher(String[][] carteString, Joueur joueurNeutre, Joueur joueurRouge, Joueur joueurBleu) {
		for (int y = 0; y < carteString.length; y++) {
			for (int x = 0; x < carteString[0].length; x++) {
				if (carteString[y][x].contains("Eau")) {
					this.grille.add(new Case(new Eau(), new Coord(x, y)));
				} else if (carteString[y][x].contains("Foret")) {
					this.grille.add(new Case(new Foret(), new Coord(x, y)));
				} else if (carteString[y][x].contains("Montagne")) {
					this.grille.add(new Case(new Montagne(), new Coord(x, y)));
				} else if (carteString[y][x].contains("Plaine")) {
					this.grille.add(new Case(new Plaine(), new Coord(x, y)));
				} else if (carteString[y][x].contains("QG")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.add(new Case(new QG(joueurRouge), new Coord(x, y)));
					} else {
						this.grille.add(new Case(new QG(joueurBleu), new Coord(x, y)));
					}
				} else if (carteString[y][x].contains("Usine")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.add(new Case(new Usine(joueurRouge), new Coord(x, y)));
					} else if (carteString[y][x].contains(":2")) {
						this.grille.add(new Case(new Usine(joueurBleu), new Coord(x, y)));
					} else {
						this.grille.add(new Case(new Usine(joueurNeutre), new Coord(x, y)));
					}
				} else if (carteString[y][x].contains("Ville")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.add(new Case(new Ville(joueurRouge), new Coord(x, y)));
					} else if (carteString[y][x].contains(":2")) {
						this.grille.add(new Case(new Ville(joueurBleu), new Coord(x, y)));
					} else {
						this.grille.add(new Case(new Ville(joueurNeutre), new Coord(x, y)));
					}
				}
				if (carteString[y][x].contains(";Bazooka")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new Bazooka(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new Bazooka(joueurBleu));
					}
				} else if (carteString[y][x].contains(";Bombardier")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new Bombardier(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new Bombardier(joueurBleu));
					}
				} else if (carteString[y][x].contains(";DCA")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new DCA(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new DCA(joueurBleu));
					}
				} else if (carteString[y][x].contains(";Helico")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new Helicoptere(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new Helicoptere(joueurBleu));
					}
				} else if (carteString[y][x].contains(";Infanterie")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new Infanterie(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new Infanterie(joueurBleu));
					}
				} else if (carteString[y][x].contains(";Tank")) {
					if (carteString[y][x].contains(":1")) {
						this.grille.get(this.grille.size() - 1).setUnite(new Tank(joueurRouge));
					} else {
						this.grille.get(this.grille.size() - 1).setUnite(new Tank(joueurBleu));
					}
				}
				// System.out.print(carteString[y][x]);
				// if (x != carteString[0].length) {
				// System.out.print(" | ");
				// } else {
				// System.out.println();
				// }
			}
			// System.out.println();
		}
	}

	/**
	 * Affiche pour chaque cases le terain et l'unité
	 */
	public void afficherTerrainEtUnite() {
		for (Case ca : this.grille) {
			Affichage.dessineImageDansCase(ca.getCoord().getX(), ca.getCoord().getY(), ca.getTerrain().getImage());
			if (ca.getUnite() != null) {
				Affichage.dessineImageDansCase(ca.getCoord().getX(), ca.getCoord().getY(), ca.getUnite().getImage());
			}
		}
	}

	public void afficherCasesDeplacementsPossible(Deplacement deplacement, ArrayList<Case> casesDeplacementsPossible) {
		if (!deplacement.getUnite().getChemin().isEmpty()
				&& deplacement.getUnite().getChemin().getFirst().getUnite() != null) {
			for (Case ca : this.grille) {
				if (casesDeplacementsPossible.contains(ca)) {
					Affichage.afficherCasesDeplacementsPossible(ca.getCoord().getX(), ca.getCoord().getY());
				}
				if (deplacement.getUnite().getCasesAttaquablesMnt(this, casesDeplacementsPossible).contains(ca)) {
					Affichage.afficherCasesAttaquesPossibleMnt(ca.getCoord().getX(), ca.getCoord().getY());
				} else if (deplacement.getUnite().getCasesAttaquables(this, casesDeplacementsPossible).contains(ca)) {
					Affichage.afficherCasesAttaquesPossible(ca.getCoord().getX(), ca.getCoord().getY());
				}
			}
		}
	}

	public void reinitialiserImageUnite() {
		for (Case ca : this.grille) {
			if (ca.getUnite() != null) {
				ca.getUnite().setImage(true);
			}
		}
	}
}
