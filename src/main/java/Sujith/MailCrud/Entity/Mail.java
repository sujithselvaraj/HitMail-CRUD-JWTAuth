package Sujith.MailCrud.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail")
public class Mail
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    @ElementCollection
    private List<String> recipients;
    private String subject;
    private String content;
    private boolean deleted = false;
    @Column(name = "deleted_for_recipient", nullable = false, columnDefinition = "boolean default false")
    private boolean deletedForRecipient = false;

    private LocalDateTime time;


//    @ElementCollection
//    private Set<String> deletedRecipients = new HashSet<>();






}
