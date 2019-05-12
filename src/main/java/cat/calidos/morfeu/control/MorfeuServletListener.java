// MORFEU SERVLET LISTENER . JAVA

package cat.calidos.morfeu.control;

import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** Servlet context initialisation callbacks
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuServletListener implements ServletContextListener {

protected final static Logger log = LoggerFactory.getLogger(MorfeuServletListener.class);

public static final String ASYNC_CONTEXT_QUEUE = "ASYNC_CONTEXT_QUEUE";
public static final String ASYNC_TIMEOUT = "ASYNC_TIMEOUT";

private final LinkedBlockingQueue<AsyncContext> contextQueue = new LinkedBlockingQueue<AsyncContext>();


@Override
public void contextInitialized(ServletContextEvent sce) {

	log.info("------ Servlet context initialized ------");
	sce.getServletContext().setAttribute("counter", (Integer)0);
	sce.getServletContext().setAttribute(ASYNC_CONTEXT_QUEUE, contextQueue);
}


@Override @SuppressWarnings("unchecked")
public void contextDestroyed(ServletContextEvent sce) {

	log.info("------ Servlet context initialized  -----");
	((LinkedBlockingQueue<AsyncContext>)sce.getServletContext().getAttribute(ASYNC_CONTEXT_QUEUE)).clear();

}


}

/*
 *    Copyright 2019 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

