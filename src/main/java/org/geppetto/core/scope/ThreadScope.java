package org.geppetto.core.scope;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.NamedInheritableThreadLocal;

/**
 * Thread scope implementation.
 * 
 * @author David Winterfeldt
 */
public class ThreadScope implements Scope {
		
    protected final Map<String, Object> _hBeans = new HashMap<String, Object>();
    
	private static final Log logger = LogFactory.getLog(ThreadScope.class);

	/**
     * Gets bean from scope.
     */
    public Object get(String name, ObjectFactory<?> factory) {
        Object result = null;
        
        Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();
        
        if (!hBeans.containsKey(name)) {
            result = factory.getObject();
            
            hBeans.put(name, result);
        } else {
            result = hBeans.get(name);
        }
        
        
        return result;
    }
    
    /**
     * Removes bean from scope.
     */
    public Object remove(String name) {
        Object result = null;
        
        Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();

        if (hBeans.containsKey(name)) {
            result = hBeans.get(name);
            
            hBeans.remove(name);
        }

        return result;
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        ThreadScopeContextHolder.currentThreadScopeAttributes().registerRequestDestructionCallback(name, callback);
    }

    /**
     * Resolve the contextual object for the given key, if any.
     * Which in this case will always be <code>null</code>.
     */
    public Object resolveContextualObject(String key) {
        return null;
    }

    /**
     * Gets current thread name as the conversation id.
     */
    public String getConversationId() {
        return Thread.currentThread().getName();
    }
}