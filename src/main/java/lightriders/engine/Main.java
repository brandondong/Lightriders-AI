package lightriders.engine;

import java.util.Scanner;

import lightriders.ai.IBot;

public class Main {

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			EngineInputHandler inputHandler = new EngineInputHandler(IBot.newCompetitionBot());
			while (s.hasNextLine()) {
				String line = s.nextLine();
				if (!line.isEmpty()) {
					String[] parts = line.split(" ");
					switch (parts[0]) {
					case "settings":
						inputHandler.settings(parts[1], parts[2]);
						break;
					case "update":
						inputHandler.update(parts[2], parts[3]);
						break;
					case "action":
						System.out.println(inputHandler.action(parts[2]));
						break;
					}
				}
			}
		}
	}

}
