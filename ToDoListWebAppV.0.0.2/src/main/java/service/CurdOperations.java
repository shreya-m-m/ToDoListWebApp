package service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import entities.TaskEntity;
import entities.UserEntity;

@Service
public class CurdOperations {
    
    @Autowired
	protected SessionFactory factory;
   
    
    public UserEntity registerr(UserEntity user) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
 
    public UserEntity validateUser(String username, String password) {
        try (Session session = factory.openSession()) {
            Criteria crit = session.createCriteria(UserEntity.class);
            crit.add(Restrictions.eq("username", username));
            crit.add(Restrictions.eq("password", password));
            return (UserEntity) crit.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
 
    }
 
//    public List<UserEntity> getUsers() {
//        Session session = factory.openSession();
//        Transaction transaction = session.beginTransaction();
//        List<UserEntity> users = session.createQuery("FROM UserEntity u", UserEntity.class).getResultList();
//        transaction.commit();
//        session.close();
//        return users;
//    }
 
    public TaskEntity addTask(Long userId, String description, String fromTime, String toTime,String status) {
        TaskEntity task = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
 
            // Retrieve the user by id
            UserEntity user = session.get(UserEntity.class, userId);
 
            // Create the task and associate it with the user
            task = new TaskEntity(description,fromTime,toTime,status);
            task.setUser(user);
 
            session.save(task);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return task;
    }
 
 
    public List<TaskEntity> getTask(Long userId, int pageNumber, int pageSize) {
        try (Session session = factory.openSession()) {
        	 Transaction transaction= session.beginTransaction();
        	 Query<TaskEntity> query = session.createQuery("FROM TaskEntity t WHERE t.user.user_id = :userId", TaskEntity.class);
             query.setParameter("userId", userId);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<TaskEntity> tasks = query.getResultList();
            session.getTransaction().commit();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    public void updateTask(TaskEntity updatedTask) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();

            // Retrieve the task from the database
            TaskEntity task = session.get(TaskEntity.class, updatedTask.getTask_id());

            // Check if the task exists and update its details
            if (task != null) {
                task.setDescription(updatedTask.getDescription());
                task.setToTime(updatedTask.getToTime());
                
                task.setStatus(updatedTask.getStatus());

                // Save the updated task
                session.update(task);
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TaskEntity deleteTask(TaskEntity task) {
		Session session = factory.openSession();
		session.beginTransaction();
		session.delete(task);
		session.getTransaction().commit();
		return task;
	}

    public boolean isFromTimeAlreadyExists(Long userId, String fromTime) {
        try (Session session = factory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM TaskEntity t WHERE t.user.user_id = :userId AND t.fromTime = :fromTime", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("fromTime", fromTime);
            Long count = query.uniqueResult();
            return count != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//    public int getTotalTasks(Long userId) {
//        try (Session session = factory.openSession()) {
//            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM MyTask t WHERE t.user.id = :userId", Long.class);
//            query.setParameter("userId", userId);
//            return query.getSingleResult().intValue();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    public int getTotalTasks(Long userId) {
        // Approximate total tasks to display 5 pages
        int approxTasks = 5;
        return approxTasks;
    }

}

