// MORFEU SERVLET LISTENER . JAVA

package cat.calidos.morfeu.control;

import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.webapp.GenericAsyncHttpServlet;


/** Servlet context initialisation callbacks
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuServletListener implements ServletContextListener {

protected final static Logger log = LoggerFactory.getLogger(MorfeuServletListener.class);

private final LinkedBlockingQueue<AsyncContext> contextQueue = new LinkedBlockingQueue<AsyncContext>();


@Override
public void contextInitialized(ServletContextEvent sce) {

	log.info("------ Servlet context initialized ------");
	ServletContext ctxt = sce.getServletContext();
	ctxt.setAttribute("counter", (Integer)0);
	ctxt.setAttribute(GenericAsyncHttpServlet.ASYNC_CONTEXT_QUEUE, contextQueue);
}


@Override @SuppressWarnings("unchecked")
public void contextDestroyed(ServletContextEvent sce) {

	log.info("------ Servlet context initialized  -----");
	ServletContext ctxt = sce.getServletContext();
	((LinkedBlockingQueue<AsyncContext>)ctxt.getAttribute(GenericAsyncHttpServlet.ASYNC_CONTEXT_QUEUE)).clear();

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

