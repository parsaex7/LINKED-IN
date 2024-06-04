package Server.controllers;

import Server.DAO.PrivateMessageDAO;
import Server.Exceptions.PrivateChatNotExsitException;
import Server.models.Message;
import Server.models.PrivateMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class PrivateMessageController {
    private static PrivateMessageDAO privateMessageDAO;

    public PrivateMessageController() {
        privateMessageDAO = new PrivateMessageDAO();
    }

    public void sendMessage(String senderEmail, String receiverEmail, String message) throws SQLException {
        PrivateMessage privateMessage = new PrivateMessage(senderEmail, receiverEmail, message);
        privateMessageDAO.sendPrivateMessage(privateMessage);
    }

    public void deleteMessage(String senderEmail, String receiverEmail) throws SQLException, PrivateChatNotExsitException {
        PrivateMessage privateMessage = new PrivateMessage(senderEmail, receiverEmail, "");
        privateMessageDAO.deletePrivateMessage(privateMessage);
    }

    public void editPrivateMessage(String sender_email, String receiver_email, int message_id, String message_text) throws SQLException, PrivateChatNotExsitException {
        PrivateMessage privateMessage = new PrivateMessage(sender_email, receiver_email, message_text);
        privateMessage.setMessage_id(message_id);
        privateMessageDAO.editPrivateMessage(privateMessage);
    }

    public String getMessagesByTwoEmail(String senderEmail, String receiverEmail) throws SQLException, PrivateChatNotExsitException, JsonProcessingException {
        ArrayList<Message> messages = privateMessageDAO.getPrivateMessagesByTwoEmail(senderEmail, receiverEmail);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(messages);
    }

    public String getMessagesBySessionId(int sessionId) throws SQLException, JsonProcessingException, PrivateChatNotExsitException {
        ArrayList<Message> messages = privateMessageDAO.getPrivateMessagesByChatId(sessionId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(messages);
    }
}
