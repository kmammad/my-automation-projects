package testng_practice2;

import com.github.javafaker.Faker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Practice2Utils {

    public static void writeSignUpDataToFile(String fileSignUp, String fileLogIn, int quantity) {

        List<String> email = new ArrayList<>();
        List<String> password = new ArrayList<>();

      //  String email, String password, String fullName, String month, int day, int year

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileSignUp))) {
            for (int i = 0; i < quantity; i++) {
                Faker faker = new Faker();

                email.add(faker.internet().emailAddress());
                password.add(faker.internet().password());

                bw.write(email.get(i));
                bw.write(",");
                bw.write(password.get(i));
                bw.write(",");
                bw.write(faker.name().fullName());
                bw.write(",");
                bw.write(String.valueOf(faker.number().numberBetween(1, 27)));
                bw.write(",");
                bw.write(String.valueOf(faker.number().numberBetween(1960, 2005)));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileLogIn))) {
            for (int i = 0; i < quantity; i++) {
                bw.write(email.get(i));
                bw.write(",");
                bw.write(password.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
