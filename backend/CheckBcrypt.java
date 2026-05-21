import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class CheckBcrypt {
  public static void main(String[] args) {
    System.out.println(new BCryptPasswordEncoder().matches("Senha123", "$2a$10$7EqJtq98hPqEX7fNZaFWoOaQ8m0LkP4g3l8Y7Y9fQfGm0m3Y0sY5K"));
  }
}
