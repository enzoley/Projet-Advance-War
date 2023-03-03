package action;

import deplacement.Deplacement;
import java.util.ArrayList;
import plateau.Case;
import plateau.Coord;
import plateau.Grille;
import ressources.Affichage;
import terrain.*;
import unite.Unite;

public class Action {
	private static final String attendre = "Attendre";
	private static final String attaquer = "Attaquer";
	private static final String capturer = "Capturer";

	private ArrayList<Case> casesAttaquables;
	private int indice;
	private boolean over;

	public Action() {
		this.casesAttaquables = new ArrayList<Case>(4);
		this.indice = 0;
		this.over = false;
	}

	public ArrayList<Case> getCasesAttaquables() {
		return this.casesAttaquables;
	}

	public void setCasesAttaquables(ArrayList<Case> casesAttaquables) {
		this.casesAttaquables = casesAttaquables;
	}

	public int getIndice() {
		return this.indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public boolean isOver() {
		return this.over;
	}

	public boolean getOver() {
		return this.over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	/**
	 * Indice + 1
	 */
	public void upIndice() {
		if (this.indice == this.casesAttaquables.size() - 1) {
			this.indice = 0;
		} else {
			this.indice += 1;
		}
	}

	/**
	 * Indice - 1
	 */
	public void downIndice() {
		if (this.indice == 0) {
			this.indice = this.casesAttaquables.size() - 1;
		} else {
			this.indice -= 1;
		}
	}

	/**
	 * Renvoi une case parmit toutes celles qu'il est possible d'attaquer
	 *
	 * @return Case
	 */
	public Case getOneCaseAttaquable() {
		return this.casesAttaquables.get(this.indice);
	}

	/**
	 * Réinitialisation des attributs
	 */
	private void reinitialiser() {
		this.casesAttaquables.clear();
		this.indice = 0;
	}

	public void setCaseAttaquable(Grille grille, Case caseAttaquant, Case caseArrivee) {
		for (Case case1 : grille.getCasesAdjacentes(caseArrivee)) {
			if (case1.getUnite() != null && caseAttaquant.getUnite() != null
					&& case1.getUnite().getJoueur().estEnnemi(caseAttaquant.getUnite().getJoueur())
					&& caseAttaquant.getUnite().peutAttaquer(case1.getUnite())) {
				this.casesAttaquables.add(case1);
			}
		}
	}

	/**
	 * True seulement si il est possible de capturer
	 *
	 * @param Case caseAttaquant
	 * @param Case caseArrivee
	 * @return boolean
	 */
	private boolean peutCapturer(Case caseAttaquant, Case caseArrivee) {
		if (caseAttaquant.getUnite() != null && caseArrivee.getTerrain() instanceof Propriete
				&& (((Propriete) caseArrivee.getTerrain()).getJoueur().estEnnemi(caseAttaquant.getUnite().getJoueur())
						|| ((Propriete) caseArrivee.getTerrain()).getJoueur().estNeutre())) {
			return true;
		}
		return false;
	}

	/**
	 * Tableau contenant le nom des action réalisable, on peut toujours attendre par
	 * contre l'attaque et la capture dépendent de d'autres paramêtres
	 *
	 * @param Grille grille
	 * @param Case   caseAttaquant
	 * @param Case   caseArrivee
	 * @return
	 */
	private String[] getOptions(Grille grille, Case caseAttaquant, Case caseArrivee) {
		setCaseAttaquable(grille, caseAttaquant, caseArrivee);
		finCapture(caseAttaquant, caseArrivee);
		ArrayList<String> o = new ArrayList<String>();
		o.add(attendre);
		if (!this.casesAttaquables.isEmpty()) {
			o.add(attaquer);
		}
		if (peutCapturer(caseAttaquant, caseArrivee)) {
			o.add(capturer);
		}
		String[] option = new String[o.size()];
		for (int i = 0; i < option.length; i++) {
			option[i] = o.get(i);
		}
		return option;
	}

	/**
	 * Action a effectuer si on choisi d'attendre (Popup)
	 *
	 * @param Deplacement deplacement
	 */
	private void attendre(Deplacement deplacement) {
		deplacement.getUnite().getChemin().getFirst().getUnite().setImage(false);
		deplacement.getUnite().deplacementConfirmer();
		deplacement.MoveCurseur();
	}

	/**
	 * Action a effectuer si on choisi de quitter = echap (Popup)
	 *
	 * @param Deplacement deplacement
	 */
	private void quitter(Deplacement deplacement) {
		deplacement.deselectionerUnite();
	}

	/**
	 * Lorsque l'on attaque une unité, elle riposte
	 *
	 * @param caseAttaquant
	 * @param attaquant
	 * @param caseEnnemi
	 * @param ennemi
	 */
	private void attaqueContreAttaque(Case caseAttaquant, Unite attaquant, Case caseEnnemi, Unite ennemi) {
		System.out.println(attaquant.getName() + " (" + attaquant.getPV() + ") attaque " + ennemi.getName() + " ("
				+ ennemi.getPV() + ") avec " + attaquant.getArmePerformante(caseEnnemi.getUnite()).getName());
		attaquant.attaquer(caseEnnemi, ennemi);
		ennemi.attaquer(caseAttaquant, attaquant);
		System.out.println("resultat : " + attaquant.getName() + " (" + attaquant.getPV() + ") et " + ennemi.getName()
				+ " (" + ennemi.getPV() + ")");
	}

	/**
	 * Action a effectuer si on choisi d'attaquer (Popup)
	 *
	 * @param Deplacement deplacement
	 */
	private void attaquer(Deplacement deplacement) {
		attendre(deplacement);
		if (this.casesAttaquables.size() == 1) {
			attaqueContreAttaque(deplacement.getUnite().getCaseUniteDeplace().getLast(),
					deplacement.getUnite().getCaseUniteDeplace().getLast().getUnite(), getOneCaseAttaquable(),
					getOneCaseAttaquable().getUnite());
			reinitialiser();
		}
	}

	/**
	 * Gestion du cas où il y a plusieurs cases attaquables, après le choix de la
	 * case il faut attaquer, réinitialiser, remettre le curseur sur notre unité et
	 * pouvoir de nouveau bouger le curseur avec les flêches directionnles
	 *
	 * @param Deplacement deplacement
	 */
	public void finirAttaque(Deplacement deplacement) {
		downIndice();
		attaqueContreAttaque(deplacement.getUnite().getCaseUniteDeplace().getLast(),
				deplacement.getUnite().getCaseUniteDeplace().getLast().getUnite(),
				this.casesAttaquables.get(this.indice), this.casesAttaquables.get(this.indice).getUnite());
		reinitialiser();
		deplacement.setCoordCurseur(new Coord(deplacement.getUnite().getCaseUniteDeplace().getLast().getCoord().getX(),
				deplacement.getUnite().getCaseUniteDeplace().getLast().getCoord().getY()));
		deplacement.MoveCurseur();
	}

	/**
	 * Restaure les PV d'une propriete si une unité arrête la capture
	 *
	 * @param Case caseDepart
	 * @param Case caseArrivee
	 */
	private void finCapture(Case caseDepart, Case caseArrivee) {
		if (caseDepart.getTerrain() instanceof Propriete && !caseDepart.coordEquals(caseArrivee)
				&& ((Propriete) caseDepart.getTerrain()).getJoueur().getId() != caseDepart.getUnite().getJoueur()
						.getId()) {
			((Propriete) caseDepart.getTerrain()).restaurerPV();
			System.out.println(caseDepart.getUnite().getName() + "a mis fin à la capture");
		}
	}

	/**
	 * Une unitée capture une propriete
	 *
	 * @param Deplacement deplacement
	 */
	private void capturer(Deplacement deplacement) {
		attendre(deplacement);
		int pointsVie = ((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain()).getPV();
		((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain())
				.capturer(deplacement.getUnite().getCaseUniteDeplace().getLast().getUnite());
		if (((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain()).Detruit()) {
			setOver(true);
			System.out.print("Le jeu est fini, le joueur "
					+ ((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain()).getJoueur()
							.getNameAdversaire()
					+ " a gagne");
		} else if (pointsVie > ((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain())
				.getPV()) {
			System.out.println(deplacement.getUnite().getCaseUniteDeplace().getLast().getUnite().getName()
					+ " capture une propriete ("
					+ ((Propriete) deplacement.getUnite().getCaseUniteDeplace().getLast().getTerrain()).getPV() + ") ");
		} else {
			System.out.println(deplacement.getUnite().getCaseUniteDeplace().getLast().getUnite().getName()
					+ " a capturer une propriete");
		}
	}

	/**
	 * Popup avec les actions possiblent (Attendre, Attaquer, Capturer) +
	 * realisation de l'action selectionnée + echap => annule le deplacement
	 *
	 * @param grille
	 * @param deplacement
	 */
	public void confirmerDeplacement(Grille grille, Deplacement deplacement) {
		String message = "Vous avez confirmer le deplacement, à présent vous voulez";
		String[] options = getOptions(grille, deplacement.getUnite().getChemin().getFirst(),
				deplacement.getUnite().getChemin().getLast());
		switch (Affichage.popup(message, options, true, 1)) {
		case 0:
			attendre(deplacement);
			reinitialiser();
			break;
		case -1:
			quitter(deplacement);
			reinitialiser();
			break;
		case 2:
			capturer(deplacement);
			reinitialiser();
			break;
		case 1:
			switch (options[1]) {
			case attaquer:
				attaquer(deplacement);
				break;
			case capturer:
				capturer(deplacement);
				reinitialiser();
				break;
			}
			break;
		}
	}
}
