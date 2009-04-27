/**
 *
 */
package test.main;

import java.io.InputStream;

import org.apache.xmlbeans.XmlObject;

import test.mscg.xsd.InnerElement;
import test.mscg.xsd.TESTDocument;
import test.mscg.xsd.TestType;
import test.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TESTDocument doc = null;

		String action = null;
		if(args.length > 0)
			action = args[0];

		if("create".equals(action)){
			doc = TESTDocument.Factory.newInstance();
			TestType test = doc.addNewTEST();
			test.setAttr1(43);
			test.setAttr2(23.4532);
			InnerElement el1 = test.addNewEl();
			el1.setId(1);
			el1.setInEl1("Elemento interno 1-1");
			el1.setInEl2("Elemento interno 1-2");
			InnerElement el2 = test.addNewEl();
			el2.setId(2);
			el2.setInEl1("Elemento interno 2-1");
			el2.setInEl2("Elemento interno 2-2");
			InnerElement el3 = test.addNewEl();
			el3.setId(-3);
			el3.setInEl1("Elemento interno 3-1");
			el3.setInEl2("aa");
			System.out.println("Generated document:");
			System.out.println(doc.xmlText(Util.getStandardOptions()));
		}
		else{
			String inputFile = "input.xml";
			InputStream in = Main.class.getResourceAsStream("/" + inputFile);
			if(in != null){
				try {
					doc = TESTDocument.Factory.parse(in);
					System.out.println("Document read:");
					System.out.println(doc.xmlText(Util.getStandardOptions()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				System.out.println("Cannot find input file \"" + inputFile + "\" in classpath.");
			}
		}

		if(doc != null){
			try{
				System.out.println("First search");
				// using XPath on document
				String queryExpression =
				    "declare namespace m='http://mscg.test/xsd';" +
				    "/m:TEST/m:el[@id=1]";
				XmlObject returns[] = doc.selectPath(queryExpression);
				InnerElement elems[] = (InnerElement[]) returns;
				for(int i = 0, l = elems.length; i < l; i++){
					printInnerElement(elems[i]);
				}

				System.out.println("Second search");
				queryExpression =
				    "declare namespace m='http://mscg.test/xsd';" +
				    "/m:TEST/m:el[@id>1]";
				returns = doc.selectPath(queryExpression);
				elems = (InnerElement[]) returns;
				for(int i = 0, l = elems.length; i < l; i++){
					printInnerElement(elems[i]);
				}

				System.out.println("Third search");
				queryExpression =
				    "declare namespace m='http://mscg.test/xsd';" +
				    "/m:TEST/m:el[abs(@id)>1 and string-length(m:inEl2)<=2]";
				returns = doc.selectPath(queryExpression);
				elems = (InnerElement[]) returns;
				for(int i = 0, l = elems.length; i < l; i++){
					printInnerElement(elems[i]);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	public static void printInnerElement(InnerElement el){
		System.out.println("Content of inner element with id " + el.getId());
		System.out.println("    Value 1: " + (el.isSetInEl1() ? el.getInEl1() : "Not set"));
		System.out.println("    Value 2: " + (el.isSetInEl2() ? el.getInEl2() : "Not set"));
	}

}
