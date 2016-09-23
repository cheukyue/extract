package us.ceka.dao.impl;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import us.ceka.dao.AbstractDao;

public abstract class AbstractDaoImpl<PK extends Serializable, T> implements AbstractDao<PK, T> {
    
    private final Class<? extends T> persistentClass;
    protected final Logger log = LoggerFactory.getLogger(getClass().getName());
     
    @SuppressWarnings("unchecked")
    public AbstractDaoImpl(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
     
    @Autowired
    private SessionFactory sessionFactory;
 
    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    
	@Override
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }
 
    public void persist(T entity) {
        getSession().persist(entity);
    }
 
    public void delete(T entity) {
        getSession().delete(entity);
    }
    
     /*
    public Criteria createEntityCriteria(){
        return getSession().createCriteria(persistentClass);
    }
    */
 
}
