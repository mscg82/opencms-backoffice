package com.mscg.engine;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;

import org.apache.http.impl.auth.NTLMEngine;
import org.apache.http.impl.auth.NTLMEngineException;

import com.mscg.config.ConfigLoader;


public class JCIFSEngine implements NTLMEngine {

	static{
		List<String> names = new LinkedList<String>();
		try{
			names = (List<String>) ConfigLoader.getInstance().get("jcifs-properties.property.name");
		} catch(ClassCastException e){
			names.add((String) ConfigLoader.getInstance().get("jcifs-properties.property.name"));
		}
		List<String> values = new LinkedList<String>();
		try{
			values = (List<String>) ConfigLoader.getInstance().get("jcifs-properties.property.value");
		} catch(ClassCastException e){
			values.add((String) ConfigLoader.getInstance().get("jcifs-properties.property.value"));
		}

		Iterator<String> namesIt = names.iterator();
		Iterator<String> valuesIt = values.iterator();

		while(namesIt.hasNext() && valuesIt.hasNext()){
			String name = namesIt.next();
			String value = valuesIt.next();
			jcifs.Config.setProperty(name, value);
		}
	}

    public String generateType1Msg(
            String domain,
            String workstation) throws NTLMEngineException {

        Type1Message t1m = new Type1Message(
                Type1Message.getDefaultFlags(),
                domain,
                workstation);
        return Base64.encode(t1m.toByteArray());
    }

    public String generateType3Msg(
            String username,
            String password,
            String domain,
            String workstation,
            String challenge) throws NTLMEngineException {
        Type2Message t2m;
        try {
            t2m = new Type2Message(Base64.decode(challenge));
        } catch (IOException ex) {
            throw new NTLMEngineException("Invalid Type2 message", ex);
        }
        Type3Message t3m = new Type3Message(
                t2m,
                password,
                domain,
                username,
                workstation, Type3Message.getDefaultFlags(t2m));
        return Base64.encode(t3m.toByteArray());
    }

}