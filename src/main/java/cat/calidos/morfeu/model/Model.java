// MODEL . JAVA

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSSchemaSet;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Model extends ComplexCellModel {

protected XSSchemaSet schema;
public static final String MODEL_NAMESPACE = "";


public Model(URI u, 
				String name,
				String desc,
				Type type,
				int minOccurs, 
				int maxOccurs, 
				Metadata meta, 
				Optional<String> defaultValue,
				Attributes<CellModel> attributes,
				XSSchemaSet s,
				Composite<CellModel> children) {

	// TODO: fetch description from annotation
	super(u, name, desc, type, minOccurs, maxOccurs, meta, defaultValue, attributes, children);

	this.schema = s;

}


public int getComplextTypeCount() {

	//	XSSchema modelSchema = schema.getSchema("http://dani.calidos.com/morfeu/model");
	Iterator<XSComplexType> typeIterator = schema.iterateComplexTypes();
	int c = 0;
	while (typeIterator.hasNext()) {
		typeIterator.next();
		c++;
	}

	return c;

}


}
