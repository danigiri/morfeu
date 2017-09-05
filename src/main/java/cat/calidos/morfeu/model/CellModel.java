package cat.calidos.morfeu.model;


public interface CellModel extends Locatable {

boolean isSimple();

boolean isComplex();

boolean isReference();

ComplexCellModel asComplex();

CellModelReference asReference();

Type getType();

String getPresentation();

String getThumb();

}
