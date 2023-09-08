package Sujith.MailCrud.Service;

import Sujith.MailCrud.Entity.DeletedMail;
import Sujith.MailCrud.Entity.Mail;
import Sujith.MailCrud.Exception.ResourceNotFoundException;
import Sujith.MailCrud.Repository.DeleteRepository;
import Sujith.MailCrud.Repository.MailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MailService
{
    private final MailRepository mailRepository;
    private final DeleteRepository deleteRepository;
    public Optional<Mail> getMailById(Long id){
        return mailRepository.findById(id);
    }

    public Mail saveMail(Mail mail)
    {
        return mailRepository.save(mail);
    }

    public List<Mail> getUndeletedMailsByRecipient(String recipient) {
        return mailRepository.findByRecipientsContainingAndDeletedForRecipientIsFalse(recipient);
    }


    public List<Mail> getMailsBySender(String sender)
    {
        return mailRepository.findBySender(sender);
    }

    public Mail updateMailById(Mail updatedMail, Long id) {
        Optional<Mail> optionalExistingMail = mailRepository.findById(id);

        if (optionalExistingMail.isPresent()) {
            Mail existingMail = optionalExistingMail.get();


            existingMail.setSender(updatedMail.getSender());
            existingMail.setRecipients(updatedMail.getRecipients());
            existingMail.setSubject(updatedMail.getSubject());
            existingMail.setContent(updatedMail.getContent());

            return mailRepository.save(existingMail);
        } else {
            throw new ResourceNotFoundException("Mail not found with id: " + id);
        }
    }


    public List<DeletedMail> getDeletedMailsForUser(String userEmail) {
        List<DeletedMail> deletedMails = deleteRepository.findByRecipientsContaining(userEmail);
        return deletedMails;
    }

    public void deleteMail(Long id, String userEmail) {
        Optional<Mail> optionalMail = mailRepository.findById(id);

        if (optionalMail.isPresent()) {
            Mail mail = optionalMail.get();


                if (mail.getRecipients().contains(userEmail)) {
                if (!mail.isDeletedForRecipient()) {
                    mail.setDeletedForRecipient(true);
                                        mail.getRecipientDeletionStatus().put(userEmail, true);

                    mailRepository.save(mail);


                    DeletedMail deletedMail = new DeletedMail();
                    deletedMail.setSender(mail.getSender());
                    deletedMail.setRecipients(Collections.singletonList(userEmail));
                    deletedMail.setSubject(mail.getSubject());
                    deletedMail.setContent(mail.getContent());
                    deletedMail.setId(mail.getId());
                    deletedMail.setDeletedAt(LocalDateTime.now());
                    deleteRepository.save(deletedMail);
                } else {
                    throw new ResourceNotFoundException("This email is already deleted for you.");
                }
            } else {

                throw new ResourceNotFoundException("You don't have permission to delete this email.");
            }
        } else {
            throw new ResourceNotFoundException("Mail not found with id: " + id);
        }
    }

    public Optional<DeletedMail> viewDeletedMail(Long id){
        return deleteRepository.findById(id);
    }

    @Transactional
    public void clearTrash(String recipientEmail) {
        deleteRepository.deleteByRecipientsContaining(recipientEmail);
    }

    @Transactional
    public void deleteDeletedMailById(Long id) {
        deleteRepository.deleteById(id);
    }






}
