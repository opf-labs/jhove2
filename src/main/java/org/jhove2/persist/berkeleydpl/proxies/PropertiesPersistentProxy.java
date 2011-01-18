/**
 * 
 */
package org.jhove2.persist.berkeleydpl.proxies;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PersistentProxy;

/**
 * @author smorrissey
 *
 */
@Persistent(proxyFor=Properties.class)
public class PropertiesPersistentProxy implements PersistentProxy<Properties> {

	HashMap<String, String> propsMap;
	/**
	 * 
	 */
	public PropertiesPersistentProxy() {}

	@Override
	public Properties convertProxy() {
		Properties properties = null;
		if (propsMap != null){
			properties = new Properties();
			for (Map.Entry<String, String> entry:propsMap.entrySet()){
				properties.setProperty(entry.getKey(), entry.getValue());
			}
		}
		return properties;
	}

	@Override
	public void initializeProxy(Properties properties) {
		if (properties != null){
			propsMap = new HashMap<String, String>();
			for (String key:properties.stringPropertyNames()){
				propsMap.put(key, properties.getProperty(key));
			}
		}
	}

}
