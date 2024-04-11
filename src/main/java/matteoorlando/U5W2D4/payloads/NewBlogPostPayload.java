package matteoorlando.U5W2D4.payloads;

import lombok.Getter;

@Getter
public class NewBlogPostPayload {
    private int authorId;
    private String category;
    private String content;
    private double readingTime;
    private String title;
}
