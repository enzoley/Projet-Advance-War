package arme;

import java.util.*;

public abstract class Arme {
  protected Map<String, Double> tableEfficacite; //Table représentant l'efficacité de chaque arme par rapport à l'ennemi

  public Map<String, Double> getTableEfficacite() {
    return tableEfficacite;
  }

  public void setTableEfficacite(Map<String, Double> tableEfficacite) {
    this.tableEfficacite = tableEfficacite;
  }

  public abstract String getName();
}
