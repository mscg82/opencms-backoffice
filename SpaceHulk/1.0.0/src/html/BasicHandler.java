// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Stack;

class BasicHandler extends DefaultHandler
{
    public Node topNode;
    private Stack nodeStack = new Stack();

    public BasicHandler()
    {
    }

    public void startDocument()
        throws SAXException
    {
        topNode = new Node("");
        nodeStack.removeAllElements();
        nodeStack.push(topNode);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        //util.Debug.message("startElement qName " + qName + " uri " + uri + " localName " + localName);
        Node node = (Node) nodeStack.peek();
        //util.Debug.message("Add node " + qName + " to " + node.name);
        Node newNode = new Node(qName);
        if (attributes != null && attributes.getLength() > 0)
        {
            //util.Debug.message(qName + " attr size " + attributes.getLength());
            for (int j = 0; j < attributes.getLength(); j++)
            {
                //util.Debug.message(attributes.getLocalName(j) + ":" + attributes.getQName(j)
                    //+ " =\"" + attributes.getValue(j)+"\"");
                newNode.addAttribute(attributes.getQName(j), attributes.getValue(j));
            }
        }
        node.appendNode(newNode);
        nodeStack.push(newNode);
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (length > 0)
        {
            // TODO Collapse multiple white space at beginning and end into a single whitespace
            String text = new String(ch, start, length);
            text = text.replace('\n', ' ');
            Node node = (Node) nodeStack.peek();
            node.appendText(text);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        nodeStack.pop();
        //util.Debug.message("endElement " + qName + " " + ((Node) nodeStack.peek()).name);
    }

    public void endDocument() throws SAXException
    {
    }
}
