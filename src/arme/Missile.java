package arme;

import java.util.TreeMap;
import ressources.Noms;

public class Missile extends Arme {

  public Missile() {
    this.tableEfficacite = new TreeMap<>();
    this.tableEfficacite.put(Noms.Infanterie, 0.0);
    this.tableEfficacite.put(Noms.Bazooka, 0.0);
    this.tableEfficacite.put(Noms.Tank, 0.7);
    this.tableEfficacite.put(Noms.DCA, 0.4);
    this.tableEfficacite.put(Noms.Helicoptere, 0.0);
    this.tableEfficacite.put(Noms.Bombardier, 0.0);
    this.tableEfficacite.put(Noms.Convoi, 0.7);
  }

  public String getName() {
    return Noms.Missile;
  }
}
