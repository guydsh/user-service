import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrypPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encrypedPassword = encoder.encode("admin");
        System.out.println(encrypedPassword);
    }
}
