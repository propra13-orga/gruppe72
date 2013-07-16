package fart.dungeoncrawler.network;

import java.io.Serializable;

/**
 * ClientInfo stores all data that is important for the other clients. A list with one ClientInfo per client
 * is sent to every joining player and permanently updated.
 * @author Felix
 *
 */
public class ClientInfo implements Serializable {
	private static final long serialVersionUID = -1394412798338935906L;
	
	public String name;
	public byte ID;
	public boolean ready;
	
	/**
	 * Creates a ClientInfo from a given ServerClient.
	 * @param client serverClient
	 */
	public ClientInfo(ServerClient client)  {
		name = client.getClientname();
		ID = client.getID();
		ready = client.isReady();
	}
	
	/**
	 * Creates a ClientInfo from a given Client.
	 * @param client client
	 */
	public ClientInfo(Client client)  {
		name = client.getClientname();
		ID = client.getID();
		ready = client.isReady();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ClientInfo) {
			ClientInfo c = (ClientInfo)o;
			if(c.ID == this.ID)
				return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return ID + 7 * 17;
	}
}
