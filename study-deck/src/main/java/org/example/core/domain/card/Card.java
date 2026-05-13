package org.example.core.domain.card;

import java.time.Instant;

public class Card {
    private Long id;
    private Long ownerId;
    private Long subCategoryId;
    private String displayName;
    private String question;
    private Long bestAnswer;

    private Instant createdAt;
    private Instant updatedAt;
    private Long createdUser;
    private Long updatedUser;
}
