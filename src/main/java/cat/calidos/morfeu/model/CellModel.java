package cat.calidos.morfeu.model;

import java.util.Optional;
import java.util.OptionalInt;

public interface CellModel extends Locatable {

static final int UNBOUNDED = -1;

boolean isSimple();

boolean isComplex();

boolean isReference();

Optional<CellModel> getReference();

Type getType();

int getMinOccurs();

Optional<String> getDefaultValue();

OptionalInt getMaxOccurs();

Metadata getMetadata();

ComplexCellModel asComplex();


}
