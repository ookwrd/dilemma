package chemistry;

public class Connection {
	
	private String from;
	
	private String to;
	
	private double strength;
	
	public Connection(String from, String to, double strength){
		this.from = from;
		this.to = to;
		this.strength = strength;
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getTo(){
		return to;
	}
	
	public double getStrength(){
		return strength;
	}
	
	public boolean equals(Object o){
		if(o.getClass() != Connection.class){
			return false;
		}
		
			Connection c = (Connection) o;
			
		return from == c.from && to == c.to;
	}

}
