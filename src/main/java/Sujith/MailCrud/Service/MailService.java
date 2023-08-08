package Sujith.MailCrud.Service;

import Sujith.MailCrud.Entity.DeletedMail;
import Sujith.MailCrud.Entity.Mail;
import Sujith.MailCrud.Exception.ResourceNotFoundException;
import Sujith.MailCrud.Repository.DeleteRepository;
import Sujith.MailCrud.Repository.MailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MailService
{
    private final MailRepository mailRepository;
    private final DeleteRepository deleteRepository;
    public Optional<Mail> getMailById(Long id)
    {
        return mailRepository.findById(id);
    }

    public Mail saveMail(Mail mail)
    {
        return mailRepository.save(mail);
    }


    public void deleteMail(Long id)
    {
        Optional<Mail> optionalMail=mailRepository.findById(id);

        if(optionalMail.isPresent()){
            Mail mail=optionalMail.get();
        DeletedMail deletedMail = new DeletedMail();
        deletedMail.setSender(mail.getSender());
        deletedMail.setRecipients(new ArrayList<>(mail.getRecipients()));
        deletedMail.setSubject(mail.getSubject());
        deletedMail.setContent(mail.getContent());
        deletedMail.setDeletedAt(LocalDateTime.now());

        deleteRepository.save(deletedMail);
        mailRepository.delete(mail);}
        else {
            throw new ResourceNotFoundException("Mail not found with id: " + id);
        }
    }

    public List<Mail> getMailsByRecipient(String recipient) {
        return mailRepository.findByRecipientsContaining(recipient);
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
        return deleteRepository.findBySender(userEmail);
    }
}
