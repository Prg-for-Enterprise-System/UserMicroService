package bt.edu.gcit.usermicroservice.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;
import bt.edu.gcit.usermicroservice.service.UserService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;
    private ImageUploadService imageUploadService;

    @Autowired
    public UserRestController(
            UserService userService,
            ImageUploadService imageUploadService) {

        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public User save(
            @RequestPart("firstName") @Valid @NotNull String firstName,

            @RequestPart("lastName") @Valid @NotNull String lastName,

            @RequestPart("email") @Valid @NotNull String email,

            @RequestPart("password") @Valid @NotNull String password,

            @RequestPart("photo") @Valid @NotNull MultipartFile photo,

            @RequestPart("roles") @Valid @NotNull String rolesJson) {

        try {

            // Create User object
            User user = new User();

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            // Convert roles JSON string to Set<Role>
            ObjectMapper objectMapper = new ObjectMapper();

            Set<Role> roles = objectMapper.readValue(
                    rolesJson,
                    new TypeReference<Set<Role>>() {
                    });

            user.setRoles(roles);

            // Upload image to Cloudinary
            System.out.println("Uploading photo to Cloudinary...");

            String imageUrl = imageUploadService.uploadImage(photo);

            System.out.println("Cloudinary URL: " + imageUrl);

            // Save Cloudinary URL into photo field
            user.setPhoto(imageUrl);

            // Save user
            return userService.save(user);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error while uploading photo",
                    e
            );
        }
    }

    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(
            @RequestParam String email) {

        boolean isDuplicate = userService.isEmailDuplicate(email);

        return ResponseEntity.ok(isDuplicate);
    }

    @PutMapping("/users/{id}")
    public User updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser) {

        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {

        userService.deleteById(id);
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id,
            @RequestBody Map<String, Boolean> requestBody) {

        Boolean enabled = requestBody.get("enabled");

        userService.updateUserEnabledStatus(id, enabled);

        System.out.println("User enabled status updated successfully");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }
}