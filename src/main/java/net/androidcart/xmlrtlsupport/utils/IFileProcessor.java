package net.androidcart.xmlrtlsupport.utils;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Amin Amini on 12/29/17.
 */
public interface IFileProcessor {
    boolean process(final String path) throws Exception;
    boolean shouldProcessFile(final String path);
}