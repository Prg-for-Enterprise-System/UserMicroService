package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Lazy;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.save(user);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Override
    public User findByID(int theId) {
        return userDAO.findByID(theId);
    }

    @Override
    @Transactional
    public User updateUser(int id, User updatedUser) {
        User existingUser = userDAO.findByID(id);

        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());

        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setRoles(updatedUser.getRoles());
        return userDAO.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        userDAO.deleteById(theId);
    }

    @Override
    @Transactional
    public void updateUserEnabledStatus(int id, boolean enabled) {
        userDAO.updateUserEnabledStatus(id, enabled);
    }

    @Override
    @Transactional
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        User user = findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }

        String originalFilename = StringUtils.cleanPath(
            photo.getOriginalFilename() != null ? photo.getOriginalFilename() : "upload"  // ← null guard
        );

        int dotIndex = originalFilename.lastIndexOf(".");
        String filenameWithoutExtension = dotIndex > 0 ? originalFilename.substring(0, dotIndex) : originalFilename;
        String filenameExtension = dotIndex > 0 ? originalFilename.substring(dotIndex + 1) : "";

        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp
                + (filenameExtension.isEmpty() ? "" : "." + filenameExtension);

        // Use absolute path to avoid working-directory issues
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().resolve(filename);
        photo.transferTo(uploadPath);

        user.setPhoto(filename);
        userDAO.save(user);  // ← use userDAO.save() directly to avoid re-encoding password
    }
}