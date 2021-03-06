/*******************************************************************************
 * Copyright (c) 2008, 2010 Freescale Semiconductor and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Freescale Semiconductor - initial API and implementation
 *     IBM Corporation - ongoing development
 *******************************************************************************/
package org.eclipse.core.internal.resources;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.internal.utils.Messages;
import org.eclipse.core.internal.utils.Policy;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.variableresolvers.PathVariableResolver;
import org.eclipse.core.runtime.*;
import org.eclipse.osgi.util.NLS;

/**
 * Repository for all variable providers available through the extension points.
 * 
 * @since 3.6
 */
public class ProjectVariableProviderManager {

	public static class Descriptor {
		PathVariableResolver provider= null;

		String name= null;

		String value= null;

		public Descriptor(IExtension extension, IConfigurationElement element) throws RuntimeException, CoreException {
			name= element.getAttribute("variable"); //$NON-NLS-1$
			value= element.getAttribute("value"); //$NON-NLS-1$
			try {
				provider= (PathVariableResolver)element.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException t) {
				t.printStackTrace();
			}
			if (name == null)
				fail(NLS.bind(Messages.mapping_invalidDef, extension.getUniqueIdentifier()));
		}

		protected void fail(String reason) throws CoreException {
			throw new ResourceException(new Status(IStatus.ERROR, ResourcesPlugin.PI_RESOURCES, 1, reason, null));
		}

		public String getName() {
			return name;
		}

		public String getValue(String variable, IResource resource) {
			if (value != null)
				return value;
			return provider.getValue(variable, resource);
		}

		public String[] getVariableNames(String variable, IResource resource) {
			if (provider != null)
				return provider.getVariableNames(variable, resource);
			return null;
		}
	}

	private static Map descriptors;

	private static ProjectVariableProviderManager instance;

	public synchronized static ProjectVariableProviderManager getDefault() {
		if (instance == null) {
			instance= new ProjectVariableProviderManager();
		}
		return instance;
	}

	public Descriptor[] getDescriptors() {
		lazyInitialize();
		return (Descriptor[])descriptors.values().toArray(new Descriptor[descriptors.size()]);
	}

	protected void lazyInitialize() {
		if (descriptors != null)
			return;
		IExtensionPoint point= Platform.getExtensionRegistry().getExtensionPoint(ResourcesPlugin.PI_RESOURCES, ResourcesPlugin.PT_VARIABLE_PROVIDERS);
		IExtension[] extensions= point.getExtensions();
		descriptors= new HashMap(extensions.length * 2 + 1);
		for (int i= 0, imax= extensions.length; i < imax; i++) {
			IConfigurationElement[] elements= extensions[i].getConfigurationElements();
			int count= elements.length;
			for (int j= 0; j < count; j++) {
				IConfigurationElement element= elements[j];
				String elementName= element.getName();
				if (elementName.equalsIgnoreCase("variableResolver")) { //$NON-NLS-1$
					Descriptor desc= null;
					try {
						desc= new Descriptor(extensions[i], element);
					} catch (CoreException e) {
						Policy.log(e);
					}
					if (desc != null)
						descriptors.put(desc.getName(), desc);
				}
			}
		}
	}

	public Descriptor findDescriptor(String name) {
		Object result= descriptors.get(name);
		if (result != null)
			return (Descriptor)result;
		return null;
	}
}
