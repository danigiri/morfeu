package cat.calidos.morfeu.model;

import java.util.OptionalInt;

public interface CellModel extends Locatable {

static final int UNBOUNDED = -1;

boolean isSimple();

boolean isComplex();

boolean isReference();

Type getType();

int getMinOccurs();

OptionalInt getMaxOccurs();

Metadata getMetadata();

ComplexCellModel asComplex();

CellModelReference asReference();

}
