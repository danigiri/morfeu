package cat.calidos.morfeu.webapp.injection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HttpFilterModuleTest {

private FilterChain			chain;
private HttpServletRequest	request;
private HttpServletResponse	response;

@BeforeEach
public void setup() {

	chain = Mockito.mock(FilterChain.class);
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);

}


@Test @DisplayName("Handling exceptions test")
public void testHandledExceptions() throws Exception {

	doThrow(new ServletException()).when(chain).doFilter(null, null);

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f0 = (	req,
																		resp) -> true;
	var filters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	filters.add(f0);

	assertThrows(
			MorfeuRuntimeException.class,
			() -> HttpFilterModule.process(filters, filters, null, null, chain));

}


@Test @DisplayName("Right order of filters test")
public void testFilterOrder() {

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f0 = (	req,
																		resp) -> true;
	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f1 = (	req,
																		resp) -> true;
	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f2 = (	req,
																		resp) -> true;

	var filters = new HashMap<Integer, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>(
			3);
	filters.put(HttpFilterModule.IDENTITY_INDEX, HttpFilterModule.identity());
	filters.put(0, f0);
	filters.put(1, f1);
	filters.put(2, f2);

	List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> filterList = HttpFilterModule
			.preFilters(filters);
	assertAll(
			"Testing filter order",
			() -> assertNotNull(filterList),
			() -> assertEquals(3, filterList.size(), "Wrong filter list size"),
			() -> assertEquals(f0, filterList.get(0), "f0 should be the first filter"),
			() -> assertEquals(f1, filterList.get(1), "f1 should be the second filter"),
			() -> assertEquals(f2, filterList.get(2), "f2 should be the third filter"));

}


@Test @DisplayName("Test processing")
public void testProcess() throws Exception {

	when(request.getHeader(anyString())).then(returnsFirstArg());

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f0 = (	req,
																		resp) -> {
		resp.setHeader("foo0", req.getHeader("foo0"));

		return true;

	};
	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f1 = (	req,
																		resp) -> {
		resp.setHeader("foo1", req.getHeader("foo1"));

		return true;

	};
	var preFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	preFilters.add(f0);
	preFilters.add(f1);

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f2 = (	req,
																		resp) -> {
		resp.setHeader("foo2", req.getHeader("foo2"));

		return true;

	};
	var postFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	postFilters.add(f2);

	boolean continue_ = HttpFilterModule.process(preFilters, postFilters, request, response, chain);
	assertTrue(continue_, "filter chain was not stopped");

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

	when(request.getHeader(anyString())).then(returnsFirstArg());

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f0 = (	req,
																		resp) -> {
		req.getHeader("foo0");

		return true;

	};
	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f1 = (	req,
																		resp) -> {
		req.getHeader("foo1");

		return false; // this will stop

	};
	var preFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	preFilters.add(f0);
	preFilters.add(f1);

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f2 = (	req,
																		resp) -> {
		req.getHeader("foo2");

		return true;

	};
	var postFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	postFilters.add(f2);

	boolean continue_ = HttpFilterModule.process(preFilters, postFilters, request, response, chain);

	assertAll(
			"Checking filter stopped",
			() -> assertFalse(continue_, "filter chain continued when it should have stopped"),
			() -> assertEquals(
					2,
					Mockito.mockingDetails(request).getInvocations().size(),
					"ran 3 filters and not 2"));
	verify(request).getHeader("foo0");
	verify(request).getHeader("foo1");

}


@Test @DisplayName("Test pre and post filter")
public void testPrePost() throws Exception {

	StringBuffer testBuffer = new StringBuffer("");

	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f0 = (	req,
																		resp) -> {

		testBuffer.append("0");

		return true;

	};
	BiFunction<HttpServletRequest, HttpServletResponse, Boolean> f1 = (	req,
																		resp) -> {

		testBuffer.append("1");

		return true;

	};
	var preFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	preFilters.add(f0);
	var postFilters = new LinkedList<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>>();
	postFilters.add(f1);

	boolean continue_ = HttpFilterModule.process(preFilters, postFilters, request, response, chain);

	assertAll(
			"Checking filter stopped",
			() -> assertTrue(continue_, "filter chain stopped when it should continue"),
			() -> assertEquals("01", testBuffer.toString(), "Should have ran the two filters"));

}

}

/*
 * Copyright 2024 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
