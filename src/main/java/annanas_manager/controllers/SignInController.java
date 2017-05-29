package annanas_manager.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SignInController {

    @RequestMapping(value = "/sign_up", method = RequestMethod.GET)
    public Principal login(Principal principal) {
        return principal;
    }
}
