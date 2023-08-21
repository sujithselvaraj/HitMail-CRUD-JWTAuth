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
//@OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, orphanRemoval = true)
//private List<MailRecipient> recipients = new ArrayList<>();
    @Column(length = 1000)
    private String subject;
    @Column(length = 10000)
    private String content;
    private boolean deleted = false;
    @Column(name = "deleted_for_recipient", nullable = false, columnDefinition = "boolean default false")
    private boolean deletedForRecipient = false;

    private LocalDateTime time;

    @ElementCollection
    @MapKeyColumn(name = "recipient_email")
    @Column(name = "is_deleted")
    private Map<String, Boolean> recipientDeletionStatus = new HashMap<>();
//    @ElementCollection
//    private Set<String> deletedRecipients = new HashSet<>();






}
