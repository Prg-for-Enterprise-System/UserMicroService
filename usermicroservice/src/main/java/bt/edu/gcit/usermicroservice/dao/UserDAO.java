package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;

public interface UserDAO {
    User save(User user);
}