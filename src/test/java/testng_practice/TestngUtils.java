package testng_practice;

import com.github.javafaker.Faker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestngUtils {

    public static Object [][] readData(String path){
        List<String> allLines = null;
        try{
            allLines = Files.readAllLines(Path.of(path));
        } catch (IOException e){
            throw new RuntimeException();
        }
        List< List<String> > data = new ArrayList<>();
        for (String eachLine : allLines) {
            String[] split = eachLine.split(",");
            List<String> list = Arrays.asList(split);
            data.add(list);
        }
        Object[][] arr = new Object[data.size()][data.get(0).size()];
        for (int i = 0; i < arr.length; i++) {
            Object[] each = data.get(i).toArray();
            arr[i]=each;
        }
        return arr;
    }

    public static void writeSignUpDataToFile(String fileSignUp, String fileLogIn, int quantity) {

        List<String> username = new ArrayList<>();
        List<String> password = new ArrayList<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileSignUp))) {
            for (int i = 0; i < quantity; i++) {
                Faker faker = new Faker();
                username.add(faker.name().username());
                password.add(faker.internet().password());
                bw.write(username.get(i));
                bw.write(",");
                bw.write(faker.name().firstName());
                bw.write(",");
                bw.write(faker.name().lastName());
                bw.write(",");
                bw.write(faker.internet().emailAddress());
                bw.write(",");
                bw.write(password.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileLogIn))) {
            for (int i = 0; i < quantity; i++) {
                bw.write(username.get(i));
                bw.write(",");
                bw.write(password.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void writeInvalidLoginDataToFile(String fileName, int quantity){

            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                for (int i = 0; i < quantity; i++) {
                    Faker faker = new Faker();
                    bw.write(faker.name().username());
                    bw.write(",");
                    bw.write(faker.internet().password());
                    bw.newLine();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }


}
