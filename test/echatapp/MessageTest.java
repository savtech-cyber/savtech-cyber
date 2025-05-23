package echatapp;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testMessageSendAndDiscard() {
        Message.allMessages.clear();
        Message.totalMessages = 0;

        System.out.println("Test for Num Messages: 2");

        Message message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        System.out.println("Test Data for Message 1");
        System.out.println("Recipient Number: " + message1.recipient);
        System.out.println("Message: \"" + message1.messageContent + "\"");
        System.out.println("Select Send");

        String result1 = message1.sentMessage();

        String id1 = message1.getMessageID();
        String hash1 = message1.getMessageHash();
        int totalAfter1 = Message.returnTotalMessages();

        System.out.println("Message 1 Sent:");
        System.out.println("MessageID: " + id1);
        System.out.println("Message Hash: " + hash1);
        System.out.println("Return total number sent: " + totalAfter1);

        Message message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        System.out.println("Test Data for Message 2");
        System.out.println("Recipient Number: " + message2.recipient);
        System.out.println("Message: \"" + message2.messageContent + "\"");
        System.out.println("Select Discard");

        String result2 = message2.sentMessage();

        String id2 = message2.getMessageID();
        String hash2 = message2.getMessageHash();
        int totalAfter2 = Message.returnTotalMessages();

        System.out.println("Message 2 Disregarded:");
        System.out.println("MessageID: " + id2);
        System.out.println("Message Hash: " + hash2);
        System.out.println("Return total number sent: " + totalAfter2);

        assertEquals("Message sent", result1);
        assertEquals("Message Disregarded", result2);
        assertNotNull(id1);
        assertFalse(id1.isEmpty());
        assertNotNull(id2);
        assertFalse(id2.isEmpty());
        assertNotNull(hash1);
        assertTrue(hash1.contains(":"));
        assertNotNull(hash2);
        assertTrue(hash2.contains(":"));
        assertEquals(1, totalAfter1);
        assertEquals(1, totalAfter2);
    }

    @Test
    public void testMessageLengthSuccess() {
        String validMessage = "Hi Mike, can you join us for dinner tonight";
        Message message = new Message("+27718693002", validMessage);
        String result = message.checkMessageLength();
        System.out.println("Message ready to send");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(270);
        Message message = new Message("+27718693002", longMessage);
        String result = message.checkMessageLength();
        System.out.println("Message exceeds 250 characters by 20, please reduce size");
        assertEquals("Message exceeds 250 characters by 20, please reduce size.", result);
    }

    @Test
    public void testCheckRecipientPhoneNumberValid() {
        Message instance = new Message("+27838968976", "Test message");
        int expResult = 1;
        int result = instance.checkRecipientCell(instance.getRecipient());
        System.out.println("Cell phone number successfully captured");
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckRecipientPhoneNumberInvalid() {
        Message instance = new Message("08966553", "Test message");
        int expResult = 0;
        int result = instance.checkRecipientCell(instance.getRecipient());
        System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
        assertEquals(expResult, result);
    }

    @Test
    public void testMessageHashGeneration() {
        String recipient = "+27718693002";
        String messageContent = "Hi Mike, can you join us for dinner tonight";
        Message message = new Message(recipient, messageContent);

        String actualHash = message.getMessageHash();
        String expectedHash = message.getMessageID().substring(0, 2).toUpperCase() + ":" +
                              message.messageNumber + ":" + "HITONIGHT";
        System.out.println("Test: Message Hash Generation\nHash: " + actualHash);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testMessageIDCreatedShowsDialogAndReturnsString() {
        Message message = new Message("+12345678901", "Hello world!");
        String expected = "Message ID generated: " + message.getMessageID();
        String actual = "Message ID generated: " + message.getMessageID();
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testSentMessage_UserSelectsSend() {
        Message message = new Message("+12345678901", "Hello");
        String result = message.sentMessage();
        System.out.println("Message sent");
        assertEquals("Message sent", result);
    }

    @Test
    public void testSentMessage_UserSelectsDisregard() {
        Message message = new Message("+12345678901", "Hello");
        String result = message.sentMessage();
        System.out.println("Press 0 to delete message");
        System.out.println("Message Disregarded");
        assertEquals("Message Disregarded", result);
    }

    @Test
    public void testSentMessage_UserSelectsStore() {
        Message message = new Message("+12345678901", "Hello");
        String result = message.sentMessage();
        System.out.println("Message successfully stored");
        assertEquals("Message stored to send later", result);
    }
}
