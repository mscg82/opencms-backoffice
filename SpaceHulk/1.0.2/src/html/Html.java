// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package html;

import radui.View;
import radui.VerticalView;
import radui.StackView;
import radui.FillView;
import radui.TextView;
import radui.SpaceView;
import radui.ScrollView;
import radui.ScrollTheme;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import main.SpaceHulk;
import util.MiscUtils;

public class Html
{
    private Node node_;
    private int bgColor_ = 0x000000;
    private Style body_ = new Style(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
    private Style h1_ = new Style(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
    
    public Html(String filename)
        throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException
    {
        h1_.anchor = Graphics.HCENTER | Graphics.TOP;

        InputStream is = getClass().getResourceAsStream(MiscUtils.RESOURCES_FOLDER + filename);
        if (is != null)
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            BasicHandler bh = new BasicHandler();
            saxParser.parse(is, bh);
            node_ = bh.topNode;
            //util.Debug.message("Html::Html " + node_);

            Node style = node_.getSubChild("html/style");
            if (style != null)
                setStyle(style);
        }
    }
    
    Node getNode()
    {
        return node_;
    }
    
    Style getH1()
    {
        return h1_;
    }
    
    Style getBody()
    {
        return body_;
    }
    
    int getBgColor()
    {
        return bgColor_;
    }
    
    private void setStyle(Node style)
    {
        try {
            //util.Debug.message("Html::setStyle");
            for (int i = 0; i < style.children.size(); ++i)
            {
                Object o = style.children.elementAt(i);
                if (o instanceof String)
                {
                    String s = (String) o;
                    //util.Debug.message("Style: " + s);
                    int begin = 0;
                    int start;
                    while ((start = s.indexOf('{', begin)) != -1)
                    {
                            int end = s.indexOf('}', start);
                            //util.Debug.message("---  " + start + " " + end);
                            String name = s.substring(begin, start).trim();
                            String text = s.substring(start + 1, end - 1).trim();
                            setStyle(name, text);
                            begin = end + 1;
                    }
                }
            }
        } catch(Exception e) {
            SpaceHulk.instance.showException("Html::setStyle", e);
        }
    }
    
    private void setStyle(String name, String text)
    {
        //util.Debug.message("Html::setStyle '" + name + "' '" + text + "'");
        Style s = null;
        if (name.equals("body"))
            s = body_;
        else if (name.equals("h1"))
            s = h1_;
        else
            util.Debug.error("Html::setStyle Unknown '" + name + "'");
        if (s != null)
            s.setStyle(text);
    }
    
    public View createView()
    {
        //util.Debug.message("Html::createView");
        Node html = node_.getChild("html");
        if (html != null)
        {
            ScrollTheme st = new ScrollTheme();
            st.color = body_.scrollTrackColor;
            st.colorBack = body_.scrollBaseColor;
            return new ScrollView(createHtmlView(html), Font.getDefaultFont().getHeight(), st);
        }
        else
            return null;
    }
    
    private View createHtmlView(Node html)
    {
        //util.Debug.message("Html::createHtmlView");
        VerticalView vv = new VerticalView();
        for (int i = 0; i < html.children.size(); ++i)
        {
            Object o = html.children.elementAt(i);
            if (o instanceof Node)
            {
                Node n = (Node) o;
                if (n.name.equals("body"))
                    createBodyView(n, vv);
            }
        }
        return vv;
    }

    private void createBodyView(Node body, VerticalView vv)
    {
        //util.Debug.message("Html::createBodyView");
        for (int i = 0; i < body.children.size(); ++i)
        {
            Object o = body.children.elementAt(i);
            if (o instanceof Node)
            {
                Node n = (Node) o;
                if (n.name.equals("p"))
                    createPView(n, vv);
                else if (n.name.equals("h1"))
                    createH1View(n, vv);
                else
                    util.Debug.error("Html::createBodyView Unknown'" + n.name + "'");
            }
        }
    }
    
    private void createPView(Node P, VerticalView vv)
    {
        //util.Debug.message("Html::createPView " + P.name);
        createStyleView(P, body_, vv);
    }
    
    private void createH1View(Node H1, VerticalView vv)
    {
        //util.Debug.message("Html::createH1View " + H1.name);
        createStyleView(H1, h1_, vv);
    }

    private void createStyleView(Node n, Style ns, VerticalView vv)
    {
        //util.Debug.message("Html::createStyleView " + n.name);
        
        StringBuffer sb = new StringBuffer();
        fillNode(n, sb);
        //util.Debug.message("Html::createStyleView str " + sb.toString());
        TextView tv = new TextView(sb.toString().trim());
        tv.setFont(ns.font);
        tv.setColor(ns.color);
        tv.setAnchor(ns.anchor);
        
        if (ns.bg != bgColor_)
        {
            StackView sv = new StackView();
            sv.add(new FillView(ns.bg));
            sv.add(tv);
            vv.add(sv);
        }
        else
            vv.add(tv);
        
        vv.add(new SpaceView(0, ns.paraSpace));
    }

    private void fillNode(Node node, StringBuffer sb)
    {
        //util.Debug.message("Html::fillNode" + node.name);
        for (int i = 0; i < node.children.size(); ++i)
        {
            Object o = node.children.elementAt(i);
            if (o instanceof String)
                sb.append((String) o);
            else if (o instanceof Node)
            {
                Node n = (Node) o;
                if (n.name.equals("i"))
                    fillI(n, sb);
                else if (n.name.equals("b"))
                    fillB(n, sb);
                else
                    util.Debug.error("Html::fillNode Unknown'" + n.name + "'");
            }
        }
    }
    
    private void fillI(Node I, StringBuffer sb)
    {
        //util.Debug.message("Html::fillI " + I.name);
        sb.append(TextView.OPEN_ITALIC);
        fillNode(I, sb);
        sb.append(TextView.CLOSE_ITALIC);
    }
    
    private void fillB(Node I, StringBuffer sb)
    {
        //util.Debug.message("Html::fillB " + I.name);
        sb.append(TextView.OPEN_BOLD);
        fillNode(I, sb);
        sb.append(TextView.CLOSE_BOLD);
    }
}
