import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;


public class Main {
	
	public static void main(String[] args)throws Exception {
		
		JexlContext jc = JexlHelper.createContext();
		Person mickey = new Person(1, "Mickey");
		Person donald = new Person(2, "Donald");
		List<Person> persons = new ArrayList<Person>();
		persons.add(mickey);
		persons.add(donald);
		jc.getVars().put("number", 1);
		jc.getVars().put("persons", persons);
		Expression e = ExpressionFactory.createExpression("number");
	    Object o = e.evaluate(jc);
	    System.out.println(o.getClass());
	    
	    e = ExpressionFactory.createExpression("persons[0].name");
	    o = e.evaluate(jc);
	    System.out.println(o);
	}
}
