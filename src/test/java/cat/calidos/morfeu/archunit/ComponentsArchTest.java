package cat.calidos.morfeu.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** Proof of concept of arch unit testing, validating somme basic code structure assumptions on ComponentBuilders
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@AnalyzeClasses(packages = "cat.calidos.morfeu")
public class ComponentsArchTest {

private static final String COMPONENT = "@Component";

private static final String PRODUCTION_COMPONENT = "@ProductionComponent";

@ArchTest
private final ArchRule componentsShouldBeNamedAsComponents = classes()
																.that()
																.areAnnotatedWith(PRODUCTION_COMPONENT)
																.or()
																.areAnnotatedWith(COMPONENT)
																.should()
																.haveSimpleNameEndingWith("Component");

@ArchTest
private final ArchRule componentsShouldBeInInjectionPackages = classes()
																.that()
																.areAnnotatedWith(PRODUCTION_COMPONENT)
																.or()
																.areAnnotatedWith(COMPONENT)
																.should()
																.resideInAPackage("..injection..");

}

/*
 *    Copyright 2020 Daniel Giribet
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

