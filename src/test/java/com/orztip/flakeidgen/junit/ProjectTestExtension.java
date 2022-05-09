package com.orztip.flakeidgen.junit;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @link https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
 *
 */
public class ProjectTestExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource{

    private static boolean started = false;

	@Override
	public void close() throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if (!started) {
            started = true;
            this.beforeAllStartReal(context);
        }
		
	}
	
	private void beforeAllStartReal(ExtensionContext context) throws Exception {
        // Your "before all tests" startup logic goes here
        context.getRoot().getStore(GLOBAL).put("any unique name", "test1111");
		
	}
	
}
