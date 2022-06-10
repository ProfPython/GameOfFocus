
/****
 * 
 * 
 * 
 * Handles saving of a game session into a file which 
 * can be loaded to resume a game.
 * 
 * 
 */



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileManager {
	private static final String FILE_NAME = "GameSessions.ser";

	private FileManager() {
		throw new IllegalStateException("Utility class");
	}

	public static void save(ArrayList<GameSession> content) throws IOException {
		try {
			FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(content);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<GameSession> load() throws IOException, ClassNotFoundException {
		ArrayList<GameSession> content = null;
		try {
			FileInputStream fileIn = new FileInputStream(FILE_NAME);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			content = (ArrayList<GameSession>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			// When the classes change we might get a serializable issue so to slove that we
			// remove everything
			// Note this type of an issue will only occure in development due to the changes
			// in the classes
			content = new ArrayList<GameSession>();
			save(content);
			return content;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		return content;
	}

}