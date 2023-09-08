package Sujith.MailCrud.Repository;

import Sujith.MailCrud.Entity.DeletedMail;
import Sujith.MailCrud.Entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail,Long>
{

    List<Mail> findBySender(String sender);

    List<Mail> findByRecipientsContainingAndDeletedForRecipientIsFalse(String recipient);

}
