package projects.dm.nodes.messages;

import sinalgo.nodes.messages.Message;

public class DMMessage extends Message {

	private static int msgCounter = 0;
	private int msgId;
	public Type type;
	public int tag;
	
	public enum Type {
		UN, DEUX;
	}
	
	public DMMessage(Type t, int tag) {
		super();
		msgId = msgCounter;
		msgCounter++;
		type = t;
		this.tag = tag;
	}

	public Message clone() {
		return this;
	}
	
	public String toString() {
		return "dm " + msgId;
	}
}