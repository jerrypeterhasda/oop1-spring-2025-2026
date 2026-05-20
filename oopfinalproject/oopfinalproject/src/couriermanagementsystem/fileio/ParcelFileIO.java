package couriermanagementsystem.fileio;
 
import couriermanagementsystem.entity.Parcel;
 
import java.io.*;
 

public class ParcelFileIO 
{
    private static final String FILE_NAME = "parcels.txt";
     private static final String TEMP_FILE = "parcels_temp.txt";
 
    public static void createFileIfNotExists() throws IOException {
        File file = new File(FILE_NAME);
 
        if (!file.exists())
            file.createNewFile();
    }
    public static boolean idExists(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                Parcel p = Parcel.fromLine(line); 
 
                if (p != null && p.getId().equals(id))
                    return true;
            }
        } catch (IOException ignored) {
        }
        return false; 
    }
    public static int countRecords() {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
 
            while ((line = br.readLine()) != null) {
                if (Parcel.fromLine(line) != null)
                    count++;
            }
        } catch (IOException ignored) {
            
        }
        return count;
    }
 
    
    public static void addParcel(Parcel p) throws IOException {
        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            pw.println(p.toLine());
        }
    }
 
    public static boolean updateParcel(Parcel p) throws IOException {
        File inputFile = new File(FILE_NAME); 
        File tempFile  = new File(TEMP_FILE); 
        boolean found  = false;               
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
 
            String line;
            while ((line = br.readLine()) != null) {
                Parcel existing = Parcel.fromLine(line); 
 
                if (existing != null && existing.getId().equals(p.getId())) {
                    bw.write(p.toLine()); 
                    found = true;         
                } else {
                    bw.write(line);      
                }
                bw.newLine();
            }
        }
        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize update."); 
            }
        } else {
            tempFile.delete();
        }
        return found; 
    }
    public static boolean deleteParcel(String id) throws IOException {
        File inputFile = new File(FILE_NAME); 
        File tempFile  = new File(TEMP_FILE); 
        boolean found  = false;               
 
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
 
            String line;
            while ((line = br.readLine()) != null) {
                Parcel existing = Parcel.fromLine(line); 
 
                if (existing != null && existing.getId().equals(id)) {
                    found = true;
                    continue; 
                }
 
                bw.write(line);
                bw.newLine(); 
            }
        }
        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize delete."); 
            }
        } else {
            tempFile.delete();
        }
        return found; 
    }
 
    public static Object[][] getAllParcels() {
        int total       = countRecords();       
        Object[][] rows = new Object[total][6]; 
        int idx         = 0;           
 
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
 
            while ((line = br.readLine()) != null && idx < total) {
                Parcel p = Parcel.fromLine(line); 
 
                if (p != null) {             
                    Object[] row = p.toRow(); 
                    rows[idx][0]= row[0];   
                    rows[idx][1]=row[1];
                    rows[idx][2]=row[2];
                    rows[idx][3]=row[3];
                    rows[idx][4] =row[4];    
                    rows[idx][5]= row[5];
                    idx++;                   
                }
            }
        } catch (IOException ignored) {
        }
        return rows; 
    }
 
    public static Object[][] searchParcels(String keyword) {
        String kw = keyword.toLowerCase();
 
        int matchCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Parcel p = Parcel.fromLine(line); 
 
                if (p != null && (p.getId().toLowerCase().contains(kw)
                        || p.getSender().toLowerCase().contains(kw))) {
                    matchCount++; 
                }
            }
        } catch (IOException ignored) {
        }
 
        Object[][] results = new Object[matchCount][6]; 
        int idx            = 0;                         
 
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
 
            while ((line = br.readLine()) != null && idx < matchCount) {
                Parcel p = Parcel.fromLine(line); 
                
                if (p != null && (p.getId().toLowerCase().contains(kw)
                        || p.getSender().toLowerCase().contains(kw))) {
                    Object[] row = p.toRow(); 
                    results[idx][0]= row[0];    
                    results[idx][1]=row[1];    
                    results[idx][2]=row[2];    
                    results[idx][3]= row[3];    
                    results[idx][4]=row[4];   
                    results[idx][5] =row[5];    
                    idx++;                    
                }
            }
        } catch (IOException ignored) {
        }
        return results; 
    }
}
