package bt.edu.gcit.usermicroservice.security.oauth;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import bt.edu.gcit.usermicroservice.service.CustomerService;
import bt.edu.gcit.usermicroservice.entity.Customer;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

@Component
public class OAuth2LoginSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {
    private CustomerService customerService;

    @Autowired
    @Lazy
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oauthUser = (CustomerOAuth2User) authentication.getPrincipal();
        String name = oauthUser.getName();
        String email = oauthUser.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oauthUser.getClientName();
        System.out.println("OAuth2LoginSuccessHandler: " + name + " | " + email);
        System.out.println("Client Name : " + clientName);
        AuthenticationType authenticationType = getAuthenticationType(clientName);
        Customer customer = customerService.findByEMail(email);
        if (customer == null) {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode,
                    authenticationType);
        } else {
            customerService.updateAuthenticationType(customer.getId(),
                    authenticationType);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName) {
        if (clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else {
            return AuthenticationType.DATABASE;
        }
    }
}