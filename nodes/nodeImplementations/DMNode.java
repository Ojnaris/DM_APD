package projects.dm.nodes.nodeImplementations;

import java.awt.Color;
import java.util.Random;

import projects.dm.nodes.messages.DMMessage;
import projects.dm.nodes.messages.DMMessage.Type;
import projects.dm.nodes.timers.InitTimer;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.tools.storage.ReusableListIterator;

public class DMNode extends sinalgo.nodes.Node {
	
	private boolean Output;
	private int V1;
	private int V2;
	private int Tag;
	private State etat;
	
	public enum State {
		ACTIF, PASSIF;
	}

	/* dmNode() { 
	 *   // no constructor code, it breaks the way sinalgo builds the nodes. 
	 *   // instead use the init() method 
	 * }
	 * */
	public void init() {
		setColor(Color.GREEN);
		(new InitTimer(this)).startRelative(InitTimer.timerRefresh, this);
		this.Output = false;
		this.etat = State.ACTIF;
		this.Tag = ID;
	}
	
	public void initiate() {
		DMMessage DM = new DMMessage(Type.UN, Tag);
		System.out.println(this + " is sending now message " + DM);
		Node node = nextNode(outgoingConnections);
		send(DM, node);
	}
		
	public String toString() {
		return " " + ID + " "; 
	}

	public void handleMessages(sinalgo.nodes.messages.Inbox inbox) {
		while(inbox.hasNext()) {
			sinalgo.nodes.messages.Message msg = inbox.next();
			if (msg instanceof DMMessage) {
				DMMessage dm = (DMMessage) msg;
				if(this.etat == State.PASSIF) {
					Node next = nextNode(outgoingConnections);		
					send(dm, next);
					System.out.println(this + " received message " + dm + 
									   " and sends it now to " + next);
				}
				else {
					switch(dm.type) {
					case UN :
						Pour_Un(dm);
						break;
					case DEUX :
						Pour_Deux(dm);
						break;
					}
				}
			}
		}
	}
	
	private static Random random = new Random();
	Node randomWalkChoice(sinalgo.nodes.Connections neighbors) {
		int degree = neighbors.size();
		System.out.println("degree = " + degree);
		if (degree == 0) throw new RuntimeException("no neighbor");
		int positionOfNext = random.nextInt(degree);
		sinalgo.tools.storage.ReusableListIterator<sinalgo.nodes.edges.Edge> iter 
			= neighbors.iterator();
		Node node = iter.next().endNode;
		for (int i = 1; i <= positionOfNext; i++) node = iter.next().endNode;
		return node;
	}
	
	Node nextNode(sinalgo.nodes.Connections neighbors) {
		int degree = neighbors.size();
		System.out.println("degree = " + degree);
		sinalgo.tools.storage.ReusableListIterator<Edge> iter = neighbors.iterator();
		Node node = iter.next().endNode;
		System.out.println("neighbor : " + node);
		return node;
	}

	
	public void preStep() {};
	public void neighborhoodChange() {};
	public void postStep() {}; 
	public void checkRequirements() throws sinalgo.configuration.WrongConfigurationException {};
	public void draw(java.awt.Graphics g, sinalgo.gui.transformation.PositionTransformation pt, 
					 boolean highlight) {
		// draw the node as a circle with the text inside
		super.drawNodeAsDiskWithText(g, pt, highlight, toString(), 20, Color.black);
	}
	public void Pour_Un(DMMessage m) {
		if(m.tag == Tag) {
			this.Output = true;
			setColor(Color.BLUE);
		} else {
			this.V1 = m.tag;
			Node next = randomWalkChoice(outgoingConnections);
			send(new DMMessage(Type.DEUX, m.tag), next);
			System.out.println(this + " is sending now message of type DEUX avec tag " + this.Tag);
		}
	}
	public void Pour_Deux(DMMessage m) {
		this.V2 = m.tag;
		if(this.V1 < this.V2 && this.V1 < this.Tag) {
			this.Tag = this.V1;
			Node next = randomWalkChoice(outgoingConnections);
			send(new DMMessage(Type.UN, this.Tag), next);
			System.out.println(this + " is sending now message of type UN " + this.Tag);
		} else {
			this.etat = State.PASSIF;
		}
	}
	
}