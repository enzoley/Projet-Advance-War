package arme;

import java.util.TreeMap;
import ressources.Noms;

public class Bombe extends Arme {

  public Bombe() {
    this.tableEfficacite = new TreeMap<>();
    this.tableEfficacite.put(Noms.Infanterie, 1.0);
    this.tableEfficacite.put(Noms.Bazooka, 1.0);
    this.tableEfficacite.put(Noms.Tank, 1.0);
    this.tableEfficacite.put(Noms.DCA, 0.7);
    this.tableEfficacite.put(Noms.Helicoptere, 0.0);
    this.tableEfficacite.put(Noms.Bombardier, 0.0);
    this.tableEfficacite.put(Noms.Convoi, 1.0);
  }

  public String getName() {
    return Noms.Bombe;
  }
}
