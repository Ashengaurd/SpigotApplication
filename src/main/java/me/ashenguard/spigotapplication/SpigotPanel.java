package me.ashenguard.spigotapplication;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.ashenguard.api.spigot.SpigotResource;
import me.ashenguard.api.utils.WebReader;
import me.ashenguard.api.versions.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpigotPanel extends Application {
    public static Image BLANK;

    private final int spigotID;
    private final Version version;

    protected GridPane getCreditGridPane() {
        if (!credits) return new GridPane();

        GridPane gridPane = new GridPane();
        Label creditDevelopment = new Label();
        creditDevelopment.setText("AGMDevelopment - https://discord.gg/6exsySK");
        creditDevelopment.setTextFill(Color.GRAY);
        creditDevelopment.setFont(new Font(10));
        gridPane.add(creditDevelopment, 0, 0);
        Label creditPerson = new Label();
        creditPerson.setText("Designed by Ashenguard - Ashenguard#9043");
        creditPerson.setTextFill(Color.GRAY);
        creditPerson.setFont(new Font(8));
        gridPane.add(creditPerson, 0, 1);
        return gridPane;
    }

    protected Stage mainStage = null;
    protected Scene mainScene = null;
    private SpigotResource resource = null;

    private boolean connectionLog = false;
    private boolean credits = true;
    private List<Integer> dependencies = new ArrayList<>();
    private String description = "";
    private String support = null;

    public void addDependency(int... spigotID) {
        for (int ID : spigotID) dependencies.add(ID);
    }
    public void setDependencies(List<Integer> dependencies) {
        this.dependencies = dependencies;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    protected List<Integer> getDependencies() {
        return dependencies;
    }
    protected String getSupportLink() {
        return support == null ? resource.alternativeSupport : support;
    }
    protected String getDescription() {
        return description;
    }

    public SpigotPanel(int spigotID, Version version) {
        this.spigotID = spigotID;
        this.version = version;
    }

    public void disableCredits() {
        this.credits = false;
    }
    public void enableConnectionLog() {
        this.connectionLog = false;
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        BLANK = new Image("https://i.ibb.co/2hZM74C/svg-13-512.png");
        resource = new SpigotResource(spigotID);

        //  - Connection log
        if (connectionLog) showConnectionLog();

        mainStage.setTitle(String.format("%s - Version: %s", resource.name , version.toString(true)));
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.getIcons().add(resource.logo);

        SpigotResourceWindow resourceWindow = new SpigotResourceWindow(resource);

        //  - Resource Center Layout
        GridPane centerResource = new GridPane();
        centerResource.setPadding(new Insets(10, 0, 10, 0));
        centerResource.setHgap(10);
        centerResource.setVgap(8);
        centerResource.add(resourceWindow.getResourceDates(false, 0), 0, 0, 2, 1);
        centerResource.add(SpigotPanel.createLinkButton(this, "Spigot Page", resource.page, 245, true), 0, 0);
        centerResource.add(SpigotPanel.createLinkButton(this, "Support Page", getSupportLink(), 245, true), 1, 0);
        centerResource.add(SpigotPanel.createLinkButton(this, "Information Page", resource.additionalInformation, 245, true), 0, 1);
        centerResource.add(SpigotPanel.createLinkButton(this, "Download Update", resource.downloadLink, 245, resource.version.isHigher(version)), 1, 1);
        centerResource.add(getDescriptions(), 0, 2, 2, 1);

        //  - Resource Layout
        BorderPane resourceLayout = new BorderPane();
        resourceLayout.setPadding(new Insets(0, 10, 0, 0));
        resourceLayout.setBottom(getCreditGridPane());
        resourceLayout.setTop(resourceWindow.getResourceTop());
        resourceLayout.setCenter(centerResource);

        //  - Main Layout
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setLeft(resourceLayout);
        layout.setRight(getDependencyPane());

        mainScene = new Scene(layout, 800, 600);
        mainScene.setOnMouseClicked(event -> System.out.println(String.format("Mouse pos %.2f, %.2f", event.getX(), event.getY())));
        stage.setScene(mainScene);
        stage.show();
    }

    private void showConnectionLog() {
        ListView<String> listView = new ListView<>(FXCollections.observableArrayList(WebReader.getLogs()));
        listView.setStyle("-fx-alignment: CENTER-LEFT;");
        listView.setPlaceholder(new Label("No Connection recorded"));
        listView.getSelectionModel().setSelectionMode(null);
        listView.setMinSize(380, 580);

        VBox logBox = new VBox(0, listView);
        logBox.setPadding(new Insets(10));
        Stage logStage = new Stage();
        Scene scene = new Scene(logBox, 400, 600, Color.WHITE);
        logStage.setScene(scene);
        logStage.setTitle("Connection Log");
        logStage.setResizable(false);
        logStage.setX(0);
        logStage.setY(0);
        logStage.show();

        new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                listView.setItems(FXCollections.observableArrayList(WebReader.getLogs()));
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            }
        }.start();
    }

    private TextArea getDescriptions() {
        TextArea description = new TextArea();
        description.setText(getDescription());
        description.setWrapText(true);
        description.setEditable(false);
        description.setMaxSize(500, 360);
        description.setMinSize(500, 360);
        return description;
    }

    private Pane getDependencyPane() {
        TableView<SpigotResource> tableView = new TableView<>();
        Button button = new Button();

        ObservableList<SpigotResource> dependencyObservableList = FXCollections.observableArrayList(getDependencies().stream().map(SpigotResource::new).collect(Collectors.toList()));
        TableColumn<SpigotResource, String> column = new TableColumn<>("Requirements");
        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name));
        column.setCellFactory(this::pluginCallback);
        column.setMinWidth(280);
        column.setMaxWidth(280);
        column.setStyle( "-fx-alignment: CENTER-LEFT;");

        tableView.setPlaceholder(new Label("No Requirement Found"));
        tableView.setItems(dependencyObservableList);
        tableView.getColumns().add(column);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setOnMouseClicked(event -> {
            SpigotResource selection = tableView.getSelectionModel().getSelectedItem();
            button.setDisable(selection == null);

            if (event.getClickCount() > 1) {
                new SpigotResourceWindow(selection).setupScene(this);
            }
        });
        tableView.setFixedCellSize(40);
        tableView.setMinSize(270, 550);
        tableView.setMaxSize(270, 550);
        tableView.getSelectionModel().clearSelection();

        button.setDisable(true);
        button.setText("Open Requirement Page");
        button.setOnMouseClicked(event -> {
            SpigotResource selected = tableView.getSelectionModel().getSelectedItem();
            tableView.getSelectionModel().clearSelection();
            button.setDisable(true);

            if (selected == null) return;
            new SpigotResourceWindow(selected).setupScene(this);
        });
        button.setMinWidth(270);
        button.setMaxWidth(270);
        button.setAlignment(Pos.BOTTOM_CENTER);

        GridPane tablePane = new GridPane();
        tablePane.setVgap(10);
        tablePane.add(tableView, 0, 0);
        tablePane.add(button, 0, 1);
        return tablePane;
    }

    private TableCell<SpigotResource, String> pluginCallback(TableColumn<SpigotResource, String> param) {
        return new TableCell<SpigotResource, String>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (name == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    SpigotResource item = (SpigotResource) this.getTableRow().getItem();
                    ImageView graphic = new ImageView();
                    graphic.setImage(item.logo);
                    graphic.setPreserveRatio(true);
                    graphic.setFitHeight(30);

                    setGraphic(graphic);
                    setText(String.format("%s by %s", item.name, item.author.name));
                }
                this.setItem(name);
            }
        };
    }

    public static Button createLinkButton(Application application, String name, String url, double size, boolean enable) {
        Button button = new Button(name);
        button.setOnMouseClicked(event -> application.getHostServices().showDocument(url));
        button.setTooltip(new Tooltip(String.format("Open: %s", url)));
        button.setDisable(url == null || !enable);
        if (size > 0) button.setPrefWidth(size);
        return button;
    }
}
