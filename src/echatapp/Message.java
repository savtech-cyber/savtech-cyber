
package echatapp;
//the imports used 
import java.io.File;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.simple.parser.JSONParser;

public class Message {
 // string Variables
 boolean sent = false;
 private String messageID;
 int messageNumber;
 public  String recipient;
 public String messageContent;
 private String messageHash;
 static int totalMessages = 0;
 //Array list created
 static ArrayList<Message> allMessages = new ArrayList<>();
 // Contructor used to call out variables
 public Message(String recipient, String messageContent) {
 this.messageID = generateRandomID();
 this.messageNumber = ++totalMessages;
 this.recipient = recipient;
 this.messageContent = messageContent;
 this.messageHash = createMessageHash();
 allMessages.add(this);
 }
 
 public Message(){
     
 }
 
 
//Method to acquire a random ID for messages
 private String generateRandomID() {
 Random random = new Random();
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < 10; i++) {
 sb.append(random.nextInt(10));
 }
 return sb.toString();
 }
//method  that checks the that checks the id is not more then  10 digits 
 public boolean checkMessageID() {
 return messageID.length() <= 10;
 }


 
 public String checkMessageLength() {
    int length = messageContent.length();
    if (length <= 250) {
        return "Message ready to send.";
    } else {
        return "Message exceeds 250 characters by " + (length - 250) + ", please reduce size.";
    }
}

//Check  the Receipents cell phone  number  that is has an International code 
 public int checkRecipientCell() {
 if (recipient.length() ==12 && recipient.startsWith("+")) {
 return 1; // Valid
 }
 return 0; // Invalid
 }
 //Check  the Receipents cell phone  number  that is has an International code 
 public int checkRecipientCell(String recipient) {
 if (recipient.length() == 12 && recipient.startsWith("+")) {
 return 1; // Valid
 }
 return 0; // Invalid
 }
//Method for creating a Message Hash
 public String createMessageHash() {
 String[] words = messageContent.split("\\s+");
 if (words.length == 0) {
 return "";
 }

 String firstWord = words[0];
 String lastWord = words[words.length - 1];

 return (messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
 }
 //Method  for to send Message
 public String sentMessage() {
     // this dsplays the options using joptionpane to sent message diregard and store message to send later
     
 String[] options = {"Send Message", "Disregard Message", "Store Message to send later",};
 int choice = JOptionPane.showOptionDialog(
 null,
 "Choose an option for this message:",
 "Message Options",
 JOptionPane.DEFAULT_OPTION,
 JOptionPane.QUESTION_MESSAGE,
 null,
 options,
 options[0]
 );

 switch (choice) {
 case 0:
     sent = true;
     storeMessage();
 return "Message sent";
 case 1:
 allMessages.remove(this);
 totalMessages--;
 storeMessage();
 return "Message Disregarded";
 case 2:
     storeMessage();
 return "Message stored to send later";
 case 3:
 storeMessage();
 return "Message stored in JSON file";
 default:
 return "No action taken";
 }
 }
//method used To show the message with the messageID message hash recipient and the message
 public static String printMessages() {
    StringBuilder result = new StringBuilder();
    for (Message message : allMessages) {
        if(message.sent){
        String messageDetails = "MessageID: " + message.messageID
            + "\nMessage Hash: " + message.messageHash
            + "\nRecipient: " + message.recipient
            + "\nMessage: " + message.messageContent;
        
        // Append to the result (for return)
        result.append(messageDetails).append("\n");

        // Show each message in a dialog box
        JOptionPane.showMessageDialog(null, messageDetails);
        }
    }
    return result.toString();
}

//method to  reutrn the number of messages sent 
 public static int returnTotalMessages() {
 return totalMessages;
 }
//method to store the message in a json file
 @SuppressWarnings("unchecked")
 public void storeMessage() {
    JSONObject messageDetails = new JSONObject();
    messageDetails.put("messageID", messageID);
    messageDetails.put("messageNumber", messageNumber);
    messageDetails.put("recipient", recipient);
    messageDetails.put("messageContent", messageContent);
    messageDetails.put("messageHash", messageHash);

    JSONArray messageList = new JSONArray();

    // Load existing messages from file 
    try {
        File file = new File("messages.json");
        if (file.exists()) {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);
            Object obj = parser.parse(reader);
            messageList = (JSONArray) obj;
            reader.close();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error reading existing messages: " + e.getMessage());
    }

    // Add the new message
    messageList.add(messageDetails);

    // Write updated list to the file
    try (FileWriter fileWriter = new FileWriter("messages.json")) {
        fileWriter.write(messageList.toJSONString());
        fileWriter.flush();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
    }
} 
 
/***************************************************************************************
*    Title: Java  Json File
*    Author: ChatGPT (OpenAI)
*    Date: 20 April 2025
*    Code version: GPT-4
*    Availability: https://chatgpt.com/
*    Description: Assisting in the method on how to store  messages in json file
***************************************************************************************/


 public String getMessageID() {
 return messageID;
 }

 public String getMessageHash() {
 return messageHash;
 }

 public String getRecipient() {
 return recipient;
 }

 public String getMessageContent() {
 return messageContent;
 }
}
