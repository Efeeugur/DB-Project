package model.dao.impl;

import model.dao.SessionDAO;
import model.entity.Session;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of SessionDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class SessionDAOImpl implements SessionDAO {
    
    private final Map<Integer, Session> sessions = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Session save(Session session) {
        session.setId(nextId++);
        sessions.put(session.getId(), session);
        return session;
    }
    
    @Override
    public Optional<Session> findById(int id) {
        return Optional.ofNullable(sessions.get(id));
    }
    
    @Override
    public List<Session> findAll() {
        return new ArrayList<>(sessions.values());
    }
    
    @Override
    public Session update(Session session) {
        if (sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), session);
            return session;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return sessions.remove(id) != null;
    }
    
    @Override
    public int count() {
        return sessions.size();
    }
    
    @Override
    public List<Session> findByCourseId(int courseId) {
        return sessions.values().stream()
                .filter(s -> s.getCourseId() == courseId)
                .collect(Collectors.toList());
    }
}
