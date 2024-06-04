package Server.DAO;

import Server.Exceptions.PrivateChatNotExsitException;
import Server.models.Message;
import Server.models.PrivateMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrivateMessageDAO {
    private Connection connection;

    public PrivateMessageDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void sendPrivateMessage(PrivateMessage privateMessage) throws SQLException {
        String sender_email = privateMessage.getSender_email();
        String receiver_email = privateMessage.getReceiver_email();

        PreparedStatement statement = connection.prepareStatement("SELECT session_id FROM private_message WHERE sender_email = ? AND receiver_email = ? OR sender_email = ? AND receiver_email = ?");

        statement.setString(1, sender_email);
        statement.setString(2, receiver_email);
        statement.setString(3, receiver_email);
        statement.setString(4, sender_email);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int session_id = resultSet.getInt("session_id");
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO private_message (session_id, sender_email, receiver_email, message_text) VALUES (?, ?, ?, ?)");
            insertStatement.setInt(1, session_id);
            insertStatement.setString(2, sender_email);
            insertStatement.setString(3, receiver_email);
            insertStatement.setString(4, privateMessage.getMessage_text());
            insertStatement.executeUpdate();
        } else {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO private_message (sender_email, receiver_email, message_text) VALUES (?, ?, ?)");
            insertStatement.setString(1, sender_email);
            insertStatement.setString(2, receiver_email);
            insertStatement.setString(3, privateMessage.getMessage_text());
            insertStatement.executeUpdate();
        }
    }

    public void deletePrivateMessage(PrivateMessage privateMessage) throws SQLException, PrivateChatNotExsitException {
        String sender_email = privateMessage.getSender_email();
        String receiver_email = privateMessage.getReceiver_email();

        PreparedStatement statement = connection.prepareStatement("SELECT session_id FROM private_message WHERE sender_email = ? AND receiver_email = ? OR sender_email = ? AND receiver_email = ?");

        statement.setString(1, sender_email);
        statement.setString(2, receiver_email);
        statement.setString(3, receiver_email);
        statement.setString(4, sender_email);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int session_id = resultSet.getInt("session_id");
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM private_message WHERE session_id = ? AND message_id = ?");
            deleteStatement.setInt(1, session_id);
            deleteStatement.setInt(2, privateMessage.getMessage_id());
            deleteStatement.executeUpdate();
        } else {
            throw new PrivateChatNotExsitException();
        }
    }

    public void editPrivateMessage(PrivateMessage privateMessage) throws SQLException, PrivateChatNotExsitException {
        String sender_email = privateMessage.getSender_email();
        String receiver_email = privateMessage.getReceiver_email();

        PreparedStatement statement = connection.prepareStatement("SELECT session_id FROM private_message WHERE sender_email = ? AND receiver_email = ? OR sender_email = ? AND receiver_email = ?");

        statement.setString(1, sender_email);
        statement.setString(2, receiver_email);
        statement.setString(3, receiver_email);
        statement.setString(4, sender_email);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int session_id = resultSet.getInt("session_id");
            PreparedStatement editStatement = connection.prepareStatement("UPDATE private_message SET message_text = ? WHERE session_id = ? AND message_id = ?");
            editStatement.setString(1, privateMessage.getMessage_text());
            editStatement.setInt(2, session_id);
            editStatement.setInt(3, privateMessage.getMessage_id());
            editStatement.executeUpdate();
        } else {
            throw new PrivateChatNotExsitException();
        }
    }

    public ArrayList<Message> getPrivateMessagesByTwoEmail(String sender_email, String receiver_email) throws SQLException, PrivateChatNotExsitException {
        PreparedStatement statement = connection.prepareStatement("SELECT sender_email, message_text FROM private_message WHERE sender_email = ? AND receiver_email = ? OR sender_email = ? AND receiver_email = ?");
        statement.setString(1, sender_email);
        statement.setString(2, receiver_email);
        statement.setString(3, receiver_email);
        statement.setString(4, sender_email);

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();
        while (resultSet.next()) {
            String message_text = resultSet.getString("message_text");
            String sender_email1 = resultSet.getString("sender_email");
            messages.add(new Message(sender_email1, message_text));
        }
        if (messages.isEmpty()) {
            throw new PrivateChatNotExsitException();
        }
        return messages;
    }

    public ArrayList<Message> getPrivateMessagesByChatId(int session_id) throws SQLException, PrivateChatNotExsitException {
        PreparedStatement statement = connection.prepareStatement("SELECT sender_email, message_text FROM private_message WHERE session_id = ?");
        statement.setInt(1, session_id);

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();
        while (resultSet.next()) {
            String message_text = resultSet.getString("message_text");
            String sender_email1 = resultSet.getString("sender_email");
            messages.add(new Message(sender_email1, message_text));
        }
        if (messages.isEmpty()) {
            throw new PrivateChatNotExsitException();
        }
        return messages;
    }
}
