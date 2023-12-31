package Sujith.MailCrud.Repository;

import Sujith.MailCrud.Entity.DeletedMail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeleteRepository extends JpaRepository<DeletedMail,Long>
{
    List<DeletedMail> findByRecipientsContaining(String recipient);

    void deleteByRecipientsContaining(String recipientEmail);
}
