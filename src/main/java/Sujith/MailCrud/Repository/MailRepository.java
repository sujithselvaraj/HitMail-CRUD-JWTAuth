package Sujith.MailCrud.Repository;

import Sujith.MailCrud.Entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail,Long>
{
    List<Mail> findByRecipientsContaining(String recipient);

    List<Mail> findBySender(String sender);
}
