package plateau;

import java.util.Objects;
import terrain.Terrain;
import unite.Unite;

public class Case {
	private Terrain terrain;
	private Coord coord;
	private Unite unite;

	public Case(Terrain terrain, Coord coord) {
		this.terrain = terrain;
		this.coord = coord;
		this.unite = null;
	}

	public Unite getUnite() {
		if (this.unite != null) {
			return this.unite.isAlive() ? this.unite : null;
		}
		return null;
	}

	public void setUnite(Unite unite) {
		this.unite = unite;
	}

	public Case(Coord coord) {
		this.coord = coord;
	}

	public Terrain getTerrain() {
		return this.terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Coord getCoord() {
		return this.coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	/**
	 * Vérifie que les coordonnées de la case (this) soient identique à ceux de la
	 * case en param
	 *
	 * @param Case case1
	 * @return boolean
	 */
	public boolean coordEquals(Case case1) {
		return this.coord.equals(case1.getCoord());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Case)) {
			return false;
		}
		Case case1 = (Case) o;
		return (Objects.equals(terrain, case1.terrain) && Objects.equals(coord, case1.coord)
				&& Objects.equals(unite, case1.unite));
	}
}
