// CELL MODEL . JAVA

package cat.calidos.morfeu.model;

import java.util.Optional;
import java.util.OptionalInt;


public interface CellModel extends Locatable {

static final int UNBOUNDED = -1;

boolean isSimple();

boolean isComplex();

boolean isReference();

boolean isAttribute();

Optional<CellModel> getReference();

Type getType();

int getMinOccurs();

Optional<String> getDefaultValue();

OptionalInt getMaxOccurs();

Metadata getMetadata();

Optional<String> getCategory();

ComplexCellModel asComplex();

}

/*
 * Copyright 2018 Daniel Giribet
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
