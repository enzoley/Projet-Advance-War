/** package principal */
package main;

import action.Action;
import deplacement.Deplacement;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import librairies.AssociationTouches;
import librairies.StdDraw;
import plateau.*;
import plateau.Case;
import ressources.*;
import terrain.Propriete;
import terrain.Usine;
import unite.*;

public class Jeu {
	private int indexJoueurActif; // l'indice du joueur actif: 1 = rouge, 2 = bleu
	// l'indice 0 est reserve au neutre, qui ne joue pas mais peut posseder des
	// proprietes

	private Grille grille;
	private Deplacement deplacement;
	private Joueur joueurNeutre;
	private Joueur joueurRouge;
	private Joueur joueurBleu;
	private Action action;
	private int nbTour;
	private boolean chagementJoueurRougeAuto;
	private boolean chagementJoueurBleuAuto;
	private ArrayList<Case> casesDeplacementsPossible;

	public Jeu(String fileName) throws Exception {
		// appel au parseur, qui renvoie un tableau de String
		String[][] carteString = ParseurCartes.parseCarte(fileName);

		this.grille = new Grille();
		this.joueurNeutre = new Joueur(0);
		this.joueurRouge = new Joueur(1);
		this.joueurBleu = new Joueur(2);
		this.action = new Action();
		this.nbTour = 1;
		chagementJoueurRougeAuto = false;
		chagementJoueurBleuAuto = false;
		casesDeplacementsPossible = new ArrayList<Case>();

		// Modeliser le tableau 2D de String en Objet, selon notre structure
		grille.afficher(carteString, joueurNeutre, joueurRouge, joueurBleu);

		// a vous de manipuler ce tableau de String pour le transformer en une carte
		// avec vos propres classes, a l'aide de la methode split de la classe String

		Config.setDimension(carteString[0].length, carteString.length);
		// initialise la configuration avec la longueur de la carte

		this.deplacement = new Deplacement(new Coord(0, 0));
		indexJoueurActif = joueurRouge.getId();
		// rouge commence
	}

	public boolean isOver() {
		return this.action.isOver();
	}

	public void afficheStatutJeu() {
		Affichage.videZoneTexte();
		if (!isOver()) {
			Affichage.afficheTexteDescriptif("Partie en cours");
		} else {
			Affichage.afficheTexteDescriptif("Le jeu est fini, le joueur "
					+ ((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain()).getJoueur()
							.getNameAdversaire()
					+ " a gagné");
		}
		Color couleur;
		if (this.indexJoueurActif == 1) {
			couleur = Color.RED;
		} else {
			couleur = Color.BLUE;
		}

		Affichage.afficheTexteDansCase(0, Config.longueurCarteYCases,
				"Joueur " + Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).getName()
						+ " : "
						+ Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).getNbCredit(),
				couleur, 1.5, 0.5, Font.decode("PLAIN"));

