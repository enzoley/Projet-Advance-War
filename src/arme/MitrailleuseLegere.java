package arme;

import java.util.TreeMap;
import ressources.Noms;

public class MitrailleuseLegere extends Arme {

  public MitrailleuseLegere() {
    this.tableEfficacite = new TreeMap<>();
    this.tableEfficacite.put(Noms.Infanterie, 0.6);
    this.tableEfficacite.put(Noms.Bazooka, 0.55);
    this.tableEfficacite.put(Noms.Tank, 0.15);
    this.tableEfficacite.put(Noms.DCA, 0.1);
    this.tableEfficacite.put(Noms.Helicoptere, 0.3);
    this.tableEfficacite.put(Noms.Bombardier, 0.0);
    this.tableEfficacite.put(Noms.Convoi, 0.4);
  }

  public String getName() {
    return Noms.MitrailleuseLegere;
  }
}
