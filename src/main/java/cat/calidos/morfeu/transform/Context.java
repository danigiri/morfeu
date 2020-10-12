package cat.calidos.morfeu.transform;

import java.util.EmptyStackException;

/** A transformation context, holds state information of a given type T (usually a stack) and generates output of type O
 * 	(usually a string)
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface Context<T, O> {


/**	@return is the context empty? */
boolean empty();


/** @return current output as it is */
O output();


/** Replace current output with new one
*	@param output new output
*	@return previous output
*/
O setOutput(O output);


/** Append to output, depends on the output implementation
*	@param output new output to append
*/
void appendToOutput(O output);


/** @return top of the context */
Processor<T,O> peek() throws EmptyStackException;


/** @return remove and return top of the context */
Processor<T,O> pop() throws EmptyStackException;


/** Add an item to the top of the context to be processed
*	@param item to be added
*	@return just added item
*/
Processor<T,O> push(Processor<T,O> item);


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

