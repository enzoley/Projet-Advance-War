package arme;

import java.util.TreeMap;
import ressources.Noms;

public class MitrailleuseLourde extends Arme {

  public MitrailleuseLourde() {
    this.tableEfficacite = new TreeMap<>();
    this.tableEfficacite.put(Noms.Infanterie, 1.0);
    this.tableEfficacite.put(Noms.Bazooka, 0.8);
    this.tableEfficacite.put(Noms.Tank, 0.3);
    this.tableEfficacite.put(Noms.DCA, 0.3);
    this.tableEfficacite.put(Noms.Helicoptere, 1.1);
    this.tableEfficacite.put(Noms.Bombardier, 0.7);
    this.tableEfficacite.put(Noms.Convoi, 0.5);
  }

  public String getName() {
    return Noms.MitrailleuseLourde;
  }
}
