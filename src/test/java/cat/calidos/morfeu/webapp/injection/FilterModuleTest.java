package cat.calidos.morfeu.webapp.injection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FilterModuleTest {

private HttpFilterModule filterModule;


@BeforeEach
public void setup() {
	filterModule = new HttpFilterModule();
}


@Test @DisplayName("Handling exceptions test")
public void testHandledExceptions() {

	assertThrows(IllegalStateException.class, () -> filterModule.request(null));
	assertThrows(IllegalStateException.class, () -> filterModule.response(null));

}


@Test @DisplayName("Right order of filters test")
public void testFilterOrder() {

	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f0 = (reqResp, chain) -> {};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f1 = (reqResp, chain) -> {};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f2 = (reqResp, chain) -> {};

	HashMap<Integer, BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>> filters 
					= new HashMap<Integer, BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>>(3);
	filters.put(HttpFilterModule.IDENTITY_INDEX, HttpFilterModule.identity());
	filters.put(0, f0);
	filters.put(1, f1);
	filters.put(2, f2);

	List<BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>> filterList
																					= filterModule.filterList(filters);
	assertAll("",
		() -> assertNotNull(filterList),
		() -> assertEquals(3, filterList.size(), "Wrong filter list size"),
		() -> assertEquals(f0, filterList.get(0), "f0 should be the first filter"),
		() -> assertEquals(f1, filterList.get(1), "f1 should be the second filter"),
		() -> assertEquals(f2, filterList.get(2), "f2 should be the third filter")
	);

}


@Test @DisplayName("Test processing")
public void testProcess() throws Exception {

	HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	when(request.getHeader(anyString())).then(returnsFirstArg());
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>f0 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		resp.setHeader("foo0", req.getHeader("foo0"));

		try {
			chain.doFilter(req, resp);
		} catch (Exception e) {}

	};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f1 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		resp.setHeader("foo1", req.getHeader("foo1"));

		try {
			chain.doFilter(req, resp);
		} catch (Exception e) {}

	};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f2 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		resp.setHeader("foo2", req.getHeader("foo2"));

		try {
			chain.doFilter(req, resp);
		} catch (Exception e) {}

	};

	List<BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>> filters 
							= new LinkedList<BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>>();
	filters.add(f0);
	filters.add(f1);
	filters.add(f2);

	Pair<HttpServletRequest, HttpServletResponse> reqResp 
												= new Pair<HttpServletRequest, HttpServletResponse>(request, response);
	boolean process = filterModule.process(filters, reqResp, filterModule.chain(filters));
	assertTrue(process,"filter chain was not stopped");

	InOrder requestVerifier = Mockito.inOrder(request);
	requestVerifier.verify(request).getHeader("foo0");
	requestVerifier.verify(request).getHeader("foo1");
	requestVerifier.verify(request).getHeader("foo2");

	InOrder responseVerifier = Mockito.inOrder(response);
	responseVerifier.verify(response).setHeader("foo0", "foo0");
	responseVerifier.verify(response).setHeader("foo1", "foo1");
	responseVerifier.verify(response).setHeader("foo2", "foo2");

}


@Test @DisplayName("Test stopping filter chain")
public void testStopping() throws Exception {

	HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	when(request.getHeader(anyString())).then(returnsFirstArg());
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>f0 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		req.getHeader("foo0");

		try {
			chain.doFilter(req, resp);
		} catch (Exception e) {}

	};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f1 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		req.getHeader("foo1");
	};
	BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain> f2 = (reqResp, chain) -> {

		HttpServletRequest req = reqResp.left();
		HttpServletResponse resp = reqResp.right();
		req.getHeader("foo2");

	};
	List<BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>> filters 
							= new LinkedList<BiConsumer<Pair<HttpServletRequest, HttpServletResponse>, FilterChain>>();
	filters.add(f0);
	filters.add(f1);
	filters.add(f2);

	Pair<HttpServletRequest, HttpServletResponse> reqResp 
	= new Pair<HttpServletRequest, HttpServletResponse>(request, response);
	boolean process = filterModule.process(filters, reqResp, filterModule.chain(filters));

	assertAll("Checking filter stopped",
		() -> assertFalse(process,"filter chain was stopped"),
		() -> assertEquals(2, Mockito.mockingDetails(request).getInvocations().size(), "ran 3 filters and not 2")
	);
	verify(request).getHeader("foo0");
	verify(request).getHeader("foo1");

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

