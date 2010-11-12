package com.jolira.enzian.app;

import java.util.Locale;

import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;

final class EnzianResourceStreamLoacator extends ResourceStreamLocator {
    @Override
    public IResourceStream locate(final Class<?> clazz, final String path) {
        final IResourceStream located = super.locate(clazz, path);

        if (located != null) {
            return located;
        }

        final String stripped = stripPackageName(clazz, path);

        if (stripped == null) {
            return null;
        }

        return super.locate(clazz, stripped);
    }

    @Override
    public IResourceStream locate(final Class<?> clazz, final String path, final String style, final Locale locale,
            final String extension) {
        final IResourceStream located = super.locate(clazz, path, style, locale, extension);

        if (located != null) {
            return located;
        }

        final String stripped = stripPackageName(clazz, path);

        if (stripped == null) {
            return null;
        }

        return super.locate(clazz, stripped, style, locale, extension);
    }

    private String stripPackageName(final Class<?> clazz, final String path) {
        final Package pkg = clazz.getPackage();
        final String pkgName = pkg.getName();
        final String pkgPath = pkgName.replace('.', '/') + '/';

        if (!path.startsWith(pkgPath)) {
            return null;
        }

        final int pkgPathLen = pkgPath.length();

        return path.substring(pkgPathLen);
    }
}
