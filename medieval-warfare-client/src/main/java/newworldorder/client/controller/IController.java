package newworldorder.client.controller;

public interface IController {
	public void login(String username, String password);
	
	public void requestGame(int numPlayers);
}