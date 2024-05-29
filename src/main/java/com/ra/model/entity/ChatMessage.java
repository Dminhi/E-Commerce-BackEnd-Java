package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_sender_id", referencedColumnName = "id")
    private User userSender;
    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;
    private String content;
    private LocalDate timeSender;
}
