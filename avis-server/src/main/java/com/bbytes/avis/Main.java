package com.bbytes.avis;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class for Avis Server
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
public class Main {

	private static Logger LOG = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LOG.info("Starting Avis Server....");
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "classpath:/spring/app-context.xml" }, false);
			// LOG.debug("context file loaded successfully");
			// context.getEnvironment().getPropertySources()
			// .addFirst(new ResourcePropertySource("classpath:revealConf/reveal_app.properties"));
			LOG.debug("property file loaded successfully");
			context.refresh();
			LOG.debug("context file refreshed successfully");
			LOG.info("Avis server is running....");
			LOG.info("Enter :q and hit enter to quit: ");
			while (true) {

				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String str = bufferRead.readLine();
				if (str != null && !str.isEmpty()) {
					if (str.trim().equals(":q")) {
						LOG.debug("exiting system...");
						System.exit(0);
					}
				}
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

}
