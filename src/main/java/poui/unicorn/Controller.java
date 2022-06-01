package poui.unicorn;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

/*
import java.io.IOException;
import com.sun.glass.ui.CommonDialogs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    @FXML
    void nextButtonClick(ActionEvent event) {

        nextButton.getScene().getWindow().hide();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("viewtwo.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        stage.setTitle("Unicorn");
        stage.getIcons().add(
                new Image(Objects.requireNonNull(Application.class.getResourceAsStream("unicorn.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
*/


public class Controller implements Initializable {

    @FXML
    private Button chooserButton;

    @FXML
    private Button saverButton;

    @FXML
    private Button blackAndWhite;

    @FXML
    private Button contours;

    @FXML
    private Button negative;

    @FXML
    private Button emboss;

    @FXML
    private Button blur;

    @FXML
    private ImageView beforeImagеForm;

    @FXML
    private ImageView afterImagеForm;

    private File file;

    private BufferedImage result;

    @FXML
    void fileChooser() {
        FileChooser choose = new FileChooser();
        choose.setInitialDirectory(new File("D:\\"));
        choose.getExtensionFilters().addAll(new ExtensionFilter("JPEG", "*.jpg", "*.jpeg","*.jpe"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("BMP", "*.bmp","*.rle","*.dib"),
                new ExtensionFilter("GIF", "*.gif"),
                new ExtensionFilter("JPEG 2000", "*.jpf", "*.jpx","*.jp2","*.j2c","*.j2k","*.jpc")
        );
        choose.setTitle("Выбрать картинку");

        try {
            file = choose.showOpenDialog(null);
            System.out.println("Файл был выбран");

            if (file != null){

                String localUrl = file.toURI().toString();
                Image image = new Image(localUrl);
                beforeImagеForm.setImage(image);
            }
        }catch (Exception ex){
            System.out.println("Файл не был выбран");
        }
    }

    @FXML
    void fileSaver() {
        FileChooser save = new FileChooser();
        save.setInitialDirectory(new File("D:\\"));
        save.getExtensionFilters().addAll(new ExtensionFilter("JPEG", "*.jpg", "*.jpeg","*.jpe"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("BMP", "*.bmp","*.rle","*.dib"),
                new ExtensionFilter("GIF", "*.gif"),
                new ExtensionFilter("JPEG 2000", "*.jpf", "*.jpx","*.jp2","*.j2c","*.j2k","*.jpc")
        );
        save.setTitle("Сохранить");
        save.setInitialFileName("New Picture");

        try {
            file = save.showSaveDialog(null);
            ImageIO.write(result, "png", file);
            System.out.println("Файл был сохранён");
        }catch (Exception ex){
            System.out.println("Файл не сохранился");
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        chooserButton.setOnAction(event -> {
            fileChooser();// Выбор изображения для обработки
        });

        // Методы обработки изображений
        blackAndWhite.setOnAction(event -> {
            transformBandW(file);
        });

        blur.setOnAction(event -> {
            transform(file, 9,1,1,1,1,1,1,1,1,1);
        });

        negative.setOnAction(event -> {
            transformNeg(file, 1,255,0,0,0,0,-1,0,0,0,0);
        });

        contours.setOnAction(event -> {
            transform(file, 1,-1,0,1,-2,0,2,-1,0,1);
        });

        emboss.setOnAction(event -> {
            transform(file, 1,-2,-1,0,-1,1,1,0,1,2);
        });

        saverButton.setOnAction(event -> {
            fileSaver();//Сохранялка изображений
        });
    }

    // метод обработки изображений в негативе
    void transformNeg(File file, int div, int offset, int one, int two, int three, int four, int five,int six, int seven, int eight, int nine){
        try {

            BufferedImage source = ImageIO.read(file); //Добыча исходного изображения

            int pictureWidth = source.getWidth(); // Данный обрабатываемого изображения
            int pictureHeight = source.getHeight();
            int pictureType = source.getType();

            BufferedImage mid = new BufferedImage(pictureWidth + 2, pictureHeight + 2, pictureType); // Создание болванки для изображения с расширенными краями (для корректной обработки)
            result = new BufferedImage(pictureWidth, pictureHeight, pictureType); //Создание болванки для готового изображения

            Color[] top = new Color[pictureWidth];// Добавление краевых пикселей по верхнему краю изображения
            for (int x = 0; x < pictureWidth; x++) {
                top [x] = new Color(source.getRGB(x, 0));
                mid.setRGB(x + 1, 0, top[x].getRGB());
            }

            Color[] left = new Color[pictureHeight];// Добавление краевых пикселей по левому краю изображения
            for (int y = 0; y < pictureHeight; y++) {
                left [y] = new Color(source.getRGB(0, y));
                mid.setRGB(0, y + 1, left[y].getRGB());
            }

            Color[] bottom = new Color[pictureWidth];// Добавление краевых пикселей по нижнему краю изображения
            for (int x = 0; x < pictureWidth; x++) {
                bottom [x] = new Color(source.getRGB(x, pictureHeight - 1));
                mid.setRGB(x + 1, pictureHeight + 1, bottom[x].getRGB());
            }

            Color[] right = new Color[pictureHeight];// Добавление краевых пикселей по правому краю изображения
            for (int y = 0; y < pictureHeight; y++) {
                right [y] = new Color(source.getRGB(pictureWidth - 1, y));
                mid.setRGB(pictureWidth + 1, y + 1, right[y].getRGB());
            }

            for (int x = 0; x < pictureWidth; x++) { // Определение положения исходного изображения внутри расширенной болванки
                for (int y = 0; y < pictureHeight; y++) {
                    Color colorMid = new Color(source.getRGB(x,y));
                    mid.setRGB(x + 1, y + 1, colorMid.getRGB());
                }
            }

            mid.setRGB(0,0, top[0].getRGB()); // Добавление угловых пикселей на расширенную болванку
            mid.setRGB(0,pictureHeight + 1, left[pictureHeight-1].getRGB());
            mid.setRGB(pictureWidth + 1,pictureHeight + 1, bottom[pictureWidth-1].getRGB());
            mid.setRGB(pictureWidth + 1,0, right[0].getRGB());

            int myDiv = div; // дополнительные переменные для корректной обработки изображения
            int myOffset = offset;

            int[]  mask = { // Маска обработки изображения
                    one,two,three,
                    four,five,six,
                    seven,eight,nine
            };

            for (int x = 0; x < pictureWidth; x++) {    // Каждый поток обрабатывает каждый четверный пиксель ряда, в сумме, обрабатывает четверть изображения
                for (int y = 0; y < pictureHeight; y++) {

                    Color[] color = { // выборка девяти нужных пикселей для их дальнейшей обработки
                            new Color(mid.getRGB(x, y)),
                            new Color(mid.getRGB(x + 1, y)),
                            new Color(mid.getRGB(x + 2, y)),
                            new Color(mid.getRGB(x, y + 1)),
                            new Color(mid.getRGB(x + 1, y + 1)),
                            new Color(mid.getRGB(x + 2, y + 1)),
                            new Color(mid.getRGB(x, y + 2)),
                            new Color(mid.getRGB(x + 1, y + 2)),
                            new Color(mid.getRGB(x + 2, y + 2))
                    };

                    int[] blue = new int[9]; // Разделённые на спектры пиксели имеют свой мини массив
                    int[] red = new int[9];
                    int[] green = new int[9];

                    int[] newBlue = new int[9]; // Эти три массива для обработанных разделённых пикселей
                    int[] newRed = new int[9];
                    int[] newGreen = new int[9];

                    int valueBlue = 0; // Места для готовых пикселей
                    int valueRed = 0;
                    int valueGreen = 0;

                    for (int i = 0; i < 9; i++){ // Обработка пикселей

                        blue[i] = color[i].getBlue(); // Заполнение массивов
                        red[i] = color[i].getRed();
                        green[i] = color[i].getGreen();

                        newBlue[i] = blue[i] * mask[i]; // умножение выбранных пикселей на обрабатывающую маску
                        newRed[i] = red[i] * mask[i];
                        newGreen[i] = green[i] * mask[i];

                        valueBlue += newBlue[i]; // Суммирование полученных элементов спектров для дальнейшей обработки
                        valueRed += newRed[i];
                        valueGreen += newGreen[i];

                    }

                    valueBlue = valueBlue/myDiv; // Кореектировка цвета с использованием дополнительной переменной
                    valueRed = valueRed/myDiv;   // (После суммирования значения пикселей превышают 255)
                    valueGreen = valueGreen/myDiv;

                    if (valueBlue <= 0){                 // Дополнительная ситуативная обработка вышедших за пределы опредения цвета значений
                        valueBlue = valueBlue + myOffset;
                    }
                    if (valueRed <= 0){
                        valueRed = valueRed + myOffset;
                    }
                    if (valueGreen <= 0){
                        valueGreen = valueGreen + myOffset;
                    }

                    if (valueBlue > 255){
                        valueBlue = valueBlue - myOffset;
                    }
                    if (valueRed > 255){
                        valueRed = valueRed - myOffset;
                    }
                    if (valueGreen > 255){
                        valueGreen = valueGreen - myOffset;
                    }

                    Color newValue = new Color(valueRed, valueGreen, valueBlue); // Возвращение пикселя (Объединение спектров)

                    result.setRGB(x, y, newValue.getRGB()); // Размещение готовых пикселей на итоговой болванке изображения
                }
            }

            File file1 = new File(Application.class.getResource("buffer").getPath());
            ImageIO.write(result, "png", file1);
            Image image1 = new Image(file1.toURI().toString());
            afterImagеForm.setImage(image1);

            System.out.println("Файл был трансформирован"); // Хорошее оповещение

        } catch (IOException e) {

            System.out.println("Файл не удалось трансфомировать"); // Оповещение которое не хотелоть бы видеть
        }
    }

    void transform(File file, int div, int one, int two, int three, int four, int five,int six, int seven, int eight, int nine){
        try {

            BufferedImage source = ImageIO.read(file); // Добыча исходного изображения

            int pictureWidth = source.getWidth(); // Данные исходного изображения
            int pictureHeight = source.getHeight();
            int pictureType = source.getType();

            BufferedImage mid = new BufferedImage(pictureWidth + 2, pictureHeight + 2, pictureType); // Создание болванки для изображения с расширенными краями (для корректной обработки)
            result = new BufferedImage(pictureWidth, pictureHeight, pictureType); //Создание болванки для готового изображения

            Color[] top = new Color[pictureWidth];   //Массивы для краевых пикселей изображений внутри расширенной болванки
            Color[] left = new Color[pictureHeight];
            Color[] bottom = new Color[pictureWidth];
            Color[] right = new Color[pictureHeight];

            for (int x = 0; x < pictureWidth; x++) { // Добавление верхних краевых пикселей для расширенной болванки
                top [x] = new Color(source.getRGB(x, 0));
                mid.setRGB(x + 1, 0, top[x].getRGB());
            }

            for (int y = 0; y < pictureHeight; y++) { // Добавление краевых пикселей по левому краю для расширенной болванки
                left [y] = new Color(source.getRGB(0, y));
                mid.setRGB(0, y + 1, left[y].getRGB());
            }

            for (int x = 0; x < pictureWidth; x++) { // Добавление нижних краевых пикселей для расширенной болванки
                bottom [x] = new Color(source.getRGB(x, pictureHeight - 1));
                mid.setRGB(x + 1, pictureHeight + 1, bottom[x].getRGB());
            }

            for (int y = 0; y < pictureHeight; y++) { // Добавление краевых пикселей по правому краю для расширенной болванки
                right [y] = new Color(source.getRGB(pictureWidth - 1, y));
                mid.setRGB(pictureWidth + 1, y + 1, right[y].getRGB());
            }

            for (int x = 0; x < pictureWidth; x++) { // Определение местоположения исходного изображения внутри расширенной болванки
                for (int y = 0; y < pictureHeight; y++) {
                    Color colorMid = new Color(source.getRGB(x,y));
                    mid.setRGB(x + 1, y + 1, colorMid.getRGB());
                }
            }

            mid.setRGB(0,0, top[0].getRGB());
            mid.setRGB(0,pictureHeight + 1, left[pictureHeight-1].getRGB());
            mid.setRGB(pictureWidth + 1,pictureHeight + 1, bottom[pictureWidth-1].getRGB());
            mid.setRGB(pictureWidth + 1,0, right[0].getRGB());

            int myDiv = div; // Дополнительная переменная для корректной обработки изображения

            int[]  mask = { // Маска обработки изображений (маски задаются конструктором исходя из выбраного фильтра)
                    one,two,three,
                    four,five,six,
                    seven,eight,nine
            };

            for (int x = 0; x < pictureWidth; x++) {    // Каждый поток обрабатывает каждый четверный пиксель ряда, в сумме, обрабатывает четверть изображения
                for (int y = 0; y < pictureHeight; y++) {

                    Color[] color = { // выборка девяти нужных пикселей для их дальнейшей обработки
                            new Color(mid.getRGB(x, y)),
                            new Color(mid.getRGB(x + 1, y)),
                            new Color(mid.getRGB(x + 2, y)),
                            new Color(mid.getRGB(x, y + 1)),
                            new Color(mid.getRGB(x + 1, y + 1)),
                            new Color(mid.getRGB(x + 2, y + 1)),
                            new Color(mid.getRGB(x, y + 2)),
                            new Color(mid.getRGB(x + 1, y + 2)),
                            new Color(mid.getRGB(x + 2, y + 2))
                    };

                    int[] blue = new int[9]; // Разделённые на спектры пиксели имеют свой мини массив
                    int[] red = new int[9];
                    int[] green = new int[9];

                    int[] newBlue = new int[9]; // Эти три массива для обработанных разделённых пикселей
                    int[] newRed = new int[9];
                    int[] newGreen = new int[9];

                    int valueBlue = 0; // Места для готовых пикселей
                    int valueRed = 0;
                    int valueGreen = 0;

                    for (int i = 0; i < 9; i++){ // Обработка пикселей

                        blue[i] = color[i].getBlue(); // Заполнение массивов
                        red[i] = color[i].getRed();
                        green[i] = color[i].getGreen();

                        newBlue[i] = blue[i] * mask[i]; // умножение выбранных пикселей на обрабатывающую маску
                        newRed[i] = red[i] * mask[i];
                        newGreen[i] = green[i] * mask[i];

                        valueBlue += newBlue[i]; // Суммирование полученных элементов спектров для дальнейшей обработки
                        valueRed += newRed[i];
                        valueGreen += newGreen[i];

                    }

                    valueBlue = valueBlue/myDiv; // Кореектировка цвета с использованием дополнительной переменной
                    valueRed = valueRed/myDiv;   // (После суммирования значения пикселей превышают 255)
                    valueGreen = valueGreen/myDiv;

                    if (valueBlue < 0){     // Дополнительная ситуативная обработка вышедших за пределы опредения цвета значений
                        valueBlue = 0;
                    }
                    if (valueRed < 0){
                        valueRed = 0;
                    }
                    if (valueGreen < 0){
                        valueGreen = 0;
                    }

                    if (valueBlue > 255){
                        valueBlue = 255;
                    }
                    if (valueRed > 255){
                        valueRed = 255;
                    }
                    if (valueGreen > 255){
                        valueGreen = 255;
                    }

                    Color newValue = new Color(valueRed, valueGreen, valueBlue); // Возвращение пикселя (Объединение спектров)

                    result.setRGB(x, y, newValue.getRGB()); // Размещение готовых пикселей на итоговой болванке изображения
                }
            }

            File file1 = new File(Application.class.getResource("buffer").getPath());
            ImageIO.write(result, "png", file1);
            Image image1 = new Image(file1.toURI().toString());
            afterImagеForm.setImage(image1);

            System.out.println("Файл был трансформирован"); // Хорошее оповещение

        } catch (IOException e) {

            System.out.println("Файл не удалось трансфомировать"); // Оповещение которое не хотелоть бы видеть
        }
    }

    void transformBandW(File file){ // Метод для обработки изображений черно-белым фильтром
        try {

            BufferedImage source = ImageIO.read(file); //Добыча изображения которое нуждается в обработке
            result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType()); // Создание болванки для обработанного изображения

            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {

                    Color color = new Color(source.getRGB(x, y)); // Выбор пикселя для обработки

                    int blue = color.getBlue(); // Разложение пикселя на спектры
                    int red = color.getRed();
                    int green = color.getGreen();

                    int grey = (int) (red * 0.199 + green * 0.587 + blue * 0.214); // Что то на подобии маски

                    int newRed = grey; // Усреднение значения пикселя приравниванием всех спектров к одному значению
                    int newGreen = grey;
                    int newBlue = grey;

                    Color newColor = new Color(newRed, newGreen, newBlue); // Восстановление пикселя

                    result.setRGB(x, y, newColor.getRGB()); // Установка пикселя на нужную позицию внитри болванки
                }
            }

            File file1 = new File(Application.class.getResource("buffer").getPath());
            ImageIO.write(result, "png", file1);
            Image image1 = new Image(file1.toURI().toString());
            afterImagеForm.setImage(image1);

            System.out.println("Файл был трансформирован"); // Хорошее оповещение

        } catch (IOException e) {

            System.out.println("Файл не удалось трансфомировать"); // Оповещение которое не хотелоть бы видеть
        }
    }
}