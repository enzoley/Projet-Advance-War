package terrain;

public abstract class Terrain {
  protected String image;

  public Terrain(String image) {
    this.image = image;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
