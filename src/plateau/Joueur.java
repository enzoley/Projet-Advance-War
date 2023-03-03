package plateau;

public class Joueur {
	private int id;
	private int nbCredit;

	public Joueur(int id) {
		this.id = id;
		this.nbCredit = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbCredit() {
		return nbCredit;
	}

	public void setNbCredit(int nbCredit) {
		this.nbCredit = nbCredit;
	}

	public boolean estNeutre() {
		return this.id == 0;
	}

	public boolean estRouge() {
		return this.id == 1;
	}

	public boolean estBleu() {
		return this.id == 2;
	}

	public boolean estEnnemi(Joueur ennemi) {
		return (estBleu() && ennemi.estRouge()) || (estRouge() && ennemi.estBleu());
	}

	public static Joueur getJoueurActuel(int id, Joueur joueurNeutre, Joueur joueurRouge, Joueur joueurBleu) {
		if (id == 1) {
			return joueurRouge;
		} else if (id == 2) {
			return joueurBleu;
		} else {
			return joueurNeutre;
		}
	}

	public void retirerCredits(int cout) {
		this.nbCredit = this.nbCredit - cout;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Joueur)) {
			return false;
		}
		Joueur joueur = (Joueur) o;
		return id == joueur.id && nbCredit == joueur.nbCredit;
	}

	/**
	 * Methode objet d'instance permettant d'obtenir le nom du joueur à partir de
	 * son id
	 * 
	 * @return String
	 */
	public String getName() {
		if (estNeutre()) {
			return "neutre";
		} else if (estRouge()) {
			return "rouge";
		} else if (estBleu()) {
			return "bleu";
		} else {
			return "";
		}
	}

	/**
	 * Methode objet d'instance permettant d'obtenir le nom du joueur à partir de
	 * son id
	 * 
	 * @return String
	 */
	public String getNameAdversaire() {
		if (estNeutre()) {
			return "neutre";
		} else if (estRouge()) {
			return "bleu";
		} else if (estBleu()) {
			return "rouge";
		} else {
			return "";
		}
	}
}
