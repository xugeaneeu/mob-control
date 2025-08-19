package view;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetManager {
  private final Map<String, Image> textures = new HashMap<>();

  public void loadTexture(String name, String resourcePath) {
    Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
    textures.put(name, img);
  }

  public void preloadTexture() {
    loadTexture("background", "/assets/background.png");
    loadTexture("castle", "/assets/castle.png");
    loadTexture("bullet", "/assets/bullet.png");
  }

  public Image getTexture(String name) {
    Image img = textures.get(name);
    if (img == null) {
      throw new RuntimeException("No texture found for name " + name);
    }
    return img;
  }

  public void dispose() {
    textures.clear();
  }
}
