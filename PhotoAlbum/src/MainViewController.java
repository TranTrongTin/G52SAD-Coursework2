import Classes.Album;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MainViewController {
    private final static int HGAP = 15;
    private final static int VGAP = 15;
    private final static int ALBUMPANE_WIDTH = 210;
    private final static int ALBUM_WIDTH = 200;
    private final static int ALBUM_HEIGHT = 180;
    private final static int LABEL_WIDTH = 200;
    private final static int LABEL_HEIGHT = 55;
    // Private fields for components
    private int numberOfAlbum = 0;
    private Stage myStage;
    @FXML
    private GridPane AlbumPane;
    // Private fields for the dog and cat images
    private String[] listOfFolder;
    private Image[] listOfImage;
    public MainViewController() {
    }

    public static void main(String args[]) {
    }

    public int getnumberOfAlbum() {
        return numberOfAlbum;
    }

    public void setnumberOfAlbum(int numberOfAlbum) {
        this.numberOfAlbum = numberOfAlbum;
    }

    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }

    // Initialize method
    @FXML
    public void testing() {
        //AlbumName.setText("Hihi haha");
        System.out.println(getColCount(AlbumPane));
        //-------------
//        ColumnConstraints column = new ColumnConstraints(100);
//        AlbumPane.getColumnConstraints().add(column);
        //-------------
        for (int i = 0; i < 2 * numberOfAlbum; i++) {
            //final ImageView image = (ImageView) AlbumPane.getChildren().get(i);
            final Node node = AlbumPane.getChildren().get(i);
            if (node.getId().endsWith("IV")) {
                final ImageView image = (ImageView) AlbumPane.getChildren().get(i);
                image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            System.out.println("IV clicked\n" + image.getId());
                        }
                    }
                });
            }
        }
    }

    //Return the number of row of gridPane
    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

    //Return the number of column of gridPane
    private int getColCount(GridPane pane) {
        int numCols = pane.getColumnConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer ColIndex = GridPane.getColumnIndex(child);
                if (ColIndex != null) {
                    numCols = Math.max(numCols, ColIndex + 1);
                }
            }
        }
        return numCols;
    }

    //Check mouseClick at which imageView
    public void checkClicked(final ImageView iv) {
        iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    System.out.println("IV clicked " + iv.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader((getClass().getResource("AlbumView.fxml")));
                        Parent albumView = loader.load();
                        PhotoViewController controller = loader.getController();
                        Scene album_scene = new Scene(albumView);
                        //+++++++++++++++++
                        controller.setAlbum(new Album(iv.getId()));
                        controller.setUpImage();
                        //+++++++++++++++++
                        myStage.hide(); //optional
                        myStage.setScene(album_scene);
                        controller.setStage(myStage);
                        myStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //display album
    public void setUpAlbum() {
        int pos = 0;
        //get folders and the quantity
        String path = "PhotoAlbum/src/";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isDirectory() && !(file.getName().equals("Classes"))) {
                numberOfAlbum++;
            }
        }
        listOfFolder = new String[numberOfAlbum];
        for (File file : listOfFiles) {
            if (file.isDirectory() && !(file.getName().equals("Classes"))) {
                listOfFolder[pos] = file.getName();
                pos++;
            }
        }

        //get representative picture for album
        pos = 0;
        listOfImage = new Image[numberOfAlbum];
        for (String string : listOfFolder) {
            path = "PhotoAlbum/src/".concat(string);
            File representImg = new File(path);
            File[] tmpList = representImg.listFiles();
            listOfImage[pos] = PhotoViewController.convertToImage(tmpList[0]);
            pos++;
        }
        //
        //config the gridpane
        int numCol = numberOfAlbum / 3 + 1;
        for (int i = 0; i < numCol - 1; i++) {
            ColumnConstraints column = new ColumnConstraints(ALBUMPANE_WIDTH);
            column.setHalignment(HPos.CENTER);
            AlbumPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 3; i++) {
            AlbumPane.getRowConstraints().get(i).setValignment(VPos.BOTTOM);
        }
        System.out.println(numberOfAlbum);
        AlbumPane.setPrefWidth(numCol * (ALBUMPANE_WIDTH + HGAP));


        //display representative album's image
        pos = 0;
        for (Image image : listOfImage) {
            ImageView iv = new ImageView();

            iv.setFitHeight(ALBUM_HEIGHT - VGAP);
            iv.setFitWidth(ALBUM_WIDTH);
            iv.setImage(image);
            iv.setCursor(Cursor.HAND);
            iv.setId(listOfFolder[pos]);

            System.out.println(iv.getId());
            checkClicked(iv);
            AlbumPane.add(iv, pos / 3, pos % 3);
            pos++;
        }


        //display label list
        pos = 0;
        for (String string : listOfFolder) {
            //create label name tag for album
            Label nameTag = new Label();
            String tmp = string;
            tmp = tmp.concat(" Album");

            setLabelProperty(nameTag);
            nameTag.setText(tmp);
            AlbumPane.add(nameTag, pos / 3, pos % 3);
            System.out.println(tmp);
            pos++;
        }

        //initialize list array
    }

    //set properites for label name tag
    private void setLabelProperty(Label nameTag) {
        nameTag.alignmentProperty().set(Pos.BOTTOM_CENTER);
        nameTag.setAlignment(Pos.CENTER);
        nameTag.setOpaqueInsets(new Insets(2, 0, 2, 0));
        nameTag.setTextFill(Color.web("bbbbbb"));
        nameTag.setStyle("-fx-background-color: \"#222222\"");
        nameTag.setPrefWidth(LABEL_WIDTH);
        nameTag.setPrefHeight(LABEL_HEIGHT);
    }
}
