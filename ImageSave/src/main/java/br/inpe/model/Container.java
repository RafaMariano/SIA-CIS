package br.inpe.model;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Container {
	 private ApplicationContext contextoSpring;
	 private static Container container;
	 
	  private Container() {
		  
	  }
	  
	   private ApplicationContext getContextoSpring() {
	     if (contextoSpring == null) {
	       contextoSpring = new ClassPathXmlApplicationContext("applicationContext.xml");
	     }
	     return contextoSpring;
	   }
	   
	    public synchronized Object getBean(String nome) {
	     ApplicationContext contexto = getContextoSpring();
	     if (contexto != null) {
	       try {
	         return contexto.getBean(nome);
	       } catch (NoSuchBeanDefinitionException ex) {
	         return null;
	       }
	     }
	     return null;
	   }
	   
	    public synchronized Object getBean(String nome, Class classe) {
	     ApplicationContext context = getContextoSpring();
	     if (context != null) {
	       return context.getBean(nome, classe);
	     }
	     return null;
	   }
	   
	    public static synchronized Container getInstancia() {
	    	if (container == null) 
	    		container = new Container();
	    	
	 	     return container;
	    }

}