package com.proserus.stocks.bp.dao;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.lang3.Validate;

import com.google.inject.Singleton;

@Singleton
public class PersistenceManager {

	private EntityManager em = Persistence.createEntityManagerFactory("jpaDemo").createEntityManager();

	public Object persist(Object o) {
		Validate.notNull(o);
		
		//EntityManagerFactory emf  = Persistence.createEntityManagerFactory("jpaDemo");
		//EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		boolean wasAlreadyActive = false;
		if(tx.isActive()){
			em.joinTransaction();
			wasAlreadyActive = true;
		}else{
			tx.begin();
		}
		
		try{
			em.persist(o);
		}catch(EntityExistsException e){
			em.refresh(o);
		}
		
		if(!wasAlreadyActive){
			tx.commit();
		}
		
		//em.close();
		return o;
		//emf.close();
	}
	
	
	public void remove(Object o){
		Validate.notNull(o);
		
		//EntityManagerFactory emf  = Persistence.createEntityManagerFactory("jpaDemo");
		//EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		if(!tx.isActive()){
			tx.begin();
		}
		em.remove(o);
		tx.commit();
		//em.close();
		//emf.close();
	}
	
	public EntityManager getEntityManager(){
		if(em==null){
			try{
				em = Persistence.createEntityManagerFactory("jpaDemo").createEntityManager();
			} catch (Throwable e) {
				//ignore
			}
		}
		return em;
	}
	
	public void close(){
		em.close();
	}
	
	
}
