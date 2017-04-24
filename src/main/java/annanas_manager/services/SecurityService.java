package annanas_manager.services;



public interface SecurityService {

    String findLoggedInUserEmail();
    void autologin(String username, String password);
}
