package terrain;

import plateau.Joueur;
import unite.Unite;

public abstract class Propriete extends Terrain {
  protected Joueur joueur;
  protected int PV;

  public Propriete(String im, Joueur joueur) {
    super(im);
    this.joueur = joueur;
    this.PV = 20;
  }

  /**
   * Retire les PV de l'unite en fonction du nombre de degat
   * PV double avec 1 décimale
   *
   * @param double degat
   */
  private void retirerPV(int degat) {
    this.PV = (this.PV - degat < 0) ? 0 : this.PV - degat;
  }

  public void restaurerPV() {
    this.PV = 20;
  }

  public void capturer(Unite ennemi) {
    retirerPV(ennemi.getPVsup());
    if (getPV() <= 0 && !(this instanceof QG)) {
      setJoueur(ennemi.getJoueur());
      restaurerPV();
    }
  }

  public boolean Detruit() {
    return this.PV <= 0 ? true : false;
  }

  public Joueur getJoueur() {
    return this.joueur;
  }

  public void setJoueur(Joueur joueur) {
    this.joueur = joueur;
    setImage(actualiserImage(joueur));
  }

  public int getPV() {
    return this.PV;
  }

  public void setPV(int PV) {
    this.PV = PV;
  }

  protected abstract String actualiserImage(Joueur joueur);
}
