package me.ashenguard.api.spigot;

import javafx.scene.image.Image;
import me.ashenguard.api.utils.WebReader;
import me.ashenguard.spigotapplication.SpigotPanel;
import org.json.JSONObject;

public class SpigotAuthor {
    public final int ID;
    public final Image avatar;
    public final String name;

    public SpigotAuthor(int ID) {
        this.ID = ID;

        JSONObject jsonObject = new WebReader(String.format("https://api.spiget.org/v2/authors/%d/", ID)).readJSON();
        if (jsonObject == null || ID < 0) {
            this.name = "404 NotFound";
            this.avatar = SpigotPanel.BLANK;
        } else {
            this.name = jsonObject.optString("name");
            this.avatar = new Image(String.format("https://www.spigotmc.org/%s", jsonObject.getJSONObject("icon").getString("url")));
        }
    }
}
