package bt.edu.gcit.usermicroservice.rest; 
 
import bt.edu.gcit.usermicroservice.entity.Role; 
import bt.edu.gcit.usermicroservice.service.RoleService; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.bind.annotation.RequestBody; 
 
@RestController 
@RequestMapping("/api") 
public class RoleRestController { 
   private RoleService roleService; 
 
   @Autowired 
   public RoleRestController(RoleService roleService) { 
       this.roleService = roleService; 
   } 
 
   @PostMapping("/roles") 
   public void addRole(@RequestBody Role role) { 
       roleService.addRole(role); 
   } 
} 