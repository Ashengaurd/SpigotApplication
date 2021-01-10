package me.ashenguard.spigotapplication;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import me.ashenguard.api.spigot.SpigotResource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class SpigotResourceWindow {
    private final SpigotResource resource;

    public Pane getResourceTop() {
        GridPane top = new GridPane();
        top.setHgap(10);

        //  - Resource Logo
        ImageView logoImage = new ImageView(resource.logo);
        logoImage.setPreserveRatio(true);
        logoImage.setFitHeight(100);
        logoImage.relocate(0, 0);
        ImageView avatarImage = new ImageView(resource.author.avatar);
        avatarImage.setPreserveRatio(true);
        avatarImage.setFitHeight(35);
        avatarImage.relocate(65, 65);
        Rectangle logoBorder = new Rectangle(100, 100);
        logoBorder.setFill(Color.TRANSPARENT);
        logoBorder.setStroke(Color.BLACK);
        logoBorder.setStrokeWidth(1);
        logoBorder.relocate(0, 0);
        Rectangle avatarBorder = new Rectangle(35, 35);
        avatarBorder.setFill(Color.TRANSPARENT);
        avatarBorder.setStroke(Color.BLACK);
        avatarBorder.setStrokeWidth(1);
        avatarBorder.relocate(65, 65);

        Pane logoPane = new Pane();
        logoPane.getChildren().addAll(logoImage, avatarImage, logoBorder, avatarBorder);
        top.add(logoPane, 0, 0, 1, 3);

        //  - Name
        Label nameLabel = new Label();
        nameLabel.setText(resource.name);
        nameLabel.setFont(new Font( 24));
        top.add(nameLabel, 1, 0);

        //  - Version
        Label versionLabel = new Label();
        versionLabel.setText("Latest Version: " + resource.version.toString(true));
        versionLabel.setFont(new Font( 16));
        versionLabel.setTextFill(Color.DARKGRAY);
        top.add(versionLabel, 2, 0);

        //  - Author
        Label authorLabel = new Label();
        authorLabel.setText("Created by " + resource.author.name);
        authorLabel.setFont(new Font( 16));
        top.add(authorLabel, 1, 1, 2, 1);

        //  - Tag
        Label tagLabel = new Label();
        tagLabel.setWrapText(true);
        tagLabel.setText(resource.tag);
        tagLabel.setFont(new Font(12));
        top.add(tagLabel, 1, 2, 2, 1);

        return top;
    }

    public List<Button> getResourceButtons(SpigotPanel application, double size) {
        return Arrays.asList(
                SpigotPanel.createLinkButton(application, "Spigot Page", resource.page, size, true),
                SpigotPanel.createLinkButton(application, "Support Page", resource.alternativeSupport, size, true),
                SpigotPanel.createLinkButton(application, "Information Page", resource.additionalInformation, size, true)
        );
    }
    
    public Pane getResourceDates(boolean horizontal, double spacing) {
        GridPane pane = new GridPane();
        pane.setVgap(spacing);
        pane.setHgap(spacing);
        
        //  - Release Date
        Label releaseDateLabel = new Label();
        releaseDateLabel.setText("Released in " + new SimpleDateFormat("EEE dd MMM yyyy").format(resource.releaseDate));
        releaseDateLabel.setFont(new Font( 12));
        pane.add(releaseDateLabel, 0, 0);

        //  - Update Date
        Label updateDateLabel = new Label();
        updateDateLabel.setText("Updated in " + new SimpleDateFormat("EEE dd MMM yyyy").format(resource.updateDate));
        updateDateLabel.setFont(new Font( 12));
        pane.add(updateDateLabel, horizontal ? 1 : 0, horizontal ? 0 : 1);
        
        return pane;
    }
    
    public SpigotResourceWindow(SpigotResource resource) {
        this.resource = resource;
    }

    public BorderPane getLayout(SpigotPanel application, boolean mainScene) {
        VBox center = new VBox(8);
        BorderPane bottom = new BorderPane();

        // Layout setup
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        layout.setCenter(center);
        layout.setBottom(bottom);
        layout.setTop(getResourceTop());

        // Setup CENTER
        HBox buttonBox = new HBox(9);
        buttonBox.getChildren().addAll(getResourceButtons(application, 254));
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        center.getChildren().add(buttonBox);
        center.getChildren().add(getResourceDates(false, 0));
        
        // Setup BOTTOM
        bottom.setLeft(application.getCreditGridPane());
        if (!mainScene) {
            Button backButton = new Button();
            backButton.setText("Back");
            backButton.setOnMouseClicked(event -> application.mainStage.setScene(application.mainScene));
            bottom.setRight(backButton);
        }
        
        return layout;
    }

    public void setupScene(SpigotPanel application) {
        Scene scene = new Scene(getLayout(application, false), 800, 600);
        application.mainStage.setScene(scene);
    }

}
