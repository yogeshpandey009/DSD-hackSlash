package com.asu.score.hackslash.helper;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import com.asu.score.hackslash.properties.Constants;

public class ImageProviderHelper {
	
	/**
	 * Returns the image descriptor with the given relative path.
	 */
	public static ImageDescriptor getImageDescriptor(String relativePath) {
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
        IPath path = new Path("icons/" + relativePath);
        URL url = FileLocator.find(bundle, path, null);
        return ImageDescriptor.createFromURL(url);
	}
	
	/**
	 * Returns the url of the image in the file system.
	 * 
	 * @param relativePath
	 * @return
	 */
	public static Image getImage(String relativePath){
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
        IPath path = new Path("images/" + relativePath);
        URL url = FileLocator.find(bundle, path, null);
        ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
        return descriptor.createImage();
	}
}
