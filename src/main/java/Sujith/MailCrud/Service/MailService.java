package Sujith.MailCrud.Service;

import Sujith.MailCrud.Entity.DeletedMail;
import Sujith.MailCrud.Entity.Mail;
import Sujith.MailCrud.Exception.ResourceNotFoundException;
import Sujith.MailCrud.Repository.DeleteRepository;
import Sujith.MailCrud.Repository.MailRepository;
import Sujith.MailCrud.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    private UserRepository userRepository;
    public Optional<Mail> getMailById(Long id){
        return mailRepository.findById(id);
    }

    public Mail saveMail(Mail mail)
    {
        return mailRepository.save(mail);
    }

//    public Mail saveMail(Mail mail) {
//        Mail savedMail = mailRepository.save(mail);
//        for (String recipientEmail : mail.getRecipients()) {
//            MailRecipient mailRecipient = new MailRecipient();
//            mailRecipient.setMail(savedMail);
//            mailRecipient.setRecipientEmail(recipientEmail);
//            mailRecipientRepository.save(mailRecipient);
//        }
//        return savedMail;
//    }


//    public List<Mail> getMailsByRecipient(String recipient) {
//        return mailRepository.findByRecipientsContaining(recipient);
//    }

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

//    public void deleteMail(Long id, String userEmail) {
//        Optional<Mail> optionalMail = mailRepository.findById(id);
//
//        if (optionalMail.isPresent()) {
//            Mail mail = optionalMail.get();
//
//            if (mail.getRecipients().contains(userEmail)) {
//                // Move the mail to the recipient's deleted mails
//                DeletedMail deletedMail = new DeletedMail();
//                deletedMail.setSender(mail.getSender());
//                deletedMail.setRecipients(Collections.singletonList(userEmail));
//                deletedMail.setSubject(mail.getSubject());
//                deletedMail.setContent(mail.getContent());
//                deletedMail.setDeletedAt(LocalDateTime.now());
//                deleteRepository.save(deletedMail);
//
//                // Remove the email from recipient's inbox
//                mail.getRecipients().remove(userEmail);
//                mailRepository.save(mail);
//            } else {
//                throw new ResourceNotFoundException("You don't have permission to delete this email.");
//            }
//        } else {
//            throw new ResourceNotFoundException("Mail not found with id: " + id);
//        }
//    }


//    public void deleteMail(Long id, String userEmail) {
//        Optional<Mail> optionalMail = mailRepository.findById(id);
//
//        if (optionalMail.isPresent()) {
//            Mail mail = optionalMail.get();
//
//            if (mail.getRecipients().contains(userEmail)) {
//                // Mark the mail as deleted for the recipient
//                mail.setDeletedForRecipient(true);
//                mailRepository.save(mail);
//
//                // Move the mail to the recipient's deleted mails
//                DeletedMail deletedMail = new DeletedMail();
//                deletedMail.setSender(mail.getSender());
//                deletedMail.setRecipients(Collections.singletonList(userEmail));
//                deletedMail.setSubject(mail.getSubject());
//                deletedMail.setContent(mail.getContent());
//                deletedMail.setDeletedAt(LocalDateTime.now());
//                deleteRepository.save(deletedMail);
//            } else {
//                throw new ResourceNotFoundException("You don't have permission to delete this email.");
//            }
//        } else {
//            throw new ResourceNotFoundException("Mail not found with id: " + id);
//        }
//    }


    ///corct
//public void deleteMail(Long id, String userEmail) {
//    Optional<Mail> optionalMail = mailRepository.findById(id);
//
//    if (optionalMail.isPresent()) {
//        Mail mail = optionalMail.get();
//
//        if (mail.getRecipients().contains(userEmail)) {
//            // Mark the mail as deleted for the recipient
//            mail.setDeletedForRecipient(true);
//            mailRepository.save(mail);
//
//            // Move the mail to the recipient's deleted mails
//            DeletedMail deletedMail = new DeletedMail();
//            deletedMail.setSender(mail.getSender());
//            deletedMail.setRecipients(Collections.singletonList(userEmail));
//            deletedMail.setSubject(mail.getSubject());
//            deletedMail.setContent(mail.getContent());
//            deletedMail.setDeletedAt(LocalDateTime.now());
//            deleteRepository.save(deletedMail);
//        } else {
//            throw new ResourceNotFoundException("You don't have permission to delete this email.");
//        }
//    } else {
//        throw new ResourceNotFoundException("Mail not found with id: " + id);
//    }
//}


    public void deleteMail(Long id, String userEmail) {
        Optional<Mail> optionalMail = mailRepository.findById(id);

        if (optionalMail.isPresent()) {
            Mail mail = optionalMail.get();

            if (mail.getRecipients().contains(userEmail)) {
                // Check if the mail is already deleted for this user
                if (!mail.isDeletedForRecipient()) {
                    // Mark the mail as deleted for the recipient
                    mail.setDeletedForRecipient(true);
                    mailRepository.save(mail);

                    // Create a DeletedMail entry for this user
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





    public List<DeletedMail> getDeletedMailsForUser(String userEmail) {
        return deleteRepository.findByRecipientsContaining(userEmail);
    }














}
