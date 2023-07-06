package trabalho.entities;

import java.security.MessageDigest;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AuthToken {
    public enum UserType {
        student,
        teacher
    }

    @Id
    private String value;
    @Column(nullable = false)
    private UserType userType;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String cpf;
    @Column(nullable = false)
    private String phoneNumber;

    public AuthToken() {}

    public AuthToken(UserType userType, String name, String email, String phoneNumber, String cpf) throws Exception {
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        genToken();
    }

    public String value() {
        return this.value;
    }

    public UserType userType() {
        return this.userType;
    }

    public String name() {
        return this.name;
    }

    public String email() {
        return this.email;
    }

    public String cpf() {
        return this.cpf;
    }

    public String phoneNumber() { return this.phoneNumber; }

    private void genToken() throws Exception {
        Random random = new Random();
        int number = random.nextInt(1000001);
        String token = (this.name + this.email + this.phoneNumber + Integer.toString(number));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] inputBytes = token.getBytes();
        byte[] hashBytes = md.digest(inputBytes);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        this.value = stringBuilder.toString();
    }
}
