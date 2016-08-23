package com.springboot.akka.model;

public class Message {

    final private String payload;
    final private long id;
    final private String senderToReceiver;

    public Message(String payload, long id, String senderToReceiver) {
        this.payload = payload;
        this.id = id;
        this.senderToReceiver = senderToReceiver;
    }

    public String getPayload() {
        return payload;
    }

    public long getId() {
        return id;
    }


    public String getSenderToReceiver() {
        return senderToReceiver;
    }

    @Override
    public String toString() {
        return "Message{" + "payload='" + payload + '\'' +
                ", id=" + id +
                ", senderToReceiver=" + senderToReceiver + '}';
    }
}
