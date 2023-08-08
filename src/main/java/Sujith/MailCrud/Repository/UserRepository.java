package Sujith.MailCrud.Repository;

import Sujith.MailCrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>
{
    User findByEmail(String email);
}
