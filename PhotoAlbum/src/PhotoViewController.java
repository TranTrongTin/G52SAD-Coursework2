import Classes.Album;
import Classes.EnhanceImage;
import Classes.JPEGImage;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotoViewController {
    @FXML
    private Slider zoom_slider;

    @FXML
    private ImageView mainPhoto;

    @FXML
    private MenuBar menuBarr;

    @FXML
    private Label AlbumName;

    @FXML
    private GridPane PhotoPane;

    @FXML
    private Button addPhoto;

    @FXML
    private Button deletePhoto;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private ToggleButton toBW;

    @FXML
    private ToggleButton toNeg;

    @FXML
    private ToggleButton toBlu;

    @FXML
    private ToggleButton toContr;

    @FXML
    private TilePane tilePane;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Other private fields
    private Stage stage;
    private Album album;
    private Image[] listOfImage;
    private Image originalImage;
    private int currentPos = 0;
    private int numberOfPhoto;
    private final static int HGAP = 15;
    private final static int PHOTOPANE_WITH = 140 + HGAP;
    private final static int SLIDER_IMAGE_WIDTH = 120;
    private final static int SLIDER_IMAGE_HEIGHT = 100;
    private File[] listOfFiles;

    //constructor
    public PhotoViewController() {
    }

    //handle back and forward buttons
    @FXML
    public void previousClick() {
        currentPos--;
        transition_Fade(mainPhoto, mainPhoto.getImage(), listOfImage[currentPos]);
        //mainPhoto.setImage(listOfImage[currentPos]);
        disableButton();
    }

    @FXML
    public void nextClick() {
        currentPos++;
        transition_Fade(mainPhoto, mainPhoto.getImage(), listOfImage[currentPos]);
        //mainPhoto.setImage(listOfImage[currentPos]);
        disableButton();
    }

    @FXML
    void zoomIn() {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut() {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }

    @FXML
    void rotateRight() {
        mainPhoto.setRotate(mainPhoto.getRotate() + 90);
    }

    @FXML
    void rotateLeft() {
        mainPhoto.setRotate(mainPhoto.getRotate() - 90);
    }

    @FXML
    void toBlackWhite() {
        Image image = mainPhoto.getImage();
        JPEGImage jpegImage = JPEGImage.FxToJPEG(image, (int) (image.getWidth()), (int) (image.getHeight()));
        jpegImage = EnhanceImage.toBlackWhite(jpegImage);
        Image imageBW = SwingFXUtils.toFXImage(jpegImage.img, null);
        if (toBW.isSelected()) {
            originalImage = image;
            mainPhoto.setImage(imageBW);
        } else {
            mainPhoto.setImage(originalImage);
        }
    }

    @FXML
    void toContrast() {
        Image image = mainPhoto.getImage();
        JPEGImage jpegImage = JPEGImage.FxToJPEG(image, (int) (image.getWidth()), (int) (image.getHeight()));
        jpegImage = EnhanceImage.toContrast(jpegImage);
        Image imageBW = SwingFXUtils.toFXImage(jpegImage.img, null);
        if (toContr.isSelected()) {
            originalImage = image;
            mainPhoto.setImage(imageBW);
        } else {
            mainPhoto.setImage(originalImage);
        }
    }

    @FXML
    void toBlur() {
        Image image = mainPhoto.getImage();
        JPEGImage jpegImage = JPEGImage.FxToJPEG(image, (int) (image.getWidth()), (int) (image.getHeight()));
        jpegImage = EnhanceImage.toBlur(jpegImage);
        Image imageBW = SwingFXUtils.toFXImage(jpegImage.img, null);
        if (toBlu.isSelected()) {
            originalImage = image;
            mainPhoto.setImage(imageBW);
        } else {
            mainPhoto.setImage(originalImage);
        }
    }

    @FXML
    void toNegative() {
        Image image = mainPhoto.getImage();
        JPEGImage jpegImage = JPEGImage.FxToJPEG(image, (int) (image.getWidth()), (int) (image.getHeight()));
        jpegImage = EnhanceImage.toNegative(jpegImage);
        Image imageBW = SwingFXUtils.toFXImage(jpegImage.img, null);
        if (toNeg.isSelected()) {
            originalImage = image;
            mainPhoto.setImage(imageBW);
        } else {
            mainPhoto.setImage(originalImage);
        }
    }
    @FXML
    public void addPhotos() {
        //get the photos from browser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );
        List<File> list =
                fileChooser.showOpenMultipleDialog(this.stage);
        if (list != null) {
            for (File file : list) {
                openFile(file);
            }
        }
        //check for overlapping images
        boolean overlapped = false;
        if (list != null)
            for (int i = 0; i < listOfFiles.length; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (listOfFiles[i].getName().equals(list.get(j).getName())) {
                        overlapped = true;
                        Parent Popup = null;
                        try {
                            Stage parent = new Stage();
                            //parent = (Stage) mainPhoto.getScene().getWindow();
                            Popup = FXMLLoader.load(
                                    getClass().getResource("Alert.fxml"));
                            parent.setScene(new Scene(Popup));
                            parent.initModality(Modality.APPLICATION_MODAL);
                            parent.initOwner(stage);
                            parent.showAndWait();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        //store images in the proper album
        if (!overlapped) {
            for (int i = 0; i < list.size(); i++) {
                Image image = convertToImage(list.get(i));
                String path = "PhotoAlbum/src/".concat(album.getName()).concat("/").concat(list.get(i).getName());
                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
                BufferedImage imageRGB = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.OPAQUE);
                Graphics2D graphics = imageRGB.createGraphics();
                graphics.drawImage(bImage, 0, 0, null);
                try {
                    String name = list.get(i).getName();
                    String type = name.substring(name.length() - 3, name.length());
                    System.out.println(type);
                    ImageIO.write(imageRGB, type, new File(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                graphics.dispose();
                //re-modify slider Photo pane
                ColumnConstraints column = new ColumnConstraints(PHOTOPANE_WITH - HGAP);
                PhotoPane.getColumnConstraints().add(column);
            }
            //reload the interface
            int tmp = numberOfPhoto;
            numberOfPhoto += list.size();
            PhotoPane.setPrefWidth(numberOfPhoto * (PHOTOPANE_WITH));
            for (int pos = tmp; pos < numberOfPhoto; pos++) {
                Image image = convertToImage(list.get(pos - tmp));
                listOfImage[pos] = image;
                addImageView(image, pos);
            }
            System.out.println("added");
        }

    }

    @FXML
    public void closeAlert(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backToMainView() {

        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("ImageView.fxml")));
            Parent albumView = loader.load();
            MainViewController controller = loader.getController();
            controller.setMyStage(stage);
            controller.setUpAlbum();
            Scene album_scene = new Scene(albumView);

            stage.hide(); //optional
            stage.setScene(album_scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(File file) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
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

    //Get all image in the chosen album and display
    public void setUpImage() {
        //display album name
        String name = "";
        name = name.concat(album.getName());
        name = name.concat(" Album");
        AlbumName.setText(name);
        //get files
        String path = "PhotoAlbum/src/";
        path = path.concat(this.album.getName());
        File folder = new File(path);
        listOfFiles = folder.listFiles();
        numberOfPhoto = listOfFiles.length - 1; //exclude thumbs.db

        //initialize list array
        listOfImage = new Image[200];

        //display main photo
        mainPhoto.setCursor(Cursor.HAND);
        mainPhoto.setImage(convertToImage(listOfFiles[0]));

        //config the gridpane
        album.setNumberOfPhoto(numberOfPhoto);
        System.out.println(numberOfPhoto);
        for (int i = 0; i < numberOfPhoto - 1; i++) {
            ColumnConstraints column = new ColumnConstraints(PHOTOPANE_WITH - HGAP);
            PhotoPane.getColumnConstraints().add(column);
        }
        PhotoPane.setPrefWidth(numberOfPhoto * (PHOTOPANE_WITH));

        //display slider
        int pos = 0;
        for (File file : listOfFiles) {
            if (isImage(file)) {
                final Image image = convertToImage(file);
                listOfImage[pos] = image;
                addImageView(image, pos);
                pos++;
            }
        }
        System.out.println(currentPos);

        initFeatures();
    }

    //init values of editing features
    private void initFeatures() {
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
    }

    //Zoom slider function
    private void zoom(double scaleValue) {
//    System.out.println("airportapp.Controller.zoom, scaleValue: " + scaleValue);
        mainPhoto.setScaleX(scaleValue);
        mainPhoto.setScaleY(scaleValue);
    }

    //convert jvava.io.File to javafx.scene.image
    public static Image convertToImage(File file) {
        BufferedImage buffImage = null;
        try {
            buffImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(buffImage, null);
        return image;
    }

    //check if that file is an image
    private boolean isImage(File file) {
        return (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith("bmp"));
    }

    //re-display the slider
    public void addImageView(final Image image, int pos) {
        ImageView iv = new ImageView();
        iv.setFitHeight(SLIDER_IMAGE_HEIGHT);
        iv.setFitWidth(SLIDER_IMAGE_WIDTH);
        iv.setImage(image);
        iv.setCursor(Cursor.HAND);

        final int finalPos = pos;
        iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    transition_Fade(mainPhoto, mainPhoto.getImage(), image);
                    //mainPhoto.setImage(image);
                    currentPos = finalPos;
                    System.out.print(currentPos + " ");
                    disableButton();
                }
            }
        });
        PhotoPane.getColumnConstraints().get(pos).setHalignment(HPos.CENTER);
        PhotoPane.add(iv, pos, 0);
    }

    //Transition animation method
    public static void transition_Fade(ImageView imageView, Image image1, Image image2) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), imageView);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);
        ft.play();
        imageView.setImage(image2);
    }

    //Disable previous/next button (first/last image chosen)
    private void disableButton() {
        if (currentPos == 0) {
            prevButton.setDisable(true);
            nextButton.setDisable(false);
        } else if (currentPos == numberOfPhoto - 1) {
            nextButton.setDisable(true);
            prevButton.setDisable(false);
        } else {
            nextButton.setDisable(false);
            prevButton.setDisable(false);
        }
    }
}
