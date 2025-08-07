package com.khoihuynh.services;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import javax.swing.JTextArea;


public class Service {

   private static Service instance; 
   private  SocketIOServer server; 
   private JTextArea textArea; 
   private final int PORT_NUMBER = 9999;
   public static Service getInstance(JTextArea textArea) {
       if(instance == null) {
           instance = new Service(textArea); 
       } 
       
       return instance;
   } 
   
   private Service(JTextArea textArea) {
       this.textArea = textArea;
   } 
   
   
   public void startServer() {
       Configuration config = new Configuration(); 
       config.setPort(PORT_NUMBER);
       server = new SocketIOServer(config); 
       
       server.start();
       textArea.append("Server has start on port " + PORT_NUMBER + "\n");
   }
}
