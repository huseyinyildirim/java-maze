package org.buluton;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * JavaFX App
 */
public class App extends Application {

    private static GraphicsContext gc;

    @FXML
    private Label lblWidth;
    @FXML
    private Label lblHeight;
    @FXML
    private Label lblStart;
    @FXML
    private Label lblExit;
    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource( "app.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 1100, 400);
            stage.setTitle("Labirent Oyunu");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Hata: " + e.getMessage()); //Exception hatası
            e.printStackTrace(); // Detaylı hata dökümü
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    private void btnRead() {
        try {
            // Resource içindeki maze.txt okumak için File sınıfı ile bir instance oluşturuyoruz.
            File f = new File("src/main/resources/maze.txt");

            // İlgili dosyamızı okumak için Scanner sınıfını kullanıyoruz.
            Scanner sc = new Scanner(f);

            // Satır sayısını tutmak için
            int rowsCount = 0;

            // Sütun sayısını tutmak için
            int columnCount = 0;

            List<Coordinate> coordinates = new ArrayList<>();

            while (sc.hasNextLine()) {
                // Dosyayı satır satır alıyoruz
                String data = sc.nextLine();

                // Her satır geliryor mu kontrol etmek için print edelim
                //System.out.println(data);

                // Her satırdaki # sembolünü hesaplamak için split ediyoruz
                String[] arrSharp = data.split("#");
                // Sütunu uzunluğunu bulmak için en büyük split uzunluğunu column değişkenine set ediyoruz
                columnCount = (arrSharp.length > columnCount) ? arrSharp.length : columnCount;
                // Her satır döndüğünde rows bir arttırıyoruz
                rowsCount++;

                // maze.txt içindeki her karakterin koordinatlarını çıkarıyoruz.
                String[] arrChar = data.split("(?!^)");

                // Burası en önemli kısımı. Split edilen karakterlerin koordinatlarını liste ekliyoruz
                for (int i = 0; i < arrChar.length; i++){
                    coordinates.add(new Coordinate(i+1, rowsCount, arrChar[i]));
                }
            }

            // Scanner sınıfı kapatmamız lazım ki dosya ile bağlantısı kesilsin
            sc.close();

            // Genişlik ve uzunluk değerler yazdırıyoruz
            lblHeight.setText(String.valueOf(rowsCount));
            lblWidth.setText(String.valueOf(columnCount));

            gc = canvas.getGraphicsContext2D();

            // Listeye koordinatları alınan karakterlerin tipine göre canvasta oluşturuyoruz
            for (int i = 0; i < coordinates.size(); i++){

                if (coordinates.get(i)._symbol.equals("#")) {
                    gc.setFill(Color.BLACK);
                    gc.fillText("#", 20 + coordinates.get(i)._x * 10, 25 + coordinates.get(i)._y * 10);
                } else if (coordinates.get(i)._symbol.equals("S")) {
                    gc.setFill(Color.GREEN);
                    gc.fillText("S", 20 + coordinates.get(i)._x * 10, 25 + coordinates.get(i)._y * 10);

                    // Burada neden 2'ye bölüp 1 ekliyoruz.
                    // maze.txt içindeki Y düzleminde # arasında boşluklar olduğu için onlarıda sayıyor.
                    // 1 arttırmamızın sebebi for döngüsü sıfırdan başladığı için
                    lblStart.setText((coordinates.get(i)._x/2)+1 +", "+ coordinates.get(i)._y);
                } else if (coordinates.get(i)._symbol.equals("E")) {
                    gc.setFill(Color.RED);
                    gc.fillText("E", 20 + coordinates.get(i)._x * 10, 25 + coordinates.get(i)._y * 10);

                    // Burada neden 2'ye bölüp 1 ekliyoruz.
                    // maze.txt içindeki Y düzleminde # arasında boşluklar olduğu için onlarıda sayıyor.
                    // 1 arttırmamızın sebebi for döngüsü sıfırdan başladığı için
                    lblExit.setText((coordinates.get(i)._x/2)+1 +", "+ coordinates.get(i)._y);
                } else {
                    gc.setFill(Color.TRANSPARENT);
                    gc.fillText(" ", 20 + coordinates.get(i)._x * 10, 25 + coordinates.get(i)._y * 10);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Hata: " + e.getMessage()); //Exception hatası
            e.printStackTrace(); // Detaylı hata dökümü
        }
    }

    @FXML
    private void btnSolved(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText("Bilgilendirme");
        alert.setContentText("Lütfen btnSolved() method yorum satırlarını okuyunuz.");

        alert.showAndWait();

        /*
        Sayın Ahmet Hocam,

        Çözümüde yapacak algoritmayı kurdum ama verdiğiniz dosyadaki karakter düzeninden dolayı bir çözüm üretemedim.
        Y ekseninde # sembollerin arasında boşluklar olması beni kilitliyor.
        Eğer iki # sembolü arasında boşluklar olmasa labirentteki bütün boşluklar benim için yol anlamına geliyor.
        S karakteri koordinatını zaten bulmuştuk.
        Döngü ile gittiğimiz koordinattaki sembolü alarak orası duvar mı yoksa boşluk mu kontrol edebiliriz.

        Çözüm yolunu bulmak için, etrafındaki koordinatları gezmek için aşağıdaki liste kullanacağız.

        char right = coordinates[row][col + 1];
		char left = coordinates[row][col - 1];
		char up = coordinates[row - 1][col];
		char down = coordinates[row + 1][col];

		Eğer çıkmazsa girersek boşluk buluncaya kadar bir geri adıma yani koordinata aldıracağız.
		Bu çözüm tamamen recursive bir fonksiyona dayanmaktadır.
         */
    }

    @FXML
    private void btnClear() {
        // Labelleri sıfırlıyoruz
        lblHeight.setText("0");
        lblWidth.setText("0");

        lblStart.setText("0, 0");
        lblExit.setText("0, 0");

        // Canvası temizliyoruz
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    private void btnExit() {
        // Uygulamadan çıkıyoruz
        System.exit(0);
    }
}