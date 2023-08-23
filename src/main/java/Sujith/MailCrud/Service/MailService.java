package Sujith.MailCrud.Service;

import Sujith.MailCrud.Entity.DeletedMail;
import Sujith.MailCrud.Entity.Mail;
import Sujith.MailCrud.Exception.ResourceNotFoundException;
import Sujith.MailCrud.Repository.DeleteRepository;
import Sujith.MailCrud.Repository.MailRepository;
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
    public Optional<Mail> getMailById(Long id){
        return mailRepository.findById(id);
    }

    public Mail saveMail(Mail mail)
    {
        return mailRepository.save(mail);
    }





//    public List<Mail> getMailsByRecipient(String recipient) {
//        // Query the database to find emails where the recipient's email is in the list
//        // and the recipientDeletionStatus is false
//        return mailRepository.findByRecipientsContainingAndRecipientDeletionStatus(recipient, false);
//    }


    public List<Mail> getUndeletedMailsByRecipient(String recipient) {
        return mailRepository.findByRecipientsContainingAndDeletedForRecipientIsFalse(recipient);
    }


//    public List<Mail> getUndeletedMailsByRecipient(String recipient) {
//        return mailRepository.findUndeletedMailsByRecipient(recipient);
//    }


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

//final
//    public void deleteMail(Long id, String userEmail) {
//        Optional<Mail> optionalMail = mailRepository.findById(id);
//
//        if (optionalMail.isPresent()) {
//            Mail mail = optionalMail.get();
//
//            if (mail.getRecipients().contains(userEmail)) {
//                // Check if the mail is already deleted for this user
//                if (!mail.isDeletedForRecipient()) {
//                    // Mark the mail as deleted for the recipient
//                    mail.setDeletedForRecipient(true);
//                    mailRepository.save(mail);
//
//                    // Create a DeletedMail entry for this user
//                    DeletedMail deletedMail = new DeletedMail();
//                    deletedMail.setSender(mail.getSender());
//                    deletedMail.setRecipients(Collections.singletonList(userEmail));
//                    deletedMail.setSubject(mail.getSubject());
//                    deletedMail.setContent(mail.getContent());
//                    deletedMail.setId(mail.getId());
//                    deletedMail.setDeletedAt(LocalDateTime.now());
//                    deleteRepository.save(deletedMail);
//                } else {
//                    throw new ResourceNotFoundException("This email is already deleted for you.");
//                }
//            } else {
//                throw new ResourceNotFoundException("You don't have permission to delete this email.");
//            }
//        } else {
//            throw new ResourceNotFoundException("Mail not found with id: " + id);
//        }
//    }





//    public List<DeletedMail> getDeletedMailsForUser(String userEmail) {
//        return deleteRepository.findByRecipientsContaining(userEmail);
//    }




    public List<DeletedMail> getDeletedMailsForUser(String userEmail) {
        List<DeletedMail> deletedMails = deleteRepository.findByRecipientsContaining(userEmail);
        return deletedMails;
    }
//    public List<Mail> getInboxMailsForUser(String userEmail) {
//        List<Mail> allMails = mailRepository.findAll(); // Fetch all emails
//        List<Mail> inboxMails = new ArrayList<>();
//
//        for (Mail mail : allMails) {
//            // Check if the email is not deleted for this recipient
//            if (mail.getRecipientDeletionStatus().getOrDefault(userEmail, false)) {
//                inboxMails.add(mail);
//            }
//        }
//
//        return inboxMails;
//    }
//public List<Mail> getInboxMailsForUser(String userEmail) {
//    // Query the database to find emails where the recipient's email is in the list
//    // and the recipientDeletionStatus is false
//    return mailRepository.findByRecipientsContainingAndRecipientDeletionStatus(userEmail, false);
//}



//    public void deleteMail(Long id, String userEmail) {
//        Optional<Mail> optionalMail = mailRepository.findById(id);
//
//        if (optionalMail.isPresent()) {
//            Mail mail = optionalMail.get();
//
//            if (mail.getRecipients().contains(userEmail)) {
//                // Check if the mail is already deleted for this user
//                if (!mail.getRecipientDeletionStatus().getOrDefault(userEmail, false)) {
//                    // Mark the mail as deleted for the recipient
//                    mail.getRecipientDeletionStatus().put(userEmail, true);
//                    mailRepository.save(mail);
//
//                    // Create a DeletedMail entry for this user
//                    DeletedMail deletedMail = new DeletedMail();
//                    deletedMail.setSender(mail.getSender());
//                    deletedMail.setRecipients(Collections.singletonList(userEmail));
//                    deletedMail.setSubject(mail.getSubject());
//                    deletedMail.setContent(mail.getContent());
//                    deletedMail.setId(mail.getId());
//                    deletedMail.setDeletedAt(LocalDateTime.now());
//                    deleteRepository.save(deletedMail);
//                } else {
//                    throw new ResourceNotFoundException("This email is already deleted for you.");
//                }
//            } else {
//                throw new ResourceNotFoundException("You don't have permission to delete this email.");
//            }
//        } else {
//            throw new ResourceNotFoundException("Mail not found with id: " + id);
//        }
//    }
//final
//
//
    public void deleteMail(Long id, String userEmail) {
        Optional<Mail> optionalMail = mailRepository.findById(id);

        if (optionalMail.isPresent()) {
            Mail mail = optionalMail.get();


                if (mail.getRecipients().contains(userEmail)) {
                // Check if the mail is already deleted for this user
                if (!mail.isDeletedForRecipient()) {
                    // Mark the mail as deleted for the recipient
                    mail.setDeletedForRecipient(true);
                                        mail.getRecipientDeletionStatus().put(userEmail, true);

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

    public Optional<DeletedMail> viewDeletedMail(Long id){
        return deleteRepository.findById(id);
    }

//    public void deleteMail(Long id, String userEmail) {
//        Optional<Mail> optionalMail = mailRepository.findById(id);
//
//        if (optionalMail.isPresent()) {
//            Mail mail = optionalMail.get();
//
//            // Check if the user is a recipient of the mail
//            if (mail.getRecipients().contains(userEmail)) {
//                // Check if the mail is already deleted for this user
//                if (!mail.getRecipientDeletionStatus().getOrDefault(userEmail, false)) {
//                    // Mark the mail as deleted for the recipient
//                    mail.getRecipientDeletionStatus().put(userEmail, true);
//                    mailRepository.save(mail);
//
//                    // Create a DeletedMail entry for this user
//                    DeletedMail deletedMail = new DeletedMail();
//                    deletedMail.setSender(mail.getSender());
//                    deletedMail.setRecipients(Collections.singletonList(userEmail));
//                    deletedMail.setSubject(mail.getSubject());
//                    deletedMail.setContent(mail.getContent());
//                    deletedMail.setId(mail.getId());
//                    deletedMail.setDeletedAt(LocalDateTime.now());
//                    deleteRepository.save(deletedMail);
//                } else {
//                    throw new ResourceNotFoundException("This email is already deleted for you.");
//                }
//            } else {
//                throw new ResourceNotFoundException("You don't have permission to delete this email.");
//            }
//        } else {
//            throw new ResourceNotFoundException("Mail not found with id: " + id);
//        }
//    }











}
