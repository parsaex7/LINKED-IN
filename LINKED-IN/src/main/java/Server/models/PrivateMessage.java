package Server.models;

import java.sql.Timestamp;

public class PrivateMessage {
    private int session_id;
    private int message_id;
    private String sender_email;
    private String receiver_email;
    private String message_text;
    private Timestamp timestamp;

    public PrivateMessage(int session_id, int message_id, String sender_email, String receiver_email, String message_text) {
        this.session_id = session_id;
        this.message_id = message_id;
        this.sender_email = sender_email;
        this.receiver_email = receiver_email;
        this.message_text = message_text;
    }

    public PrivateMessage(int session_id, String sender_email, String receiver_email, String message_text) {
        this.session_id = session_id;
        this.sender_email = sender_email;
        this.receiver_email = receiver_email;
        this.message_text = message_text;
    }

    public PrivateMessage(String sender_email, String receiver_email, String message_text) {
        this.sender_email = sender_email;
        this.receiver_email = receiver_email;
        this.message_text = message_text;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getReceiver_email() {
        return receiver_email;
    }

    public void setReceiver_email(String receiver_email) {
        this.receiver_email = receiver_email;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
