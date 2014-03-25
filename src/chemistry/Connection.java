package chemistry;

public class Connection implements Cloneable{
	
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
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public String getTo(){
		return to;
	}
	
	public void setTo(String to){
		this.to = to;
	}
	
	public double getStrength(){
		return strength;
	}
	
	public void setStrength(double strength){
		this.strength = strength;
	}
	
	public String toString(){
		return "C f: "+from+" t: "+to+" s: "+strength;
	}
	
	public boolean equals(Object o){
		if(o.getClass() != Connection.class){
			return false;
		}
		
			Connection c = (Connection) o;
			
		return from == c.from && to == c.to;
	}
	
	//public 

}
