package Sujith.MailCrud.Repository;

import Sujith.MailCrud.Entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail,Long>
{
    List<Mail> findByRecipientsContaining(String recipient);

    List<Mail> findBySender(String sender);

    List<Mail> findByRecipientsContainingAndDeletedForRecipientIsFalse(String recipient);
    @Query("SELECT m FROM Mail m WHERE :recipient MEMBER OF m.recipients AND m.recipientDeletionStatus[KEY(m.recipientDeletionStatus)] = false")
    List<Mail> findUndeletedMailsByRecipient(@Param("recipient") String recipient);


    List<Mail> findByRecipientsContainingAndRecipientDeletionStatus(String recipient, boolean recipientDeletionStatus);

//    List<Mail> findByRecipientsContainingAndDeletedForRecipientIsTrue(String recipient);


}
