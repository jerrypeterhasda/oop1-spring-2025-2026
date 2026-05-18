// Declares that this class belongs to the "entity" package (folder)
package couriermanagementsystem.entity;

/**
 * Parcel - Data model representing a single parcel record.
 *
 * Each parcel has five pieces of information:
 * id          - a unique 8-digit numeric string that identifies the parcel
 * sender      - the full name of the person sending the parcel
 * receiver    - the full name of the person receiving the parcel
 * weight      - the weight of the parcel stored as a String (validated as a number)
 * destination - the delivery destination of the parcel
 *
 * This class also provides helper methods to convert a Parcel to/from
 * the CSV (comma-separated) format used in the data file (parcels.txt).
 */
public class Parcel {

    // --- Fields (private so only this class can access them directly) ---

    private String id;          // Unique 8-digit parcel ID, e.g. "20240001"
    private String sender;      // Full name of the sender, e.g. "Alice Smith"
    private String receiver;    // Full name of the receiver, e.g. "Bob Jones"
    private String weight;      // Weight of the parcel as text, e.g. "2.5"
    private String destination; // Delivery destination, e.g. "Dhaka"

    // --- Constructor ---

    /**
     * Creates a new Parcel with all five required fields.
     *
     * @param id          Unique 8-digit parcel ID.
     * @param sender      Full name of the sender.
     * @param receiver    Full name of the receiver.
     * @param weight      Weight of the parcel (numeric string).
     * @param destination Delivery destination name.
     */
    public Parcel(String id, String sender, String receiver, String weight, String destination) {
        this.id          = id;          // Assign the provided id to this object's id field
        this.sender      = sender;      // Assign the provided sender name
        this.receiver    = receiver;    // Assign the provided receiver name
        this.weight      = weight;      // Assign the provided weight
        this.destination = destination; // Assign the provided destination
    }

    // --- Getters (read-only access to private fields) ---

    /**
     * Returns the parcel's ID.
     *
     * @return 8-digit parcel ID string.
     */
    public String getId() {
        return id; // Return the id field value
    }

    /**
     * Returns the sender's name.
     *
     * @return Full name of the sender.
     */
    public String getSender() {
        return sender; // Return the sender field value
    }

    /**
     * Returns the receiver's name.
     *
     * @return Full name of the receiver.
     */
    public String getReceiver() {
        return receiver; // Return the receiver field value
    }

    /**
     * Returns the parcel's weight.
     *
     * @return Weight as a string (e.g. "2.5").
     */
    public String getWeight() {
        return weight; // Return the weight field value
    }

    /**
     * Returns the parcel's destination.
     *
     * @return Destination name string.
     */
    public String getDestination() {
        return destination; // Return the destination field value
    }

    // --- Setters (allow controlled modification of private fields) ---

    /**
     * Updates the parcel's ID.
     *
     * @param id New 8-digit ID string.
     */
    public void setId(String id) {
        this.id = id; // Replace the current id with the new one
    }

    /**
     * Updates the sender's name.
     *
     * @param sender New sender name string.
     */
    public void setSender(String sender) {
        this.sender = sender; // Replace the current sender with the new one
    }

    /**
     * Updates the receiver's name.
     *
     * @param receiver New receiver name string.
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver; // Replace the current receiver with the new one
    }

    /**
     * Updates the parcel's weight.
     *
     * @param weight New weight string.
     */
    public void setWeight(String weight) {
        this.weight = weight; // Replace the current weight with the new one
    }

    /**
     * Updates the parcel's destination.
     *
     * @param destination New destination name string.
     */
    public void setDestination(String destination) {
        this.destination = destination; // Replace the current destination with the new one
    }

    // --- File serialization helpers ---

    /**
     * Converts this Parcel into a single CSV (comma-separated) line
     * suitable for writing to the parcels.txt data file.
     *
     * Example output: "20240001,Alice Smith,Bob Jones,2.5,Dhaka"
     *
     * @return A CSV string with id, sender, receiver, weight, and destination
     *         separated by commas.
     */
    public String toLine() {
        // Concatenate all five fields with commas between them
        return id + "," + sender + "," + receiver + "," + weight + "," + destination;
    }

    /**
     * Parses a CSV line read from the data file and creates a Parcel object.
     *
     * This is a static factory method — it belongs to the class, not an instance,
     * so it can be called without first creating a Parcel.
     *
     * Example input: "20240001,Alice Smith,Bob Jones,2.5,Dhaka"
     *
     * @param line A CSV string from the data file (may be null or malformed).
     * @return A new Parcel if the line is valid; null otherwise.
     */
    public static Parcel fromLine(String line) {
        // Reject null lines (e.g. blank reads at end of file)
        if (line == null)
            return null;

        // Split the line on commas; limit=-1 keeps empty trailing fields
        String[] data = line.split(",", -1);

        // A valid line must have exactly 5 fields: id, sender, receiver, weight, destination
        if (data.length != 5)
            return null; // Malformed line — ignore it

        // Build and return a new Parcel from the parsed fields
        return new Parcel(data[0], data[1], data[2], data[3], data[4]);
    }

    /**
     * Converts this Parcel into an Object array row for display in a JTable.
     *
     * JTable rows are represented as Object arrays where each element
     * corresponds to a column: [ID, Sender, Receiver, Weight, Destination].
     *
     * @return Object array with five elements ready for table insertion.
     */
    public Object[] toRow() {
        // Return a new array containing all five fields in column order
        return new Object[] { id, sender, receiver, weight, destination };
    }
}
