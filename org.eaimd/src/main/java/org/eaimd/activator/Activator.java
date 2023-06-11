/* EAIMD - eaimd.org - 2023 */
package org.eaimd.activator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eaimd.ai_md.utils.CU;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import lombok.SneakyThrows;

public class Activator extends AbstractUIPlugin {

	/* The plug-in ID */
	public static final String PLUGIN_ID = "org.eaimd"; //$NON-NLS-1$

	/* The shared instance */
	private static Activator plugin;
	
	private static String pluginFolderPathName;
	
	@SneakyThrows({IOException.class, URISyntaxException.class})
	public Activator() {
		
		/* Plug-in root folder */
		URL pluginRootURL = FileLocator.find(Platform.getBundle(PLUGIN_ID), new Path("/"), null);
		pluginFolderPathName = (new File(FileLocator.resolve(pluginRootURL).toURI())).getCanonicalFile().getCanonicalPath();
		
		/* Log */
		System.setProperty("eaimd_plugin_folder", pluginFolderPathName);
		System.setProperty("log4j.configurationFile", "file:/" + pluginFolderPathName + CU.S + "config" + CU.S + "log4j2.yaml");
		
		App.initialize();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String getPluginFolder() {
		return pluginFolderPathName;
	}
}
