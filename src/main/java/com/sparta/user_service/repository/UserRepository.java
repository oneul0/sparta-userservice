package com.sparta.user_service.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.sparta.user_service.domain.User;

@Repository
public class UserRepository {
	private final Map<Long, User> idStore = new ConcurrentHashMap<>();
	private final Map<String, Long> usernameToId = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(1L);

	public Optional<User> findById(Long id) {
		return Optional.ofNullable(idStore.get(id));
	}

	public Optional<User> findByUsername(String username) {
		Long id = usernameToId.get(username);
		if (id == null) return Optional.empty();
		return findById(id);
	}

	public boolean existsByUsername(String username) {
		return usernameToId.containsKey(username);
	}

	public User save(String username, String password, String nickname) {
		Long id = sequence.getAndIncrement();
		User user = new User(id, username, password, nickname);
		idStore.put(id, user);
		usernameToId.put(username, id);
		return user;
	}
}
