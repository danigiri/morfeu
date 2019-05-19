/*
 *    Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import cat.calidos.morfeu.runtime.api.Task;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTaskManager {

Map<String, ExecTask> tasks;


public ExecTaskManager(Map<String, ExecTask> tasks) {
	this.tasks = tasks;
}


/**	@param ready task to be added to the manager, will replace existing task with the same id */
public void addReadyTask(ExecReadyTask task, String id) {
	this.tasks.put(id, task);
}


//public Optional<ExectReadyTask> getReadyTask() {
//	
//	ExecTask task = tasks.get(id);
//	return task==null || task.getStatus()!=Task.READY
//}


/**	@return all tasks handled by the manager */
public List<ExecTask> getTasks() {
	return new ArrayList<ExecTask>(tasks.values());
}


public OptionalInt getStatusOfTask(String id) {

	ExecTask task = tasks.get(id);

	return task==null ? OptionalInt.empty() : OptionalInt.of(task.getStatus());

}


}
