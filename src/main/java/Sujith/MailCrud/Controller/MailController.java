package Sujith.MailCrud.Controller;


import Sujith.MailCrud.DTO.SimpleMailDTO;
import Sujith.MailCrud.Entity.*;
import Sujith.MailCrud.Service.MailService;
import Sujith.MailCrud.Service.UserService;
import Sujith.MailCrud.utility.JWTUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/mails")
public class MailController
{
    private MailService mailService;
    private UserService userService;
    private JWTUtility jwtUtility;
    private AuthenticationManager authenticationManager;

    //get email by id (which is for view)
    @GetMapping("/{id}")
    public ResponseEntity<Mail> getMailById(@PathVariable Long id) {
        Optional<Mail> mail = mailService.getMailById(id);
        return mail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //write a mail
    @PostMapping
    public ResponseEntity<?> saveMail(@RequestBody Mail mail) {
        for (String recipient : mail.getRecipients()) {
            User user = userService.getUserByEmail(recipient);
            User sender=userService.getUserByEmail(mail.getSender());
            if (user == null) {
                return  ResponseEntity.badRequest().body("Recipient " + recipient + " is not a valid user.");
            }
            if(sender==null)
            {
                return ResponseEntity.badRequest().body("Sender " + mail.getSender() + " is not a valid user.");
            }
        }

        Mail savedMail = mailService.saveMail(mail);
        return new ResponseEntity<>("Email Sent Successfully to "+mail.getRecipients(), HttpStatus.CREATED);
    }

    //delete the emails by their id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMailById(@PathVariable("id") Long id) {
        mailService.deleteMail(id);
        return new ResponseEntity<>("Email Deleted Successfully.",HttpStatus.OK);
    }

    //get the received emails or (inbox) for the specific logged users
    @GetMapping("/received-mails")
    public ResponseEntity<List<SimpleMailDTO>> getRecipientMails(@RequestHeader String recipient) {
        List<Mail> recipientMails = mailService.getMailsByRecipient(recipient);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (Mail mail : recipientMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTOs.add(simpleMailDTO);
        }

        return ResponseEntity.ok(simpleMailDTOs);
    }

    //sent-box get the specific emails sent by the specific users
    @GetMapping("/get-send-emails")
    public ResponseEntity<List<SimpleMailDTO>> getMailsBySender(@RequestHeader String sender)
    {
        List<Mail> sendedMails=mailService.getMailsBySender(sender);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (Mail mail : sendedMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTOs.add(simpleMailDTO);
        }

        return ResponseEntity.ok(simpleMailDTOs);
    }

    //update the mail by id
    @PutMapping("/update-mail/{id}")
    public ResponseEntity<Mail> updateMailById(@PathVariable Long id,@RequestBody Mail updatedMail)
    {
        Mail updateMail=mailService.updateMailById(updatedMail, id);
        return ResponseEntity.ok(updateMail);
    }

    //get the deleted mails (trash for specific users)
    @GetMapping("/deleted-mails")
    public ResponseEntity<List<SimpleMailDTO>> getDeletedMailsForUser(@RequestHeader String userEmail) {
        List<DeletedMail> deletedMails = mailService.getDeletedMailsForUser(userEmail);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (DeletedMail mail : deletedMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTOs.add(simpleMailDTO);
        }
        return ResponseEntity.ok(simpleMailDTOs);
    }
    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception
    {
        System.out.println("1212");
        try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   jwtRequest.getUsername(),
                   jwtRequest.getPassword())
            );
            System.out.println("1");

            final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
            String token = null;

            if (userDetails.getUsername().equals (jwtRequest.getUsername())) {
                token = jwtUtility.generateToken(userDetails);
                System.out.println(token);

            }
            return new JWTResponse(token);

        }
        catch (BadCredentialsException e)
        {
            throw new Exception("INVALID_CREDENTIALS",e);
        }


    }
}
