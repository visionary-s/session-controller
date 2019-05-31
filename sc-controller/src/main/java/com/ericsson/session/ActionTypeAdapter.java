package com.ericsson.session;

import java.util.Arrays;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * ActionType adapter.
 * @author shibiyun
 *
 */
public class ActionTypeAdapter extends XmlAdapter<String, ActionType> {
	
	@Override
	public ActionType unmarshal(String str) throws Exception {
		
		Optional<ActionType> actionType = Arrays.stream(ActionType.values())
				.filter(type -> str.equals(type.getValue()))
                .findAny();
		return actionType.orElse(ActionType.START);
	}
	
	@Override
	public String marshal(ActionType str) throws Exception {
		return str.getValue();
	}
}
