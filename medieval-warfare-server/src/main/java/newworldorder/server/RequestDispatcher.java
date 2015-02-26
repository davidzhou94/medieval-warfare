package newworldorder.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import newworldorder.common.network.message.AbstractCommand;
import newworldorder.common.network.message.CommandExecutor;
import newworldorder.common.network.message.RemoteCommand;
import newworldorder.server.service.ServiceLocator;

public class RequestDispatcher implements CommandExecutor {

	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private ServiceLocator locator;
	
	public RequestDispatcher() throws IOException {
		locator = ServiceLocator.getInstance();
	}

	@Override
	public void execute(AbstractCommand command) {
		if (command instanceof RemoteCommand) {
			RemoteCommand remoteCommand = (RemoteCommand) command;
			remoteCommand.setServiceLocator(locator);
			
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					remoteCommand.execute();
				}
			});
		}
	}
}