package newworldorder.game.controller;

import newworldorder.common.matchmaking.GameRequest;
import newworldorder.common.network.AmqpAdapter;
import newworldorder.common.network.CommandConsumer;
import newworldorder.common.network.command.JoinGameCommand;
import newworldorder.common.network.command.LoginCommand;

import org.springframework.beans.factory.annotation.Value;

public class ClientController implements IController {
	private final AmqpAdapter adapter;
	private final Session session;
	private final CommandConsumer consumer;

	@Value("${rabbitmq.consumeFrom}")
	private String notifyExchange;

	@Value("${rabbitmq.publishTo}")
	private String commandExchange;

	@Value("${rabbitmq.routingKey}")
	private String routingKey;

	private ClientController(AmqpAdapter adapter, Session session,
			CommandConsumer consumer) {
		super();
		this.adapter = adapter;
		this.session = session;
		this.consumer = consumer;
	}

	@Override
	public boolean login(String username, String password) {
		if (username.equals("") || username == null) {
			return false;
		} else {
			LoginCommand loginCommand = new LoginCommand(username, password);
			adapter.send(loginCommand, commandExchange, routingKey);
			session.setUsername(username);
			consumer.startConsumingFromDirectExchange(notifyExchange, username);
			return true;
		}
	}

	@Override
	public void logout() {
		session.setUsername("");
		consumer.stop();
		// TODO: send logout command to server to remove from queue.
	}

	@Override
	public void requestGame(int numPlayers) {
		String username = session.getUsername();
		GameRequest curRequest = new GameRequest(username, numPlayers);
		JoinGameCommand joinGameCommand = new JoinGameCommand(username,
				curRequest);
		adapter.send(joinGameCommand, commandExchange, routingKey);
	}
}