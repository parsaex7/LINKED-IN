package Server.models;

public class Message {
    private String sender_email;
    private String message_text;

    public Message(String sender_email, String message_text) {
        this.sender_email = sender_email;
        this.message_text = message_text;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
}
