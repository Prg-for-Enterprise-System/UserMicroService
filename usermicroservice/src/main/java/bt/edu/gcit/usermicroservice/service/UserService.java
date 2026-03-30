package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.web.multipart.MultipartFile; 
import java.io.IOException; 

public interface UserService {
    User save(User user);
    boolean isEmailDuplicate(String email); 
    User updateUser(int id, User updatedUser);
    void deleteById(int theId); 
    void updateUserEnabledStatus(int id, boolean enabled); 
    void uploadUserPhoto(int id, MultipartFile photo) throws IOException; 
    User findByID(int theId); 
}