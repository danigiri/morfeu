package cat.calidos.morfeu.model;


public interface CellModel extends Locatable {

static final int UNBOUNDED = -1;

boolean isSimple();

boolean isComplex();

boolean isReference();

Type getType();

int getMinOccurs();

int getMaxOccurs();

Metadata getMetadata();

ComplexCellModel asComplex();

CellModelReference asReference();

}