		if (grille.getCase(deplacement.getCoordCurseur()).getUnite() != null) {
			Affichage.afficheTexteDansCase(Config.longueurCarteXCases - 2, Config.longueurCarteYCases,
					"PV : " + (int) (grille.getCase(deplacement.getCoordCurseur()).getUnite().getPV()),
					Color.BLACK, 0, 0.5, Font.decode("PLAIN"));
		}
		if (grille.getCase(deplacement.getCoordCurseur()).getTerrain() instanceof Propriete) {
			Affichage.afficheTexteDansCase(Config.longueurCarteXCases - 2, Config.longueurCarteYCases,
					"PV du batiment : "
							+ (int) ((((Propriete) grille.getCase(deplacement.getCoordCurseur()).getTerrain()).getPV())),
					Color.BLACK, 0, 1, Font.decode("PLAIN"));
		}
	}

	public void display() {
		StdDraw.clear();
		afficheStatutJeu();

		// Affiche les terrains (environnemnt) et les Unitees
		grille.afficherTerrainEtUnite();
		grille.afficherCasesDeplacementsPossible(deplacement, casesDeplacementsPossible);

		// Affiche le deplacement d'une troupe selectionnée sous forme d’une fleche
		// parcourant les cases
		this.deplacement.getUnite().affiche();

		Affichage.dessineGrille(); // affiche une grille, mais n'affiche rien dans les cases
		drawGameCursor();
		StdDraw.show(); // montre a l'ecran les changement demandes
	}

	public void initialDisplay() {
		StdDraw.enableDoubleBuffering(); // rend l'affichage plus fluide: tout draw est mis en buffer et ne s'affiche
		// qu'au prochain StdDraw.show();
		display();
	}

	public void drawGameCursor() {
		Affichage.dessineCurseur(this.deplacement.getCoordCurseur().getX(), this.deplacement.getCoordCurseur().getY());
	}

	public void update() {
		AssociationTouches toucheSuivante = AssociationTouches.trouveProchaineEntree(); // cette fonction boucle jusqu'a
		// la prochaine entree de
		// l'utilisateur

		changerJoueur(toucheSuivante);
		selectionerUnite(toucheSuivante);
		deselectionerUnite(toucheSuivante);
		confirmerDeplacementUnite(toucheSuivante);
		produireUnite(toucheSuivante);
		this.deplacement.getCurseur().deplacer(toucheSuivante);
		this.deplacement.getUnite().deplacer(toucheSuivante, this.deplacement.getCoordCurseur(), grille);
		uniteNonDeplace(toucheSuivante, deplacement);
		if (deplacement.getUnite().getChemin().size() == 1) {
			casesDeplacementsPossible = deplacement.getUnite().getDeplacements(grille);
		}
		if (deplacement.getUnite().getChemin().isEmpty()) {
			casesDeplacementsPossible.clear();
		}
		for (Case c : grille.getGrille()) {
			if (c.getTerrain() instanceof Propriete && c.getUnite() == null) {
				((Propriete) c.getTerrain()).restaurerPV(); //Ici on vérifie si les propriétés sont occupées par une unité sinon on restaure leurs PVs
			}
		}
		display();
	}

	/**
	 * Config qui permet de selectionner une unité appartenant au joueur actif
	 *
	 * @param toucheSuivante
	 */
	private void selectionerUnite(AssociationTouches toucheSuivante) {
		if (this.deplacement.canMoveCurseur() && toucheSuivante.isEntree()
				&& this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite() != null
				&& this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite().getJoueur()
						.getId() == indexJoueurActif
				&& !this.deplacement.getUnite().getCaseUniteDeplace()
						.contains(this.grille.getCase(this.deplacement.getCoordCurseur()))) {
			this.deplacement.MoveUnite();
		}
	}

	/**
	 * Configuration qui permet de déselectionner l'unitée selectionnée
	 *
	 * @param toucheSuivante
	 */
	private void deselectionerUnite(AssociationTouches toucheSuivante) {
		if (this.deplacement.canMoveUnite() && toucheSuivante.isEchap()) {
			this.deplacement.deselectionerUnite();
		}
	}

	/**
	 * Confirmer le deplacement de l'unité et effecter les actions selectionées
	 * (Attendre, Attaquer, Capturer)
	 *
	 * @param toucheSuivante
	 */
	private void confirmerDeplacementUnite(AssociationTouches toucheSuivante) {
		if (this.deplacement.canMoveUnite() && toucheSuivante.isEntree()
				&& !this.deplacement.getUnite().getChemin().isEmpty()
				&& (this.deplacement.getUnite().getChemin().getLast().getUnite() == null
						|| this.deplacement.getUnite().getChemin().size() == 1)) {
			this.action.confirmerDeplacement(this.grille, this.deplacement);
		}
		finirAttaque(toucheSuivante);
	}

	/**
	 * Dans le cas ou il y a plusieurs untités attaquable alors vous pouvez choisir
	 * laquel avec les flêches directionnels ou entré pour attaquer l'unité où ce
	 * trouve le curseur tapé 'a'
	 *
	 * @param toucheSuivante
	 */
	private void finirAttaque(AssociationTouches toucheSuivante) {
		if (!this.action.getCasesAttaquables().isEmpty()) {
			this.action.upIndice();
			this.deplacement.dontMove(this.action.getOneCaseAttaquable());
			if (toucheSuivante.isCaractere('a')) {
				this.action.finirAttaque(this.deplacement);
			}
		}
	}

	/**
	 * Changement de joueur actif
	 *
	 * @param toucheSuivante
	 */
	private void changerJoueur(AssociationTouches toucheSuivante) {
		changerJoueurManuel(toucheSuivante);
		changerJoueurAuto();
		changerModeChangerJoueur(toucheSuivante,
				Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu));
	}

	/**
	 * True si le joueur courant est en mode auto pour le changement de joueur
	 * 
	 * @return boolean
	 */
	private boolean chagementJoueurAuto() {
		if (Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).estBleu()) {
			return chagementJoueurBleuAuto;
		} else {
			return chagementJoueurRougeAuto;
		}
	}

	/**
	 * Changement de joueur actif en mode manuel
	 *
	 * @param toucheSuivante
	 */
	private void changerJoueurManuel(AssociationTouches toucheSuivante) {
		// ATTENTION ! si vous voulez detecter d'autres touches que 't',
		// vous devez les ajouter au tableau Config.TOUCHES_PERTINENTES_CARACTERES
		if (toucheSuivante.isCaractere('t') && !chagementJoueurAuto()) {
			String[] options = { "Oui" };
			if (Affichage.popup(
					"Finir le tour du joueur "
							+ Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).getName(),
					options, true, 1) == 0) {
				// Passer au joueur suivant
				joueurSuivant();
			}
		}
	}

	/**
	 * Changement de joueur actif en mode automatique
	 *
	 * @param toucheSuivante
	 */
	private void changerJoueurAuto() {
		// ATTENTION ! si vous voulez detecter d'autres touches que 't',
		// vous devez les ajouter au tableau Config.TOUCHES_PERTINENTES_CARACTERES
		if (chagementJoueurAuto()
				&& grille.getCasesUniteJoueurNonDeplace(
						Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu), deplacement)
						.isEmpty()
				&& grille.usinesPleines(
						Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu))) {
			joueurSuivant();
		}
	}

	private void changerModeChangerJoueur(AssociationTouches toucheSuivante, Joueur joueurActuel) {
		if (toucheSuivante.isCaractere('j')) {
			if (joueurActuel.estBleu()) {
				if (chagementJoueurBleuAuto) {
					chagementJoueurBleuAuto = false;
					System.out.println("Joueur bleu : fin de tour manuel en appuyant sur 't'");
				} else {
					chagementJoueurBleuAuto = true;
					System.out.println("Joueur bleu : fin de tour automatique");
				}
			} else if (joueurActuel.estRouge()) {
				if (chagementJoueurRougeAuto) {
					chagementJoueurRougeAuto = false;
					System.out.println("Joueur rouge : fin de tour manuel en appuyant sur 't'");
				} else {
					chagementJoueurRougeAuto = true;
					System.out.println("Joueur rouge : fin de tour automatique");
				}
			}
		}
	}

	/**
	 * Passer au joueur suivant
	 */
	private void joueurSuivant() {
		grille.reinitialiserImageUnite();
		if (indexJoueurActif == joueurRouge.getId()) {
			indexJoueurActif = joueurBleu.getId();
			if (this.nbTour > 1) {
				this.grille.actualiserCredits(joueurBleu);
			}
			this.nbTour++;
		} else {
			indexJoueurActif = joueurRouge.getId();
			if (this.nbTour > 1) {
				this.grille.actualiserCredits(joueurRouge);
			}
		}
		this.deplacement.getUnite().getCaseUniteDeplace().clear();
		System.out.println("Joueur actuel : "
				+ (Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)).getName());
	}

	/**
	 * Permet de produire des unitées si le joueur selectonne une usine lui
	 * appartenant sans unitée dessus
	 *
	 * @param toucheSuivante
	 */
	private void produireUnite(AssociationTouches toucheSuivante) {
		if (toucheSuivante.isEntree() && this.deplacement.canMoveCurseur()
				&& this.grille.getCase(this.deplacement.getCoordCurseur()).getTerrain() instanceof Usine
				&& this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite() == null
				&& ((Usine) this.grille.getCase(this.deplacement.getCoordCurseur()).getTerrain()).getJoueur()
						.equals(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu))) {
			String message = "Choisissez une unité à produire";
			Map<Integer, Unite> map = getMapIntegerUnites();
			if (!map.isEmpty()) {
				this.grille.getCase(this.deplacement.getCoordCurseur())
						.setUnite(map.get(Affichage.popup(message, getOptions(map), true, 1)));
				if (this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite() != null) {
					this.deplacement.getUnite().getCaseUniteDeplace()
							.add(this.grille.getCase(this.deplacement.getCoordCurseur()));
					Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).retirerCredits(
							this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite().getPrix());
					this.grille.getCase(this.deplacement.getCoordCurseur()).getUnite().setImage(false);
				}
			}
		}
	}

	/**
	 * Pour chaque unitées vérifier que le joueur actif est le nb de credits que
	 * coute l'unité, si c'est le cas la mettre dans la map
	 *
	 * @return Map<Integer, Unite> map
	 */
	private Map<Integer, Unite> getMapIntegerUnites() {
		Map<Integer, Unite> map = new HashMap<Integer, Unite>();
		ArrayList<Unite> list = new ArrayList<Unite>();
		list.add(new Infanterie(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
		list.add(new Bazooka(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
		list.add(new Tank(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
		list.add(new DCA(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
		list.add(new Helicoptere(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
		list.add(new Bombardier(Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));

		int key = 0;
		for (Unite unite1 : list) {
			if (Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu).getNbCredit()
					- unite1.getPrix() >= 0) {
				map.put(key, unite1);
				key++;
			}
		}

		return map;
	}

	/**
	 * Obtient le tableau de Nom des Unitées présente dans la map
	 *
	 * @param Map<Integer, Unite> map
	 * @return String[] options
	 */
	private String[] getOptions(Map<Integer, Unite> map) {
		String[] option = new String[map.size()];
		for (int i = 0; i < option.length; i++) {
			option[i] = map.get(i).getName() + " (" + map.get(i).getPrix() + ")";
		}
		return option;
	}

	/**
	 * Met le curseur sur une unitée que vous pouver déplacer quand toutes vos unité
	 * on été deplacer on met le curseur sur les usines qui penvent encore produir
	 * des unitées
	 *
	 * @param toucheSuivante
	 * @param deplacement
	 */
	private void uniteNonDeplace(AssociationTouches toucheSuivante, Deplacement deplacement) {
		if (toucheSuivante.isCaractere('u') && deplacement.canMoveCurseur()) {
			if (grille.getCasesUniteJoueurNonDeplace(
					Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu), deplacement)
					.isEmpty()) {
				if (grille.getCaseProduirUnite(
						Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)) == null) {
					System.out.println("Vous ne pouvez plus jouer");
				} else {
					deplacement.dontMove(grille.getCaseProduirUnite(
							Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu)));
					deplacement.MoveCurseur();
				}
			} else if (grille.getCasesUniteJoueurNonDeplace(
					Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu), deplacement)
					.size() == 1) {
				deplacement.dontMove(grille.getCasesUniteJoueurNonDeplace(
						Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu), deplacement)
						.get(0));
				deplacement.MoveCurseur();
			} else {
				Random random = new Random();
				deplacement
						.dontMove(grille.getCasesUniteJoueurNonDeplace(
								Joueur.getJoueurActuel(indexJoueurActif, joueurNeutre, joueurRouge, joueurBleu),
								deplacement).get(
										random.nextInt(grille
												.getCasesUniteJoueurNonDeplace(Joueur.getJoueurActuel(indexJoueurActif,
														joueurNeutre, joueurRouge, joueurBleu), deplacement)
												.size() - 1)));
				deplacement.MoveCurseur();
			}
		}
	}
	
	
}
