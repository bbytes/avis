package com.bbytes.avis.rabbitmq;

import org.apache.log4j.Logger;
import org.springframework.util.ErrorHandler;


/**
 * Default Error Handler for all rabbit mq errors
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class ErrorHandlerImpl implements ErrorHandler{

	private Logger log = Logger.getLogger(ErrorHandlerImpl.class);
	
	@Override
	public void handleError(Throwable arg0) {
		log.error(arg0.getMessage());
		arg0.printStackTrace();
	}

}
