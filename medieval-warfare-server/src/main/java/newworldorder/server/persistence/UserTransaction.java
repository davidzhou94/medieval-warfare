
package newworldorder.server.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import newworldorder.common.model.User;
import newworldorder.common.persistence.IUserTransaction;
import newworldorder.common.persistence.PersistenceException;
import newworldorder.server.service.OnlineUsers;

@Component
public class UserTransaction implements IUserTransaction {
	private UserStore userStore;
	private OnlineUsers onlineUsers;
	
	@Autowired
	public UserTransaction(UserStore userStore, OnlineUsers users) {
		this.userStore = userStore;
		this.onlineUsers = users;
	}
	
	@Override
	public synchronized void createUser(User user) throws PersistenceException {
		try {
			if (!userStore.containsUsername(user.getUsername())) {
				userStore.insertUser(user);
			}
			else {
				throw new PersistenceException("User with username '" + user.getUsername() + "' already exists.");
			}
		}
		catch (Exception e) {
			throw new PersistenceException("Unknown exception.");
		}
	}

	@Override
	public synchronized boolean loginUser(User user) throws PersistenceException {
		try {
			User saved = userStore.selectUser(user.getUsername());
			if (user.equals(saved)) {
				onlineUsers.add(user.getUsername());
				return true;
			}
			else return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public synchronized void logoutUser(User user) {
		onlineUsers.remove(user.getUsername());
	}
}
