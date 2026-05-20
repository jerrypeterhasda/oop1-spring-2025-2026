package couriermanagementsystem.entity;

public class Parcel {

    private String id;            
    private String sender;        
    private String receiver;      
    private String weight;        
    private String destination;   
    private String courierStatus;

    public Parcel(String id, String sender, String receiver, String weight,
                  String destination, String courierStatus){
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.destination= destination;
        this.courierStatus= courierStatus;
    }

 
    public String getId(){
        return id;
    }
    public String getSender(){
        return sender;
    }
    public String getReceiver(){
        return receiver;
    }
    public String getWeight(){
        return weight;
    }
    public String getDestination(){
        return destination;
    }

    public String getCourierStatus(){
        return courierStatus;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    public void setWeight(String weight){
        this.weight= weight;
    }

    public void setDestination(String destination){
        this.destination= destination;
    }

    public void setCourierStatus(String courierStatus) {
        this.courierStatus= courierStatus;
    }

    public String toLine(){
        return id + "," + sender + "," + receiver + "," + weight + ","
                + destination + "," + courierStatus;
    }


    public static Parcel fromLine(String line) {
        if (line == null)
            return null;

        String[] data = line.split(",", -1);

        if (data.length != 6)
            return null; 

        return new Parcel(data[0], data[1], data[2], data[3], data[4], data[5]);
    }

    public Object[] toRow() {
        return new Object[] { id, sender, receiver, weight, destination, courierStatus };
    }
}
