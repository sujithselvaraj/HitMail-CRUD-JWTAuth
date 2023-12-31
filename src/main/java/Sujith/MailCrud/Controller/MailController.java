package Sujith.MailCrud.Controller;


import Sujith.MailCrud.DTO.SimpleMailDTO;
import Sujith.MailCrud.Entity.*;
import Sujith.MailCrud.Exception.ResourceNotFoundException;
import Sujith.MailCrud.Service.MailService;
import Sujith.MailCrud.Service.UserService;
//import Sujith.MailCrud.utility.JWTUtility;
import lombok.AllArgsConstructor;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/mails")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600)
public class MailController
{
    private MailService mailService;

    //get email by id (which is for view)
    @GetMapping("/{id}")
    public ResponseEntity<Mail> getMailById(@PathVariable Long id) {

        Optional<Mail> mail = mailService.getMailById(id);

        return mail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/view/{id}")
    public ResponseEntity<DeletedMail> viewDeletedMail(@PathVariable Long id) {

        Optional<DeletedMail> mail = mailService.viewDeletedMail(id);

        return mail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
//    write a mail
    @PostMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> saveMail(@RequestBody Mail mail,@RequestHeader String username) {

        mail.setSender(username);
        mail.setTime(LocalDateTime.now());
        for (String recipient : mail.getRecipients()) {
            if (doesUserExistByUsername(recipient)==false)
            {
                return  ResponseEntity.badRequest().body("Recipient " + recipient + " is not a valid user.");
            }
        }

        Mail savedMail = mailService.saveMail(mail);
        ApiResponse<String> response = new ApiResponse<>("success", "Email Sent Successfully"+mail.getRecipients(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public boolean doesUserExistByUsername(String username) {
        try {
            String url = "http://localhost:8080/admin/master/console/#/mailapplication/users?username=" + username;
            System.out.println(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("sujith", "sujith@1234");

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.println(response.getStatusCode().is2xxSuccessful());

            return response.getStatusCode().is2xxSuccessful();
        }
         catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //get the received emails or (inbox) for the specific logged users
    @GetMapping("/received-mails")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<ApiResponse<List<SimpleMailDTO>>> getRecipientMails(@RequestHeader String username) {

        List<Mail> recipientMails = mailService.getUndeletedMailsByRecipient(username);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (Mail mail : recipientMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTO.setId(mail.getId());
            simpleMailDTO.setRecipients(mail.getRecipients());
            simpleMailDTO.setContent(mail.getContent());
            simpleMailDTO.setTime(mail.getTime());
            simpleMailDTOs.add(simpleMailDTO);
        }
        ApiResponse<List<SimpleMailDTO>> response = new ApiResponse<>("success", "Received Mails Retrieved", simpleMailDTOs);
        return ResponseEntity.ok(response);
    }

    //sent-box get the specific emails sent by the specific users
    @GetMapping("/get-send-emails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<SimpleMailDTO>>> getMailsBySender(@RequestHeader String username)
    {
        List<Mail> sendedMails=mailService.getMailsBySender(username);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (Mail mail : sendedMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setRecipients(mail.getRecipients());
            simpleMailDTO.setId(mail.getId());
            simpleMailDTO.setTime(mail.getTime());
            simpleMailDTO.setContent(mail.getContent());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTOs.add(simpleMailDTO);
        }
        ApiResponse<List<SimpleMailDTO>> response = new ApiResponse<>("success", "Sent Mails Retrieved", simpleMailDTOs);
        return ResponseEntity.ok(response);

    }

    //update the mail by id
    @PutMapping("/update-mail/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Mail>> updateMailById(@PathVariable Long id,@RequestBody Mail updatedMail,@RequestHeader String username)
    {
        updatedMail.setSender(username);
        Mail updateMail=mailService.updateMailById(updatedMail, id);
        ApiResponse<Mail> response = new ApiResponse<>("success", "Mail Updated Successfully", updateMail);
        return ResponseEntity.ok(response);
    }


//    @PostMapping("/authenticate")
//    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest, HttpServletResponse response) throws Exception
//    {
//
//        try {
//           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                   jwtRequest.getUsername(),
//                   jwtRequest.getPassword())
//            );
//
//            final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
//            String token = null;
//            if (userDetails.getUsername().equals (jwtRequest.getUsername())) {
//                token = jwtUtility.generateToken(userDetails);
//            }
//
//            Cookie cookie = new Cookie("BearerToken", token);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(true); // For HTTPS only
//            cookie.setPath("/"); // Set the cookie path
//            cookie.setMaxAge(1800000);
//            cookie.setDomain("");
//
//            // Add the cookie to the response
//            response.addCookie(cookie);
//
//            return new JWTResponse(token);
//
//        }
//        catch (BadCredentialsException e)
//        {
//            throw new Exception("INVALID_CREDENTIALS",e);
//        }
//
//
//    }


    //delete the mail by using their id and received mails for an users and received users only delted the mail
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> deleteMailById(@PathVariable Long id,@RequestHeader String username) {
        try {
            mailService.deleteMail(id, username);
            ApiResponse<String> response = new ApiResponse<>("success", "Email Deleted Successfully", null);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            ApiResponse<String> response = new ApiResponse<>("error", "Email Not Found", null);
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>("error", "Unauthorized to Delete Email", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);        }
    }

    @GetMapping("/deleted-mails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<SimpleMailDTO>>> getDeletedMailsForUser(@RequestHeader String username) {

        List<DeletedMail> deletedMails = mailService.getDeletedMailsForUser(username);
        List<SimpleMailDTO> simpleMailDTOs = new ArrayList<>();
        for (DeletedMail mail : deletedMails) {
            SimpleMailDTO simpleMailDTO = new SimpleMailDTO();
            simpleMailDTO.setSender(mail.getSender());
            simpleMailDTO.setSubject(mail.getSubject());
            simpleMailDTO.setId(mail.getId());
            simpleMailDTO.setTime(mail.getDeletedAt());
            simpleMailDTOs.add(simpleMailDTO);
        }
        ApiResponse<List<SimpleMailDTO>> response = new ApiResponse<>("success", "Deleted Mails Retrieved", simpleMailDTOs);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/users")
//    public ResponseEntity<ApiResponse<?>> saveUser(@RequestBody User user)
//    {
//
//        User savedUser=userService.saveUser(user);
//        ApiResponse<String> response = new ApiResponse<>("success", "User Created Successfully", "User Created Successfully as " + user.getEmail());
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/reply/{id}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> replyToMail(@PathVariable Long id, @RequestBody Mail replyMail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = authentication.getName();

        Optional<Mail> originalMailOptional = mailService.getMailById(id);

        if (originalMailOptional.isPresent()) {
            Mail originalMail = originalMailOptional.get();

            replyMail.setSender(senderEmail);
            replyMail.setTime(LocalDateTime.now());

            List<String> recipients = new ArrayList<>();
            recipients.add(originalMail.getSender());
            recipients.addAll(replyMail.getRecipients());
            replyMail.setRecipients(recipients);

            replyMail.setSubject("Re: " + originalMail.getSubject());


            Mail savedMail = mailService.saveMail(replyMail);

            ApiResponse<String> response = new ApiResponse<>("success", "Email Replied Successfully", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>("error", "Original Email Not Found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    // Forward an email by creating a new email
    @PostMapping("/forward/{id}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> forwardMail(@PathVariable Long id, @RequestBody Mail forwardMail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = authentication.getName();

        Optional<Mail> originalMailOptional = mailService.getMailById(id);

        if (originalMailOptional.isPresent()) {
            Mail originalMail = originalMailOptional.get();

            forwardMail.setSender(senderEmail);
            forwardMail.setTime(LocalDateTime.now());


            Mail savedMail = mailService.saveMail(forwardMail);

            ApiResponse<String> response = new ApiResponse<>("success", "Email Forwarded Successfully", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>("error", "Original Email Not Found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Clear the trash folder by permanently deleting all emails
    @DeleteMapping("/clear-trash")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> clearTrash(@RequestHeader String username) {

        try {
            mailService.clearTrash(username);
            ApiResponse<String> response = new ApiResponse<>("success", "Trash Cleared Successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>("error", "Failed to Clear Trash", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @DeleteMapping("/trash/{id}")
    public void deleteDeletedMailById(@PathVariable Long id) {
        mailService.deleteDeletedMailById(id);
    }


//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//
//        Cookie cookie = new Cookie("BearerToken",null);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        return ResponseEntity.ok().build();
//    }

}
