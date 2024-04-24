package main;

import game.GameController;

public class Main {

	public static void main(String[] args) {
		// GameController erzeuge (als Singleton) und diesen aufrufen 
		GameController.getInstance().launch(); 
	}

}
