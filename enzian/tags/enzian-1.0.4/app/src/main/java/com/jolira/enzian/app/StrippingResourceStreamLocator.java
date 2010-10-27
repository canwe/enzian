package com.jolira.enzian.app;

import java.util.Locale;

import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.IResourceStreamLocator;

final class StrippingResourceStreamLocator implements IResourceStreamLocator {
    private final IResourceStreamLocator resourceStreamLocator;

    StrippingResourceStreamLocator(final IResourceStreamLocator resourceStreamLocator) {
        this.resourceStreamLocator = resourceStreamLocator;
    }

    @Override
    public IResourceStream locate(final Class<?> clazz, final String path) {
        final IResourceStream located = resourceStreamLocator.locate(clazz, path);

        if (located != null) {
            return located;
        }

        final String stripped = stripPackageName(clazz, path);

        if (stripped == null) {
            return null;
        }

        return resourceStreamLocator.locate(clazz, stripped);
    }

    @Override
    public IResourceStream locate(final Class<?> clazz, final String path, final String style, final Locale locale,
            final String extension) {
        final IResourceStream located = resourceStreamLocator.locate(clazz, path, style, locale, extension);

        if (located != null) {
            return located;
        }

        final String stripped = stripPackageName(clazz, path);

        if (stripped == null) {
            return null;
        }

        return resourceStreamLocator.locate(clazz, stripped, style, locale, extension);
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
