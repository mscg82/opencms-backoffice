// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package html;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import main.SpaceHulk;

class Node
{
    public String name;
    private Hashtable attributes = null;
    public Vector children = new Vector();
    
    public Node(String _name)
    {
        name = _name;
    }
    
    public void addAttribute(String _name, String _value)
    {
        if (attributes == null)
            attributes = new Hashtable();
        attributes.put(_name, _value);
    }
    
    public String getAttribute(String _name)
    {
        if (attributes == null)
            return null;
        else
            return (String) attributes.get(_name);
    }
    
    public void appendText(String _text)
    {
        children.addElement(_text);
    }
    
    public void appendNode(Node _node)
    {
        //util.Debug.message("+Append child: " + _node.name + " of " + name + " " + children.size());
        children.addElement(_node);
    }
    
    public Node getChild(String childName)
    {
        if (children != null)
        {
            //util.Debug.message("Get child: " + childName + " of " + name + " " + children.size());
            Enumeration e = children.elements();
            while (e.hasMoreElements())
            {
                Object o = e.nextElement();
                if (o instanceof Node)
                {
                    Node child = (Node) o;
                    //util.Debug.message("Test child: " + child.name);
                    if (child.name.equals(childName))
                    {
                        //util.Debug.message("Found child: " + child.name);
                        return child;
                    }
                }
            }
        }
        //util.Debug.message("child not found");
        return null;
    }
    
    
    public Node getSubChild(String path)
    {
        try {
            //util.Debug.message("Path: " + path);
            int i = path.indexOf('/');
            if (i == -1)
            {
                Node child = getChild(path);
                if (child != null)
                    return child;
                else
                    return null;
            }
            else
            {
                Node child = getChild(path.substring(0, i));
                if (child != null)
                    return child.getSubChild(path.substring(i + 1));
                else
                    return null;
            }
        } catch(Exception e) {
            SpaceHulk.instance.showException("Node::getSubChild", e);
            return null;
        }
    }
}
