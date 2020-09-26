package com.example.demo;

import java.time.Instant;

public class Message {

	private String message; // Hold a message

	private long created = Instant.now().getEpochSecond(); // Something instance specific

    /**
     * Allow creation from a message.
     * @param message
     */
    public Message(String message) {
        this.message = message;
    }

    public Message(String message, long created) {
        this.message = message;
        this.created = created;
    }
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Message{message=" + this.getMessage() + ", created='" + this.getCreated() + "'}";
	}
}
